package io.github.dreamy.seckill.handler

import com.typesafe.scalalogging.LazyLogging
import io.undertow.server.HttpServerExchange

/**
 * undertow 异常处理器
 *
 * @author 梦境迷离
 * @since 2019-08-18
 * @version v1.0
 */
trait ExceptionHandler extends LazyLogging {

  def handleException(exchange: HttpServerExchange, cause: Throwable): Unit

}
