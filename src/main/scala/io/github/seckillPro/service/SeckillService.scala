package io.github.seckillPro.service

import com.typesafe.scalalogging.LazyLogging
import io.github.seckillPro.db.DatabaseSupport
import io.github.seckillPro.entity.SeckillUser
import io.github.seckillPro.presenter.GoodsVo
import io.github.seckillPro.redis.RedisService
import io.github.seckillPro.redis.key.SeckillKey
import io.github.seckillPro.util.{MD5Utils, UUIDUtils}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 秒杀服务
 *
 * @author 梦境迷离
 * @time 2019-08-06
 * @version v2.0
 */
trait SeckillService extends LazyLogging {

  /**
   * 秒杀
   */
  def seckill(user: SeckillUser, goodsVo: GoodsVo) = {
    DatabaseSupport.getDB.beginIfNotYet()
    (for {
      // 减库存 下订单 写入秒杀订单
      //TODO 这个事务有问题，库存还是被减了
      reduceStock <- GoodsService.reduceStock(goodsVo)
      order <- reduceStock match {
        case success if success > 0 =>
          //order_info seckill_order
          //成功反回订单，失败反回None
          logger.info(s"reduce stock status: ${success}")
          OrderService.createOrder(user, goodsVo).map {
            case order@Some(o) =>
              logger.info(s"reduce stock successful when create order")
              order
            case order@None =>
              logger.info(s"reduce stock failed when create order")
              None
          }
        case _ =>
          //卖完了，记录下
          logger.info(s"reduce stock failed: had over")
          setGoodsOver(goodsVo.goods.id.getOrElse(-1))
          Future.successful(None)
      }
    } yield order).recover {
      //减库存 下订单 写入秒杀订单
      case e: Exception =>
        logger.warn(s"Seckill failed by: ${e.getMessage}, then rollback")
        DatabaseSupport.getDB.rollbackIfActive()
        None
    }
  }

  /**
   * 还原环境【测试用】
   */
  def reset(goodsList: Seq[GoodsVo]) = {
    DatabaseSupport.getDB.beginIfNotYet()
    (for {
      _ <- GoodsService.resetStock(goodsList)
      _ <- OrderService.deleteOrders()
    } yield Unit).recover {
      case e: Exception =>
        logger.warn(s"Reset goods list failed by: ${e.getMessage}, then rollback")
        DatabaseSupport.getDB.rollbackIfActive()
        Unit
    }
  }

  /**
   * 前台轮询查询秒杀结果
   */
  def getSeckillResult(userId: Long, goodsId: Long) =
    OrderService.getSeckillOrderByUserIdGoodsId(userId, goodsId) flatMap {
      //Option 有问题:Failed to invoke public scala.Option() with no args
      //秒杀成功
      //返回秒杀商品给前台用来查看订单
      case Some(order) => Future.successful(order.goodsId.getOrElse(-1))
      case None => getGoodsOver(goodsId).map {
        //因为卖完了
        case true => -1
        case false => 0
      }
    }

  /**
   * 设置商品是否秒杀完标记
   */
  def setGoodsOver(goodsId: Long) = Future.successful {
    RedisService.set(SeckillKey.isGoodsOver, "" + goodsId, true)
  }

  /**
   * 商品是否秒杀完的标记
   */
  def getGoodsOver(goodsId: Long) = Future.successful {
    RedisService.exists(SeckillKey.isGoodsOver, "" + goodsId)
  }

  /**
   * 生成随机uuid，作为获取地址携带的参数
   */
  def createSeckillPath(user: SeckillUser, goodsId: Long) = Future.successful {
    if (user == null || goodsId < 1) {
      None
    } else {
      val str = MD5Utils.md5(UUIDUtils.uuid + "123456")
      RedisService.set(SeckillKey.getSeckillPath, "" + user.id + "_" + goodsId, str)
      Option(str)
    }
  }

  /**
   * 判断前端传来的uuid是否与redis中的相同
   */
  def checkPath(user: SeckillUser, goodsId: Long, path: String) = Future.successful {
    if (user == null || path == null) {
      false
    } else {
      val pathOld = RedisService.get(SeckillKey.getSeckillPath, "" + user.id + "_" + goodsId, classOf[String])
      path.equals(pathOld)
    }
  }
}

object SeckillService extends SeckillService
