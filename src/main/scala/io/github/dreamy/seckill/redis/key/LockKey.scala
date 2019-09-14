package io.github.dreamy.seckill.redis.key

import io.github.dreamy.seckill.redis.BasePrefix

/**
 * 基于setnx的redis分布式锁
 *
 * @author 梦境迷离
 * @since 2019-09-14
 * @version v1.0
 */
class LockKey private(expireSe: Int, var prefix: String) extends BasePrefix(expireSe, prefix)

object LockKey {

  //分布式锁过期时间5分钟
  final val getSingleRedisLock = new LockKey(60 * 5, "redisLock")

}
