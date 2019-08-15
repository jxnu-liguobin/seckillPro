package io.github.seckillPro.disruptor

import io.github.seckillPro.entity.SeckillUser

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
