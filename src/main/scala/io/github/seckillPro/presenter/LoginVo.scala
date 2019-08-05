package io.github.seckillPro.presenter

import io.github.seckillPro.util.{VerifyRegex, VerifyEmpty}

/**
 * 登陆视图对象
 *
 * @author 梦境迷离
 * @time 2019年8月2日
 * @version v2.0
 */
case class LoginVo(mobile: String, password: String) {
  require(VerifyRegex.isMobile(mobile), "手机号格式错误")
  require(VerifyEmpty.empty(password))
}
