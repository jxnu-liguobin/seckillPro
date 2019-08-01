package io.github.seckillPro.entity

import java.time.LocalDateTime

/**
 * 订单信息
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class OrderInfo(id: Long, userId: Long, goodsId: Long, deliveryAddrId: Long, goodsName: String, goodsCount: Int,
                     goodsPrice: Double, orderChannel: Int, status: Int, createDate: LocalDateTime = LocalDateTime.now(),
                     payDate: LocalDateTime = LocalDateTime.now())