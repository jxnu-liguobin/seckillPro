package io.github.service.test

import io.github.BaseTest
import io.github.dreamy.seckill.dao.GoodsDao
import io.github.dreamy.seckill.service.SeckillService
import scalikejdbc.DB

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 *
 * @author 梦境迷离
 * @time 2019-08-06
 * @version v2.0
 */
object SeckillServiceTest extends BaseTest with App {

  //
  //  test_seckill
  //  test_getSeckillResult
  //  test_createSeckillPath_checkPath
  test_reset
  //  test_setGoodsOver
  //  test_getGoodsOver


  def test_seckill = {
    val res = SeckillService.seckill(mockSeckillUser, mockGoodsVo) //默认商品
    println("seckill: " + Await.result(res, Duration.Inf))
  }

  def test_reset = {
    val goodsVo = GoodsDao.getGoodsVoByGoodsId(1).apply()
    println(goodsVo.get)
    val res = Await.result(SeckillService.reset(Seq(goodsVo.get)), Duration.Inf) //默认商品
    println("reset: " + res)
  }

  def test_getSeckillResult = {
    val res = SeckillService.getSeckillResult(mockUserId, mockGoodsId)
    println("getSeckillResult: " + Await.result(res, Duration.Inf))
  }

  def test_setGoodsOver = {
    val res = SeckillService.setGoodsOver(mockGoodsId)
    println("setGoodsOver: " + Await.result(res, Duration.Inf))
  }

  def test_getGoodsOver = {
    val res = SeckillService.getGoodsOver(mockGoodsId)
    println("getGoodsOver: " + Await.result(res, Duration.Inf))
  }

  def test_createSeckillPath_checkPath = {
    val path = Await.result(SeckillService.createSeckillPath(mockSeckillUser, mockGoodsId), Duration.Inf)
    println("createSeckillPath: " + path)

    val res = SeckillService.checkPath(mockSeckillUser, mockGoodsId, path.getOrElse(""))
    println("checkPath: " + Await.result(res, Duration.Inf))
  }
}
