package io.github.seckillPro.serializer

import java.lang.reflect.Type

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.github.seckillPro.entity.SeckillOrder

/**
 * gson序列化
 *
 * @author 梦境迷离
 * @time 2019-08-06
 * @version v2.0
 */
object GsonSerializerAdapter {

  final lazy private val IntListType: Type = new TypeToken[SeckillOrder]() {}.getType
  final lazy private val gs = getGsonBuilder.registerTypeAdapter(IntListType, SeckillOrderSerialize).create

  def getGson = gs


  private final lazy val getGsonBuilder = new GsonBuilder()
}
