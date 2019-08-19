package io.github.dreamy.seckill.database

import scalikejdbc.{ConnectionPool, DB, DBSession, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
 * 数据库连接以及CRUD事物封装
 *
 * @author 梦境迷离
 * @time 2019-08-01
 * @version v2.0
 */
trait RepositorySupport extends DataSourceSupport {

  def readOnly[A](execution: DBSession => A): Future[A] = concurrent.Future {
    using(getDB) { db: DB =>
      db.readOnly((session: DBSession) => execution(session))
    }
  }

  def localTx[A](execution: DBSession => A): Future[A] = concurrent.Future {
    using(getDB) { db: DB =>
      db.localTx((session: DBSession) => execution(session))
    }
  }

  @deprecated
  def localTxWithoutFuture[A](execution: DBSession => A): A =
    using(getDB) { db: DB =>
      db.localTx((session: DBSession) => execution(session))
    }

  def getAutoCommitSession = getDB.autoCommitSession()

  def getReadOnlySession = getDB.readOnlySession()

  def getConnectionPool = ConnectionPool.get()

  def getDB: DB = DB(ConnectionPool.get().borrow())
}