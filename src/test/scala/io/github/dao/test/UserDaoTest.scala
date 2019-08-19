package io.github.dao.test

import io.github.BaseTest
import io.github.dreamy.seckill.dao.UserDao

/**
 * 普通用户测试
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
object UserDaoTest extends BaseTest with App {

  val user = UserDao.getById(1).apply()

  println(user.getOrElse("null"))
}
