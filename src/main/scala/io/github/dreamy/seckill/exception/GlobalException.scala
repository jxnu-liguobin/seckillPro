package io.github.dreamy.seckill.exception

import io.github.dreamy.seckill.presenter.CodeMsg

/**
 * 全局定义
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */

@SerialVersionUID(1L)
case class GlobalException(cm: CodeMsg) extends RuntimeException {

  def getCm() = cm

  /** 这里直接打印出传入的异常信息. */
  override def toString() = cm.toString

} 