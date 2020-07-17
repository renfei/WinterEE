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
INSERT INTO `oauth_client_details` VALUES ('Your-Module-Serve', 'WinterEE', 'sha1:64000:18:mZqWx+7xCXJ1aM2PqxO8H7yYutCJ1Y9o:6zyReU4QGCYx8DADaEjdkZtS', 'WinterEE-Core-Serve', 'client_credentials', 'http://localhost/callback', NULL, 7200, 86400, NULL, '2020-06-24 20:48:10', 0, 0, 'false');
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
-- Records of oauth_code
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_account
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_account`;
CREATE TABLE `winteree_core_account` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `uuid` varchar(36) NOT NULL COMMENT 'UUID',
  `tenant_uuid` varchar(36) NOT NULL COMMENT '租户ID',
  `office_uuid` varchar(36) DEFAULT NULL COMMENT '所属公司ID',
  `department_uuid` varchar(36) DEFAULT NULL COMMENT '所属部门ID',
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='账户服务';

-- ----------------------------
-- Records of winteree_core_account
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_account` VALUES (1, '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '2020-03-31 13:10:11', 'admin', 'i@renfei.net', '13001000000', 'sha1:64000:18:5eIrOGu492SUG8VVd0qsRJv+eQgt1E1I:ePI6MBezyKgMUP9rzNCG4SP0', 1, NULL, 3, NULL, NULL, '2020-06-23 15:13:53');
INSERT INTO `winteree_core_account` VALUES (2, '6853F941-EF46-4B0D-AC2C-5A6B8D5C9626', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'ed9ee135-029d-42d9-8a1c-b9a43a8c55ea', '1106dc4a-4a15-434e-b78d-282a3bcffc71', '2020-06-23 17:32:03', '用户001', 'seaf@fg', '+18395603410', NULL, 1, NULL, 0, NULL, NULL, '2020-06-23 17:33:13');
INSERT INTO `winteree_core_account` VALUES (3, 'BBB1DA44-658F-4F3F-B33E-24F14F6486B0', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'efa83b70-edbb-4f0b-8f21-9f37a3cc39f9', '', '2020-06-23 17:40:31', '用户002', 'tgh@dfhadfg', '412323123', 'sha1:64000:18:9BYaJlIvs33TlIQ1NXW2z02njk1w/61V:nkI57T/7D3Nxu+UQkxCdCKXB', 1, NULL, 0, NULL, NULL, '2020-06-23 17:41:03');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_cms_category
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_cms_category`;
CREATE TABLE `winteree_core_cms_category` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `site_uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属站点',
  `en_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '英文名',
  `zh_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '中文名',
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'UUID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE,
  UNIQUE KEY `uk_ename` (`en_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='CMS系统分类';

-- ----------------------------
-- Records of winteree_core_cms_category
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_cms_category` VALUES (10, '1BD49B64-3206-4E27-A4F5-93DD9CD28157', 'testcat', '测试分类', 'CC7F7D92-3816-4881-BA94-3D29A22C22C9');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_cms_comments
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_cms_comments`;
CREATE TABLE `winteree_core_cms_comments` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `post_uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章ID',
  `account_uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '登陆账户ID',
  `author` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '作者名称',
  `author_email` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '作者邮箱',
  `author_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '作者链接',
  `author_IP` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '作者IP',
  `author_address` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '作者物理地址',
  `addtime` datetime NOT NULL COMMENT '评论时间',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '评论内容',
  `is_delete` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否删除',
  `parent_id` bigint(20) unsigned DEFAULT NULL COMMENT '父级评论ID',
  `is_owner` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否是官方回复',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CMS系统评论表';

-- ----------------------------
-- Records of winteree_core_cms_comments
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_cms_menu
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_cms_menu`;
CREATE TABLE `winteree_core_cms_menu` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL COMMENT 'UUID',
  `puuid` varchar(36) NOT NULL COMMENT '父级ID，顶级是站点ID',
  `site_uuid` varchar(36) NOT NULL COMMENT '站点ID',
  `menu_text` varchar(255) NOT NULL COMMENT '菜单名称',
  `menu_link` varchar(255) NOT NULL COMMENT '菜单链接',
  `menu_icon` varchar(255) DEFAULT NULL COMMENT '图标',
  `is_new_win` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否新窗口打开',
  `menu_type` int(11) NOT NULL COMMENT '菜单类型',
  `order_number` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(36) NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_by` varchar(36) NOT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='CMS系统菜单表';

-- ----------------------------
-- Records of winteree_core_cms_menu
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_cms_posts
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_cms_posts`;
CREATE TABLE `winteree_core_cms_posts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'UUID',
  `site_uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属站点',
  `category_uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章分类',
  `featured_image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '特色图像',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章标题',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文章内容',
  `is_original` tinyint(1) unsigned NOT NULL COMMENT '是否原创文章',
  `source_url` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '原文链接',
  `source_name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '文章来源名称',
  `views` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '浏览量',
  `thumbs_up` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '点赞',
  `thumbs_down` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '点踩',
  `release_time` datetime NOT NULL COMMENT '发布时间',
  `create_time` datetime NOT NULL COMMENT '添加时间',
  `create_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_by` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '更新人',
  `describes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '文章简介用于SEO',
  `keyword` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '关键字用于SEO',
  `is_delete` tinyint(1) unsigned NOT NULL DEFAULT '0' COMMENT '软删除',
  `is_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否允许评论',
  `avg_views` double NOT NULL DEFAULT '0' COMMENT '日均浏览量',
  `avg_comment` double NOT NULL DEFAULT '0' COMMENT '日均评论量',
  `page_rank` double NOT NULL DEFAULT '1000' COMMENT '权重排序',
  `page_rank_update_time` datetime DEFAULT NULL COMMENT '权重更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1003290 DEFAULT CHARSET=utf8mb4 COMMENT='CMS系统文章表';

-- ----------------------------
-- Records of winteree_core_cms_posts
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_cms_posts` VALUES (1003289, '54003D18-D84B-4F4E-B311-C5DEEBB89C76', '1BD49B64-3206-4E27-A4F5-93DD9CD28157', 'CC7F7D92-3816-4881-BA94-3D29A22C22C9', 'https://cdn.renfei.net/opt/upload/a27e8bfce3c74849abaff0d1eace3beb.png', '东方红', '<p>申达股份</p><p>阿斯蒂芬</p><p>发是</p>', 1, '分', '瑞王坟', 0, 0, 0, '2020-07-01 14:52:53', '2020-07-01 14:52:53', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-01 15:14:16', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '爱疯电视柜', '阿萨德噶', 0, 1, 0, 0, 0, NULL);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_cms_site
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_cms_site`;
CREATE TABLE `winteree_core_cms_site` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `tenant_uuid` varchar(36) NOT NULL COMMENT '所属租户',
  `office_uuid` varchar(36) DEFAULT NULL COMMENT '所属公司',
  `department_uuid` varchar(36) DEFAULT NULL COMMENT '所属部门',
  `site_name` varchar(255) NOT NULL COMMENT '站点名称',
  `site_domain` varchar(255) NOT NULL COMMENT '站点域名',
  `site_keyword` varchar(255) DEFAULT NULL COMMENT '站点关键字',
  `site_description` varchar(255) DEFAULT NULL COMMENT '站点描述',
  `icp_no` varchar(255) DEFAULT NULL COMMENT 'ICP备案号',
  `is_comment` tinyint(1) NOT NULL DEFAULT '1' COMMENT '全局评论开关',
  `gongan_no` varchar(255) DEFAULT NULL COMMENT '公安备案号',
  `analysis_code` varchar(255) DEFAULT NULL COMMENT '统计代码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(36) NOT NULL COMMENT '创建人',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `update_by` varchar(36) NOT NULL COMMENT '更新人',
  `site_enable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='CMS系统站点表';

-- ----------------------------
-- Records of winteree_core_cms_site
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_cms_site` VALUES (1, '1BD49B64-3206-4E27-A4F5-93DD9CD28157', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '', '', '测试站点', 'demo.com', '测试,演示,test,demo', '这是一个测试站点', '', 1, '', '', '2020-06-28 17:13:03', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 17:46:43', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 1);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_cms_tag
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_cms_tag`;
CREATE TABLE `winteree_core_cms_tag` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `site_uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属站点',
  `en_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '英文名',
  `zh_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '中文名',
  `describe` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '描述',
  `uuid` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'UUID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE,
  UNIQUE KEY `uk_ename` (`en_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COMMENT='CMS系统标签分类';

-- ----------------------------
-- Records of winteree_core_cms_tag
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_cms_tag` VALUES (36, '1BD49B64-3206-4E27-A4F5-93DD9CD28157', 'testtag', '测试标签', NULL, '17CE6CBF-174B-4152-9949-F415CDFB33B7');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_cms_tag_posts
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_cms_tag_posts`;
CREATE TABLE `winteree_core_cms_tag_posts` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `tag_uuid` varchar(36) NOT NULL COMMENT '标签ID',
  `post_uuid` varchar(36) NOT NULL COMMENT '文章ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='CMS系统文章与标签关系表';

-- ----------------------------
-- Records of winteree_core_cms_tag_posts
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_cms_tag_posts` VALUES (3, '1BF5DD79-F1EA-4397-A671-89A41CE4CF40', '17CE6CBF-174B-4152-9949-F415CDFB33B7', '54003D18-D84B-4F4E-B311-C5DEEBB89C76');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_files
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_files`;
CREATE TABLE `winteree_core_files` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL,
  `tenant_uuid` varchar(36) NOT NULL COMMENT '租户ID',
  `office_uuid` varchar(36) DEFAULT NULL COMMENT '所属公司',
  `department_uuid` varchar(36) DEFAULT NULL COMMENT '所属部门',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `original_file_name` varchar(255) NOT NULL COMMENT '原始文件名',
  `storage_type` varchar(255) NOT NULL COMMENT '存储类型',
  `storage_path` varchar(255) NOT NULL COMMENT '存储路径',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `create_by` varchar(36) NOT NULL COMMENT '创建人',
  `bucke_name` varchar(255) DEFAULT NULL COMMENT '储存桶',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_files
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_files` VALUES (1, 'CD7481E2-CDB8-4B9B-A4E2-6C98ABAA4F0A', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '5cdd05c66fd3487593095da876a34f04.png', 'WX20200630-122626.png', 'oss', 'opt/upload', '2020-07-01 10:40:25', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (2, 'E8D5512D-E191-46D7-8A69-1E728C8908C1', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '85289c1e6b684b77bb00f0d47714d10c.png', 'WX20200630-122626.png', 'oss', 'opt/upload', '2020-07-01 10:44:13', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (3, '1EC890EB-9742-433B-83A6-3BF00E367A0F', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '089d9f24fe6149b8be2ad127c5820d06.png', 'WX20200630-122626.png', 'oss', 'opt/upload/', '2020-07-01 10:47:08', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (4, 'E0A905D9-D1B6-4EB6-9C6E-526F5C16144F', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '17fa371f31154443bd3032b9949cf4d7.png', 'WX20200630-122626.png', 'oss', 'opt/upload/', '2020-07-01 10:48:12', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (5, 'B6C22447-3696-4292-9A00-634623992CEC', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '19292c2510534e49981e596dad285ed5.png', 'WX20200628-173751.png', 'oss', 'opt/upload/', '2020-07-01 10:50:56', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (6, '90A894CB-DC17-46E7-B54B-39D9D07A514A', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, 'f901da0f3b694ddc9ed12c9a0f0a2af2.png', 'WX20200630-122626.png', 'oss', 'opt/upload/', '2020-07-01 10:50:56', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (7, '9087E0EF-D68A-4ACF-A84C-69DCA10CD3A6', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '1569f8bdc8f94503900ed85f96430911.png', 'WX20200630-122626.png', 'oss', 'opt/upload/', '2020-07-01 10:57:12', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (8, '67BEB9A5-0F5D-494C-B870-CA8808E8A877', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, 'e3d21c06f4034a898598ceec581dc949.png', 'WX20200630-122626.png', 'oss', 'opt/upload/', '2020-07-01 12:01:39', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (9, '384A3522-8D1A-4A91-B45F-5573F562EFF0', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, '71cfd28ae596463a8d0d58ee0b2ecd8b.png', 'WX20200630-122626.png', 'oss', 'opt/upload/', '2020-07-01 12:07:35', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
INSERT INTO `winteree_core_files` VALUES (10, 'FDD05BFB-0756-4A38-8AD9-B46A5D7EA63A', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, NULL, 'a27e8bfce3c74849abaff0d1eace3beb.png', 'WX20200630-122626.png', 'oss', 'opt/upload/', '2020-07-01 14:52:53', '9369919A-F95E-44CF-AB0A-6BCD1D933403', 'renfei');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_geospatial
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_geospatial`;
CREATE TABLE `winteree_core_geospatial` (
  `id` varchar(36) NOT NULL,
  `fk_id` varchar(36) NOT NULL COMMENT '外键ID',
  `fk_type` int(11) NOT NULL COMMENT '外键类型：1租户，2公司',
  `longitude` decimal(14,10) NOT NULL COMMENT '经度',
  `latitude` decimal(14,10) NOT NULL COMMENT '纬度',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='地理空间';

-- ----------------------------
-- Records of winteree_core_geospatial
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_geospatial` VALUES ('1440317b-bf08-47b8-be11-2f6e0bfdaa56', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 1, 116.2088790000, 39.9291030000);
INSERT INTO `winteree_core_geospatial` VALUES ('1e0ef2aa-cdc1-4483-bf70-f18a58f4f130', 'efa83b70-edbb-4f0b-8f21-9f37a3cc39f9', 2, 116.4125450000, 39.9100670000);
INSERT INTO `winteree_core_geospatial` VALUES ('39d3e70e-fd7b-475e-ac7c-f284fe8d2b08', 'ed9ee135-029d-42d9-8a1c-b9a43a8c55ea', 2, 116.4039840000, 39.9137600000);
INSERT INTO `winteree_core_geospatial` VALUES ('5aeaf9dd-ee9d-4f1b-9acf-c6d4a3c1ba02', '911939fa-11b3-446a-9afd-e0620f2e4065', 2, 116.3793530000, 39.9128620000);
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
) ENGINE=InnoDB AUTO_INCREMENT=5626 DEFAULT CHARSET=utf8mb4 COMMENT='日志表';

-- ----------------------------
-- Records of winteree_core_log
-- ----------------------------
BEGIN;
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8mb4 COMMENT='菜单表';

-- ----------------------------
-- Records of winteree_core_menu
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_menu` VALUES (1, 'root', '99406120-183c-4029-a992-1c707aba5ba2', '平台管理', 0, NULL, NULL, 'mdi-chevron-up', 1, 1, NULL, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-20 20:30:41', NULL, NULL, NULL, 0, 0, 'core.platformsettings');
INSERT INTO `winteree_core_menu` VALUES (2, '99406120-183c-4029-a992-1c707aba5ba2', 'bfd720bd-b923-483f-9a4d-a5c43c177bfd', '菜单管理', 2, '/setting/menu', NULL, 'mdi-clipboard-list', 1, 1, NULL, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-20 20:32:49', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 21:50:31', NULL, 0, 0, 'core.menusettings');
INSERT INTO `winteree_core_menu` VALUES (3, '99406120-183c-4029-a992-1c707aba5ba2', '31380c03-eee8-4e85-ad3e-a919d86b96aa', '租户管理', 1, '/setting/tenant', NULL, 'mdi-account-group-outline', 1, 1, NULL, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-20 20:35:11', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 22:28:31', NULL, 0, 0, 'core.tenantsettings');
INSERT INTO `winteree_core_menu` VALUES (4, 'bfd720bd-b923-483f-9a4d-a5c43c177bfd', 'eff2d51d-107a-4085-9925-ade657a33880', '菜单浏览权限', 0, NULL, NULL, NULL, 1, 0, 'platf:menu:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-21 20:55:57', NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `winteree_core_menu` VALUES (5, 'bfd720bd-b923-483f-9a4d-a5c43c177bfd', '807f746f-b7b0-4aff-9674-f71e39a5c92f', '菜单新增权限', 1, NULL, NULL, NULL, 1, 0, 'platf:menu:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-22 20:11:42', NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `winteree_core_menu` VALUES (6, 'bfd720bd-b923-483f-9a4d-a5c43c177bfd', '3f06a551-7039-4dcb-b74e-fcd7bef6a129', '菜单删除权限', 2, NULL, NULL, NULL, 1, 0, 'platf:menu:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-22 20:12:53', NULL, NULL, NULL, 0, 0, NULL);
INSERT INTO `winteree_core_menu` VALUES (7, 'bfd720bd-b923-483f-9a4d-a5c43c177bfd', '8d3704bc-6ee9-4186-9a51-6f4578241d24', '菜单修改权限', 3, NULL, NULL, NULL, 1, 0, 'platf:menu:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-22 20:13:39', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 14:18:51', NULL, 0, 0, NULL);
INSERT INTO `winteree_core_menu` VALUES (8, '31380c03-eee8-4e85-ad3e-a919d86b96aa', '261c6f77-b4ac-4f20-95d0-9079dd0c82b6', '租户浏览权限', 0, '', '', '', 1, 0, 'platf:tenant:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 15:54:55', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 15:55:17', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (9, '31380c03-eee8-4e85-ad3e-a919d86b96aa', 'ed2c9c1a-ffce-4547-981a-b9e1bd86e8fb', '租户修改权限', 0, '', '', '', 1, 0, 'platf:tenant:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 15:55:40', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 15:56:09', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (10, '31380c03-eee8-4e85-ad3e-a919d86b96aa', '0d58cfe9-8335-4846-8738-55e73cd8f8c9', '租户添加权限', 0, '', '', '', 1, 0, 'platf:tenant:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 15:57:29', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 15:57:29', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (12, 'root', 'c26dd43a-6da7-4dd6-989e-ba9783f99679', '首页', 99999, '/home', '', 'mdi-home', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 22:00:13', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-23 22:00:13', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (13, 'root', '36103ee9-6f0d-438b-9c5a-667bcc8e7a89', '系统设置', 1, '', '', '', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-24 14:17:12', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-24 14:22:41', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (14, '36103ee9-6f0d-438b-9c5a-667bcc8e7a89', '88f5a3d0-dadd-43e9-9b09-38645cd582ee', '日志查看', 99999, '/setting/log', '', 'mdi-text-box-search', 1, 1, 'platf:log:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-24 14:21:39', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-24 14:22:29', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (15, '99406120-183c-4029-a992-1c707aba5ba2', '654836a1-b663-4c59-bea8-c592b24eb168', 'OAuth客户端管理', 0, '/setting/oauthclient', '', 'mdi-android', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:05:47', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:05:47', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (16, '654836a1-b663-4c59-bea8-c592b24eb168', '0f9e53ea-6e96-443a-a5eb-38ce310eac38', '客户端浏览权限', 0, '', '', '', 1, 0, 'platf:oauth:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:07:02', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:07:02', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (17, '654836a1-b663-4c59-bea8-c592b24eb168', '536d8e67-1848-4411-a7d9-be232b6e4282', '客户端修改权限', 1, '', '', '', 1, 0, 'platf:oauth:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:07:29', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:07:29', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (18, '654836a1-b663-4c59-bea8-c592b24eb168', 'e872863a-50b5-467f-a4e0-25307588c2d8', '客户端新增权限', 2, '', '', '', 1, 0, 'platf:oauth:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:07:49', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:07:49', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (19, '654836a1-b663-4c59-bea8-c592b24eb168', '40bd5091-e09d-4c22-960b-38373f6dbb42', '客户端删除权限', 3, '', '', '', 1, 0, 'platf:oauth:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:08:08', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-04-27 17:29:56', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (20, '31380c03-eee8-4e85-ad3e-a919d86b96aa', '8767a8e2-4e15-4121-b74b-e26be5c60131', '租户管理权限', 9999, '', '', '', 1, 0, 'platf:tenant:manage', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-05-06 22:28:47', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-05-06 22:28:47', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (21, '36103ee9-6f0d-438b-9c5a-667bcc8e7a89', '3958c51e-e2db-4778-a0ed-86c30c88a759', '基础资料', 0, '/setting/tenantinfo', '', 'mdi-file-document-edit-outline', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-10 16:15:26', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-10 17:16:51', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (22, '3958c51e-e2db-4778-a0ed-86c30c88a759', 'a6c0a304-e55d-4782-97c4-77e2bd762f37', '基础资料修改权限', 0, '', '', '', 1, 0, 'platf:tenantinfo:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-11 21:36:57', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-11 21:36:57', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (23, 'root', '8dfce183-71ea-4011-89ac-b6b0739f5e01', '组织管理', 3, '', '', '', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:25:22', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:45:16', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (24, '8dfce183-71ea-4011-89ac-b6b0739f5e01', '89e8e17b-446b-4f5d-9270-1b8b36bf787d', '公司管理', 2, '/organization/company', '', 'mdi-account-supervisor-circle', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:29:45', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 13:49:05', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (25, '89e8e17b-446b-4f5d-9270-1b8b36bf787d', '3fcd9235-13fc-4868-bf7b-b392f069b626', '获取公司信息权限', 0, '', '', '', 1, 0, 'platf:company:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:30:46', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:30:46', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (26, '89e8e17b-446b-4f5d-9270-1b8b36bf787d', '359c1552-8458-4a18-965a-e4250352745a', '新增公司权限', 0, '', '', '', 1, 0, 'platf:company:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:31:07', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:31:07', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (27, '89e8e17b-446b-4f5d-9270-1b8b36bf787d', 'ebc59fe5-6783-45c7-8072-2aa4e5037b48', '修改公司信息权限', 0, '', '', '', 1, 0, 'platf:company:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:31:31', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-12 21:31:31', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (28, 'root', 'a4c13987-75de-4218-abae-2a7a75bcc05a', '权限管理', 2, '', '', '', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:44:59', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:53:07', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (29, 'a4c13987-75de-4218-abae-2a7a75bcc05a', '96094ee8-5242-4332-9c0a-0d50bf47789b', '角色管理', 0, '/rights/role', '', 'mdi-account-convert-outline', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:50:25', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:54:30', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (30, '96094ee8-5242-4332-9c0a-0d50bf47789b', 'fd5037f3-a1e1-4735-b7da-68707fbc10c4', '角色查看权限', 0, '', '', '', 1, 0, 'platf:role:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:50:52', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:50:52', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (31, '96094ee8-5242-4332-9c0a-0d50bf47789b', 'ec3ad7f8-376a-4c98-a899-ee9343dc19e8', '角色添加权限', 0, '', '', '', 1, 0, 'platf:role:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:51:21', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:51:21', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (32, '96094ee8-5242-4332-9c0a-0d50bf47789b', '43e6fea3-5091-4aec-8696-6f33f239e058', '角色编辑权限', 0, '', '', '', 1, 0, 'platf:role:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:51:41', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:51:41', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (33, '96094ee8-5242-4332-9c0a-0d50bf47789b', '3706dcbb-541c-4c94-b0f9-3f8bf4792d8a', '角色删除权限', 0, '', '', '', 1, 0, 'platf:role:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:51:58', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 16:51:58', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (34, '8dfce183-71ea-4011-89ac-b6b0739f5e01', 'e77afe54-0024-4149-9c69-5f36379ea0fa', '部门管理', 1, '/organization/department', '', 'mdi-account-supervisor', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 14:10:55', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 13:49:08', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (35, 'e77afe54-0024-4149-9c69-5f36379ea0fa', '74b3624e-7594-4072-87b9-53b438880af1', '部门查看权限', 0, '', '', '', 1, 0, 'platf:department:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:23:24', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:23:24', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (36, 'e77afe54-0024-4149-9c69-5f36379ea0fa', 'af060596-af30-4626-b23d-59649800a2b8', '部门修改权限', 0, '', '', '', 1, 0, 'platf:department:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:23:45', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:23:45', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (37, 'e77afe54-0024-4149-9c69-5f36379ea0fa', 'ef119372-38f3-4185-8e3d-5b19d45540b8', '部门添加权限', 0, '', '', '', 1, 0, 'platf:department:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:24:01', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:24:01', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (38, 'e77afe54-0024-4149-9c69-5f36379ea0fa', '46566130-2c81-44bb-b4eb-609c528e23b5', '部门删除权限', 0, '', '', '', 1, 0, 'platf:department:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:24:16', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 16:24:16', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (39, '8dfce183-71ea-4011-89ac-b6b0739f5e01', '4bb5648d-46aa-42ff-b74b-2be6ea8cafcc', '用户管理', 3, '/account', '', 'mdi-account', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 13:47:36', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 13:48:57', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (40, '4bb5648d-46aa-42ff-b74b-2be6ea8cafcc', 'a3b7f80f-4e9d-4c1b-96c0-6197e756c095', '获取组织架构树权限', 0, '', '', '', 1, 0, 'platf:AllOrganization:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 13:48:12', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 13:48:12', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (41, '4bb5648d-46aa-42ff-b74b-2be6ea8cafcc', 'd281763b-5a9c-4b24-95f0-0b3114902c39', '账户查询权限', 0, '', '', '', 1, 0, 'platf:account:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 17:12:50', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 17:12:50', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (42, 'root', 'dcd8939b-7bb3-4801-80f3-d4ad1d9d63af', '场地管理', 3, '', '', '', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 18:18:25', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 18:21:32', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (43, 'dcd8939b-7bb3-4801-80f3-d4ad1d9d63af', 'b7724904-f10b-45c4-9980-66b5df1c4527', '会议室预约', 0, '', '', 'mdi-clock-outline', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 18:21:14', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 18:21:14', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (44, 'dcd8939b-7bb3-4801-80f3-d4ad1d9d63af', '9aa3112f-b4a3-47f4-9a6c-8b49cd0b05a1', '房间管理', 0, '', '', 'mdi-office-building', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 18:22:05', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 18:22:05', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (45, '4bb5648d-46aa-42ff-b74b-2be6ea8cafcc', 'db77661d-201f-4308-ba9c-f511ff9564f9', '重置账户密码权限', 0, '', '', '', 1, 0, 'platf:account:resetpasseord', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 15:54:27', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 15:54:27', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (46, '4bb5648d-46aa-42ff-b74b-2be6ea8cafcc', '8bdc28e1-975c-487b-871d-da2b8fa66718', '账户添加权限', 0, '', '', '', 1, 0, 'platf:account:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 15:54:58', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 15:54:58', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (47, '4bb5648d-46aa-42ff-b74b-2be6ea8cafcc', '826b6a89-54dc-4195-8db0-308ad612a2aa', '账户编辑权限', 0, '', '', '', 1, 0, 'platf:account:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 15:55:14', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 15:55:14', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (48, '4bb5648d-46aa-42ff-b74b-2be6ea8cafcc', '473970da-92b1-4907-a359-4ad2bc5d6816', '获取公司列表', 0, '', '', '', 1, 0, 'platf:company:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 16:11:27', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-23 16:11:27', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (49, 'root', '01f46395-ebfa-4d9b-8620-6d7f466ed035', 'CMS管理', 2, '', '', '', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:14:17', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:14:34', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (50, '01f46395-ebfa-4d9b-8620-6d7f466ed035', '77a802e0-fea1-43a6-9cf0-c3e8f2a1c8fa', '站点管理', 0, '/cms/site', '', 'mdi-web', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:21:22', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:21:22', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (51, '77a802e0-fea1-43a6-9cf0-c3e8f2a1c8fa', 'a720310f-2192-4052-9b62-b7b8a5439514', '站点信息查询权限', 0, '', '', '', 1, 0, 'platf:cmssite:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:21:53', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:21:53', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (52, '77a802e0-fea1-43a6-9cf0-c3e8f2a1c8fa', '553ed243-ec9a-438a-a06b-915ea2e7215b', '站点添加权限', 0, '', '', '', 1, 0, 'platf:cmssite:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:22:09', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:22:09', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (53, '77a802e0-fea1-43a6-9cf0-c3e8f2a1c8fa', '726faeea-b356-480a-bb6b-3f0c2223943a', '站点修改权限', 0, '', '', '', 1, 0, 'platf:cmssite:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:22:30', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:22:30', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (54, '77a802e0-fea1-43a6-9cf0-c3e8f2a1c8fa', 'a26b8dfe-bd1a-4f17-a616-7b7f084640f6', '站点删除权限', 0, '', '', '', 1, 0, 'platf:cmssite:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:22:45', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:22:45', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (55, '01f46395-ebfa-4d9b-8620-6d7f466ed035', '3e31f21c-23c6-4486-8b55-2a49dc110ad1', '文章分类管理', 0, '/cms/category', '', 'mdi-shape', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:24:09', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:24:09', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (56, '3e31f21c-23c6-4486-8b55-2a49dc110ad1', '92868f84-4a67-44f7-b161-a052cb337f3a', '文章分类查询权限', 0, '', '', '', 1, 0, 'platf:cmscategory:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:24:37', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:24:37', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (57, '3e31f21c-23c6-4486-8b55-2a49dc110ad1', 'a6e02f3d-85a7-488a-ab7f-0f79a599c817', '文章分类添加权限', 0, '', '', '', 1, 0, 'platf:cmscategory:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:25:09', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:25:09', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (58, '3e31f21c-23c6-4486-8b55-2a49dc110ad1', 'f4780138-78f2-4039-9cae-876e71d52acf', '文章分类修改权限', 0, '', '', '', 1, 0, 'platf:cmscategory:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:25:26', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:25:26', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (59, '3e31f21c-23c6-4486-8b55-2a49dc110ad1', 'cbba044d-69e4-4d89-8588-937d6d2b9440', '文章分类删除权限', 0, '', '', '', 1, 0, 'platf:cmscategory:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:25:42', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:25:42', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (60, '01f46395-ebfa-4d9b-8620-6d7f466ed035', 'af99b7f8-7ed5-4299-bbb0-efcd6de6b6d7', '文章管理', 0, '/cms/posts', '', 'mdi-text-box-multiple', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:26:59', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:26:59', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (61, 'af99b7f8-7ed5-4299-bbb0-efcd6de6b6d7', '00100b1a-c902-4345-a6e7-b46f4185c189', '文章查询权限', 0, '', '', '', 1, 0, 'platf:cmsposts:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:29:26', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:29:26', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (62, 'af99b7f8-7ed5-4299-bbb0-efcd6de6b6d7', '3825e1eb-da47-4f24-815b-41ad1ceb9de1', '文章添加权限', 0, '', '', '', 1, 0, 'platf:cmsposts:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:32:35', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:32:35', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (63, 'af99b7f8-7ed5-4299-bbb0-efcd6de6b6d7', '451d9eca-16b8-4324-a8e7-38232c9e23f5', '文章修改权限', 0, '', '', '', 1, 0, 'platf:cmsposts:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:32:51', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:32:51', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (64, 'af99b7f8-7ed5-4299-bbb0-efcd6de6b6d7', '1a615743-4f85-4fb0-8498-40d476be728f', '文章删除权限', 0, '', '', '', 1, 0, 'platf:cmsposts:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:33:08', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:33:08', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (65, '01f46395-ebfa-4d9b-8620-6d7f466ed035', '36b6a872-e505-4964-b041-f382ef654d87', '文章标签管理', 0, '/cms/tag', '', 'mdi-tag', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:00', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:00', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (66, '36b6a872-e505-4964-b041-f382ef654d87', '38f6d6e7-5ed5-40c8-ae4e-969858f2940c', '文章标签查询权限', 0, '', '', '', 1, 0, 'platf:cmstag:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:18', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:18', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (67, '36b6a872-e505-4964-b041-f382ef654d87', '082550b5-2a0b-4035-8fe8-49f5bf64e4a9', '文章标签添加权限', 0, '', '', '', 1, 0, 'platf:cmstag:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:39', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:39', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (68, '36b6a872-e505-4964-b041-f382ef654d87', 'e04bdf6f-4523-4f25-ad9f-546e992b3125', '文章标签修改权限', 0, '', '', '', 1, 0, 'platf:cmstag:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:54', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:34:54', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (69, '36b6a872-e505-4964-b041-f382ef654d87', '7d996a93-dc8e-456b-bf49-5ca447a83eb7', '文章标签删除权限', 0, '', '', '', 1, 0, 'platf:cmstag:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:35:16', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-28 15:35:16', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (70, '36103ee9-6f0d-438b-9c5a-667bcc8e7a89', '61bd3d32-04ab-40ee-95b3-5fc3834f51a9', '公开文件上传权限', 0, '', '', '', 1, 0, 'platf:publicfile:upload', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-01 10:34:08', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-01 10:34:08', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (71, '01f46395-ebfa-4d9b-8620-6d7f466ed035', '22b3c7d1-9240-4674-ad7c-5e94e2c95a22', '站点菜单管理', 0, '', '', 'mdi-microsoft-xbox-controller-menu', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:58:45', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:58:45', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (72, '22b3c7d1-9240-4674-ad7c-5e94e2c95a22', '84d41972-ff03-4761-9e0f-39b12e23dcb0', '添加菜单（CMS系统）', 0, '', '', '', 1, 0, 'platf:cmsmenu:add', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:59:06', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:59:06', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (73, '22b3c7d1-9240-4674-ad7c-5e94e2c95a22', 'cb43411b-dcf2-46d3-adb9-1d44deaaaeff', '修改菜单（CMS系统）', 0, '', '', '', 1, 0, 'platf:cmsmenu:update', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:59:24', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:59:24', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (74, '22b3c7d1-9240-4674-ad7c-5e94e2c95a22', 'f1d1725f-d170-44cd-a43a-b95b44854b2d', '删除菜单（CMS系统）', 0, '', '', '', 1, 0, 'platf:cmsmenu:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:59:40', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 16:59:40', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (75, '99406120-183c-4029-a992-1c707aba5ba2', 'f5c113e0-73b5-4524-87c0-8cc15c31f219', '定时任务管理', 0, '', '', 'mdi-timer', 1, 1, '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:02:28', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:02:28', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (76, 'f5c113e0-73b5-4524-87c0-8cc15c31f219', '38de382e-4b8d-4286-9b52-0dc5c162a1e6', '获取定时任务列表', 0, '', '', '', 1, 0, 'platf:task:view', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:03:01', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:03:01', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (77, 'f5c113e0-73b5-4524-87c0-8cc15c31f219', '4eacc35d-e2dd-463e-bf98-56301205d7d3', '保存/修改定时任务', 0, '', '', '', 1, 0, 'platf:task:save', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:03:25', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:03:25', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (78, 'f5c113e0-73b5-4524-87c0-8cc15c31f219', '5071378b-96f1-43ad-9447-f5bf3d335175', '手动触发定时任务', 0, '', '', '', 1, 0, 'platf:task:exec', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:03:48', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:03:48', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (79, 'f5c113e0-73b5-4524-87c0-8cc15c31f219', 'ce05d3d1-fa05-4905-b64c-fef5822e28e6', '暂停定时任务', 0, '', '', '', 1, 0, 'platf:task:pause', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:04:06', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:04:06', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (80, 'f5c113e0-73b5-4524-87c0-8cc15c31f219', '29e984b0-0b1d-4f87-accf-c2e011396746', '恢复定时任务', 0, '', '', '', 1, 0, 'platf:task:resume', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:04:22', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:04:22', '', 0, 0, '');
INSERT INTO `winteree_core_menu` VALUES (81, 'f5c113e0-73b5-4524-87c0-8cc15c31f219', '3272df13-ba90-4bc1-a07e-2ecaf74de05a', '删除定时任务', 0, '', '', '', 1, 0, 'platf:task:delete', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:04:39', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-07-17 17:04:39', '', 0, 0, '');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_organization
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_organization`;
CREATE TABLE `winteree_core_organization` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `uuid` varchar(36) NOT NULL,
  `tenant_uuid` varchar(36) NOT NULL COMMENT '租户编号',
  `parent_uuid` varchar(36) NOT NULL COMMENT '父级编号',
  `org_type` int(11) NOT NULL DEFAULT '1' COMMENT '机构类型：1公司，2部门',
  `name` varchar(100) NOT NULL COMMENT '名称',
  `address` varchar(255) DEFAULT NULL COMMENT '联系地址',
  `zip_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `master` varchar(100) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(200) DEFAULT NULL COMMENT '电话',
  `fax` varchar(200) DEFAULT NULL COMMENT '传真',
  `email` varchar(200) DEFAULT NULL COMMENT '邮箱',
  `primary_person` varchar(64) DEFAULT NULL COMMENT '主负责人',
  `deputy_person` varchar(64) DEFAULT NULL COMMENT '副负责人',
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
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='机构表';

-- ----------------------------
-- Records of winteree_core_organization
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_organization` VALUES (1, 'ed9ee135-029d-42d9-8a1c-b9a43a8c55ea', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 1, '演示公司A', '北京市东城区西长安街', '100000', '任霏', '13001000000', '1324', 'i@renfei.net', '任霏', '张小二', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 15:59:22', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 15:59:22', '', '0');
INSERT INTO `winteree_core_organization` VALUES (2, 'efa83b70-edbb-4f0b-8f21-9f37a3cc39f9', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 1, '演示公司B', '北京市东城区正义路7号', '', '', '', '', '', '', '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 16:02:28', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 17:11:09', '', '0');
INSERT INTO `winteree_core_organization` VALUES (3, '0087b758-a13a-41b3-910f-cf606dcb907e', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'ed9ee135-029d-42d9-8a1c-b9a43a8c55ea', 1, '演示公司A的子公司', '', '', '', '', '', '', '', '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 16:21:44', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 16:21:44', '', '0');
INSERT INTO `winteree_core_organization` VALUES (4, '911939fa-11b3-446a-9afd-e0620f2e4065', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 1, '演示系统C', '北京市西城区复兴门内大街40号', '', '', '', '', '', '', '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 16:34:19', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-14 16:47:04', '', '0');
INSERT INTO `winteree_core_organization` VALUES (5, '1106dc4a-4a15-434e-b78d-282a3bcffc71', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'ed9ee135-029d-42d9-8a1c-b9a43a8c55ea', 2, '演示公司A的部门', '大师傅', '1001', '任霏', '13001000000', '123', 'i@renfei.net', '任霏', '任霏', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:20:31', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:20:31', '', '0');
INSERT INTO `winteree_core_organization` VALUES (6, 'f33269f4-88ba-4cd0-8379-e5f7de67e6f7', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'ed9ee135-029d-42d9-8a1c-b9a43a8c55ea', 2, '演示公司A的部门2', '大师傅', '1001', '任霏', '13001000000', '123', 'i@renfei.net', '任霏', '任霏', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:29:54', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:32:42', '', '0');
INSERT INTO `winteree_core_organization` VALUES (7, 'a450e2d5-bdd8-4be9-8908-3b87d1301f68', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '0087b758-a13a-41b3-910f-cf606dcb907e', 2, '演示部门3', '', '', '任霏', '', '', '', '', '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:36:22', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:36:22', '', '0');
INSERT INTO `winteree_core_organization` VALUES (8, 'e7426d64-28bd-432b-b027-33b05af95b75', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '1106dc4a-4a15-434e-b78d-282a3bcffc71', 2, '子级部门22', '', '', '任霏', '', '', '', '', '', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:36:58', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-22 11:39:59', '', '0');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_blob_triggers
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_blob_triggers`;
CREATE TABLE `winteree_core_qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `winteree_core_qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `winteree_core_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_blob_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_calendars
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_calendars`;
CREATE TABLE `winteree_core_qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(190) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_calendars
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_cron_triggers
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_cron_triggers`;
CREATE TABLE `winteree_core_qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `winteree_core_qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `winteree_core_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_cron_triggers
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_qrtz_cron_triggers` VALUES ('WinterEEScheduler', 'triggerUpdatePostPageRankJob', 'CMS', '0 0 3 * * ?', 'Asia/Shanghai');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_fired_triggers
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_fired_triggers`;
CREATE TABLE `winteree_core_qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `INSTANCE_NAME` varchar(190) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(190) DEFAULT NULL,
  `JOB_GROUP` varchar(190) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`),
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_fired_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_job_details
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_job_details`;
CREATE TABLE `winteree_core_qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(190) NOT NULL,
  `JOB_GROUP` varchar(190) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`),
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_job_details
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_qrtz_job_details` VALUES ('WinterEEScheduler', 'UpdatePostPageRankJob', 'CMS', '定时更新文章评级', 'com.winteree.core.task.UpdatePostPageRankJob', '0', '0', '0', '0', 0xACED0005737200156F72672E71756172747A2E4A6F62446174614D61709FB083E8BFA9B0CB020000787200266F72672E71756172747A2E7574696C732E537472696E674B65794469727479466C61674D61708208E8C3FBC55D280200015A0013616C6C6F77735472616E7369656E74446174617872001D6F72672E71756172747A2E7574696C732E4469727479466C61674D617013E62EAD28760ACE0200025A000564697274794C00036D617074000F4C6A6176612F7574696C2F4D61703B787000737200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000010770800000010000000007800);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_locks
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_locks`;
CREATE TABLE `winteree_core_qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_locks
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_qrtz_locks` VALUES ('WinterEEScheduler', 'STATE_ACCESS');
INSERT INTO `winteree_core_qrtz_locks` VALUES ('WinterEEScheduler', 'TRIGGER_ACCESS');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_paused_trigger_grps
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_paused_trigger_grps`;
CREATE TABLE `winteree_core_qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_paused_trigger_grps
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_scheduler_state
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_scheduler_state`;
CREATE TABLE `winteree_core_qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(190) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_scheduler_state
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_qrtz_scheduler_state` VALUES ('WinterEEScheduler', 'RenFeiMBP1594976091777', 1594976686167, 10000);
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_simple_triggers
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_simple_triggers`;
CREATE TABLE `winteree_core_qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `winteree_core_qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `winteree_core_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_simple_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_simprop_triggers
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_simprop_triggers`;
CREATE TABLE `winteree_core_qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `winteree_core_qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `winteree_core_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_simprop_triggers
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_qrtz_triggers
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_qrtz_triggers`;
CREATE TABLE `winteree_core_qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(190) NOT NULL,
  `TRIGGER_GROUP` varchar(190) NOT NULL,
  `JOB_NAME` varchar(190) NOT NULL,
  `JOB_GROUP` varchar(190) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(190) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`),
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`),
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`),
  CONSTRAINT `winteree_core_qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `winteree_core_qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of winteree_core_qrtz_triggers
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_qrtz_triggers` VALUES ('WinterEEScheduler', 'triggerUpdatePostPageRankJob', 'CMS', 'UpdatePostPageRankJob', 'CMS', NULL, 1595012400000, -1, 5, 'WAITING', 'CRON', 1594975669000, 0, NULL, 0, '');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_role
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_role`;
CREATE TABLE `winteree_core_role` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `uuid` varchar(36) NOT NULL,
  `tenant_uuid` varchar(36) DEFAULT NULL COMMENT '所属租户',
  `office_uuid` varchar(36) DEFAULT NULL COMMENT '归属机构',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `enname` varchar(255) DEFAULT NULL COMMENT '英文名称',
  `role_type` varchar(255) DEFAULT NULL COMMENT '角色类型',
  `data_scope` int(11) DEFAULT '3' COMMENT '数据范围,0:全部1:本公司2:本公司和本部门;3:本部门',
  `useable` tinyint(1) DEFAULT '1' COMMENT '是否可用',
  `create_by` varchar(64) NOT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(64) NOT NULL COMMENT '更新者',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注信息',
  `del_flag` char(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- ----------------------------
-- Records of winteree_core_role
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_role` VALUES (1, '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', NULL, '租户管理员', NULL, NULL, 0, 1, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-17 23:08:17', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 13:33:07', '测试角色的备注', '0');
INSERT INTO `winteree_core_role` VALUES (2, '54168D73-B773-4B25-A4A5-552E82F812EE', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', 'ed9ee135-029d-42d9-8a1c-b9a43a8c55ea', '测试', NULL, NULL, 1, 1, '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 13:34:45', '9369919A-F95E-44CF-AB0A-6BCD1D933403', '2020-06-18 13:34:45', '测试', '0');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COMMENT='角色与菜单关联表';

-- ----------------------------
-- Records of winteree_core_role_menu
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_role_menu` VALUES (13, '1C110097-440F-48EC-9E00-23BBE9F9B494', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', 'c26dd43a-6da7-4dd6-989e-ba9783f99679');
INSERT INTO `winteree_core_role_menu` VALUES (14, 'C33B8F32-CD47-42CB-8E5C-3A369B49864F', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '8dfce183-71ea-4011-89ac-b6b0739f5e01');
INSERT INTO `winteree_core_role_menu` VALUES (15, 'E108D058-D548-4FBB-AE13-9AF569566300', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '89e8e17b-446b-4f5d-9270-1b8b36bf787d');
INSERT INTO `winteree_core_role_menu` VALUES (16, '95D377D4-EB25-431B-8890-816E61E18926', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '3fcd9235-13fc-4868-bf7b-b392f069b626');
INSERT INTO `winteree_core_role_menu` VALUES (17, 'FE36D5B4-7C1D-4414-AD95-4716C780A6B6', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '359c1552-8458-4a18-965a-e4250352745a');
INSERT INTO `winteree_core_role_menu` VALUES (18, '2ABF72E7-0304-41A5-8634-21BBC3F225D6', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', 'ebc59fe5-6783-45c7-8072-2aa4e5037b48');
INSERT INTO `winteree_core_role_menu` VALUES (19, '9675FC3A-B7D6-464D-BE7E-BFF23A472B1E', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', 'a4c13987-75de-4218-abae-2a7a75bcc05a');
INSERT INTO `winteree_core_role_menu` VALUES (20, '56578BEC-BFEC-4AE5-B6DD-28ACDAD78264', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '96094ee8-5242-4332-9c0a-0d50bf47789b');
INSERT INTO `winteree_core_role_menu` VALUES (21, 'E3D80CB4-2FEA-4ED8-B395-86BE448EE7B3', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', 'fd5037f3-a1e1-4735-b7da-68707fbc10c4');
INSERT INTO `winteree_core_role_menu` VALUES (22, 'A4A78893-5E15-4680-BE3F-6F986D6220FC', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', 'ec3ad7f8-376a-4c98-a899-ee9343dc19e8');
INSERT INTO `winteree_core_role_menu` VALUES (23, 'A977C56B-3D21-490A-B983-AF0D0454BBFE', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '43e6fea3-5091-4aec-8696-6f33f239e058');
INSERT INTO `winteree_core_role_menu` VALUES (24, 'DB060430-13BA-464F-BD12-3EEEA5E1D26E', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '3706dcbb-541c-4c94-b0f9-3f8bf4792d8a');
INSERT INTO `winteree_core_role_menu` VALUES (25, '21833D3C-59EE-4583-871C-FE2482BA7E80', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '36103ee9-6f0d-438b-9c5a-667bcc8e7a89');
INSERT INTO `winteree_core_role_menu` VALUES (26, 'CDBBF740-1942-4BF6-B52C-590B3905368D', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '88f5a3d0-dadd-43e9-9b09-38645cd582ee');
INSERT INTO `winteree_core_role_menu` VALUES (27, 'CDE64AF2-28B4-427A-9497-AE52D62B0BB7', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', '3958c51e-e2db-4778-a0ed-86c30c88a759');
INSERT INTO `winteree_core_role_menu` VALUES (28, '7EEA7B03-9E73-4F9D-8225-98498BAD5976', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2', 'a6c0a304-e55d-4782-97c4-77e2bd762f37');
INSERT INTO `winteree_core_role_menu` VALUES (29, '30197263-87DE-4C00-A09C-314C724FD529', '54168D73-B773-4B25-A4A5-552E82F812EE', 'c26dd43a-6da7-4dd6-989e-ba9783f99679');
INSERT INTO `winteree_core_role_menu` VALUES (30, '8E736491-D24F-45FE-A3AE-41D896AC93B7', '54168D73-B773-4B25-A4A5-552E82F812EE', '8dfce183-71ea-4011-89ac-b6b0739f5e01');
INSERT INTO `winteree_core_role_menu` VALUES (31, '321B1F9E-2ACB-4A7F-9C07-4A2750DCDC71', '54168D73-B773-4B25-A4A5-552E82F812EE', '89e8e17b-446b-4f5d-9270-1b8b36bf787d');
INSERT INTO `winteree_core_role_menu` VALUES (32, '3AAC5D66-6C21-42C4-96C4-BF8618268017', '54168D73-B773-4B25-A4A5-552E82F812EE', '3fcd9235-13fc-4868-bf7b-b392f069b626');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COMMENT='秘钥表';

-- ----------------------------
-- Records of winteree_core_secret_key
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_secret_key` VALUES (1, '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', '2020-04-21 11:57:34', NULL, 'cd48362b-fc2b-432d-9eaf-2c9d54f83923');
INSERT INTO `winteree_core_secret_key` VALUES (2, NULL, 'G0dUm7GqVKNOI0WX', '2020-04-21 11:57:34', NULL, '64a01c36-f863-494d-80ab-85439c512536');
INSERT INTO `winteree_core_secret_key` VALUES (3, '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', '2020-04-21 13:56:43', NULL, '9ac93039-0e73-43cf-ace2-82032f1a8711');
INSERT INTO `winteree_core_secret_key` VALUES (4, NULL, '988tF14c7U0A5FcD', '2020-04-21 13:56:43', NULL, '9070458e-4335-488b-9388-6d107458ac00');
INSERT INTO `winteree_core_secret_key` VALUES (5, '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', '2020-04-21 16:58:42', NULL, '7cdb6e2f-82e5-4bde-a05d-55ba13fece66');
INSERT INTO `winteree_core_secret_key` VALUES (6, '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', '2020-04-21 17:14:54', NULL, 'f140ae53-5d38-4b01-be16-a306bbdf119e');
INSERT INTO `winteree_core_secret_key` VALUES (7, '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', '2020-04-25 16:40:51', NULL, '1bd93b2c-c642-424b-b69f-dc50384c7ab6');
INSERT INTO `winteree_core_secret_key` VALUES (8, NULL, '124ee6tm56T3F3BC', '2020-04-25 16:40:51', NULL, '35657532-5856-40ae-8d7c-25e9e243a2f8');
INSERT INTO `winteree_core_secret_key` VALUES (9, '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', '2020-06-11 21:37:20', NULL, '5672e881-32d8-420b-955f-7a344108cb10');
INSERT INTO `winteree_core_secret_key` VALUES (10, NULL, 'imNLlnFCz2vUdca5', '2020-06-11 21:37:21', NULL, '83ab8259-4f0f-4de9-9b07-5092736b2e9a');
INSERT INTO `winteree_core_secret_key` VALUES (11, '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', '2020-06-12 23:09:14', NULL, 'd67ef3ed-1759-4b86-afd9-3df583692724');
INSERT INTO `winteree_core_secret_key` VALUES (12, NULL, 'wYkH2KG7Sx82EJYI', '2020-06-12 23:09:14', NULL, 'b1dcf9b8-d2f2-484e-a71d-1214a6f80bf6');
INSERT INTO `winteree_core_secret_key` VALUES (13, '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', '2020-06-17 16:52:23', NULL, 'a95b3d2d-280e-4ba1-aa91-e0fb7b11e680');
INSERT INTO `winteree_core_secret_key` VALUES (14, NULL, '1C73y8FjiD6CLQt7', '2020-06-17 16:52:23', NULL, '4f89d844-2d01-40ac-9ca5-bb55634f5a64');
INSERT INTO `winteree_core_secret_key` VALUES (15, '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', '2020-06-18 16:24:36', NULL, '2238d4a5-a140-4224-98a9-f607d3a1d300');
INSERT INTO `winteree_core_secret_key` VALUES (16, NULL, 'L72QDy2F4F791xbN', '2020-06-18 16:24:36', NULL, '9ba297be-656a-44ec-a018-e4aac94b3b63');
INSERT INTO `winteree_core_secret_key` VALUES (17, 'MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAke0BRPrz1PmVeAZEPNLtG/1vxg235efyGL+mccEoypfm3YjplvmK/qafyTKzGOlAfVieCjgbjh3zEA3QOWf50OFkQ7k5Z6Xoy5ZnnteeLyGoEWKbovjsJ5fJJBU35YG65wmnwHeJZAPxpZCljMyYjfv0Ctjch1xE06alDROdbUQQv89qiAwL3vxbqbNhCZw+HOhyKt4K5RDGHw+eX+Lr7KA1ATTBcbk1Xp2rKaQXFUPEkVhin313yEjjReI7AEezLz9zE5QW3rcjfOjlUGIh3PoAWG5OsZSqy4dVrVkdhkvZLxwP2snrzX9YMMHlEdKfH/ce0Dz9AcdBHgS9ZVulPQIDAQAB', 'MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCR7QFE+vPU+ZV4BkQ80u0b/W/GDbfl5/IYv6ZxwSjKl+bdiOmW+Yr+pp/JMrMY6UB9WJ4KOBuOHfMQDdA5Z/nQ4WRDuTlnpejLlmee154vIagRYpui+Ownl8kkFTflgbrnCafAd4lkA/GlkKWMzJiN+/QK2NyHXETTpqUNE51tRBC/z2qIDAve/Fups2EJnD4c6HIq3grlEMYfD55f4uvsoDUBNMFxuTVenasppBcVQ8SRWGKffXfISONF4jsAR7MvP3MTlBbetyN86OVQYiHc+gBYbk6xlKrLh1WtWR2GS9kvHA/ayevNf1gwweUR0p8f9x7QPP0Bx0EeBL1lW6U9AgMBAAECggEAeE3atBHon/a74V3ZOAP08xR35+I7cFFO6FRoVnQWux+Md6t+WLeWfebE6MzhlnIGdtBuSqKDJSFP2DZrKkisTibj0GmOCANh7yYqe2vrv5ORyfNvuGZb2zBp5VOQ56eSfRMat7RVEp2lxQeBVJkAVF4Zji/Hb9kD7TH/9M3tBbzFmW/NKCD5zZkebXL3m2P6GSQhbybBvj1giU/SLu4yY1Kt1IAbkt8/6MhvzEyH0ImhO5geKBO6juy1ctx29Oc88LxWZ1B6Ts54Fou3FiJpm4S1x3isbugOuAsWPhs7S9bq8Uc9ZGTVMqiEW27YZjmko0jClu8zxQQemUZ8fwF5SQKBgQD2lJdr9TuP8JoITUdL4COiImoqZe+nylbjiAxCDCBXdmOZ3vbVJE2NvAypL8H6E3yrvRp67POW7EStQLNX8hSTPHO66n1GwhhQzpf1NJCmCCneQ2OsFwTSVF7deBUVMR1c5XMfybekIVPg6eH8l8xsc1iXAz9oAmgT9ieA6g9Y8wKBgQCXgBJVR/1TbVeR65nqA9yrzGjLh60QNhG0/x39wEBhQ56ErdOs5HVCj8EnU+8m7GlXI40leNHKVNULzkTVsAFT+RT5gCve8gpClKyo89S+qGzHQHWc1bDwM/G28cdUD8RsiY6jWVYCKEzTrf+lDsqIFqk19MNinvYWQ5iQLZCVDwKBgBATth+62IxVAqYB6+EdlxUuKz/ib5LAFZQ2PZ5czgsF1A6nQ+63tDrGhQjO7VXRyXIlHUHgiy1O8FdffZIBXagP6/fG7C4d35n9Llk1eSzqIVi1ih0hTxYrnBfSiYVdM23oI+5xp4CTD7X9adefShvUFXWbJ5ovdfu/M+oq2J9TAoGAE8pOGsfRpsFEzDH7pB8nB2YnVlb9231EqOEemNQxVMNRhMF0QkSBsNU4hRuoxOvjAtgJCxkSRpz4S5eBVGyub7q86667jF8PygzD1Tos008LDqgz9O3RND+E1sIUqL16ijcZ4+q6moOLLaewGm6pNEfJiNvVXh+nw9bGqibZmS8CgYEAqrPrKYpPIJI+CbiXU5s/G4zr4dACEdoqeYHzfW/oN5jt6WctDHHCHruiF+GnDFraJx7rb7eg8GF3dkdHq4VhDwzaKqUwx6P2A3rgIaeZNDVNo0LJSo9+pD5c6ok6xBAYowMAc06554G+i8rcGVODEBc/tmqS6XuMUyOgTveB/z0=', '2020-06-22 13:40:19', NULL, '2951fa3f-f8b4-461b-9efe-cb8e6197b92b');
INSERT INTO `winteree_core_secret_key` VALUES (18, NULL, 'pO2uE77bh9Oso4HQ', '2020-06-22 13:40:19', NULL, 'd9a2fddc-4c4a-4adf-b350-bb3af791512b');
INSERT INTO `winteree_core_secret_key` VALUES (19, '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', '2020-06-22 14:06:21', NULL, '8a82725c-8906-4719-ab88-9c5f90f1b66c');
INSERT INTO `winteree_core_secret_key` VALUES (20, NULL, 'p340MYnQfvDNfn2F', '2020-06-22 14:06:21', NULL, '7c7b98b4-2f7d-4208-9683-3269717b11fc');
INSERT INTO `winteree_core_secret_key` VALUES (21, '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', '2020-07-06 16:05:51', NULL, 'e5520839-0ab1-4d52-a8f0-20973d6892d4');
INSERT INTO `winteree_core_secret_key` VALUES (22, NULL, 'WjxfXgmVm6s566AY', '2020-07-06 16:05:51', NULL, '92a1917f-5f25-4d37-8493-a3fd914dd274');
INSERT INTO `winteree_core_secret_key` VALUES (23, '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', '2020-07-07 13:29:05', NULL, '3fa70258-87ea-4f99-a169-fd3b20ebf8b9');
INSERT INTO `winteree_core_secret_key` VALUES (24, NULL, 'wpg1Cau3pXcchNXn', '2020-07-07 13:29:05', NULL, '449cbbc3-2e98-4494-80c8-bdff8a56b890');
INSERT INTO `winteree_core_secret_key` VALUES (25, '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', '2020-07-15 12:41:56', NULL, '0f9ce224-98a2-4cc4-9179-3e44415bf89c');
INSERT INTO `winteree_core_secret_key` VALUES (26, NULL, 'PDc6Qd8wwk829390', '2020-07-15 12:41:56', NULL, '4171c6e2-73aa-41ba-8372-350651cf045c');
COMMIT;

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
-- Records of winteree_core_setting
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_tenant
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_tenant`;
CREATE TABLE `winteree_core_tenant` (
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
-- Records of winteree_core_tenant
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_tenant` VALUES (1, 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '系统租户', '2020-04-07 10:02:23', '2099-12-31 12:59:59', 1, '2020-04-27 13:44:40');
COMMIT;

-- ----------------------------
-- Table structure for winteree_core_tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `winteree_core_tenant_info`;
CREATE TABLE `winteree_core_tenant_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `uuid` varchar(36) NOT NULL COMMENT 'uuid',
  `tenant_uuid` varchar(36) NOT NULL COMMENT 'tenant_uuid',
  `administrators` text COMMENT '负责人',
  `contact` text COMMENT '联系方式',
  `address` text COMMENT '地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uuid` (`uuid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='租户基础信息';

-- ----------------------------
-- Records of winteree_core_tenant_info
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_tenant_info` VALUES (1, 'b11eac80-1312-49f4-a171-d1fd0056d550', 'BC21F895-63DA-4E94-9D9E-D4CD2DCFB189', '任霏', 'i@renfei.net', '北京市石景山区');
COMMIT;

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='用户与角色关联表';

-- ----------------------------
-- Records of winteree_core_user_role
-- ----------------------------
BEGIN;
INSERT INTO `winteree_core_user_role` VALUES (1, 'AAFC8C03-3FEC-4E13-BAFD-1EB77A33DFCF', '6853F941-EF46-4B0D-AC2C-5A6B8D5C9626', '997FBB41-0EE2-44ED-B94F-EFDDA316EAA2');
INSERT INTO `winteree_core_user_role` VALUES (2, 'EBDBD489-28CF-43D7-9E2E-CCD1968282FE', 'BBB1DA44-658F-4F3F-B33E-24F14F6486B0', '54168D73-B773-4B25-A4A5-552E82F812EE');
COMMIT;

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

-- ----------------------------
-- Records of winteree_core_verification_code
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
