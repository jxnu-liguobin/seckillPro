-- how to init data base
-- mysql -h localhost  -u  root -p
-- create database seckill
-- use seckill
-- source schema.sql
-- source data.sql
-- There is no limit on null, but in the code.
-- time is ms

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for goods
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods`
(
    `id`           bigint(64) NOT NULL AUTO_INCREMENT,
    `goods_name`   varchar(255)   DEFAULT NULL,
    `goods_title`  varchar(255)   DEFAULT NULL,
    `goods_img`    varchar(255)   DEFAULT NULL,
    `goods_detail` varchar(255)   DEFAULT NULL,
    `goods_price`  decimal(10, 2) DEFAULT NULL,
    `goods_stock`  int(32)        DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info`
(
    `id`               bigint(64) NOT NULL AUTO_INCREMENT,
    `user_id`          bigint(64)     DEFAULT NULL,
    `delivery_addr_id` bigint(64)     DEFAULT NULL,
    `goods_id`         bigint(64)     DEFAULT NULL,
    `goods_name`       varchar(255)   DEFAULT NULL,
    `goods_count`      varchar(255)   DEFAULT NULL,
    `goods_price`      decimal(10, 2) DEFAULT NULL,
    `order_channel`    int(255)       DEFAULT NULL,
    `status`           int(255)       DEFAULT NULL,
    `create_date`      bigint         DEFAULT NULL,
    `pay_date`         bigint         DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for seckill_goods
-- ----------------------------
DROP TABLE IF EXISTS `seckill_goods`;
CREATE TABLE `seckill_goods`
(
    `id`            bigint(64) NOT NULL AUTO_INCREMENT,
    `goods_id`      bigint(64)     DEFAULT NULL,
    `stock_count`   int(64)        DEFAULT NULL,
    `start_date`    bigint         DEFAULT NULL,
    `end_date`      bigint         DEFAULT NULL,
    `seckill_price` decimal(10, 2) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for seckill_order
-- ----------------------------
DROP TABLE IF EXISTS `seckill_order`;
CREATE TABLE `seckill_order`
(
    `id`       bigint(64) NOT NULL AUTO_INCREMENT,
    `user_id`  bigint(64) DEFAULT NULL,
    `order_id` bigint(64) DEFAULT NULL,
    `goods_id` bigint(64) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

ALTER TABLE `seckill_order`
    ADD UNIQUE (`user_id`, `goods_id`);

-- ----------------------------
-- Records of seckill_order
-- ----------------------------

-- ----------------------------
-- Table structure for seckill_user
-- ----------------------------
DROP TABLE IF EXISTS `seckill_user`;
CREATE TABLE `seckill_user`
(
    `id`            bigint(64) NOT NULL,
    `nickname`      varchar(255) DEFAULT NULL,
    `password`      varchar(255) DEFAULT NULL,
    `salt`          varchar(255) DEFAULT NULL,
    `head`          varchar(255) DEFAULT NULL,
    `registerDate`  bigint       DEFAULT NULL,
    `lastLoginDate` bigint       DEFAULT NULL,
    `loginCount`    int(255)     DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`   bigint(64) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8;