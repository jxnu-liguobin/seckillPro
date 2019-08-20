package io.github.dreamy.seckill.entity

import java.time.LocalDateTime

import io.github.dreamy.seckill.util.ImplicitUtils
import play.api.libs.json.{ Json, Writes }

/**
 * 秒杀用户
 *
 * id 不是自增的，可以是手机号
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class SeckillUser(id: Option[Long], nickname: String, password: String, salt: String, head: String,
                       loginCount: Int, registerDate: Option[LocalDateTime] = Option(LocalDateTime.now()),
                       lastLoginDate: Option[LocalDateTime] = Option(LocalDateTime.now()))

object SeckillUser {

  implicit val writer: Writes[SeckillUser] = (s: SeckillUser) => Json.obj(
    "id" -> s.id,
    "nickname" -> s.nickname,
    "password" -> s.password,
    "salt" -> s.salt,
    "head" -> s.head,
    "loginCount" -> s.loginCount,
    "registerDate" -> ImplicitUtils.toStr(s.registerDate),
    "lastLoginDate" -> ImplicitUtils.toStr(s.lastLoginDate),
  )
}