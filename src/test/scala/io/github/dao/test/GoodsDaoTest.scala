package io.github.dao.test

import java.time.LocalDateTime

import io.github.BaseTest
import io.github.seckillPro.dao.GoodsDao
import io.github.seckillPro.entity.SeckillGoods

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * 商品测试
 *
 * @author 梦境迷离
 * @time 2019-08-02
 * @version v2.0
 */
object GoodsDaoTest extends BaseTest with App {

  val id = 1
  val goodsId = Some(1L)
  val stockCount = 2
  val price = 2
  val seckillGoods = SeckillGoods(None, goodsId, stockCount, price, Option(LocalDateTime.now()))

  val listGoodsVo = Await.result(GoodsDao.listGoodsVo(), Duration.Inf)
  listGoodsVo.foreach(x => println(x))

  val goodsVo = Await.result(GoodsDao.getGoodsVoByGoodsId(1), Duration.Inf)
  goodsVo match {
    case Some(goodsVo) => println(goodsVo)
    case None => println("Not found")
  }

  val reduceCount = Await.result(GoodsDao.reduceStock(seckillGoods), Duration.Inf)
  println(reduceCount)

  val resetStockCount = Await.result(GoodsDao.resetStock(seckillGoods), Duration.Inf)
  println(resetStockCount)

}
