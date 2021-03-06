package io.github.dreamy.seckill.util

import java.time.LocalDateTime

import io.github.dreamy.seckill.util.DateUtils._

/**
 * 隐式对象工具
 *
 * @author 梦境迷离
 * @since 2019-08-02
 * @version v2.0
 */
@deprecated
object ImplicitUtils {

  //数据库插入时需要手动使用该转换
  implicit def toLong (localDateTime: Option[LocalDateTime]) = localDateTimeToLong(localDateTime)

  implicit def toLocalDateTime (long: Option[Long]) = longToLocalDateTime(long)

  implicit def toStr (localDateTime: Option[LocalDateTime]) = localDateTimeToString(localDateTime)

  implicit def toStrNoMS (localDateTime: Option[LocalDateTime]) = localDateTimeToStringNoMS(localDateTime)
}