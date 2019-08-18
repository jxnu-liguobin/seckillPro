package io.github.dao.test

import io.github.seckillPro.database.RepositorySupport
import scalikejdbc._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object TestDbSql extends App {

  object RepositorySupportTest extends RepositorySupport

  import RepositorySupportTest._

  RepositorySupportTest.init()

  select

  update

  select

  def select = {
    val listNames = readOnly { implicit session =>
      sql"""
          select * from goods
          """.map {
        res =>
          (res.int(1), res.string(2), res.string(3),
            res.string(4), res.string(5),
            res.string(6), res.int(6))
      }.list().apply()
    }

    val listNamesA = Await.result(listNames, Duration.Inf)
    println(listNamesA)
  }


  /**
   * execute方法执行返回的都是布尔值。
   * executeupdate返回的都是int整数类型。
   * execute方法在执行SQL语句的时候比较麻烦，而executeupdate比较方便
   * execute executes java.sql.PreparedStatement#execute()
   * update executes java.sql.PreparedStatement#executeUpdate() 一般使用这种，得到影响的条数
   */
  def update = {
    val updateNames = localTx {
      val name = "after update "
      implicit session => {
        sql"""
            update goods set goods_name = $name where id = 1
        """.update().apply()
      }
    }
    val updateNamesA = Await.result(updateNames, Duration.Inf)
    println(updateNamesA)

  }


}
