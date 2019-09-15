package io.github.dreamy.seckill.redis

import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.redis.key.LockKey

/**
 * redis 分布式锁，（setnx单机版）
 * 可能导致死锁
 *
 * @author 梦境迷离
 * @since 2019-09-14
 * @version v1.0
 */
object RedisLock extends LazyLogging {

  /**
   * 待测试
   *
   * @param key     需要被加锁的key
   * @param current 当前时间戳 毫秒
   * @param expire  过期时间 毫秒
   * @return 是否加锁成功
   */
  def lock(key: String, current: Long, expire: Long) = {
    //如果键不存在则新增,存在则不改变已经有的值
    //如果设置成功，直接反回true，ttl -1表示key存在但已经过期,-2表示key不存在了
    if (RedisService.setIfAbsent(LockKey.getSingleRedisLock, key, current, expire)) true
    else {
      //redis键值已经存在的情况
      //乐观锁，类似CAS操作，这样使得没有获取锁的用户都不能成功操作，高并发下，可能由很多用户出现错误
      //不加这步会因为业务抛出异常造成死锁
      val currentValue = RedisService.get(LockKey.getSingleRedisLock, key, classOf[String]).toLong // 获取当前值
      //判断当前新值是否小于当前系统的时间戳.
      if (currentValue < System.currentTimeMillis) {
        //获取上一个锁【旧值】的时间，getAndSet类似i++ ,先获取，再设置.
        val oldValue = RedisService.getAndSet(LockKey.getSingleRedisLock, key, current, expire).toLong //返回一个字符串改为返回Long，也就是键的旧值。
        //如果键不存在，则返回null。如果键存在，得到的oldValue不是空，使用旧值与currentValue比较，不相同，则说明已经被其他线程修改过.
        if (oldValue == currentValue) {
          logger.info(s"lock successfully")
          true
        }
        else false
      } else false //已经超时，直接返回false,加锁失败.
    }
  }

  /**
   * 解锁
   *
   * @param key    需要被加锁的key
   * @param expire 过期时间 毫秒
   * @return 无
   */
  def unlock(key: String, current: Long, expire: Long) = {
    val currentValue = RedisService.get(LockKey.getSingleRedisLock, key, classOf[String]).toLong
    if (currentValue == (current + expire)) {
      logger.info(s"unlock successfully")
      RedisService.delete(LockKey.getSingleRedisLock, key)
    } else {
      logger.info(s"unlock failed , maybe have been updated value by other or have expired")
    }
  }
}
