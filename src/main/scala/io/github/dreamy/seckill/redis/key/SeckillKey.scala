package io.github.dreamy.seckill.redis.key

import io.github.dreamy.seckill.redis.BasePrefix

/**
 * 定义Redis 秒杀键 前缀
 *
 * @author 梦境迷离
 * @since 2019年8月5日
 * @version v2.0
 */
class SeckillKey private(expireSe: Int, var prefix: String) extends BasePrefix(expireSe, prefix)

object SeckillKey {

  //商品是否卖完了
  final val isGoodsOver = new SeckillKey(0, "go")

  //商品秒杀路径
  final val getSeckillPath = new SeckillKey(60, "mp")

  //商品详情的验证码
  final val getSeckillVerifyCode = new SeckillKey(300, "vc")
}