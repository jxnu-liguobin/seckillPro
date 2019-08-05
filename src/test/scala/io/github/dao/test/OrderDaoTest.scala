package io.github.dao.test

import io.github.BaseTest
import io.github.seckillPro.dao.OrderDao
import io.github.seckillPro.entity.{OrderInfo, SeckillOrder}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 *
 * @author 梦境迷离
 * @time 2019-08-04
 * @version v2.0
 */
object OrderDaoTest extends BaseTest with App {

  println("===================insert======================")
  val insertRes = Await.result(OrderDao.insert(OrderInfo(None, Option(1), Option(1), Option(1), "goodsName", 11, 1.02, 1, 2)), Duration.Inf)
  println("id => " + insertRes)

  println("===================insertSeckillOrder======================")
  val insertSeckillOrderRes = Await.result(OrderDao.insertSeckillOrder(SeckillOrder(None, Option(1), Option(1), Option(1))), Duration.Inf)
  println(insertSeckillOrderRes)

  println("===================getSeckillOrderByUserIdGoodsId======================")
  val seckillOrder = Await.result(OrderDao.getSeckillOrderByUserIdGoodsId(1,
    1), Duration.Inf)
  println(seckillOrder)

  println("===================getOrderById======================")
  val getOrderByIdRes = Await.result(OrderDao.getOrderById(insertRes), Duration.Inf)
  println(getOrderByIdRes)

  println("===================deleteOrders======================")
  val deleteOrdersRes = Await.result(OrderDao.deleteOrders(), Duration.Inf)
  println(deleteOrdersRes)

  println("===================deleteSeckillaOrders======================")
  val deleteSeckillaOrdersRes = Await.result(OrderDao.deleteSeckillaOrders(), Duration.Inf)
  println(deleteSeckillaOrdersRes)


}
