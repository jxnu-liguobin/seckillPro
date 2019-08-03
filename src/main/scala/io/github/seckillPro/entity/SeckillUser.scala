package io.github.seckillPro.entity

import java.time.LocalDateTime

/**
 * 秒杀用户
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class SeckillUser(id: Long, nickname: String, password: String, salt: String, head: String,
                       loginCount: Int, registerDate: LocalDateTime = LocalDateTime.now(),
                       lastLoginDate: LocalDateTime = LocalDateTime.now())