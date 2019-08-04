package io.github.seckillPro.entity

import java.time.LocalDateTime

import io.github.seckillPro.util.ImplicitUtils

/**
 * 订单信息
 *
 * deliveryAddrId交付地址先设置为可选的
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class OrderInfo(id: Long, userId: Long, goodsId: Long, deliveryAddrId: Option[Long], goodsName: String, goodsCount: Int,
                     goodsPrice: Double, orderChannel: Int, status: Int, createDate: Option[LocalDateTime] = Option(LocalDateTime.now()),
                     payDate: Option[LocalDateTime] = None) {

  override def toString: String = {
    s"OrderInfo:[id:[$id],userId:[$userId],goodsId:[$goodsId],deliveryAddrId:[$deliveryAddrId],goodsName:[$goodsName]," +
      s"goodsCount:[$goodsCount],goodsPrice:[$goodsPrice],orderChannel:[$orderChannel],status:[$status]," +
      s"createDate:[${ImplicitUtils.toStr(createDate)}],payDate:[${ImplicitUtils.toStr(payDate)}]]"
  }
}