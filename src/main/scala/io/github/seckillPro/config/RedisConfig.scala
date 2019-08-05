package io.github.seckillPro.config

/**
 * Redis配置属性
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
private case class RedisConfig(host: String, port: Int, timeout: Int, password: String, poolMaxTotal: Int, poolMaxIdle: Int, poolMaxWait: Int)

object RedisConfig {

  def getRedisConfig = RedisConfig(
    ConfigLoader.getStringValue("host"),
    ConfigLoader.getIntValue("port"),
    ConfigLoader.getIntValue("timeout"),
    ConfigLoader.getStringValue("password"),
    ConfigLoader.getIntValue("poolMaxTotal"),
    ConfigLoader.getIntValue("poolMaxIdle"),
    ConfigLoader.getIntValue("poolMaxWait"),
  )

}
