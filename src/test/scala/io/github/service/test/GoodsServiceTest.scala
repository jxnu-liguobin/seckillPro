package io.github.service.test

import io.github.BaseTest
import io.github.seckillPro.service.GoodsService

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
object GoodsServiceTest extends BaseTest with App {

  val goodsVo = Await.result(GoodsService.getGoodsVoByGoodsId(1), Duration.Inf)
  println(goodsVo)

  val goodsVos = Await.result(GoodsService.listGoodsVo, Duration.Inf)
  goodsVos.foreach(println)

  val c = Await.result(GoodsService.reduceStock(goodsVo.get.copy(stockCount = 9)), Duration.Inf)
  println(c)

  val sum = Await.result(GoodsService.resetStock(goodsVos), Duration.Inf)
  println(sum)

}
