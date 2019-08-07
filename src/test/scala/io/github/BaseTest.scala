package io.github

import io.github.seckillPro.db.DatabaseSupport
import io.github.seckillPro.entity.{OrderInfo, SeckillOrder, SeckillUser}
import io.github.seckillPro.util.MD5Utils
import scalikejdbc.DB

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

  /**
   * -1表示记录不存在时的默认值
   */
  val mockSeckillUser = SeckillUser(Option(15312345678L), "user",
    MD5Utils.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1)

  val mockOrderInfo = OrderInfo(None, Option(1), Option(1), Option(1), "goodsName", 11, 1.02, 1, 2)

  val mockSeckillOrder = SeckillOrder(Option(1), Option(1), Option(1), Option(1))

  val mockGoodsId = 1L

  val mockUserId = 15312345678L
}

object BaseTest
