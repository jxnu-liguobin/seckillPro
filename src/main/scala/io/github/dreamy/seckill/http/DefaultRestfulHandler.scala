package io.github.dreamy.seckill.http

import io.github.dreamy.seckill.config.Constant
import io.github.dreamy.seckill.handler.impl.DefaultExceptionHandler
import io.github.dreamy.seckill.handler.{ ExceptionHandler, RestfulHandler }

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
   * @param result 传过来的是有效的json对象
   * @return
   */
  override def writeAsBytes (result: Any): Array[Byte] = {
    logger.info(result.toString)
    result.toString.getBytes(Constant.default_chartset)
  }
}
