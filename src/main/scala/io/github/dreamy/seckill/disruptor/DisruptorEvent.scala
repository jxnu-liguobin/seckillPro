package io.github.dreamy.seckill.disruptor

import io.github.dreamy.seckill.entity.SeckillUser

/**
 * Disruptor事件
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
abstract class DisruptorEvent {
  self =>

  var goodsId: Long
  var seckillUser: SeckillUser

}
