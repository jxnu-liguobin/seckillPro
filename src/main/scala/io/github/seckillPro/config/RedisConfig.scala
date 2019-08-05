package io.github.seckillPro.config

/**
 * Redis配置属性
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
case class RedisConfig(host: String, port: Int, timeout: Int, password: String, poolMaxTotal: Int, poolMaxIdle: Int, poolMaxWait: Int)

object RedisConfig {

  def getRedisConfig = RedisConfig(
    ConfigLoader.getStringValue("redis.host"),
    ConfigLoader.getIntValue("redis.port"),
    ConfigLoader.getIntValue("redis.timeout"),
    ConfigLoader.getStringValue("redis.password"),
    ConfigLoader.getIntValue("redis.poolMaxTotal"),
    ConfigLoader.getIntValue("redis.poolMaxIdle"),
    ConfigLoader.getIntValue("redis.poolMaxWait")
  )

}
