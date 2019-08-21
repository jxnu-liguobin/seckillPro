package io.github.dreamy.seckill.presenter

import scala.util.Try

/**
 * 状态码信息
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class CodeMsg private(code: Int, msg: String) {

  /**
   * 参数格式化
   */
  def fillArgs(args: AnyRef*): CodeMsg = {
    val code = this.code
    val message = String.format(this.msg, args)
    new CodeMsg(code, message)
  }

  //其他未定义异常默认500
  def getCode = Try(this.code).getOrElse(500)
}

/**
 * 状态码伴生对象，定义常量
 */
object CodeMsg {

  // 通用的错误码
  /**
   * success
   */
  val SUCCESS = CodeMsg(0, "success")

  /**
   * 服务端异常
   */
  val SERVER_ERROR = CodeMsg(510, "服务端异常")

  /**
   * 参数校验异常
   */
  val BIND_ERROR = CodeMsg(511, "参数校验异常：%s")

  /**
   * 请求非法
   */
  val REQUEST_ILLEGAL = CodeMsg(512, "请求非法")

  /**
   * 访问太频繁！
   */
  val ACCESS_LIMIT_REACHED = CodeMsg(514, "访问太频繁！")

  // 登录模块 52X
  /**
   * Session不存在或者已经失效
   */
  val SESSION_ERROR = CodeMsg(520, "Session不存在或者已经失效")

  /**
   * 登录密码不能为空
   */
  val PASSWORD_EMPTY = CodeMsg(521, "登录密码不能为空")

  /**
   * 手机号不能为空
   */
  val MOBILE_EMPTY = CodeMsg(522, "手机号不能为空")

  /**
   * 手机号格式错误
   */
  val MOBILE_ERROR = CodeMsg(523, "手机号格式错误")

  /**
   * 手机号不存在
   */
  val MOBILE_NOT_EXIST = CodeMsg(524, "手机号不存在")

  /**
   * 密码错误
   */
  val PASSWORD_ERROR = CodeMsg(525, "密码错误")

  // 商品模块 53X

  // 订单模块 54X
  /**
   * 订单不存在
   */
  val ORDER_NOT_EXIST = CodeMsg(540, "订单不存在")

  // 秒杀模块 5005XX
  /**
   * 商品已经秒杀完毕
   */
  val SECKILL_OVER = CodeMsg(550, "商品已经秒杀完毕")

  /**
   * 不能重复秒杀
   */
  val REPEATE_SECKILL = CodeMsg(551, "不能重复秒杀")

  /**
   * 秒杀失败
   */
  val SECKILL_FAIL = CodeMsg(552, "秒杀失败")


  /**
   * 其他
   */
  val INTERNAL_ERROR = CodeMsg(500, "服务器内部错误")

}