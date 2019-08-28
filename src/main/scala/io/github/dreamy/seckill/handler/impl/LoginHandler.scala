package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.{ DefaultRestfulHandler, SessionBuilder }
import io.github.dreamy.seckill.presenter.{ CodeMsg, LoginVo, Result }
import io.github.dreamy.seckill.service.SeckillUserService
import io.github.dreamy.seckill.util.VerifyEmpty
import io.undertow.server.HttpServerExchange
import io.undertow.server.handlers.form.FormDataParser
import io.undertow.util.Methods
import play.api.libs.json.Json

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

  override def methods: Set[String] = single(Methods.POST_STRING)

  /**
   * 携带的token直接反回，否则登录，生成新token，会覆盖原有cookie中的token
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
        Future.successful(Json.toJson(Result.success(token))) //TODO目前不考虑token刷新
      } else {
        SeckillUserService.getByToken(exchange, token) match {
          case Some(user) => session.setAttribute(token, user)
            Future.successful(Json.toJson(Result.success(token))) //TODO目前不考虑token刷新
          case None => throw GlobalException(CodeMsg.TOKEN_ERROR) //无效token
        }
      }
    }
    else {
      val loginVo2 = exchange.getAttachment(FormDataParser.FORM_DATA)
      val loginVo = LoginVo(loginVo2.get("mobile").getFirst.getValue, loginVo2.get("password").getFirst.getValue)
      //使用raw或form-data提交得到的是LinkedHashMap，value是Dueue
      SeckillUserService.login(exchange, loginVo).map { token =>
        Json.toJson(Result.success(token))
      }.elapsed("模拟用户登录表单提交")
    }
  }
}
