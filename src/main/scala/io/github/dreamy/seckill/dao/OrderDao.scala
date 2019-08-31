package io.github.dreamy.seckill.dao

import io.github.dreamy.seckill.entity
import io.github.dreamy.seckill.entity.{ OrderInfo, SeckillOrder }
import io.github.dreamy.seckill.util.CustomConversions._
import scalikejdbc._

/**
 * 订单
 *
 * @author 梦境迷离
 * @time 2019-08-04
 * @version v2.0
 */
trait OrderDao {

  /**
   * 根据用户id、商品id,查询秒杀订单
   */
  def getSeckillOrderByUserIdGoodsId(userId: Long, goodsId: Long) = {
    sql"""
              select * from seckill_order where user_id=${userId} and goods_id=${goodsId}
          """.map { so =>
      SeckillOrder(so.longOpt("id"), so.longOpt("user_id"),
        so.longOpt("order_id"), so.longOpt("goods_id"))
    }.single()
  }

  /**
   * 订单插入成功，并返回主键
   */
  def insert(orderInfo: OrderInfo) = {
    val long = orderInfo.createDate.toLong
    sql"""
              insert into order_info(user_id, goods_id,delivery_addr_id, goods_name, goods_count, goods_price, order_channel, status, create_date)
              values (${orderInfo.userId}, ${orderInfo.goodsId}, ${orderInfo.deliveryAddrId}, ${orderInfo.goodsName}, ${orderInfo.goodsCount}, ${orderInfo.goodsPrice},
          ${orderInfo.orderChannel},${orderInfo.status},${long} )
              """.updateAndReturnGeneratedKey("id")
  }

  /**
   * 新增秒杀订单
   */
  def insertSeckillOrder(seckillOrder: SeckillOrder) = {
    sql"""
              insert into seckill_order(user_id, goods_id, order_id) values(${seckillOrder.userId},
           ${seckillOrder.goodsId}, ${seckillOrder.orderId})
          """.update()
  }

  /**
   * 根据订单id,查询订单信息
   */
  def getOrderById(orderId: Long) = {
    sql"""
              select * from order_info where id = ${orderId}
          """.map {
      o =>
        val c = (o.longOpt("create_date")).toLocalDateTimeOpt
        val p = o.longOpt("pay_date").toLocalDateTimeOpt
        val order = entity.OrderInfo(o.longOpt("id"), o.longOpt("user_id"), o.longOpt("goods_id"),
          o.longOpt("delivery_addr_id"), o.string("goods_name"), o.int("goods_count"),
          o.double("goods_price"), o.int("order_channel"), o.int("status"),
          c, p)
        order
    }.single()
  }

  /**
   * 删除所有订单信息
   */
  def deleteOrders() = {
    sql"""
              delete from order_info
          """.update()
  }

  /**
   * 删除所有秒杀订单
   */
  def deleteSeckillaOrders() = {
    sql"""
             delete from seckill_order
          """.update()
  }
}

object OrderDao extends OrderDao
