package io.github.seckillPro.service

import java.time.LocalDateTime

import com.typesafe.scalalogging.LazyLogging
import io.github.seckillPro.dao.OrderDao
import io.github.seckillPro.db.DatabaseSupport
import io.github.seckillPro.entity.{OrderInfo, SeckillOrder, SeckillUser}
import io.github.seckillPro.exception.GlobalException
import io.github.seckillPro.presenter.{CodeMsg, GoodsVo}
import io.github.seckillPro.redis.RedisService
import io.github.seckillPro.redis.key.OrderKey

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 订单
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
trait OrderService extends LazyLogging {

  /**
   * 是否已经秒杀过【(user.id，goods.id)是数据库的唯一索引，且秒杀时，这个将存进redis】
   */
  def getSeckillOrderByUserIdGoodsId(userId: Long, goodsId: Long) = {
    Future {
      Option(RedisService.get(OrderKey.getSeckillOrderByUidGid, "" + userId + "_" + goodsId, classOf[SeckillOrder]))
    }
  }

  /**
   * 订单创建
   */
  def createOrder(user: SeckillUser, goodsVo: GoodsVo) = {
    val orderInfo = OrderInfo(None, user.id, goodsVo.goods.id, Option(0L), goodsVo.goods.goodsName, goodsCount = 1,
      goodsVo.goods.goodsPrice, orderChannel = 1, status = 0, Option(LocalDateTime.now()), Option(LocalDateTime.now()))
    DatabaseSupport.getDB.beginIfNotYet()
    (for {
      id <- OrderDao.insert(orderInfo)
      seckillOrder = SeckillOrder(None, user.id, Option(id), goodsVo.goods.id)
      _ <- OrderDao.insertSeckillOrder(seckillOrder)
    } yield {
      //    生成订单的时候写完mysql,也要写进redis中,下次点击将直接去缓存，响应快
      RedisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.id.getOrElse(-1) + "_" + goodsVo.goods.id, seckillOrder)
      orderInfo.copy(id = Some(id))
      throw GlobalException(CodeMsg.SECKILL_FAIL)
    }).recover {
      case e: Exception =>
        logger.warn(s"Seckill failed when create order by: ${e.getMessage}, then rollback")
        DatabaseSupport.getDB.rollbackIfActive()
    }
  }

  /**
   * 根据订单id查询订单信息
   */
  def getOrderById(orderId: Long) = {
    OrderDao.getOrderById(orderId)
  }

  /**
   * 删除订单
   *
   * 先删除订单再删除秒杀订单
   */
  def deleteOrders() = {
    DatabaseSupport.getDB.beginIfNotYet()
    (for {
      _ <- OrderDao.deleteOrders()
      _ <- OrderDao.deleteSeckillaOrders()
    } yield Unit).recover {
      case e: Exception =>
        logger.warn(s"Delete all orders failed by: ${e.getMessage}, then rollback")
        DatabaseSupport.getDB.rollbackIfActive()
    }
  }
}

object OrderService extends OrderService