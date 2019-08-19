package io.github.dreamy.seckill.disruptor

import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.presenter.CodeMsg
import io.github.dreamy.seckill.service.{ GoodsService, OrderService, SeckillService }
import io.github.dreamy.seckill.util.ConditionUtils

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

/** Disruptor具体服务实现
 *
 * 处理秒杀实际逻辑
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
class SeckillServiceHandler extends LazyLogging {

  def startSeckil(goodsId: Long, seckillUser: SeckillUser) = {
    logger.info("receive message, start seckill goodsIds: [" + goodsId + "] seckillUser: [" + seckillUser + "]")
    //查数据库，得到秒杀商品视图对象
    val s = for {
      goodsOpt <- GoodsService.getGoodsVoByGoodsId(goodsId)
      _ <- ConditionUtils.failCondition(goodsOpt.isEmpty || seckillUser.id.isEmpty, GlobalException(CodeMsg.INTERNAL_ERROR))
      goods = goodsOpt.get
      //此等待期间库存可能改变，所以需要再次判断库存
      stock = goods.stockCount
      //判断库存是否合法
      _ <- ConditionUtils.failCondition(stock <= 0, GlobalException(CodeMsg.SECKILL_OVER))
      // 从redis中获取秒杀订单，再次判断是否已经秒杀到了
      order <- OrderService.getSeckillOrderByUserIdGoodsId(seckillUser.id.get, goodsId)
      _ <- ConditionUtils.failCondition(order.isDefined, GlobalException(CodeMsg.SECKILL_OVER))
      ret <- order match {
        // 减库存 下订单 写入秒杀订单
        case None => SeckillService.seckill(seckillUser, goods)
      }
    } yield {
      Unit
    }
    import scala.concurrent.duration._
    Await.result(s, 6 seconds)
  }
}

object SeckillServiceHandlerImpl {
  val newInstance = new SeckillServiceHandler
}
