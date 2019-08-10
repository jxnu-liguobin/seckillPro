package io.github.seckillPro.service

import io.github.seckillPro.dao.SeckillUserDao
import io.github.seckillPro.db.DatabaseSupport
import io.github.seckillPro.entity.SeckillUser
import io.github.seckillPro.exception.GlobalException
import io.github.seckillPro.presenter.CodeMsg
import io.github.seckillPro.redis.RedisService
import io.github.seckillPro.redis.key.SeckillUserKey
import io.github.seckillPro.util.MD5Utils
import scalikejdbc.DBSession

/**
 * 秒杀用户
 *
 * @author 梦境迷离
 * @time 2019-08-07
 * @version v2.0
 */
trait SeckillUserService extends SeckillUserServiceComponent {

  /**
   * token是唯一的，需要传进来，不然无法登陆了
   */
  def updatePasswordById(token: String, id: Long, formPass: String) = {
    localTx { implicit session =>
      val user = getById(id)
      if (user == null)
        throw GlobalException(CodeMsg.MOBILE_NOT_EXIST)
      //只有id和密码是需要的
      val newPass = MD5Utils.formPassToDBPass(formPass, user.salt)
      // 更新数据库，更新什么字段就传入什么字段，减少传输的数据
      val toBeUpdate = SeckillUser(Option(id), "", newPass, user.salt, "", 1)
      updatePassword(toBeUpdate)
      // 清楚缓存再更新
      RedisService.delete(SeckillUserKey.getById, "" + id)
      toBeUpdate.copy(password = newPass)
      //更新，如果删除token[uuid唯一]就无法登陆了
      RedisService.set(SeckillUserKey.token, token, user)
      true
    }
  }
}

trait SeckillUserServiceComponent extends DatabaseSupport {

  //  TODO
  /**
   * 登录
   */

  //  TODO
  /**
   * 设置cookie
   */

  //  TODO
  /**
   * 根据token获取用户信息
   */

  /**
   * 查询秒杀用户
   *
   * 存在，返回
   * 不在，去数据查询，再放入redis
   */
  def getById(id: Long)(implicit session: DBSession = getReadOnlySession) = {
    // 取缓存
    val user = RedisService.get(SeckillUserKey.getById, "" + id, classOf[SeckillUser])
    if (user != null) {
      user
    } else {
      SeckillUserDao.getById(id).apply() match {
        case Some(u) =>
          logger.info(s"seckill user: $u")
          RedisService.set(SeckillUserKey.getById, "" + id, u)
          u
        case None =>
          throw GlobalException(CodeMsg.MOBILE_NOT_EXIST)
        //          null
      }
    }
  }

  def updatePassword(toBeUpdate: SeckillUser)(implicit session: DBSession = getAutoCommitSession) = {
    SeckillUserDao.update(toBeUpdate).apply()
  }
}

object SeckillUserService extends SeckillUserService {

  final val COOKI_NAME_TOKEN = "token"

}
