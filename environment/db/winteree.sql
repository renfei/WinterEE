SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL COMMENT '客户端标识',
  `resource_ids` varchar(255) DEFAULT NULL COMMENT '接入资源列表',
  `client_secret` varchar(255) DEFAULT NULL COMMENT '客户端秘钥',
  `scope` varchar(255) DEFAULT NULL COMMENT '授权范围',
  `authorized_grant_types` varchar(255) DEFAULT NULL,
  `web_server_redirect_uri` varchar(255) DEFAULT NULL,
  `authorities` varchar(255) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` longtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `archived` tinyint(4) DEFAULT NULL,
  `trusted` tinyint(4) DEFAULT NULL,
  `autoapprove` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth2客户端';

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
BEGIN;
INSERT INTO `oauth_client_details` VALUES ('Browser', 'WinterEE', 'sha1:64000:18:mZqWx+7xCXJ1aM2PqxO8H7yYutCJ1Y9o:6zyReU4QGCYx8DADaEjdkZtS', 'WinterEE-Core-Serve', 'auto_password,refresh_token,verification_code', 'http://localhost/callback', NULL, 7200, 864000, NULL, '2020-04-09 17:45:13', 0, 0, 'false');
INSERT INTO `oauth_client_details` VALUES ('WinterEE-Core-Serve', 'WinterEE', 'sha1:64000:18:mZqWx+7xCXJ1aM2PqxO8H7yYutCJ1Y9o:6zyReU4QGCYx8DADaEjdkZtS', 'WinterEE-Core-Serve', 'client_credentials', 'http://localhost/callback', NULL, 7200, 864000, NULL, '2020-04-15 14:12:35', 0, 0, 'false');
INSERT INTO `oauth_client_details` VALUES ('WinterEE-Gateway-Serve', 'WinterEE', 'sha1:64000:18:mZqWx+7xCXJ1aM2PqxO8H7yYutCJ1Y9o:6zyReU4QGCYx8DADaEjdkZtS', 'WinterEE-Core-Serve', 'client_credentials', 'http://localhost/callback', NULL, 7200, 864000, NULL, '2020-04-13 14:55:27', 0, 0, 'false');
INSERT INTO `oauth_client_details` VALUES ('WinterEE-UAA-Serve', 'WinterEE', 'sha1:64000:18:mZqWx+7xCXJ1aM2PqxO8H7yYutCJ1Y9o:6zyReU4QGCYx8DADaEjdkZtS', 'WinterEE-Core-Serve', 'client_credentials', 'http://localhost/callback', NULL, 7200, 864000, NULL, '2020-04-09 17:45:17', 0, 0, 'false');
COMMIT;

-- ----------------------------
-- Table structure for oauth_code
-- ----------------------------
DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `code` varchar(255) DEFAULT NULL,
  `authentication` blob,
  KEY `code_index` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for winteree_core_ tenant
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_ tenant`;
CREATE TABLE `winteree_core_ tenant` (
  `uuid` varchar(36) NOT NULL COMMENT '租户ID',
  `name` varchar(255) NOT NULL COMMENT '租户名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `expiry_date` datetime NOT NULL COMMENT '到期时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户表';

-- ----------------------------
-- Records of winteree_core_ tenant
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_ tenant` VALUES ('BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '系统租户', '2020-04-07 10:02:23', '2099-12-31 12:59:59', 1);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_account
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_account`;
CREATE TABLE `winteree_core_account` (
  `uuid` varchar(36) NOT NULL,
  `tenant_id` varchar(36) NOT NULL COMMENT '租户ID',
  `create_time` datetime NOT NULL COMMENT '注册时间',
  `user_name` varchar(255) DEFAULT NULL COMMENT '用户名',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机',
  `passwd` varchar(255) DEFAULT NULL COMMENT '密码',
  `user_status` int(10) unsigned DEFAULT '1' COMMENT '状态',
  `lock_time` datetime DEFAULT NULL COMMENT '锁止时间',
  `error_count` int(11) NOT NULL DEFAULT '0' COMMENT '密码错误次数',
  `last_name` varchar(255) DEFAULT NULL COMMENT '姓氏',
  `first_name` varchar(255) DEFAULT NULL COMMENT '名字',
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `username` (`user_name`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `phone` (`phone`),
  KEY `fk_tenant_account` (`tenant_id`),
  CONSTRAINT `fk_tenant_account` FOREIGN KEY (`tenant_id`) REFERENCES `winteree_core_ tenant` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账户服务';

-- ----------------------------
-- Records of winteree_core_account
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_account` VALUES ('9369919A-F95E-44CF-AB0A-6BCD1D933403', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '2020-03-31 13:10:11', 'admin', NULL, NULL, 'sha1:64000:18:5eIrOGu492SUG8VVd0qsRJv+eQgt1E1I:ePI6MBezyKgMUP9rzNCG4SP0', 1, NULL, 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_log
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_log`;
CREATE TABLE `winteree_core_log` (
  `id` varchar(36) NOT NULL COMMENT 'ID',
  `date_time` datetime NOT NULL COMMENT '时间',
  `log_type` varchar(255) NOT NULL COMMENT '日志类别',
  `log_sub_type` varchar(255) DEFAULT NULL COMMENT '日志二级类别',
  `logValue` longtext COMMENT '日志内容',
  `tenant_id` varchar(36) DEFAULT NULL COMMENT '租户ID',
  `account_id` varchar(36) DEFAULT NULL COMMENT '账户ID',
  `client_id` varchar(36) DEFAULT NULL COMMENT '客户端编号',
  `client_ip` varchar(255) DEFAULT NULL COMMENT '客户端IP',
  `request_url` text COMMENT '请求URL',
  `request_method` varchar(255) DEFAULT NULL COMMENT '请求方式',
  `request_head` text COMMENT '请求头信息',
  `request_body` longtext COMMENT '请求体',
  `status_code` varchar(255) DEFAULT NULL COMMENT '响应状态码',
  `response_head` text COMMENT '响应头信息',
  `response_body` longtext COMMENT '响应体',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志表';

-- ----------------------------
-- Table structure for winteree_core_menu
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_menu`;
CREATE TABLE `winteree_core_menu` (
  `id` varchar(36) NOT NULL COMMENT '编号',
  `parent_id` varchar(36) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `href` varchar(2000) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_show` char(1) NOT NULL COMMENT '是否在菜单中显示',
  `is_menu` varchar(255) DEFAULT NULL COMMENT '是菜单还是权限',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `weight` int(11) NOT NULL DEFAULT '0' COMMENT '菜单权重',
  PRIMARY KEY (`id`),
  KEY `winteree_core_menu_parent_id` (`parent_id`),
  KEY `winteree_core_menu_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of winteree_core_menu
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_menu` VALUES ('19E712FB-808C-446C-8557-184103BB1B64', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '平台管理', 1, NULL, NULL, NULL, '1', NULL, NULL, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-07 14:17:48', '', '2020-04-07 14:18:19', NULL, '0', 0);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_office
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_office`;
CREATE TABLE `winteree_core_office` (
  `id` varchar(36) NOT NULL COMMENT '编号',
  `tenant_id` varchar(36) NOT NULL COMMENT '租户编号',
  `parent_id` varchar(36) NOT NULL COMMENT '父级编号',
  `parent_ids` varchar(2000) NOT NULL COMMENT '所有父级编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `PRIMARY_PERSON` varchar(64) DEFAULT NULL COMMENT '主负责人',
  `DEPUTY_PERSON` varchar(64) DEFAULT NULL COMMENT '副负责人',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `winteree_core_office_parent_id` (`parent_id`),
  KEY `winteree_core_office_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- ----------------------------
-- Table structure for winteree_core_role
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_role`;
CREATE TABLE `winteree_core_role` (
  `id` varchar(36) NOT NULL COMMENT '编号',
  `office_id` varchar(36) DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enname` varchar(255) DEFAULT NULL COMMENT '英文名称',
  `role_type` varchar(255) DEFAULT NULL COMMENT '角色类型',
  `data_scope` char(1) DEFAULT NULL COMMENT '数据范围',
  `is_sys` varchar(64) DEFAULT NULL COMMENT '是否系统数据',
  `useable` varchar(64) DEFAULT NULL COMMENT '是否可用',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_date` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Table structure for winteree_core_secret_key
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_secret_key`;
CREATE TABLE `winteree_core_secret_key` (
  `id` varchar(36) NOT NULL,
  `public_key` text,
  `private_key` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秘钥表';

-- ----------------------------
-- Table structure for winteree_core_setting
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_setting`;
CREATE TABLE `winteree_core_setting` (
  `id` varchar(36) NOT NULL,
  `keys` varchar(255) NOT NULL COMMENT 'key',
  `values` varchar(255) NOT NULL COMMENT '值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自定义设置';

-- ----------------------------
-- Table structure for winteree_core_verification_code
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_verification_code`;
CREATE TABLE `winteree_core_verification_code` (
  `id` varchar(36) NOT NULL,
  `account_id` varchar(36) NOT NULL COMMENT '账号ID',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `verification_code` varchar(255) NOT NULL COMMENT '验证码',
  `dead_date` datetime NOT NULL COMMENT '失效时间',
  `usable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `sended` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已发送',
  `content_text` varchar(255) NOT NULL COMMENT '发送内容',
  `validation_type` varchar(255) DEFAULT NULL COMMENT '验证类型，登陆？注册？',
  `creat_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码';

SET FOREIGN_KEY_CHECKS = 1;
