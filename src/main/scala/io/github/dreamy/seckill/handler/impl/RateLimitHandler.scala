package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.limit.RateLimit
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * 测试限流
 *
 * @author 梦境迷离
 * @since 2019-09-09
 * @version v1.0
 */
class RateLimitHandler extends DefaultRestfulHandler {
  override def route: String = "/test/limit"

  override def methods: Set[String] = single(Methods.GET_STRING)

  //需要限流的接口需要重写该属性
  override def rateLimit: RateLimit = RateLimit(2, 2)

  override def get(exchange: HttpServerExchange): Future[Any] = {
    Future {
      Json.obj("hello" -> "world")
    }.elapsed("限流测试")
  }

}
