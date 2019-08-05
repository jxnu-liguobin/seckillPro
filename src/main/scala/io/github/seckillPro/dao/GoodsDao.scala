package io.github.seckillPro.dao

import io.github.seckillPro.config.CommonComponet
import io.github.seckillPro.entity.{Goods, SeckillGoods}
import io.github.seckillPro.presenter.GoodsVo
import scalikejdbc._

import scala.concurrent.Future

/**
 * 商品
 *
 * @author 梦境迷离
 * @time 2019-08-02
 * @version v2.0
 */
trait GoodsDao extends CommonComponet {

  /**
   * 查询出所有商品视图对象
   */
  def listGoodsVo(): Future[Seq[GoodsVo]] = {
    readOnly { implicit s =>
      sql"""
          select g.*,mg.seckill_price,mg.stock_count, mg.start_date, mg.end_date from seckill_goods mg left join goods g on mg.goods_id = g.id
      """.map {
        goods =>
          val g = Goods(goods.long(1), goods.string(2), goods.string(3),
            goods.string(4), goods.string(5), goods.string(6), goods.string(7))
          GoodsVo(g, goods.double(8),
            goods.int(9),
            goods.longOpt(10),
            goods.longOpt(11)
          )
      }.list().apply()
    }

  }

  /**
   * 根据商品id=秒杀的商品id,查询出商品视图对象
   */
  def getGoodsVoByGoodsId(goodsId: Long): Future[Option[GoodsVo]] = {
    readOnly {
      implicit s =>
        sql"""
              select g.*,mg.seckill_price, mg.stock_count,mg.start_date, mg.end_date from seckill_goods mg
              left join goods g on mg.goods_id = g.id where g.id = ${goodsId}
          """.map {
          goods =>
            val g = Goods(goods.long(1), goods.string(2), goods.string(3),
              goods.string(4), goods.string(5), goods.string(6), goods.string(7))
            GoodsVo(g, goods.double(8),
              goods.int(9),
              goods.longOpt(10),
              goods.longOpt(11)
            )
        }.single().apply()
    }
  }

  /**
   * 库存减1
   */
  def reduceStock(g: SeckillGoods): Future[Int] = {
    localTx {
      implicit s =>
        sql"""
              update seckill_goods set stock_count = stock_count - 1 where goods_id = ${g.goodsId} and stock_count > 0
          """.update().apply()
    }
  }

  /**
   * 库存修改
   */
  def resetStock(g: SeckillGoods): Future[Int] = {
    localTx {
      implicit s =>
        sql"""
              update seckill_goods set stock_count = ${g.stockCount} where goods_id = ${g.goodsId}
          """.update().apply()
    }
  }
}

object GoodsDao extends GoodsDao
