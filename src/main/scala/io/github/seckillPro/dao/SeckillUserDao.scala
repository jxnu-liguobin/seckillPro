package io.github.seckillPro.dao

import io.github.seckillPro.config.CommonComponet
import io.github.seckillPro.entity.SeckillUser
import scalikejdbc._

import scala.concurrent.Future

/**
 * 秒杀用户
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
trait SeckillUserDao extends CommonComponet {

  /**
   * 根据id查询秒杀用户
   */
  def getById(id: Long): Future[Option[SeckillUser]] = {
    readOnly {
      implicit s =>
        sql"""
              select * from seckill_user where id = ${id}
          """.map {
          user =>
            //使用表的列名更加清晰
            SeckillUser(user.long("id"), user.string("nickname"), user.string("password"),
              user.string("salt"), user.string("head"), user.int("loginCount"),
              user.longOpt("registerDate"), user.longOpt("lastLoginDate"))
        }.single().apply()
    }
  }

  /**
   * 根据id和密码更新秒杀用户
   */
  def update(toBeUpdate: SeckillUser): Future[Int] = {
    localTx {
      implicit s =>
        sql"""
              update seckill_user set password = ${toBeUpdate.password} where id = ${toBeUpdate.id}
          """.update().apply()
    }
  }

}

object SeckillUserDao extends SeckillUserDao
