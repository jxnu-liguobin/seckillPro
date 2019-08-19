package io.github.dreamy.seckill.presenter

import io.github.dreamy.seckill.entity.OrderInfo

/**
 * 订单详情视图对象对象
 *
 * @author 梦境迷离
 * @time 2019年8月2日
 * @version v2.0
 */
case class OrderDetailVo(goods: GoodsVo, order: OrderInfo)