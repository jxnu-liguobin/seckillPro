package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.presenter.{ CodeMsg, OrderDetailPresenter }
import io.github.dreamy.seckill.service.{ GoodsService, OrderService, SeckillUserService }
import io.github.dreamy.seckill.util.HandlerUtils._
import io.github.dreamy.seckill.util.{ ConditionUtils, VerifyEmpty }
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

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
    val token = getCookieValueByName(exchange, SeckillUserService.COOKI_NAME_TOKEN)
    val user = isLogin(exchange, token)
    if (VerifyEmpty.noEmpty(user)) {
      val orderId = getQueryParamValue(exchange, "orderId").getOrElse("-1").toLong
      (for {
        orderInfoOpt <- OrderService.getOrderById(orderId)
        _ <- ConditionUtils.failCondition(orderInfoOpt.isEmpty, GlobalException(CodeMsg.ORDER_NOT_EXIST))
        goodsVoOpt <- GoodsService.getGoodsVoByGoodsId(orderInfoOpt.get.goodsId.getOrElse(-1L))
        _ <- ConditionUtils.failCondition(goodsVoOpt.isEmpty, GlobalException(CodeMsg.ORDER_NOT_EXIST))
      } yield {
        resultSuccess(Json.toJson(OrderDetailPresenter(goodsVoOpt.get, orderInfoOpt.get)))
      }).elapsed("秒杀订单详情查询")
    } else {
      tokenNotFound
    }
  }
}
