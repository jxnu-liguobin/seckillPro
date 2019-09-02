package io.github.dreamy.seckill.presenter

import io.github.dreamy.seckill.entity.OrderInfo
import io.github.dreamy.seckill.util.CustomConversions._
import play.api.libs.json.{ Json, Writes }

/**
 * 订单详情视图对象对象
 *
 * @author 梦境迷离
 * @since 2019年8月2日
 * @version v2.0
 */
case class OrderDetailPresenter(goodsVo: GoodsVo, orderInfo: OrderInfo)

object OrderDetailPresenter {

  implicit val writer: Writes[OrderDetailPresenter] = (orderDetailPresenter: OrderDetailPresenter) => {

    val orderInfo = orderDetailPresenter.orderInfo
    val orderInfoObj = Json.obj(
      "id" -> orderInfo.id.toHashOpt,
      "userId" -> orderInfo.userId.toHashOpt,
      "goodsId" -> orderInfo.goodsId.getOrElse(-1L).asInstanceOf[Double].toHash, //兼容目前GoodsVo的问题
      "deliveryAddrId" -> orderInfo.deliveryAddrId.toHashOpt,
      "goodsName" -> orderInfo.goodsName,
      "goodsCount" -> orderInfo.goodsCount, //秒杀默认商品数量是1
      "goodsPrice" -> orderInfo.goodsPrice,
      "orderChannel" -> orderInfo.orderChannel.toHash,
      "status" -> orderInfo.status,
      "createDate" -> orderInfo.createDate.toStrOpt,
      "payDate" -> orderInfo.payDate.toStrOpt
    ).removeNull
    Json.obj(
      "goodsVo" -> orderDetailPresenter.goodsVo,
      "orderInfo" -> orderInfoObj
    )
  }

}