package io.github.dreamy.seckill.presenter

import java.time.LocalDateTime

import io.github.dreamy.seckill.entity.Goods
import io.github.dreamy.seckill.util.{ ImplicitUtils, MD5Utils }
import play.api.libs.json.{ Json, Writes }

/**
 * 商品视图对象
 *
 * case class 不能继承
 *
 * @author 梦境迷离
 * @time 2019年8月2日
 * @version v2.0
 */
case class GoodsVo (goods: Goods, seckillPrice: Double, stockCount: Int,
                    startDate: Option[LocalDateTime] = Option(LocalDateTime.now()), endDate: Option[LocalDateTime] = Option(LocalDateTime.now()))

object GoodsVo {
  implicit val writer: Writes[GoodsVo] = (goodsVo: GoodsVo) => {
    val goodsV = Json.obj(
      //含秒杀库存，秒杀价格，不显示实际总库存
      "id" -> MD5Utils.md5(goodsVo.goods.id.getOrElse(-1).toString),
      "goodsName" -> goodsVo.goods.goodsName,
      "goodsTitle" -> goodsVo.goods.goodsTitle,
      "goodsImg" -> goodsVo.goods.goodsImg,
      "goodsDetail" -> goodsVo.goods.goodsDetail,
      "goodsPrice" -> goodsVo.goods.goodsPrice,
      "stockCount" -> goodsVo.stockCount,
      "seckillPrice" -> goodsVo.seckillPrice,
      "startDate" -> ImplicitUtils.toStr(goodsVo.startDate),
      "endDate" -> ImplicitUtils.toStr(goodsVo.endDate)
    )
    Json.obj(
      "goodsVo" -> goodsV
    )
  }

}