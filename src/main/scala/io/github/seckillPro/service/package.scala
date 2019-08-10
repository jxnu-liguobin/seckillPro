package io.github.seckillPro

/**
 * 秒杀系统，完善事务与异步支持版
 *
 * @author 梦境迷离
 * @time 2019-08-09
 * @version v2.0
 */
package object service {

  /**
   * 所以需要事务处理的地方，子操作需要同步执行。
   * {{{
   *   Future {
   *    //这里需要同步执行
   *      Service.insert
   *      Service.update
   *      Service.select
   *      Service.delete
   *   }
   * }}}
   */

}
