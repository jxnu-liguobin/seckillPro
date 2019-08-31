package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.{ DefaultRestfulHandler, SessionBuilder }
import io.github.dreamy.seckill.presenter.{ CodeMsg, OrderDetailPresenter }
import io.github.dreamy.seckill.service.{ GoodsService, OrderService, SeckillUserService }
import io.github.dreamy.seckill.util.ConditionUtils
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
 *
 * @author 梦境迷离
 * @since 2019-08-31
 * @version v1.0
 */
class OrderHandler extends DefaultRestfulHandler {

  override def route: String = "/order/detail/{orderId}"

  override def methods: Set[String] = single(Methods.GET_STRING)

  override def get(exchange: HttpServerExchange): Future[Any] = {
    SessionBuilder.getSession(exchange) match {
      case Some(session) =>
        val token = Try(exchange.getRequestCookies.get(SeckillUserService.COOKI_NAME_TOKEN).getValue).getOrElse("")
        val sessionUser = session.getAttribute(token).asInstanceOf[SeckillUser]
        val user = if (sessionUser != null) {
          session.setAttribute(token, sessionUser)
          sessionUser
        } else {
          SeckillUserService.getByToken(exchange, token) match {
            case Some(u) => session.setAttribute(token, u)
              u
            case None =>
              session.invalidate(exchange)
              throw GlobalException(CodeMsg.TOKEN_ERROR) //无效token
          }
        }
        logger.info(s"current user: ${user}")
        val orderId = getQueryParamValue(exchange, "orderId").getOrElse("-1").toLong
        for {
          orderInfoOpt <- OrderService.getOrderById(orderId)
          _ <- ConditionUtils.failCondition(orderInfoOpt.isEmpty, GlobalException(CodeMsg.ORDER_NOT_EXIST))
          goodsVoOpt <- GoodsService.getGoodsVoByGoodsId(orderInfoOpt.get.goodsId.getOrElse(-1L))
          _ <- ConditionUtils.failCondition(goodsVoOpt.isEmpty, GlobalException(CodeMsg.ORDER_NOT_EXIST))
        } yield {
          result(Json.toJson(OrderDetailPresenter(goodsVoOpt.get, orderInfoOpt.get)))
        }
      case _ => sessionNotFound
    }
  }
}
