package io.github.dreamy.seckill.entity

import java.time.LocalDateTime

/**
 * 秒杀用户
 *
 * id 不是自增的，可以是手机号
 *
 * @author 梦境迷离
 * @since 2019年8月1日
 * @version v2.0
 */
case class SeckillUser(id: Option[Long], nickname: String, password: String, salt: String, head: String,
                       loginCount: Int, registerDate: Option[LocalDateTime] = Option(LocalDateTime.now()),
                       lastLoginDate: Option[LocalDateTime] = Option(LocalDateTime.now()))

object SeckillUser {
  //default
  def default(): SeckillUser = new SeckillUser(Option(-1), "系统",
    "", "", "", 1, Option(LocalDateTime.now()), Option(LocalDateTime.now))
}