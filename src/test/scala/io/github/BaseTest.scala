package io.github

import io.github.seckillPro.db.DatabaseSupport

/**
 * 测试基类
 *
 * 目前不引入测试框架，后期改用scalatest plus
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
class BaseTest {

  DatabaseSupport.init()

}

object BaseTest
