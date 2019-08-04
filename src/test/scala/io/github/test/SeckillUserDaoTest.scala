package io.github.test

import io.github.seckillPro.dao.SeckillUserDao
import io.github.seckillPro.entity.SeckillUser
import io.github.seckillPro.util.MD5Util

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * 秒杀用户测试
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
object SeckillUserDaoTest extends BaseTest with App {

  val seckUser = Await.result(SeckillUserDao.getById(15312345678L), Duration.Inf)
  println(seckUser.getOrElse("null"))

  val seckUser2 = Await.result(SeckillUserDao.update(SeckillUser(15312345678l, "user",
    MD5Util.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1)), Duration.Inf)
  println(seckUser2)

}
