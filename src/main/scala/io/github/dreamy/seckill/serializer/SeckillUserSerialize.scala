package io.github.dreamy.seckill.serializer

import java.lang.reflect.Type

import com.google.gson._
import io.github.dreamy.seckill.entity
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.util.CustomConversions._

/**
 * 自定义SeckillUser的序列化
 *
 * @author 梦境迷离
 * @time 2019-08-07
 * @version v2.0
 */
class SeckillUserSerialize extends JsonSerializer[SeckillUser] with JsonDeserializer[SeckillUser] {
  override def serialize (seckill: SeckillUser, typeOfSrc: Type, context: JsonSerializationContext): JsonElement = {
    val data = new JsonObject
    val id = seckill.id.getOrElse(-1).asInstanceOf[Number]
    val nickname = seckill.nickname
    val password = seckill.password
    val salt = seckill.salt
    val head = seckill.head
    val loginCount = seckill.loginCount
    val registerDate = seckill.registerDate.toLong
    val lastLoginDate = seckill.lastLoginDate.toLong
    data.addProperty("id", id)
    data.addProperty("nickname", nickname)
    data.addProperty("password", password)
    data.addProperty("salt", salt)
    data.addProperty("head", head)
    data.addProperty("loginCount", loginCount)
    data.addProperty("registerDate", registerDate)
    data.addProperty("lastLoginDate", lastLoginDate)
    data
  }

  override def deserialize (json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): SeckillUser = {
    import com.google.gson.JsonObject
    val jsonObject: JsonObject = json.getAsJsonObject
    entity.SeckillUser(
      Option(jsonObject.get("id").getAsLong),
      jsonObject.get("nickname").getAsString,
      jsonObject.get("password").getAsString,
      jsonObject.get("salt").getAsString,
      jsonObject.get("head").getAsString,
      jsonObject.get("loginCount").getAsInt,
      Option(jsonObject.get("registerDate").getAsLong).toLocalDateTimeOpt,
      Option(jsonObject.get("lastLoginDate").getAsLong).toLocalDateTimeOpt
    )
  }
}

object SeckillUserSerialize extends SeckillUserSerialize