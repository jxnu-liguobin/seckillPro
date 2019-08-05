package io.github.seckillPro.config

import com.typesafe.scalalogging.LazyLogging
import io.github.seckillPro.db.DatabaseSupport
import io.github.seckillPro.util.ImplicitUtils

/**
 * 通用组件接口
 *
 * @author 梦境迷离
 * @time 2019-08-02
 * @version v2.0
 */
trait CommonComponet extends DatabaseSupport with ImplicitUtils with LazyLogging