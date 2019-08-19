package io.github.dreamy.seckill.dao

import io.github.dreamy.seckill.entity
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.util.ImplicitUtils
import scalikejdbc._

/**
 * 秒杀用户
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
trait SeckillUserDao extends ImplicitUtils {

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
          user.longOpt("registerDate"), user.longOpt("lastLoginDate"))
    }.single()
  }

  //  TODO 事务移到service

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
