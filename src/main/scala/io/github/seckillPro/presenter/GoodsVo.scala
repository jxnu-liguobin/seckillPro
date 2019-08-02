package io.github.seckillPro.presenter

import java.time.LocalDateTime

import io.github.seckillPro.entity.Goods

/**
 * 商品视图对象
 *
 * case class 不能继承
 *
 * @author 梦境迷离
 * @time 2019年8月2日
 * @version v2.0
 */
case class GoodsVo(goods: Goods, seckillPrice: Double, stockCount: Int,
                   startDate: LocalDateTime = LocalDateTime.now(), endDate: LocalDateTime = LocalDateTime.now())