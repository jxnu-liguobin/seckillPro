package io.github.dreamy.seckill.util

/**
 * 空值验证工具类
 *
 * @author 梦境迷离
 * @time 2019-08-02
 * @version v2.0
 */
object VerifyEmpty {

  def empty(string: String) = if (string == null || string.equals("")) true else false

  def empty(string: Option[String]) = if (string.isEmpty || string.getOrElse("").equals("")) true else false

  def empty(obj: AnyRef) = if (obj == null) true else false

  def noEmpty(obj: AnyRef) = if (obj != null) true else false

  //所有条件都为空才为true &&
  def allEmpty(obj: AnyRef*) = if (obj.exists(_ != null)) false else true

  //有条件为空即为true  ||
  def oneEmpty(obj: AnyRef*) = if (obj.contains(null)) true else false


}
