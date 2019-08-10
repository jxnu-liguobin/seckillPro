package io.github.dao.test

import java.time.LocalDateTime

import io.github.BaseTest
import io.github.seckillPro.dao.GoodsDao
import io.github.seckillPro.entity.SeckillGoods

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

  val listGoodsVo = GoodsDao.listGoodsVo().apply()
  listGoodsVo.foreach(x => println(x))

  val goodsVo = GoodsDao.getGoodsVoByGoodsId(1).apply()
  goodsVo match {
    case Some(goodsVo) => println(goodsVo)
    case None => println("Not found")
  }

  val reduceCount = GoodsDao.reduceStock(seckillGoods).apply()
  println(reduceCount)

  val resetStockCount = GoodsDao.resetStock(seckillGoods).apply()
  println(resetStockCount)

}
