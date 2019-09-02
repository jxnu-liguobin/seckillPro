package io.github.dreamy.seckill.presenter

import io.github.dreamy.seckill.util.CustomConversions._
import play.api.libs.json.{ JsNull, JsValue, Json, Writes }

/**
 * 返回结果
 *
 * @author 梦境迷离
 * @since 2019年8月1日
 * @version v2.0
 */
case class Result[T] private(code: Int = 0, msg: String = "success", data: Option[T])

object Result {

  def success[T](data: T) = Result[T](data = Option(data))

  def error[T](cm: CodeMsg): Result[T] = Result[T](code = cm.code, msg = cm.msg, data = None)

  //对这个泛型类使用play-json转化为json，gson自定义实在麻烦，除了redis，能不用就不用
  implicit val writer: Writes[Result[_]] = (result: Result[_]) => {

    //TODO
    result.data.getOrElse("") match {
      case ds: String =>
        Json.obj(
          "code" -> result.code,
          "msg" -> result.msg,
          "data" -> ds
        ).removeNull
      case dj: JsValue =>
        Json.obj(
          "code" -> result.code,
          "msg" -> result.msg,
          "data" -> dj
        ).removeNull
      case _ => Json.obj(
        "code" -> result.code,
        "msg" -> result.msg,
        "data" -> JsNull
      ).removeNull
    }
  }

}