package io.github.dreamy.seckill.config

import com.typesafe.config.ConfigFactory

/**
 * 配置文件
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
object ConfigLoader {


  private final lazy val config = ConfigFactory.load("application.conf")
  private final lazy val configWrapper = ConfigConversions.ConfigWrapper(config)

  def getStringValue(key: String) = configWrapper.getStringOpt(key)

  def getIntValue(key: String) = configWrapper.getIntOpt(key)

  def getConfig = config
}
