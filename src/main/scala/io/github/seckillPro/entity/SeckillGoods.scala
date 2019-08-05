package io.github.seckillPro.entity

import java.time.LocalDateTime

import io.github.seckillPro.util.ImplicitUtils
import play.api.libs.json.{Json, Writes}

/**
 * 秒杀商品
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class SeckillGoods(id: Option[Long], goodsId: Option[Long], stockCount: Int, seckillPrice: Double,
                        startDate: Option[LocalDateTime] = Option(LocalDateTime.now()),
                        endDate: Option[LocalDateTime] = Option(LocalDateTime.now()))

object SeckillGoods {

  implicit val writer: Writes[SeckillGoods] = (s: SeckillGoods) => Json.obj(
    "id" -> s.id,
    "goodsId" -> s.goodsId,
    "stockCount" -> s.stockCount,
    "seckillPrice" -> s.seckillPrice,
    "startDate" -> ImplicitUtils.toStr(s.startDate),
    "endDate" -> ImplicitUtils.toStr(s.endDate),
  )
}