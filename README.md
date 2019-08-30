Seckill API Version 2.0
---

[![Build Status](https://travis-ci.org/jxnu-liguobin/seckillPro.svg?branch=master)](https://travis-ci.org/jxnu-liguobin/seckillPro)
![GitHub](https://img.shields.io/github/license/jxnu-liguobin/seckillPro.svg)
![GitHub top language](https://img.shields.io/github/languages/top/jxnu-liguobin/seckillPro.svg)

A seckill API project which support high-performance asynchronous implemented by Scala but don't hava any pages

一个使用Scala实现的高性能异步的慕课网秒杀系统的后端接口项目（函数式 & 异步编程风格）。

Skills
---

* scala
* undertow
* mysql
* scalikejdbc
* disruptor
* redis
* akka


Others Version
---
[Scala V1.0](https://github.com/jxnu-liguobin/SpringBoot-SecKill-Scala)

[Java  V1.0](https://github.com/jxnu-liguobin/SpringBoot-SecKill-Scala/tree/seckill)


### 秒杀接口第二版

#### 主要说明

1. 本项目的基本的功能与2018年的慕课网秒杀系统基本一致，但本版本不包含任何页面，是纯Scala+json接口项目，对于原有反回页面的部分仅模拟GET请求
2. 此版是Scala第二版，使用了较多的函数式特性和Scala初中级的特性以及部分Scala特有的技术栈，适合Scala基础扎实的新生，不再适合小白
3. 与第一版不同，此版不适合Java选手。此版几乎不会看到命令式代码，全文使用Future编程，并且尽量使用少依赖第三方客户端或者Jar
4. 业务上没太多的改变，注释比较多，希望能帮助理解，有不少踩坑
5. 单测目前使用main方法，后续改用scalatest-plus
6. 使用高性能队列disruptor，和高性能嵌入式服务器undertow，以及高性能缓存redis

#### 主要技术

* scala 2.12.8
* java 8
* undertow 2.0.24.Final
* mysql 5.6
* scalikejdbc 3.3.+ 
* disruptor 3.4.2
* redis 2.9.0 (jedis)
* akka 暂未用

#### 使用

1. `git clone https://github.com/jxnu-liguobin/seckillPro`
2. `cd seckillPro;sbt compile`
3. 以sbt项目导入IDEA
4. 启动redis，默认配置即可
4. 查看handler包下的uri，类似Spring的controller中的uri或Playframework的routes中的uri，启动SeckillServerStartUp.scala
5. 通过浏览器或postman测试，`http:127.0.0.1:8080/uri 路径中的查询参数是必选的，其他查询参数：类似?后面的都是可选的`

