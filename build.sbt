name := "seckillPro"

organization := "io.github.dreamy"

version := "0.1"

scalaVersion := "2.12.8"

//有的依赖目前并没使用到
//TODO 拆分依赖与版本、增加Scala跨版本配置
libraryDependencies ++= Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.scalikejdbc" %% "scalikejdbc-async" % "0.12.+",
  "org.scalikejdbc" %% "scalikejdbc" % "3.3.+",
  "com.github.jasync-sql" % "jasync-mysql" % "1.0.+",
  "org.slf4j" % "slf4j-simple" % "1.7.+",
  "mysql" % "mysql-connector-java" % "5.1.46",
  "com.typesafe" % "config" % "1.3.4",
  "com.zaxxer" % "HikariCP" % "3.1.0",
  "commons-codec" % "commons-codec" % "1.10",
  "org.apache.commons" % "commons-lang3" % "3.7",
  "com.alibaba" % "fastjson" % "1.2.56",
  "redis.clients" % "jedis" % "2.9.0",
  "com.typesafe.play" %% "play-json" % "2.7.0",
  "com.google.code.gson" % "gson" % "2.8.5",
  "com.lmax" % "disruptor" % "3.4.2",
  "com.google.guava" % "guava" % "23.0",
  "io.undertow" % "undertow-core" % "2.0.24.Final",
  "com.google.inject" % "guice" % "4.2.2"
)
