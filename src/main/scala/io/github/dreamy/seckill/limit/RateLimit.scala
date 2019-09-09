package io.github.dreamy.seckill.limit

/**
 * 需要限流的需要构造该实例
 *
 * @author 梦境迷离
 * @since 2019-09-09
 * @version v1.0
 * @param seconds   时间
 * @param maxCount  最多次数
 * @param token     cookie的token
 * @param needLogin 是否需要登录
 */
case class RateLimit(seconds: Int, maxCount: Int, token: Option[String] = None, needLogin: Boolean = true)
