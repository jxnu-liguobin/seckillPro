package io.github.seckillPro.serializer

import java.lang.reflect.Type

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.seckillPro.entity.{SeckillOrder, SeckillUser}

/**
 * gson序列化
 *
 * @author 梦境迷离
 * @time 2019-08-06
 * @version v2.0
 */
object GsonSerializerAdapter {

  final lazy private val seckillOrder: Type = new TypeToken[SeckillOrder]() {}.getType
  final lazy private val seckillUser: Type = new TypeToken[SeckillUser]() {}.getType
  final lazy private val gs = getGsonBuilder.
    registerTypeAdapter(seckillOrder, SeckillOrderSerialize).
    registerTypeAdapter(seckillUser, SeckillUserSerialize).
    create()

  def getGson = gs


  private final lazy val getGsonBuilder = new GsonBuilder()
}
