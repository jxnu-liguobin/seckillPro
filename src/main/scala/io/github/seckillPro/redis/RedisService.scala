package io.github.seckillPro.redis

import java.util

import com.google.gson.GsonBuilder
import com.typesafe.scalalogging.LazyLogging
import io.github.seckillPro.config.RedisPoolFactory
import io.github.seckillPro.util.{ResourceUtils, VerifyEmpty}
import redis.clients.jedis.{Jedis, ScanParams, ScanResult}

import scala.collection.mutable.ArrayBuffer

/**
 * redis服务
 *
 * @author 梦境迷离
 * @time 2019-08-05
 * @version v2.0
 */
object RedisService extends LazyLogging {

  final lazy private val jedisPool = RedisPoolFactory.JedisPoolFactory()
  final lazy private val gs = new GsonBuilder().serializeNulls().create()

  def get[T](prefix: KeyPrefix, key: String, clazz: Class[T]): T = {
    try {
      ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
        // 生成真正的key
        val realKey = prefix.getPrefix() + key
        val str = jedis.get(realKey)
        logger.info(s"get real key:$realKey")
        RedisService.stringToBean(str, clazz)
      }
    }
  }

  /**
   * 设置对象
   */
  def set[T](prefix: KeyPrefix, key: String, value: T): Boolean = {
    val str = RedisService.beanToString(value)
    if (VerifyEmpty.empty(str)) false else {
      // 生成真正的key
      val realKey = prefix.getPrefix() + key
      val seconds = prefix.expireSeconds()
      ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
        logger.info(s"set real key:$realKey")
        if (seconds <= 0)
          jedis.set(realKey, str)
        else
          jedis.setex(realKey, seconds, str)
        true
      }
    }
  }

  /**
   * 判断key是否存在
   */
  def exists[T](prefix: KeyPrefix, key: String): Boolean = {
    ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
      // 生成真正的key
      val realKey = prefix.getPrefix() + key
      logger.info(s"exists real key:$realKey")
      jedis.exists(realKey)
    }
  }

  /**
   * 增加值
   */
  def incr[T](prefix: KeyPrefix, key: String): Long = {
    ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
      // 生成真正的key
      val realKey = prefix.getPrefix() + key
      logger.info(s"incr real key:$realKey")
      jedis.incr(realKey)
    }
  }

  /**
   * 减少值
   */
  def decr[T](prefix: KeyPrefix, key: String): Long = {
    ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
      // 生成真正的key
      val realKey = prefix.getPrefix() + key
      logger.info(s"decr real key:$realKey")
      jedis.decr(realKey)
    }
  }

  /**
   * 删除
   */
  def delete(prefix: KeyPrefix, key: String): Boolean = {
    ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
      // 生成真正的key
      val realKey = prefix.getPrefix() + key
      logger.info(s"delete real key:$realKey")
      jedis.del(realKey) > 0
    }
  }

  /**
   * 删除
   */
  def delete(prefix: KeyPrefix): Boolean = {
    val keys: util.List[String] = scanKeys(prefix.getPrefix())
    val keyss = new ArrayBuffer[String]()
    keys.forEach(k => keyss.+=:(k))
    if (prefix == null) {
      false
    }
    else if (keys == null || keys.size() <= 0) {
      true
    } else {
      ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
        jedis.del(keyss: _*)
        logger.info(s"delete key prefix:$prefix")
        true
      }
    }
  }

  /**
   * 去除前100 key
   */
  private def scanKeys(key: String): util.List[String] = {
    ResourceUtils.using(jedisPool.getResource) { jedis: Jedis =>
      val keys = new util.ArrayList[String]()
      var cursor = "0"
      val sp: ScanParams = new ScanParams()
      //注意这里不能直接用match
      sp.`match`("*" + key + "*")
      sp.count(100)
      do {
        val ret: ScanResult[String] = jedis.scan(cursor, sp)
        val result: util.List[String] = ret.getResult
        if (result != null && result.size() > 0) {
          keys.addAll(result)
        }
        // 再处理cursor
        cursor = ret.getStringCursor
      } while (!cursor.equals("0"))
      keys
    }
  }

  /**
   * 通用的工具，将bean转化为String
   */
  def beanToString[T](value: T): String = {
    if (value == null) null else {
      val clazz: Class[_ <: T] = value.getClass
      if (clazz == classOf[Int] || clazz == classOf[Integer]) {
        "" + value
      } else if (clazz == classOf[String]) {
        value.asInstanceOf[String]
      } else if (clazz == classOf[Long] || clazz == classOf[Long]) {
        "" + value
      } else {
        gs.toJson(value)
      }
    }
  }

  /**
   * 通用的工具，将String转化为bean
   */
  def stringToBean[T](str: String, clazz: Class[T]): T = {
    /**
     * Scala基本类型就是包装类型,可以说没有原生类型一说
     */
    if (VerifyEmpty.empty(str) || clazz == null) {
      return null.asInstanceOf[T]
    }
    if (clazz == classOf[Int] || clazz == classOf[Integer]) {
      str.toInt.asInstanceOf[T]
    } else if (clazz == classOf[String]) {
      str.asInstanceOf[T]
    } else if (clazz == classOf[Long] || clazz == classOf[Long]) {
      str.toLong.asInstanceOf[T]
    } else {
      gs.fromJson(str, clazz)
    }
  }
}

