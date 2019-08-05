package io.github.seckillPro.redis.key

import io.github.seckillPro.redis.BasePrefix

/**
 * 定义Redis 订单键 前缀
 *
 * @author 梦境迷离
 * @time 2019年8月5日
 * @version v2.0
 */
class OrderKey private(var prefix: String) extends BasePrefix(prefix)

object OrderKey {

  //获取秒杀生成的订单，从中取值，检查用户是否多次秒杀，
  final val getSeckillOrderByUidGid = new OrderKey("moug")
}