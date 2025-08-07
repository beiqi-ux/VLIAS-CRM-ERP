-- MySQL dump 10.13  Distrib 5.7.24, for macos10.14 (x86_64)
--
-- Host: localhost    Database: vliascrm
-- ------------------------------------------------------
-- Server version	5.7.24

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `api_config`
--

DROP TABLE IF EXISTS `api_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'API ID',
  `api_name` varchar(100) NOT NULL COMMENT 'API名称',
  `api_code` varchar(50) NOT NULL COMMENT 'API编码',
  `api_url` varchar(255) NOT NULL COMMENT 'API地址',
  `request_method` varchar(10) NOT NULL COMMENT '请求方法',
  `content_type` varchar(100) DEFAULT NULL COMMENT '内容类型',
  `timeout` int(11) DEFAULT '5000' COMMENT '超时时间(毫秒)',
  `retry_times` int(11) DEFAULT '0' COMMENT '重试次数',
  `request_params` text COMMENT '请求参数',
  `response_params` text COMMENT '响应参数',
  `headers` text COMMENT '请求头',
  `status` tinyint(4) DEFAULT '1',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `api_type` int(11) NOT NULL,
  `auth_config` text,
  `auth_type` int(11) DEFAULT NULL,
  `need_auth` tinyint(4) DEFAULT '0',
  `request_headers` text,
  `response_format` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_code` (`api_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='API配置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_config`
--

LOCK TABLES `api_config` WRITE;
/*!40000 ALTER TABLE `api_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_log`
--

DROP TABLE IF EXISTS `api_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `api_id` bigint(20) DEFAULT NULL COMMENT 'API ID',
  `api_code` varchar(50) DEFAULT NULL COMMENT 'API编码',
  `platform_id` bigint(20) DEFAULT NULL COMMENT '平台ID',
  `platform_code` varchar(50) DEFAULT NULL COMMENT '平台编码',
  `request_url` varchar(500) NOT NULL COMMENT '请求地址',
  `request_method` varchar(10) NOT NULL COMMENT '请求方法',
  `request_headers` text COMMENT '请求头',
  `request_body` text COMMENT '请求体',
  `response_headers` text COMMENT '响应头',
  `response_body` text COMMENT '响应体',
  `http_status` int(11) DEFAULT NULL COMMENT 'HTTP状态码',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 0-失败 1-成功',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '错误消息',
  `request_time` bigint(20) DEFAULT NULL COMMENT '请求耗时(毫秒)',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `api_name` varchar(50) DEFAULT NULL,
  `execution_time` bigint(20) DEFAULT NULL,
  `request_params` text,
  `response_code` int(11) DEFAULT NULL,
  `result` int(11) NOT NULL,
  `source_system` varchar(50) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `user_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_api_id` (`api_id`),
  KEY `idx_platform_id` (`platform_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口调用日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_log`
--

LOCK TABLES `api_log` WRITE;
/*!40000 ALTER TABLE `api_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_permission`
--

DROP TABLE IF EXISTS `api_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `api_id` bigint(20) NOT NULL COMMENT 'API ID',
  `api_code` varchar(50) NOT NULL COMMENT 'API编码',
  `app_key` varchar(100) NOT NULL COMMENT '应用Key',
  `app_secret` varchar(200) DEFAULT NULL COMMENT '应用Secret',
  `ip_whitelist` text COMMENT 'IP白名单',
  `rate_limit` int(11) DEFAULT '0' COMMENT '限流次数/分钟',
  `expired_time` datetime DEFAULT NULL COMMENT '过期时间',
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `daily_limit` int(11) DEFAULT '0',
  `end_time` datetime(6) DEFAULT NULL,
  `expire_days` int(11) DEFAULT NULL,
  `expire_type` tinyint(4) DEFAULT '1',
  `permission_type` int(11) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `scope` tinyint(4) DEFAULT '1',
  `start_time` datetime(6) DEFAULT NULL,
  `target_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_id` (`api_id`,`app_key`),
  KEY `idx_app_key` (`app_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_permission`
--

LOCK TABLES `api_permission` WRITE;
/*!40000 ALTER TABLE `api_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_platform`
--

DROP TABLE IF EXISTS `api_platform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `api_platform` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '平台ID',
  `platform_name` varchar(100) NOT NULL COMMENT '平台名称',
  `platform_code` varchar(50) NOT NULL COMMENT '平台编码',
  `platform_type` int(11) NOT NULL,
  `app_id` varchar(100) DEFAULT NULL COMMENT 'AppID',
  `app_secret` varchar(200) DEFAULT NULL COMMENT 'AppSecret',
  `access_token` varchar(200) DEFAULT NULL COMMENT 'AccessToken',
  `token_expire_time` datetime DEFAULT NULL COMMENT 'Token过期时间',
  `other_params` text COMMENT '其他参数',
  `status` tinyint(4) DEFAULT '1',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `app_private_key` text,
  `app_public_key` text,
  `other_config` text,
  `platform_icon` varchar(255) DEFAULT NULL,
  `platform_public_key` text,
  `platform_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `platform_code` (`platform_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='第三方平台表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_platform`
--

LOCK TABLES `api_platform` WRITE;
/*!40000 ALTER TABLE `api_platform` DISABLE KEYS */;
/*!40000 ALTER TABLE `api_platform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_device`
--

DROP TABLE IF EXISTS `app_device`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_device` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `member_id` bigint(20) DEFAULT NULL COMMENT '会员ID',
  `device_token` varchar(100) NOT NULL COMMENT '设备Token',
  `device_type` varchar(50) NOT NULL COMMENT '设备类型',
  `device_brand` varchar(50) DEFAULT NULL COMMENT '设备品牌',
  `device_model` varchar(50) DEFAULT NULL COMMENT '设备型号',
  `os_type` varchar(20) DEFAULT NULL COMMENT '操作系统类型',
  `os_version` varchar(20) DEFAULT NULL COMMENT '操作系统版本',
  `app_version` varchar(20) DEFAULT NULL COMMENT 'APP版本',
  `push_id` varchar(100) DEFAULT NULL COMMENT '推送ID',
  `last_active_time` datetime DEFAULT NULL COMMENT '最后活跃时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `device_token` (`device_token`),
  KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='设备表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_device`
--

LOCK TABLES `app_device` WRITE;
/*!40000 ALTER TABLE `app_device` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_device` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_push`
--

DROP TABLE IF EXISTS `app_push`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_push` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '推送ID',
  `push_title` varchar(100) NOT NULL COMMENT '推送标题',
  `push_content` text NOT NULL COMMENT '推送内容',
  `push_type` int(11) NOT NULL,
  `target_type` tinyint(4) NOT NULL COMMENT '目标类型 1-全部 2-指定用户 3-指定标签',
  `target_ids` varchar(500) DEFAULT NULL COMMENT '目标ID集合',
  `platform` tinyint(4) DEFAULT NULL COMMENT '平台 0-全部 1-Android 2-iOS 3-小程序',
  `link_type` tinyint(4) DEFAULT NULL COMMENT '链接类型 1-URL 2-页面 3-商品 4-订单',
  `link_value` varchar(255) DEFAULT NULL COMMENT '链接值',
  `push_time` datetime DEFAULT NULL COMMENT '推送时间',
  `status` tinyint(4) DEFAULT '1',
  `success_count` int(11) DEFAULT '0' COMMENT '成功数量',
  `fail_count` int(11) DEFAULT '0' COMMENT '失败数量',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '错误消息',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `click_count` int(11) DEFAULT '0',
  `extra_data` text,
  `fail_reason` text,
  `is_timing` tinyint(4) DEFAULT '0',
  `plan_time` datetime(6) DEFAULT NULL,
  `provider` int(11) DEFAULT NULL,
  `push_platform` int(11) NOT NULL,
  `push_scope` int(11) NOT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `total_count` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_push_type` (`push_type`),
  KEY `idx_status` (`status`),
  KEY `idx_push_time` (`push_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推送记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_push`
--

LOCK TABLES `app_push` WRITE;
/*!40000 ALTER TABLE `app_push` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_push` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_setting`
--

DROP TABLE IF EXISTS `app_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_setting` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `push_switch` tinyint(4) DEFAULT '1',
  `sound_switch` tinyint(4) DEFAULT '1',
  `vibrate_switch` tinyint(4) DEFAULT '1',
  `theme` varchar(20) DEFAULT 'default' COMMENT '主题',
  `language` varchar(10) DEFAULT 'zh_CN' COMMENT '语言',
  `currency` varchar(10) DEFAULT 'CNY' COMMENT '货币',
  `font_size` varchar(10) DEFAULT 'medium' COMMENT '字体大小',
  `privacy_settings` text COMMENT '隐私设置JSON',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_id` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户设置表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_setting`
--

LOCK TABLES `app_setting` WRITE;
/*!40000 ALTER TABLE `app_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_version`
--

DROP TABLE IF EXISTS `app_version`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '版本ID',
  `version_name` varchar(20) NOT NULL COMMENT '版本名称',
  `version_code` int(11) NOT NULL COMMENT '版本号',
  `platform` int(11) NOT NULL,
  `download_url` varchar(255) DEFAULT NULL COMMENT '下载地址',
  `update_content` text NOT NULL COMMENT '更新内容',
  `file_size` int(11) DEFAULT NULL COMMENT '文件大小(KB)',
  `is_force` tinyint(4) DEFAULT '0',
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `platform` (`platform`,`version_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='版本表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_version`
--

LOCK TABLES `app_version` WRITE;
/*!40000 ALTER TABLE `app_version` DISABLE KEYS */;
/*!40000 ALTER TABLE `app_version` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_contact`
--

DROP TABLE IF EXISTS `crm_contact`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_contact` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '联系人ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `name` varchar(50) NOT NULL COMMENT '联系人姓名',
  `position` varchar(50) DEFAULT NULL COMMENT '职位',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `is_primary` tinyint(4) DEFAULT '0',
  `gender` tinyint(4) DEFAULT '0',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `wechat` varchar(50) DEFAULT NULL COMMENT '微信号',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ号',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户联系人表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_contact`
--

LOCK TABLES `crm_contact` WRITE;
/*!40000 ALTER TABLE `crm_contact` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_contact` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_customer`
--

DROP TABLE IF EXISTS `crm_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `customer_name` varchar(100) NOT NULL COMMENT '客户名称',
  `customer_code` varchar(50) DEFAULT NULL COMMENT '客户编码',
  `customer_type` int(11) DEFAULT NULL,
  `industry_id` bigint(20) DEFAULT NULL COMMENT '所属行业ID',
  `source_id` bigint(20) DEFAULT NULL COMMENT '客户来源ID',
  `level_id` bigint(20) DEFAULT NULL COMMENT '客户等级ID',
  `status` int(11) DEFAULT NULL,
  `score` int(11) DEFAULT '0' COMMENT '客户评分',
  `owner_user_id` bigint(20) DEFAULT NULL COMMENT '负责人ID',
  `is_public` int(11) DEFAULT NULL,
  `next_follow_time` datetime DEFAULT NULL COMMENT '下次跟进时间',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `remark` tinytext,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_owner_user_id` (`owner_user_id`),
  KEY `idx_customer_name` (`customer_name`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_is_public` (`is_public`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_customer`
--

LOCK TABLES `crm_customer` WRITE;
/*!40000 ALTER TABLE `crm_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_customer_group`
--

DROP TABLE IF EXISTS `crm_customer_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_customer_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `group_id` bigint(20) NOT NULL COMMENT '分组ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`,`group_id`),
  UNIQUE KEY `UK5hogye7rg11m8u3qm7smu2ddt` (`customer_id`,`group_id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_group_id` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户分组关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_customer_group`
--

LOCK TABLES `crm_customer_group` WRITE;
/*!40000 ALTER TABLE `crm_customer_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_customer_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_customer_tag`
--

DROP TABLE IF EXISTS `crm_customer_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_customer_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `tag_id` bigint(20) NOT NULL COMMENT '标签ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `customer_id` (`customer_id`,`tag_id`),
  UNIQUE KEY `UK2l77n581ehiuwg9iybwepq3p1` (`customer_id`,`tag_id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户标签关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_customer_tag`
--

LOCK TABLES `crm_customer_tag` WRITE;
/*!40000 ALTER TABLE `crm_customer_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_customer_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_follow_record`
--

DROP TABLE IF EXISTS `crm_follow_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_follow_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '跟进记录ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `follow_type` int(11) DEFAULT NULL,
  `follow_content` text NOT NULL COMMENT '跟进内容',
  `next_follow_time` datetime DEFAULT NULL COMMENT '下次跟进时间',
  `files` varchar(500) DEFAULT NULL COMMENT '附件',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `update_time` datetime(6) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_create_by` (`create_by`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户跟进记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_follow_record`
--

LOCK TABLES `crm_follow_record` WRITE;
/*!40000 ALTER TABLE `crm_follow_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_follow_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_group`
--

DROP TABLE IF EXISTS `crm_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_group` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分组ID',
  `group_name` varchar(50) NOT NULL COMMENT '分组名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分组ID',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户分组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_group`
--

LOCK TABLES `crm_group` WRITE;
/*!40000 ALTER TABLE `crm_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_lead`
--

DROP TABLE IF EXISTS `crm_lead`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_lead` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '线索ID',
  `lead_name` varchar(100) NOT NULL COMMENT '线索名称',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人姓名',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `source_id` bigint(20) DEFAULT NULL COMMENT '线索来源ID',
  `status` tinyint(4) DEFAULT '1',
  `owner_user_id` bigint(20) DEFAULT NULL COMMENT '负责人ID',
  `content` text COMMENT '线索内容',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `is_transformed` tinyint(4) DEFAULT '0',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '转化后客户ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `last_follow_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_owner_user_id` (`owner_user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_is_transformed` (`is_transformed`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户线索表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_lead`
--

LOCK TABLES `crm_lead` WRITE;
/*!40000 ALTER TABLE `crm_lead` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_lead` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_public_pool`
--

DROP TABLE IF EXISTS `crm_public_pool`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_public_pool` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
  `reason_type` int(11) DEFAULT NULL,
  `reason` varchar(200) DEFAULT NULL COMMENT '进入原因',
  `previous_owner_id` bigint(20) DEFAULT NULL COMMENT '前负责人ID',
  `enter_time` datetime NOT NULL COMMENT '进入公海时间',
  `status` tinyint(4) DEFAULT '1',
  `receive_user_id` bigint(20) DEFAULT NULL COMMENT '领取人ID',
  `receive_time` datetime DEFAULT NULL COMMENT '领取时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_status` (`status`),
  KEY `idx_previous_owner_id` (`previous_owner_id`),
  KEY `idx_receive_user_id` (`receive_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户公海池表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_public_pool`
--

LOCK TABLES `crm_public_pool` WRITE;
/*!40000 ALTER TABLE `crm_public_pool` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_public_pool` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `crm_tag`
--

DROP TABLE IF EXISTS `crm_tag`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `crm_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '标签ID',
  `tag_name` varchar(50) NOT NULL COMMENT '标签名称',
  `tag_color` varchar(20) DEFAULT NULL COMMENT '标签颜色',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `tag_name` (`tag_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户标签表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `crm_tag`
--

LOCK TABLES `crm_tag` WRITE;
/*!40000 ALTER TABLE `crm_tag` DISABLE KEYS */;
/*!40000 ALTER TABLE `crm_tag` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dist_commission`
--

DROP TABLE IF EXISTS `dist_commission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dist_commission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `distributor_id` bigint(20) NOT NULL COMMENT '分销员ID',
  `amount` decimal(10,2) NOT NULL COMMENT '佣金金额',
  `type` int(11) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单编号',
  `withdraw_id` bigint(20) DEFAULT NULL COMMENT '提现记录ID',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `before_amount` decimal(10,2) DEFAULT NULL COMMENT '变动前金额',
  `after_amount` decimal(10,2) DEFAULT NULL COMMENT '变动后金额',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_distributor_id` (`distributor_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_withdraw_id` (`withdraw_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dist_commission`
--

LOCK TABLES `dist_commission` WRITE;
/*!40000 ALTER TABLE `dist_commission` DISABLE KEYS */;
/*!40000 ALTER TABLE `dist_commission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dist_distributor`
--

DROP TABLE IF EXISTS `dist_distributor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dist_distributor` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分销员ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `name` varchar(50) NOT NULL COMMENT '分销员姓名',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `level_id` bigint(20) DEFAULT '1' COMMENT '分销等级ID',
  `status` tinyint(4) DEFAULT '1',
  `invite_code` varchar(20) NOT NULL COMMENT '邀请码',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级分销员ID',
  `parent_path` varchar(500) DEFAULT NULL COMMENT '上级路径ID，用逗号分隔',
  `total_commission` decimal(10,2) DEFAULT '0.00' COMMENT '累计佣金',
  `available_commission` decimal(10,2) DEFAULT '0.00' COMMENT '可用佣金',
  `withdrawn_commission` decimal(10,2) DEFAULT '0.00' COMMENT '已提现佣金',
  `freezing_commission` decimal(10,2) DEFAULT '0.00' COMMENT '冻结佣金',
  `team_count` int(11) DEFAULT '0' COMMENT '团队人数',
  `first_level_count` int(11) DEFAULT '0' COMMENT '一级人数',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_user_id` bigint(20) DEFAULT NULL COMMENT '审核人',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_id` (`member_id`),
  UNIQUE KEY `invite_code` (`invite_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_level_id` (`level_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分销员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dist_distributor`
--

LOCK TABLES `dist_distributor` WRITE;
/*!40000 ALTER TABLE `dist_distributor` DISABLE KEYS */;
/*!40000 ALTER TABLE `dist_distributor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dist_level`
--

DROP TABLE IF EXISTS `dist_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dist_level` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `level_name` varchar(50) NOT NULL COMMENT '等级名称',
  `level_value` int(11) NOT NULL COMMENT '等级值',
  `first_rate` decimal(5,4) NOT NULL COMMENT '一级佣金比例',
  `second_rate` decimal(5,4) DEFAULT NULL COMMENT '二级佣金比例',
  `third_rate` decimal(5,4) DEFAULT NULL COMMENT '三级佣金比例',
  `upgrade_type` int(11) DEFAULT NULL,
  `upgrade_value` int(11) DEFAULT NULL,
  `description` text COMMENT '等级描述',
  `status` tinyint(4) DEFAULT '1',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_value` (`level_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分销等级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dist_level`
--

LOCK TABLES `dist_level` WRITE;
/*!40000 ALTER TABLE `dist_level` DISABLE KEYS */;
/*!40000 ALTER TABLE `dist_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dist_order`
--

DROP TABLE IF EXISTS `dist_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dist_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `order_amount` decimal(10,2) NOT NULL COMMENT '订单金额',
  `distributor_id` bigint(20) NOT NULL COMMENT '分销员ID',
  `member_id` bigint(20) NOT NULL COMMENT '下单会员ID',
  `level` int(11) DEFAULT '1' COMMENT '分销层级 1-一级 2-二级 3-三级',
  `commission_rate` decimal(5,4) NOT NULL COMMENT '佣金比例',
  `commission_amount` decimal(10,2) NOT NULL COMMENT '佣金金额',
  `status` tinyint(4) DEFAULT '1',
  `settlement_id` bigint(20) DEFAULT NULL COMMENT '结算ID',
  `settlement_time` datetime DEFAULT NULL COMMENT '结算时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_distributor_id` (`distributor_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分销订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dist_order`
--

LOCK TABLES `dist_order` WRITE;
/*!40000 ALTER TABLE `dist_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `dist_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dist_promotion_code`
--

DROP TABLE IF EXISTS `dist_promotion_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dist_promotion_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `distributor_id` bigint(20) NOT NULL COMMENT '分销员ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `code_type` tinyint(4) NOT NULL COMMENT '码类型 1-商品 2-分享链接 3-店铺',
  `code_value` varchar(100) NOT NULL COMMENT '码值',
  `target_id` bigint(20) DEFAULT NULL COMMENT '目标ID（商品ID、店铺ID等）',
  `qr_code` varchar(255) DEFAULT NULL COMMENT '二维码图片',
  `poster_url` varchar(255) DEFAULT NULL COMMENT '海报图片',
  `visit_count` int(11) DEFAULT '0' COMMENT '访问次数',
  `order_count` int(11) DEFAULT '0' COMMENT '订单次数',
  `status` tinyint(4) DEFAULT '1',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `code` varchar(20) NOT NULL,
  `convert_count` int(11) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `link_url` varchar(255) DEFAULT NULL,
  `qrcode_url` varchar(255) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `scan_count` int(11) DEFAULT '0',
  `type` int(11) NOT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_distributor_id` (`distributor_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_code_type` (`code_type`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='推广码表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dist_promotion_code`
--

LOCK TABLES `dist_promotion_code` WRITE;
/*!40000 ALTER TABLE `dist_promotion_code` DISABLE KEYS */;
/*!40000 ALTER TABLE `dist_promotion_code` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dist_withdraw`
--

DROP TABLE IF EXISTS `dist_withdraw`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dist_withdraw` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `withdraw_no` varchar(50) NOT NULL COMMENT '提现单号',
  `distributor_id` bigint(20) NOT NULL COMMENT '分销员ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `amount` decimal(10,2) NOT NULL COMMENT '提现金额',
  `fee` decimal(10,2) DEFAULT '0.00' COMMENT '手续费',
  `actual_amount` decimal(10,2) NOT NULL COMMENT '实际到账金额',
  `withdraw_type` tinyint(4) NOT NULL COMMENT '提现方式 1-微信 2-支付宝 3-银行卡',
  `account` varchar(100) NOT NULL COMMENT '收款账号',
  `account_name` varchar(50) NOT NULL COMMENT '收款人姓名',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '银行名称',
  `bank_branch` varchar(100) DEFAULT NULL COMMENT '支行名称',
  `status` tinyint(4) DEFAULT '1' COMMENT '状态 1-申请中 2-已审核 3-已拒绝 4-提现中 5-已到账 6-已取消',
  `reject_reason` varchar(200) DEFAULT NULL COMMENT '拒绝原因',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_user_id` bigint(20) DEFAULT NULL COMMENT '审核人',
  `payment_time` datetime DEFAULT NULL COMMENT '打款时间',
  `payment_user_id` bigint(20) DEFAULT NULL COMMENT '打款人',
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '交易流水号',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `withdraw_no` (`withdraw_no`),
  KEY `idx_distributor_id` (`distributor_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金提现表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dist_withdraw`
--

LOCK TABLES `dist_withdraw` WRITE;
/*!40000 ALTER TABLE `dist_withdraw` DISABLE KEYS */;
/*!40000 ALTER TABLE `dist_withdraw` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_account`
--

DROP TABLE IF EXISTS `fin_account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '账户ID',
  `account_name` varchar(50) NOT NULL COMMENT '账户名称',
  `account_no` varchar(50) DEFAULT NULL COMMENT '账号',
  `account_type` int(11) NOT NULL,
  `balance` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `initial_balance` decimal(10,2) DEFAULT '0.00' COMMENT '初始余额',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户行',
  `bank_branch` varchar(100) DEFAULT NULL COMMENT '支行',
  `status` tinyint(4) DEFAULT '1',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `account_holder` varchar(50) DEFAULT NULL,
  `current_balance` decimal(10,2) DEFAULT '0.00',
  PRIMARY KEY (`id`),
  KEY `idx_account_type` (`account_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_account`
--

LOCK TABLES `fin_account` WRITE;
/*!40000 ALTER TABLE `fin_account` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_bill`
--

DROP TABLE IF EXISTS `fin_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_bill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '账单ID',
  `bill_no` varchar(50) NOT NULL COMMENT '账单编号',
  `bill_type` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `status` int(11) DEFAULT NULL,
  `subject_id` bigint(20) DEFAULT NULL COMMENT '科目ID',
  `account_id` bigint(20) DEFAULT NULL COMMENT '账户ID',
  `ref_type` varchar(50) DEFAULT NULL COMMENT '关联类型',
  `ref_id` bigint(20) DEFAULT NULL COMMENT '关联ID',
  `bill_date` date NOT NULL COMMENT '账单日期',
  `settlement_time` datetime DEFAULT NULL COMMENT '结算时间',
  `remark` tinytext,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `bill_no` (`bill_no`),
  KEY `idx_bill_type` (`bill_type`),
  KEY `idx_status` (`status`),
  KEY `idx_subject_id` (`subject_id`),
  KEY `idx_ref_id` (`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_bill`
--

LOCK TABLES `fin_bill` WRITE;
/*!40000 ALTER TABLE `fin_bill` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_bill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_commission`
--

DROP TABLE IF EXISTS `fin_commission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_commission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `commission_no` varchar(50) NOT NULL COMMENT '佣金结算单号',
  `distributor_id` bigint(20) NOT NULL COMMENT '分销员ID',
  `period_start` date NOT NULL COMMENT '结算周期开始日期',
  `period_end` date NOT NULL COMMENT '结算周期结束日期',
  `total_order_amount` decimal(38,2) DEFAULT NULL,
  `commission_amount` decimal(38,2) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `settlement_time` datetime DEFAULT NULL COMMENT '结算时间',
  `remark` tinytext,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `commission_no` (`commission_no`),
  KEY `idx_distributor_id` (`distributor_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金结算表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_commission`
--

LOCK TABLES `fin_commission` WRITE;
/*!40000 ALTER TABLE `fin_commission` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_commission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_commission_item`
--

DROP TABLE IF EXISTS `fin_commission_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_commission_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `commission_id` bigint(20) NOT NULL COMMENT '佣金结算单ID',
  `commission_no` varchar(50) NOT NULL COMMENT '佣金结算单号',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `order_amount` decimal(38,2) NOT NULL,
  `commission_rate` decimal(38,2) NOT NULL,
  `commission_amount` decimal(38,2) NOT NULL,
  `order_time` datetime DEFAULT NULL COMMENT '订单时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_commission_id` (`commission_id`),
  KEY `idx_commission_no` (`commission_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='佣金结算明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_commission_item`
--

LOCK TABLES `fin_commission_item` WRITE;
/*!40000 ALTER TABLE `fin_commission_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_commission_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_invoice`
--

DROP TABLE IF EXISTS `fin_invoice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_invoice` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '发票ID',
  `invoice_no` varchar(50) NOT NULL COMMENT '发票编号',
  `invoice_type` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `tax_amount` decimal(10,2) DEFAULT '0.00' COMMENT '税额',
  `total_amount` decimal(10,2) NOT NULL COMMENT '价税合计',
  `invoice_title` varchar(100) NOT NULL COMMENT '发票抬头',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `invoice_content` varchar(200) DEFAULT NULL COMMENT '发票内容',
  `status` tinyint(4) DEFAULT '1',
  `target_type` tinyint(4) DEFAULT NULL COMMENT '对象类型 1-客户 2-供应商 3-其他',
  `target_id` bigint(20) DEFAULT NULL COMMENT '对象ID',
  `target_name` varchar(100) DEFAULT NULL COMMENT '对象名称',
  `email` varchar(100) DEFAULT NULL COMMENT '接收邮箱',
  `ref_type` varchar(50) DEFAULT NULL COMMENT '关联类型',
  `ref_id` bigint(20) DEFAULT NULL COMMENT '关联ID',
  `issue_time` datetime DEFAULT NULL COMMENT '开票时间',
  `issue_user_id` bigint(20) DEFAULT NULL COMMENT '开票人',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `business_id` bigint(20) DEFAULT NULL,
  `business_no` varchar(50) DEFAULT NULL,
  `business_type` int(11) DEFAULT NULL,
  `direction` int(11) NOT NULL,
  `drawer_address_phone` varchar(200) DEFAULT NULL,
  `drawer_bank_account` varchar(200) DEFAULT NULL,
  `drawer_name` varchar(100) NOT NULL,
  `drawer_tax_no` varchar(50) DEFAULT NULL,
  `invoice_amount` decimal(10,2) NOT NULL,
  `invoice_code` varchar(50) DEFAULT NULL,
  `invoice_date` date NOT NULL,
  `payee_address_phone` varchar(200) DEFAULT NULL,
  `payee_bank_account` varchar(200) DEFAULT NULL,
  `payee_name` varchar(100) NOT NULL,
  `payee_tax_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `invoice_no` (`invoice_no`),
  KEY `idx_invoice_type` (`invoice_type`),
  KEY `idx_status` (`status`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='发票表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_invoice`
--

LOCK TABLES `fin_invoice` WRITE;
/*!40000 ALTER TABLE `fin_invoice` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_invoice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_settlement`
--

DROP TABLE IF EXISTS `fin_settlement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_settlement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '结算ID',
  `settlement_no` varchar(50) NOT NULL COMMENT '结算单号',
  `settlement_type` int(11) NOT NULL,
  `amount` decimal(38,2) NOT NULL,
  `settlement_method` int(11) NOT NULL,
  `account_id` bigint(20) DEFAULT NULL COMMENT '账户ID',
  `transaction_no` varchar(100) DEFAULT NULL COMMENT '交易流水号',
  `settlement_date` date NOT NULL COMMENT '结算日期',
  `target_type` int(11) DEFAULT NULL,
  `target_id` bigint(20) DEFAULT NULL COMMENT '对象ID',
  `target_name` varchar(100) DEFAULT NULL COMMENT '对象名称',
  `status` int(11) DEFAULT NULL,
  `remark` tinytext,
  `confirm_user_id` bigint(20) DEFAULT NULL COMMENT '确认人',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `settlement_no` (`settlement_no`),
  KEY `idx_settlement_type` (`settlement_type`),
  KEY `idx_status` (`status`),
  KEY `idx_target_id` (`target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='结算表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_settlement`
--

LOCK TABLES `fin_settlement` WRITE;
/*!40000 ALTER TABLE `fin_settlement` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_settlement` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_settlement_item`
--

DROP TABLE IF EXISTS `fin_settlement_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_settlement_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `settlement_id` bigint(20) NOT NULL COMMENT '结算ID',
  `settlement_no` varchar(50) NOT NULL COMMENT '结算单号',
  `bill_id` bigint(20) NOT NULL COMMENT '账单ID',
  `bill_no` varchar(50) NOT NULL COMMENT '账单编号',
  `amount` decimal(38,2) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_settlement_id` (`settlement_id`),
  KEY `idx_settlement_no` (`settlement_no`),
  KEY `idx_bill_id` (`bill_id`),
  KEY `idx_bill_no` (`bill_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='结算明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_settlement_item`
--

LOCK TABLES `fin_settlement_item` WRITE;
/*!40000 ALTER TABLE `fin_settlement_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_settlement_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_subject`
--

DROP TABLE IF EXISTS `fin_subject`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '科目ID',
  `subject_name` varchar(50) NOT NULL COMMENT '科目名称',
  `subject_code` varchar(50) NOT NULL COMMENT '科目编码',
  `subject_type` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父科目ID',
  `level` int(11) DEFAULT '1' COMMENT '层级',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `subject_code` (`subject_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_subject_type` (`subject_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='财务科目表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_subject`
--

LOCK TABLES `fin_subject` WRITE;
/*!40000 ALTER TABLE `fin_subject` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_subject` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fin_transaction`
--

DROP TABLE IF EXISTS `fin_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fin_transaction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `transaction_no` varchar(50) NOT NULL COMMENT '交易流水号',
  `transaction_type` int(11) NOT NULL,
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `account_id` bigint(20) NOT NULL COMMENT '账户ID',
  `target_account_id` bigint(20) DEFAULT NULL COMMENT '目标账户ID（转账时使用）',
  `transaction_method` int(11) DEFAULT NULL,
  `transaction_time` datetime NOT NULL COMMENT '交易时间',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `ref_type` varchar(50) DEFAULT NULL COMMENT '关联类型',
  `ref_id` bigint(20) DEFAULT NULL COMMENT '关联ID',
  `remark` tinytext,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_no` (`transaction_no`),
  KEY `idx_transaction_type` (`transaction_type`),
  KEY `idx_account_id` (`account_id`),
  KEY `idx_target_account_id` (`target_account_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收支记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fin_transaction`
--

LOCK TABLES `fin_transaction` WRITE;
/*!40000 ALTER TABLE `fin_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `fin_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_area`
--

DROP TABLE IF EXISTS `inv_area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_area` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库区ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `area_name` varchar(50) NOT NULL COMMENT '库区名称',
  `area_code` varchar(50) DEFAULT NULL COMMENT '库区编码',
  `status` tinyint(4) DEFAULT '1',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouse_id` (`warehouse_id`,`area_code`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库区表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_area`
--

LOCK TABLES `inv_area` WRITE;
/*!40000 ALTER TABLE `inv_area` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_batch`
--

DROP TABLE IF EXISTS `inv_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `batch_number` varchar(50) NOT NULL COMMENT '批次号/序列号',
  `stock_qty` int(11) DEFAULT '0' COMMENT '库存数量',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiry_date` date DEFAULT NULL COMMENT '到期日期',
  `entry_time` datetime DEFAULT NULL COMMENT '入库时间',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouse_id` (`warehouse_id`,`goods_id`,`sku_id`,`batch_number`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`),
  KEY `idx_batch_number` (`batch_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='批次/序列号表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_batch`
--

LOCK TABLES `inv_batch` WRITE;
/*!40000 ALTER TABLE `inv_batch` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_check`
--

DROP TABLE IF EXISTS `inv_check`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_check` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '盘点单ID',
  `check_no` varchar(50) NOT NULL COMMENT '盘点单号',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `status` tinyint(4) DEFAULT '1',
  `check_time` date DEFAULT NULL COMMENT '盘点日期',
  `begin_time` datetime DEFAULT NULL COMMENT '盘点开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '盘点结束时间',
  `check_user_id` bigint(20) DEFAULT NULL COMMENT '盘点人',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `check_no` (`check_no`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存盘点表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_check`
--

LOCK TABLES `inv_check` WRITE;
/*!40000 ALTER TABLE `inv_check` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_check` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_check_item`
--

DROP TABLE IF EXISTS `inv_check_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_check_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `check_id` bigint(20) NOT NULL COMMENT '盘点单ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `batch_number` varchar(50) DEFAULT NULL COMMENT '批次号',
  `system_qty` int(11) DEFAULT '0' COMMENT '系统数量',
  `actual_qty` int(11) DEFAULT '0' COMMENT '实际数量',
  `diff_qty` int(11) DEFAULT '0' COMMENT '差异数量',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_check_id` (`check_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存盘点明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_check_item`
--

LOCK TABLES `inv_check_item` WRITE;
/*!40000 ALTER TABLE `inv_check_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_check_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_location`
--

DROP TABLE IF EXISTS `inv_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_location` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库位ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `area_id` bigint(20) DEFAULT NULL COMMENT '库区ID',
  `location_name` varchar(50) NOT NULL COMMENT '库位名称',
  `location_code` varchar(50) DEFAULT NULL COMMENT '库位编码',
  `status` tinyint(4) DEFAULT '1',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouse_id` (`warehouse_id`,`location_code`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_area_id` (`area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库位表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_location`
--

LOCK TABLES `inv_location` WRITE;
/*!40000 ALTER TABLE `inv_location` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_location` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_stock`
--

DROP TABLE IF EXISTS `inv_stock`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `stock_qty` int(11) DEFAULT '0' COMMENT '库存数量',
  `lock_qty` int(11) DEFAULT '0' COMMENT '锁定数量',
  `available_qty` int(11) DEFAULT '0' COMMENT '可用数量',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouse_id` (`warehouse_id`,`goods_id`,`sku_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_stock`
--

LOCK TABLES `inv_stock` WRITE;
/*!40000 ALTER TABLE `inv_stock` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_stock` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_stock_flow`
--

DROP TABLE IF EXISTS `inv_stock_flow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_stock_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `batch_number` varchar(50) DEFAULT NULL COMMENT '批次号',
  `flow_type` int(11) NOT NULL,
  `change_qty` int(11) NOT NULL COMMENT '变动数量',
  `before_qty` int(11) DEFAULT NULL COMMENT '变动前数量',
  `after_qty` int(11) DEFAULT NULL COMMENT '变动后数量',
  `order_id` bigint(20) DEFAULT NULL COMMENT '关联订单ID',
  `order_item_id` bigint(20) DEFAULT NULL COMMENT '关联订单项ID',
  `ref_id` bigint(20) DEFAULT NULL COMMENT '关联单据ID',
  `ref_type` varchar(50) DEFAULT NULL COMMENT '关联单据类型',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`),
  KEY `idx_flow_type` (`flow_type`),
  KEY `idx_ref_id` (`ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存流水表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_stock_flow`
--

LOCK TABLES `inv_stock_flow` WRITE;
/*!40000 ALTER TABLE `inv_stock_flow` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_stock_flow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_transfer`
--

DROP TABLE IF EXISTS `inv_transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_transfer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '调拨单ID',
  `transfer_no` varchar(50) NOT NULL COMMENT '调拨单号',
  `from_warehouse_id` bigint(20) NOT NULL COMMENT '源仓库ID',
  `to_warehouse_id` bigint(20) NOT NULL COMMENT '目标仓库ID',
  `status` tinyint(4) DEFAULT '1',
  `transfer_time` date DEFAULT NULL COMMENT '调拨日期',
  `out_time` datetime DEFAULT NULL COMMENT '出库时间',
  `in_time` datetime DEFAULT NULL COMMENT '入库时间',
  `transfer_user_id` bigint(20) DEFAULT NULL COMMENT '调拨人',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `transfer_no` (`transfer_no`),
  KEY `idx_from_warehouse_id` (`from_warehouse_id`),
  KEY `idx_to_warehouse_id` (`to_warehouse_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存调拨表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_transfer`
--

LOCK TABLES `inv_transfer` WRITE;
/*!40000 ALTER TABLE `inv_transfer` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_transfer_item`
--

DROP TABLE IF EXISTS `inv_transfer_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_transfer_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `transfer_id` bigint(20) NOT NULL COMMENT '调拨单ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `batch_number` varchar(50) DEFAULT NULL COMMENT '批次号',
  `transfer_qty` int(11) NOT NULL COMMENT '调拨数量',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_transfer_id` (`transfer_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存调拨明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_transfer_item`
--

LOCK TABLES `inv_transfer_item` WRITE;
/*!40000 ALTER TABLE `inv_transfer_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_transfer_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inv_warehouse`
--

DROP TABLE IF EXISTS `inv_warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inv_warehouse` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '仓库ID',
  `warehouse_name` varchar(50) NOT NULL COMMENT '仓库名称',
  `warehouse_code` varchar(50) DEFAULT NULL COMMENT '仓库编码',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `status` tinyint(4) DEFAULT '1',
  `is_default` tinyint(4) DEFAULT '0',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inv_warehouse`
--

LOCK TABLES `inv_warehouse` WRITE;
/*!40000 ALTER TABLE `inv_warehouse` DISABLE KEYS */;
/*!40000 ALTER TABLE `inv_warehouse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mem_address`
--

DROP TABLE IF EXISTS `mem_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mem_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `consignee` varchar(50) NOT NULL COMMENT '收货人',
  `mobile` varchar(20) NOT NULL COMMENT '手机号',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `detail_address` varchar(200) NOT NULL COMMENT '详细地址',
  `zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `is_default` tinyint(4) DEFAULT '0',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员收货地址表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mem_address`
--

LOCK TABLES `mem_address` WRITE;
/*!40000 ALTER TABLE `mem_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `mem_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mem_collection`
--

DROP TABLE IF EXISTS `mem_collection`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mem_collection` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_id` (`member_id`,`goods_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员收藏表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mem_collection`
--

LOCK TABLES `mem_collection` WRITE;
/*!40000 ALTER TABLE `mem_collection` DISABLE KEYS */;
/*!40000 ALTER TABLE `mem_collection` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mem_footprint`
--

DROP TABLE IF EXISTS `mem_footprint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mem_footprint` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员足迹表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mem_footprint`
--

LOCK TABLES `mem_footprint` WRITE;
/*!40000 ALTER TABLE `mem_footprint` DISABLE KEYS */;
/*!40000 ALTER TABLE `mem_footprint` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mem_growth`
--

DROP TABLE IF EXISTS `mem_growth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mem_growth` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `growth` int(11) NOT NULL COMMENT '成长值变动',
  `type` int(11) NOT NULL,
  `operation_type` int(11) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `before_growth` int(11) DEFAULT NULL COMMENT '变动前成长值',
  `after_growth` int(11) DEFAULT NULL COMMENT '变动后成长值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员成长值表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mem_growth`
--

LOCK TABLES `mem_growth` WRITE;
/*!40000 ALTER TABLE `mem_growth` DISABLE KEYS */;
/*!40000 ALTER TABLE `mem_growth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mem_level`
--

DROP TABLE IF EXISTS `mem_level`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mem_level` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '等级ID',
  `level_name` varchar(50) NOT NULL COMMENT '等级名称',
  `level_value` int(11) NOT NULL COMMENT '等级值',
  `growth_min` int(11) NOT NULL COMMENT '所需最低成长值',
  `growth_max` int(11) NOT NULL COMMENT '所需最高成长值',
  `discount` decimal(3,2) DEFAULT '1.00' COMMENT '折扣率',
  `description` text COMMENT '等级描述',
  `icon` varchar(255) DEFAULT NULL COMMENT '等级图标',
  `status` tinyint(4) DEFAULT '1',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `level_value` (`level_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员等级表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mem_level`
--

LOCK TABLES `mem_level` WRITE;
/*!40000 ALTER TABLE `mem_level` DISABLE KEYS */;
/*!40000 ALTER TABLE `mem_level` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mem_member`
--

DROP TABLE IF EXISTS `mem_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mem_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint(4) DEFAULT '0',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `level_id` bigint(20) DEFAULT '1' COMMENT '会员等级ID',
  `points` int(11) DEFAULT '0' COMMENT '积分',
  `growth` int(11) DEFAULT '0' COMMENT '成长值',
  `status` tinyint(4) DEFAULT '1',
  `source` int(11) DEFAULT NULL,
  `invite_code` varchar(20) DEFAULT NULL COMMENT '邀请码',
  `inviter_id` bigint(20) DEFAULT NULL COMMENT '邀请人ID',
  `total_order_amount` decimal(10,2) DEFAULT '0.00' COMMENT '累计消费金额',
  `order_count` int(11) DEFAULT '0' COMMENT '订单数量',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT NULL COMMENT '最后登录IP',
  `register_time` datetime NOT NULL COMMENT '注册时间',
  `register_ip` varchar(50) DEFAULT NULL COMMENT '注册IP',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_time` datetime(6) NOT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `mobile` (`mobile`),
  KEY `idx_level_id` (`level_id`),
  KEY `idx_inviter_id` (`inviter_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mem_member`
--

LOCK TABLES `mem_member` WRITE;
/*!40000 ALTER TABLE `mem_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `mem_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mem_points`
--

DROP TABLE IF EXISTS `mem_points`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mem_points` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `points` int(11) NOT NULL COMMENT '积分变动值',
  `type` int(11) NOT NULL,
  `operation_type` int(11) NOT NULL,
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `before_points` int(11) DEFAULT NULL COMMENT '变动前积分',
  `after_points` int(11) DEFAULT NULL COMMENT '变动后积分',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mem_points`
--

LOCK TABLES `mem_points` WRITE;
/*!40000 ALTER TABLE `mem_points` DISABLE KEYS */;
/*!40000 ALTER TABLE `mem_points` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_cart`
--

DROP TABLE IF EXISTS `ord_cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ord_cart` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `quantity` int(11) DEFAULT '1' COMMENT '数量',
  `checked` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `member_id` (`member_id`,`goods_id`,`sku_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_cart`
--

LOCK TABLES `ord_cart` WRITE;
/*!40000 ALTER TABLE `ord_cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `ord_cart` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_logistics`
--

DROP TABLE IF EXISTS `ord_logistics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ord_logistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `delivery_no` varchar(50) NOT NULL COMMENT '物流单号',
  `delivery_company` varchar(50) DEFAULT NULL COMMENT '物流公司',
  `delivery_company_code` varchar(50) DEFAULT NULL COMMENT '物流公司编码',
  `delivery_status` tinyint(4) DEFAULT '0',
  `consignee` varchar(50) NOT NULL COMMENT '收货人',
  `mobile` varchar(20) NOT NULL COMMENT '联系电话',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `address` varchar(200) NOT NULL COMMENT '详细地址',
  `zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `tracking_info` text COMMENT '物流跟踪信息',
  `tracking_update_time` datetime DEFAULT NULL COMMENT '物流更新时间',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_delivery_no` (`delivery_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单物流表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_logistics`
--

LOCK TABLES `ord_logistics` WRITE;
/*!40000 ALTER TABLE `ord_logistics` DISABLE KEYS */;
/*!40000 ALTER TABLE `ord_logistics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_operation_log`
--

DROP TABLE IF EXISTS `ord_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ord_operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `operation_type` int(11) NOT NULL,
  `operation_content` varchar(500) NOT NULL COMMENT '操作内容',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
  `operator_type` tinyint(4) DEFAULT '1',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_operation_log`
--

LOCK TABLES `ord_operation_log` WRITE;
/*!40000 ALTER TABLE `ord_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `ord_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_order`
--

DROP TABLE IF EXISTS `ord_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ord_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `order_type` tinyint(4) DEFAULT '1',
  `order_source` tinyint(4) DEFAULT '1',
  `order_status` tinyint(4) DEFAULT '1',
  `pay_status` tinyint(4) DEFAULT '0',
  `delivery_status` tinyint(4) DEFAULT '0',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '订单总金额',
  `goods_amount` decimal(10,2) DEFAULT '0.00' COMMENT '商品总金额',
  `freight_amount` decimal(10,2) DEFAULT '0.00' COMMENT '运费金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `coupon_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠券金额',
  `points_amount` decimal(10,2) DEFAULT '0.00' COMMENT '积分抵扣金额',
  `pay_amount` decimal(10,2) DEFAULT '0.00' COMMENT '实付金额',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `pay_type` int(11) DEFAULT NULL,
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '支付交易号',
  `consignee` varchar(50) DEFAULT NULL COMMENT '收货人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `zip_code` varchar(20) DEFAULT NULL COMMENT '邮编',
  `delivery_company` varchar(50) DEFAULT NULL COMMENT '物流公司',
  `delivery_no` varchar(50) DEFAULT NULL COMMENT '物流单号',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
  `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `cancel_reason` varchar(200) DEFAULT NULL COMMENT '取消原因',
  `buyer_remark` varchar(200) DEFAULT NULL COMMENT '买家备注',
  `seller_remark` varchar(200) DEFAULT NULL COMMENT '卖家备注',
  `invoice_type` tinyint(4) DEFAULT '0',
  `invoice_title` varchar(100) DEFAULT NULL COMMENT '发票抬头',
  `invoice_tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `invoice_content` varchar(100) DEFAULT NULL COMMENT '发票内容',
  `invoice_email` varchar(100) DEFAULT NULL COMMENT '发票邮箱',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_pay_status` (`pay_status`),
  KEY `idx_delivery_status` (`delivery_status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_order`
--

LOCK TABLES `ord_order` WRITE;
/*!40000 ALTER TABLE `ord_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `ord_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_order_item`
--

DROP TABLE IF EXISTS `ord_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ord_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `sku_name` varchar(100) DEFAULT NULL COMMENT 'SKU名称',
  `goods_image` varchar(255) DEFAULT NULL COMMENT '商品图片',
  `goods_spec` varchar(200) DEFAULT NULL COMMENT '商品规格',
  `goods_price` decimal(10,2) NOT NULL COMMENT '商品单价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `quantity` int(11) NOT NULL COMMENT '购买数量',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `real_amount` decimal(10,2) NOT NULL COMMENT '实际金额',
  `refund_status` tinyint(4) DEFAULT '0',
  `refund_amount` decimal(10,2) DEFAULT '0.00' COMMENT '退款金额',
  `comment_status` tinyint(4) DEFAULT '0',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_order_item`
--

LOCK TABLES `ord_order_item` WRITE;
/*!40000 ALTER TABLE `ord_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `ord_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_payment`
--

DROP TABLE IF EXISTS `ord_payment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ord_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '支付ID',
  `pay_no` varchar(50) NOT NULL COMMENT '支付流水号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `pay_type` int(11) DEFAULT NULL,
  `pay_amount` decimal(10,2) NOT NULL COMMENT '支付金额',
  `pay_status` tinyint(4) DEFAULT '0',
  `transaction_id` varchar(100) DEFAULT NULL COMMENT '支付交易号',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `refund_amount` decimal(10,2) DEFAULT '0.00' COMMENT '退款金额',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端IP',
  `device_info` varchar(100) DEFAULT NULL COMMENT '设备信息',
  `pay_channel_extra` text COMMENT '支付渠道额外信息',
  `callback_content` text COMMENT '回调内容',
  `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pay_no` (`pay_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_transaction_id` (`transaction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单支付表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_payment`
--

LOCK TABLES `ord_payment` WRITE;
/*!40000 ALTER TABLE `ord_payment` DISABLE KEYS */;
/*!40000 ALTER TABLE `ord_payment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ord_return`
--

DROP TABLE IF EXISTS `ord_return`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ord_return` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退货ID',
  `return_no` varchar(50) NOT NULL COMMENT '退货单号',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单编号',
  `order_item_id` bigint(20) DEFAULT NULL COMMENT '订单项ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `return_type` int(11) NOT NULL,
  `return_status` tinyint(4) DEFAULT '1',
  `reason_type` int(11) DEFAULT NULL,
  `reason` varchar(200) DEFAULT NULL COMMENT '退货原因',
  `description` text COMMENT '详细描述',
  `evidence` varchar(500) DEFAULT NULL COMMENT '凭证图片，多个以逗号分隔',
  `return_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `return_qty` int(11) DEFAULT NULL COMMENT '退货数量',
  `return_freight_amount` decimal(10,2) DEFAULT '0.00' COMMENT '退运费金额',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_user_id` bigint(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `reject_reason` varchar(200) DEFAULT NULL COMMENT '拒绝原因',
  `delivery_company` varchar(50) DEFAULT NULL COMMENT '退货物流公司',
  `delivery_no` varchar(50) DEFAULT NULL COMMENT '退货物流单号',
  `delivery_time` datetime DEFAULT NULL COMMENT '退货发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '退货签收时间',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_transaction_id` varchar(100) DEFAULT NULL COMMENT '退款交易号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `return_no` (`return_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_return_status` (`return_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单退换货表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ord_return`
--

LOCK TABLES `ord_return` WRITE;
/*!40000 ALTER TABLE `ord_return` DISABLE KEYS */;
/*!40000 ALTER TABLE `ord_return` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_department`
--

DROP TABLE IF EXISTS `org_department`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门ID',
  `org_id` bigint(20) NOT NULL COMMENT '所属组织ID',
  `dept_name` varchar(50) NOT NULL COMMENT '部门名称',
  `dept_code` varchar(50) NOT NULL COMMENT '部门编码',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门ID',
  `leader` varchar(50) DEFAULT NULL COMMENT '部门负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_id` (`org_id`,`dept_code`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='部门表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_department`
--

LOCK TABLES `org_department` WRITE;
/*!40000 ALTER TABLE `org_department` DISABLE KEYS */;
INSERT INTO `org_department` VALUES (1,1,'技术部','TECH',0,'技术总监',NULL,NULL,1,0,NULL,'2025-08-05 19:27:02',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `org_department` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_position`
--

DROP TABLE IF EXISTS `org_position`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_position` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime(6) NOT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `update_by` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  `dept_id` bigint(20) DEFAULT NULL,
  `org_id` bigint(20) NOT NULL,
  `position_code` varchar(50) NOT NULL,
  `position_name` varchar(50) NOT NULL,
  `remark` text,
  `sort` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_position`
--

LOCK TABLES `org_position` WRITE;
/*!40000 ALTER TABLE `org_position` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_position` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_region`
--

DROP TABLE IF EXISTS `org_region`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_region` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '区域ID',
  `region_name` varchar(50) NOT NULL COMMENT '区域名称',
  `region_code` varchar(50) NOT NULL COMMENT '区域编码',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父区域ID',
  `level` int(11) DEFAULT '1' COMMENT '层级',
  `full_name` varchar(200) DEFAULT NULL COMMENT '完整名称',
  `status` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `region_code` (`region_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='区域表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_region`
--

LOCK TABLES `org_region` WRITE;
/*!40000 ALTER TABLE `org_region` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_region` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_store`
--

DROP TABLE IF EXISTS `org_store`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `org_store` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '门店ID',
  `org_id` bigint(20) NOT NULL COMMENT '所属组织ID',
  `store_name` varchar(100) NOT NULL COMMENT '门店名称',
  `store_code` varchar(50) NOT NULL COMMENT '门店编码',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `latitude` decimal(38,2) DEFAULT NULL,
  `longitude` decimal(38,2) DEFAULT NULL,
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `business_hours` varchar(100) DEFAULT NULL COMMENT '营业时间',
  `store_area` decimal(38,2) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL COMMENT '门店图片',
  `status` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` tinytext,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `store_code` (`store_code`),
  KEY `idx_org_id` (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='门店表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_store`
--

LOCK TABLES `org_store` WRITE;
/*!40000 ALTER TABLE `org_store` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_store` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_attribute`
--

DROP TABLE IF EXISTS `prod_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_attribute` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '属性ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `attr_name` varchar(50) NOT NULL COMMENT '属性名',
  `attr_value` varchar(200) DEFAULT NULL COMMENT '属性值',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `attr_code` varchar(50) DEFAULT NULL,
  `attr_type` int(11) NOT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `is_filterable` tinyint(4) DEFAULT '0',
  `is_required` int(11) DEFAULT NULL,
  `is_searchable` int(11) DEFAULT NULL,
  `attr_group` int(11) DEFAULT NULL,
  `attr_unit` varchar(20) DEFAULT NULL,
  `default_value` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `is_detail_show` int(11) DEFAULT NULL,
  `is_list_show` int(11) DEFAULT NULL,
  `max_value` double DEFAULT NULL,
  `min_value` double DEFAULT NULL,
  `placeholder` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `validation_message` varchar(100) DEFAULT NULL,
  `validation_regex` varchar(200) DEFAULT NULL,
  `attribute_code` varchar(50) NOT NULL,
  `attribute_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品属性表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_attribute`
--

LOCK TABLES `prod_attribute` WRITE;
/*!40000 ALTER TABLE `prod_attribute` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_brand`
--

DROP TABLE IF EXISTS `prod_brand`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_brand` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
  `brand_name` varchar(50) NOT NULL COMMENT '品牌名称',
  `brand_logo` varchar(255) DEFAULT NULL COMMENT '品牌LOGO',
  `description` varchar(200) DEFAULT NULL COMMENT '品牌描述',
  `website` varchar(100) DEFAULT NULL COMMENT '品牌网址',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `brand_name` (`brand_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='商品品牌表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_brand`
--

LOCK TABLES `prod_brand` WRITE;
/*!40000 ALTER TABLE `prod_brand` DISABLE KEYS */;
INSERT INTO `prod_brand` VALUES (1,'苹果',NULL,'Apple品牌',NULL,1,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(2,'华为',NULL,'HUAWEI品牌',NULL,2,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(3,'小米',NULL,'MI品牌',NULL,3,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `prod_brand` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_category`
--

DROP TABLE IF EXISTS `prod_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类ID',
  `level` int(11) DEFAULT '1' COMMENT '层级 1-一级 2-二级 3-三级',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(11) DEFAULT NULL,
  `is_show` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_category`
--

LOCK TABLES `prod_category` WRITE;
/*!40000 ALTER TABLE `prod_category` DISABLE KEYS */;
INSERT INTO `prod_category` VALUES (1,'电子产品',0,1,NULL,1,1,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(2,'服装鞋帽',0,1,NULL,2,1,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(3,'家居用品',0,1,NULL,3,1,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `prod_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_goods`
--

DROP TABLE IF EXISTS `prod_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `goods_code` varchar(50) DEFAULT NULL COMMENT '商品编码',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `brand_id` bigint(20) DEFAULT NULL COMMENT '品牌ID',
  `goods_type` int(11) DEFAULT NULL,
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `weight` decimal(10,2) DEFAULT NULL COMMENT '重量(kg)',
  `volume` decimal(10,2) DEFAULT NULL COMMENT '体积(m³)',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `selling_price` decimal(10,2) NOT NULL COMMENT '售价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `min_price` decimal(10,2) DEFAULT NULL COMMENT '最低售价',
  `stock_qty` int(11) DEFAULT '0' COMMENT '库存数量',
  `warn_stock` int(11) DEFAULT '0' COMMENT '库存预警值',
  `sale_qty` int(11) DEFAULT '0' COMMENT '销量',
  `status` int(11) DEFAULT NULL,
  `is_recommended` int(11) DEFAULT NULL,
  `is_hot` int(11) DEFAULT NULL,
  `is_new` int(11) DEFAULT NULL,
  `keywords` varchar(200) DEFAULT NULL COMMENT '关键词',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签',
  `main_image` varchar(255) DEFAULT NULL COMMENT '主图',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `brief` varchar(255) DEFAULT NULL COMMENT '简介',
  `description` text COMMENT '详情描述',
  `remark` text COMMENT '备注',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `audit_status` int(11) DEFAULT NULL,
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_user_id` bigint(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_brand_id` (`brand_id`),
  KEY `idx_goods_name` (`goods_name`),
  KEY `idx_status` (`status`),
  KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_goods`
--

LOCK TABLES `prod_goods` WRITE;
/*!40000 ALTER TABLE `prod_goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_image`
--

DROP TABLE IF EXISTS `prod_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `is_main` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `update_by` varchar(255) DEFAULT NULL,
  `update_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_image`
--

LOCK TABLES `prod_image` WRITE;
/*!40000 ALTER TABLE `prod_image` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_review`
--

DROP TABLE IF EXISTS `prod_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_review` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_item_id` bigint(20) DEFAULT NULL COMMENT '订单明细ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `content` text COMMENT '评价内容',
  `images` varchar(500) DEFAULT NULL COMMENT '评价图片，多个以逗号分隔',
  `star` tinyint(4) DEFAULT '5',
  `is_anonymous` tinyint(4) DEFAULT '0',
  `status` tinyint(4) DEFAULT '1',
  `reply_content` text COMMENT '商家回复',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `reply_user_id` bigint(20) DEFAULT NULL COMMENT '回复人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_review`
--

LOCK TABLES `prod_review` WRITE;
/*!40000 ALTER TABLE `prod_review` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_sku`
--

DROP TABLE IF EXISTS `prod_sku`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_sku` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_code` varchar(50) DEFAULT NULL COMMENT 'SKU编码',
  `sku_name` varchar(100) NOT NULL COMMENT 'SKU名称',
  `spec_values` text COMMENT '规格值JSON',
  `original_price` decimal(10,2) DEFAULT NULL COMMENT '原价',
  `selling_price` decimal(10,2) NOT NULL COMMENT '售价',
  `cost_price` decimal(10,2) DEFAULT NULL COMMENT '成本价',
  `stock_qty` int(11) DEFAULT '0' COMMENT '库存数量',
  `warn_stock` int(11) DEFAULT '0' COMMENT '库存预警值',
  `sale_qty` int(11) DEFAULT '0' COMMENT '已售数量',
  `weight` int(11) DEFAULT NULL,
  `volume` int(11) DEFAULT NULL,
  `sku_image` varchar(255) DEFAULT NULL COMMENT 'SKU图片',
  `barcode` varchar(50) DEFAULT NULL COMMENT '条形码',
  `status` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `min_price` decimal(10,2) DEFAULT NULL,
  `sale_price` decimal(10,2) DEFAULT NULL,
  `sort_order` int(11) DEFAULT NULL,
  `spec_attrs` text,
  `stock_quantity` int(11) DEFAULT NULL,
  `warning_stock` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品SKU表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_sku`
--

LOCK TABLES `prod_sku` WRITE;
/*!40000 ALTER TABLE `prod_sku` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_sku` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prod_specification`
--

DROP TABLE IF EXISTS `prod_specification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prod_specification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '规格ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `spec_name` varchar(50) NOT NULL COMMENT '规格名称',
  `spec_values` text COMMENT '规格值JSON',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `category_id` bigint(20) DEFAULT NULL,
  `default_value` varchar(100) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `is_required` int(11) DEFAULT NULL,
  `is_searchable` int(11) DEFAULT NULL,
  `is_sku` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `spec_code` varchar(50) NOT NULL,
  `spec_type` int(11) NOT NULL,
  `spec_unit` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `validation_message` varchar(100) DEFAULT NULL,
  `validation_regex` varchar(200) DEFAULT NULL,
  `specification_code` varchar(50) NOT NULL,
  `specification_name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_9at3wp1v6k9i38curpkvukhwk` (`spec_code`),
  KEY `idx_goods_id` (`goods_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品规格表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prod_specification`
--

LOCK TABLES `prod_specification` WRITE;
/*!40000 ALTER TABLE `prod_specification` DISABLE KEYS */;
/*!40000 ALTER TABLE `prod_specification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_coupon`
--

DROP TABLE IF EXISTS `prom_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `coupon_name` varchar(100) NOT NULL COMMENT '优惠券名称',
  `coupon_type` tinyint(4) DEFAULT '1',
  `discount_type` tinyint(4) DEFAULT '1',
  `discount_value` decimal(10,2) NOT NULL COMMENT '优惠值（金额/折扣）',
  `min_amount` decimal(10,2) DEFAULT '0.00' COMMENT '使用门槛金额',
  `max_discount` decimal(10,2) DEFAULT NULL COMMENT '最大优惠金额',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `validity_days` int(11) DEFAULT NULL COMMENT '有效天数（领取后）',
  `total_qty` int(11) DEFAULT NULL COMMENT '发行总量',
  `used_qty` int(11) DEFAULT '0' COMMENT '已使用数量',
  `receive_qty` int(11) DEFAULT '0' COMMENT '已领取数量',
  `per_limit` int(11) DEFAULT '1' COMMENT '每人限领',
  `use_range` tinyint(4) DEFAULT '1',
  `range_values` text COMMENT '范围值JSON',
  `status` tinyint(4) DEFAULT '1',
  `description` text COMMENT '使用说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_coupon_type` (`coupon_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_coupon`
--

LOCK TABLES `prom_coupon` WRITE;
/*!40000 ALTER TABLE `prom_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_full_reduction`
--

DROP TABLE IF EXISTS `prom_full_reduction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_full_reduction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `activity_name` varchar(100) NOT NULL COMMENT '活动名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(4) DEFAULT '0',
  `use_range` tinyint(4) DEFAULT '1',
  `range_values` text COMMENT '范围值JSON',
  `rules` text NOT NULL COMMENT '满减规则JSON',
  `description` text COMMENT '活动说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='满减活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_full_reduction`
--

LOCK TABLES `prom_full_reduction` WRITE;
/*!40000 ALTER TABLE `prom_full_reduction` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_full_reduction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_group_buy`
--

DROP TABLE IF EXISTS `prom_group_buy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_group_buy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `activity_name` varchar(100) NOT NULL COMMENT '活动名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(4) DEFAULT '0',
  `group_num` int(11) NOT NULL COMMENT '成团人数',
  `limit_qty` int(11) DEFAULT '1' COMMENT '每人限购',
  `limit_group_qty` int(11) DEFAULT NULL COMMENT '每人限参团',
  `group_time` int(11) NOT NULL COMMENT '成团时限(小时)',
  `description` text COMMENT '活动说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `activity_code` varchar(50) DEFAULT NULL,
  `allow_auto_group` tinyint(4) DEFAULT '0',
  `creator_id` bigint(20) DEFAULT NULL,
  `discount_rate` double DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `join_limit` int(11) DEFAULT NULL,
  `leader_discount_type` varchar(20) DEFAULT NULL,
  `leader_free` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_group_buy`
--

LOCK TABLES `prom_group_buy` WRITE;
/*!40000 ALTER TABLE `prom_group_buy` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_group_buy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_group_buy_goods`
--

DROP TABLE IF EXISTS `prom_group_buy_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_group_buy_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_buy_id` bigint(20) NOT NULL COMMENT '拼团活动ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `group_price` decimal(10,2) NOT NULL COMMENT '拼团价格',
  `stock_qty` int(11) NOT NULL COMMENT '活动库存',
  `limit_qty` int(11) DEFAULT '1' COMMENT '每人限购',
  `sold_qty` int(11) DEFAULT '0' COMMENT '已售数量',
  `virtual_group_num` int(11) DEFAULT '0' COMMENT '虚拟成团数',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_buy_id` (`group_buy_id`,`goods_id`,`sku_id`),
  KEY `idx_group_buy_id` (`group_buy_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_group_buy_goods`
--

LOCK TABLES `prom_group_buy_goods` WRITE;
/*!40000 ALTER TABLE `prom_group_buy_goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_group_buy_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_group_buy_member`
--

DROP TABLE IF EXISTS `prom_group_buy_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_group_buy_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_record_id` bigint(20) NOT NULL COMMENT '拼团记录ID',
  `group_no` varchar(50) NOT NULL COMMENT '团编号',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '订单编号',
  `is_leader` tinyint(4) DEFAULT '0',
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_group_record_id` (`group_record_id`),
  KEY `idx_group_no` (`group_no`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团成员表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_group_buy_member`
--

LOCK TABLES `prom_group_buy_member` WRITE;
/*!40000 ALTER TABLE `prom_group_buy_member` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_group_buy_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_group_buy_record`
--

DROP TABLE IF EXISTS `prom_group_buy_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_group_buy_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `group_buy_id` bigint(20) NOT NULL COMMENT '拼团活动ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `group_no` varchar(50) NOT NULL COMMENT '团编号',
  `leader_id` bigint(20) NOT NULL COMMENT '团长ID',
  `required_num` int(11) NOT NULL COMMENT '需要人数',
  `current_num` int(11) DEFAULT '1' COMMENT '当前人数',
  `status` tinyint(4) DEFAULT '1',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `expire_time` datetime NOT NULL COMMENT '到期时间',
  `success_time` datetime DEFAULT NULL COMMENT '成团时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `group_no` (`group_no`),
  KEY `idx_group_buy_id` (`group_buy_id`),
  KEY `idx_leader_id` (`leader_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='拼团记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_group_buy_record`
--

LOCK TABLES `prom_group_buy_record` WRITE;
/*!40000 ALTER TABLE `prom_group_buy_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_group_buy_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_member_coupon`
--

DROP TABLE IF EXISTS `prom_member_coupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_member_coupon` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `coupon_id` bigint(20) NOT NULL COMMENT '优惠券ID',
  `coupon_code` varchar(50) NOT NULL COMMENT '优惠券码',
  `status` tinyint(4) DEFAULT '1',
  `get_type` tinyint(4) DEFAULT '1',
  `get_time` datetime NOT NULL COMMENT '获取时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `order_id` bigint(20) DEFAULT NULL COMMENT '使用订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '使用订单号',
  `use_amount` decimal(38,2) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `update_time` datetime(6) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_status` (`status`),
  KEY `idx_coupon_code` (`coupon_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员优惠券表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_member_coupon`
--

LOCK TABLES `prom_member_coupon` WRITE;
/*!40000 ALTER TABLE `prom_member_coupon` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_member_coupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_points_goods`
--

DROP TABLE IF EXISTS `prom_points_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_points_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `points_price` int(11) NOT NULL COMMENT '积分价格',
  `cash_price` decimal(10,2) DEFAULT '0.00' COMMENT '现金价格',
  `stock_qty` int(11) NOT NULL COMMENT '兑换库存',
  `limit_qty` int(11) DEFAULT '1' COMMENT '每人限兑',
  `exchange_qty` int(11) DEFAULT '0' COMMENT '已兑换数量',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(4) DEFAULT '0',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='积分商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_points_goods`
--

LOCK TABLES `prom_points_goods` WRITE;
/*!40000 ALTER TABLE `prom_points_goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_points_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_seckill`
--

DROP TABLE IF EXISTS `prom_seckill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_seckill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '活动ID',
  `activity_name` varchar(100) NOT NULL COMMENT '活动名称',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime NOT NULL COMMENT '结束时间',
  `status` tinyint(4) DEFAULT '0',
  `limit_qty` int(11) DEFAULT '1' COMMENT '每人限购',
  `description` text COMMENT '活动说明',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_time` (`start_time`,`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_seckill`
--

LOCK TABLES `prom_seckill` WRITE;
/*!40000 ALTER TABLE `prom_seckill` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_seckill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prom_seckill_goods`
--

DROP TABLE IF EXISTS `prom_seckill_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `prom_seckill_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `seckill_id` bigint(20) NOT NULL COMMENT '秒杀活动ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `seckill_price` decimal(10,2) NOT NULL COMMENT '秒杀价格',
  `stock_qty` int(11) NOT NULL COMMENT '秒杀库存',
  `limit_qty` int(11) DEFAULT '1' COMMENT '每人限购',
  `sold_qty` int(11) DEFAULT '0' COMMENT '已售数量',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `seckill_id` (`seckill_id`,`goods_id`,`sku_id`),
  KEY `idx_seckill_id` (`seckill_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秒杀商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prom_seckill_goods`
--

LOCK TABLES `prom_seckill_goods` WRITE;
/*!40000 ALTER TABLE `prom_seckill_goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `prom_seckill_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_order`
--

DROP TABLE IF EXISTS `pur_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '采购单ID',
  `order_no` varchar(50) NOT NULL COMMENT '采购单号',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `order_status` tinyint(4) DEFAULT '1',
  `warehouse_id` bigint(20) DEFAULT NULL COMMENT '入库仓库ID',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '订单总金额',
  `pay_status` tinyint(4) DEFAULT '0',
  `paid_amount` decimal(10,2) DEFAULT '0.00' COMMENT '已付金额',
  `expected_time` date DEFAULT NULL COMMENT '预计到货日期',
  `delivery_status` tinyint(4) DEFAULT '0',
  `receipt_status` tinyint(4) DEFAULT '0',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `remark` text COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '制单人ID',
  `audit_id` bigint(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `order_no` (`order_no`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_order`
--

LOCK TABLES `pur_order` WRITE;
/*!40000 ALTER TABLE `pur_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_order_item`
--

DROP TABLE IF EXISTS `pur_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `order_id` bigint(20) NOT NULL COMMENT '采购单ID',
  `order_no` varchar(50) NOT NULL COMMENT '采购单号',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `sku_name` varchar(100) DEFAULT NULL COMMENT 'SKU名称',
  `goods_spec` varchar(200) DEFAULT NULL COMMENT '商品规格',
  `goods_unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `purchase_price` decimal(10,2) NOT NULL COMMENT '采购价',
  `quantity` int(11) NOT NULL COMMENT '采购数量',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `received_qty` int(11) DEFAULT '0' COMMENT '已入库数量',
  `remain_qty` int(11) DEFAULT '0' COMMENT '待入库数量',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_order_item`
--

LOCK TABLES `pur_order_item` WRITE;
/*!40000 ALTER TABLE `pur_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_receipt`
--

DROP TABLE IF EXISTS `pur_receipt`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_receipt` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '入库单ID',
  `receipt_no` varchar(50) NOT NULL COMMENT '入库单号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '采购单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '采购单号',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `receipt_status` tinyint(4) DEFAULT '1',
  `receipt_type` tinyint(4) DEFAULT '1',
  `receipt_time` date DEFAULT NULL COMMENT '入库日期',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '入库总金额',
  `receipt_user_id` bigint(20) DEFAULT NULL COMMENT '入库人',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `remark` text COMMENT '备注',
  `audit_id` bigint(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `receipt_no` (`receipt_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_order_no` (`order_no`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购入库表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_receipt`
--

LOCK TABLES `pur_receipt` WRITE;
/*!40000 ALTER TABLE `pur_receipt` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_receipt` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_receipt_item`
--

DROP TABLE IF EXISTS `pur_receipt_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_receipt_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `receipt_id` bigint(20) NOT NULL COMMENT '入库单ID',
  `receipt_no` varchar(50) NOT NULL COMMENT '入库单号',
  `order_id` bigint(20) DEFAULT NULL COMMENT '采购单ID',
  `order_item_id` bigint(20) DEFAULT NULL COMMENT '采购单项ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `sku_name` varchar(100) DEFAULT NULL COMMENT 'SKU名称',
  `goods_spec` varchar(200) DEFAULT NULL COMMENT '商品规格',
  `goods_unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `batch_number` varchar(50) DEFAULT NULL COMMENT '批次号',
  `production_date` date DEFAULT NULL COMMENT '生产日期',
  `expiry_date` date DEFAULT NULL COMMENT '到期日期',
  `purchase_price` decimal(10,2) NOT NULL COMMENT '采购价',
  `quantity` int(11) NOT NULL COMMENT '入库数量',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `location_id` bigint(20) DEFAULT NULL COMMENT '库位ID',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_receipt_id` (`receipt_id`),
  KEY `idx_receipt_no` (`receipt_no`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购入库明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_receipt_item`
--

LOCK TABLES `pur_receipt_item` WRITE;
/*!40000 ALTER TABLE `pur_receipt_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_receipt_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_reconciliation`
--

DROP TABLE IF EXISTS `pur_reconciliation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_reconciliation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `reconciliation_no` varchar(50) NOT NULL COMMENT '对账单号',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `start_date` date DEFAULT NULL COMMENT '对账开始日期',
  `end_date` date DEFAULT NULL COMMENT '对账结束日期',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '对账总金额',
  `paid_amount` decimal(10,2) DEFAULT '0.00' COMMENT '已付金额',
  `unpaid_amount` decimal(10,2) DEFAULT '0.00' COMMENT '未付金额',
  `status` tinyint(4) DEFAULT '1',
  `settlement_time` datetime DEFAULT NULL COMMENT '结算时间',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `reconciliation_no` (`reconciliation_no`),
  KEY `idx_supplier_id` (`supplier_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商对账表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_reconciliation`
--

LOCK TABLES `pur_reconciliation` WRITE;
/*!40000 ALTER TABLE `pur_reconciliation` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_reconciliation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_reconciliation_item`
--

DROP TABLE IF EXISTS `pur_reconciliation_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_reconciliation_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `reconciliation_id` bigint(20) NOT NULL COMMENT '对账单ID',
  `reconciliation_no` varchar(50) NOT NULL COMMENT '对账单号',
  `bill_type` int(11) NOT NULL,
  `bill_id` bigint(20) NOT NULL COMMENT '单据ID',
  `bill_no` varchar(50) NOT NULL COMMENT '单据编号',
  `bill_date` date DEFAULT NULL COMMENT '单据日期',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_reconciliation_id` (`reconciliation_id`),
  KEY `idx_reconciliation_no` (`reconciliation_no`),
  KEY `idx_bill_id` (`bill_id`),
  KEY `idx_bill_no` (`bill_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商对账明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_reconciliation_item`
--

LOCK TABLES `pur_reconciliation_item` WRITE;
/*!40000 ALTER TABLE `pur_reconciliation_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_reconciliation_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_return`
--

DROP TABLE IF EXISTS `pur_return`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_return` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '退货单ID',
  `return_no` varchar(50) NOT NULL COMMENT '退货单号',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `receipt_id` bigint(20) DEFAULT NULL COMMENT '关联入库单ID',
  `receipt_no` varchar(50) DEFAULT NULL COMMENT '关联入库单号',
  `return_status` tinyint(4) DEFAULT '1',
  `return_time` date DEFAULT NULL COMMENT '退货日期',
  `total_amount` decimal(10,2) DEFAULT '0.00' COMMENT '退货总金额',
  `reason_type` int(11) DEFAULT NULL,
  `reason` varchar(200) DEFAULT NULL COMMENT '退货原因',
  `remark` text COMMENT '备注',
  `audit_id` bigint(20) DEFAULT NULL COMMENT '审核人ID',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_remark` varchar(200) DEFAULT NULL COMMENT '审核备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `return_no` (`return_no`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_warehouse_id` (`warehouse_id`),
  KEY `idx_receipt_id` (`receipt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购退货表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_return`
--

LOCK TABLES `pur_return` WRITE;
/*!40000 ALTER TABLE `pur_return` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_return` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_return_item`
--

DROP TABLE IF EXISTS `pur_return_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_return_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `return_id` bigint(20) NOT NULL COMMENT '退货单ID',
  `return_no` varchar(50) NOT NULL COMMENT '退货单号',
  `receipt_id` bigint(20) DEFAULT NULL COMMENT '关联入库单ID',
  `receipt_item_id` bigint(20) DEFAULT NULL COMMENT '关联入库单项ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `goods_name` varchar(100) NOT NULL COMMENT '商品名称',
  `sku_name` varchar(100) DEFAULT NULL COMMENT 'SKU名称',
  `goods_spec` varchar(200) DEFAULT NULL COMMENT '商品规格',
  `goods_unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `batch_number` varchar(50) DEFAULT NULL COMMENT '批次号',
  `purchase_price` decimal(10,2) NOT NULL COMMENT '采购价',
  `quantity` int(11) NOT NULL COMMENT '退货数量',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_return_id` (`return_id`),
  KEY `idx_return_no` (`return_no`),
  KEY `idx_receipt_id` (`receipt_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购退货明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_return_item`
--

LOCK TABLES `pur_return_item` WRITE;
/*!40000 ALTER TABLE `pur_return_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_return_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_supplier`
--

DROP TABLE IF EXISTS `pur_supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `supplier_name` varchar(100) NOT NULL COMMENT '供应商名称',
  `supplier_code` varchar(50) DEFAULT NULL COMMENT '供应商编码',
  `contact` varchar(50) DEFAULT NULL COMMENT '联系人',
  `mobile` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `province` varchar(50) DEFAULT NULL COMMENT '省份',
  `city` varchar(50) DEFAULT NULL COMMENT '城市',
  `district` varchar(50) DEFAULT NULL COMMENT '区县',
  `address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '开户行',
  `bank_account` varchar(50) DEFAULT NULL COMMENT '银行账号',
  `tax_no` varchar(50) DEFAULT NULL COMMENT '税号',
  `status` tinyint(4) DEFAULT '1',
  `level` int(11) DEFAULT NULL,
  `credit_level` int(11) DEFAULT NULL,
  `settlement_type` int(11) DEFAULT NULL,
  `settlement_day` int(11) DEFAULT NULL COMMENT '结算天数',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `supplier_code` (`supplier_code`),
  KEY `idx_supplier_name` (`supplier_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_supplier`
--

LOCK TABLES `pur_supplier` WRITE;
/*!40000 ALTER TABLE `pur_supplier` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pur_supplier_goods`
--

DROP TABLE IF EXISTS `pur_supplier_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pur_supplier_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `supplier_goods_code` varchar(50) DEFAULT NULL COMMENT '供应商商品编码',
  `supplier_goods_name` varchar(100) DEFAULT NULL COMMENT '供应商商品名称',
  `purchase_price` decimal(10,2) NOT NULL COMMENT '采购价',
  `min_purchase_qty` int(11) DEFAULT '1' COMMENT '最小采购量',
  `delivery_day` int(11) DEFAULT NULL COMMENT '交货天数',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `supplier_id` (`supplier_id`,`goods_id`,`sku_id`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_sku_id` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商商品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pur_supplier_goods`
--

LOCK TABLES `pur_supplier_goods` WRITE;
/*!40000 ALTER TABLE `pur_supplier_goods` DISABLE KEYS */;
/*!40000 ALTER TABLE `pur_supplier_goods` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_custom`
--

DROP TABLE IF EXISTS `report_custom`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_custom` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `report_name` varchar(100) NOT NULL COMMENT '报表名称',
  `report_code` varchar(50) NOT NULL COMMENT '报表编码',
  `report_type` int(11) NOT NULL,
  `data_source` varchar(100) NOT NULL COMMENT '数据源',
  `sql_content` tinytext,
  `params` tinytext,
  `fields` tinytext,
  `chart_config` tinytext,
  `status` int(11) DEFAULT NULL,
  `remark` tinytext,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `report_code` (`report_code`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自定义报表表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_custom`
--

LOCK TABLES `report_custom` WRITE;
/*!40000 ALTER TABLE `report_custom` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_custom` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_customer`
--

DROP TABLE IF EXISTS `report_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `report_date` date NOT NULL COMMENT '报表日期',
  `new_customer_count` int(11) DEFAULT '0' COMMENT '新增客户数',
  `total_customer_count` int(11) DEFAULT '0' COMMENT '累计客户数',
  `active_customer_count` int(11) DEFAULT '0' COMMENT '活跃客户数',
  `transaction_customer_count` int(11) DEFAULT '0' COMMENT '成交客户数',
  `lost_customer_count` int(11) DEFAULT '0' COMMENT '流失客户数',
  `transaction_rate` decimal(38,2) DEFAULT NULL,
  `retention_rate` decimal(38,2) DEFAULT NULL,
  `customer_unit_price` decimal(38,2) DEFAULT NULL,
  `repurchase_rate` decimal(38,2) DEFAULT NULL,
  `stats_type` int(11) NOT NULL,
  `dimension` varchar(50) DEFAULT NULL COMMENT '统计维度',
  `dimension_id` bigint(20) DEFAULT NULL COMMENT '维度ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `report_date` (`report_date`,`stats_type`,`dimension`,`dimension_id`),
  KEY `idx_report_date` (`report_date`),
  KEY `idx_stats_type` (`stats_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户分析表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_customer`
--

LOCK TABLES `report_customer` WRITE;
/*!40000 ALTER TABLE `report_customer` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_product`
--

DROP TABLE IF EXISTS `report_product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `report_date` date NOT NULL COMMENT '报表日期',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `sales_count` int(11) DEFAULT '0' COMMENT '销量',
  `sales_amount` decimal(38,2) DEFAULT NULL,
  `refund_count` int(11) DEFAULT '0' COMMENT '退款数量',
  `refund_amount` decimal(38,2) DEFAULT NULL,
  `view_count` int(11) DEFAULT '0' COMMENT '浏览次数',
  `favorite_count` int(11) DEFAULT '0' COMMENT '收藏次数',
  `cart_count` int(11) DEFAULT '0' COMMENT '加购次数',
  `conversion_rate` decimal(38,2) DEFAULT NULL,
  `stats_type` int(11) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `report_date` (`report_date`,`stats_type`,`goods_id`,`sku_id`),
  KEY `idx_report_date` (`report_date`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_category_id` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分析表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_product`
--

LOCK TABLES `report_product` WRITE;
/*!40000 ALTER TABLE `report_product` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `report_sales`
--

DROP TABLE IF EXISTS `report_sales`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `report_sales` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `report_date` date NOT NULL COMMENT '报表日期',
  `order_count` int(11) DEFAULT '0' COMMENT '订单数量',
  `order_amount` decimal(38,2) DEFAULT NULL,
  `paid_order_count` int(11) DEFAULT '0' COMMENT '已支付订单数',
  `paid_order_amount` decimal(38,2) DEFAULT NULL,
  `refund_order_count` int(11) DEFAULT '0' COMMENT '退款订单数',
  `refund_amount` decimal(38,2) DEFAULT NULL,
  `goods_count` int(11) DEFAULT '0' COMMENT '商品销量',
  `new_member_count` int(11) DEFAULT '0' COMMENT '新增会员数',
  `stats_type` int(11) NOT NULL,
  `dimension` varchar(50) DEFAULT NULL COMMENT '统计维度',
  `dimension_id` bigint(20) DEFAULT NULL COMMENT '维度ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `report_date` (`report_date`,`stats_type`,`dimension`,`dimension_id`),
  KEY `idx_report_date` (`report_date`),
  KEY `idx_stats_type` (`stats_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售报表表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `report_sales`
--

LOCK TABLES `report_sales` WRITE;
/*!40000 ALTER TABLE `report_sales` DISABLE KEYS */;
/*!40000 ALTER TABLE `report_sales` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serv_evaluation`
--

DROP TABLE IF EXISTS `serv_evaluation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serv_evaluation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `ticket_id` bigint(20) DEFAULT NULL COMMENT '工单ID',
  `ticket_no` varchar(50) DEFAULT NULL COMMENT '工单编号',
  `service_type` tinyint(4) NOT NULL COMMENT '服务类型 1-在线客服 2-电话客服 3-工单处理',
  `staff_id` bigint(20) DEFAULT NULL COMMENT '客服ID',
  `staff_name` varchar(50) DEFAULT NULL COMMENT '客服姓名',
  `score` int(11) NOT NULL,
  `content` varchar(500) DEFAULT NULL COMMENT '评价内容',
  `service_time` datetime DEFAULT NULL COMMENT '服务时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `attitude_score` int(11) DEFAULT NULL,
  `professional_score` int(11) DEFAULT NULL,
  `service_user_id` bigint(20) NOT NULL,
  `service_user_name` varchar(50) DEFAULT NULL,
  `speed_score` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_staff_id` (`staff_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服评价表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serv_evaluation`
--

LOCK TABLES `serv_evaluation` WRITE;
/*!40000 ALTER TABLE `serv_evaluation` DISABLE KEYS */;
/*!40000 ALTER TABLE `serv_evaluation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serv_repair`
--

DROP TABLE IF EXISTS `serv_repair`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serv_repair` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `repair_no` varchar(50) NOT NULL COMMENT '维修单号',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `order_id` bigint(20) DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单编号',
  `goods_id` bigint(20) NOT NULL COMMENT '商品ID',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'SKU ID',
  `problem_desc` text NOT NULL COMMENT '问题描述',
  `images` varchar(500) DEFAULT NULL COMMENT '图片，多个以逗号分隔',
  `status` tinyint(4) DEFAULT '1',
  `repair_type` int(11) NOT NULL,
  `repair_amount` decimal(10,2) DEFAULT NULL COMMENT '维修金额',
  `repair_result` text COMMENT '维修结果',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `start_time` datetime DEFAULT NULL COMMENT '维修开始时间',
  `finish_time` datetime DEFAULT NULL COMMENT '维修完成时间',
  `remark` text COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `address` varchar(200) DEFAULT NULL,
  `complete_time` datetime(6) DEFAULT NULL,
  `contact` varchar(50) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `goods_image` varchar(255) DEFAULT NULL,
  `goods_name` varchar(100) NOT NULL,
  `logistics_company` varchar(50) DEFAULT NULL,
  `logistics_no` varchar(50) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `repair_fee` decimal(10,2) DEFAULT '0.00',
  `return_logistics_company` varchar(50) DEFAULT NULL,
  `return_logistics_no` varchar(50) DEFAULT NULL,
  `return_time` datetime(6) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `repair_no` (`repair_no`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_goods_id` (`goods_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='维修记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serv_repair`
--

LOCK TABLES `serv_repair` WRITE;
/*!40000 ALTER TABLE `serv_repair` DISABLE KEYS */;
/*!40000 ALTER TABLE `serv_repair` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serv_ticket`
--

DROP TABLE IF EXISTS `serv_ticket`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serv_ticket` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '工单ID',
  `ticket_no` varchar(50) NOT NULL COMMENT '工单编号',
  `member_id` bigint(20) NOT NULL COMMENT '会员ID',
  `ticket_type` tinyint(4) NOT NULL COMMENT '工单类型 1-咨询 2-建议 3-投诉 4-售后 5-其他',
  `title` varchar(100) NOT NULL COMMENT '工单标题',
  `content` text NOT NULL COMMENT '工单内容',
  `images` varchar(500) DEFAULT NULL COMMENT '图片，多个以逗号分隔',
  `status` tinyint(4) DEFAULT '1',
  `priority` tinyint(4) DEFAULT '1',
  `order_id` bigint(20) DEFAULT NULL COMMENT '关联订单ID',
  `order_no` varchar(50) DEFAULT NULL COMMENT '关联订单编号',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '关联商品ID',
  `handler_id` bigint(20) DEFAULT NULL COMMENT '处理人ID',
  `handler_name` varchar(50) DEFAULT NULL COMMENT '处理人姓名',
  `handle_time` datetime DEFAULT NULL COMMENT '受理时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `evaluation_score` tinyint(4) DEFAULT NULL COMMENT '评价分数 1-5',
  `evaluation_content` varchar(500) DEFAULT NULL COMMENT '评价内容',
  `evaluation_time` datetime DEFAULT NULL COMMENT '评价时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `assign_time` datetime(6) DEFAULT NULL,
  `attachments` varchar(500) DEFAULT NULL,
  `contact` varchar(50) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL,
  `remark` text,
  `satisfaction` int(11) DEFAULT NULL,
  `service_user_id` bigint(20) DEFAULT NULL,
  `source` int(11) DEFAULT NULL,
  `type` int(11) NOT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `ticket_no` (`ticket_no`),
  KEY `idx_member_id` (`member_id`),
  KEY `idx_status` (`status`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serv_ticket`
--

LOCK TABLES `serv_ticket` WRITE;
/*!40000 ALTER TABLE `serv_ticket` DISABLE KEYS */;
/*!40000 ALTER TABLE `serv_ticket` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serv_ticket_record`
--

DROP TABLE IF EXISTS `serv_ticket_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serv_ticket_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `ticket_id` bigint(20) NOT NULL COMMENT '工单ID',
  `ticket_no` varchar(50) NOT NULL COMMENT '工单编号',
  `content` text NOT NULL COMMENT '跟进内容',
  `images` varchar(500) DEFAULT NULL COMMENT '图片，多个以逗号分隔',
  `operate_type` tinyint(4) NOT NULL COMMENT '操作类型 1-回复 2-转交 3-变更状态 4-其他',
  `operator_id` bigint(20) NOT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) NOT NULL COMMENT '操作人姓名',
  `operator_type` tinyint(4) DEFAULT '1' COMMENT '操作人类型 1-客服 2-会员',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `attachments` varchar(500) DEFAULT NULL,
  `handle_type` int(11) NOT NULL,
  `handler_id` bigint(20) DEFAULT NULL,
  `handler_name` varchar(50) DEFAULT NULL,
  `is_visible` tinyint(4) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `idx_ticket_id` (`ticket_id`),
  KEY `idx_ticket_no` (`ticket_no`),
  KEY `idx_operator_id` (`operator_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工单跟进表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serv_ticket_record`
--

LOCK TABLES `serv_ticket_record` WRITE;
/*!40000 ALTER TABLE `serv_ticket_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `serv_ticket_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_currency`
--

DROP TABLE IF EXISTS `sys_currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_currency` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '币种ID',
  `currency_code` varchar(10) NOT NULL COMMENT '币种代码',
  `currency_name` varchar(50) NOT NULL COMMENT '币种名称',
  `currency_symbol` varchar(10) NOT NULL COMMENT '币种符号',
  `precision_digit` int(11) DEFAULT '2' COMMENT '精度位数',
  `status` tinyint(4) DEFAULT '1',
  `is_default` tinyint(4) DEFAULT '0',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `decimal_places` int(11) DEFAULT '2',
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `currency_code` (`currency_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='币种表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_currency`
--

LOCK TABLES `sys_currency` WRITE;
/*!40000 ALTER TABLE `sys_currency` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_currency` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典ID',
  `dict_name` varchar(50) NOT NULL COMMENT '字典名称',
  `dict_code` varchar(50) NOT NULL COMMENT '字典编码',
  `description` varchar(200) DEFAULT NULL COMMENT '字典描述',
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `dict_code` (`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='数据字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
INSERT INTO `sys_dict` VALUES (1,'用户状态','user_status','用户状态字典',1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(2,'商品状态','goods_status','商品状态字典',1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(3,'订单状态','order_status','订单状态字典',1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(4,'通用状态','common_status','系统通用状态：启用/禁用',1,'2025-08-05 19:31:07','2025-08-05 19:31:07','system','system',0),(5,'性别','gender','通用性别字典，适用于用户、客户、联系人等所有需要性别信息的场景',1,'2025-08-05 19:31:07','2025-08-05 19:31:07','system','system',0);
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_item`
--

DROP TABLE IF EXISTS `sys_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_dict_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典项ID',
  `dict_id` bigint(20) NOT NULL COMMENT '字典ID',
  `item_text` varchar(50) NOT NULL COMMENT '字典项文本',
  `item_value` varchar(50) NOT NULL COMMENT '字典项值',
  `description` varchar(200) DEFAULT NULL COMMENT '字典项描述',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_dict_id` (`dict_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='数据字典项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_item`
--

LOCK TABLES `sys_dict_item` WRITE;
/*!40000 ALTER TABLE `sys_dict_item` DISABLE KEYS */;
INSERT INTO `sys_dict_item` VALUES (1,1,'禁用','0',NULL,1,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(2,1,'正常','1',NULL,2,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(3,2,'下架','0',NULL,1,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(4,2,'上架','1',NULL,2,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(5,3,'待付款','1',NULL,1,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(6,3,'待发货','2',NULL,2,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(7,3,'待收货','3',NULL,3,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(8,3,'已完成','4',NULL,4,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(9,3,'已取消','5',NULL,5,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0),(10,4,'禁用','0','禁用状态',1,1,'2025-08-05 19:31:07','2025-08-05 19:31:07','system','system',0),(11,4,'正常','1','正常状态',2,1,'2025-08-05 19:31:07','2025-08-05 19:31:07','system','system',0),(12,5,'未知','0','性别未知或不愿透露',1,1,'2025-08-05 19:31:07','2025-08-05 19:31:07','system','system',0),(13,5,'男','1','男性',2,1,'2025-08-05 19:31:07','2025-08-05 19:31:07','system','system',0),(14,5,'女','2','女性',3,1,'2025-08-05 19:31:07','2025-08-05 19:31:07','system','system',0);
/*!40000 ALTER TABLE `sys_dict_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_exchange_rate`
--

DROP TABLE IF EXISTS `sys_exchange_rate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_exchange_rate` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `source_currency` varchar(10) NOT NULL COMMENT '源币种',
  `target_currency` varchar(10) NOT NULL COMMENT '目标币种',
  `exchange_rate` decimal(16,8) NOT NULL,
  `effective_date` date NOT NULL COMMENT '生效日期',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `auto_update` tinyint(4) DEFAULT '0',
  `data_source` tinyint(4) DEFAULT '1',
  `effective_time` datetime(6) NOT NULL,
  `expiry_time` datetime(6) DEFAULT NULL,
  `last_update_time` datetime(6) DEFAULT NULL,
  `remark` varchar(200) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '1',
  `expiry_date` date DEFAULT NULL,
  `last_update_date` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `source_currency` (`source_currency`,`target_currency`,`effective_date`),
  KEY `idx_effective_date` (`effective_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='汇率表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_exchange_rate`
--

LOCK TABLES `sys_exchange_rate` WRITE;
/*!40000 ALTER TABLE `sys_exchange_rate` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_exchange_rate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_language`
--

DROP TABLE IF EXISTS `sys_language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_language` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '语言ID',
  `language_code` varchar(10) NOT NULL COMMENT '语言代码',
  `language_name` varchar(50) NOT NULL COMMENT '语言名称',
  `icon` varchar(255) DEFAULT NULL COMMENT '语言图标',
  `status` tinyint(4) DEFAULT '1',
  `is_default` tinyint(4) DEFAULT '0',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  `remark` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `language_code` (`language_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='语言表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_language`
--

LOCK TABLES `sys_language` WRITE;
/*!40000 ALTER TABLE `sys_language` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_language` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_log`
--

DROP TABLE IF EXISTS `sys_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '操作',
  `method` varchar(200) DEFAULT NULL COMMENT '方法名',
  `params` text COMMENT '请求参数',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `status` int(11) DEFAULT NULL,
  `error_msg` text COMMENT '错误消息',
  `operation_time` bigint(20) DEFAULT NULL COMMENT '操作耗时',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `request_uri` varchar(200) DEFAULT NULL COMMENT '请求URI',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_log`
--

LOCK TABLES `sys_log` WRITE;
/*!40000 ALTER TABLE `sys_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_login_log`
--

DROP TABLE IF EXISTS `sys_login_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '登录日志ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `login_type` int(11) DEFAULT NULL,
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `device_type` varchar(50) DEFAULT NULL COMMENT '设备类型',
  `os_type` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `browser_type` varchar(50) DEFAULT NULL COMMENT '浏览器类型',
  `status` int(11) DEFAULT NULL,
  `fail_reason` varchar(200) DEFAULT NULL COMMENT '失败原因',
  `login_time` datetime NOT NULL COMMENT '登录时间',
  `create_time` datetime(6) NOT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `update_time` datetime(6) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  KEY `idx_login_time` (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_login_log`
--

LOCK TABLES `sys_login_log` WRITE;
/*!40000 ALTER TABLE `sys_login_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_login_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `menu_code` varchar(50) NOT NULL COMMENT '菜单编码',
  `menu_type` int(11) NOT NULL,
  `path` varchar(200) DEFAULT NULL COMMENT '路由地址',
  `component` varchar(200) DEFAULT NULL COMMENT '组件路径',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `visible` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `permission_code` varchar(50) DEFAULT NULL COMMENT '权限标识',
  `is_frame` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `menu_code` (`menu_code`),
  UNIQUE KEY `UK_b679yhxaq3tpo2ri78i4tndgl` (`menu_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu` DISABLE KEYS */;
INSERT INTO `sys_menu` VALUES (19,0,'系统管理','system',1,'#','','Setting',1,1,1,'system',NULL,'2025-08-01 15:25:01','2025-08-04 16:53:16',NULL,'18845913092',0),(20,0,'组织架构','org',1,'#','','OfficeBuilding',2,1,1,'org',NULL,'2025-08-01 15:25:01','2025-08-04 21:29:21',NULL,'18845913092',0),(21,0,'商品管理','product',1,'#','Layout','Goods',3,1,1,'product',NULL,'2025-08-01 15:25:01','2025-08-01 15:25:01',NULL,NULL,0),(22,0,'个人中心','profile',2,'/profile','system/UserProfile','User',99,1,1,'profile',NULL,'2025-08-01 15:25:01','2025-08-01 15:25:01',NULL,NULL,0),(23,19,'用户管理','system:user',2,'/users','system/UserList','User',1,1,1,'user',NULL,'2025-08-01 15:25:11','2025-08-01 15:25:11',NULL,NULL,0),(24,19,'角色管理','system:role',2,'/roles','system/RoleList','UserFilled',2,1,1,'role',NULL,'2025-08-01 15:25:11','2025-08-01 15:25:11',NULL,NULL,0),(25,19,'权限管理','system:permission',2,'/permissions','system/PermissionList','Lock',3,1,1,'permission',NULL,'2025-08-01 15:25:11','2025-08-01 15:25:11',NULL,NULL,0),(26,19,'菜单管理','system:menu',2,'/menus','system/MenuList','Menu',4,1,1,'menu',NULL,'2025-08-01 15:25:11','2025-08-01 15:25:11',NULL,NULL,0),(27,19,'数据字典管理','system:dict',2,'/dicts','system/DictList','Collection',5,1,1,'dict',NULL,'2025-08-01 15:25:11','2025-08-01 15:25:11',NULL,NULL,0),(28,20,'组织机构管理','org:organization',2,'/organizations','system/OrganizationList','SetUp',1,1,1,'org-management',NULL,'2025-08-01 15:25:22','2025-08-01 15:25:22',NULL,NULL,0),(29,20,'部门管理','org:department',2,'/departments','system/DepartmentList','Files',2,1,1,'dept-management',NULL,'2025-08-01 15:25:22','2025-08-01 15:25:22',NULL,NULL,0),(30,20,'岗位管理','org:position',2,'/positions','system/PositionList','List',3,1,1,'position-management',NULL,'2025-08-01 15:25:22','2025-08-01 15:25:22',NULL,NULL,0),(31,21,'商品管理','product:goods',2,'/goods','product/GoodsList','Box',1,1,1,'product-info-management:list',NULL,'2025-08-01 15:25:31','2025-08-01 15:25:31',NULL,NULL,0),(32,21,'分类管理','product:category',2,'/categories','product/CategoryList','Grid',2,1,1,'product-category-management:list',NULL,'2025-08-01 15:25:31','2025-08-01 15:25:31',NULL,NULL,0),(33,21,'品牌管理','product:brand',2,'/brands','product/BrandList','Star',3,1,1,'product-brand-management:list',NULL,'2025-08-01 15:25:31','2025-08-01 15:25:31',NULL,NULL,0),(40,21,'规格管理','product:specification',2,'/specifications','product/SpecificationList','SetUp',4,1,1,'product-specification-management:list',0,'2025-08-04 12:02:28','2025-08-04 12:02:28','1','1',0),(41,21,'属性管理','product:attribute',2,'/attributes','product/AttributeList','List',5,1,1,'product-attribute-management:list',0,'2025-08-04 12:02:28','2025-08-04 12:02:28','1','1',0),(42,21,'SKU管理','product:sku',2,'/skus','product/SkuList','Grid',6,1,1,'product-sku-management:list',0,'2025-08-04 12:02:28','2025-08-04 12:02:28','1','1',0),(43,0,'数据面板','dashboard',2,'/home','HomeView','DataBoard',0,1,1,'dashboard',0,'2025-08-04 13:49:48','2025-08-04 14:24:06','1','18845913092',1);
/*!40000 ALTER TABLE `sys_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_message`
--

DROP TABLE IF EXISTS `sys_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `title` varchar(100) NOT NULL COMMENT '消息标题',
  `content` text NOT NULL COMMENT '消息内容',
  `message_type` int(11) NOT NULL,
  `target_type` int(11) NOT NULL,
  `target_ids` varchar(500) DEFAULT NULL COMMENT '接收对象ID集合',
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `status` tinyint(4) DEFAULT '1',
  `send_type` tinyint(4) DEFAULT '1',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idx_message_type` (`message_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_message`
--

LOCK TABLES `sys_message` WRITE;
/*!40000 ALTER TABLE `sys_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_organization`
--

DROP TABLE IF EXISTS `sys_organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_organization` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '组织ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父组织ID',
  `org_name` varchar(50) NOT NULL COMMENT '组织名称',
  `org_code` varchar(50) NOT NULL COMMENT '组织编码',
  `org_type` int(11) DEFAULT NULL,
  `leader` varchar(50) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `sort` int(11) DEFAULT '0' COMMENT '排序',
  `status` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_code` (`org_code`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='组织机构表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_organization`
--

LOCK TABLES `sys_organization` WRITE;
/*!40000 ALTER TABLE `sys_organization` DISABLE KEYS */;
INSERT INTO `sys_organization` VALUES (1,0,'VLIAS总公司','VLIAS_HQ',1,'系统管理员',NULL,NULL,NULL,0,1,'2025-08-05 19:27:02',NULL,NULL,NULL,0);
/*!40000 ALTER TABLE `sys_organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_param`
--

DROP TABLE IF EXISTS `sys_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '参数ID',
  `param_key` varchar(100) NOT NULL COMMENT '参数键',
  `param_value` varchar(500) NOT NULL COMMENT '参数值',
  `param_type` int(11) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL COMMENT '参数描述',
  `status` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `param_key` (`param_key`),
  KEY `idx_param_type` (`param_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统参数表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_param`
--

LOCK TABLES `sys_param` WRITE;
/*!40000 ALTER TABLE `sys_param` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `permission_name` varchar(50) NOT NULL COMMENT '权限名称',
  `permission_code` varchar(50) NOT NULL COMMENT '权限编码',
  `permission_type` int(11) NOT NULL,
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父权限ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '关联菜单ID',
  `description` varchar(200) DEFAULT NULL COMMENT '权限描述',
  `status` int(11) DEFAULT NULL,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `is_core` int(11) DEFAULT NULL,
  `level_depth` int(11) DEFAULT NULL,
  `permission_path` varchar(500) DEFAULT NULL,
  `sort_order` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission_code` (`permission_code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_menu_id` (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=167 DEFAULT CHARSET=utf8mb4 COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
INSERT INTO `sys_permission` VALUES (1,'组织架构','org',1,0,NULL,'组织架构管理功能',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,1,NULL,2),(2,'系统管理','system',1,0,NULL,'系统管理功能',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,1,NULL,1),(3,'商品管理','product',1,0,NULL,'商品管理功能',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,0,1,NULL,3),(4,'订单管理','order',1,0,NULL,'订单管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,4),(5,'客户管理','customer',1,0,NULL,'客户管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,5),(6,'库存管理','inventory',1,0,NULL,'库存管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,6),(7,'财务管理','finance',1,0,NULL,'财务管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,7),(8,'会员管理','member',1,0,NULL,'会员管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,8),(9,'营销管理','promotion',1,0,NULL,'营销管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,9),(10,'采购管理','purchase',1,0,NULL,'采购管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,10),(11,'供应链','supply',1,0,NULL,'供应链管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,11),(12,'分销管理','distribution',1,0,NULL,'分销管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,12),(13,'服务管理','service',1,0,NULL,'服务管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,13),(14,'报表分析','report',1,0,NULL,'报表分析模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,14),(15,'API管理','api',1,0,NULL,'API管理模块',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,1,NULL,15),(16,'组织机构管理','org-management',2,1,NULL,'组织机构管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,2,NULL,1),(17,'部门管理','dept-management',2,1,NULL,'部门管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,2,NULL,2),(18,'岗位管理','position-management',2,1,NULL,'岗位管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,2,NULL,3),(19,'区域管理','region-management',2,1,NULL,'区域管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(20,'用户管理','user-management',2,2,NULL,'用户管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,2,NULL,1),(21,'角色管理','role-management',2,2,NULL,'角色管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,2,NULL,2),(22,'权限管理','permission-management',2,2,NULL,'权限管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,2,NULL,3),(23,'菜单管理','menu-management',2,2,NULL,'菜单管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,2,NULL,4),(24,'字典管理','dict-management',2,2,NULL,'数据字典管理功能',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,0,2,NULL,5),(25,'系统配置','system-config',2,2,NULL,'系统配置管理',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,6),(31,'订单处理','order-process',2,4,NULL,'订单处理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(32,'支付管理','payment-management',2,4,NULL,'支付管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(33,'物流管理','logistics-management',2,4,NULL,'物流管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(34,'退换货管理','return-management',2,4,NULL,'退换货管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(35,'客户信息管理','customer-info',2,5,NULL,'客户信息管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(36,'客户分组管理','customer-group',2,5,NULL,'客户分组管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(37,'跟进记录管理','follow-record',2,5,NULL,'跟进记录管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(38,'公海管理','public-pool',2,5,NULL,'公海管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(39,'仓库管理','warehouse-management',2,6,NULL,'仓库管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(40,'库存查询','stock-query',2,6,NULL,'库存查询功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(41,'出入库管理','stock-flow',2,6,NULL,'出入库管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(42,'盘点管理','stock-check',2,6,NULL,'盘点管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(43,'账户管理','account-management',2,7,NULL,'账户管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(44,'收支管理','transaction-management',2,7,NULL,'收支管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(45,'发票管理','invoice-management',2,7,NULL,'发票管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(46,'结算管理','settlement-management',2,7,NULL,'结算管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(47,'会员信息管理','member-info',2,8,NULL,'会员信息管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(48,'会员等级管理','member-level',2,8,NULL,'会员等级管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(49,'积分管理','points-management',2,8,NULL,'积分管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(50,'地址管理','address-management',2,8,NULL,'地址管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(51,'优惠券管理','coupon-management',2,9,NULL,'优惠券管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(52,'促销活动管理','promotion-activity',2,9,NULL,'促销活动管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(53,'团购管理','group-buy',2,9,NULL,'团购管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(54,'秒杀管理','seckill-management',2,9,NULL,'秒杀管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(55,'采购订单管理','purchase-order',2,10,NULL,'采购订单管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(56,'供应商管理','supplier-management',2,10,NULL,'供应商管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(57,'收货管理','receipt-management',2,10,NULL,'收货管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(58,'采购退货管理','purchase-return',2,10,NULL,'采购退货管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(59,'供应链节点管理','supply-node',2,11,NULL,'供应链节点管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(60,'供应链关系管理','supply-relation',2,11,NULL,'供应链关系管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(61,'供应链监控','supply-monitor',2,11,NULL,'供应链监控功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(62,'分销商管理','distributor-management',2,12,NULL,'分销商管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(63,'分销等级管理','distribution-level',2,12,NULL,'分销等级管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(64,'佣金管理','commission-management',2,12,NULL,'佣金管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(65,'提现管理','withdraw-management',2,12,NULL,'提现管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(66,'工单管理','ticket-management',2,13,NULL,'工单管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(67,'维修管理','repair-management',2,13,NULL,'维修管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(68,'退换货服务','return-service',2,13,NULL,'退换货服务功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(69,'服务评价管理','evaluation-management',2,13,NULL,'服务评价管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(70,'销售报表','sales-report',2,14,NULL,'销售报表功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(71,'客户报表','customer-report',2,14,NULL,'客户报表功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(72,'商品报表','product-report',2,14,NULL,'商品报表功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(73,'自定义报表','custom-report',2,14,NULL,'自定义报表功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(74,'API配置管理','api-config',2,15,NULL,'API配置管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,1),(75,'API权限管理','api-permission',2,15,NULL,'API权限管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,2),(76,'API日志管理','api-log',2,15,NULL,'API日志管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,3),(77,'平台管理','platform-management',2,15,NULL,'平台管理功能',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,0,2,NULL,4),(78,'查看组织','org-management:view',3,16,NULL,'查看组织列表',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,1),(79,'新增组织','org-management:create',3,16,NULL,'新增组织',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,2),(80,'编辑组织','org-management:edit',3,16,NULL,'编辑组织信息',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,3),(81,'删除组织','org-management:delete',3,16,NULL,'删除组织',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,4),(82,'查看部门','dept-management:view',3,17,NULL,'查看部门列表',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,1),(83,'新增部门','dept-management:create',3,17,NULL,'新增部门',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,2),(84,'编辑部门','dept-management:edit',3,17,NULL,'编辑部门信息',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,3),(85,'删除部门','dept-management:delete',3,17,NULL,'删除部门',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,4),(86,'查看岗位','position-management:view',3,18,NULL,'查看岗位列表',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,1),(87,'新增岗位','position-management:create',3,18,NULL,'新增岗位',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,2),(88,'编辑岗位','position-management:edit',3,18,NULL,'编辑岗位信息',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,3),(89,'删除岗位','position-management:delete',3,18,NULL,'删除岗位',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,4),(90,'查看用户','user-management:view',3,20,NULL,'查看用户列表',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,1),(91,'新增用户','user-management:create',3,20,NULL,'新增用户',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,2),(92,'编辑用户','user-management:edit',3,20,NULL,'编辑用户信息',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,3),(93,'删除用户','user-management:delete',3,20,NULL,'删除用户',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,4),(94,'重置密码','user-management:reset-password',3,20,NULL,'重置用户密码权限',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,3,NULL,5),(95,'查看角色','role-management:view',3,21,NULL,'查看角色列表',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,1),(96,'新增角色','role-management:create',3,21,NULL,'新增角色',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,2),(97,'编辑角色','role-management:edit',3,21,NULL,'编辑角色信息',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,3),(98,'删除角色','role-management:delete',3,21,NULL,'删除角色',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,4),(99,'分配权限','role-management:assign-permission',3,21,NULL,'为角色分配权限',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,3,NULL,5),(100,'查看权限','permission-management:view',3,22,NULL,'查看权限列表',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,1),(101,'新增权限','permission-management:create',3,22,NULL,'新增权限',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,2),(102,'编辑权限','permission-management:edit',3,22,NULL,'编辑权限信息',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,3),(103,'删除权限','permission-management:delete',3,22,NULL,'删除权限',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,4),(104,'同步权限','permission-management:sync',3,22,NULL,'同步权限配置',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,3,NULL,5),(105,'重置权限','permission-management:reset',3,22,NULL,'重置权限配置',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,3,NULL,6),(106,'验证权限','permission-management:validate',3,22,NULL,'验证权限配置',1,'2025-08-05 19:34:33','2025-08-05 19:34:33',NULL,NULL,0,1,3,NULL,7),(107,'查看菜单','menu-management:view',3,23,NULL,'查看菜单列表',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,1),(108,'新增菜单','menu-management:create',3,23,NULL,'新增菜单',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,2),(109,'编辑菜单','menu-management:edit',3,23,NULL,'编辑菜单信息',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,3),(110,'删除菜单','menu-management:delete',3,23,NULL,'删除菜单',1,'2025-08-05 19:34:33','2025-08-05 21:28:00',NULL,'18845913092',0,1,3,NULL,4),(111,'个人中心','profile',1,0,NULL,'个人信息管理功能',1,'2025-08-05 20:03:14','2025-08-05 21:28:00',NULL,'18845913092',0,0,1,NULL,999),(113,'查看商品','product-info-management:view',3,151,NULL,'查看商品列表',1,'2025-08-05 20:03:14','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,1),(115,'编辑商品','product-info-management:edit',3,151,NULL,'编辑商品信息',1,'2025-08-05 20:03:14','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,3),(116,'删除商品','product-info-management:delete',3,151,NULL,'删除商品',1,'2025-08-05 20:03:14','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,4),(118,'查看分类','product-category-management:view',3,153,NULL,'查看分类列表',1,'2025-08-05 20:03:14','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,1),(120,'编辑分类','product-category-management:edit',3,153,NULL,'编辑分类信息',1,'2025-08-05 20:03:14','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,3),(121,'删除分类','product-category-management:delete',3,153,NULL,'删除分类',1,'2025-08-05 20:03:14','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,4),(125,'查看品牌','product-brand-management:view',3,155,NULL,'查看品牌列表',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,1),(127,'编辑品牌','product-brand-management:edit',3,155,NULL,'编辑品牌信息',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,3),(128,'删除品牌','product-brand-management:delete',3,155,NULL,'删除品牌',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,4),(131,'查看规格','product-specification-management:view',3,157,NULL,'查看规格列表',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,1),(133,'编辑规格','product-specification-management:edit',3,157,NULL,'编辑规格信息',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,3),(134,'删除规格','product-specification-management:delete',3,157,NULL,'删除规格',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,4),(137,'查看属性','product-attribute-management:view',3,159,NULL,'查看属性列表',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,1),(139,'编辑属性','product-attribute-management:edit',3,159,NULL,'编辑属性信息',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,3),(140,'删除属性','product-attribute-management:delete',3,159,NULL,'删除属性',1,'2025-08-05 20:04:46','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,4),(143,'查看SKU','product-sku-management:view',3,161,NULL,'查看SKU列表',1,'2025-08-05 20:04:47','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,1),(145,'编辑SKU','product-sku-management:edit',3,161,NULL,'编辑SKU信息',1,'2025-08-05 20:04:47','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,3),(146,'删除SKU','product-sku-management:delete',3,161,NULL,'删除SKU',1,'2025-08-05 20:04:47','2025-08-05 21:28:00',NULL,'18845913092',0,0,3,NULL,4),(147,'查看字典','dict-management:view',3,24,NULL,'查看字典列表',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,1),(148,'新增字典','dict-management:create',3,24,NULL,'新增字典',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(149,'编辑字典','dict-management:edit',3,24,NULL,'编辑字典信息',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,3),(150,'删除字典','dict-management:delete',3,24,NULL,'删除字典',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,4),(151,'商品信息管理','product-info-management',2,3,NULL,'商品信息管理功能',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,2,NULL,1),(152,'新增商品','product-info-management:create',3,151,NULL,'新增商品',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(153,'商品分类管理','product-category-management',2,3,NULL,'商品分类管理功能',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,2,NULL,2),(154,'新增分类','product-category-management:create',3,153,NULL,'新增分类',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(155,'商品品牌管理','product-brand-management',2,3,NULL,'商品品牌管理功能',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,2,NULL,3),(156,'新增品牌','product-brand-management:create',3,155,NULL,'新增品牌',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(157,'商品规格管理','product-specification-management',2,3,NULL,'商品规格管理功能',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,2,NULL,4),(158,'新增规格','product-specification-management:create',3,157,NULL,'新增规格',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(159,'商品属性管理','product-attribute-management',2,3,NULL,'商品属性管理功能',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,2,NULL,5),(160,'新增属性','product-attribute-management:create',3,159,NULL,'新增属性',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(161,'SKU管理','product-sku-management',2,3,NULL,'SKU管理功能',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,2,NULL,6),(162,'新增SKU','product-sku-management:create',3,161,NULL,'新增SKU',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(163,'个人信息管理','profile-info-management',2,111,NULL,'个人信息管理功能',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,2,NULL,1),(164,'查看信息','profile-info-management:view',3,163,NULL,'查看个人信息',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,1),(165,'编辑信息','profile-info-management:edit',3,163,NULL,'编辑个人信息',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,2),(166,'修改密码','profile-info-management:password',3,163,NULL,'修改个人密码',1,'2025-08-05 21:28:00','2025-08-05 21:28:00','18845913092','18845913092',0,0,3,NULL,3);
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名称',
  `role_code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` varchar(200) DEFAULT NULL COMMENT '角色描述',
  `status` int(11) DEFAULT NULL,
  `org_id` bigint(20) DEFAULT NULL COMMENT '所属组织ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_code` (`role_code`),
  UNIQUE KEY `UK_jqdita2l45v2gglry7bp8kl1f` (`role_code`),
  KEY `idx_org_id` (`org_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role` DISABLE KEYS */;
INSERT INTO `sys_role` VALUES (1,'超级管理员','admin','系统超级管理员，拥有所有权限',1,NULL,'2025-07-30 22:06:30','2025-08-04 23:47:09',NULL,NULL,0),(2,'品牌管理员','BRAND_ADMIN','品牌管理角色，只能管理品牌',1,NULL,'2025-08-01 12:45:58','2025-08-05 12:58:05','1','18845913092',0),(8,'经理','MANAGER','部门经理，拥有大部分管理权限',1,NULL,'2025-08-01 01:31:58','2025-08-01 01:31:58','system','system',0),(9,'员工','EMPLOYEE','普通员工，拥有基础权限',1,NULL,'2025-08-01 01:31:58','2025-08-01 01:31:58','system','system',0),(10,'仓库管理员','WAREHOUSE','仓库管理员，拥有库存相关权限',1,NULL,'2025-08-01 01:31:58','2025-08-01 01:31:58','system','system',0);
/*!40000 ALTER TABLE `sys_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_permission`
--

DROP TABLE IF EXISTS `sys_role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_id` (`role_id`,`permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=622 DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_permission`
--

LOCK TABLES `sys_role_permission` WRITE;
/*!40000 ALTER TABLE `sys_role_permission` DISABLE KEYS */;
INSERT INTO `sys_role_permission` VALUES (128,2,28,'2025-08-05 19:34:33',1),(129,2,3,'2025-08-05 19:34:33',1),(131,8,3,'2025-08-05 19:34:33',1),(132,8,4,'2025-08-05 19:34:33',1),(133,8,5,'2025-08-05 19:34:33',1),(134,8,6,'2025-08-05 19:34:33',1),(135,8,7,'2025-08-05 19:34:33',1),(136,8,8,'2025-08-05 19:34:33',1),(137,8,9,'2025-08-05 19:34:33',1),(138,8,10,'2025-08-05 19:34:33',1),(139,8,11,'2025-08-05 19:34:33',1),(140,8,12,'2025-08-05 19:34:33',1),(141,8,13,'2025-08-05 19:34:33',1),(142,8,14,'2025-08-05 19:34:33',1),(143,8,15,'2025-08-05 19:34:33',1),(144,8,19,'2025-08-05 19:34:33',1),(145,8,24,'2025-08-05 19:34:33',1),(146,8,25,'2025-08-05 19:34:33',1),(147,8,26,'2025-08-05 19:34:33',1),(148,8,27,'2025-08-05 19:34:33',1),(149,8,28,'2025-08-05 19:34:33',1),(150,8,29,'2025-08-05 19:34:33',1),(151,8,30,'2025-08-05 19:34:33',1),(152,8,31,'2025-08-05 19:34:33',1),(153,8,32,'2025-08-05 19:34:33',1),(154,8,33,'2025-08-05 19:34:33',1),(155,8,34,'2025-08-05 19:34:33',1),(156,8,35,'2025-08-05 19:34:33',1),(157,8,36,'2025-08-05 19:34:33',1),(158,8,37,'2025-08-05 19:34:33',1),(159,8,38,'2025-08-05 19:34:33',1),(160,8,39,'2025-08-05 19:34:33',1),(161,8,40,'2025-08-05 19:34:33',1),(162,8,41,'2025-08-05 19:34:33',1),(163,8,42,'2025-08-05 19:34:33',1),(164,8,43,'2025-08-05 19:34:33',1),(165,8,44,'2025-08-05 19:34:33',1),(166,8,45,'2025-08-05 19:34:33',1),(167,8,46,'2025-08-05 19:34:33',1),(168,8,47,'2025-08-05 19:34:33',1),(169,8,48,'2025-08-05 19:34:33',1),(170,8,49,'2025-08-05 19:34:33',1),(171,8,50,'2025-08-05 19:34:33',1),(172,8,51,'2025-08-05 19:34:33',1),(173,8,52,'2025-08-05 19:34:33',1),(174,8,53,'2025-08-05 19:34:33',1),(175,8,54,'2025-08-05 19:34:33',1),(176,8,55,'2025-08-05 19:34:33',1),(177,8,56,'2025-08-05 19:34:33',1),(178,8,57,'2025-08-05 19:34:33',1),(179,8,58,'2025-08-05 19:34:33',1),(180,8,59,'2025-08-05 19:34:33',1),(181,8,60,'2025-08-05 19:34:33',1),(182,8,61,'2025-08-05 19:34:33',1),(183,8,62,'2025-08-05 19:34:33',1),(184,8,63,'2025-08-05 19:34:33',1),(185,8,64,'2025-08-05 19:34:33',1),(186,8,65,'2025-08-05 19:34:33',1),(187,8,66,'2025-08-05 19:34:33',1),(188,8,67,'2025-08-05 19:34:33',1),(189,8,68,'2025-08-05 19:34:33',1),(190,8,69,'2025-08-05 19:34:33',1),(191,8,70,'2025-08-05 19:34:33',1),(192,8,71,'2025-08-05 19:34:33',1),(193,8,72,'2025-08-05 19:34:33',1),(194,8,73,'2025-08-05 19:34:33',1),(195,8,74,'2025-08-05 19:34:33',1),(196,8,75,'2025-08-05 19:34:33',1),(197,8,76,'2025-08-05 19:34:33',1),(198,8,77,'2025-08-05 19:34:33',1),(265,10,6,'2025-08-05 19:34:33',1),(266,10,42,'2025-08-05 19:34:33',1),(267,10,41,'2025-08-05 19:34:33',1),(268,10,40,'2025-08-05 19:34:33',1),(269,10,39,'2025-08-05 19:34:33',1),(482,9,113,'2025-08-05 21:34:33',NULL),(483,9,164,'2025-08-05 21:34:33',NULL),(484,9,3,'2025-08-05 21:34:33',NULL),(485,9,151,'2025-08-05 21:34:33',NULL),(486,9,111,'2025-08-05 21:34:33',NULL),(487,9,163,'2025-08-05 21:34:33',NULL),(488,1,2,'2025-08-05 21:35:24',NULL),(489,1,20,'2025-08-05 21:35:24',NULL),(490,1,90,'2025-08-05 21:35:24',NULL),(491,1,91,'2025-08-05 21:35:24',NULL),(492,1,92,'2025-08-05 21:35:24',NULL),(493,1,93,'2025-08-05 21:35:24',NULL),(494,1,94,'2025-08-05 21:35:24',NULL),(495,1,21,'2025-08-05 21:35:24',NULL),(496,1,95,'2025-08-05 21:35:24',NULL),(497,1,96,'2025-08-05 21:35:24',NULL),(498,1,97,'2025-08-05 21:35:24',NULL),(499,1,98,'2025-08-05 21:35:24',NULL),(500,1,99,'2025-08-05 21:35:24',NULL),(501,1,22,'2025-08-05 21:35:24',NULL),(502,1,100,'2025-08-05 21:35:24',NULL),(503,1,101,'2025-08-05 21:35:24',NULL),(504,1,102,'2025-08-05 21:35:24',NULL),(505,1,103,'2025-08-05 21:35:24',NULL),(506,1,104,'2025-08-05 21:35:24',NULL),(507,1,105,'2025-08-05 21:35:24',NULL),(508,1,106,'2025-08-05 21:35:24',NULL),(509,1,23,'2025-08-05 21:35:24',NULL),(510,1,107,'2025-08-05 21:35:24',NULL),(511,1,108,'2025-08-05 21:35:24',NULL),(512,1,109,'2025-08-05 21:35:24',NULL),(513,1,110,'2025-08-05 21:35:24',NULL),(514,1,24,'2025-08-05 21:35:24',NULL),(515,1,25,'2025-08-05 21:35:24',NULL),(516,1,1,'2025-08-05 21:35:24',NULL),(517,1,16,'2025-08-05 21:35:24',NULL),(518,1,78,'2025-08-05 21:35:24',NULL),(519,1,79,'2025-08-05 21:35:24',NULL),(520,1,80,'2025-08-05 21:35:24',NULL),(521,1,81,'2025-08-05 21:35:24',NULL),(522,1,17,'2025-08-05 21:35:24',NULL),(523,1,82,'2025-08-05 21:35:24',NULL),(524,1,83,'2025-08-05 21:35:24',NULL),(525,1,84,'2025-08-05 21:35:24',NULL),(526,1,85,'2025-08-05 21:35:24',NULL),(527,1,18,'2025-08-05 21:35:24',NULL),(528,1,86,'2025-08-05 21:35:24',NULL),(529,1,87,'2025-08-05 21:35:24',NULL),(530,1,88,'2025-08-05 21:35:24',NULL),(531,1,89,'2025-08-05 21:35:24',NULL),(532,1,19,'2025-08-05 21:35:24',NULL),(533,1,113,'2025-08-05 21:35:24',NULL),(534,1,115,'2025-08-05 21:35:24',NULL),(535,1,116,'2025-08-05 21:35:24',NULL),(536,1,118,'2025-08-05 21:35:24',NULL),(537,1,120,'2025-08-05 21:35:24',NULL),(538,1,121,'2025-08-05 21:35:24',NULL),(539,1,125,'2025-08-05 21:35:24',NULL),(540,1,127,'2025-08-05 21:35:24',NULL),(541,1,128,'2025-08-05 21:35:24',NULL),(542,1,131,'2025-08-05 21:35:24',NULL),(543,1,133,'2025-08-05 21:35:24',NULL),(544,1,134,'2025-08-05 21:35:24',NULL),(545,1,137,'2025-08-05 21:35:24',NULL),(546,1,139,'2025-08-05 21:35:24',NULL),(547,1,140,'2025-08-05 21:35:24',NULL),(548,1,143,'2025-08-05 21:35:24',NULL),(549,1,145,'2025-08-05 21:35:24',NULL),(550,1,146,'2025-08-05 21:35:24',NULL),(551,1,4,'2025-08-05 21:35:24',NULL),(552,1,31,'2025-08-05 21:35:24',NULL),(553,1,32,'2025-08-05 21:35:24',NULL),(554,1,33,'2025-08-05 21:35:24',NULL),(555,1,34,'2025-08-05 21:35:24',NULL),(556,1,5,'2025-08-05 21:35:24',NULL),(557,1,35,'2025-08-05 21:35:24',NULL),(558,1,36,'2025-08-05 21:35:24',NULL),(559,1,37,'2025-08-05 21:35:24',NULL),(560,1,38,'2025-08-05 21:35:24',NULL),(561,1,6,'2025-08-05 21:35:24',NULL),(562,1,39,'2025-08-05 21:35:24',NULL),(563,1,40,'2025-08-05 21:35:24',NULL),(564,1,41,'2025-08-05 21:35:24',NULL),(565,1,42,'2025-08-05 21:35:24',NULL),(566,1,7,'2025-08-05 21:35:24',NULL),(567,1,43,'2025-08-05 21:35:24',NULL),(568,1,44,'2025-08-05 21:35:24',NULL),(569,1,45,'2025-08-05 21:35:24',NULL),(570,1,46,'2025-08-05 21:35:24',NULL),(571,1,8,'2025-08-05 21:35:24',NULL),(572,1,47,'2025-08-05 21:35:24',NULL),(573,1,48,'2025-08-05 21:35:24',NULL),(574,1,49,'2025-08-05 21:35:24',NULL),(575,1,50,'2025-08-05 21:35:24',NULL),(576,1,9,'2025-08-05 21:35:24',NULL),(577,1,51,'2025-08-05 21:35:24',NULL),(578,1,52,'2025-08-05 21:35:24',NULL),(579,1,53,'2025-08-05 21:35:24',NULL),(580,1,54,'2025-08-05 21:35:24',NULL),(581,1,10,'2025-08-05 21:35:24',NULL),(582,1,55,'2025-08-05 21:35:24',NULL),(583,1,56,'2025-08-05 21:35:24',NULL),(584,1,57,'2025-08-05 21:35:24',NULL),(585,1,58,'2025-08-05 21:35:24',NULL),(586,1,11,'2025-08-05 21:35:24',NULL),(587,1,59,'2025-08-05 21:35:24',NULL),(588,1,60,'2025-08-05 21:35:24',NULL),(589,1,61,'2025-08-05 21:35:24',NULL),(590,1,12,'2025-08-05 21:35:24',NULL),(591,1,62,'2025-08-05 21:35:24',NULL),(592,1,63,'2025-08-05 21:35:24',NULL),(593,1,64,'2025-08-05 21:35:24',NULL),(594,1,65,'2025-08-05 21:35:24',NULL),(595,1,13,'2025-08-05 21:35:24',NULL),(596,1,66,'2025-08-05 21:35:24',NULL),(597,1,67,'2025-08-05 21:35:24',NULL),(598,1,68,'2025-08-05 21:35:24',NULL),(599,1,69,'2025-08-05 21:35:24',NULL),(600,1,14,'2025-08-05 21:35:24',NULL),(601,1,70,'2025-08-05 21:35:24',NULL),(602,1,71,'2025-08-05 21:35:24',NULL),(603,1,72,'2025-08-05 21:35:24',NULL),(604,1,73,'2025-08-05 21:35:24',NULL),(605,1,15,'2025-08-05 21:35:24',NULL),(606,1,74,'2025-08-05 21:35:24',NULL),(607,1,75,'2025-08-05 21:35:24',NULL),(608,1,76,'2025-08-05 21:35:24',NULL),(609,1,77,'2025-08-05 21:35:24',NULL),(610,1,111,'2025-08-05 21:35:24',NULL),(611,1,163,'2025-08-05 21:35:24',NULL),(612,1,164,'2025-08-05 21:35:24',NULL),(613,1,165,'2025-08-05 21:35:24',NULL),(614,1,166,'2025-08-05 21:35:24',NULL),(615,1,3,'2025-08-05 21:35:24',NULL),(616,1,151,'2025-08-05 21:35:24',NULL),(617,1,153,'2025-08-05 21:35:24',NULL),(618,1,155,'2025-08-05 21:35:24',NULL),(619,1,157,'2025-08-05 21:35:24',NULL),(620,1,159,'2025-08-05 21:35:24',NULL),(621,1,161,'2025-08-05 21:35:24',NULL);
/*!40000 ALTER TABLE `sys_role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_task`
--

DROP TABLE IF EXISTS `sys_task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_task` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `task_group` varchar(50) NOT NULL COMMENT '任务组名',
  `task_class` varchar(255) NOT NULL COMMENT '任务类',
  `task_method` varchar(100) NOT NULL COMMENT '任务方法',
  `task_params` varchar(500) DEFAULT NULL COMMENT '任务参数',
  `cron_expression` varchar(50) NOT NULL COMMENT 'cron表达式',
  `status` int(11) DEFAULT NULL,
  `remark` tinytext,
  `concurrent` int(11) DEFAULT NULL,
  `execute_times` int(11) DEFAULT '0' COMMENT '已执行次数',
  `last_execute_time` datetime DEFAULT NULL COMMENT '上次执行时间',
  `next_execute_time` datetime DEFAULT NULL COMMENT '下次执行时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `task_name` (`task_name`,`task_group`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_task`
--

LOCK TABLES `sys_task` WRITE;
/*!40000 ALTER TABLE `sys_task` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_task_log`
--

DROP TABLE IF EXISTS `sys_task_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_task_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `task_id` bigint(20) NOT NULL COMMENT '任务ID',
  `task_name` varchar(100) NOT NULL COMMENT '任务名称',
  `task_group` varchar(50) NOT NULL COMMENT '任务组名',
  `task_class` varchar(255) NOT NULL COMMENT '任务类',
  `task_method` varchar(100) NOT NULL COMMENT '任务方法',
  `task_params` varchar(500) DEFAULT NULL COMMENT '任务参数',
  `execution_time` bigint(20) DEFAULT NULL COMMENT '执行耗时(毫秒)',
  `status` int(11) DEFAULT NULL,
  `error_msg` tinytext,
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_task_id` (`task_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_task_log`
--

LOCK TABLES `sys_task_log` WRITE;
/*!40000 ALTER TABLE `sys_task_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_task_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_translation`
--

DROP TABLE IF EXISTS `sys_translation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_translation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '翻译ID',
  `language_code` varchar(10) NOT NULL COMMENT '语言代码',
  `translation_key` varchar(100) NOT NULL COMMENT '翻译键',
  `translation_value` text NOT NULL COMMENT '翻译值',
  `module` varchar(50) DEFAULT NULL COMMENT '所属模块',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  `update_by` bigint(20) DEFAULT NULL COMMENT '更新人',
  `is_deleted` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `language_code` (`language_code`,`translation_key`,`module`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='翻译表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_translation`
--

LOCK TABLES `sys_translation` WRITE;
/*!40000 ALTER TABLE `sys_translation` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_translation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `gender` int(11) DEFAULT NULL,
  `mobile` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `org_id` bigint(20) DEFAULT NULL COMMENT '所属组织ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '所属部门ID',
  `position_id` bigint(20) DEFAULT NULL COMMENT '岗位ID',
  `status` int(11) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(255) DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT '0',
  `emp_no` varchar(50) DEFAULT NULL,
  `user_type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `idx_org_id` (`org_id`),
  KEY `idx_dept_id` (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'18845913092','$2a$10$ZQzXOkem0hOhBEjR.5SiVurf/b2Z.Q3.9zs/HcnwTOvIM.hjEvevS','齐城旭',NULL,1,'18845913092','2608850328@qq.com',NULL,NULL,NULL,1,'2025-08-05 19:51:54','2025-08-05 19:45:05','2025-08-05 19:51:41',NULL,'18845913092',0,NULL,NULL);
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_message`
--

DROP TABLE IF EXISTS `sys_user_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_message` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `message_id` bigint(20) NOT NULL COMMENT '消息ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `is_read` int(11) DEFAULT NULL,
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `is_deleted` tinyint(4) DEFAULT '0',
  `update_time` datetime(6) DEFAULT NULL,
  `create_by` bigint(20) DEFAULT NULL,
  `update_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_message_id` (`message_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户消息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_message`
--

LOCK TABLES `sys_user_message` WRITE;
/*!40000 ALTER TABLE `sys_user_message` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_role`
--

DROP TABLE IF EXISTS `sys_user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_id` (`user_id`,`role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_role`
--

LOCK TABLES `sys_user_role` WRITE;
/*!40000 ALTER TABLE `sys_user_role` DISABLE KEYS */;
INSERT INTO `sys_user_role` VALUES (2,1,1,'2025-08-05 21:17:45',NULL);
/*!40000 ALTER TABLE `sys_user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'vliascrm'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-05 22:15:32
