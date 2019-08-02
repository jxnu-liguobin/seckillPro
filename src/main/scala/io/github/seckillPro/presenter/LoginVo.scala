package io.github.seckillPro.presenter

import io.github.seckillPro.util.{ValidatorUtilScala, VerifyEmpty}

/**
 * 登陆视图对象
 *
 * @author 梦境迷离
 * @time 2019年8月2日
 * @version v2.0
 */
case class LoginVo(mobile: String, password: String) {
  require(ValidatorUtilScala.isMobile(mobile), "手机号格式错误")
  require(VerifyEmpty.empty(password))
}
