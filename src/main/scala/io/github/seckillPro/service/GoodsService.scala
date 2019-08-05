package io.github.seckillPro.service

import io.github.seckillPro.dao.GoodsDao
import io.github.seckillPro.entity.SeckillGoods
import io.github.seckillPro.presenter.GoodsVo

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 商品
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
trait GoodsService {

  /**
   * 查询全部
   */
  def listGoodsVo = GoodsDao.listGoodsVo()

  /**
   * 根据商品id查询商品视图对象
   */
  def getGoodsVoByGoodsId(id: Long) = GoodsDao.getGoodsVoByGoodsId(id)

  /**
   * 修改商品库存
   *
   * 默认每次减1
   */
  def reduceStock(goods: GoodsVo) = {
    val g = SeckillGoods(id = -1, goodsId = goods.goods.id, stockCount = goods.stockCount
      , seckillPrice = goods.seckillPrice)
    GoodsDao.reduceStock(g).map(x => x)
  }

  /**
   * 恢复库存,0为正确
   */
  def resetStock(goodsList: Seq[GoodsVo]) = {
    for (goods <- goodsList) {
      val g = SeckillGoods(id = -1, goodsId = goods.goods.id, stockCount = goods.stockCount, seckillPrice = goods.seckillPrice)
      GoodsDao.resetStock(g).map(x => x)
    }
    Future.successful(0)
  }
}

object GoodsService extends GoodsService
