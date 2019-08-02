package io.github.seckillPro.util

/**
 * 空值验证工具类
 *
 * @author 梦境迷离
 * @time 2019-08-02
 * @version v2.0
 */
object VerifyEmpty {

  def empty(string: String) = if (string == null || string.equals("")) {
    true
  } else false


  def empty(string: Option[String]) = if (string.isEmpty || string.getOrElse("").equals("")) {
    true
  } else false

}
