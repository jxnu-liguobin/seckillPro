package io.github.dreamy.seckill.entity

import java.time.LocalDateTime

/**
 * 订单信息
 *
 * deliveryAddrId交付地址先设置为可选的
 *
 * @author 梦境迷离
 * @since 2019年8月1日
 * @version v2.0
 */
case class OrderInfo(id: Option[Long], userId: Option[Long], goodsId: Option[Long], deliveryAddrId: Option[Long], goodsName: String, goodsCount: Int,
                     goodsPrice: Double, orderChannel: Int, status: Int, createDate: Option[LocalDateTime] = Option(LocalDateTime.now()),
                     payDate: Option[LocalDateTime] = None)
