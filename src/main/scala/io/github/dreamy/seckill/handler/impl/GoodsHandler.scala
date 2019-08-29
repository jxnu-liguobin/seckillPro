package io.github.dreamy.seckill.handler.impl

import java.util

import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.{ DefaultRestfulHandler, SessionBuilder }
import io.github.dreamy.seckill.presenter.GoodsVo._
import io.github.dreamy.seckill.presenter.{ CodeMsg, GoodsDetailPresenter, GoodsVo, Result }
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.GoodsKey
import io.github.dreamy.seckill.service.GoodsService
import io.github.dreamy.seckill.util.CustomConversions._
import io.github.dreamy.seckill.util.{ ConditionUtils, VerifyEmpty }
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Try


/**
 * 商品控制器，有点复杂，未完全测试（you dian luan）
 *
 * @author 梦境迷离
 * @since 2019-08-25
 * @version v1.0
 */
class GoodsHandler extends DefaultRestfulHandler {
  //detail or list
  override def route: String = "/goods/{type}"

  override def methods: Set[String] = single(Methods.GET_STRING)

  //TODO 对于秒杀的所有接口，有没有必要再使用Result需要考虑。
  override def get(exchange: HttpServerExchange): Future[Any] = {
    val sessionNotFound = Future {
      Json.toJson(Result.error(CodeMsg.SESSION_ERROR))
    }

    //TODO handler的逻辑多了
    def list() = {
      import scala.collection.JavaConverters._
      SessionBuilder.getSession(exchange) match {
        case Some(session) =>
          val goodsVos = RedisService.get(GoodsKey.getGoodsList, "", classOf[util.LinkedList[GoodsVo]])
          val user = session.getAttribute("user").asInstanceOf[SeckillUser]
          logger.info("list invoke, user: " + user)
          (goodsVos, user) match {
            case (_, u) if VerifyEmpty.empty(u) => sessionNotFound
            case (g, u) if !VerifyEmpty.oneEmpty(Seq(g, u): _*) => Future {
              Json.toJson(Result.success(Json.toJson(toJsonObjSeq(goodsVos))))
            }
            case (g, u) if VerifyEmpty.empty(g) && VerifyEmpty.noEmpty(u) =>
              for {
                goodsVoList <- GoodsService.listGoodsVo()
              } yield {
                RedisService.set(GoodsKey.getGoodsList, "", goodsVoList.asJava)
                Json.toJson(Result.success(Json.toJson(goodsVoList)))
              }
          }
        case None => sessionNotFound
      }
    }

    def detail() = {
      val goodsIdStr = Try(exchange.getQueryParameters.get("goodsId").getFirst).getOrElse("-1")
      //不加L无法推断类型
      val goodsId = Try(goodsIdStr.toLong).getOrElse(-1L)
      SessionBuilder.getSession(exchange) match {
        case Some(session) =>
          val user = session.getAttribute("user").asInstanceOf[SeckillUser]
          logger.info("detail invoke, user: " + user)
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
        case None => sessionNotFound
      }
    }

    Try(exchange.getQueryParameters.get("type").getFirst).getOrElse("list") match {
      case "list" => list().elapsed("查询商品列表")
      case "detail" => detail().elapsed("查询单个商品详情")
    }
  }
}
