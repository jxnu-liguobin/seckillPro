package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.{ DefaultRestfulHandler, SessionBuilder }
import io.github.dreamy.seckill.presenter.{ CodeMsg, LoginVo }
import io.github.dreamy.seckill.service.SeckillUserService
import io.github.dreamy.seckill.util.VerifyEmpty
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try


/**
 * 登录，目前这里因为没有页码，所以反回Result.success(token)表示成功
 *
 * @author 梦境迷离
 * @since 2019-08-28
 * @version v1.0
 */
class LoginHandler extends DefaultRestfulHandler {
  override def route: String = "/login"

  override def methods: Set[String] = multi(Methods.POST_STRING, Methods.GET_STRING)

  override def get(exchange: HttpServerExchange): Future[Any] = {
    Future {
      result("登录页面")
    }.elapsed("模拟打开登录页面")
  }

  /**
   * 携带的token直接反回，否则登录，生成新token，会覆盖原有cookie中的token
   *
   * cookie和redis都是7天，session是基于memory，目前没有使用redis
   *
   * @param exchange
   * @return
   */
  override def post(exchange: HttpServerExchange): Future[Any] = {
    val session = SessionBuilder.getOrCreateSession(exchange)
    logger.info(s"session-id-request: ${session.getId}")
    //携带cookie的请求会校验token是否有效，有效的获取到用户信息，并刷新session中的token-user，反回token（反回token只是用于测试）
    val token = Try(exchange.getRequestCookies.get(SeckillUserService.COOKI_NAME_TOKEN).getValue).getOrElse("")
    if (VerifyEmpty.noEmpty(token)) {
      logger.info(s"token: ${token}")
      val sessionUser = session.getAttribute(token).asInstanceOf[SeckillUser]
      if (VerifyEmpty.noEmpty(sessionUser)) {
        session.setAttribute(token, sessionUser)
        Future.successful(result(token))
      } else {
        //redis token 30天，cookie token 7天，cookie JSESSIONID 30分钟，session 30分钟
        //登录成功会刷新cookie中的JSESSIONID、token和redis中的token的过期时间，其中JSESSIONID与session过期时间一致
        //当cookie中的token还没有过期，但是session的cookie过期了，则会查redis，若redis的过期了但是token却没有过期，则认为是错误的token
        SeckillUserService.getByToken(exchange, token) match {
          case Some(user) => session.setAttribute(token, user)
            Future.successful(result(token))
          case None =>
            session.invalidate(exchange)
            throw GlobalException(CodeMsg.TOKEN_ERROR) //无效token
        }
      }
    }
    else {
      val loginVo = LoginVo(getFormDataByName(exchange, "mobile"), getFormDataByName(exchange, "password"))
      //使用raw或form-data提交得到的是LinkedHashMap，value是Dueue
      SeckillUserService.login(exchange, loginVo).map { token =>
        result(token)
      }.elapsed("模拟用户登录表单提交")
    }
  }
}
