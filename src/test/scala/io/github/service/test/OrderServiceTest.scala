package io.github.service.test

import io.github.BaseTest
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.OrderKey
import io.github.dreamy.seckill.service.{ GoodsService, OrderService }
import io.github.dreamy.seckill.util.MD5Utils

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
object OrderServiceTest extends BaseTest with App {

  RedisService.set(OrderKey.getSeckillOrderByUidGid, "" + 15312345678L + "_" + 1, mockSeckillOrder)

  val seckillOrder = Await.result(OrderService.getSeckillOrderByUserIdGoodsId(15312345678L, 1), Duration.Inf)
  println("order => " + seckillOrder)

  val goodsVo = Await.result(GoodsService.getGoodsVoByGoodsId(1), Duration.Inf)
  println("goodsVo => " + goodsVo)
  val createOrder = OrderService.createOrder(SeckillUser(Option(15312345678L), "user",
    MD5Utils.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1), goodsVo.get)
  println(createOrder)

  val order = Await.result(OrderService.getOrderById(1), Duration.Inf)
  println(order)

}
