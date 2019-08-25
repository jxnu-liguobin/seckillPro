package io.github.dreamy.seckill.entity

import java.time.LocalDateTime

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