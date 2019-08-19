package io.github

import java.time.LocalDateTime

import io.github.dreamy.seckill.database.RepositorySupport
import io.github.dreamy.seckill.entity.{ Goods, OrderInfo, SeckillOrder, SeckillUser }
import io.github.dreamy.seckill.presenter.GoodsVo
import io.github.dreamy.seckill.util.MD5Utils
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

  object RepositorySupportTest extends RepositorySupport

  RepositorySupportTest.init()

  /**
   * -1表示记录不存在时的默认值
   */
  val mockSeckillUser = SeckillUser(Option(15312345678L), "user",
    MD5Utils.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1)

  val mockOrderInfo = OrderInfo(None, Option(1), Option(1), Option(1), "goodsName", 11, 1.02, 1, 2)

  val mockSeckillOrder = SeckillOrder(Option(1), Option(1), Option(1), Option(1))

  val mockGoodsId = 1L

  val mockUserId = 15312345678L

  implicit val session = RepositorySupportTest.getDB.autoCommitSession()

  val mockGoodsVo = GoodsVo(Goods(Some(1), "android手机", "android手机", "/img/meta10.png", "android手机", 100.0, 100)
    , 0.01, 1, Option(LocalDateTime.now()), Option(LocalDateTime.now()))


}

object BaseTest
