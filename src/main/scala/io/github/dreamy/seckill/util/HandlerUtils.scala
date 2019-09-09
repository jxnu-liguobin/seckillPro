package io.github.dreamy.seckill.util

import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.SessionBuilder
import io.github.dreamy.seckill.presenter.CodeMsg
import io.github.dreamy.seckill.service.SeckillUserService
import io.undertow.server.HttpServerExchange

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.util.Try

/**
 *
 * @author 梦境迷离
 * @since 2019-09-09
 * @version v1.0
 */
object HandlerUtils extends LazyLogging {

  /**
   * key-value一一对应的查询参数
   *
   * @param exchange
   */
  def getQueryParams(exchange: HttpServerExchange) = {
    val paramsMaps = exchange.getQueryParameters
    val scalaMaps = new mutable.HashMap[String, String]
    val querys = paramsMaps.keySet()
    querys.forEach(x => scalaMaps.+=(x -> Try(paramsMaps.get(x).getFirst).getOrElse("")))
    scalaMaps.filter(x => x._2 != "")
    scalaMaps.toMap
  }

  def isLogin(exchange: HttpServerExchange, token: String) = {
    val session = SessionBuilder.getOrCreateSession(exchange)
    logger.info(s"session-id: ${session.getId}")
    val sessionUser = session.getAttribute(token).asInstanceOf[SeckillUser]
    val user = if (VerifyEmpty.noEmpty(sessionUser)) {
      logger.info(s"current user in session: $sessionUser by token: $token")
      session.setAttribute(token, sessionUser)
      sessionUser
    }
    else {
      SeckillUserService.getByToken(exchange, token) match {
        case Some(u) =>
          logger.info(s"current user in redis cache: $u")
          session.setAttribute(token, u)
          u
        case None =>
          session.invalidate(exchange)
          throw GlobalException(CodeMsg.TOKEN_ERROR) //无效token
      }
    }
    logger.info(s"current user: ${user}")
    user
  }

  /**
   * 从cookie取
   */
  def getCookieValueByName(exchange: HttpServerExchange, cookieName: String) = {
    val cookies = exchange.getRequestCookies.asScala
    if (VerifyEmpty.empty(cookies) || cookies.size <= 0) null
    else {
      cookies.find(k => k._1.equals(cookieName)) match {
        case Some(kvCookie) => kvCookie._2.getValue
        case None => null
      }
    }
  }
}
