package io.github.dreamy.seckill.disruptor

import io.github.dreamy.seckill.entity.SeckillUser

/**
 * 事件对象
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
final case class SeckillMessage(var goodsId: Long, var seckillUser: SeckillUser) extends DisruptorEvent

//初始化需要
object SeckillMessage {
  def apply(): SeckillMessage = SeckillMessage(-1, SeckillUser(Option(-1L), "", "", "", "", -1))
}