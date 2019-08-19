package io.github.dreamy.seckill.service

import java.time.LocalDateTime

import io.github.dreamy.seckill.dao.OrderDao
import io.github.dreamy.seckill.database.RepositorySupport
import io.github.dreamy.seckill.entity.{ OrderInfo, SeckillOrder, SeckillUser }
import io.github.dreamy.seckill.presenter.GoodsVo
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.OrderKey
import scalikejdbc.DBSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 订单
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
trait OrderService extends OrderServiceComponent {

  /**
   * 是否已经秒杀过【(user.id，goods.id)是数据库的唯一索引，且秒杀时，这个将存进redis】
   */
  def getSeckillOrderByUserIdGoodsId(userId: Long, goodsId: Long) =
    Future {
      Option(RedisService.get(OrderKey.getSeckillOrderByUidGid, "" + userId + "_" + goodsId, classOf[SeckillOrder]))
    }

}

trait OrderServiceComponent extends RepositorySupport {
  /**
   * 订单创建
   */
  //TODO 常量提取、异常处理、订单逻辑完善
  def createOrder(user: SeckillUser, goodsVo: GoodsVo)(implicit session: DBSession = getAutoCommitSession) = {
    val orderInfo = OrderInfo(None, user.id, goodsVo.goods.id, Option(0L), goodsVo.goods.goodsName, goodsCount = 1,
      goodsVo.goods.goodsPrice, orderChannel = 1, status = 0, Option(LocalDateTime.now()), Option(LocalDateTime.now()))
    val id = OrderDao.insert(orderInfo).apply()
    val seckillOrder = SeckillOrder(None, user.id, Option(id), goodsVo.goods.id)
    OrderDao.insertSeckillOrder(seckillOrder).apply()
    //生成订单的时候写完mysql,也要写进redis中,下次点击将直接去缓存，响应快
    RedisService.set(OrderKey.getSeckillOrderByUidGid, "" + user.id.getOrElse(-1) + "_" + goodsVo.goods.id, seckillOrder)
    orderInfo.copy(id = Some(id))
  }


  /**
   * 根据订单id查询订单信息
   */
  def getOrderById(orderId: Long)(implicit session: DBSession = getReadOnlySession) =
    OrderDao.getOrderById(orderId).apply()

  /**
   * 删除订单
   *
   * 先删除订单再删除秒杀订单
   */
  def deleteOrders()(implicit session: DBSession = getAutoCommitSession) = {
    OrderDao.deleteOrders().apply()
    OrderDao.deleteSeckillaOrders().apply()
  }
}

object OrderService extends OrderService