package io.github.seckillPro.config

import com.typesafe.config.ConfigFactory

/**
 * 配置文件
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
object ConfigLoader {

  def defaultConfig = ConfigFactory.load("application.conf")

  def getStringValue(key: String) = defaultConfig.getString(key)

  def getIntValue(key: String) = defaultConfig.getInt(key)

}
