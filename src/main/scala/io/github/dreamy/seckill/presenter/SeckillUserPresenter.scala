package io.github.dreamy.seckill.presenter

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.util.CustomConversions._
import play.api.libs.json.{ Json, Writes }

/**
 * 登录后的用户视图
 *
 * @author 梦境迷离
 * @since 2019-09-01
 * @version v1.0
 */
case class SeckillUserPresenter(seckillUser: SeckillUser, token: String)

object SeckillUserPresenter {
  implicit val writer: Writes[SeckillUserPresenter] = (seckillUserPresenter: SeckillUserPresenter) => {
    val seckillUser = seckillUserPresenter.seckillUser
    //不显示密码、盐、注册时间
    Json.obj(
      "id" -> seckillUser.id.toHashOpt,
      "token" -> seckillUserPresenter.token,
      "nickname" -> seckillUser.nickname,
      "head" -> seckillUser.head,
      "loginCount" -> seckillUser.loginCount,
      "lastLoginDate" -> seckillUser.lastLoginDate.toStrOpt
    ).removeNull
  }
}
