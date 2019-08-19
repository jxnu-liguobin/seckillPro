package io.github.dreamy.seckill.disruptor

import com.lmax.disruptor.EventHandler
import com.typesafe.scalalogging.LazyLogging

/** Disruptor清理事件处理器
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
object SeckillCleanHandler extends EventHandler[SeckillMessage] with LazyLogging {
  override def onEvent(t: SeckillMessage, l: Long, b: Boolean): Unit = {
    logger.info(s"clean SeckillMessage: [$t]")
    t.goodsId = -1
    t.seckillUser = null
  }
}
