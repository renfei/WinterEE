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
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL COMMENT '租户ID',
  `name` varchar(255) NOT NULL COMMENT '租户名称',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `expiry_date` datetime NOT NULL COMMENT '到期时间',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='租户表';

-- ----------------------------
-- Records of winteree_core_ tenant
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_ tenant` VALUES (1, 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '系统租户', '2020-04-07 10:02:23', '2099-12-31 12:59:59', 1, NULL);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_account
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_account`;
CREATE TABLE `winteree_core_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `tenant_uuid` varchar(36) NOT NULL COMMENT '租户ID',
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
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH,
  UNIQUE KEY `uk_username` (`user_name`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='账户服务';

-- ----------------------------
-- Records of winteree_core_account
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_account` VALUES (1, '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '2020-03-31 13:10:11', 'admin', 'i@renfei.net', '13001000000', 'sha1:64000:18:5eIrOGu492SUG8VVd0qsRJv+eQgt1E1I:ePI6MBezyKgMUP9rzNCG4SP0', 1, NULL, 0, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_log
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_log`;
CREATE TABLE `winteree_core_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uuid` varchar(36) NOT NULL,
  `create_time` datetime NOT NULL COMMENT '时间',
  `log_type` varchar(255) NOT NULL COMMENT '日志类别',
  `log_sub_type` varchar(255) DEFAULT NULL COMMENT '日志二级类别',
  `logValue` longtext COMMENT '日志内容',
  `tenant_uuid` varchar(36) DEFAULT NULL COMMENT '租户ID',
  `account_uuid` varchar(36) DEFAULT NULL COMMENT '账户ID',
  `client_uuid` varchar(36) DEFAULT NULL COMMENT '客户端编号',
  `client_ip` varchar(255) DEFAULT NULL COMMENT '客户端IP',
  `request_url` text COMMENT '请求URL',
  `request_method` varchar(255) DEFAULT NULL COMMENT '请求方式',
  `request_head` text COMMENT '请求头信息',
  `request_body` longtext COMMENT '请求体',
  `status_code` varchar(255) DEFAULT NULL COMMENT '响应状态码',
  `response_head` text COMMENT '响应头信息',
  `response_body` longtext COMMENT '响应体',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='日志表';

-- ----------------------------
-- Table structure for winteree_core_menu
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_menu`;
CREATE TABLE `winteree_core_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `parent_uuid` varchar(36) NOT NULL COMMENT '父级编号',
  `uuid` varchar(36) NOT NULL COMMENT '编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `sort` decimal(10,0) NOT NULL COMMENT '排序',
  `href` varchar(2000) DEFAULT NULL COMMENT '链接',
  `target` varchar(20) DEFAULT NULL COMMENT '目标',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `is_show` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是否在菜单中显示',
  `is_menu` tinyint(1) unsigned NOT NULL DEFAULT '1' COMMENT '是菜单还是权限',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '删除标记',
  `weight` int(11) NOT NULL DEFAULT '0' COMMENT '菜单权重',
  `i18n` varchar(255) DEFAULT NULL COMMENT '国际化语言标注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH,
  KEY `idx_parent_id` (`parent_uuid`),
  KEY `idx_del_flag` (`is_delete`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of winteree_core_menu
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_menu` VALUES (1, 'root', '99406120-183c-4029-a992-1c707aba5ba2', '平台管理', 0, NULL, NULL, 'mdi-chevron-up', 1, 1, NULL, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-20 20:30:41', NULL, NULL, NULL, 0, 0, 'core.platformsettings');
INSERT INTO `winteree_core_menu` VALUES (2, '99406120-183c-4029-a992-1c707aba5ba2', 'bfd720bd-b923-483f-9a4d-a5c43c177bfd', '菜单管理', 2, NULL, NULL, 'mdi-clipboard-list', 1, 1, NULL, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-20 20:32:49', NULL, NULL, NULL, 0, 0, 'core.menusettings');
INSERT INTO `winteree_core_menu` VALUES (3, '99406120-183c-4029-a992-1c707aba5ba2', '31380c03-eee8-4e85-ad3e-a919d86b96aa', '租户管理', 1, NULL, NULL, 'mdi-account-group-outline', 1, 1, NULL, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-20 20:35:11', NULL, NULL, NULL, 0, 0, 'core.tenantsettings');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_office
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_office`;
CREATE TABLE `winteree_core_office` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `uuid` varchar(36) NOT NULL,
  `tenant_uuid` varchar(36) NOT NULL COMMENT '租户编号',
  `parent_uuid` varchar(36) NOT NULL COMMENT '父级编号',
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
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH,
  KEY `idx_parent_id` (`parent_uuid`),
  KEY `idex_del_flag` (`del_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- ----------------------------
-- Table structure for winteree_core_role
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_role`;
CREATE TABLE `winteree_core_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `uuid` varchar(36) NOT NULL,
  `office_uuid` varchar(36) DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enname` varchar(255) DEFAULT NULL COMMENT '英文名称',
  `role_type` varchar(255) DEFAULT NULL COMMENT '角色类型',
  `data_scope` int(11) DEFAULT '1' COMMENT '数据范围,1:全部组织2:本部门及以下3:仅限本部门',
  `useable` tinyint(1) DEFAULT '1' COMMENT '是否可用',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Table structure for winteree_core_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_role_menu`;
CREATE TABLE `winteree_core_role_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `role_uuid` varchar(36) NOT NULL COMMENT '角色UUID',
  `menu_uuid` varchar(36) NOT NULL COMMENT '菜单UUID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单关联表';

-- ----------------------------
-- Table structure for winteree_core_secret_key
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_secret_key`;
CREATE TABLE `winteree_core_secret_key` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `public_key` text COMMENT '公钥',
  `private_key` text COMMENT '私钥',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL,
  `uuid` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='秘钥表';

-- ----------------------------
-- Table structure for winteree_core_setting
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_setting`;
CREATE TABLE `winteree_core_setting` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `keys` varchar(255) NOT NULL COMMENT 'key',
  `values` varchar(255) NOT NULL COMMENT '值',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自定义设置';

-- ----------------------------
-- Table structure for winteree_core_user_role
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_user_role`;
CREATE TABLE `winteree_core_user_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `account_uuid` varchar(36) NOT NULL,
  `role_uuid` varchar(36) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与角色关联表';

-- ----------------------------
-- Table structure for winteree_core_verification_code
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_verification_code`;
CREATE TABLE `winteree_core_verification_code` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL COMMENT 'UUID',
  `account_uuid` varchar(36) NOT NULL COMMENT '账号ID',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `verification_code` varchar(255) NOT NULL COMMENT '验证码',
  `dead_date` datetime NOT NULL COMMENT '失效时间',
  `usable` tinyint(4) NOT NULL DEFAULT '1' COMMENT '是否有效',
  `sended` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否已发送',
  `content_text` varchar(255) NOT NULL COMMENT '发送内容',
  `validation_type` varchar(255) DEFAULT NULL COMMENT '验证类型，登陆？注册？',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='验证码';

SET FOREIGN_KEY_CHECKS = 1;
