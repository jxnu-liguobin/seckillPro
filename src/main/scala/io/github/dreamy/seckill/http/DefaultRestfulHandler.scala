package io.github.dreamy.seckill.http

import io.github.dreamy.seckill.config.Constant
import io.github.dreamy.seckill.handler.impl.DefaultExceptionHandler
import io.github.dreamy.seckill.handler.{ ExceptionHandler, RestfulHandler }
import play.api.libs.json.{ JsValue, Json }

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
}
