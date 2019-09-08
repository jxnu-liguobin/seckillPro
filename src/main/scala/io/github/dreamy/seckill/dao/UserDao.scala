package io.github.dreamy.seckill.dao

import io.github.dreamy.seckill.entity.User
import scalikejdbc._

/**
 * 普通用户
 *
 * @author 梦境迷离
 * @since 2019-08-03
 * @version v2.0
 */
trait UserDao {

  /**
   * 根据用户id，查询用户
   */
  def getById (id: Long) = {
    sql"""
              select * from user where id = ${id}
          """.map {
      user => User(user.long("id"), user.string("name"))
    }.single()
  }

  /**
   * 新增用户
   */
  def insert (id: Int, name: String) = {
    sql"""
               insert into user(id,name) values(${id},${name})
          """.update()
  }
}

object UserDao extends UserDao
