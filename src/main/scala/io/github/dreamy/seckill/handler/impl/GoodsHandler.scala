package io.github.dreamy.seckill.handler.impl

import java.util

import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.presenter.GoodsVo._
import io.github.dreamy.seckill.presenter.{ CodeMsg, GoodsDetailPresenter, GoodsVo }
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.GoodsKey
import io.github.dreamy.seckill.service.{ GoodsService, SeckillUserService }
import io.github.dreamy.seckill.util.CustomConversions._
import io.github.dreamy.seckill.util.HandlerUtils._
import io.github.dreamy.seckill.util.{ ConditionUtils, VerifyEmpty }
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods
import play.api.libs.json.Json

import scala.collection.JavaConverters._
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
    //TODO handler的逻辑多了
    def list() = {
      val token = getCookieValueByName(exchange, SeckillUserService.COOKI_NAME_TOKEN)
      val user = isLogin(exchange, token)
      if (VerifyEmpty.noEmpty(user)) {
        val goodsVos = RedisService.get(GoodsKey.getGoodsList, "", classOf[util.LinkedList[GoodsVo]])
        goodsVos match {
          case g if VerifyEmpty.noEmpty(g) => Future {
            resultSuccess(Json.toJson(toJsonObjSeq(goodsVos)))
          }
          case g if VerifyEmpty.empty(g) =>
            for {
              goodsVoList <- GoodsService.listGoodsVo()
            } yield {
              RedisService.set(GoodsKey.getGoodsList, "", goodsVoList.asJava)
              resultSuccess(Json.toJson(goodsVoList))
            }
        }
      } else {
        tokenNotFound
      }
    }

    def detail() = {
      val goodsIdStr = getQueryParamValue(exchange, "goodsId").getOrElse("-1")
      //不加L无法推断类型
      val goodsId = Try(goodsIdStr.toLong).getOrElse(-1L)
      val token = getCookieValueByName(exchange, SeckillUserService.COOKI_NAME_TOKEN)
      val user = isLogin(exchange, token)
      if (VerifyEmpty.noEmpty(user)) {
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
          resultSuccess(Json.toJson(GoodsDetailPresenter(seckillStatus, remainSeconds, goodsVoOpt.get, user)))
        }
      } else {
        tokenNotFound
      }
    }

    getQueryParamValue(exchange, "type") match {
      case Some("list") => list().elapsed("查询商品列表")
      case Some("detail") => detail().elapsed("查询单个商品详情")
      case _ => Future.successful(resultSuccess("不支持的查询操作")).elapsed("非法请求")
    }
  }
}
