package io.github.dreamy.seckill.redis.key

import io.github.dreamy.seckill.redis.BasePrefix

/**
 * 定义Redis 用户键 前缀
 *
 * @author 梦境迷离
 * @time 2019年8月5日
 * @version v2.0
 */
class UserKey private(var prefix: String) extends BasePrefix(prefix)

object UserKey {

  final val getById = new UserKey("id")

  final val getByName = new UserKey("name")
}