/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50719
Source Host           : localhost:3306
Source Database       : jonedb

Target Server Type    : MYSQL
Target Server Version : 50719
File Encoding         : 65001

Date: 2019-05-17 15:51:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for wf_table
-- ----------------------------
DROP TABLE IF EXISTS `wf_table`;
CREATE TABLE `wf_table` (
  `num` int(11) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `tb` int(11) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `price` decimal(20,2) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `index_wf_t_tb` (`tb`)
) ENGINE=InnoDB AUTO_INCREMENT=206450 DEFAULT CHARSET=utf8;
