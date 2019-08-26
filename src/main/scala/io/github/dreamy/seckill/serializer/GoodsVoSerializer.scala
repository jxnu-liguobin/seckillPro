package io.github.dreamy.seckill.serializer

import java.lang.reflect.Type

import com.google.gson._
import io.github.dreamy.seckill.entity.Goods
import io.github.dreamy.seckill.presenter.GoodsVo
import io.github.dreamy.seckill.util.CustomConversions._

/**
 * 商品视图
 *
 * TODO 太麻烦了
 *
 * @author 梦境迷离
 * @since 2019-08-25
 * @version v1.0
 */
class GoodsVoSerializer extends JsonSerializer[GoodsVo] with JsonDeserializer[GoodsVo] {
  override def serialize (src: GoodsVo, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val goods = new JsonObject
    //getOrElse后无法推断类型了，bug
    goods.addProperty("id", src.goods.id.getOrElse(-1L))
    goods.addProperty("goodsName", src.goods.goodsName)
    goods.addProperty("goodsTitle", src.goods.goodsTitle)
    goods.addProperty("goodsImg", src.goods.goodsImg)
    goods.addProperty("goodsDetail", src.goods.goodsDetail)
    goods.addProperty("goodsPrice", src.goods.goodsPrice)
    goods.addProperty("goodsStock", src.goods.goodsStock)
    val goodsVo = new JsonObject
    goodsVo.add("goods", goods)
    goodsVo.addProperty("seckillPrice", src.seckillPrice)
    goodsVo.addProperty("stockCount", src.stockCount)
    goodsVo.addProperty("startDate", src.startDate.toLong)
    goodsVo.addProperty("endDate", src.endDate.toLong)
    goodsVo
  }


  /**
   * 反序列化时，从集合中反回的是LinkedTreeMap，就算存在此隐式也无法直接被转化为Scala case class
   *
   * @param json
   * @param typeOfT
   * @param context
   * @return
   */
  override def deserialize (json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GoodsVo = {
    import com.google.gson.JsonObject
    val jsonObject: JsonObject = json.getAsJsonObject

    /**
     * "goods":{"id":1,"goodsName":"after update ","goodsTitle":"android手机","goodsImg":"/img/meta10.png","goodsDetail":"android手机","goodsPrice":100.0,"goodsStock":100},
     * "seckillPrice":0.01,"stockCount":0,"startDate":1546300800000,"endDate":1546300800000}
     */
    GoodsVo(
      Goods(Option(
        jsonObject.getAsJsonObject("goods").get("id").getAsLong),
        jsonObject.getAsJsonObject("goods").get("goodsName").getAsString,
        jsonObject.getAsJsonObject("goods").get("goodsTitle").getAsString,
        jsonObject.getAsJsonObject("goods").get("goodsImg").getAsString,
        jsonObject.getAsJsonObject("goods").get("goodsDetail").getAsString,
        jsonObject.getAsJsonObject("goods").get("goodsPrice").getAsDouble,
        jsonObject.getAsJsonObject("goods").get("goodsStock").getAsInt
      ),
      jsonObject.get("seckillPrice").getAsDouble,
      jsonObject.get("stockCount").getAsInt,
      Option(jsonObject.get("startDate").getAsLong).toLocalDateTimeOpt,
      Option(jsonObject.get("endDate").getAsLong).toLocalDateTimeOpt)
  }
}

object GoodsVoSerializer extends GoodsVoSerializer
