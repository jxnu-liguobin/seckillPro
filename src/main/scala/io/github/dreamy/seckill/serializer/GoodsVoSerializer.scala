package io.github.dreamy.seckill.serializer

import java.lang.reflect.Type

import com.google.gson._
import io.github.dreamy.seckill.entity.Goods
import io.github.dreamy.seckill.presenter.GoodsVo
import io.github.dreamy.seckill.util.ImplicitUtils

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
    goods.addProperty("id", src.goods.id.getOrElse(-1).asInstanceOf[Number])
    goods.addProperty("goodsName", src.goods.goodsName)
    goods.addProperty("goodsTitle", src.goods.goodsTitle)
    goods.addProperty("goodsImg", src.goods.goodsImg)
    goods.addProperty("goodsPrice", src.goods.goodsPrice)
    goods.addProperty("goodsStock", src.goods.goodsStock)
    goods.addProperty("goodsDetail", src.goods.goodsDetail)
    val goodsVo = new JsonObject
    goodsVo.add("goods", goods)
    goodsVo.addProperty("seckillPrice", src.seckillPrice)
    goodsVo.addProperty("stockCount", src.stockCount)
    goodsVo.addProperty("startDate", ImplicitUtils.toLong(src.startDate))
    goodsVo.addProperty("endDate", ImplicitUtils.toLong(src.endDate))
    goodsVo
  }

  override def deserialize (json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): GoodsVo = {
    import com.google.gson.JsonObject
    val jsonObject: JsonObject = json.getAsJsonObject
    GoodsVo(
      Goods(Option(jsonObject.get("id").getAsLong), jsonObject.get("goodsName").getAsString,
        jsonObject.get("goodsTitle").getAsString,
        jsonObject.get("goodsImg").getAsString,
        jsonObject.get("goodsDetail").getAsString,
        jsonObject.get("goodsPrice").getAsDouble,
        jsonObject.get("goodsStock").getAsInt
      ),
      jsonObject.get("seckillPrice").getAsDouble,
      jsonObject.get("stockCount").getAsInt,
      ImplicitUtils.toLocalDateTime(Option(jsonObject.get("startDate").getAsLong)),
      ImplicitUtils.toLocalDateTime(Option(jsonObject.get("endDate").getAsLong)))
  }
}

object GoodsVoSerializer extends GoodsVoSerializer
