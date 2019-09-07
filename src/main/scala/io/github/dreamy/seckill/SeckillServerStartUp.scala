package io.github.dreamy.seckill

import com.google.inject.Guice
import io.github.dreamy.seckill.database.RepositorySupport
import io.github.dreamy.seckill.module.SeckillModule
import io.github.dreamy.seckill.redis.StockCountInitialization

/**
 * 秒杀系统启动类
 *
 * @author 梦境迷离
 * @time 2019-08-19
 * @version v1.0
 */
object SeckillServerStartUp extends App {

  //初始化db
  RepositorySupport.init()
  //启动undertow
  start()
  //初始化redis库存缓存
  StockCountInitialization.initCache()

  /**
   * 80端口可能出现Permission denied: connect
   */
  def start(): Unit = {
    var server: SeckillServer = null
    val moudle = new SeckillModule
    val inject = Guice.createInjector(moudle)
    server = new SeckillServer(inject)
    server.startUp()
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run(): Unit = {
        println("server shutdown, bye bye!")
        server.shutdown()
      }
    })
  }
}
