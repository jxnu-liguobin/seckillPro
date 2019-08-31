package io.github.dreamy.seckill.service

import io.github.dreamy.seckill.dao.SeckillUserDao
import io.github.dreamy.seckill.database.RepositorySupport
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.SessionBuilder
import io.github.dreamy.seckill.presenter.{ CodeMsg, LoginVo }
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.SeckillUserKey
import io.github.dreamy.seckill.util.{ MD5Utils, UUIDUtils, VerifyEmpty }
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.CookieImpl
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
      if (VerifyEmpty.empty(user))
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

trait SeckillUserServiceComponent extends RepositorySupport {

  /**
   * 添加cookie
   *
   * @param exchange undertow中 类似Java request的对象
   * @param token    生成的uuid
   * @param user     登录用户
   * @return
   */
  private[this] def addCookie(exchange: HttpServerExchange, token: String, user: SeckillUser) = {
    //TODO 这里存在用户
    RedisService.set(SeckillUserKey.token, token, user)
    val cookie = new CookieImpl(SeckillUserService.COOKI_NAME_TOKEN, token)
    //TODO 成功设置到浏览器
    cookie.setMaxAge(60 * 60 * 24 * 7)
    cookie.setPath("/")
    //TODO 未验证是否需要在service层返回HttpServerExchange对象
    exchange.setResponseCookie(cookie)
  }

  /**
   * 登录
   */
  def login(exchange: HttpServerExchange, loginVo: LoginVo) = {
    localTx { implicit session =>
      if (VerifyEmpty.empty(loginVo)) throw GlobalException(CodeMsg.SERVER_ERROR)
      // 判断手机号是否存在
      val user = getById(loginVo.mobile.toLong)
      if (VerifyEmpty.empty(user)) throw GlobalException(CodeMsg.MOBILE_NOT_EXIST)
      val sessionCustom = SessionBuilder.getOrCreateSession(exchange)
      val token = UUIDUtils.uuid
      logger.info(s"session-id-login: ${sessionCustom.getId}")
      sessionCustom.setAttribute(token, user)
      // 验证密码
      val dbPass = user.password
      val saltDB = user.salt
      val calcPass = MD5Utils.formPassToDBPass(loginVo.password, saltDB)
      if (!calcPass.equals(dbPass)) throw GlobalException(CodeMsg.PASSWORD_ERROR)
      // 生成cookie
      addCookie(exchange, token, user)
      token
    }
  }

  /**
   * 根据token获取用户信息
   */
  def getByToken(exchange: HttpServerExchange, token: String) = {
    if (VerifyEmpty.empty(token)) None
    else {
      val user = RedisService.get(SeckillUserKey.token, token, classOf[SeckillUser])
      // 延长有效期
      if (VerifyEmpty.noEmpty(user)) addCookie(exchange, token, user)
      Option(user)
    }
  }

  /**
   * 查询秒杀用户
   *
   * 存在，返回
   * 不在，去数据查询，再放入redis
   */
  def getById(id: Long)(implicit session: DBSession = getReadOnlySession) = {
    // 取缓存
    val user = RedisService.get(SeckillUserKey.getById, "" + id, classOf[SeckillUser])
    if (VerifyEmpty.noEmpty(user)) {
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
