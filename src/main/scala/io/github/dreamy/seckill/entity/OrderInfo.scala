package io.github.dreamy.seckill.entity

import java.time.LocalDateTime

import io.github.dreamy.seckill.util.ImplicitUtils
import play.api.libs.json.{ Json, Writes }

/**
 * 订单信息
 *
 * deliveryAddrId交付地址先设置为可选的
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class OrderInfo(id: Option[Long], userId: Option[Long], goodsId: Option[Long], deliveryAddrId: Option[Long], goodsName: String, goodsCount: Int,
                     goodsPrice: Double, orderChannel: Int, status: Int, createDate: Option[LocalDateTime] = Option(LocalDateTime.now()),
                     payDate: Option[LocalDateTime] = None)


object OrderInfo {

  implicit val writer: Writes[OrderInfo] = (o: OrderInfo) => Json.obj(
    "id" -> o.id,
    "userId" -> o.userId,
    "goodsId" -> o.goodsId,
    "deliveryAddrId" -> o.deliveryAddrId,
    "goodsName" -> o.goodsName,
    "goodsCount" -> o.goodsCount,
    "goodsPrice" -> o.goodsPrice,
    "orderChannel" -> o.orderChannel,
    "status" -> o.status,
    "createDate" -> ImplicitUtils.toStr(o.createDate),
    "payDate" -> ImplicitUtils.toStr(o.payDate),
  )
}