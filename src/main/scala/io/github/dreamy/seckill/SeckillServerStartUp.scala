package io.github.dreamy.seckill

import java.util.concurrent.CountDownLatch

import com.google.inject.Guice
import io.github.dreamy.seckill.module.SeckillModule

/**
 * 秒杀系统启动类
 *
 * @author 梦境迷离
 * @time 2019-08-19
 * @version v1.0
 */
object SeckillServerStartUp extends App {

  start()

  /**
   * 80端口可能出现Permission denied: connect
   */
  def start(): Unit = {
    val c = new CountDownLatch(1)
    var server: SeckillServer = null
    try {
      val moudle = new SeckillModule
      val inject = Guice.createInjector(moudle)
      server = new SeckillServer(inject)
      server.startUp()
      c.await()
    } catch {
      case _ =>
        c.countDown()
        server.shutdown()
    }
  }
}
