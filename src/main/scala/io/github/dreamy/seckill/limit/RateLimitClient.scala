package io.github.dreamy.seckill.limit

import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.presenter.CodeMsg
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.AccessKey
import io.github.dreamy.seckill.util.HandlerUtils
import io.undertow.server.HttpServerExchange

/**
 * redis 限流
 *
 * @author 梦境迷离
 * @since 2019-09-09
 * @version v1.0
 */
object RateLimitClient extends LazyLogging {

  /**
   * 限流
   *
   * 目前没想好以什么方式使用这个
   *
   * @param rateLimit
   * @param exchange
   * @return 是否通过限流控制
   */
  def seckillLimit(rateLimit: RateLimit, exchange: HttpServerExchange) = {
    //需要登录
    if (rateLimit.needLogin) {
      val user = HandlerUtils.isLogin(exchange, rateLimit.token)
      if (user == null) false else {
        //TODO 是否应当并发控制？
        val key = exchange.getRequestURI + "_" + user.id
        val ak = AccessKey.withExpire(rateLimit.seconds)
        val count = RedisService.get(ak, key, classOf[Integer]).toInt
        if (count == null) {
          logger.info(s"new user id is: ${user.id}")
          RedisService.set(ak, key, 1)
        } else if (count < rateLimit.maxCount) {
          logger.info(s"old user id is: ${user.id}")
          RedisService.incr(ak, key)
          true
        } else throw GlobalException(CodeMsg.ACCESS_LIMIT_REACHED)
      }
    } else true
  }
}
