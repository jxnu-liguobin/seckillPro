package io.github.service.test

import io.github.BaseTest
import io.github.seckillPro.entity.User
import io.github.seckillPro.redis.RedisService
import io.github.seckillPro.redis.key.SeckillKey

/**
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
object RedisServiceTest extends BaseTest with App {

  //不能序列化Scala的Option类型
  val use = RedisService.beanToString(User(1, "sss"))
  println(use)

  val user = RedisService.stringToBean(use, classOf[User])
  println(user)

  val setRes = RedisService.set[User](SeckillKey.isGoodsOver, "key", user)

  val getRes = RedisService.get(SeckillKey.isGoodsOver, "key", classOf[User])
  println(getRes)

}
