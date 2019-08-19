package io.github.dreamy.seckill.redis

/**
 * Redis 键
 *
 * @author 梦境迷离
 * @time 2019年8月5日
 * @version v2.0
 */
trait KeyPrefix {

  /** 过期时间. */
  def expireSeconds(): Int

  /** redis前缀. */
  def getPrefix(): String
}