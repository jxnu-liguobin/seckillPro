package io.github.seckillPro.dao

import io.github.seckillPro.config.CommonComponet
import io.github.seckillPro.entity.{OrderInfo, SeckillOrder}
import scalikejdbc._

import scala.concurrent.Future

/**
 * 订单
 *
 * @author 梦境迷离
 * @time 2019-08-04
 * @version v2.0
 */
trait OrderDao extends CommonComponet {

  /**
   * 根据用户id、商品id,查询秒杀订单
   */
  def getSeckillOrderByUserIdGoodsId(userId: Long, goodsId: Long): Future[Option[SeckillOrder]] = {
    readOnly {
      implicit s =>
        sql"""
              select * from seckill_order where user_id=${userId} and goods_id=${goodsId}
          """.map { so =>
          SeckillOrder(so.longOpt("id"), so.longOpt("user_id"),
            so.longOpt("order_id"), so.longOpt("goods_id"))
        }.single().apply()
    }
  }

  /**
   * 订单插入成功，并返回主键
   */
  def insert(orderInfo: OrderInfo): Future[Long] = {
    localTx {
      implicit s =>
        sql"""
              insert into order_info(user_id, goods_id, goods_name, goods_count, goods_price, order_channel, status, create_date)
              values (${orderInfo.userId}, ${orderInfo.goodsId}, ${orderInfo.goodsName}, ${orderInfo.goodsCount}, ${orderInfo.goodsPrice},
          ${orderInfo.orderChannel},${orderInfo.status},${toLong(orderInfo.createDate)} )
              """.updateAndReturnGeneratedKey("id").apply()
    }
  }

  /**
   * 新增秒杀订单
   */
  def insertSeckillOrder(seckillOrder: SeckillOrder): Future[Int] = {
    localTx {
      implicit s =>
        sql"""
              insert into seckill_order(user_id, goods_id, order_id) values(${seckillOrder.userId},
           ${seckillOrder.goodsId}, ${seckillOrder.orderId})
          """.update().apply()
    }
  }

  /**
   * 根据订单id,查询订单信息
   */
  def getOrderById(orderId: Long): Future[Option[OrderInfo]] = {
    readOnly {
      implicit s =>
        sql"""
              select * from order_info where id = ${orderId}
          """.map {
          o =>
            val c = toLocalDateTime(o.longOpt("create_date"))
            val p = toLocalDateTime(o.longOpt("pay_date"))
            val order = OrderInfo(o.longOpt("id"), o.longOpt("user_id"), o.longOpt("goods_id"),
              o.longOpt("delivery_addr_id"), o.string("goods_name"), o.int("goods_count"),
              o.double("goods_price"), o.int("order_channel"), o.int("status"),
              c, p)
            order
        }.single().apply()
    }
  }

  /**
   * 删除所有订单信息
   */
  def deleteOrders(): Future[Int] = {
    localTx {
      implicit s =>
        sql"""
              delete from order_info
          """.update().apply()
    }
  }

  /**
   * 删除所有秒杀订单
   */
  def deleteSeckillaOrders(): Future[Int] = {
    localTx {
      implicit s =>
        sql"""
             delete from seckill_order
          """.update().apply()
    }
  }
}

object OrderDao extends OrderDao
