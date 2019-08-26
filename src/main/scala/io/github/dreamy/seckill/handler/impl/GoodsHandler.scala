package io.github.dreamy.seckill.handler.impl

import java.util
import java.util.concurrent.LinkedBlockingDeque

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.presenter.GoodsVo._
import io.github.dreamy.seckill.presenter.{ CodeMsg, GoodsDetailPresenter, GoodsVo, Result }
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.GoodsKey
import io.github.dreamy.seckill.service.GoodsService
import io.github.dreamy.seckill.util.CustomConversions._
import io.github.dreamy.seckill.util.{ ConditionUtils, MD5Utils, VerifyEmpty }
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.Json

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
      val goodsVos = RedisService.get(GoodsKey.getGoodsList, "", classOf[util.LinkedList[GoodsVo]])
      (goodsVos, user) match {
        //商品缓冲和用户会话都不存在
        case (_, u) if VerifyEmpty.empty(u) => Future {
          Json.toJson(Result.error(CodeMsg.SESSION_ERROR))
        }
        //商品缓冲和用户会话都存在
        case (g, u) if !VerifyEmpty.oneEmpty(Seq(g, u): _*) => Future {
          Json.toJson(Result.success(Json.toJson(toJsonObjSeq(goodsVos))))
        }
        //商品缓冲不在，但是用户会话存在
        case (g, u) if VerifyEmpty.empty(g) && VerifyEmpty.noEmpty(u) =>
          for {
            goodsVoList <- GoodsService.listGoodsVo()
          } yield {
            RedisService.set(GoodsKey.getGoodsList, "", goodsVoList.sortBy(x => x.goods.id.get).asJava)
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
        val now = System.currentTimeMillis()
        var seckillStatus = 0
        var remainSeconds: Long = 0
        if (now < goodsVoOpt.get.startDate.toLong) { // 秒杀还没开始，倒计时
          seckillStatus = 0
          remainSeconds = ((goodsVoOpt.get.startDate.toLong - now) / 1000)
        } else if (now > goodsVoOpt.get.endDate.toLong) { // 秒杀已经结束
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
