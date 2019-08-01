package io.github.seckillPro.presenter

/**
 * 返回结果
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class Result[T] private(code: Int = 0, msg: String = "success", data: Option[T])

object Result {

  def success[T](data: T) = new Result[T](data = Option(data))

  def error[T](cm: CodeMsg): Result[T] = new Result[T](code = cm.code, msg = cm.msg, data = None)

}