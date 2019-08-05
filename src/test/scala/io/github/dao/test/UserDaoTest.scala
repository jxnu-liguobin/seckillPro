package io.github.dao.test

import io.github.BaseTest
import io.github.seckillPro.dao.UserDao

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * 普通用户测试
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
object UserDaoTest extends BaseTest with App {

  val user = Await.result(UserDao.getById(1), Duration.Inf)

  println(user.getOrElse("null"))
}
