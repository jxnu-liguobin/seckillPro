package io.github.dreamy.seckill.redis.key

import io.github.dreamy.seckill.redis.BasePrefix

/**
 * 基于setnx的redis分布式锁
 *
 * @author 梦境迷离
 * @since 2019-09-14
 * @version v1.0
 */
class LockKey private(var prefix: String) extends BasePrefix(prefix)

object LockKey {

  //分布式锁过期时间是由用户设置的
  final val getSingleRedisLock = new LockKey("lock")

}
