package io.github.service.test

import io.github.dreamy.seckill.redis.RedisLock

/**
 *
 * @author 梦境迷离
 * @since 2019-09-14
 * @version v1.0
 */
object RedisLockTest extends App {

  val testKey = "testKey"
  val cu = System.currentTimeMillis()
  Console println RedisLock.lock(testKey, cu, 60000)
  RedisLock.unlock(testKey, cu, 60000)


}
