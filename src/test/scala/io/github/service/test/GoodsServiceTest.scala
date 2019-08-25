package io.github.service.test

import io.github.BaseTest
import io.github.dreamy.seckill.service.GoodsService
import play.api.libs.json.Json

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
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

  val goodsVos = GoodsService.listGoodsVo
  Await.result(goodsVos.map(x => println("json:" + Json.toJson(x))), Duration.Inf)

  val c = GoodsService.reduceStock(goodsVo.get.copy(stockCount = 9))
  println(c)

  //  val sum = Await.result(GoodsService.resetStock(goodsVos), Duration.Inf)
  //  println(sum)

}
