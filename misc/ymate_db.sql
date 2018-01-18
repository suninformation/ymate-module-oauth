-- ----------------------------
--  Table structure for `ym_oauth_client`
-- ----------------------------
DROP TABLE IF EXISTS `ym_oauth_client`;
CREATE TABLE `ym_oauth_client` (
  `id` varchar(32) NOT NULL COMMENT '客户端唯一标识',
  `name` varchar(32) NOT NULL COMMENT '客户端名称',
  `icon_url` varchar(255) DEFAULT NULL COMMENT '客户端LOGO图片URL地址',
  `secret_key` varchar(32) NOT NULL COMMENT '客户端密钥',
  `description` varchar(1000) DEFAULT NULL COMMENT '客户端描述',
  `access_token` varchar(128) DEFAULT NULL COMMENT '客户端访问令牌',
  `last_access_token` varchar(128) DEFAULT NULL COMMENT '上次客户端访问令牌',
  `expires_in` int(11) DEFAULT '0' COMMENT '令牌过期时间(秒)',
  `ip_white_list` varchar(200) DEFAULT NULL COMMENT '客户端访问IP地址白名单',
  `type` smallint(2) unsigned DEFAULT '0' COMMENT '客户端类型',
  `status` smallint(2) unsigned DEFAULT '0' COMMENT '客户端状态',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `last_modify_time` bigint(13) DEFAULT '0' COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth客户端授权信息';

-- ----------------------------
--  Table structure for `ym_oauth_user`
-- ----------------------------
DROP TABLE IF EXISTS `ym_oauth_user`;
CREATE TABLE `ym_oauth_user` (
  `id` varchar(32) NOT NULL COMMENT '用户授权唯一标识',
  `uid` varchar(32) NOT NULL COMMENT '用户唯一标识',
  `client_id` varchar(32) NOT NULL COMMENT '客户端唯一标识',
  `is_authorized` smallint(1) unsigned DEFAULT '0' COMMENT '用户是否已授权',
  `access_token` varchar(128) DEFAULT NULL COMMENT '用户授权访问令牌',
  `last_access_token` varchar(128) DEFAULT NULL COMMENT '上次用户授权访问令牌',
  `refresh_token` varchar(128) DEFAULT NULL COMMENT '授权刷新令牌',
  `refresh_count` smallint(1) unsigned DEFAULT '0' COMMENT '授权令牌已刷新次数',
  `expires_in` int(11) DEFAULT '0' COMMENT '令牌过期时间(秒)',
  `scope` varchar(100) DEFAULT NULL COMMENT '用户已授权作用域',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  `last_modify_time` bigint(13) DEFAULT '0' COMMENT '最后修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OAuth用户授权信息';