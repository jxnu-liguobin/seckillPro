package io.github.dreamy.seckill.redis.key

import io.github.dreamy.seckill.redis.BasePrefix

/**
 * 定义Redis 访问键 前缀
 *
 * @author 梦境迷离
 * @since 2019年8月5日
 * @version v2.0
 */
class AccessKey private(expireSe: Int, var prefix: String) extends BasePrefix(expireSe, prefix)

object AccessKey {

  def withExpire(expireSeconds: Integer): AccessKey = {
    new AccessKey(expireSeconds, "access")
  }
}