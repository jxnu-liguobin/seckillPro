package io.github.dreamy.seckill.config

import io.github.dreamy.seckill.limit.RateLimit

import scala.concurrent.duration._

/**
 * 系统常量
 *
 * @author 梦境迷离
 * @since 2019-08-19
 * @version v1.0
 */
object Constant {

  final val default_chartset = "utf-8"

  final val SECKILL_HANDLE = 6 seconds

  //同一个用户，在5秒内最多请求秒杀系统5次，原Java或Scala V1.0版为5，5
  //秒杀系统有登录，查询，秒杀等等接口，这个数应该比可能用到的接口的最大值要大一点，保证用户访问过所有接口后才被限流
  final lazy val default_ratelimit = RateLimit(maxCount = 10, seconds = 5, needLogin = true)

  final val cookie_name_token = "token"

}
