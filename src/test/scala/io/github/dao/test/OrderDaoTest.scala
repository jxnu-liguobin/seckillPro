package io.github.dao.test

import io.github.BaseTest
import io.github.dreamy.seckill.dao.OrderDao
import io.github.dreamy.seckill.entity.{ OrderInfo, SeckillOrder }

/**
 *
 * @author 梦境迷离
 * @time 2019-08-04
 * @version v2.0
 */
object OrderDaoTest extends BaseTest with App {

  println("===================insert======================")
  //  val insertRes = Await.result(OrderDao.insert(OrderInfo(None, Option(1), Option(1), Option(1), "goodsName", 11, 1.02, 1, 2)).apply(), Duration.Inf)
  val insertRes = OrderDao.insert(OrderInfo(None, Option(1), Option(1), Option(1), "goodsName", 11, 1.02, 1, 2)).apply()
  println("id => " + insertRes)

  println("===================insertSeckillOrder======================")
  //  val insertSeckillOrderRes = Await.result(OrderDao.insertSeckillOrder(SeckillOrder(None, Option(1), Option(1), Option(1))).apply(), Duration.Inf)
  val insertSeckillOrderRes = OrderDao.insertSeckillOrder(SeckillOrder(None, Option(1), Option(1), Option(1))).apply()
  println(insertSeckillOrderRes)

  println("===================getSeckillOrderByUserIdGoodsId======================")
  val seckillOrder = OrderDao.getSeckillOrderByUserIdGoodsId(1,
    1).apply()
  println(seckillOrder)

  println("===================getOrderById======================")
  val getOrderByIdRes = OrderDao.getOrderById(insertRes).apply()
  println(getOrderByIdRes)

  println("===================deleteOrders======================")
  val deleteOrdersRes = OrderDao.deleteOrders()
  println(deleteOrdersRes)

  println("===================deleteSeckillaOrders======================")
  val deleteSeckillaOrdersRes = OrderDao.deleteSeckillaOrders()
  println(deleteSeckillaOrdersRes)


}
