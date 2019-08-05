package io.github.seckillPro.dao

import io.github.seckillPro.config.CommonComponet
import io.github.seckillPro.entity.User
import scalikejdbc._

import scala.concurrent.Future

/**
 * 普通用户
 *
 * @author 梦境迷离
 * @time 2019-08-03
 * @version v2.0
 */
trait UserDao extends CommonComponet {

  /**
   * 根据用户id，查询用户
   */
  def getById(id: Long): Future[Option[User]] = {
    readOnly {
      implicit s =>
        sql"""
              select * from user where id = ${id}
          """.map {
          user => User(user.long("id"), user.string("name"))
        }.single().apply()
    }
  }

  /**
   * 新增用户
   */
  def insert(id: Int, name: String): Future[Int] = {
    localTx {
      implicit s =>
        sql"""
               insert into user(id,name) values(${id},${name})
          """.update().apply()
    }
  }
}

object UserDao extends UserDao
