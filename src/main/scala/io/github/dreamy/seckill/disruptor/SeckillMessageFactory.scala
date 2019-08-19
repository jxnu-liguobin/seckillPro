package io.github.dreamy.seckill.disruptor

import com.lmax.disruptor.EventFactory

/**
 * Disruptor事件生成工厂
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
object SeckillMessageFactory extends EventFactory[SeckillMessage] {
  override def newInstance(): SeckillMessage = SeckillMessage()
}
