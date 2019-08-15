package io.github.seckillPro.disruptor

import java.util.concurrent.ThreadFactory

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.lmax.disruptor.dsl.Disruptor

/**
 * Disruptor测试主类
 *
 * @author 梦境迷离
 * @version 1.0,2019-08-01
 */
object SeckillMessageQueueServer {

  object DisruptorEngine {

    def create: Disruptor[SeckillMessage] = {
      val threadFactory: ThreadFactory = new ThreadFactoryBuilder().setNameFormat("sigkill-test-handler-%d").build()
      val disruptor: Disruptor[SeckillMessage] = new Disruptor[SeckillMessage](SeckillMessageFactory, 1024, threadFactory)
      disruptor.handleEventsWith(SeckillMessageConsumer).`then`(SeckillCleanHandler) //使用唯一的消费者，没有在这进行封装路由。
      disruptor.setDefaultExceptionHandler(SeckillExceptionHandler)
      disruptor
    }
  }

}