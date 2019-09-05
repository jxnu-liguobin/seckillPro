package io.github.dreamy.seckill.util

import scala.concurrent.Future

/**
 * 条件处理工具
 *
 * @author 梦境迷离
 * @version 1.0, 2019-07-15
 */
object ConditionUtils {

  /** for推断中抛出异常
   *
   * {{{
   * for{
   *       _ <- failCondition(user.isEmpty, InternalErrorException(s"user not found ${p.userId}"))
   * }
   * }}}
   *
   * @param condition
   * @param PlayException
   * @return
   */
  def failCondition(condition: Boolean, PlayException: => Exception): Future[Unit] =
    if (condition) Future.failed(PlayException) else Future.successful(())

  /**
   * {{{
   * for {
   *   _ <- ConditionUtils.failConditionWithAction(stock < 0, GlobalException(CodeMsg.REQUEST_ILLEGAL))(goodsId, (goodsIdArgs: Long) => {
   *           localOverMap.put(goodsIdArgs, true)
   *           Unit
   *   })
   * }
   * }}}
   *
   * @param condition
   * @param PlayException
   */
  def failConditionWithAction(condition: Boolean, PlayException: => Exception)(args: Long, f: Long => Unit) = {
    if (condition) {
      f(args)
      Future.failed(PlayException)
    } else Future.successful(())
  }

}
