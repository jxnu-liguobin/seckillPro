package io.github.seckillPro.entity

import java.time.LocalDateTime

/**
 * 秒杀商品
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class SeckillGoods(id: Long, goodsId: Long, stockCount: Int, seckillPrice: Double,
                        startDate: LocalDateTime = LocalDateTime.now(), endDate: LocalDateTime = LocalDateTime.now())