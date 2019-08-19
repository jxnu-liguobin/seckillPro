package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.handler.ExceptionHandler
import io.undertow.server.HttpServerExchange

/**
 * 默认的异常处理器
 *
 * @author 梦境迷离
 * @time 2019-08-19
 * @version v1.0
 */
class DefaultExceptionHandler extends ExceptionHandler {
  override def handleException(exchange: HttpServerExchange, cause: Throwable): Unit = {
    logger.error(s"found exception cause: ${cause.getMessage}")
  }
}

object DefaultExceptionHandler {
  def apply(): DefaultExceptionHandler = new DefaultExceptionHandler()
}