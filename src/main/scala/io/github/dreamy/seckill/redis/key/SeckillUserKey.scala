package io.github.dreamy.seckill.redis.key

import io.github.dreamy.seckill.redis.BasePrefix

/**
 * 定义Redis秒杀用户键 前缀
 *
 * @author 梦境迷离
 * @time 2019年8月5日
 * @version v2.0
 */
class SeckillUserKey private(expireSe: Integer, var prefix: String) extends BasePrefix(expireSe, prefix)

object SeckillUserKey {

  //用户登陆token过期时间
  final val TOKEN_EXPIRE = 3600 * 24 * 7

  //用户登陆token前缀
  final val token = new SeckillUserKey(TOKEN_EXPIRE, "tk")

  // 对象不发生变化，则永久有效
  final val getById = new SeckillUserKey(0, "id")
}