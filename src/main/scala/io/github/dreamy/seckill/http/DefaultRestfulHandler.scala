package io.github.dreamy.seckill.http

import io.github.dreamy.seckill.config.Constant
import io.github.dreamy.seckill.handler.impl.DefaultExceptionHandler
import io.github.dreamy.seckill.handler.{ ExceptionHandler, RestfulHandler }
import io.github.dreamy.seckill.presenter.{ CodeMsg, Result }
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.form.FormDataParser
import play.api.libs.json.{ JsValue, Json }

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try

/**
 * 默认的restful实现
 *
 * @author 梦境迷离
 * @since 2019-08-25
 * @version v1.0
 */
abstract class DefaultRestfulHandler extends RestfulHandler with RoutingHandler {

  /**
   * 使用默认的异常处理器
   */
  override protected val exceptionHandler: ExceptionHandler = DefaultExceptionHandler()

  /**
   *
   * @param result 传过来的是有效的json对象h或gson序列化的转义后的string
   * @return 字节数组
   */
  override def writeAsBytes(result: Any): Array[Byte] = {
    logger.info("result: \n" + Json.prettyPrint(result.asInstanceOf[JsValue]))
    result.toString.getBytes(Constant.default_chartset)
  }

  @deprecated
  def sessionNotFound = Future {
    Json.toJson(Result.error(CodeMsg.SESSION_ERROR))
  }

  def result(data: JsValue) = Json.toJson(Result.success(data))

  def result(data: String) = Json.toJson(Result.success(data))

  /**
   * key-value一一对应的查询参数
   *
   * @param exchange
   */
  def getQueryParams(exchange: HttpServerExchange) = {
    val paramsMaps = exchange.getQueryParameters
    val scalaMaps = new mutable.HashMap[String, String]
    val querys = paramsMaps.keySet()
    querys.forEach(x => scalaMaps.+=(x -> Try(paramsMaps.get(x).getFirst).getOrElse("")))
    scalaMaps.filter(x => x._2 != "")
    scalaMaps.toMap
  }

  def getQueryParamValue(exchange: HttpServerExchange, paramName: String) = getQueryParams(exchange).get(paramName)

  def getFormDataByName(exchange: HttpServerExchange, name: String) = {
    val formData = exchange.getAttachment(FormDataParser.FORM_DATA)
    formData.get(name).getFirst.getValue
  }
}
