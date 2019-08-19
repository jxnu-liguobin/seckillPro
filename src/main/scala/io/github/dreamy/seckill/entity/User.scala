package io.github.dreamy.seckill.entity

/**
 * 测试的用户实体
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
case class User(id: Long, name: String) {
  //Gson需要无参构造
  def this() {
    this(1, "")
  }
}