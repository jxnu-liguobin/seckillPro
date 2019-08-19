package io.github.dao.test

import io.github.BaseTest
import io.github.dreamy.seckill.dao.SeckillUserDao
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.util.MD5Utils

/**
 * 秒杀用户测试
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
object SeckillUserDaoTest extends BaseTest with App {

  val seckUser = SeckillUserDao.getById(15312345678L).apply()
  println(seckUser.getOrElse("null"))

  val seckUser2 = SeckillUserDao.update(SeckillUser(Option(15312345678l), "user",
    MD5Utils.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1)).apply()
  println(seckUser2)

}
