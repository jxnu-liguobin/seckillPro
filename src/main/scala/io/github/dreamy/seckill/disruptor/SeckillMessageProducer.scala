package io.github.dreamy.seckill.disruptor

import com.lmax.disruptor.{ EventTranslatorTwoArg, RingBuffer }
import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.entity.SeckillUser

/**
 * Disruptor服务提供者
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
class SeckillMessageProducer(ringBuffer: RingBuffer[SeckillMessage]) extends LazyLogging {

  object SeckillEventTranslatorVararg extends EventTranslatorTwoArg[SeckillMessage, SeckillMessage, Boolean] {
    override def translateTo(t: SeckillMessage, l: Long, a: SeckillMessage, b: Boolean): Unit = {
      t.goodsId = a.goodsId
      t.seckillUser = a.seckillUser
    }
  }

  def seckill(goodsId: Long, seckillUser: SeckillUser): Unit = {
    logger.info(s"send message, goodsId: $goodsId, seckillUser: $seckillUser")
    this.ringBuffer.publishEvent(SeckillEventTranslatorVararg, SeckillMessage(goodsId, seckillUser), true)
  }
}
