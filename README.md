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
7. 该版本是在入职Scala开发工程师三个月时写的，可能后续会出Scala第三版，通过对比1.0 Scala版本，可以大致知道新手的Scala编程演变，因为第一版是自学时
8. 不要拘泥于具体业务，本项目只为了熟练掌握Scala初中级函数式特性

#### 环境

- Scala 2.12.x
- Java 8
- Sbt 1.2.8

#### 使用

1. `git clone https://github.com/jxnu-liguobin/seckillPro`
2. `cd seckillPro;sbt compile`
3. 以sbt项目导入IDEA
4. 启动redis，默认配置即可
4. 查看handler包下的uri，类似Spring的controller中的uri或Playframework的routes中的uri，启动SeckillServerStartUp.scala
5. 通过浏览器或postman测试，`http://127.0.0.1:8080/${uri} 路径中的查询参数是必选的，其他查询参数：类似?后面的都是可选的`
6. 必须先请求登录handler，否则没有cookie/token，后续不能请求任何handlers

## TODO

1. 接口级限流
2. 异步链路追踪
3. 乐观锁
4. Redis分布式锁
5. 分布式Session
6. 好看的页面
7. 单测覆盖与重构
8. 接口压力测试
9. 超时重试
10. 错误降级处理
11. 支持Zookeeper（未试过Scala是否可行，可能Consul）、Kafka、Akka stream
12. 其他设计及优化
13. 技术分析博客

### 思考改进

1. 如何防止单个用户重复秒杀下单
2. 如何防止恶意调用秒杀接口
3. 如果用户秒杀成功，一直不支付该怎么办
4. 消息队列处理完成后，如果异步通知给用户秒杀成功
5. 如何保障 Redis、Kafka 服务的正常运行（高可用）
6. 高并发下秒杀业务如何做到不影响其他业务（隔离性）

### 分布式锁应该具备哪些条件

1. 在分布式系统环境下，一个方法在同一时间只能被一个机器的一个线程执行；
2. 高可用的获取锁与释放锁；
3. 高性能的获取锁与释放锁；
4. 具备可重入特性；
5. 具备锁失效机制，防止死锁；
6. 具备非阻塞锁特性，即没有获取到锁将直接返回获取锁失败。

