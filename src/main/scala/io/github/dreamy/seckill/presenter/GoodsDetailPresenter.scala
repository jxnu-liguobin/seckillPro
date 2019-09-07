package io.github.dreamy.seckill.presenter

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.util.CustomConversions._
import play.api.libs.json.{ Json, Writes }

/**
 * 商品详情
 *
 * presenter用于展示
 *
 * @author 梦境迷离
 * @since 2019-08-25
 * @version v1.0
 */
case class GoodsDetailPresenter(
                                 //秒杀状态
                                 seckillStatus: Int,
                                 //遗留时间
                                 remainSeconds: Long,
                                 //商品视图对象
                                 goodsVo: GoodsVo,
                                 //秒杀用户
                                 user: SeckillUser,
                               )

object GoodsDetailPresenter {

  implicit val writer: Writes[GoodsDetailPresenter] = (goodsDetailPresenter: GoodsDetailPresenter) => {

    val user = Json.obj(
      //不显示密码和盐，注册时间
      "id" -> goodsDetailPresenter.user.id.toHashOpt,
      "nickname" -> goodsDetailPresenter.user.nickname,
      "head" -> goodsDetailPresenter.user.head,
      "lastLoginDate" -> goodsDetailPresenter.user.lastLoginDate.toStrOpt)

    val goodsVo = Json.obj(
      //含秒杀库存，秒杀价格，不显示实际总库存
      "id" -> goodsDetailPresenter.goodsVo.goods.id.toHashOpt,
      "goodsName" -> goodsDetailPresenter.goodsVo.goods.goodsName,
      "goodsTitle" -> goodsDetailPresenter.goodsVo.goods.goodsTitle,
      "goodsImg" -> goodsDetailPresenter.goodsVo.goods.goodsImg,
      "goodsDetail" -> goodsDetailPresenter.goodsVo.goods.goodsDetail,
      "goodsPrice" -> goodsDetailPresenter.goodsVo.goods.goodsPrice,
      "stockCount" -> goodsDetailPresenter.goodsVo.stockCount,
      "seckillPrice" -> goodsDetailPresenter.goodsVo.seckillPrice,
      "startDate" -> goodsDetailPresenter.goodsVo.startDate.toStrOpt,
      "endDate" -> goodsDetailPresenter.goodsVo.endDate.toStrOpt
    )

    Json.obj(
      "seckillStatus" -> goodsDetailPresenter.seckillStatus,
      "remainSeconds" -> goodsDetailPresenter.remainSeconds,
      "user" -> user.removeNull,
      "goodsVo" -> goodsVo.removeNull
    )
  }
}
