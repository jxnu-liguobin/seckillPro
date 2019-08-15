package io.github.seckillPro.disruptor

import com.lmax.disruptor.EventHandler
import com.typesafe.scalalogging.LazyLogging

/**
 * Disruptor消费者
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
object SeckillMessageConsumer extends EventHandler[SeckillMessage] with LazyLogging {
  override def onEvent(seckillEvent: SeckillMessage, l: Long, b: Boolean): Unit = {
    logger.info(s"receive SeckillEvent message, consumer: [$seckillEvent]")
    SeckillServiceHandlerImpl.newInstance.startSeckil(seckillEvent.goodsId, seckillEvent.seckillUser)
  }
}
