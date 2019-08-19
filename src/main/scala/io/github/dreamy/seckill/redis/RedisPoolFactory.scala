package io.github.dreamy.seckill.redis

import io.github.dreamy.seckill.config.RedisConfig
import redis.clients.jedis.{ JedisPool, JedisPoolConfig }

/**
 * redis连接池
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
object RedisPoolFactory {

  /**
   * 没有密码
   */
  def JedisPoolFactory(): JedisPool = {
    val poolConfig = new JedisPoolConfig()
    poolConfig.setMaxIdle(RedisConfig.getRedisConfig.poolMaxIdle)
    poolConfig.setMaxTotal(RedisConfig.getRedisConfig.poolMaxTotal)
    poolConfig.setMaxWaitMillis(RedisConfig.getRedisConfig.poolMaxWait * 1000)
    new JedisPool(poolConfig, RedisConfig.getRedisConfig.host, RedisConfig.getRedisConfig.port,
      RedisConfig.getRedisConfig.timeout * 1000)

  }
}
