package io.github.dreamy.seckill.presenter

import java.time.LocalDateTime

import io.github.dreamy.seckill.entity.Goods
import io.github.dreamy.seckill.util.ImplicitUtils

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
                   startDate: Option[LocalDateTime] = Option(LocalDateTime.now()), endDate: Option[LocalDateTime] = Option(LocalDateTime.now())) {

  override def toString: String = {
    s"GoodsVo:[goods:$goods,seckillPrice:$seckillPrice,stockCount:$stockCount," +
      s"startDate:${ImplicitUtils.toStr(startDate)},endDate:${ImplicitUtils.toStr(endDate)}]"
  }
}