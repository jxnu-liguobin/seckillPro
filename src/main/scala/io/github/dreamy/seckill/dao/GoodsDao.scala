package io.github.dreamy.seckill.dao

import io.github.dreamy.seckill.entity.{ Goods, SeckillGoods }
import io.github.dreamy.seckill.presenter
import io.github.dreamy.seckill.util.CustomConversions._
import scalikejdbc._

/**
 * 商品
 *
 * @author 梦境迷离
 * @since 2019-08-02
 * @version v2.0
 */
trait GoodsDao {

  /**
   * 查询出所有商品视图对象
   */
  def listGoodsVo () = {
    sql"""
          select g.*,mg.seckill_price,mg.stock_count, mg.start_date, mg.end_date from seckill_goods mg left join goods g on mg.goods_id = g.id
      """.map {
      goods =>
        val g = Goods(Some(goods.long(1)), goods.string(2), goods.string(3),
          goods.string(4), goods.string(5), goods.double(6), goods.int(7))
        presenter.GoodsVo(g, goods.double(8),
          goods.int(9),
          goods.longOpt(10).toLocalDateTimeOpt,
          goods.longOpt(11).toLocalDateTimeOpt
        )
    }.list()

  }

  /**
   * 根据商品id=秒杀的商品id,查询出商品视图对象
   */
  def getGoodsVoByGoodsId (goodsId: Long) = {
    sql"""
              select g.*,mg.seckill_price, mg.stock_count,mg.start_date, mg.end_date from seckill_goods mg
              left join goods g on mg.goods_id = g.id where g.id = ${goodsId}
          """.map {
      goods =>
        val g = Goods(goods.longOpt(1), goods.string(2), goods.string(3),
          goods.string(4), goods.string(5), goods.double(6), goods.int(7))
        presenter.GoodsVo(g, goods.double(8),
          goods.int(9),
          goods.longOpt(10).toLocalDateTimeOpt,
          goods.longOpt(11).toLocalDateTimeOpt
        )
    }.single()
  }

  /**
   * 库存减1
   */
  def reduceStock (g: SeckillGoods) = {
    sql"""
              update seckill_goods set stock_count = stock_count - 1 where goods_id = ${g.goodsId} and stock_count > 0
          """.update()
  }

  /**
   * 库存修改
   */
  def resetStock (g: SeckillGoods) = {
    sql"""
              update seckill_goods set stock_count = ${g.stockCount} where goods_id = ${g.goodsId}
          """.update()
  }
}

object GoodsDao extends GoodsDao
