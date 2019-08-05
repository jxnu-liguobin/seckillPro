package io.github.seckillPro.redis.key

import io.github.seckillPro.redis.BasePrefix

/**
 * 定义Redis 商品键 前缀
 *
 * @author 梦境迷离
 * @time 2019年8月5日
 * @version v2.0
 */
class GoodsKey private(expireSe: Integer, var prefix: String) extends BasePrefix(expireSe, prefix)

object GoodsKey {

  //获取秒杀商品列表
  final val getGoodsList = new GoodsKey(60, "gl")

  //获取商品详情
  final val getGoodsDetail = new GoodsKey(60, "gd")

  //获取秒杀商品库存
  final val getSeckillGoodsStock = new GoodsKey(0, "gs")
}