package io.github.seckillPro.service

import io.github.seckillPro.dao.GoodsDao
import io.github.seckillPro.db.DatabaseSupport
import io.github.seckillPro.entity.SeckillGoods
import io.github.seckillPro.presenter.GoodsVo
import scalikejdbc.DBSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 商品
 * 由外部调用者提供session，实现多操作的事务
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
trait GoodsService extends GoodsServiceComponent {

  /**
   * 查询全部
   */
  def listGoodsVo()(implicit session: DBSession = getReadOnlySession) =
    Future {
      GoodsDao.listGoodsVo().apply()
    }

  /**
   * 根据商品id查询商品视图对象
   */
  def getGoodsVoByGoodsId(id: Long)(implicit session: DBSession = getReadOnlySession) =
    Future {
      GoodsDao.getGoodsVoByGoodsId(id).apply()
    }

}

/**
 * 用以实现需要事务处理的操作
 */
trait GoodsServiceComponent extends DatabaseSupport {

  /**
   * 修改商品库存
   *
   * 默认每次减1
   */
  def reduceStock(goods: GoodsVo)(implicit session: DBSession = getAutoCommitSession) = {
    val g = SeckillGoods(None, goodsId = goods.goods.id, stockCount = goods.stockCount
      , seckillPrice = goods.seckillPrice)
    GoodsDao.reduceStock(g).apply()
  }

  /**
   * 恢复库存,0为正确
   */
  def resetStock(goodsList: Seq[GoodsVo])(implicit session: DBSession = getAutoCommitSession) = {
    for (goods <- goodsList) {
      val g = SeckillGoods(None, goodsId = goods.goods.id, stockCount = goods.stockCount, seckillPrice = goods.seckillPrice)
      GoodsDao.resetStock(g).apply()
    }
  }

}

object GoodsService extends GoodsService
