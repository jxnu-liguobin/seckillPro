package io.github.dreamy.seckill.redis

/**
 * 定义Redis 键 和 过期时间
 *
 * @author 梦境迷离
 * @since 2019年8月5日
 * @version v2.0
 */
abstract class BasePrefix extends KeyPrefix {

  /** 默认0代表永不过期 */
  private var expireSe: Int = 0

  var prefix: String

  /** 带参辅助构造 */
  def this(prefix: String) {
    this()
    this.prefix = prefix
  }

  /** 带参辅助构造 */
  def this(expireSeconds: Int, prefix: String) {
    this()
    this.expireSe = expireSeconds
    this.prefix = prefix

  }

  override def getPrefix() = {
    val className = getClass().getSimpleName()
    className + ":" + prefix
  }

  override def expireSeconds() = expireSe
}