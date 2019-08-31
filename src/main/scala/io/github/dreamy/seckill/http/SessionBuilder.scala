package io.github.dreamy.seckill.http

import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.presenter.CodeMsg
import io.github.dreamy.seckill.util.VerifyEmpty
import io.undertow.UndertowMessages
import io.undertow.server.session._
import io.undertow.server.{ HttpHandler, HttpServerExchange }

/**
 * session 构造
 *
 * @author 梦境迷离
 * @since 2019-08-28
 * @version v1.0
 */
object SessionBuilder {

  /**
   * 创建session
   * id=tPls5-uHfsMFXyxmaEAmUC0lzAlaX4fTA2B5fG06
   * 超时30分钟
   *
   * @param exchange
   * @return
   */
  def getOrCreateSession(exchange: HttpServerExchange) = {
    val sessionManager: SessionManager = exchange.getAttachment(SessionManager.ATTACHMENT_KEY)
    val sessionConfig: SessionConfig = exchange.getAttachment(SessionConfig.ATTACHMENT_KEY)
    if (sessionManager == null) throw UndertowMessages.MESSAGES.sessionManagerNotFound
    val s = sessionManager.getSession(exchange, sessionConfig)
    val session = if (VerifyEmpty.empty(s)) sessionManager.createSession(exchange, sessionConfig) else s
    if (VerifyEmpty.empty(session)) throw GlobalException(CodeMsg.INTERNAL_ERROR)
    session
  }

  /**
   * 获取session
   *
   * @param exchange
   * @return
   */
  @deprecated
  def getSession(exchange: HttpServerExchange) = {
    val sessionManager: SessionManager = exchange.getAttachment(SessionManager.ATTACHMENT_KEY)
    val sessionConfig: SessionConfig = exchange.getAttachment(SessionConfig.ATTACHMENT_KEY)
    if (sessionManager == null) throw UndertowMessages.MESSAGES.sessionManagerNotFound
    Option(sessionManager.getSession(exchange, sessionConfig))
  }

  /**
   * session管理处理器构造
   *
   * @param next
   * @return
   */
  def sessionManagerBuild(next: HttpHandler) = {
    val sessionConfig = new SessionCookieConfig
    val sessionManager = new InMemorySessionManager("seckill-pro", 10000, true)
    sessionManager.setDefaultSessionTimeout(30 * 60) //内存session保留30分钟
    sessionConfig.setMaxAge(30 * 60) //JSESSIONID保留30分钟
    sessionConfig.setCookieName("SECKILL-SESSION")
    new SessionAttachmentHandler(next, sessionManager, sessionConfig)
  }
}
