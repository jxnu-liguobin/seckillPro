package io.github.dreamy.seckill.presenter

import io.github.dreamy.seckill.util.{ VerifyEmpty, VerifyRegex }

/**
 * 登陆视图对象
 *
 * @author 梦境迷离
 * @since 2019年8月2日
 * @version v2.0
 */
case class LoginVo(mobile: String, password: String) {
  require(VerifyRegex.isMobile(mobile), "手机号格式错误")
  require(VerifyEmpty.noEmpty(password), "密码不能为空")
}
