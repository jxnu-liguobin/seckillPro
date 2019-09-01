package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.config.Constant
import io.github.dreamy.seckill.handler.{ ExceptionHandler, RestfulHandler }
import io.github.dreamy.seckill.http.RoutingHandler
import io.github.dreamy.seckill.serializer.GsonSerializerAdapter
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 测试handler
 *
 * @author 梦境迷离
 * @since 2019-08-19
 * @version v1.0
 */
class HelloWorldHandler extends RestfulHandler with RoutingHandler {

  override protected val exceptionHandler: ExceptionHandler = DefaultExceptionHandler()

  override def writeAsBytes (result: Any): Array[Byte] = {
    //redis通用泛型方法无法使用play-json，后续所有json直接使用自定义的gson序列化方式
    GsonSerializerAdapter.getGson.toJson(result).getBytes(Constant.default_chartset)
  }

  override def route: String = "/hello"

  override def methods: Set[String] = single(Methods.GET_STRING)

  override def get (exchange: HttpServerExchange): Future[Any] = {
    Future {
      Json.obj("hello" -> "world")
    }.elapsed("测试hello")
  }
}
