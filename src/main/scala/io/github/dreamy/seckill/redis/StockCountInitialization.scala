package io.github.dreamy.seckill.redis

import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.presenter.CodeMsg
import io.github.dreamy.seckill.redis.key.GoodsKey
import io.github.dreamy.seckill.service.GoodsService
import io.github.dreamy.seckill.util.ConditionUtils

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * 模拟监听器初始化库存到redis
 *
 * @author 梦境迷离
 * @since 2019-09-07
 * @version v1.0
 */
object StockCountInitialization extends LazyLogging {


  def getLocalOverMap = localOverMap

  private final lazy val localOverMap = new mutable.HashMap[Long, Boolean]()

  //不阻塞
  def initCache() = {
    for {
      goodses <- GoodsService.listGoodsVo()
      _ <- ConditionUtils.failCondition(goodses.isEmpty, GlobalException(CodeMsg.INTERNAL_ERROR))
    } yield {
      goodses.foreach { goodsVo =>
        RedisService.set(GoodsKey.getSeckillGoodsStock, "" + goodsVo.goods.id.getOrElse(-1L), goodsVo.stockCount)
        localOverMap.put(goodsVo.goods.id.getOrElse(-1L), false) //内存标记为没有秒杀完
      }
      logger.info(s"stock count init successfully, goods size is: ${localOverMap.size}")
    }
  }

}
