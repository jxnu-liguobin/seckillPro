package io.github.dreamy.seckill.handler.impl

import java.util
import java.util.concurrent.LinkedBlockingDeque

import com.google.gson.internal.LinkedTreeMap
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.presenter.{ CodeMsg, GoodsDetailPresenter, GoodsVo, Result }
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.GoodsKey
import io.github.dreamy.seckill.service.GoodsService
import io.github.dreamy.seckill.util.{ ConditionUtils, ImplicitUtils, MD5Utils, VerifyEmpty }
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.{ JsObject, Json }

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try


/**
 * 商品控制器，有点复杂，未完成测试（you dian luan）
 *
 * @author 梦境迷离
 * @since 2019-08-25
 * @version v1.0
 */
class GoodsHandler extends DefaultRestfulHandler {
  //detail or list
  override def route: String = "/goods/{type}"

  override def methods: Set[String] = single(Methods.GET_STRING)

  override def get (exchange: HttpServerExchange): Future[Any] = {
    //TODO handler的逻辑多了
    def list () = {
      //TODO session 有问题，目前仅使用 mock user 走逻辑
      //      val sm = exchange.getAttachment(SessionManager.ATTACHMENT_KEY);
      //      val sessionConfig = exchange.getAttachment(SessionConfig.ATTACHMENT_KEY);
      //      val session = sm.getSession(exchange, sessionConfig) //TODO null
      //      val user = session.getAttribute("user").asInstanceOf[SeckillUser]
      //      logger.info("【商品列表秒杀用户】:" + user)
      import scala.collection.JavaConverters._
      val user = SeckillUser(Option(15312345678L), "user", MD5Utils.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1)
      val goodsVos = RedisService.get(GoodsKey.getGoodsList, "", classOf[util.ArrayList[GoodsVo]])
      (goodsVos, user) match {
        //商品缓冲和用户会话都不存在
        case (_, u) if VerifyEmpty.empty(u) => Future {
          Json.toJson(Result.error(CodeMsg.SESSION_ERROR))
        }
        //商品缓冲和用户会话都存在
        case (g, u) if !VerifyEmpty.oneEmpty(Seq(g, u): _*) => Future {
          //这个goodsVos是gson序列化的,这里直接使用play-json会出问题
          //原方案这里仅缓冲html，目前是缓冲了整个商品列表
          //val gson = GsonSerializerAdapter.getGson.toJson(goodsVos) //临时使用gson转换再使用play-json,可能有性能问题且无法hash id
          val array = ArrayBuffer[JsObject]()

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
              "id" -> MD5Utils.md5(goodsMaps.get("id").asInstanceOf[Double].toString),
              "goodsName" -> goodsMaps.get("goodsName").asInstanceOf[String],
              "goodsTitle" -> goodsMaps.get("goodsTitle").asInstanceOf[String],
              "goodsImg" -> goodsMaps.get("goodsImg").asInstanceOf[String],
              "goodsDetail" -> goodsMaps.get("goodsDetail").asInstanceOf[String],
              "goodsPrice" -> goodsMaps.get("goodsPrice").asInstanceOf[Double],
              "stockCount" -> stockCount,
              "seckillPrice" -> seckillPrice,
              "startDate" -> ImplicitUtils.toStr(ImplicitUtils.toLocalDateTime(Option(startDate.toLong))),
              "endDate" -> ImplicitUtils.toStr(ImplicitUtils.toLocalDateTime(Option(endDate.toLong)))
            )
            array.+=:(goodsVoJson)
          }
          Json.toJson(Result.success(Json.toJson(array)))
        }
        //商品缓冲不在，但是用户会话存在
        case (g, u) if VerifyEmpty.empty(g) && VerifyEmpty.noEmpty(u) =>
          for {
            goodsVoList <- GoodsService.listGoodsVo()
          } yield {
            RedisService.set(GoodsKey.getGoodsList, "", goodsVoList.asJavaCollection)
            Json.toJson(Result.success(Json.toJson(goodsVoList)))
          }
      }
    }

    def detail () = {
      val goodsIdStr = exchange.getQueryParameters.getOrDefault("goodsId", new LinkedBlockingDeque[String]() {
        "-1"
      }).getFirst
      //不加L无法推断类型
      val goodsId = Try(goodsIdStr.toLong).getOrElse(-1L)
      //      val sm = exchange.getAttachment(SessionManager.ATTACHMENT_KEY);
      //      val sessionConfig = exchange.getAttachment(SessionConfig.ATTACHMENT_KEY);
      //      val session = sm.getSession(exchange, sessionConfig) //TODO null
      //      val user = session.getAttribute("user").asInstanceOf[SeckillUser]
      val user = SeckillUser(Option(15312345678L), "user", MD5Utils.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1)
      logger.info("【查看商品详情携带的用户】:" + user)
      val goodsFuture = GoodsService.getGoodsVoByGoodsId(goodsId)
      for {
        goodsVoOpt <- goodsFuture
        _ <- ConditionUtils.failCondition(goodsVoOpt.isEmpty, GlobalException(CodeMsg.SECKILL_OVER))
      } yield {
        import io.github.dreamy.seckill.util.ImplicitUtils._
        val now = System.currentTimeMillis()
        var seckillStatus = 0
        var remainSeconds: Long = 0
        if (now < goodsVoOpt.get.startDate) { // 秒杀还没开始，倒计时
          seckillStatus = 0
          remainSeconds = ((goodsVoOpt.get.startDate - now) / 1000)
        } else if (now > goodsVoOpt.get.endDate) { // 秒杀已经结束
          seckillStatus = 2
          remainSeconds = -1
        } else { // 秒杀进行中
          seckillStatus = 1
          remainSeconds = 0
        }
        //泛型的play-json没搞懂，直接转化后再赋值
        Json.toJson(Result.success(Json.toJson(GoodsDetailPresenter(seckillStatus, remainSeconds, goodsVoOpt.get, user))))
      }
    }

    exchange.getQueryParameters.getOrDefault("type", new LinkedBlockingDeque[String]() {
      "list"
    }).getFirst match {
      case "list" => list().elapsed("查询商品列表")
      case "detail" => detail().elapsed("查询单个商品详情")
    }
  }
}
