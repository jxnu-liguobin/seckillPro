package io.github.seckillPro.entity

import java.time.LocalDateTime

import io.github.seckillPro.util.ImplicitUtils

/**
 * 秒杀用户
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class SeckillUser(id: Long, nickname: String, password: String, salt: String, head: String,
                       loginCount: Int, registerDate: Option[LocalDateTime] = Option(LocalDateTime.now()),
                       lastLoginDate: Option[LocalDateTime] = Option(LocalDateTime.now())) {
  override def toString: String = {
    s"SeckillUser:[id:[$id],nickname:[$nickname],password:[$password],salt:[$salt],head:[$head],loginCount:[$loginCount]," +
      s"registerDate:[${ImplicitUtils.toStr(registerDate)}],lastLoginDate:[${ImplicitUtils.toStr(lastLoginDate)}]]"
  }
}