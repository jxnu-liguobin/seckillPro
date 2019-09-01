package io.github.dreamy.seckill.util

import java.util.regex.Pattern

import org.apache.commons.lang3.StringUtils

/**
 * 验证手机号码的工具
 *
 * 注解使用的是Java,由于Java调用半生对象失败，未知原因，改用普通类
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
object VerifyRegex {

  private final val mobile_pattern: Pattern =
    Pattern.compile("^((17[0-9])|(14[0-9])|(13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$")

  def isMobile(src: String): Boolean = {
    if (StringUtils.isEmpty(src)) {
      false
    } else mobile_pattern.matcher(src).matches()
  }
}