package io.github.dreamy.seckill.http

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
   *
   * @param exchange
   * @return
   */
  def getNewSession(exchange: HttpServerExchange) = {
    val sessionManager: SessionManager = exchange.getAttachment(SessionManager.ATTACHMENT_KEY)
    val sessionConfig: SessionConfig = exchange.getAttachment(SessionConfig.ATTACHMENT_KEY)
    if (sessionManager == null) throw UndertowMessages.MESSAGES.sessionManagerNotFound
    sessionManager.createSession(exchange, sessionConfig)
  }

  /**
   * 获取session
   *
   * @param exchange
   * @return
   */
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
    sessionManager.setDefaultSessionTimeout(30 * 6)
    new SessionAttachmentHandler(next, sessionManager, sessionConfig)
  }
}
