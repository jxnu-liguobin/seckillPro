package io.github.service.test

import io.github.BaseTest
import io.github.dreamy.seckill.config.Constant
import io.github.dreamy.seckill.service.SeckillUserService

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 *
 * @author 梦境迷离
 * @time 2019-08-07
 * @version v2.0
 */
object SeckillUserServiceTest extends BaseTest with App {

  test_getById()
  test_updatePassword()

  def test_getById(): Unit = {
    val u = SeckillUserService.getById(mockUserId)
    println(u)
  }

  def test_updatePassword(): Unit = {
    val u = Await.result(SeckillUserService.updatePasswordById(Constant.cookie_name_token, mockUserId, "123456"), Duration.Inf)
    println(u)
  }

  def test_login = ???

  def test_addCookie = ???

  def test_getByToken = ???

}
