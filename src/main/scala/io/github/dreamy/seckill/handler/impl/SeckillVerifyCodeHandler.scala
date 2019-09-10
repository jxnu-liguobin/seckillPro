package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.config.Constant
import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.util.HandlerUtils._
import io.github.dreamy.seckill.util.{ VerifyEmpty, VerifyImage }
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods

import scala.concurrent.Future

/**
 * 反回图片toString而不是图片，目前不用
 *
 * @author 梦境迷离
 * @since 2019-09-03
 * @version v1.0
 */
class SeckillVerifyCodeHandler extends DefaultRestfulHandler {

  override def route: String = "/seckill/code"

  override def methods: Set[String] = single(Methods.GET_STRING)

  override def get(exchange: HttpServerExchange): Future[Any] = {
    val token = getCookieValueByName(exchange, Constant.cookie_name_token)
    val user = isLogin(exchange, token)
    if (VerifyEmpty.noEmpty(user)) {
      val goodsId = getQueryParamValue(exchange, "goodsId").getOrElse("-1").toLong
      val image = VerifyImage.createVerifyCode(user, goodsId)
      Future.successful(resultSuccess(image.toString)) // TODO 不方便使用图片
    } else {
      tokenNotFound
    }
  }
}
