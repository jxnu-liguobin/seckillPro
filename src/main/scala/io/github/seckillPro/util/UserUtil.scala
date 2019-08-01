package io.github.seckillPro.util

import java.io.{ByteArrayOutputStream, File, RandomAccessFile}
import java.net.{HttpURLConnection, URL}

import com.alibaba.fastjson.JSON
import io.github.seckillPro.entity.SeckillUser

/**
 * 用户工具
 *
 * @author 梦境迷离
 * @time 2019年8月1日
 * @version v2.0
 */
object UserUtil {

  /**
   * 测试
   */
  def main(args: Array[String]): Unit = {
    createUser(5000);
  }

  private def createUser(count: Int) {

    val users = Seq[SeckillUser]()
    // 生成用户
    for (i <- 0 until count) {
      val user = SeckillUser(13000000000L + i, "user" + i,
        MD5Util.inputPassToDbPass("123456", "1a2b3c"), "1a2b3c", "", 1)
      users.:+(user)
    }
    println("create user")
    //先生成token
    val urlString = "http://127.0.0.1:8080/login/do_login"
    val file = new File("/Users/liguobin/tokens.txt")
    if (file.exists()) {
      file.delete()
    }
    val raf = new RandomAccessFile(file, "rw")
    file.createNewFile()
    raf.seek(0)
    for (i <- users.indices) {
      val user = users(i)
      val url = new URL(urlString)
      val co = url.openConnection().asInstanceOf[HttpURLConnection]
      co.setRequestMethod("POST")
      co.setDoOutput(true)
      val out = co.getOutputStream
      val params = "mobile=" + user.id + "&password=" + MD5Util.inputPassToFormPass("123456")
      out.write(params.getBytes())
      out.flush()
      val inputStream = co.getInputStream
      val bout = new ByteArrayOutputStream()
      var buff = new Array[Byte](1024)
      //Scala赋值返回Unit,不能像Java一样直接放在括号中
      var len = inputStream.read(buff)
      while (len != -1) {
        bout.write(buff, 0, len)
        len = inputStream.read(buff)
      }
      inputStream.close()
      bout.close()
      val response = new String(bout.toByteArray)
      val jo = JSON.parseObject(response)
      val token = jo.getString("data")
      println("create token : " + user.id)

      val row = user.id + "," + token
      raf.seek(raf.length())
      raf.write(row.getBytes())
      raf.write("\r\n".getBytes())
      println("write to file : " + user.id)
    }
    raf.close()
    println("over")
  }

}