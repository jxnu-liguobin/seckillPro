package io.github.seckillPro.util

import scala.concurrent.{ExecutionContext, Future}
import scala.language.reflectiveCalls
import scala.util.control.Exception._

/**
 * 使用借贷模式管理资源关闭
 *
 * @author 梦境迷离
 * @version 1.0, 2019-07-15
 */
object ResourceUtils {

  /**
   * 必须有close方法才能使用
   */
  type Closable = {
    def close(): Unit
  }

  /** 使用贷出模式的调用方不需要处理关闭资源等操作
   *
   * @param resource 资源
   * @param f        处理函数
   * @tparam R 资源类型
   * @tparam T 返回类型
   * @return 返回T类型
   */
  def usingIgnore[R <: Closable, T](resource: => R)(f: R => T): T = {
    try {
      f(resource)
    } finally {
      ignoring(classOf[Throwable]) apply {
        resource.close()
      }
    }
  }

  /** 不忽略异常
   *
   * @param resource 资源
   * @param f        处理函数
   * @tparam R 资源类型
   * @tparam T 返回类型
   * @return 返回T类型
   */
  def using[R <: Closable, T](resource: => R)(f: R => T): T = {
    try {
      f(resource)
    } finally {
      if (resource != null) {
        resource.close()
      }
    }
  }

  /** 基于Future的贷出模式
   *
   * @param resource 资源
   * @param f        处理函数
   * @param ec       线程上下文
   * @tparam R 资源类型
   * @tparam T 返回类型
   * @return 返回T类型
   */
  def usingFuture[R <: Closable, T](resource: R)(f: R => Future[T])(implicit ec: ExecutionContext): Future[T] = {
    f(resource) andThen { case _ => resource.close() } //任何时候都将关闭
  }
}
