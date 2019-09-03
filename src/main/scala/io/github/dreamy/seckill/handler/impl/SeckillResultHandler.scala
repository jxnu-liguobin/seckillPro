package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.service.{ SeckillService, SeckillUserService }
import io.github.dreamy.seckill.util.VerifyEmpty
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 *
 * @author 梦境迷离
 * @since 2019-09-03
 * @version v1.0
 */
class SeckillResultHandler extends DefaultRestfulHandler {

  override def route: String = "/seckill/result"

  override def methods: Set[String] = multi(Methods.GET_STRING, Methods.PUT_STRING)

  override def get(exchange: HttpServerExchange): Future[Any] = {
    val token = getCookieValueByName(exchange, SeckillUserService.COOKI_NAME_TOKEN)
    val user = isLogin(exchange, token)
    if (VerifyEmpty.noEmpty(user)) {
      val goodsId = getQueryParamValue(exchange, "goodsId").getOrElse("-1").toLong
      SeckillService.getSeckillResult(user.id.get, goodsId).map { result =>
        resultSuccess(result.toString)
      }.elapsed("查询秒杀结果")
    } else {
      tokenNotFound
    }
  }
}
