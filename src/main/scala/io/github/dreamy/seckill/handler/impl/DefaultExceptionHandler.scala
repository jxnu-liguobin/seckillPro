package io.github.dreamy.seckill.handler.impl

import java.net.BindException
import java.nio.ByteBuffer

import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.handler.ExceptionHandler
import io.github.dreamy.seckill.presenter.{ CodeMsg, Result }
import io.github.dreamy.seckill.serializer.GsonSerializerAdapter
import io.undertow.server.HttpServerExchange
import io.undertow.util.Headers

/**
 * 默认的异常处理器
 *
 * @author 梦境迷离
 * @time 2019-08-19
 * @version v1.0
 */
class DefaultExceptionHandler extends ExceptionHandler {

  private[this] lazy val contentType = "application/json;charset=utf-8"

  override def handleException(exchange: HttpServerExchange, cause: Throwable): Unit = {
    logger.error(s"found exception cause: ${cause.getMessage}")
    cause match {
      case ge: GlobalException =>
        exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, contentType)
        exchange.setStatusCode(ge.getCm().getCode)
        val jsonStr = GsonSerializerAdapter.getGson.toJson(Result.error(ge.getCm()))
        exchange.getResponseSender.send(ByteBuffer.wrap(jsonStr.getBytes()))
      case be: BindException =>
        exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, contentType)
        exchange.setStatusCode(CodeMsg.BIND_ERROR.getCode)
        val jsonStr = GsonSerializerAdapter.getGson.toJson(Result.error(CodeMsg.BIND_ERROR.fillArgs(be.getMessage)))
        exchange.getResponseSender.send(ByteBuffer.wrap(jsonStr.getBytes()))
      case _ =>
        exchange.getResponseHeaders.put(Headers.CONTENT_TYPE, contentType)
        exchange.setStatusCode(CodeMsg.INTERNAL_ERROR.getCode)
        val jsonStr = GsonSerializerAdapter.getGson.toJson(Result.error(CodeMsg.SERVER_ERROR))
        exchange.getResponseSender.send(ByteBuffer.wrap(jsonStr.getBytes()))
    }
  }
}

object DefaultExceptionHandler {
  def apply(): DefaultExceptionHandler = new DefaultExceptionHandler()
}