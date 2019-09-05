package io.github.dreamy.seckill.handler.impl

import io.github.dreamy.seckill.disruptor.SeckillMessageQueueServer
import io.github.dreamy.seckill.exception.GlobalException
import io.github.dreamy.seckill.http.DefaultRestfulHandler
import io.github.dreamy.seckill.presenter.CodeMsg
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.{ GoodsKey, OrderKey, SeckillKey }
import io.github.dreamy.seckill.service.{ GoodsService, OrderService, SeckillService, SeckillUserService }
import io.github.dreamy.seckill.util.{ ConditionUtils, VerifyEmpty, VerifyImage }
import io.undertow.server.HttpServerExchange
import io.undertow.util.Methods

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


/**
 * 秒杀具体
 *
 * @author 梦境迷离
 * @since 2019-09-03
 * @version v1.0
 */
class SeckillHandler extends DefaultRestfulHandler {

  /**
   * 系统初始化加载商品库存到redis中
   */
  private final lazy val localOverMap = new mutable.HashMap[Long, Boolean]()

  private val default_count = 10

  override def route: String = "/seckill/seckilled"

  override def methods: Set[String] = multi(Methods.GET_STRING, Methods.POST_STRING)

  override def get(exchange: HttpServerExchange): Future[Any] = {
    //TODO 获取用户和token迁出handler，使用独立的授权方法
    val token = getCookieValueByName(exchange, SeckillUserService.COOKI_NAME_TOKEN)
    val user = isLogin(exchange, token)
    if (VerifyEmpty.noEmpty(user)) {
      val goodsId = getQueryParamValue(exchange, "goodsId").getOrElse("-1").toLong
      val verifyCode = getQueryParamValue(exchange, "verifyCode").getOrElse("-1").toInt
      VerifyImage.checkVerifyCode(user, goodsId, verifyCode) match {
        case true => SeckillService.createSeckillPath(user, goodsId).map {
          path => Future.successful(resultSuccess(path.getOrElse("get seckill path failed"))).elapsed("获取真实秒杀地址")
        }
        case false => throw GlobalException(CodeMsg.REQUEST_ILLEGAL)
      }
    } else {
      tokenNotFound
    }
  }

  override def put(exchange: HttpServerExchange): Future[Any] = {
    getQueryParamValue(exchange, "rest").getOrElse("true").toBoolean match {
      case true =>
        val goodsVosListFuture = GoodsService.listGoodsVo()
        goodsVosListFuture.map { goodsVos =>
          goodsVos.foreach { goodsVo =>
            goodsVo.copy(stockCount = default_count)
            RedisService.set(GoodsKey.getSeckillGoodsStock, "" + goodsVo.goods.id.getOrElse(-1L), 10)
            localOverMap.put(goodsVo.goods.id.getOrElse(-1L), false)
          }
          logger.info(s"rest count: ${goodsVos.size}")
        }
        RedisService.delete(OrderKey.getSeckillOrderByUidGid)
        RedisService.delete(SeckillKey.isGoodsOver)
        goodsVosListFuture.map(SeckillService.reset(_))
        Future.successful(resultSuccess("true")).elapsed("重置秒杀")
      case false => Future.successful(resultSuccess("不支持的更新操作")).elapsed("非法请求")
    }
  }

  override def post(exchange: HttpServerExchange): Future[Any] = {
    val token = getCookieValueByName(exchange, SeckillUserService.COOKI_NAME_TOKEN)
    val user = isLogin(exchange, token)
    val goodsId = getQueryParamValue(exchange, "goodsId").getOrElse("-1").toLong
    val path = getQueryParamValue(exchange, "path").getOrElse("invalid path")
    if (VerifyEmpty.noEmpty(user)) {
      // 验证path
      (for {
        pathFuture <- SeckillService.checkPath(user, goodsId, path)
        // 验证path
        _ <- ConditionUtils.failCondition(pathFuture, GlobalException(CodeMsg.REQUEST_ILLEGAL))
        // 内存标记，减少redis访问
        over = localOverMap.get(goodsId)
        _ <- ConditionUtils.failCondition(over.isDefined && over.get, GlobalException(CodeMsg.REQUEST_ILLEGAL))
        // 尝试性的减小库存【预减库存】
        stock = RedisService.decr(GoodsKey.getSeckillGoodsStock, "" + goodsId) // 10
        // 内存标记，减少redis访问
        _ <- ConditionUtils.failConditionWithAction(stock < 0, GlobalException(CodeMsg.REQUEST_ILLEGAL))(goodsId, (goodsIdArgs: Long) => {
          localOverMap.put(goodsIdArgs, true)
          Unit
        })
        seckillOrderOpt <- OrderService.getSeckillOrderByUserIdGoodsId(user.id.getOrElse(-1), goodsId)
        // 判断是否已经秒杀到了
        _ <- ConditionUtils.failCondition(seckillOrderOpt.isDefined, GlobalException(CodeMsg.REPEATE_SECKILL))
      } yield {
        // 入队
        SeckillMessageQueueServer.getSeckillProducer().seckill(goodsId, user)
        resultSuccess("0")
      }).elapsed("开始创建秒杀任务")
    } else {
      tokenNotFound
    }
  }
}
