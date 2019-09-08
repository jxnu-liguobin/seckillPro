package io.github.dreamy.seckill.entity

/**
 * 商品
 * TODO id hash显示
 *
 * @author 梦境迷离
 * @since 2019年8月1日
 * @version v2.0
 */
case class Goods(id: Option[Long], goodsName: String, goodsTitle: String, goodsImg: String, goodsDetail: String,
                 goodsPrice: Double, goodsStock: Int)