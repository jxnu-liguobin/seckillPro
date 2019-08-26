package io.github.dreamy.seckill.presenter

import java.time.LocalDateTime
import java.util

import com.google.gson.internal.LinkedTreeMap
import io.github.dreamy.seckill.entity.Goods
import io.github.dreamy.seckill.util.CustomConversions._
import play.api.libs.json.{ JsObject, Json, Writes }

/**
 * 商品视图对象
 *
 * case class 不能继承
 *
 * @author 梦境迷离
 * @time 2019年8月2日
 * @version v2.0
 */
case class GoodsVo (goods: Goods, seckillPrice: Double, stockCount: Int,
                    startDate: Option[LocalDateTime] = Option(LocalDateTime.now()), endDate: Option[LocalDateTime] = Option(LocalDateTime.now()))

object GoodsVo {

  def toJsonObjSeq (goodsVos: util.LinkedList[GoodsVo]) = {
    //这个goodsVos是gson序列化的,这里直接使用play-json会出问题
    //原方案这里仅缓冲html，目前是缓冲了整个商品列表
    //val gson = GsonSerializerAdapter.getGson.toJson(goodsVos) //临时使用gson转换再使用play-json,可能有性能问题且无法hash id
    //使用fasterxml也会有这个问题，应该是List中的类型无法自动转化
    val array = new util.LinkedList[JsObject]() //同时需要保证顺序

    /**
     * 根据前面问题，还是改用gson
     * 这里巨坑，直接使用foreach或asScala会因为丢失泛型类型，从TreeMap转化到GoodsVo而报错，并且这个错误直到转化为Scala代码时才会被发现，打印goodsVos无法发现有什么问题
     * 而且gson将所有数值转化为了Double，Option中隐式转化也失效
     */
    for (g <- 0 until goodsVos.size()) {
      val goodsVo = goodsVos.get(g).asInstanceOf[LinkedTreeMap[String, _ <: Any]]
      val goodsMaps = goodsVo.get("goods").asInstanceOf[LinkedTreeMap[String, _ <: Any]]
      val stockCount = goodsVo.get("stockCount").asInstanceOf[Double]
      val seckillPrice = goodsVo.get("seckillPrice").asInstanceOf[Double]
      val startDate = goodsVo.get("startDate").asInstanceOf[Double]
      val endDate = goodsVo.get("endDate").asInstanceOf[Double]
      val goodsVoJson = Json.obj(
        //含秒杀库存，秒杀价格，不显示实际总库存
        "id" -> goodsMaps.get("id").asInstanceOf[Double].toHash,
        "goodsName" -> goodsMaps.get("goodsName").asInstanceOf[String],
        "goodsTitle" -> goodsMaps.get("goodsTitle").asInstanceOf[String],
        "goodsImg" -> goodsMaps.get("goodsImg").asInstanceOf[String],
        "goodsDetail" -> goodsMaps.get("goodsDetail").asInstanceOf[String],
        "goodsPrice" -> goodsMaps.get("goodsPrice").asInstanceOf[Double],
        "stockCount" -> stockCount,
        "seckillPrice" -> seckillPrice,
        "startDate" -> Option(startDate.toLong).toLocalDateTimeOpt.toStrOpt,
        "endDate" -> Option(endDate.toLong).toLocalDateTimeOpt.toStrOpt
      )
      array.add(goodsVoJson)
    }
    import scala.collection.JavaConverters._
    array.asScala
  }

  implicit val writer: Writes[GoodsVo] = (goodsVo: GoodsVo) => {
    Json.obj(
      //含秒杀库存，秒杀价格，不显示实际总库存
      "id" -> goodsVo.goods.id.getOrElse(-1L).asInstanceOf[Double].toHash, //hash ID类型需要一致才行，上面得到的是gson的double
      "goodsName" -> goodsVo.goods.goodsName,
      "goodsTitle" -> goodsVo.goods.goodsTitle,
      "goodsImg" -> goodsVo.goods.goodsImg,
      "goodsDetail" -> goodsVo.goods.goodsDetail,
      "goodsPrice" -> goodsVo.goods.goodsPrice,
      "stockCount" -> goodsVo.stockCount,
      "seckillPrice" -> goodsVo.seckillPrice,
      "startDate" -> goodsVo.startDate.toStrOpt,
      "endDate" -> goodsVo.endDate.toStrOpt
    )
  }
}