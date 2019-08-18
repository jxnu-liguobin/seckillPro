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
    ConfigLoader.getStringValue("redis.host").getOrElse("127.0.0.1"),
    ConfigLoader.getIntValue("redis.port").getOrElse(6379),
    ConfigLoader.getIntValue("redis.timeout").getOrElse(10),
    ConfigLoader.getStringValue("redis.password").getOrElse(""),
    ConfigLoader.getIntValue("redis.poolMaxTotal").getOrElse(1000),
    ConfigLoader.getIntValue("redis.poolMaxIdle").getOrElse(500),
    ConfigLoader.getIntValue("redis.poolMaxWait").getOrElse(500)
  )

}
