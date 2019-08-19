package io.github.dreamy.seckill.util

import java.awt.image.BufferedImage
import java.awt.{ Color, Font }
import java.util.Random

import com.typesafe.scalalogging.LazyLogging
import io.github.dreamy.seckill.entity.SeckillUser
import io.github.dreamy.seckill.redis.RedisService
import io.github.dreamy.seckill.redis.key.SeckillKey
import javax.script.ScriptEngineManager

/**
 * 验证码
 *
 * @author 梦境迷离
 * @time 2019-08-06
 * @version v2.0
 */
object VerifyImage extends LazyLogging {


  /**
   * 验证码创建，并写入到页面
   */
  def createVerifyCode(user: SeckillUser, goodsId: Long) = {
    if (user == null || goodsId <= 0) {
      null
    } else {
      val width = 80
      val height = 32
      // create the image
      val image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
      val g = image.getGraphics
      // set the background color
      g.setColor(new Color(0xDCDCDC))
      g.fillRect(0, 0, width, height)
      // draw the border
      g.setColor(Color.black)
      g.drawRect(0, 0, width - 1, height - 1)
      // create a random instance to generate the codes
      val rdm = new Random()
      // make some confusion
      for (i <- 0 until 50) {
        val x = rdm.nextInt(width)
        val y = rdm.nextInt(height)
        g.drawOval(x, y, 0, 0)
      }
      // generate a random code
      val verifyCode = VerifyImage.generateVerifyCode(rdm)
      g.setColor(new Color(0, 100, 0))
      g.setFont(new Font("Candara", Font.BOLD, 24))
      g.drawString(verifyCode, 8, 24)
      g.dispose()
      // 把验证码值存到redis中
      val rnd = calc(verifyCode)
      RedisService.set(SeckillKey.getSeckillVerifyCode, user.id + "," + goodsId, rnd)
      // 输出图片
      image
    }
  }

  /**
   * 脚本计算验证码
   */
  def calc(exp: String) = {
    try {
      val manager = new ScriptEngineManager()
      val engine = manager.getEngineByName("JavaScript")
      engine.eval(exp).asInstanceOf[Int]
    } catch {
      case e: Exception =>
        logger.info(s"Failed: ${e.getMessage}")
        0
    }
  }

  /**
   * 操作符
   */
  private final val ops = Array('+', '-', '*')

  /**
   * 生成验证码的表达式
   */
  def generateVerifyCode =
    (rdm: Random) => {
      val num1 = rdm.nextInt(10)
      val num2 = rdm.nextInt(10)
      val num3 = rdm.nextInt(10)
      val op1 = ops(rdm.nextInt(3))
      val op2 = ops(rdm.nextInt(3))
      val exp = "" + num1 + op1 + num2 + op2 + num3
      exp
    }

  /**
   * 检查验证码表达式的值，成功就删除
   */
  def checkVerifyCode(user: SeckillUser, goodsId: Long, verifyCode: Int) = {
    if (user == null || goodsId <= 0) {
      false
    } else {
      val codeOld = RedisService.get(SeckillKey.getSeckillVerifyCode, user.id + "," + goodsId, classOf[Int])
      if (codeOld == null || codeOld - verifyCode != 0) {
        false
      } else {
        RedisService.delete(SeckillKey.getSeckillVerifyCode, user.id + "," + goodsId)
        true
      }
    }
  }
}
