package io.github.dreamy.seckill.dao

import io.github.dreamy.seckill.entity
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.util.CustomConversions._
import scalikejdbc._

/**
 * 秒杀用户
 *
 * @author 梦境迷离
 * @since 2019-08-03
 * @version v2.0
 */
trait SeckillUserDao {

  /**
   * 根据id查询秒杀用户
   */
  def getById(id: Long) = {
    sql"""
              select * from seckill_user where id = ${id}
          """.map {
      user =>
        //使用表的列名更加清晰
        entity.SeckillUser(user.longOpt("id"), user.string("nickname"), user.string("password"),
          user.string("salt"), user.string("head"), user.int("loginCount"),
          user.longOpt("registerDate").toLocalDateTimeOpt, user.longOpt("lastLoginDate").toLocalDateTimeOpt)
    }.single()
  }

  /**
   * 根据id和密码更新秒杀用户
   */
  def update(toBeUpdate: SeckillUser) = {
    sql"""
              update seckill_user set password = ${toBeUpdate.password} where id = ${toBeUpdate.id}
          """.update()
  }

}

object SeckillUserDao extends SeckillUserDao
