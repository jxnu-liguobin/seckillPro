package io.github.dreamy.seckill.serializer

import java.lang.reflect.Type

import com.google.gson._
import io.github.dreamy.seckill.entity.SeckillOrder

/**
 * 自定义SeckillOrder的序列化
 *
 * @author 梦境迷离
 * @since 2019-08-06
 * @version v2.0
 */
class SeckillOrderSerialize extends JsonSerializer[SeckillOrder] with JsonDeserializer[SeckillOrder] {
  override def serialize(seckill: SeckillOrder, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val data = new JsonObject
    val id = seckill.id.getOrElse(-1).asInstanceOf[Number]
    val userId = seckill.userId.getOrElse(-1).asInstanceOf[Number]
    val orderId = seckill.orderId.getOrElse(-1).asInstanceOf[Number]
    val goodsId = seckill.goodsId.getOrElse(-1).asInstanceOf[Number]
    data.addProperty("id", id)
    data.addProperty("userId", userId)
    data.addProperty("orderId", orderId)
    data.addProperty("goodsId", goodsId)
    data
  }

  override def deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SeckillOrder = {
    import com.google.gson.JsonObject
    val jsonObject: JsonObject = json.getAsJsonObject
    SeckillOrder(Option(jsonObject.get("id").getAsLong), Option(jsonObject.get("userId").getAsLong),
      Option(jsonObject.get("orderId").getAsLong), Option(jsonObject.get("goodsId").getAsLong))
  }
}

object SeckillOrderSerialize extends SeckillOrderSerialize
