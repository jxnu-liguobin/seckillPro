package io.github.seckillPro.util

import java.util.Date

/**
 *
 * @author 梦境迷离
 * @time 2019-08-02
 * @version v2.0
 */
trait ImplicitUtils extends DateUtils {

  implicit def longToLocalDateTime(long: Long) = dateToLocalDateTime(new Date(long))

}
