/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 5.6.17 : Database - datawindow-211007-bak
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`datawindow-211007-bak` /*!40100 DEFAULT CHARACTER SET utf8 */;

/*Table structure for table `qrtz_blob_triggers` */

DROP TABLE IF EXISTS `qrtz_blob_triggers`;

CREATE TABLE `qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  CONSTRAINT `qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_blob_triggers` */

LOCK TABLES `qrtz_blob_triggers` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_calendars` */

DROP TABLE IF EXISTS `qrtz_calendars`;

CREATE TABLE `qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_calendars` */

LOCK TABLES `qrtz_calendars` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_cron_triggers` */

DROP TABLE IF EXISTS `qrtz_cron_triggers`;

CREATE TABLE `qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(120) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_cron_triggers` */

LOCK TABLES `qrtz_cron_triggers` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_fired_triggers` */

DROP TABLE IF EXISTS `qrtz_fired_triggers`;

CREATE TABLE `qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`),
  KEY `IDX_QRTZ_FT_TRIG_INST_NAME` (`SCHED_NAME`,`INSTANCE_NAME`) USING BTREE,
  KEY `IDX_QRTZ_FT_INST_JOB_REQ_RCVRY` (`SCHED_NAME`,`INSTANCE_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_FT_J_G` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_T_G` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_FT_TG` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_fired_triggers` */

LOCK TABLES `qrtz_fired_triggers` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_job_details` */

DROP TABLE IF EXISTS `qrtz_job_details`;

CREATE TABLE `qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  KEY `IDX_QRTZ_J_REQ_RECOVERY` (`SCHED_NAME`,`REQUESTS_RECOVERY`) USING BTREE,
  KEY `IDX_QRTZ_J_GRP` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_job_details` */

LOCK TABLES `qrtz_job_details` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_locks` */

DROP TABLE IF EXISTS `qrtz_locks`;

CREATE TABLE `qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_locks` */

LOCK TABLES `qrtz_locks` WRITE;

insert  into `qrtz_locks`(`SCHED_NAME`,`LOCK_NAME`) values ('SchedulerFactory','TRIGGER_ACCESS');

UNLOCK TABLES;

/*Table structure for table `qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `qrtz_paused_trigger_grps`;

CREATE TABLE `qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_paused_trigger_grps` */

LOCK TABLES `qrtz_paused_trigger_grps` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_scheduler_state` */

DROP TABLE IF EXISTS `qrtz_scheduler_state`;

CREATE TABLE `qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_scheduler_state` */

LOCK TABLES `qrtz_scheduler_state` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_simple_triggers` */

DROP TABLE IF EXISTS `qrtz_simple_triggers`;

CREATE TABLE `qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_simple_triggers` */

LOCK TABLES `qrtz_simple_triggers` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_simprop_triggers` */

DROP TABLE IF EXISTS `qrtz_simprop_triggers`;

CREATE TABLE `qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
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
  CONSTRAINT `qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_simprop_triggers` */

LOCK TABLES `qrtz_simprop_triggers` WRITE;

UNLOCK TABLES;

/*Table structure for table `qrtz_triggers` */

DROP TABLE IF EXISTS `qrtz_triggers`;

CREATE TABLE `qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `IDX_QRTZ_T_J` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_JG` (`SCHED_NAME`,`JOB_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_C` (`SCHED_NAME`,`CALENDAR_NAME`) USING BTREE,
  KEY `IDX_QRTZ_T_G` (`SCHED_NAME`,`TRIGGER_GROUP`) USING BTREE,
  KEY `IDX_QRTZ_T_STATE` (`SCHED_NAME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_STATE` (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_N_G_STATE` (`SCHED_NAME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NEXT_FIRE_TIME` (`SCHED_NAME`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST` (`SCHED_NAME`,`TRIGGER_STATE`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_STATE`) USING BTREE,
  KEY `IDX_QRTZ_T_NFT_ST_MISFIRE_GRP` (`SCHED_NAME`,`MISFIRE_INSTR`,`NEXT_FIRE_TIME`,`TRIGGER_GROUP`,`TRIGGER_STATE`) USING BTREE,
  CONSTRAINT `qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `qrtz_triggers` */

LOCK TABLES `qrtz_triggers` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_banner` */

DROP TABLE IF EXISTS `t_banner`;

CREATE TABLE `t_banner` (
  `id` varchar(32) NOT NULL,
  `title` varchar(100) DEFAULT NULL COMMENT '图片名称',
  `file_id` varchar(32) DEFAULT NULL COMMENT '图片地址',
  `sort` int(2) DEFAULT '1' COMMENT '排序号',
  `status` int(4) DEFAULT '1' COMMENT '状态：0失效1有效',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(32) DEFAULT NULL COMMENT '创建人',
  `remark` varchar(100) DEFAULT NULL COMMENT '描述',
  `href` text,
  `resource_type` varchar(32) DEFAULT NULL COMMENT '资源类型id',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源ID',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='轮播图';

/*Data for the table `t_banner` */

LOCK TABLES `t_banner` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_bi_independent` */

DROP TABLE IF EXISTS `t_bi_independent`;

CREATE TABLE `t_bi_independent` (
  `id` varchar(32) DEFAULT NULL,
  `bi_user_id` varchar(32) DEFAULT NULL COMMENT 'bi用户ID',
  `bi_project_id` varchar(32) DEFAULT NULL COMMENT 'bi项目ID',
  `bi_server_id` varchar(32) DEFAULT NULL COMMENT 'bi服务id',
  `bi_own_folder_id` varchar(255) DEFAULT NULL COMMENT 'bi自主分析地址ID（mstr创建文件夹后返回ID）',
  `bi_own_folder_name` varchar(255) DEFAULT NULL COMMENT 'bi自主分析地址名称',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `tenant_id` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='自主分析地址表';

/*Data for the table `t_bi_independent` */

LOCK TABLES `t_bi_independent` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_bi_mapping` */

DROP TABLE IF EXISTS `t_bi_mapping`;

CREATE TABLE `t_bi_mapping` (
  `id` varchar(32) NOT NULL,
  `bi_user_id` varchar(255) DEFAULT NULL COMMENT 'BI用户id',
  `user_id` varchar(32) NOT NULL COMMENT 'portal用户id',
  `type` varchar(50) NOT NULL COMMENT '类型：1MSTR',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户映射表';

/*Data for the table `t_bi_mapping` */

LOCK TABLES `t_bi_mapping` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_bi_project` */

DROP TABLE IF EXISTS `t_bi_project`;

CREATE TABLE `t_bi_project` (
  `id` varchar(32) NOT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '项目名称',
  `project` varchar(255) DEFAULT NULL COMMENT '项目参数',
  `bi_server_id` varchar(255) DEFAULT NULL,
  `param_dossier` varchar(255) DEFAULT NULL COMMENT 'dossier参数',
  `param_doc` varchar(255) DEFAULT NULL COMMENT 'document参数',
  `param_report` varchar(255) DEFAULT NULL COMMENT 'report参数',
  `param_folder` varchar(255) DEFAULT NULL COMMENT 'folder参数',
  `bo_authentication` varchar(255) DEFAULT NULL COMMENT 'bo认证参数',
  `remark` varchar(255) DEFAULT NULL COMMENT '项目描述',
  `param` varchar(255) DEFAULT NULL COMMENT '附件参数',
  `is_indepen_pro` int(4) DEFAULT '0' COMMENT 'MSTR项目是否为默认自动同步添加项目 1：是、0：否（默认）所有MSTR项目中只能有一个是自动同步添加的默认项目',
  `state` varchar(255) DEFAULT NULL COMMENT '状态',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_bi_project` */

LOCK TABLES `t_bi_project` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_bi_server` */

DROP TABLE IF EXISTS `t_bi_server`;

CREATE TABLE `t_bi_server` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '服务器名称',
  `type` int(4) DEFAULT NULL COMMENT '类型：1Mstr 2BO 3MySql 4自定义',
  `ip` varchar(255) DEFAULT NULL COMMENT '服务器ip',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `sort` int(4) DEFAULT NULL COMMENT '排序',
  `contact` varchar(50) DEFAULT NULL COMMENT '负责人',
  `remark` varchar(255) DEFAULT NULL COMMENT '描述',
  `default_uid` varchar(255) DEFAULT NULL COMMENT '默认用户名',
  `default_pwd` varchar(255) DEFAULT NULL COMMENT '默认密码',
  `url` varchar(255) DEFAULT NULL COMMENT '访问url',
  `server` varchar(255) DEFAULT NULL COMMENT 'server参数',
  `param` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `water_mark` int(11) DEFAULT '0' COMMENT '水印标识： 0 关闭  1 开启',
  `export_mode` int(11) DEFAULT '1' COMMENT '导出方式： 0 SKD方式  1 URL方式',
  `water_mark_text` varchar(65) DEFAULT NULL COMMENT '水印文本',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集成系统表';

/*Data for the table `t_bi_server` */

LOCK TABLES `t_bi_server` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_bi_user` */

DROP TABLE IF EXISTS `t_bi_user`;

CREATE TABLE `t_bi_user` (
  `id` varchar(32) NOT NULL,
  `username` varchar(100) DEFAULT NULL COMMENT 'mstr用户名',
  `password` varchar(100) DEFAULT NULL COMMENT 'mstr密码',
  `bi_group` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `own_folder_id` varchar(32) DEFAULT NULL COMMENT '自主分析文件夹ID',
  `bi_server_id` varchar(32) DEFAULT NULL,
  `bi_object_id` varchar(32) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集成用户表';

/*Data for the table `t_bi_user` */

LOCK TABLES `t_bi_user` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_comment` */

DROP TABLE IF EXISTS `t_comment`;

CREATE TABLE `t_comment` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `score` int(11) DEFAULT NULL COMMENT '分数',
  `content` mediumtext,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `tenant_id` varchar(32) DEFAULT NULL,
  `roof` int(11) DEFAULT '0',
  `reply_userid` varchar(32) DEFAULT NULL,
  `reply_content` varchar(500) DEFAULT NULL,
  `reply_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='评论表';

/*Data for the table `t_comment` */

LOCK TABLES `t_comment` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_config` */

DROP TABLE IF EXISTS `t_config`;

CREATE TABLE `t_config` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '配置项名称',
  `code` varchar(255) DEFAULT NULL COMMENT '配置项编码',
  `value` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT '1',
  `is_display` int(11) DEFAULT '1' COMMENT '是否显示。1、是；0、否（默认显示）',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置表';

/*Data for the table `t_config` */

LOCK TABLES `t_config` WRITE;

insert  into `t_config`(`id`,`name`,`code`,`value`,`remark`,`state`,`is_display`,`tenant_id`) values ('10','收藏加载类型','COLLECT_LOAD_TYPE','asyncLoad','asyncLoad（异步加载）、synchLoad 同步加载',1,1,'1'),('1045229206919401474','菜单导航','SYS_THEME','theme_left_admin','',1,1,'1'),('11','是否提示修改密码','SYS_FIRST_UPDATE_PWD','1','0  不提示   1 提示',1,1,'1'),('1102869629445902390','是否开启系统多租户','SYS_USER_TENANT','0','1表示开启，0表示关闭，默认是1',1,1,'1'),('1139067372094410753','重复登录','REPEAT_LOGIN','0','1、开启，表示同意重复登录。0,、关闭，表示不同意。',1,1,'1'),('1139067372203462657','不同意重复登录选择','NOT_ALLOW_REPEAT_LOGIN_WAY','0','0、已登录不允许再登陆。  1、后登录踢出先登录',1,1,'1'),('1139067372295737346','登录提示信息','LOGIN_PROMPT','0','1、开启。登录提示。  0、关闭  登录不提示。',1,1,'1'),('1139067372388012034','是否开启忘记密码提示','FORGET_PASSWORD_PROMPT','1','1、开启， 提示 。     0 、关闭 不提示',1,1,'1'),('1139067372622893057','是否开启密码定期更新提示','PASSWORD_UPDATE_REGULARLY','0','1、开启   提示。   0关闭、不提示',1,1,'1'),('1139067372807442433','开启密码定期更新提示时间','PASSWORD_UPDATE_REGULARLY_TIME','1','1  表示一个月  2， 表示两个月',1,1,'1'),('1139067372924882946','密码强度要求','PASSWORD_STRENGTH_REQUIRE','1','1、开启， 要求 。     0 、关闭 不要求',1,1,'1'),('1139067373252038657','长度至少','LEAST_LENGTH','0','1、勾选       0、不勾选',1,1,'1'),('1139067373612748802','长度值','LENGTH_VALUE','6','默认6',1,1,'1'),('1139067373830852610','包含数字','CONTAIN_NUMBER','0','1、勾选       0、不勾选',1,1,'1'),('1139067373927321601','包含字母','CONTAINT_LETTERS','0','1、勾选       0、不勾选',1,1,'1'),('1139067374015401985','包含符号','CONTAINT_SYMBOLS','0','1、勾选       0、不勾选',1,1,'1'),('1139067374107676673','登录页标题','SYS_LOGIN_NAME','数窗平台','登录页标题',1,1,'1'),('1139067374204145665','系统顶部LOGO','SYS_MAIN_LOGO','/upload/logo/69/97/98eed0f18de54e419601687ecd36b713.png','value值应该为上传的路径（t_file表中的file_path_view字段值）。',1,1,'1'),('1139455854113611777','密码','MAIL_PASSWORD','FKdrgnnVXPmpmGJ9','密码',1,1,'1'),('1139456362219986946','端口','MAIL_PORT','465','端口',1,1,'1'),('1139456805784412162','发件人地址','MAIL_FROM','support-3@dataondemand.cn','发件人地址',1,1,'1'),('1139457178377019393','显示名称','MAIL_ACCOUNT','德昂数窗平台','显示名称',1,1,'1'),('1139457931510439938','邮件服务器','MAIL_HOST','smtp.exmail.qq.com','邮件服务器',1,1,'1'),('1143416447615000578','系统首页名称','SYS_HOME_PAGE_NAME','首页','系统首页名称',1,1,'1'),('1143416855343292417','系统首页类型','PREFERENCE_TYPE','favorites','系统首页类型',1,1,'1'),('1143417204015783937','系统首页对象值','PREFERENCE_VALUE','','系统首页对象值',1,1,'1'),('1161514758106423298','会话有效期','SESSION_TIME','30','会话（session）有效期，默认为30分钟',1,1,'1'),('1176386275252986129','AD域管理员账号','DOMAIN_ACCOUNT','administrator','AD管理员账号',1,1,'1'),('12','显示菜单图标','SYS_MENU_ICON','1','是否显示菜单图标  1、显示  0 不显示 ；默认为0 ',1,1,'1'),('1243457021662543873','企业微信CorpId','CORPID','122222','企业微信CorpId',1,1,'1'),('1243457021922590721','企业微信Secret','SECRET','122222','企业微信Secret',1,1,'1'),('1243457021960339457','企业微信应用ID','AGENTID','122222','企业微信应用ID',1,1,'1'),('1243457022002282498','企业微信标签名','TABNAME','aaa','企业微信标签名',1,1,'1'),('1243457022040031234','企业微信信任域名','WXDOMAIN','127.158.78.788','企业微信信任域名',1,1,'1'),('13','同步创建MSTR用户','SYS_SYNC_CREATE_MSTR_USER','0','创建数窗用户的时候是否同步创建MSTR用户。1，是；0(或者空，或者不设置这个参数)否',1,0,'1'),('1334310675996000257','密码错误用户锁定','PASSWORD_ERROR_LOCK','0','1、开启   提示。   0关闭、不提示',1,1,'1'),('1334310923619319810','密码错误次数','PASSWORD_ERROR_COUNT','5','默认5',1,1,'1'),('14','微信回调地址','SYS_URL','http://wx.dataondemand.cn',NULL,1,1,'1'),('1745229276919471474','默认语言','CONFIG_LOCALE','zh_CN','系统设置中的系统默认语言',1,1,'1'),('2','验证码','SYS_CHECK_CODE','0','0禁用1启用',1,1,'1'),('3','系统名称','SYS_NAME','Report Center','',1,1,'1'),('4','登录页面','SYS_LOGIN_PAGE','login/login_edu','',1,1,'1'),('5','上传路径','SYS_UPLOAD_PATH','E:/Program Files/apache-tomcat-8.5.32/upload','上传文件路径',1,1,'1'),('6','系统logo','SYS_LOGO','/upload/logo/10/4/827525b5e2c644b4a4d846eb057ec89d.png','登录页logo图片，png格式，内部图片为 %_inner.png',1,1,'1'),('7','版权信息','SYS_COPYRIGHT','©2021 新一代数据门户','备案信息',1,1,'1'),('7176383775112656129','系统登录选项','LOGIN_OPTION','1','1:数窗用户登录  2：AD域用户登录',1,1,'1'),('7176838751152616592','AD域名','DOMAIN_NAME','datawindow.vm.com','AD域名',1,1,'1'),('7176838757252656129','AD管理员密码','DOMAIN_PASSWORD','mAServicE7m.JL','AD管理员密码',1,1,'1'),('8','系统默认首页','SYS_HOME_INDEX','index','系统首页内容',1,1,'1'),('9','邮件提醒（创建用户）','SYS_USER_EMAIL','1','0禁用1启用',1,1,'1'),('9976838751152616592','AD域IP','DOMAIN_IP','192.168.43.131','AD域IP',1,1,'1');

UNLOCK TABLES;

/*Table structure for table `t_data_permission` */

DROP TABLE IF EXISTS `t_data_permission`;

CREATE TABLE `t_data_permission` (
  `id` varchar(32) NOT NULL,
  `dpname` varchar(255) DEFAULT NULL COMMENT '数据权限名称',
  `type_id` varchar(32) DEFAULT NULL COMMENT '数据权限类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `extcode` varchar(255) DEFAULT NULL COMMENT '数据权限编码',
  `remark` varchar(640) DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT 'id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_data_permission` */

LOCK TABLES `t_data_permission` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_data_permission_type` */

DROP TABLE IF EXISTS `t_data_permission_type`;

CREATE TABLE `t_data_permission_type` (
  `id` varchar(32) NOT NULL,
  `name` varchar(320) DEFAULT NULL COMMENT '数据权限类型名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '数据权限类型父id',
  `extcode` varchar(320) DEFAULT NULL COMMENT '数据权限类型编码',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `remark` varchar(640) DEFAULT NULL COMMENT '描述',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_data_permission_type` */

LOCK TABLES `t_data_permission_type` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_datasource` */

DROP TABLE IF EXISTS `t_datasource`;

CREATE TABLE `t_datasource` (
  `id` varchar(32) NOT NULL,
  `source_name` varchar(640) DEFAULT NULL COMMENT '数据源名称',
  `is_custom` int(1) DEFAULT '0' COMMENT '自定义 1 /预定义 0 自定义-自己设置URL，预定义-程序预置URL，用户设置参数',
  `database_name` varchar(840) DEFAULT NULL COMMENT '数据库名称',
  `database_type` int(3) DEFAULT NULL COMMENT '数据库类型',
  `database_path` varchar(3600) DEFAULT NULL COMMENT '数据库地址',
  `database_port` varchar(24) DEFAULT NULL COMMENT '端口号',
  `database_username` varchar(840) DEFAULT NULL COMMENT '用户名',
  `database_password` varchar(840) DEFAULT NULL COMMENT '密码',
  `is_active` int(1) DEFAULT NULL COMMENT '是否激活（0 否 ，1 是）',
  `is_delete` int(1) DEFAULT NULL COMMENT '是否删除（0 否， 1 是）',
  `remark` varchar(3600) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_datasource` */

LOCK TABLES `t_datasource` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_dict` */

DROP TABLE IF EXISTS `t_dict`;

CREATE TABLE `t_dict` (
  `id` varchar(32) NOT NULL,
  `dict_name` varchar(255) DEFAULT NULL COMMENT '名称',
  `dict_code` varchar(100) DEFAULT NULL COMMENT '编码',
  `item_name` varchar(100) DEFAULT NULL COMMENT '字典项',
  `item_value` varchar(255) DEFAULT NULL COMMENT '字典项值',
  `item_order` int(11) DEFAULT NULL COMMENT '字典项排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `is_system` int(11) DEFAULT '0' COMMENT '是否系统内置，默认0表示用户字典',
  `is_source` int(1) DEFAULT '0' COMMENT '是否使用数据源',
  `query_sql` text COMMENT '查询sql',
  `source_id` varchar(32) DEFAULT NULL COMMENT '数据源id',
  `parent_dict_code` varchar(255) DEFAULT NULL COMMENT '父级编码',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='= ''数据字典表'' DEFAULT CHARSET=utf8';

/*Data for the table `t_dict` */

LOCK TABLES `t_dict` WRITE;

insert  into `t_dict`(`id`,`dict_name`,`dict_code`,`item_name`,`item_value`,`item_order`,`remark`,`is_system`,`is_source`,`query_sql`,`source_id`,`parent_dict_code`,`tenant_id`) values ('1','输入类型','input_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1'),('10','case级别','case_level',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('1063364188261335041',NULL,'input_type','日期区间','8',8,'',1,0,NULL,NULL,NULL,'1'),('1067354966306390018','集成日志类型','integration_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1'),('1067355021776060418',NULL,'integration_type','功能开发','1',1,'',1,0,NULL,NULL,NULL,'1'),('1067355090478759938',NULL,'integration_type','修复缺陷','2',2,'',1,0,NULL,NULL,NULL,'1'),('1068372647385817090','系统日志类型','sys_log_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1'),('1068372854303416322',NULL,'sys_log_type','新增','1',1,'新增操作',1,0,NULL,NULL,NULL,'1'),('1068372942140530689',NULL,'sys_log_type','删除','2',2,'删除操作',1,0,NULL,NULL,NULL,'1'),('1068373482362691586',NULL,'sys_log_type','编辑','3',3,'编辑操作',1,0,NULL,NULL,NULL,'1'),('1068373554181758978',NULL,'sys_log_type','查询','4',4,'查询操作',1,0,NULL,NULL,NULL,'1'),('1068373657013510145',NULL,'sys_log_type','登录','5',5,'登录操作',1,0,NULL,NULL,NULL,'1'),('1068373752131936257',NULL,'sys_log_type','注销','6',6,'注销操作',1,0,NULL,NULL,NULL,'1'),('1068373980880887809',NULL,'sys_log_type','查看预览','7',7,'查看预览报表、文件等',1,0,NULL,NULL,NULL,'1'),('1068374108651970562',NULL,'sys_log_type','上传','8',8,'上传操作',1,0,NULL,NULL,NULL,'1'),('1068374192122814466',NULL,'sys_log_type','下载','9',9,'下载操作',1,0,NULL,NULL,NULL,'1'),('1068374264466169858',NULL,'sys_log_type','收藏','10',10,'收藏操作',1,0,NULL,NULL,NULL,'1'),('1075298989247565826','报告记录','record_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1'),('1075299118369214466',NULL,'record_type','仅记录异常','1',1,'',0,0,NULL,NULL,NULL,'1'),('1075299265870303234',NULL,'record_type','异常记录+最近一次正常记录','2',2,'',0,0,NULL,NULL,NULL,'1'),('1085462331341672450',NULL,'case_reason','test','123',1,'testaaa',0,0,NULL,NULL,NULL,'1'),('11','case环境','case_evt',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('1102869626375671810','输入类型','input_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626484723714',NULL,'input_type','日期区间','8',8,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626526666754','集成日志类型','integration_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626635718658',NULL,'integration_type','功能开发','1',1,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626660884481',NULL,'integration_type','修复缺陷','2',2,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626719604738','系统日志类型','sys_log_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626769936385',NULL,'sys_log_type','新增','1',1,'新增操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626824462338',NULL,'sys_log_type','删除','2',2,'删除操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869626858016770',NULL,'sys_log_type','编辑','3',3,'编辑操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627009011713',NULL,'sys_log_type','查询','4',4,'查询操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627042566145',NULL,'sys_log_type','登录','5',5,'登录操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627088703490',NULL,'sys_log_type','注销','6',6,'注销操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627155812353',NULL,'sys_log_type','查看预览','7',7,'查看预览报表、文件等',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627185172482',NULL,'sys_log_type','上传','8',8,'上传操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627214532610',NULL,'sys_log_type','下载','9',9,'下载操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627285835777',NULL,'sys_log_type','收藏','10',10,'收藏操作',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627323584514','报告记录','record_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627352944642',NULL,'record_type','仅记录异常','1',1,'',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627453607937',NULL,'record_type','异常记录+最近一次正常记录','2',2,'',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627545882626',NULL,'case_reason','test','123',1,'testaaa',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627600408578',NULL,'input_type','文本','1',1,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627633963009',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627684294657',NULL,'input_type','数字','2',2,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627713654786',NULL,'input_type','日期','3',3,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627768180737','产品类型','case_product',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869627894009858',NULL,'case_product','mstr','1',1,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628015644674',NULL,'case_product','portal','2',2,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628053393409',NULL,'input_type','选择列表','4',4,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628137279490',NULL,'case_product','ETL','3',3,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628191805442','case原因','case_reason',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628229554178',NULL,'case_reason','培训不到位','1',1,'',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628267302913',NULL,'case_reason','沟通不到位','2',2,'',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628338606081','菜单类型','resource_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628430880769',NULL,'resource_type','功能菜单','1',1,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628548321282',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628623818754',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628661567489',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628695121921',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628737064961',NULL,'input_type','单选按钮','5',5,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628812562433','公告推荐等级','notice_level',NULL,'notice_level',NULL,NULL,1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628850311169',NULL,'notice_level','紧急','1',1,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628904837122',NULL,'notice_level','加急','2',2,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869628967751681',NULL,'notice_level','重大','3',3,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869629018083329',NULL,'notice_level','重要','4',4,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869629152301058',NULL,'notice_level','普通','5',5,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869629257158657',NULL,'input_type','复选框','6',6,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102869629320073218',NULL,'input_type','数字区间','7',7,'',1,0,NULL,NULL,NULL,'1102869620780470273'),('1102872847974436866','测试字典','Test',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1102869620780470273'),('1102872912231174145',NULL,'Test','测试01','1',1,'是否舒服撒法',0,0,NULL,NULL,NULL,'1102869620780470273'),('1125267759369785346','日期默认值-日','date_day',NULL,NULL,NULL,'日期默认值-日',1,0,NULL,NULL,NULL,'1'),('1125268427392389121',NULL,'date_day','当日','cur_day',1,'当日',1,0,NULL,NULL,NULL,'1'),('1125272181156057089',NULL,'date_day','昨日','last_day',2,'昨日',1,0,NULL,NULL,NULL,'1'),('1125272783768489985',NULL,'date_day','当月月初（1号）','cur_month_begin',4,'当月月初（1号）',1,0,NULL,NULL,NULL,'1'),('1125309944563732482',NULL,'date_day','前天','tdb_yesterday',3,'前天',1,0,NULL,NULL,NULL,'1'),('1125310225070395393',NULL,'date_day','上月月初（1号）','last_month_begin',6,'上月月初（1号）',1,0,NULL,NULL,NULL,'1'),('1125310319136051201',NULL,'date_day','上月月底','last_month_end',7,'上月月底',1,0,NULL,NULL,NULL,'1'),('1127884914435559425','筛选列','screen_list',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1'),('1127885009725952002',NULL,'screen_list','2015年初','2015-01-01',1,'',1,0,NULL,NULL,NULL,'1'),('1127885113799217153',NULL,'screen_list','2015年末','2015-12-31',2,'',1,0,NULL,NULL,NULL,'1'),('1127885183202365442',NULL,'screen_list','2016年初','2016-01-01',3,'',1,0,NULL,NULL,NULL,'1'),('1127885250856488962',NULL,'screen_list','2016年末','2016-12-31',4,'',1,0,NULL,NULL,NULL,'1'),('1141943893532930049','输入类型','input_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943893629399042',NULL,'input_type','日期区间','8',8,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943893717479426','集成日志类型','integration_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943893805559809',NULL,'integration_type','功能开发','1',1,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943893902028802',NULL,'integration_type','修复缺陷','2',2,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943893990109186','系统日志类型','sys_log_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894099161089',NULL,'sys_log_type','新增','1',1,'新增操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894187241473',NULL,'sys_log_type','删除','2',2,'删除操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894283710465',NULL,'sys_log_type','编辑','3',3,'编辑操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894375985153',NULL,'sys_log_type','查询','4',4,'查询操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894459871233',NULL,'sys_log_type','登录','5',5,'登录操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894556340225',NULL,'sys_log_type','注销','6',6,'注销操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894644420609',NULL,'sys_log_type','查看预览','7',7,'查看预览报表、文件等',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894736695297',NULL,'sys_log_type','上传','8',8,'上传操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894824775681',NULL,'sys_log_type','下载','9',9,'下载操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943894917050370',NULL,'sys_log_type','收藏','10',10,'收藏操作',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895005130753','报告记录','record_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895105794049',NULL,'record_type','仅记录异常','1',1,'',0,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895193874434',NULL,'record_type','异常记录+最近一次正常记录','2',2,'',0,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895294537729',NULL,'case_reason','test','123',1,'testaaa',0,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895374229506','日期默认值-日','date_day',NULL,NULL,NULL,'日期默认值-日',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895474892802',NULL,'date_day','当日','cur_day',1,'当日',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895562973186',NULL,'date_day','昨日','last_day',2,'昨日',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895663636482',NULL,'date_day','当月月初（1号）','cur_month_begin',4,'当月月初（1号）',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895760105474',NULL,'date_day','前天','tdb_yesterday',3,'前天',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895852380162',NULL,'date_day','上月月初（1号）','last_month_begin',6,'上月月初（1号）',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943895948849154',NULL,'date_day','上月月底','last_month_end',7,'上月月底',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896036929537','筛选列','screen_list',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896137592834',NULL,'screen_list','2015年初','2015-01-01',1,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896242450433',NULL,'screen_list','2015年末','2015-12-31',2,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896334725122',NULL,'screen_list','2016年初','2016-01-01',3,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896414416897',NULL,'screen_list','2016年末','2016-12-31',4,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896523468802',NULL,'input_type','文本','1',1,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896611549186',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896708018177',NULL,'input_type','数字','2',2,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896800292865',NULL,'input_type','日期','3',3,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896900956162','产品类型','case_product',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943896989036545',NULL,'case_product','mstr','1',1,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897072922626',NULL,'case_product','portal','2',2,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897161003010',NULL,'input_type','选择列表','4',4,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897257472001',NULL,'case_product','ETL','3',3,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897349746689','case原因','case_reason',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897446215682',NULL,'case_reason','培训不到位','1',1,'',0,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897538490370',NULL,'case_reason','沟通不到位','2',2,'',0,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897626570753','菜单类型','resource_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897718845442',NULL,'resource_type','功能菜单','1',1,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897806925825',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897899200514',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943897999863810',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898100527106',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898192801793',NULL,'input_type','单选按钮','5',5,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898285076481','公告推荐等级','notice_level',NULL,'notice_level',NULL,NULL,1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898381545473',NULL,'notice_level','紧急','1',1,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898473820161',NULL,'notice_level','加急','2',2,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898570289153',NULL,'notice_level','重大','3',3,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898666758145',NULL,'notice_level','重要','4',4,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898767421441',NULL,'notice_level','普通','5',5,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898855501825',NULL,'input_type','复选框','6',6,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1141943898956165122',NULL,'input_type','数字区间','7',7,'',1,0,NULL,NULL,NULL,'1141943878437629954'),('1143071077702103041','輸入類型','input_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071077760823298',NULL,'input_type','日期區間','8',8,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071077811154946','集成日誌類型','integration_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071077853097985',NULL,'integration_type','功能開發','1',1,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071077899235330',NULL,'integration_type','修復缺陷','2',2,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071077941178370','系統日誌類型','sys_log_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071077991510017',NULL,'sys_log_type','新增','1',1,'新增操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078033453057',NULL,'sys_log_type','刪除','2',2,'刪除操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078071201794',NULL,'sys_log_type','編輯','3',3,'編輯操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078113144834',NULL,'sys_log_type','查詢','4',4,'查詢操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078163476481',NULL,'sys_log_type','登錄','5',5,'登錄操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078205419522',NULL,'sys_log_type','註銷','6',6,'註銷操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078243168257',NULL,'sys_log_type','查看預覽','7',7,'查看預覽報表、文件等',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078301888514',NULL,'sys_log_type','上傳','8',8,'上傳操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078343831554',NULL,'sys_log_type','下載','9',9,'下載操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078398357506',NULL,'sys_log_type','收藏','10',10,'收藏操作',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078440300545','報告記錄','record_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078482243586',NULL,'record_type','僅記錄異常','1',1,'',0,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078524186626',NULL,'record_type','異常記錄+最近壹次正常記錄','2',2,'',0,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078595489793',NULL,'case_reason','test','123',1,'testaaa',0,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078645821441','日期默認值-日','date_day',NULL,NULL,NULL,'日期默認值-日',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078704541698',NULL,'date_day','當日','cur_day',1,'當日',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078742290434',NULL,'date_day','昨日','last_day',2,'昨日',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078792622081',NULL,'date_day','當月月初（1號）','cur_month_begin',4,'當月月初（1號）',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078834565121',NULL,'date_day','前天','tdb_yesterday',3,'前天',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078876508162',NULL,'date_day','上月月初（1號）','last_month_begin',6,'上月月初（1號）',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078922645505',NULL,'date_day','上月月底','last_month_end',7,'上月月底',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071078981365762','篩選列','screen_list',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079023308801',NULL,'screen_list','2015年初','2015-01-01',1,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079073640449',NULL,'screen_list','2015年末','2015-12-31',2,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079119777794',NULL,'screen_list','2016年初','2016-01-01',3,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079170109442',NULL,'screen_list','2016年末','2016-12-31',4,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079220441089',NULL,'input_type','文本','1',1,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079279161346',NULL,'resource_type','BO報表','5',5,NULL,1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079329492994',NULL,'input_type','數字','2',2,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079371436033',NULL,'input_type','日期','3',3,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079413379074','產品類型','case_product',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079459516417',NULL,'case_product','mstr','1',1,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079501459458',NULL,'case_product','portal','2',2,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079543402497',NULL,'input_type','選擇列表','4',4,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079585345538',NULL,'case_product','ETL','3',3,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079623094273','case原因','case_reason',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079665037313',NULL,'case_reason','培訓不到位','1',1,'',0,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079706980353',NULL,'case_reason','溝通不到位','2',2,'',0,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079748923394','菜單類型','resource_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079795060737',NULL,'resource_type','功能菜單','1',1,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079832809474',NULL,'resource_type','MSTR報表','2',2,NULL,1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079883141122',NULL,'resource_type','Tableau報表','3',3,NULL,1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079925084161',NULL,'resource_type','帆軟報表','4',4,NULL,1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071079971221506',NULL,'resource_type','外部鏈接','6',6,NULL,1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080013164546',NULL,'input_type','單選按鈕','5',5,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080063496194','公告推薦等級','notice_level',NULL,'notice_level',NULL,NULL,1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080105439234',NULL,'notice_level','緊急','1',1,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080151576578',NULL,'notice_level','加急','2',2,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080193519617',NULL,'notice_level','重大','3',3,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080243851266',NULL,'notice_level','重要','4',4,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080294182913',NULL,'notice_level','普通','5',5,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080331931650',NULL,'input_type','復選框','6',6,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1143071080373874690',NULL,'input_type','數字區間','7',7,'',1,0,NULL,NULL,NULL,'1143071070919913473'),('1144153368184549378','输入类型','input_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368264241154',NULL,'input_type','日期区间','8',8,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368348127233','集成日志类型','integration_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368423624706',NULL,'integration_type','功能开发','1',1,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368499122177',NULL,'integration_type','修复缺陷','2',2,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368570425345','系统日志类型','sys_log_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368662700033',NULL,'sys_log_type','新增','1',1,'新增操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368742391809',NULL,'sys_log_type','删除','2',2,'删除操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368826277889',NULL,'sys_log_type','编辑','3',3,'编辑操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368897581057',NULL,'sys_log_type','查询','4',4,'查询操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153368989855745',NULL,'sys_log_type','登录','5',5,'登录操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369086324738',NULL,'sys_log_type','注销','6',6,'注销操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369161822209',NULL,'sys_log_type','查看预览','7',7,'查看预览报表、文件等',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369237319681',NULL,'sys_log_type','上传','8',8,'上传操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369308622849',NULL,'sys_log_type','下载','9',9,'下载操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369379926017',NULL,'sys_log_type','收藏','10',10,'收藏操作',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369459617794','报告记录','record_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369526726657',NULL,'record_type','仅记录异常','1',1,'',0,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369614807042',NULL,'record_type','异常记录+最近一次正常记录','2',2,'',0,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369690304514',NULL,'case_reason','test','123',1,'testaaa',0,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369765801985','日期默认值-日','date_day',NULL,NULL,NULL,'日期默认值-日',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369837105154',NULL,'date_day','当日','cur_day',1,'当日',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369920991234',NULL,'date_day','昨日','last_day',2,'昨日',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153369992294401',NULL,'date_day','当月月初（1号）','cur_month_begin',4,'当月月初（1号）',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370059403265',NULL,'date_day','前天','tdb_yesterday',3,'前天',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370130706434',NULL,'date_day','上月月初（1号）','last_month_begin',6,'上月月初（1号）',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370231369729',NULL,'date_day','上月月底','last_month_end',7,'上月月底',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370302672897','筛选列','screen_list',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370386558977',NULL,'screen_list','2015年初','2015-01-01',1,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370462056450',NULL,'screen_list','2015年末','2015-12-31',2,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370533359618',NULL,'screen_list','2016年初','2016-01-01',3,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370608857090',NULL,'screen_list','2016年末','2016-12-31',4,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370680160257',NULL,'input_type','文本','1',1,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370755657730',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370826960897',NULL,'input_type','数字','2',2,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153370902458369',NULL,'input_type','日期','3',3,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371024093186','产品类型','case_product',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371107979265',NULL,'case_product','mstr','1',1,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371179282434',NULL,'case_product','portal','2',2,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371254779906',NULL,'input_type','选择列表','4',4,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371342860290',NULL,'case_product','ETL','3',3,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371422552065','case原因','case_reason',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371498049538',NULL,'case_reason','培训不到位','1',1,'',0,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371569352705',NULL,'case_reason','沟通不到位','2',2,'',0,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371653238785','菜单类型','resource_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371724541953',NULL,'resource_type','功能菜单','1',1,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371800039426',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371879731202',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153371972005889',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372047503362',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372127195137',NULL,'input_type','单选按钮','5',5,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372265607169','公告推荐等级','notice_level',NULL,'notice_level',NULL,NULL,1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372391436290',NULL,'notice_level','紧急','1',1,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372462739457',NULL,'notice_level','加急','2',2,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372546625538',NULL,'notice_level','重大','3',3,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372643094530',NULL,'notice_level','重要','4',4,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372726980609',NULL,'notice_level','普通','5',5,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372831838210',NULL,'input_type','复选框','6',6,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1144153372915724289',NULL,'input_type','数字区间','7',7,'',1,0,NULL,NULL,NULL,'1144153355832324098'),('1171362296965468161','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297057742849',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297129046018',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297208737794',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297284235266',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297376509954',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297456201730','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297527504897',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297607196674',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297699471361',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297783357442',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297863049218',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362297934352386',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298014044161','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298093735937',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298173427713',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298261508097',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298328616961','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298412503042',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298492194818','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298580275201',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298664161282',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298739658754',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298823544833','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298907430913',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362298995511298',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299087785986',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299171672065',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299301695489',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299406553090',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299503022081','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299582713857',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299654017026',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299742097409',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299825983489',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299905675266','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362299985367042',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300052475905',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300471906306','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300564180993',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300652261377',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300744536066',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300820033537',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300899725314',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362300979417090',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301092663297',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301193326593',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301281406978',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301394653186',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301478539266','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301562425345',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301642117122',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301726003202','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301801500673',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301876998145',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362301956689922',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362302048964609',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362302128656386',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362302212542465',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362302397091841',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362302489366530',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1171362276082028545'),('1171362785157287938','公告推薦等級','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785236979713',NULL,'notice_level','緊急','1',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785316671489',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785400557570',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785480249346',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785572524034',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785660604418','菜單類型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785748684802',NULL,'resource_type','功能菜單','1',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785832570882',NULL,'resource_type','MSTR報表','2',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362785916456962',NULL,'resource_type','Tableau報表','3',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786000343042',NULL,'resource_type','帆軟報表','4',4,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786075840514',NULL,'resource_type','BO報表','5',5,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786172309506',NULL,'resource_type','外部鏈接','6',6,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786252001281','產品類型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786335887362',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786419773441',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786503659521',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786579156994','郵件伺服器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786746929154',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786835009537','日期默認值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362786923089922',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787011170305',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787116027905',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787208302593','日期默認值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787287994369',NULL,'date_day','當日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787371880450',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787464155138',NULL,'date_day','當月月初（1號）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787640315906',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787724201985',NULL,'date_day','上月月初（1號）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362787833253890',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788042969090','日期默認值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788110077954',NULL,'date_month','當月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788193964033',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788294627330',NULL,'date_month','當月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788378513409',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788458205185','報告記錄','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788546285569',NULL,'record_type','僅記錄異常','1',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362788617588738',NULL,'record_type','異常記錄+最近一次正常記錄','2',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362789091545089','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362789188014082',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362789297065986',NULL,'sys_log_type','刪除','2',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362789376757761',NULL,'sys_log_type','編輯','3',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362789490003969',NULL,'sys_log_type','查詢','4',4,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362789611638785',NULL,'sys_log_type','登錄','5',5,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362789699719169',NULL,'sys_log_type','註銷','6',6,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790224007169',NULL,'sys_log_type','查看預覽','7',7,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790312087554',NULL,'sys_log_type','上傳','8',8,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790387585026',NULL,'sys_log_type','下載','9',9,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790463082498',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790542774273','集成日誌類型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790643437570',NULL,'integration_type','功能開發','1',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790723129346',NULL,'integration_type','修復缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790802821121','輸入類型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790874124289',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362790958010370',NULL,'input_type','數字','2',2,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362791029313537',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362791109005314',NULL,'input_type','選擇列表','4',4,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362791201280001',NULL,'input_type','單選按鈕','5',5,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362791301943298',NULL,'input_type','複選框','6',6,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362791423578113',NULL,'input_type','數字區間','7',7,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1171362791499075585',NULL,'input_type','日期區間','8',8,NULL,1,0,NULL,NULL,NULL,'1171362766052233218'),('1182466804999086081','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805070389249',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805137498114',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805208801282',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805275910145',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805343019009',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805410127873','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805481431042',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805540151297',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805611454465',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805674369025',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805741477889',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805808586754',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805875695617','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466805946998785',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806005719041',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806068633601',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806135742465','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806202851330',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806261571585','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806337069057',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806404177922',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806479675393',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806555172865','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806622281729',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806689390594',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806756499458',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806819414018',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806890717185',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466806949437441',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807020740609','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807092043778',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807159152642',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807226261506',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807293370369',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807360479234','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807419199489',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807486308354',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807863795714','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807926710273',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466807998013441',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808060928002',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808128036865',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808195145730',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808262254593',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808325169153',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808396472322',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808467775489',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808539078658',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808601993218','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808673296385',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808752988162',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808820097026','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808895594498',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466808962703361',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466809038200834',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466809109504002',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466809176612866',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466809247916034',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466809319219202',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182466809386328066',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1182466788184121346'),('1182471140558856194','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140592410625',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140630159362',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140663713793',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140697268226',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140730822657',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140768571393','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140797931521',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140831485954',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140865040386',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140898594818',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140932149249',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140965703681',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471140995063809','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141032812546',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141066366978',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141104115713',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141137670146','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141167030273',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141200584706','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141234139138',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141267693569',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141297053697',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141330608130','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141364162561',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141401911298',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141435465729',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141473214465',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141502574594',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141536129025',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141569683457','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141603237890',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141636792321',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141666152450',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141699706881',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141733261314','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141766815745',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141796175874',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141955559426','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471141993308162',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142022668290',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142060417026',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142089777153',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142127525889',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142156886017',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142190440449',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142223994881',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142257549314',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142291103745',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142320463873','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142354018305',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142391767042',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142416932866','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142450487297',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142488236034',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142517596162',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142551150593',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142580510721',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142618259457',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142651813890',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182471142685368321',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1182471133218824193'),('1182496973310730241','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973344284674',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973386227713',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973423976450',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973457530882',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973503668225',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973533028353','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973566582786',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973604331521',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973633691649',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973671440385',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973704994817',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973738549249',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973776297985','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973809852418',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973839212545',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973872766977',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973910515714','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973944070146',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1182496965047951361'),('1182496973981818882','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974015373313',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974053122050',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974086676481',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974124425217','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974157979649',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974187339777',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974220894210',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974262837250',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974296391681',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974329946114',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974363500546','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974392860673',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974430609409',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974464163841',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974501912577',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974535467009','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974569021441',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974602575874',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974833262594','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974871011329',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974908760066',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974942314498',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496974980063234',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975013617665',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975047172098',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975080726529',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975110086657',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975143641090',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975177195521',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975214944258','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975248498690',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975282053121',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975315607554','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975353356289',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975386910722',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975420465154',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975449825281',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975491768322',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975521128450',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975554682881',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1182496975588237314',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1182496965047951361'),('1183562363436531714','商品','shangping',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183564403281756161',NULL,'shangping','全部',' ',1,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183564483040641025',NULL,'shangping','苹果汁','苹果汁',2,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183564533426814977',NULL,'shangping','牛奶','牛奶',3,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183564593199841282',NULL,'shangping','番茄酱','番茄酱',4,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183564677081726978',NULL,'shangping','盐','盐',5,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183566653911728129','区域','区域',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183566730734600194',NULL,'区域','华北','华北',1,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183566791350681601',NULL,'区域','华东','华东',2,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183566857310306305',NULL,'区域','东北','东北',3,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183566919763492865',NULL,'区域','华中','华中',4,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183567060046184449',NULL,'区域','华南','华南',5,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183567108708499458',NULL,'区域','西南','西南',6,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183567151612035074',NULL,'区域','西北','西北',1,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572093475225601','城市','chengshi',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572263696859138',NULL,'chengshi','全选','  ',1,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572346916044801',NULL,'chengshi','北京','北京',2,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572422157664258',NULL,'chengshi','秦皇岛','秦皇岛',3,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572484715708418',NULL,'chengshi','长治','长治',4,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572538667040770',NULL,'chengshi','张家口','张家口',5,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572621932363778',NULL,'chengshi','天津','天津',6,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183572682745577473',NULL,'chengshi','石家庄','石家庄',7,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575070764171265','产品类别','产品类别',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575130054852610',NULL,'产品类别','饮料','饮料',1,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575198220681218',NULL,'产品类别','调味品','调味品',2,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575277266534402',NULL,'产品类别','点心','点心',3,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575335907098626',NULL,'产品类别','日用品','日用品',4,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575468388384769',NULL,'产品类别','谷类/麦片','谷类/麦片',5,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575597736525825',NULL,'产品类别','肉/家禽','肉/家禽',6,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575682813788162',NULL,'产品类别','特制品','特制品',7,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575746932113410',NULL,'产品类别','海鲜','海鲜',8,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575853731676161','销售区域','销售区域',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575911373996034',NULL,'销售区域','华北','华北',1,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183575961017778178',NULL,'销售区域','华东','华东',2,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183576027468136449',NULL,'销售区域','东北','东北',3,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183576087853531137',NULL,'销售区域','华中','华中',4,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183576154350026753',NULL,'销售区域','华南','华南',5,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183576215033217025',NULL,'销售区域','西南','西南',6,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183576334793179138',NULL,'销售区域','西北','西北',7,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183916397796261890','产品名称参数','产品名称参数',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183916453320458241',NULL,'产品名称参数','全部',' ',1,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183916968993357826',NULL,'产品名称参数','苹果汁','苹果汁',2,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183917025113145346',NULL,'产品名称参数','牛奶','牛奶',3,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183917125315067906',NULL,'产品名称参数','番茄酱','番茄酱',4,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183917200661544961',NULL,'产品名称参数','盐','盐',5,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183918227167117313','销售时间','销售时间',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183918274910879746',NULL,'销售时间','2016','2016',1,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183918317545979906',NULL,'销售时间','2017','2017',2,'',0,0,NULL,NULL,NULL,'1182466788184121346'),('1183919444446412802','销售时间','销售时间',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183919496468365313',NULL,'销售时间','2016','2016',1,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('1183919536473636866',NULL,'销售时间','2017','2017',2,'',0,0,NULL,NULL,NULL,'1182471133218824193'),('12','case类型','case_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('1200033042633691138','test','001',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1171362766052233218'),('1230044113884680193','Announcement recommendation level','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044113972760578',NULL,'notice_level','Urgent','1',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114018897922',NULL,'notice_level','Pressing','2',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114056646657',NULL,'notice_level','Weighty','3',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114102784002',NULL,'notice_level','Important','4',4,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114153115650',NULL,'notice_level','Ordinary','5',5,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114190864385','Menu type','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114237001730',NULL,'resource_type','Functional menu','1',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114274750465',NULL,'resource_type','MSTR Report','2',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114316693505',NULL,'resource_type','Tableau Report','3',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114354442241',NULL,'resource_type','FineReport','4',4,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114392190977',NULL,'resource_type','BO Report','5',5,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114429939713',NULL,'resource_type','External links','6',6,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114480271361','Product type','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114731929601',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114769678338',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114815815682',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114861953025','Mail server','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114912284674',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114950033410','Date default value-Year','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044114991976449',NULL,'date_year','other','other',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115033919489',NULL,'date_year','This year','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115071668225',NULL,'date_year','Last year','last_year',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115105222657','Date default value-Day','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115147165697',NULL,'date_day','Today','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115184914433',NULL,'date_day','Yesterday','last_day',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115222663169',NULL,'date_day','at the beginning of the month（No.1）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115256217602',NULL,'date_day','The day before yesterday','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115293966338',NULL,'date_day','Early last month（No.1）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115340103681',NULL,'date_day','Late last month','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115369463809','Date default value-Month','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115411406849',NULL,'date_month','This month','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115449155586',NULL,'date_month','Last month','last_month',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115495292930',NULL,'date_month','The same period of the month','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115549818882',NULL,'date_month','other','other',4,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115595956225','Reporting records','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115633704962',NULL,'record_type','Record only exceptions','1',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115671453697',NULL,'record_type','Abnormal record + Last normal record','2',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115864391682','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115918917633',NULL,'sys_log_type','Add','1',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044115960860674',NULL,'sys_log_type','Delete','2',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116002803713',NULL,'sys_log_type','Edit','3',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116044746754',NULL,'sys_log_type','Query','4',4,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116082495489',NULL,'sys_log_type','Log on','5',5,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116116049922',NULL,'sys_log_type','Logout','6',6,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116157992962',NULL,'sys_log_type','View preview','7',7,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116204130306',NULL,'sys_log_type','upload','8',8,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116250267650',NULL,'sys_log_type','download','9',9,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116292210690',NULL,'sys_log_type','collection','10',10,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116325765122','Integrated log type','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116359319554',NULL,'integration_type','Function Development','1',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116401262593',NULL,'integration_type','Repair defect','2',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116443205634','Input type','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116489342978',NULL,'input_type','Text','1',1,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116539674625',NULL,'input_type','Number','2',2,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116585811970',NULL,'input_type','Date','3',3,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116619366401',NULL,'input_type','Selection list','4',4,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116652920834',NULL,'input_type','Radio button','5',5,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116703252482',NULL,'input_type','check box','6',6,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116749389826',NULL,'input_type','Number interval','7',7,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230044116803915778',NULL,'input_type','Date interval','8',8,NULL,1,0,NULL,NULL,NULL,'1230044093810741250'),('1230046920117325826','Announcement recommendation level','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920163463169',NULL,'notice_level','Urgent','1',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920205406210',NULL,'notice_level','Pressing','2',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920247349249',NULL,'notice_level','Weighty','3',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920289292289',NULL,'notice_level','Important','4',4,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920335429633',NULL,'notice_level','Ordinary','5',5,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920381566978','Menu type','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920436092930',NULL,'resource_type','Functional menu','1',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920473841665',NULL,'resource_type','MSTR Report','2',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920507396097',NULL,'resource_type','Tableau Report','3',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920540950529',NULL,'resource_type','FineReport','4',4,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920582893570',NULL,'resource_type','BO Report','5',5,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920629030913',NULL,'resource_type','External links','6',6,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920675168258','Product type','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920708722689',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920759054337',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920796803074',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920847134722','Mail server','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920880689153',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920926826497','Date default value-Year','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046920972963841',NULL,'date_year','other','other',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921002323970',NULL,'date_year','This year','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921048461313',NULL,'date_year','Last year','last_year',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921098792962','Date default value-Day','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921144930305',NULL,'date_day','Today','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921195261954',NULL,'date_day','Yesterday','last_day',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921249787906',NULL,'date_day','at the beginning of the month（No.1）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921300119553',NULL,'date_day','The day before yesterday','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921342062593',NULL,'date_day','Early last month（No.1）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921379811329',NULL,'date_day','Late last month','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921421754369','Date default value-Month','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921463697409',NULL,'date_month','This month','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921509834754',NULL,'date_month','Last month','last_month',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921560166402',NULL,'date_month','The same period of the month','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921614692354',NULL,'date_month','other','other',4,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921665024002','Reporting records','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921702772738',NULL,'record_type','Record only exceptions','1',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921740521473',NULL,'record_type','Abnormal record + Last normal record','2',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921908293634','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921950236674',NULL,'sys_log_type','Add','1',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046921987985409',NULL,'sys_log_type','Delete','2',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922025734145',NULL,'sys_log_type','Edit','3',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922067677185',NULL,'sys_log_type','Query','4',4,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922113814529',NULL,'sys_log_type','Log on','5',5,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922151563265',NULL,'sys_log_type','Logout','6',6,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922193506306',NULL,'sys_log_type','View preview','7',7,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922231255042',NULL,'sys_log_type','upload','8',8,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922273198081',NULL,'sys_log_type','download','9',9,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922306752514',NULL,'sys_log_type','collection','10',10,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922352889857','Integrated log type','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922399027201',NULL,'integration_type','Function Development','1',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922445164545',NULL,'integration_type','Repair defect','2',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922478718978','Input type','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922508079106',NULL,'input_type','Text','1',1,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922554216450',NULL,'input_type','Number','2',2,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922587770881',NULL,'input_type','Date','3',3,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922621325314',NULL,'input_type','Selection list','4',4,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922654879746',NULL,'input_type','Radio button','5',5,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922688434177',NULL,'input_type','check box','6',6,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922730377217',NULL,'input_type','Number interval','7',7,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230046922768125953',NULL,'input_type','Date interval','8',8,NULL,1,0,NULL,NULL,NULL,'1230046910524952578'),('1230047410293051394','公告推薦等級','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410330800130',NULL,'notice_level','緊急','1',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410368548865',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410406297602',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410444046337',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410477600769',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410515349505','菜單類型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410561486850',NULL,'resource_type','功能菜單','1',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410590846978',NULL,'resource_type','MSTR報表','2',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410636984321',NULL,'resource_type','Tableau報表','3',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410687315970',NULL,'resource_type','帆軟報表','4',4,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410733453313',NULL,'resource_type','BO報表','5',5,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410767007745',NULL,'resource_type','外部鏈接','6',6,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410804756481','產品類型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410838310913',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410876059649',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410918002689',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410955751426','郵件伺服器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1230047401363378178'),('1230047410993500161',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411027054594','日期默認值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411060609025',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411094163457',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411136106497',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411169660929','日期默認值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411224186881',NULL,'date_day','當日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411274518530',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411320655873',NULL,'date_day','當月月初（1號）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411366793217',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411412930562',NULL,'date_day','上月月初（1號）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411467456513',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411513593857','日期默認值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411559731202',NULL,'date_month','當月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411601674241',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411639422978',NULL,'date_month','當月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411677171713',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411727503361','報告記錄','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411769446402',NULL,'record_type','僅記錄異常','1',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047411807195137',NULL,'record_type','異常記錄+最近一次正常記錄','2',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412000133122','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412050464770',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412084019201',NULL,'sys_log_type','刪除','2',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412117573634',NULL,'sys_log_type','編輯','3',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412159516673',NULL,'sys_log_type','查詢','4',4,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412209848322',NULL,'sys_log_type','登錄','5',5,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412247597058',NULL,'sys_log_type','註銷','6',6,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412293734402',NULL,'sys_log_type','查看預覽','7',7,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412335677442',NULL,'sys_log_type','上傳','8',8,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412386009089',NULL,'sys_log_type','下載','9',9,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412427952129',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412465700865','集成日誌類型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412503449602',NULL,'integration_type','功能開發','1',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412545392641',NULL,'integration_type','修復缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412595724290','輸入類型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412646055937',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412687998978',NULL,'input_type','數字','2',2,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412734136322',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412784467969',NULL,'input_type','選擇列表','4',4,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412830605314',NULL,'input_type','單選按鈕','5',5,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412868354049',NULL,'input_type','複選框','6',6,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412910297089',NULL,'input_type','數字區間','7',7,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1230047412952240130',NULL,'input_type','日期區間','8',8,NULL,1,0,NULL,NULL,NULL,'1230047401363378178'),('1232548894368862209','公告推薦等級','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894410805249',NULL,'notice_level','緊急','1',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894448553986',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894486302722',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894540828674',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894574383105',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894612131841','菜單類型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894645686274',NULL,'resource_type','功能菜單','1',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894679240705',NULL,'resource_type','MSTR報表','2',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894712795137',NULL,'resource_type','Tableau報表','3',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894750543874',NULL,'resource_type','帆軟報表','4',4,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894796681218',NULL,'resource_type','BO報表','5',5,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894834429953',NULL,'resource_type','外部鏈接','6',6,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894876372993','產品類型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548894914121729',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895216111618',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895253860354',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895295803393','郵件伺服器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895333552130',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895379689474','日期默認值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895417438210',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895459381250',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895497129985',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895543267329','日期默認值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895576821761',NULL,'date_day','當日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895698456577',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895744593921',NULL,'date_day','當月月初（1號）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895794925570',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895832674305',NULL,'date_day','上月月初（1號）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895866228738',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895899783170','日期默認值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895941726210',NULL,'date_month','當月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548895975280641',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896017223682',NULL,'date_month','當月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896071749633',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896101109761','報告記錄','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896134664194',NULL,'record_type','僅記錄異常','1',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896176607234',NULL,'record_type','異常記錄+最近一次正常記錄','2',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896331796481','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896365350913',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896403099649',NULL,'sys_log_type','刪除','2',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896436654082',NULL,'sys_log_type','編輯','3',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896474402817',NULL,'sys_log_type','查詢','4',4,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896507957249',NULL,'sys_log_type','登錄','5',5,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896537317377',NULL,'sys_log_type','註銷','6',6,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896579260418',NULL,'sys_log_type','查看預覽','7',7,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896625397762',NULL,'sys_log_type','上傳','8',8,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896663146497',NULL,'sys_log_type','下載','9',9,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896705089537',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896747032578','集成日誌類型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896793169922',NULL,'integration_type','功能開發','1',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896826724354',NULL,'integration_type','修復缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896860278785','輸入類型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896893833217',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896931581954',NULL,'input_type','數字','2',2,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896965136385',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548896998690818',NULL,'input_type','選擇列表','4',4,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548897032245249',NULL,'input_type','單選按鈕','5',5,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548897065799681',NULL,'input_type','複選框','6',6,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548897099354114',NULL,'input_type','數字區間','7',7,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232548897132908545',NULL,'input_type','日期區間','8',8,NULL,1,0,NULL,NULL,NULL,'1232548884797460481'),('1232551257053859842','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257095802881',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257129357313',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257162911745',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257200660482',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257234214913',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257267769346','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257305518082',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257343266817',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257372626946',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257414569985',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257448124418',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257498456066',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257544593410','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257578147842',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257620090881',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257662033921',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257695588354','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257729142785',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257762697218','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257792057346',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257829806082',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257863360513',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257901109250','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257934663681',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551257972412417',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258014355458',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258047909889',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258081464322',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258123407362',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258161156098','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258207293442',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258249236481',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258286985217',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258320539650',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258358288386','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258400231426',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258433785858',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258618335234','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258656083969',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258702221314',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258744164354',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258786107393',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258823856130',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258861604865',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258895159298',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258932908033',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551258979045377',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259016794114',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259046154242','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259083902977',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259121651714',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259155206145','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259188760578',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259222315010',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259255869442',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259293618177',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259327172610',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259360727042',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259398475777',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232551259427835906',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1232551248891744257'),('1232908584185503746','公告推薦等級','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584281972738',NULL,'notice_level','緊急','1',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584357470210',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584432967682',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584521048066',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584596545538',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584672043009','菜單類型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584755929089',NULL,'resource_type','功能菜單','1',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584852398082',NULL,'resource_type','MSTR報表','2',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908584927895554',NULL,'resource_type','Tableau報表','3',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585007587329',NULL,'resource_type','帆軟報表','4',4,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585087279105',NULL,'resource_type','BO報表','5',5,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585166970881',NULL,'resource_type','外部鏈接','6',6,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585246662657','產品類型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585322160129',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585389268993',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585468960769',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585536069634','郵件伺服器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585598984194',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585687064577','日期默認值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585775144962',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585867419649',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908585955500034',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586051969026','日期默認值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586148438017',NULL,'date_day','當日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586232324097',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586307821569',NULL,'date_day','當月月初（1號）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586379124737',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586442039298',NULL,'date_day','上月月初（1號）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586513342465',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586601422849','日期默認值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586681114625',NULL,'date_month','當月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586756612097',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586827915266',NULL,'date_month','當月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586899218434',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908586978910209','報告記錄','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587075379202',NULL,'record_type','僅記錄異常','1',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587146682370',NULL,'record_type','異常記錄+最近一次正常記錄','2',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587540946945','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587624833025',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587704524801',NULL,'sys_log_type','刪除','2',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587784216577',NULL,'sys_log_type','編輯','3',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587851325442',NULL,'sys_log_type','查詢','4',4,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908587926822914',NULL,'sys_log_type','登錄','5',5,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588014903297',NULL,'sys_log_type','註銷','6',6,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588086206466',NULL,'sys_log_type','查看預覽','7',7,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588161703938',NULL,'sys_log_type','上傳','8',8,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588228812801',NULL,'sys_log_type','下載','9',9,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588304310274',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588371419137','集成日誌類型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588446916609',NULL,'integration_type','功能開發','1',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588522414082',NULL,'integration_type','修復缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588593717249','輸入類型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588660826113',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588740517890',NULL,'input_type','數字','2',2,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588824403969',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588908290050',NULL,'input_type','選擇列表','4',4,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908588987981826',NULL,'input_type','單選按鈕','5',5,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908589055090689',NULL,'input_type','複選框','6',6,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908589134782466',NULL,'input_type','數字區間','7',7,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1232908589206085633',NULL,'input_type','日期區間','8',8,NULL,1,0,NULL,NULL,NULL,'1232908565093031938'),('1265476637725134849','公告推薦等級','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476637842575362',NULL,'notice_level','緊急','1',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476637960015873',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638048096258',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638131982338',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638220062722',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638291365889','菜單類型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638375251970',NULL,'resource_type','功能菜單','1',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638450749442',NULL,'resource_type','MSTR報表','2',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638530441218',NULL,'resource_type','Tableau報表','3',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638631104514',NULL,'resource_type','帆軟報表','4',4,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638719184898',NULL,'resource_type','BO報表','5',5,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638798876674',NULL,'resource_type','外部鏈接','6',6,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476638874374145','產品類型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476639490936834',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476639612571650',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476639730012162',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476639813898242','郵件伺服器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1265476599435333633'),('1265476639939727361',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640023613442','日期默認值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640120082433',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640258494466',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640375934978',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640480792578','日期默認值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640585650177',NULL,'date_day','當日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640669536257',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640761810945',NULL,'date_day','當月月初（1號）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640870862850',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476640971526146',NULL,'date_day','上月月初（1號）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641055412225',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641143492610','日期默認值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641239961602',NULL,'date_month','當月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641349013506',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641562923009',NULL,'date_month','當月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641680363522',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641785221122','報告記錄','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641869107202',NULL,'record_type','僅記錄異常','1',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476641978159106',NULL,'record_type','異常記錄+最近一次正常記錄','2',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476642544390145','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476642645053442',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476643471331329',NULL,'sys_log_type','刪除','2',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476643555217409',NULL,'sys_log_type','編輯','3',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476643685240833',NULL,'sys_log_type','查詢','4',4,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476643794292738',NULL,'sys_log_type','登錄','5',5,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476643903344641',NULL,'sys_log_type','註銷','6',6,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644012396546',NULL,'sys_log_type','查看預覽','7',7,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644142419969',NULL,'sys_log_type','上傳','8',8,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644255666178',NULL,'sys_log_type','下載','9',9,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644553461761',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644696068097','集成日誌類型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644796731393',NULL,'integration_type','功能開發','1',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644876423170',NULL,'integration_type','修復缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476644956114945','輸入類型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645040001025',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645115498498',NULL,'input_type','數字','2',2,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645249716226',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645354573825',NULL,'input_type','選擇列表','4',4,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645480402945',NULL,'input_type','單選按鈕','5',5,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645581066242',NULL,'input_type','複選框','6',6,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645681729538',NULL,'input_type','數字區間','7',7,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265476645761421313',NULL,'input_type','日期區間','8',8,NULL,1,0,NULL,NULL,NULL,'1265476599435333633'),('1265478554136186881','公告推薦等級','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478554215878657',NULL,'notice_level','緊急','1',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478554303959042',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478554404622337',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478554643697666',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478554744360962',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478554849218562','菜單類型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478554991824898',NULL,'resource_type','功能菜單','1',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555113459714',NULL,'resource_type','MSTR報表','2',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555201540097',NULL,'resource_type','Tableau報表','3',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555310592002',NULL,'resource_type','帆軟報表','4',4,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555402866690',NULL,'resource_type','BO報表','5',5,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555499335682',NULL,'resource_type','外部鏈接','6',6,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555591610369','產品類型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555662913538',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555755188226',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555847462914',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478555956514817','郵件伺服器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556065566721',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556145258497','日期默認值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556258504705',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556350779394',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556451442690',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556564688898','日期默認值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556665352193',NULL,'date_day','當日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556786987010',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556875067394',NULL,'date_day','當月月初（1號）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478556967342081',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557080588290',NULL,'date_day','上月月初（1號）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557214806018',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557311275009','日期默認值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557411938305',NULL,'date_month','當月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557520990209',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557600681986',NULL,'date_month','當月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557692956673',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557806202881','報告記錄','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478557923643394',NULL,'record_type','僅記錄異常','1',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478558145941506',NULL,'record_type','異常記錄+最近一次正常記錄','2',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478558670229505','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478558749921281',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478558838001665',NULL,'sys_log_type','刪除','2',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478558963830785',NULL,'sys_log_type','編輯','3',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559047716865',NULL,'sys_log_type','查詢','4',4,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559131602945',NULL,'sys_log_type','登錄','5',5,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559211294722',NULL,'sys_log_type','註銷','6',6,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559286792194',NULL,'sys_log_type','查看預覽','7',7,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559362289666',NULL,'sys_log_type','上傳','8',8,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559441981442',NULL,'sys_log_type','下載','9',9,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559534256129',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559647502338','集成日誌類型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559735582721',NULL,'integration_type','功能開發','1',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559819468802',NULL,'integration_type','修復缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559894966274','輸入類型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478559966269441',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478560058544130',NULL,'input_type','數字','2',2,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478560138235905',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478560226316290',NULL,'input_type','選擇列表','4',4,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478560326979585',NULL,'input_type','單選按鈕','5',5,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478560415059970',NULL,'input_type','複選框','6',6,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478560494751745',NULL,'input_type','數字區間','7',7,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265478560570249218',NULL,'input_type','日期區間','8',8,NULL,1,0,NULL,NULL,NULL,'1265478529507233794'),('1265479200218386434','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479200319049730',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479200423907330',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479200545542145',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479200696537090',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479200818171906',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479200931418113','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201023692802',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201132744706',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201283739650',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201376014338',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201464094721',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201552175105',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201636061185','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201711558658',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201778667521',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201862553601',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479201942245377','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202034520066',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202114211842','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202235846657',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202340704257',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202432978946',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202525253634','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202634305537',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202743357442',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202831437826',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479202923712513',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203057930242',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203158593538',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203267645441','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203351531521',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203443806209',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203519303682',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203603189761',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203682881537','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203775156225',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479203859042306',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479204874063874','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479205054418946',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479205142499330',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479205268328450',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479205482237953',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479205637427202',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479205855531009',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479205964582913',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479206073634818',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479206203658241',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479206304321538',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479206606311425','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479206723751937',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479207772327937',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479207847825409','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479207935905793',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479208044957698',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479208174981122',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479208271450114',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479208372113409',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479208451805185',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479208527302658',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1265479208623771650',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1265479177548173313'),('1279615493005225985','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615493147832322',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615493265272833',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615493374324738',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615493491765250',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615493600817153',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615493714063361','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615493898612737',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494037024769',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494167048194',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494284488705',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494397734913',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494527758338',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494716502017','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494838136834',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615494955577345',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495068823554',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495169486849','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495282733058',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495383396354','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495517614082',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495630860289',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495752495106',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615495874129921','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496004153346',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496121593858',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496251617282',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496377446401',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496494886914',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496612327425',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496746545154','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615496905928705',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615497035952130',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615497178558466',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615497308581890',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615497442799617','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615497614766082',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615497757372418',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615498348769281','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615498571067394',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615498709479425',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615498831114241',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615498952749058',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499061800962',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499179241473',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499279904770',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499405733889',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499510591490',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499623837698',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499766444033','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615499904856065',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500013907970',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500118765569','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500227817474',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500349452289',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500462698498',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500571750401',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500689190914',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500831797249',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615500961820674',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('1279615501066678274',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1279615460717473793'),('13','产品版本','mstr_version',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('1306074644744880129','公告推荐等级','notice_level',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074644795211777',NULL,'notice_level','紧急','1',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074644845543425',NULL,'notice_level','加急','2',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074644904263681',NULL,'notice_level','重大','3',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074644954595329',NULL,'notice_level','重要','4',4,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645004926977',NULL,'notice_level','普通','5',5,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645046870018','菜单类型','resource_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645101395969',NULL,'resource_type','功能菜单','1',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645147533313',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645202059266',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645256585217',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645306916865',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645361442818',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645415968769','产品类型','case_product',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645470494721',NULL,'case_product','mstr','1',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645529214978',NULL,'case_product','portal','2',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645571158018',NULL,'case_product','ETL','3',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645818621954','邮件服务器','MAIL_HOST',NULL,NULL,NULL,NULL,0,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645868953602',NULL,'MAIL_HOST','smtp.exmail.qq.com','smtp.exmail.qq.com',1,NULL,0,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645931868162','日期默认值-年','date_year',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074645978005506',NULL,'date_year','其他','other',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646036725762',NULL,'date_year','今年','cur_year',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646095446017',NULL,'date_year','去年','last_year',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646158360578','日期默认值-日','date_day',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646208692226',NULL,'date_day','当日','cur_day',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646275801089',NULL,'date_day','昨日','last_day',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646338715650',NULL,'date_day','当月月初（1号）','cur_month_begin',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646397435905',NULL,'date_day','前天','tdb_yesterday',4,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646443573249',NULL,'date_day','上月月初（1号）','last_month_begin',5,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646481321986',NULL,'date_day','上月月底','last_month_end',6,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646514876418','日期默认值-月','date_month',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646556819458',NULL,'date_month','当月','cur_month',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646598762498',NULL,'date_month','上月','last_month',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646636511233',NULL,'date_month','当月同期','ytb_month',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646682648578',NULL,'date_month','其他','other',4,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646732980225','报告记录','record_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646795894786',NULL,'record_type','仅记录异常','1',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074646858809345',NULL,'record_type','异常记录+最近一次正常记录','2',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647118856194','','sys_log_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647177576449',NULL,'sys_log_type','新增','1',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647248879618',NULL,'sys_log_type','删除','2',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647311794178',NULL,'sys_log_type','编辑','3',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647370514434',NULL,'sys_log_type','查询','4',4,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647425040385',NULL,'sys_log_type','登录','5',5,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647483760642',NULL,'sys_log_type','注销','6',6,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647529897985',NULL,'sys_log_type','查看预览','7',7,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647567646722',NULL,'sys_log_type','上传','8',8,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647605395458',NULL,'sys_log_type','下载','9',9,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647647338498',NULL,'sys_log_type','收藏','10',10,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647689281537','集成日志类型','integration_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647727030274',NULL,'integration_type','功能开发','1',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647768973313',NULL,'integration_type','修复缺陷','2',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647815110658','输入类型','input_type',NULL,NULL,NULL,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647857053698',NULL,'input_type','文本','1',1,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647903191042',NULL,'input_type','数字','2',2,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647940939777',NULL,'input_type','日期','3',3,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074647987077121',NULL,'input_type','选择列表','4',4,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074648029020161',NULL,'input_type','单选按钮','5',5,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074648079351810',NULL,'input_type','复选框','6',6,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074648133877761',NULL,'input_type','数字区间','7',7,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1306074648188403713',NULL,'input_type','日期区间','8',8,NULL,1,0,NULL,NULL,NULL,'1306074625434304513'),('1337345015536009217','cognos提示','ID',NULL,NULL,NULL,'',0,0,'',NULL,NULL,'1'),('1337345086453301249',NULL,'ID','北分','DEPT002',1,'',0,0,NULL,NULL,NULL,'1'),('1337345151213355009',NULL,'ID','广分','DEPT005',2,'',0,0,NULL,NULL,NULL,'1'),('1337345201872158722',NULL,'ID','陕西','DEPT010',3,'',0,0,NULL,NULL,NULL,'1'),('1356139604904357889',NULL,'case_reason','测试','3',1,'测测测',0,0,NULL,NULL,'','1'),('14',NULL,'case_evt','生产','1',1,'',0,0,NULL,NULL,NULL,NULL),('15',NULL,'case_evt','准生产','2',2,'',0,0,NULL,NULL,NULL,NULL),('16',NULL,'case_evt','demo','3',3,'',0,0,NULL,NULL,NULL,NULL),('17',NULL,'case_level','P1','P1',1,'',0,0,NULL,NULL,NULL,NULL),('18',NULL,'case_level','P2','P2',1,'',0,0,NULL,NULL,NULL,NULL),('19',NULL,'case_level','P3','P3',3,'',0,0,NULL,NULL,NULL,NULL),('2',NULL,'input_type','文本','1',1,'',1,0,NULL,NULL,NULL,'1'),('20',NULL,'case_level','P4','P4',4,'',0,0,NULL,NULL,NULL,NULL),('21',NULL,'case_type','WEB','WEB',1,'',0,0,NULL,NULL,NULL,NULL),('2132312312',NULL,'resource_type','BO报表','5',5,NULL,1,0,NULL,NULL,NULL,'1'),('22',NULL,'case_type','VI','VI',2,'',0,0,NULL,NULL,NULL,NULL),('23',NULL,'mstr_version','10.10','10.10',1,'',0,0,NULL,NULL,NULL,NULL),('24',NULL,'mstr_version','10.9','10.9',2,'',0,0,NULL,NULL,NULL,NULL),('25',NULL,'mstr_version','10.8','10.8',3,'',0,0,NULL,NULL,NULL,NULL),('26','case状态','case_state',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('27',NULL,'case_state','处理中','1',1,'',0,0,NULL,NULL,NULL,NULL),('28',NULL,'case_state','原厂处理中','2',2,'',0,0,NULL,NULL,NULL,NULL),('29',NULL,'case_state','已关闭','99',3,'',0,0,NULL,NULL,NULL,NULL),('3',NULL,'input_type','数字','2',2,'',1,0,NULL,NULL,NULL,'1'),('30','快捷回复','quick_answer',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('31',NULL,'quick_answer','我们正在处理您的问题。稍后会给您回复。','我们正在处理您的问题。稍后会给您回复。',1,'',0,0,NULL,NULL,NULL,NULL),('32','case分派人','default_transfer',NULL,NULL,NULL,'1',0,0,NULL,NULL,NULL,NULL),('33','调度任务类型','job_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('34',NULL,'job_type','case邮件发送任务','com.xin.support.job.MailSendJob',1,'',0,0,NULL,NULL,NULL,NULL),('35','任务执行频率','job_cron',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('36',NULL,'job_cron','每天0点','0 0 0 * * ? *',1,'',0,0,NULL,NULL,NULL,NULL),('37',NULL,'job_type','Demo','com.xin.portal.system.job.DemoJob',1,'',0,0,NULL,NULL,NULL,NULL),('38','项目类型','project_type',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('39',NULL,'project_type','维保项目','1',1,'',0,0,NULL,NULL,NULL,NULL),('4',NULL,'input_type','日期','3',3,'',1,0,NULL,NULL,NULL,'1'),('40',NULL,'project_type','开发项目','2',2,'',0,0,NULL,NULL,NULL,NULL),('41','项目阶段','project_stage',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('42',NULL,'project_stage','需求调研','1',1,'',0,0,NULL,NULL,NULL,NULL),('43',NULL,'project_stage','已立项','2',1,'',0,0,NULL,NULL,NULL,NULL),('44','项目状态','project_state',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,NULL),('45',NULL,'project_state','正常','1',1,'',0,0,NULL,NULL,NULL,NULL),('46',NULL,'project_state','延期','2',2,'',0,0,NULL,NULL,NULL,NULL),('47','产品类型','case_product',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1'),('48',NULL,'case_product','mstr','1',1,'',1,0,NULL,NULL,NULL,'1'),('49',NULL,'case_product','portal','2',2,'',1,0,NULL,NULL,NULL,'1'),('5',NULL,'input_type','选择列表','4',4,'',1,0,NULL,NULL,NULL,'1'),('50',NULL,'case_product','ETL','3',3,'',1,0,NULL,NULL,NULL,'1'),('51','case原因','case_reason',NULL,NULL,NULL,'',0,0,NULL,NULL,NULL,'1'),('52',NULL,'case_reason','培训不到位','1',1,'',0,0,NULL,NULL,NULL,'1'),('53',NULL,'case_reason','沟通不到位','2',2,'',0,0,NULL,NULL,NULL,'1'),('54','菜单类型','resource_type',NULL,NULL,NULL,'',1,0,NULL,NULL,NULL,'1'),('55',NULL,'resource_type','功能菜单','1',1,'',1,0,NULL,NULL,NULL,'1'),('56',NULL,'resource_type','MSTR报表','2',2,NULL,1,0,NULL,NULL,NULL,'1'),('57',NULL,'resource_type','Tableau报表','3',3,NULL,1,0,NULL,NULL,NULL,'1'),('58',NULL,'resource_type','帆软报表','4',4,NULL,1,0,NULL,NULL,NULL,'1'),('59',NULL,'resource_type','外部链接','6',6,NULL,1,0,NULL,NULL,NULL,'1'),('6',NULL,'input_type','单选按钮','5',5,'',1,0,NULL,NULL,NULL,'1'),('60','公告推荐等级','notice_level',NULL,'notice_level',NULL,NULL,1,0,NULL,NULL,NULL,'1'),('61',NULL,'notice_level','紧急','1',1,'',1,0,NULL,NULL,NULL,'1'),('62',NULL,'notice_level','加急','2',2,'',1,0,NULL,NULL,NULL,'1'),('63',NULL,'notice_level','重大','3',3,'',1,0,NULL,NULL,NULL,'1'),('64',NULL,'notice_level','重要','4',4,'',1,0,NULL,NULL,NULL,'1'),('65',NULL,'notice_level','普通','5',5,'',1,0,NULL,NULL,NULL,'1'),('7',NULL,'input_type','复选框','6',6,'',1,0,NULL,NULL,NULL,'1'),('8',NULL,'input_type','数字区间','7',7,'',1,0,NULL,NULL,NULL,'1');

UNLOCK TABLES;

/*Table structure for table `t_file` */

DROP TABLE IF EXISTS `t_file`;

CREATE TABLE `t_file` (
  `id` varchar(32) NOT NULL,
  `name_before` varchar(255) DEFAULT NULL COMMENT '上传前文件名',
  `name_after` varchar(255) DEFAULT NULL COMMENT '上传后文件名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `group_code` varchar(32) DEFAULT NULL COMMENT '文件编码（相同则代表这些文件是一组）',
  `file_size` double DEFAULT NULL COMMENT '文件大小k',
  `file_path_save` varchar(255) DEFAULT NULL COMMENT '文件存储目录',
  `file_path_view` varchar(255) DEFAULT NULL COMMENT '文档访问路径',
  `file_type` varchar(255) DEFAULT NULL COMMENT '文档类型',
  `business_info` varchar(255) DEFAULT NULL COMMENT '业务信息',
  `business_type` varchar(255) DEFAULT NULL COMMENT '业务类型;biUser-import(bi用户导入)、biMapping-import(bi映射导入)、import(用户导入)、resource（资源中文件）、thumbnail（缩略图）、logo（图标）、banner（轮播）',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文档表';

/*Data for the table `t_file` */

LOCK TABLES `t_file` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_integration_log` */

DROP TABLE IF EXISTS `t_integration_log`;

CREATE TABLE `t_integration_log` (
  `id` varchar(32) NOT NULL,
  `type` int(4) DEFAULT NULL COMMENT '类型（数据字典：integration_type）',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `content` text COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='集成记录表';

/*Data for the table `t_integration_log` */

LOCK TABLES `t_integration_log` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_issue` */

DROP TABLE IF EXISTS `t_issue`;

CREATE TABLE `t_issue` (
  `id` varchar(32) NOT NULL COMMENT 'id，主键',
  `code` varchar(32) DEFAULT NULL COMMENT '编码',
  `title` varchar(720) DEFAULT NULL COMMENT '标题',
  `lv` int(11) DEFAULT NULL COMMENT '优先级  1、高；2、中；3、低',
  `is_reply` varchar(5) DEFAULT '0',
  `ip` varchar(255) DEFAULT NULL COMMENT '客户端ip',
  `creater` varchar(32) DEFAULT NULL COMMENT '提出人（创建人）',
  `org_id` varchar(32) DEFAULT NULL COMMENT '提出人的组织',
  `create_time` datetime DEFAULT NULL COMMENT '提出时间（创建时间）',
  `state` int(11) DEFAULT NULL COMMENT '状态（1、新建；2、处理中；3、关闭）',
  `updater` varchar(32) DEFAULT NULL COMMENT '最后更新人（修改人/回复人）',
  `update_time` datetime DEFAULT NULL COMMENT '最后更新时间',
  `content` text COMMENT '问题描述（问题内容）',
  `sys_impower` varchar(720) DEFAULT NULL COMMENT '系统授权（被授权的系统名称）',
  `sys_version` varchar(255) DEFAULT NULL COMMENT '系统版本（被授权的系统版本）',
  `browser_version` varchar(720) DEFAULT NULL COMMENT '浏览器版本信息',
  `resolution` varchar(720) DEFAULT NULL COMMENT '客户端分辨率',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='问题管理表';

/*Data for the table `t_issue` */

LOCK TABLES `t_issue` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_issue_record` */

DROP TABLE IF EXISTS `t_issue_record`;

CREATE TABLE `t_issue_record` (
  `id` varchar(32) NOT NULL COMMENT '主键id',
  `issue_id` varchar(32) DEFAULT NULL COMMENT '问题id',
  `content` text COMMENT '回复内容',
  `creater` varchar(32) DEFAULT NULL COMMENT '回复人',
  `create_time` datetime DEFAULT NULL COMMENT '回复时间',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='问题管理回复表';

/*Data for the table `t_issue_record` */

LOCK TABLES `t_issue_record` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_license` */

DROP TABLE IF EXISTS `t_license`;

CREATE TABLE `t_license` (
  `id` varchar(32) NOT NULL,
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `apply_type` varchar(50) DEFAULT NULL COMMENT '申请来源：在线 离线',
  `start_time` datetime DEFAULT NULL COMMENT '授权起始日期',
  `end_time` datetime DEFAULT NULL COMMENT '授权终止日期',
  `type` int(4) DEFAULT NULL COMMENT '类型',
  `amount` varchar(255) DEFAULT '1' COMMENT '金额',
  `ext_info` varchar(255) DEFAULT NULL,
  `state` int(4) DEFAULT NULL COMMENT '状态',
  `creater` varchar(50) DEFAULT NULL COMMENT '创建人',
  `approver` varchar(32) DEFAULT NULL COMMENT '审批人',
  `approve_time` datetime DEFAULT NULL COMMENT '审核时间',
  `phone` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) DEFAULT NULL COMMENT '联系邮箱',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `company` varchar(255) DEFAULT NULL COMMENT '公司',
  `reason` varchar(255) DEFAULT NULL COMMENT '拒绝原因',
  `time_enabled` tinyint(4) DEFAULT '1',
  `mac_enabled` tinyint(255) DEFAULT '1',
  `amount_enabled` tinyint(255) DEFAULT '1',
  `mac_address` varchar(255) DEFAULT NULL,
  `customer_type` varchar(255) DEFAULT 'user' COMMENT '客户类型：company公司user个人',
  `path` varchar(255) DEFAULT NULL COMMENT '授权文件目录',
  `jvm` varchar(255) DEFAULT NULL COMMENT '虚拟机信息',
  `os_name` varchar(255) DEFAULT NULL COMMENT '操作系统',
  `os_arch` varchar(255) DEFAULT NULL,
  `os_version` varchar(255) DEFAULT NULL,
  `computer_name` varchar(255) DEFAULT NULL,
  `db_info` varchar(255) DEFAULT NULL,
  `memory` varchar(255) DEFAULT NULL,
  `max_user_count` int(4) DEFAULT NULL COMMENT '最大限制用户数',
  `version` varchar(255) DEFAULT NULL COMMENT '产品版本',
  `activate_time` datetime DEFAULT NULL COMMENT '激活日期',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='授权表';

/*Data for the table `t_license` */

LOCK TABLES `t_license` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_list_manage` */

DROP TABLE IF EXISTS `t_list_manage`;

CREATE TABLE `t_list_manage` (
  `id` varchar(32) NOT NULL,
  `name` varchar(200) DEFAULT NULL COMMENT '列表名称',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `creater` varchar(32) DEFAULT NULL COMMENT '创建人',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `introduce` varchar(200) DEFAULT NULL COMMENT '介绍',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='列表管理';

/*Data for the table `t_list_manage` */

LOCK TABLES `t_list_manage` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_list_manage_resource` */

DROP TABLE IF EXISTS `t_list_manage_resource`;

CREATE TABLE `t_list_manage_resource` (
  `list_id` varchar(32) NOT NULL COMMENT '列表id',
  `resource_id` varchar(32) NOT NULL COMMENT '资源id',
  `sort` int(11) DEFAULT '1' COMMENT '排序',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`list_id`,`resource_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='列表资源关联表';

/*Data for the table `t_list_manage_resource` */

LOCK TABLES `t_list_manage_resource` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_menu` */

DROP TABLE IF EXISTS `t_menu`;

CREATE TABLE `t_menu` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '模块名',
  `link_url` varchar(255) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL COMMENT '模块编号',
  `status` int(4) DEFAULT NULL COMMENT '0启用  1禁用',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父级id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `icon_name` varchar(100) DEFAULT NULL COMMENT '图标名',
  `sort` int(4) DEFAULT '1' COMMENT '排序',
  `type` varchar(32) DEFAULT '' COMMENT '菜单类型',
  `link_type` int(4) DEFAULT '1' COMMENT '1:页面内跳转;2:系统内;3外部',
  `state` int(11) DEFAULT '1',
  `lv` int(11) DEFAULT '1',
  `introduce` varchar(255) DEFAULT NULL,
  `creater` varchar(32) DEFAULT NULL,
  `resource_id` varchar(32) DEFAULT NULL,
  `is_mobile` int(1) DEFAULT '0' COMMENT '是否移动端资源 0 否（默认），1是',
  `show_style` varchar(255) DEFAULT NULL COMMENT 'PC端菜单显示风格。list：列表；card：卡片。默认列表',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='菜单表';

/*Data for the table `t_menu` */

LOCK TABLES `t_menu` WRITE;

insert  into `t_menu`(`id`,`name`,`link_url`,`code`,`status`,`parent_id`,`create_time`,`update_time`,`icon_name`,`sort`,`type`,`link_type`,`state`,`lv`,`introduce`,`creater`,`resource_id`,`is_mobile`,`show_style`,`tenant_id`) values ('1064463514341588993','组织管理','/organization/index',NULL,NULL,'1064830825393131522',NULL,NULL,NULL,2,'1',1,1,2,'',NULL,'4',0,NULL,'1'),('1064463561217130498','角色管理','/role/index',NULL,NULL,'1064830825393131522',NULL,NULL,NULL,3,'1',1,1,2,'',NULL,'5',0,NULL,'1'),('1064468951266869250','资源管理','/resource/index',NULL,NULL,'1064830825393131522',NULL,NULL,NULL,5,'1',1,1,2,'',NULL,'1064468887446339585',0,NULL,'1'),('1064475399409483777','权限管理','/rolePermission/index',NULL,NULL,'1064830825393131522',NULL,NULL,NULL,4,'1',1,1,1,'',NULL,'1064475233273102338',0,NULL,'1'),('1064752369976213506','系统设置','/config/index',NULL,NULL,'1064830825393131522',NULL,NULL,NULL,9,'1',1,1,1,'',NULL,'28',0,NULL,'1'),('1064830825393131522','系统管理','',NULL,NULL,'0',NULL,NULL,NULL,23,'1',1,1,1,'',NULL,'',0,NULL,'1'),('1065478714712735745','菜单管理','/menu/index',NULL,NULL,'1064830825393131522',NULL,NULL,NULL,6,'1',1,1,1,'',NULL,'6',0,NULL,'1'),('1075233065886838785','字典管理','/dict/index',NULL,NULL,'7',NULL,NULL,NULL,6,'1',1,1,1,'',NULL,'31',0,NULL,'1'),('1136452775974297602','系统公告','/notice/index',NULL,NULL,'1064830825393131522',NULL,NULL,NULL,15,'1074135544087265281',1,1,1,'这是系统公告哦',NULL,'36',0,NULL,'1'),('1136461721367764993','销售主题','',NULL,NULL,'0',NULL,NULL,NULL,2,'',1,1,1,'',NULL,'',0,'list','1'),('1136463109489451009','门店销售总览-Echarts','/h5/echartDemo2',NULL,NULL,'1136461721367764993',NULL,NULL,NULL,3,'1074486583214817281',1,1,1,'白色背景地图',NULL,'1064469435402797058',0,NULL,'1'),('1158571774103031809','我的','',NULL,NULL,'0',NULL,NULL,NULL,0,'',1,1,1,'',NULL,'',0,NULL,'1'),('1158571830671609857','我的收藏','/collect/index',NULL,NULL,'1158571774103031809',NULL,NULL,NULL,2,'1',1,1,1,'',NULL,'1064464184058691585',0,NULL,'1'),('7','集成设置','',NULL,1,'0','2017-11-27 09:26:10',NULL,'',22,'2',1,1,0,'',NULL,'',0,NULL,'1');

UNLOCK TABLES;

/*Table structure for table `t_message_center` */

DROP TABLE IF EXISTS `t_message_center`;

CREATE TABLE `t_message_center` (
  `id` varchar(255) NOT NULL COMMENT '主键',
  `title` text COMMENT '消息标题',
  `content` text COMMENT '消息内容',
  `receive_user` varchar(255) DEFAULT NULL COMMENT '接收人',
  `produce_user` varchar(255) DEFAULT NULL COMMENT '消息产生人； 如果是系统直接赋值：“system”，其他的用户填写用户id',
  `create_time` datetime DEFAULT NULL COMMENT '消息创建时间',
  `type` int(11) DEFAULT NULL COMMENT '消息类型; 1.系统提示（包括“用户过期提醒”，“系统警告”）；2.交互信息（包括“回复评论”、“评论消息”）；3.通知消息（包括“公告消息”、“通知消息”）',
  `is_read` int(11) NOT NULL DEFAULT '0' COMMENT '是否已读 0、未读；1、已读',
  `read_time` datetime DEFAULT NULL COMMENT '读取时间',
  `levels` int(11) DEFAULT NULL COMMENT '消息等级',
  `resource_id` varchar(255) DEFAULT NULL COMMENT '资源id',
  `message_source_id` varchar(255) DEFAULT NULL COMMENT '消息来源ID。例：发布评论a后，发出消息。这条消息的来源就是这个评论a（一般用在评论和问题回复中，为了方便删除消息）',
  `issue_id` varchar(255) DEFAULT NULL COMMENT '问题id',
  `tenant_id` varchar(255) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_message_center` */

LOCK TABLES `t_message_center` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_mo_content` */

DROP TABLE IF EXISTS `t_mo_content`;

CREATE TABLE `t_mo_content` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `url` varchar(200) DEFAULT NULL COMMENT 'url',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `x_axis` varchar(32) DEFAULT NULL COMMENT 'x轴占格',
  `y_axis` varchar(32) DEFAULT NULL COMMENT 'y轴占高',
  `x_num` int(11) DEFAULT NULL COMMENT '一排显示几个',
  `y_num` int(11) DEFAULT NULL COMMENT '显示几排',
  `hidden_more` int(11) DEFAULT NULL COMMENT '是否隐藏多余菜单 1隐藏 0 不隐藏',
  `type` int(11) DEFAULT NULL COMMENT '关联的模块类型 1对象 2 目录 3 HTML 4公告 5轮播图 6 列表',
  `relate` varchar(32) DEFAULT NULL COMMENT '关联的资源id,菜单id，目录id',
  `content` text COMMENT '文本内容',
  `show_type` int(11) DEFAULT NULL COMMENT '1图标+标题2缩略图+标题3缩略图+标题+描述4缩略图+标题+描述+功能',
  `resource_type` varchar(200) DEFAULT NULL COMMENT '资源类型id',
  `top_num` int(11) DEFAULT NULL COMMENT '前多少条',
  `date_type` int(11) DEFAULT NULL COMMENT '日期类型',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块内容基础信息表';

/*Data for the table `t_mo_content` */

LOCK TABLES `t_mo_content` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_module` */

DROP TABLE IF EXISTS `t_module`;

CREATE TABLE `t_module` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `state` varchar(32) DEFAULT NULL COMMENT '状态：0正常1禁用',
  `creater` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `is_delete` int(11) DEFAULT NULL COMMENT '0未删除，1 删除了',
  `is_edit` int(11) DEFAULT NULL COMMENT '0不可编辑，1可以编辑',
  `type` int(11) DEFAULT NULL COMMENT '0普通用户自己存储 ，其他为非普通用户存储',
  `comments` varchar(200) DEFAULT NULL COMMENT '备注',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板基础信息表';

/*Data for the table `t_module` */

LOCK TABLES `t_module` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_module_content` */

DROP TABLE IF EXISTS `t_module_content`;

CREATE TABLE `t_module_content` (
  `module_id` varchar(32) DEFAULT NULL COMMENT '模板id',
  `content_id` varchar(32) DEFAULT NULL COMMENT '面板内容id',
  `tenant_id` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板内容关联表';

/*Data for the table `t_module_content` */

LOCK TABLES `t_module_content` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_notice` */

DROP TABLE IF EXISTS `t_notice`;

CREATE TABLE `t_notice` (
  `id` varchar(32) NOT NULL,
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `title_color` varchar(255) DEFAULT NULL COMMENT '标题的颜色',
  `content` text COMMENT '内容',
  `publisher` varchar(255) DEFAULT NULL COMMENT '发布人',
  `publish_time` datetime DEFAULT NULL COMMENT '发布时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(32) DEFAULT NULL COMMENT '起草人',
  `notice_level` varchar(255) DEFAULT NULL COMMENT '优先级：1普通2重要3紧急',
  `state` int(11) DEFAULT '0' COMMENT '状态：0未发布、1发布(是否发布)',
  `org_code` varchar(255) DEFAULT NULL COMMENT '接收单位',
  `valid_start_time` datetime DEFAULT NULL COMMENT '有效开始时间',
  `valid_end_time` datetime DEFAULT NULL COMMENT '有效截止日期',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `read_num` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `type` int(11) DEFAULT NULL COMMENT '公告类型。1普通文字2，配置链接',
  `is_for_ever` int(1) DEFAULT NULL COMMENT '是否永久 1 是 ， 0 否',
  `tenant_id` varchar(32) DEFAULT '1' COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公告表';

/*Data for the table `t_notice` */

LOCK TABLES `t_notice` WRITE;

insert  into `t_notice`(`id`,`title`,`title_color`,`content`,`publisher`,`publish_time`,`create_time`,`creater`,`notice_level`,`state`,`org_code`,`valid_start_time`,`valid_end_time`,`sort`,`read_num`,`updater`,`update_time`,`type`,`is_for_ever`,`tenant_id`) values ('1110386932010225666','新闻发布：德昂官网（新）','','http://www.dataondemand.cn/','1067371912724770817','2021-10-07 15:42:34','2019-03-26 03:44:09','1088618188019605505','3',1,NULL,NULL,NULL,1,38,'1088618188019605505','2019-03-26 09:49:09',2,1,'1');

UNLOCK TABLES;

/*Table structure for table `t_notice_already_read` */

DROP TABLE IF EXISTS `t_notice_already_read`;

CREATE TABLE `t_notice_already_read` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `notice_id` varchar(32) NOT NULL COMMENT '公告id',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`,`notice_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_notice_already_read` */

LOCK TABLES `t_notice_already_read` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_org_data_permission` */

DROP TABLE IF EXISTS `t_org_data_permission`;

CREATE TABLE `t_org_data_permission` (
  `id` varchar(32) NOT NULL,
  `org_id` varchar(32) DEFAULT NULL COMMENT '组织id',
  `dp_id` varchar(32) DEFAULT NULL COMMENT '数据权限id',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_org_data_permission` */

LOCK TABLES `t_org_data_permission` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_organization` */

DROP TABLE IF EXISTS `t_organization`;

CREATE TABLE `t_organization` (
  `id` varchar(32) NOT NULL,
  `parent_id` varchar(32) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `status` tinyint(11) DEFAULT '1' COMMENT '1：启用(默认)，0：禁用，2：AD组织',
  `remark` varchar(255) DEFAULT NULL,
  `ext_code` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `ext_id` varchar(32) DEFAULT NULL COMMENT '外部唯一标识',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='组织表';

/*Data for the table `t_organization` */

LOCK TABLES `t_organization` WRITE;

insert  into `t_organization`(`id`,`parent_id`,`code`,`name`,`create_time`,`update_time`,`status`,`remark`,`ext_code`,`state`,`sort`,`ext_id`,`tenant_id`) values ('1','0','001','总公司','2016-10-19 16:09:13','2016-10-19 16:09:16',0,NULL,'DA',NULL,1,NULL,'1'),('19','1','001001','德昂信息',NULL,NULL,1,NULL,'DOD',NULL,NULL,NULL,'1'),('20','19','001001001','技术支持',NULL,NULL,1,NULL,'JSZC',NULL,1,NULL,'1'),('21','1','001002','客户机构',NULL,NULL,1,NULL,'CUST',NULL,NULL,NULL,'1'),('222','21','001002001','教育',NULL,NULL,1,NULL,'XDF',NULL,1,NULL,'1'),('1055671780929294338','19','001001002','产品研发',NULL,NULL,1,NULL,'CPYF',NULL,1,NULL,'1'),('1055672118159724546','19','001001003','产品销售',NULL,NULL,1,NULL,'CPXS',NULL,10,NULL,'1'),('1068517075689988097','21','001002002','汽车行业',NULL,NULL,1,NULL,'QCHY',NULL,5,NULL,'1'),('1073587458189377538','19','001001004','技术服务',NULL,NULL,1,NULL,'JSFW',NULL,1,NULL,'1'),('1073587509775122434','19','001001005','财务部',NULL,NULL,1,NULL,'CWB',NULL,1,NULL,'1'),('1068517351369007105','21','001002003','餐饮行业',NULL,NULL,1,NULL,'CYHY',NULL,2,NULL,'1'),('1069788670765105154','21','001002004','建筑行业',NULL,NULL,1,NULL,'JZHY',NULL,3,NULL,'1'),('1073587567149006849','19','001001006','运营部',NULL,NULL,1,NULL,'YYB',NULL,NULL,NULL,'1'),('1102390360642953217','21','001002005','互联网行业',NULL,NULL,1,NULL,'HLWHY',NULL,4,NULL,'1'),('1108182773236285442','21','001002006','其他',NULL,NULL,1,NULL,'QT',NULL,100,NULL,'1'),('1417038397123973122','19','001001007','填报组',NULL,NULL,1,NULL,'TBZ',NULL,1,NULL,'1'),('1417319727254589442','19','001001008','填报2组',NULL,NULL,1,NULL,'TBZ2',NULL,1,NULL,'1');

UNLOCK TABLES;

/*Table structure for table `t_organization_module` */

DROP TABLE IF EXISTS `t_organization_module`;

CREATE TABLE `t_organization_module` (
  `organization_id` varchar(32) DEFAULT NULL,
  `module_id` varchar(32) DEFAULT NULL,
  `tenant_id` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织模板关联表';

/*Data for the table `t_organization_module` */

LOCK TABLES `t_organization_module` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_permission` */

DROP TABLE IF EXISTS `t_permission`;

CREATE TABLE `t_permission` (
  `id` varchar(32) NOT NULL,
  `resource_type_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `name` varchar(50) NOT NULL COMMENT '权限名称',
  `code` varchar(255) DEFAULT NULL,
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `sort` int(11) DEFAULT NULL COMMENT '权限管理中控制显示顺序',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

/*Data for the table `t_permission` */

LOCK TABLES `t_permission` WRITE;

insert  into `t_permission`(`id`,`resource_type_id`,`name`,`code`,`resource_id`,`sort`,`tenant_id`) values ('052cf2af7d9e11eb9ad','1','订阅','subscribe',NULL,8,'1'),('05314bbe7d9e11eb9ad','2','订阅','subscribe',NULL,8,'1'),('05314c3d7d9e11eb9ad','3','订阅','subscribe',NULL,8,'1'),('05314c7c7d9e11eb9ad','4','订阅','subscribe',NULL,8,'1'),('05314cb47d9e11eb9ad','5','订阅','subscribe',NULL,8,'1'),('05314cf27d9e11eb9ad','6','订阅','subscribe',NULL,8,'1'),('05314d2a7d9e11eb9ad','7','订阅','subscribe',NULL,8,'1'),('05314d5f7d9e11eb9ad','8','订阅','subscribe',NULL,8,'1'),('05314d907d9e11eb9ad','10','订阅','subscribe',NULL,8,'1'),('05314dcc7d9e11eb9ad','11','订阅','subscribe',NULL,8,'1'),('05314e007d9e11eb9ad','12','订阅','subscribe',NULL,8,'1'),('1','1','可见','view',NULL,1,'1'),('10','3','导出','export',NULL,2,'1'),('10408049924679148001','1','日志','log',NULL,7,'1'),('10408049924679148002','2','日志','log',NULL,7,'1'),('10408049924679148003','3','日志','log',NULL,7,'1'),('10408049924679148004','4','日志','log',NULL,7,'1'),('10408049924679148005','5','日志','log',NULL,7,'1'),('10408049924679148006','6','日志','log',NULL,7,'1'),('10408049924679148007','7','日志','log',NULL,7,'1'),('10408049924679148008','8','日志','log',NULL,7,'1'),('10408049924679148009','9','日志','log',NULL,7,'1'),('11','4','导出','export',NULL,2,'1'),('1140538857002840065','1','分享','share',NULL,5,'1'),('1140538857262886914','1','评论','comment',NULL,4,'1'),('1140538955241828354','2','分享','share',NULL,5,'1'),('1140538955401211906','2','评论','comment',NULL,4,'1'),('1140539047021588482','3','分享','share',NULL,5,'1'),('1140539047608791041','3','评论','comment',NULL,4,'1'),('1140539135433322497','4','分享','share',NULL,5,'1'),('1140539135827587073','4','评论','comment',NULL,4,'1'),('1140539229876465666','5','分享','share',NULL,5,'1'),('1140539230136512514','5','评论','comment',NULL,4,'1'),('1140539322889351169','6','分享','share',NULL,5,'1'),('1140539323916955650','6','评论','comment',NULL,4,'1'),('1140539423472955393','7','分享','share',NULL,5,'1'),('1140539423619756034','7','评论','comment',NULL,4,'1'),('1140539502774661121','8','分享','share',NULL,5,'1'),('1140539502774661122','12','日志','log',NULL,7,'1'),('1140539502774661123','12','分享','share',NULL,5,'1'),('1140539502774661124','12','评论','comment',NULL,4,'1'),('1140539502774661125','12','权限用户','permission',NULL,6,'1'),('1140539502774661126','12','可见','view',NULL,1,'1'),('1140539503282171905','8','评论','comment',NULL,4,'1'),('1140539588653035521','9','分享','share',NULL,5,'1'),('1140539588858556418','9','评论','comment',NULL,4,'1'),('12','5','导出','export',NULL,2,'1'),('2','2','可见','view',NULL,1,'1'),('2433432423423434324','9','可下载','download',NULL,3,'1'),('245040849338109952','1','权限用户','permission',NULL,6,'1'),('245040849338109953','2','权限用户','permission',NULL,6,'1'),('245040849338109954','3','权限用户','permission',NULL,6,'1'),('245040849338109955','4','权限用户','permission',NULL,6,'1'),('245040849338109956','5','权限用户','permission',NULL,6,'1'),('245040849338109957','6','权限用户','permission',NULL,6,'1'),('245040849338109958','7','权限用户','permission',NULL,6,'1'),('245040849338109959','8','权限用户','permission',NULL,6,'1'),('245040849338109960','9','权限用户','permission',NULL,6,'1'),('29704084563810995457','10','可见','view',NULL,1,'1'),('3','3','可见','view',NULL,1,'1'),('38108049124679148101','10','日志','log',NULL,7,'1'),('4','4','可见','view',NULL,1,'1'),('40405394234729553282','10','分享','share',NULL,5,'1'),('5537673184217581e15','5','可见','view',NULL,1,'1'),('56705394346197560393','10','评论','comment',NULL,4,'1'),('6','6','可见','view',NULL,1,'1'),('61604084453810995474','10','权限用户','permission',NULL,6,'1'),('7','7','可见','view',NULL,1,'1'),('8','8','可见','view',NULL,1,'1'),('9','9','可见','view',NULL,1,'1');

UNLOCK TABLES;

/*Table structure for table `t_prompt` */

DROP TABLE IF EXISTS `t_prompt`;

CREATE TABLE `t_prompt` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '提示名称',
  `code` varchar(255) DEFAULT NULL COMMENT '提示参数code',
  `dict_code` varchar(255) DEFAULT NULL COMMENT '数据字典code',
  `type` int(255) DEFAULT NULL COMMENT '显示类型',
  `special` varchar(255) DEFAULT NULL COMMENT '特殊标记',
  `cascade_code` varchar(255) DEFAULT NULL COMMENT '级联参数',
  `update_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `prompt_type` varchar(255) DEFAULT NULL COMMENT '提示类型',
  `object_type` int(2) DEFAULT NULL COMMENT '如果提示类型是对象提示，需要指定对象的类型（4-度量；12-实体；如果为空默认为12）',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='提示表';

/*Data for the table `t_prompt` */

LOCK TABLES `t_prompt` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_prompt_rel` */

DROP TABLE IF EXISTS `t_prompt_rel`;

CREATE TABLE `t_prompt_rel` (
  `id` varchar(32) NOT NULL,
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `prompt_id` varchar(32) DEFAULT NULL COMMENT '提示id',
  `default_value1` varchar(255) DEFAULT NULL COMMENT '默认值1',
  `default_value2` varchar(255) DEFAULT NULL COMMENT '默认值2',
  `default_value3` varchar(255) DEFAULT NULL COMMENT '默认值3。1-2一组，3-4一组。',
  `default_value4` varchar(255) DEFAULT NULL COMMENT '默认值4',
  `date_format` varchar(255) DEFAULT NULL COMMENT '日期格式 yyyy-MM-dd  yyyyMMdd',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `hidden` int(4) NOT NULL DEFAULT '0' COMMENT '是否显示：0显示1隐藏',
  `required` int(11) DEFAULT NULL COMMENT '是否必填',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源提示关联表';

/*Data for the table `t_prompt_rel` */

LOCK TABLES `t_prompt_rel` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_resource` */

DROP TABLE IF EXISTS `t_resource`;

CREATE TABLE `t_resource` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(50) NOT NULL COMMENT '模块名',
  `link_url` varchar(3600) DEFAULT NULL COMMENT '链接地址',
  `code` varchar(50) DEFAULT NULL COMMENT '模块编号',
  `status` int(4) DEFAULT NULL COMMENT '0启用  1禁用',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '父级id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `icon_name` varchar(100) DEFAULT NULL COMMENT '图标名',
  `thumbnail_id` varchar(32) DEFAULT NULL COMMENT '缩略图文件id',
  `thumbnail` varchar(255) DEFAULT NULL COMMENT '缩略图文件路径',
  `sort` int(4) DEFAULT '1' COMMENT '排序',
  `type` varchar(32) DEFAULT '1' COMMENT '资源类型id',
  `link_type` int(4) DEFAULT '1' COMMENT '1:页面内跳转;2:系统内;3外部',
  `state` int(11) DEFAULT '1',
  `lv` int(11) DEFAULT NULL,
  `introduce` varchar(255) DEFAULT NULL,
  `creater` varchar(32) DEFAULT NULL,
  `server_id` varchar(32) DEFAULT NULL,
  `project_id` varchar(32) DEFAULT NULL,
  `report_id` varchar(100) DEFAULT NULL,
  `type_name` varchar(255) DEFAULT NULL,
  `type_value` varchar(255) DEFAULT NULL,
  `hidden_sections` varchar(255) DEFAULT NULL,
  `view_num` int(11) DEFAULT '0' COMMENT '查看数',
  `download_num` int(11) DEFAULT '0' COMMENT '下载数',
  `collect_num` int(11) DEFAULT '0' COMMENT '收藏数',
  `comment_num` int(11) DEFAULT '0' COMMENT '评论数',
  `path` varchar(255) DEFAULT NULL COMMENT '原始路径',
  `resource_type1` varchar(32) DEFAULT NULL COMMENT '资源类型（大类）',
  `resource_type2` varchar(32) DEFAULT NULL COMMENT '资源类型（小类）',
  `file_id` varchar(32) DEFAULT NULL COMMENT '文档id',
  `everyone` varchar(32) DEFAULT NULL COMMENT '所有人课件',
  `is_mobile` int(1) DEFAULT '0' COMMENT '是否移动端资源 0 否（默认），1是',
  `auto_refreshtime` decimal(10,1) DEFAULT NULL COMMENT '定时刷新间隔',
  `show_tools` int(4) DEFAULT NULL COMMENT '是否显示数窗的工具栏(0启用 1禁用)',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模块表';

/*Data for the table `t_resource` */

LOCK TABLES `t_resource` WRITE;

insert  into `t_resource`(`id`,`name`,`link_url`,`code`,`status`,`parent_id`,`create_time`,`update_time`,`icon_name`,`thumbnail_id`,`thumbnail`,`sort`,`type`,`link_type`,`state`,`lv`,`introduce`,`creater`,`server_id`,`project_id`,`report_id`,`type_name`,`type_value`,`hidden_sections`,`view_num`,`download_num`,`collect_num`,`comment_num`,`path`,`resource_type1`,`resource_type2`,`file_id`,`everyone`,`is_mobile`,`auto_refreshtime`,`show_tools`,`tenant_id`) values ('1050571891880472577','用户映射','/biUser/index','',NULL,'','2018-10-12 10:20:32','2018-12-20 12:02:34','fa-gear','1065143559800348674','/upload/resource/93/31/162b10afd92e47b28ce6af83ad7781f6.jpg',1,'1074135544087265281',1,0,2,'','1069785317050953729',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',24,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1050572011216809986','提示管理','/prompt/index','',NULL,'','2018-10-12 10:21:01','2019-03-05 11:41:25','fa-gear','','/images/thumbnail.png',1,'1074135544087265281',1,0,2,'','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',224,0,1,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1054602708361367554','版本管理','/sysRelease/index','',NULL,'','2018-10-23 13:17:34','2018-12-20 18:54:06','fa-gear','','/images/thumbnail.png',2,'1074135544087265281',1,0,2,'','1067370861682520065',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',146,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1055661719364423681','行为日志','/sysLog/index','',NULL,'','2018-10-26 11:25:42','2019-03-05 11:46:45','fa-gear','1068514489683468290','/upload/resource/62/19/9ede5c60fce542178a4c51ecb0f71236.jpg',10,'1074135544087265281',1,0,2,'','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',205,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1062283595385495554','自助分析','/userReport/index','',NULL,'','2018-11-13 17:58:40','2018-12-20 12:01:53','fa-gear','1068386639278039041','/upload/resource/41/70/48af61e411224ea28401532716052009.jpg',1,'1074135544087265281',1,0,1,'德昂多维自主分析，英文VI，用户可随意将各种维度组合，实现数据展现','1069785317050953729',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',13,0,1,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1064463981406699522','模板管理','/module/index','',NULL,'','2018-11-19 18:22:45','2019-06-21 03:34:03','fa-gear','1068514089957908481','/upload/resource/22/40/4a375ae249ae46c391a0a32fa47ef04d.jpg',1,'1074135544087265281',1,0,NULL,'这是模板管理','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',531,0,1,1,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('1064464184058691585','我的收藏','/collect/index','',NULL,'','2018-11-19 18:23:33','2018-12-21 18:15:35','fa-gear','1075958703610417154','/upload/resource/84/30/7c0ac668074d4b9ca65dd8214c0ca8fb.png',1,'1074135544087265281',1,0,NULL,'','1067371912724770817',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',563,0,1,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1064468887446339585','资源管理','/resource/index','ZYGL',NULL,'','2018-11-19 18:42:14','2019-06-21 03:31:42','fa-gear','1068386433438375937','/upload/resource/89/41/470dc4e8427e4932902b20a6d1d771f2.jpg',1,'1074135544087265281',1,0,NULL,'这是资源管理，非常重要的关键功能，请务必小心操作。','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',1699,0,2,2,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('1064469370349142018','北京国际机场航班流向图','/h5/echartDemo1','',NULL,'','2018-11-19 18:44:09','2019-06-21 03:42:40','fa-gear','1065147521001709570','/upload/resource/40/65/b7dfd8675f284757a728313412b5279e.jpg',1,'1074486583214817281',1,0,NULL,'地图范例  黑色背景','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',20,0,0,0,'','1','1074486583214817281',NULL,'off',0,NULL,NULL,'1'),('1064469435402797058','全国门店分布','/h5/echartDemo2','',NULL,'','2018-11-19 18:44:25','2021-06-30 03:16:07','fa-gear','1074507874950688769','/upload/resource/46/25/28260b8b11454e00a980b4720ddbf88e.png',1,'1074486583214817281',1,0,NULL,'白色背景地图','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',155,0,0,0,'','1','1074486583214817281',NULL,'off',0,NULL,NULL,'1'),('1064475233273102338','权限管理','/rolePermission/index','',NULL,'','2018-11-19 19:07:27','2019-06-21 03:31:20','fa-gear','1068514139773657089','/upload/resource/7/62/902b8845595a4bd2be1e6101c102470b.jpg',1,'1074135544087265281',1,0,NULL,'','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',728,0,0,0,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('1064826764988104705','轮播图','/banner/index','',NULL,'','2018-11-20 18:24:19','2019-06-21 03:36:59','fa-gear','1068514029484433409','/upload/resource/99/50/42f5924f2ec446008748c7e2d0ee66b0.jpg',6,'1074135544087265281',1,0,NULL,'/banner/index','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',215,0,0,0,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('1065129399192440834','中国地图A ','/h5/echartDemo2','',NULL,'','2018-11-21 14:26:53','2019-06-21 03:42:56','fa-gear','','/images/thumbnail.png',2,'1074486583214817281',1,0,NULL,'建议删除，重复了。','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',10,0,0,0,'','1','1074486583214817281',NULL,'off',0,NULL,NULL,'1'),('1069777342193270786','资源日志','/resourceLog/toIndex','',NULL,'','2018-12-04 10:16:09','2019-03-05 11:46:28','fa-gear','1071936990983766017','/upload/resource/77/93/799e47717f384b4ea694e343e9073ce9.jpg',1,'1074135544087265281',1,0,NULL,'资源日志查看','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',166,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1070853405942022146','问题管理','/issue/index','',NULL,'','2018-12-07 09:32:02','2019-03-05 11:42:59','fa-gear','1071937122491973634','/upload/resource/11/46/da13a50374254928be1c5b9fcc64143e.jpg',9,'1074135544087265281',1,0,NULL,'管理用户提出的问题','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',295,0,0,1,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1071954344129777666','集成系统','/biServer/index','',NULL,'','2018-12-10 10:26:46','2019-03-15 13:38:46','fa-gear','1106429510438424578','/upload/resource/5/65/7de2b270d6ad4c2f8b66ded7737b36f0.jpg',1,'1074135544087265281',1,0,NULL,'','1105308455376343042',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',553,0,0,2,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1071954504381550593','集成项目','/biProject/index','',NULL,'','2018-12-10 10:27:24','2019-03-05 11:39:32','fa-gear','','/images/thumbnail.png',1,'1074135544087265281',1,0,NULL,'','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',375,0,1,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1071954658257981441','BI账户映射','/biMapping/index','',NULL,'','2018-12-10 10:28:01','2019-03-05 11:40:52','fa-gear','','/images/thumbnail.png',1,'1074135544087265281',1,0,NULL,'','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',273,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1071954765858656257','BI账户集成','/biUser/index','',NULL,'','2018-12-10 10:28:27','2019-03-05 11:40:41','fa-gear','','/images/thumbnail.png',1,'1074135544087265281',1,0,NULL,'','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',291,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1072445122713583618','列表管理','/listManage/index','',NULL,'','2018-12-11 18:56:57','2019-06-21 03:32:21','fa-gear','1073593468878667778','/upload/resource/4/70/3ec73dd51a2a461fa990a3a3e440a5f9.png',13,'1074135544087265281',1,0,NULL,'列表管理','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',284,0,0,0,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('1073576650801758210','德昂官网','http://www.dataondemand.cn',NULL,NULL,'','2018-12-14 21:53:14','2018-12-14 21:53:14','fa-bar-chart-o','','/images/thumbnail.png',1,'1073575810644918274',1,1,NULL,'','1069785317050953729',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',9,0,0,0,'','2','1073575810644918274',NULL,NULL,0,NULL,NULL,'1'),('1073578402372468737','普策数','http://www.psdatatech.com/','',NULL,'','2018-12-14 22:00:12','2019-06-21 03:38:53','fa-bar-chart-o','','/images/thumbnail.png',1,'1073575859684720641',1,1,NULL,'普策数官网','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',18,0,0,0,'','2','1073575859684720641',NULL,'off',0,NULL,NULL,'1'),('1073582214285967362','数窗平台介绍','http://www.dataondemand.cn/DOD-BI-Portal',NULL,NULL,'','2018-12-14 22:15:21','2018-12-14 22:15:29','fa-bar-chart-o','1073582250096934914','/upload/resource/51/17/52bc0578ee5b4ff7b409ba2375880f73.png',1,'1073575810644918274',1,1,NULL,'','1069785317050953729',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',13,0,0,1,'','2','1073575810644918274',NULL,NULL,0,NULL,NULL,'1'),('1074616855952437250','产品信息','/activate/info','',NULL,'','2018-12-17 18:46:39','2019-06-21 03:35:45','fa-gear','1074867604636594177','/upload/resource/12/78/64c28d985648437cba76b6ba3854e61e.jpeg',1,'1074135544087265281',1,0,NULL,'','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',251,0,1,0,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('1075234002399424513','公共服务','','',NULL,'','2018-12-19 11:38:58','2018-12-20 11:58:03','fa-gear','','/images/thumbnail.png',1,'1074135544087265281',1,0,NULL,'','1069785317050953729',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',0,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1075262650833727490','运维服务','','',NULL,'','2018-12-19 13:32:48','2018-12-20 11:58:10','fa-gear','','/images/thumbnail.png',1,'1074135544087265281',1,0,NULL,'我是运维服务','1069785317050953729',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',3,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1075297887101915138','服务监控','/serviceState/index','',NULL,'','2018-12-19 15:52:49','2019-03-05 11:45:28','fa-gear','1075405337566138370','/upload/resource/9/47/4541d24b53534d98a72ed40a99277ded.png',1,'1074135544087265281',1,0,NULL,'配置监控服务器的端口信息','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',146,0,0,1,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1075298052449767425','服务状态','/serviceStateRecord/index','',NULL,'','2018-12-19 15:53:28','2019-03-05 11:45:14','fa-gear','1075405400849797122','/upload/resource/38/95/b39b7308ce0546f79478afeede16616c.png',1,'1074135544087265281',1,0,NULL,'查看平台所在服务器的基本信息及监控的端口状态信息','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',125,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('1075955649481822209','产品说明','/listManage/lists/1075954983749308417','DW',NULL,'','2018-12-21 11:26:32','2018-12-21 11:26:47','fa-gear','','/images/thumbnail.png',1,'1074135579436859394',1,1,NULL,'','1069785317050953729',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',110,0,1,0,'','1','1074135579436859394',NULL,NULL,0,NULL,NULL,'1'),('1133652599098322945','保险行业','','',NULL,'','2019-05-29 08:33:37','2019-05-29 08:52:04','fa-gear','','/images/thumbnail.png',2,'1074135544087265281',1,1,NULL,'','1068339153872150529','','','','',NULL,'header,footer,path,dockTop,dockLeft',1,0,0,0,'','1','1074135544087265281',NULL,'on',0,NULL,NULL,'1'),('28','系统设置','/config/index','',1,'','2017-12-08 13:21:47','2019-06-21 03:34:30','fa-gear','1112977414099955713','/upload/resource/64/71/25bc9476c65c4c3494dd53af80130341.jpg',5,'1074135544087265281',1,0,2,'','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',518,0,0,1,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('31','字典管理','/dict/index','',1,'','2018-01-27 14:10:06','2019-03-05 11:41:59','fa-gear','','/images/thumbnail.png',5,'1074135544087265281',1,0,2,'','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',148,0,0,0,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('36','系统公告','/notice/index','',1,'',NULL,'2019-06-21 03:33:02','fa-gear','1068514542082908161','/upload/resource/83/45/05e293f74b294de0bce13942f6883f50.jpg',10,'1074135544087265281',1,0,1,'这是系统公告哦','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',279,0,0,2,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('4','组织管理','/organization/index','org',1,'','2016-08-03 09:42:41','2019-03-05 11:32:59','fa-gear','1068336092932005889','/upload/resource/13/15/d07fe457734e461d8664bc73372d4869.jpg',1,'1074135544087265281',1,0,2,'','1068339153872150529',NULL,'','','',NULL,'header,footer,path,dockTop,dockLeft',1002,0,1,5,'','1','1074135544087265281',NULL,NULL,0,NULL,NULL,'1'),('5','角色管理','/role/index','role',1,'','2016-08-03 09:42:41','2019-06-21 03:31:03','fa-gear','1068514634772832257','/upload/resource/1/90/82892a3eaca54aa6bc254edae971e4ee.jpg',2,'1074135544087265281',1,0,2,'','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',508,0,2,3,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1'),('6','菜单管理','/menu/index','resource',1,'','2016-08-03 09:42:41','2019-06-21 03:32:01','fa-gear','1068386220279652353','/upload/resource/27/35/60db0b6242be48ca91f80ec7cccf0457.jpg',3,'1074135544087265281',1,0,2,'这个是菜单管理功能，用于进行菜单的编辑与修改，菜单全部来自于系统资源。','1067370861682520065','','','','',NULL,'header,footer,path,dockTop,dockLeft',888,0,0,0,'','1','1074135544087265281',NULL,'off',0,NULL,NULL,'1');

UNLOCK TABLES;

/*Table structure for table `t_resource_dict` */

DROP TABLE IF EXISTS `t_resource_dict`;

CREATE TABLE `t_resource_dict` (
  `id` varchar(32) NOT NULL,
  `resource_id` varchar(100) NOT NULL,
  `name` varchar(255) NOT NULL COMMENT '名称',
  `dict_value` varchar(255) DEFAULT NULL COMMENT '编码',
  `explain` varchar(255) DEFAULT NULL COMMENT '说明',
  `source` varchar(255) DEFAULT NULL COMMENT '来源',
  `algorithm` varchar(255) DEFAULT NULL COMMENT '算法口径',
  `modifier` varchar(255) DEFAULT '' COMMENT '创建时间',
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '修改时间',
  `tenant_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='= ''资源字典表'' DEFAULT CHARSET=utf8';

/*Data for the table `t_resource_dict` */

LOCK TABLES `t_resource_dict` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_resource_log` */

DROP TABLE IF EXISTS `t_resource_log`;

CREATE TABLE `t_resource_log` (
  `id` varchar(32) NOT NULL,
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `type` int(4) DEFAULT NULL COMMENT '记录类型：1查看2下载3添加收藏4取消收藏5添加评论6取消评论',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `creater_name` varchar(255) DEFAULT NULL COMMENT '创建人姓名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `ip` varchar(255) DEFAULT NULL COMMENT '操作ip',
  `browser` varchar(255) DEFAULT NULL,
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源操作记录表';

/*Data for the table `t_resource_log` */

LOCK TABLES `t_resource_log` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_resource_type` */

DROP TABLE IF EXISTS `t_resource_type`;

CREATE TABLE `t_resource_type` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `code` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '资源类型',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '上级id',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '说明',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='资源类型表';

/*Data for the table `t_resource_type` */

LOCK TABLES `t_resource_type` WRITE;

insert  into `t_resource_type`(`id`,`code`,`name`,`parent_id`,`sort`,`remark`,`tenant_id`) values ('1',1,'系统功能','0',1,'','1'),('10',10,'SmartBI','0',9,NULL,'1'),('1072304561931165697',9,'演示文档','9',1,'','1'),('1073107370540793858',9,'系统说明','9',1,'','1'),('1073575810644918274',2,'公司网站','2',1,'','1'),('1073575859684720641',2,'友情链接','2',3,'','1'),('1073575918094598146',2,'核心应用','2',2,'','1'),('1074135544087265281',1,'系统内置','1',1,'','1'),('1074135579436859394',1,'用户扩展','1',2,'','1'),('1074486267937374209',5,'外链范例','2',1,'','1'),('1074486583214817281',1,'内链范例','1',10,'','1'),('1078110450114121729',9,'会议记录','9',1,'','1'),('11',11,'Qlik','0',9,'','1'),('1141958267802763265',2,'外网链接','2',5,'123','1'),('12',12,'Cognos','0',9,'','1'),('2',2,'链接','0',2,NULL,'1'),('3',3,'MSTR-Dossier','0',3,'','1'),('4',4,'MSTR-Report','0',4,'','1'),('5',5,'MSTR-Document','0',5,'','1'),('6',6,'Tableau','0',6,'','1'),('7',7,'帆软','0',7,'','1'),('8',8,'BO','0',8,'','1'),('9',9,'文档','0',10,'fff','1');

UNLOCK TABLES;

/*Table structure for table `t_role` */

DROP TABLE IF EXISTS `t_role`;

CREATE TABLE `t_role` (
  `id` varchar(32) NOT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(50) DEFAULT NULL COMMENT '角色编码',
  `parent_id` varchar(32) DEFAULT NULL COMMENT '上级id',
  `type` varchar(10) DEFAULT NULL COMMENT '类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `state` varchar(32) DEFAULT NULL COMMENT '状态',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `t_role` */

LOCK TABLES `t_role` WRITE;

insert  into `t_role`(`id`,`name`,`code`,`parent_id`,`type`,`sort`,`create_time`,`update_time`,`state`,`tenant_id`) values ('1','角色','role','0','t1',1,'2016-07-22 10:58:56','2016-07-22 10:59:03',NULL,'1'),('1182466788704215042','角色','role','0',NULL,0,'2019-10-11 01:23:46',NULL,'0','1182466788184121346'),('3','管理员','admin','1','t3',1,'2016-07-22 10:59:50','2016-07-22 10:59:53','0','1'),('1102869621011156993','角色','role','0',NULL,0,'2019-03-05 17:53:03',NULL,NULL,'1102869620780470273'),('1102869621040517122','管理员','admin_system','1102869621011156993',NULL,1,'2019-03-05 17:53:03',NULL,'0','1102869620780470273'),('1102869621065682946','所有人','EVERYONE','1102869621011156993',NULL,2,'2019-03-05 17:53:03',NULL,'0','1102869620780470273'),('1073594031590686721','运维管理员','YWGL','1',NULL,1,NULL,NULL,NULL,'1'),('1074503869703208961','数据分析师','SJFXS','1',NULL,1,NULL,NULL,NULL,'1'),('4','所有人','EVERYONE','1',NULL,1,NULL,NULL,'0','1'),('6','测试人员','role_test','1',NULL,1,NULL,NULL,'0','1'),('1112996757852516354','学生','stu','1102869621011156993',NULL,1,NULL,NULL,NULL,'1102869620780470273'),('1112996823203966978','教师','tea','1102869621011156993',NULL,1,NULL,NULL,NULL,'1102869620780470273'),('1118109427953844225','测试人员','role_test','1102869621011156993',NULL,1,NULL,NULL,'0','1102869620780470273'),('1141943878978695170','角色','role','0',NULL,0,'2019-06-21 05:40:12',NULL,'0','1141943878437629954'),('1141943879075164162','管理员','admin_system','1141943878978695170',NULL,1,'2019-06-21 05:40:12',NULL,'0','1141943878437629954'),('1141943879175827457','所有人','EVERYONE','1141943878978695170',NULL,2,'2019-06-21 05:40:12',NULL,'0','1141943878437629954'),('1141943879263907842','测试人员','role_test','1141943878978695170',NULL,3,'2019-06-21 05:40:12',NULL,'0','1141943878437629954'),('1142006269787521025','老师','Teacher','1141943878978695170',NULL,4,NULL,NULL,NULL,'1141943878437629954'),('1142006403963305985','学生','student','1141943878978695170',NULL,5,NULL,NULL,NULL,'1141943878437629954'),('1142008170394435586','student2','st','1141943878978695170',NULL,1,NULL,NULL,NULL,'1141943878437629954'),('1143071071247069186','所有人','EVERYONE','1143071071167377410',NULL,2,'2019-06-24 08:19:15',NULL,'0','1143071070919913473'),('1143071071209320449','管理員','admin_system','1143071071167377410',NULL,1,'2019-06-24 08:19:15',NULL,'0','1143071070919913473'),('1143071071167377410','角色','role','0',NULL,0,'2019-06-24 08:19:15',NULL,'0','1143071070919913473'),('1143071071289012226','測試人員','role_test','1143071071167377410',NULL,3,'2019-06-24 08:19:15',NULL,'0','1143071070919913473'),('1144153356293697537','角色','role','0',NULL,0,'2019-06-27 07:59:52',NULL,'0','1144153355832324098'),('1144153356365000705','管理员','admin_system','1144153356293697537',NULL,1,'2019-06-27 07:59:52',NULL,'0','1144153355832324098'),('1144153356448886786','所有人','EVERYONE','1144153356293697537',NULL,2,'2019-06-27 07:59:52',NULL,'0','1144153355832324098'),('1144153356528578561','测试人员','role_test','1144153356293697537',NULL,3,'2019-06-27 07:59:52',NULL,'0','1144153355832324098'),('1171362276786671618','角色','role','0',NULL,0,'2019-09-10 09:58:24',NULL,'0','1171362276082028545'),('1171362276883140609','管理员','admin_system','1171362276786671618',NULL,1,'2019-09-10 09:58:24',NULL,'0','1171362276082028545'),('1171362276971220994','所有人','EVERYONE','1171362276786671618',NULL,2,'2019-09-10 09:58:24',NULL,'0','1171362276082028545'),('1171362277055107074','测试人员','role_test','1171362276786671618',NULL,3,'2019-09-10 09:58:24',NULL,'0','1171362276082028545'),('1171362766643630082','角色','role','0',NULL,0,'2019-09-10 10:00:21',NULL,'0','1171362766052233218'),('1171362766752681986','管理員','admin_system','1171362766643630082',NULL,1,'2019-09-10 10:00:21',NULL,'0','1171362766052233218'),('1171362766857539586','所有人','EVERYONE','1171362766643630082',NULL,2,'2019-09-10 10:00:21',NULL,'0','1171362766052233218'),('1171362766970785793','測試人員','role_test','1171362766643630082',NULL,3,'2019-09-10 10:00:21',NULL,'0','1171362766052233218'),('1182466788775518209','管理员','admin_system','1182466788704215042',NULL,1,'2019-10-11 01:23:46',NULL,'0','1182466788184121346'),('1171363732822528002','普通員工','ptyg','1171362766643630082',NULL,5,NULL,NULL,NULL,'1171362766052233218'),('1182466788851015681','所有人','EVERYONE','1182466788704215042',NULL,2,'2019-10-11 01:23:46',NULL,'0','1182466788184121346'),('1182466788930707457','测试人员','role_test','1182466788704215042',NULL,3,'2019-10-11 01:23:46',NULL,'0','1182466788184121346'),('1182471133436928002','角色','role','0',NULL,0,'2019-10-11 01:41:02',NULL,'0','1182471133218824193'),('1182471133474676737','管理员','admin_system','1182471133436928002',NULL,1,'2019-10-11 01:41:02',NULL,'0','1182471133218824193'),('1182471133512425474','所有人','EVERYONE','1182471133436928002',NULL,2,'2019-10-11 01:41:02',NULL,'0','1182471133218824193'),('1182471133550174209','测试人员','role_test','1182471133436928002',NULL,3,'2019-10-11 01:41:02',NULL,'0','1182471133218824193'),('1182496965442215937','角色','role','0',NULL,0,'2019-10-11 03:23:40',NULL,'0','1182496965047951361'),('1182496965488353282','管理员','admin_system','1182496965442215937',NULL,1,'2019-10-11 03:23:40',NULL,'0','1182496965047951361'),('1182496965526102018','所有人','EVERYONE','1182496965442215937',NULL,2,'2019-10-11 03:23:40',NULL,'0','1182496965047951361'),('1182496965568045057','测试人员','role_test','1182496965442215937',NULL,3,'2019-10-11 03:23:40',NULL,'0','1182496965047951361'),('1200024176307048450','售前','s','1171362766643630082',NULL,1,NULL,NULL,NULL,'1171362766052233218'),('1200024279692447746','a','a','1200024176307048450',NULL,1,NULL,NULL,NULL,'1171362766052233218'),('1230044100924280833','role','role','0',NULL,0,'2020-02-19 08:19:01',NULL,'0','1230044093810741250'),('1230044101012361217','admin','admin_system','1230044100924280833',NULL,1,'2020-02-19 08:19:01',NULL,'0','1230044093810741250'),('1230044101125607425','everyone','EVERYONE','1230044100924280833',NULL,2,'2020-02-19 08:19:01',NULL,'0','1230044093810741250'),('1230044101326934018','test','role_test','1230044100924280833',NULL,3,'2020-02-19 08:19:01',NULL,'0','1230044093810741250'),('1230046910927605762','role','role','0',NULL,0,'2020-02-19 08:30:11',NULL,'0','1230046910524952578'),('1230046910982131713','admin','admin_system','1230046910927605762',NULL,1,'2020-02-19 08:30:11',NULL,'0','1230046910524952578'),('1230046911019880449','everyone','EVERYONE','1230046910927605762',NULL,2,'2020-02-19 08:30:11',NULL,'0','1230046910524952578'),('1230046911061823490','test','role_test','1230046910927605762',NULL,3,'2020-02-19 08:30:11',NULL,'0','1230046910524952578'),('1230047401686339585','角色','role','0',NULL,0,'2020-02-19 08:32:08',NULL,'0','1230047401363378178'),('1230047401749254145','管理員','admin_system','1230047401686339585',NULL,1,'2020-02-19 08:32:08',NULL,'0','1230047401363378178'),('1230047401795391489','所有人','EVERYONE','1230047401686339585',NULL,2,'2020-02-19 08:32:08',NULL,'0','1230047401363378178'),('1230047401849917441','測試人員','role_test','1230047401686339585',NULL,3,'2020-02-19 08:32:08',NULL,'0','1230047401363378178'),('1231239758544900097','技術人員','dod_engineer','1230047401686339585',NULL,1,NULL,NULL,NULL,'1230047401363378178'),('1232548885850230786','角色','role','0',NULL,0,'2020-02-26 06:12:08',NULL,'0','1232548884797460481'),('1232548885929922561','管理員','admin_system','1232548885850230786',NULL,1,'2020-02-26 06:12:08',NULL,'0','1232548884797460481'),('1232548885976059905','所有人','EVERYONE','1232548885850230786',NULL,2,'2020-02-26 06:12:08',NULL,'0','1232548884797460481'),('1232548886013808642','測試人員','role_test','1232548885850230786',NULL,3,'2020-02-26 06:12:08',NULL,'0','1232548884797460481'),('1232551249135013889','角色','role','0',NULL,0,'2020-02-26 06:21:32',NULL,'0','1232551248891744257'),('1232551249172762625','管理员','admin_system','1232551249135013889',NULL,1,'2020-02-26 06:21:32',NULL,'0','1232551248891744257'),('1232551249210511362','所有人','EVERYONE','1232551249135013889',NULL,2,'2020-02-26 06:21:32',NULL,'0','1232551248891744257'),('1232551249248260097','测试人员','role_test','1232551249135013889',NULL,3,'2020-02-26 06:21:32',NULL,'0','1232551248891744257'),('1232908565759926273','角色','role','0',NULL,0,'2020-02-27 06:01:23',NULL,'0','1232908565093031938'),('1232908565868978177','管理員','admin_system','1232908565759926273',NULL,1,'2020-02-27 06:01:23',NULL,'0','1232908565093031938'),('1232908565982224385','所有人','EVERYONE','1232908565759926273',NULL,2,'2020-02-27 06:01:23',NULL,'0','1232908565093031938'),('1232908566074499073','測試人員','role_test','1232908565759926273',NULL,3,'2020-02-27 06:01:23',NULL,'0','1232908565093031938'),('1265476609707184130','角色','role','0',NULL,0,'2020-05-27 02:55:09',NULL,'0','1265476599435333633'),('1265476609858179073','管理員','admin_system','1265476609707184130',NULL,1,'2020-05-27 02:55:09',NULL,'0','1265476599435333633'),('1265476609967230977','所有人','EVERYONE','1265476609707184130',NULL,2,'2020-05-27 02:55:09',NULL,'0','1265476599435333633'),('1265476610051117058','測試人員','role_test','1265476609707184130',NULL,3,'2020-05-27 02:55:09',NULL,'0','1265476599435333633'),('1265478530153156609','角色','role','0',NULL,0,'2020-05-27 03:02:47',NULL,'0','1265478529507233794'),('1265478530241236994','管理員','admin_system','1265478530153156609',NULL,1,'2020-05-27 03:02:47',NULL,'0','1265478529507233794'),('1265478530325123074','所有人','EVERYONE','1265478530153156609',NULL,2,'2020-05-27 03:02:47',NULL,'0','1265478529507233794'),('1265478530413203457','測試人員','role_test','1265478530153156609',NULL,3,'2020-05-27 03:02:47',NULL,'0','1265478529507233794'),('1265479178517057538','角色','role','0',NULL,0,'2020-05-27 03:05:22',NULL,'0','1265479177548173313'),('1265479178605137922','管理员','admin_system','1265479178517057538',NULL,1,'2020-05-27 03:05:22',NULL,'0','1265479177548173313'),('1265479178705801218','所有人','EVERYONE','1265479178517057538',NULL,2,'2020-05-27 03:05:22',NULL,'0','1265479177548173313'),('1265479178814853121','测试人员','role_test','1265479178517057538',NULL,3,'2020-05-27 03:05:22',NULL,'0','1265479177548173313'),('1278957992341532673','德昂内训教师','DOD001','1102869621011156993',NULL,6,NULL,NULL,NULL,'1102869620780470273'),('1278958082430988289','德昂内训学员','DOD002','1102869621011156993',NULL,7,NULL,NULL,NULL,'1102869620780470273'),('1279615461841547265','角色','role','0',NULL,0,'2020-07-05 03:17:54',NULL,'0','1279615460717473793'),('1279615462051262466','管理员','admin_system','1279615461841547265',NULL,1,'2020-07-05 03:17:54',NULL,'0','1279615460717473793'),('1279615462214840321','所有人','EVERYONE','1279615461841547265',NULL,2,'2020-07-05 03:17:54',NULL,'0','1279615460717473793'),('1279615462349058050','测试人员','role_test','1279615461841547265',NULL,3,'2020-07-05 03:17:54',NULL,'0','1279615460717473793'),('1279638543708827650','密训讲师','001','1279615461841547265',NULL,1,NULL,NULL,NULL,'1279615460717473793'),('1279638595554619394','密训学员','002','1279615461841547265',NULL,1,NULL,NULL,NULL,'1279615460717473793'),('1283701613288595457','教师试用','12','1102869621011156993',NULL,1,NULL,NULL,NULL,'1102869620780470273'),('1285869915502911489','lll','lll','1074503869703208961',NULL,1,NULL,NULL,NULL,'1'),('1285870910567981057','ff','ff','1285869915502911489',NULL,1,NULL,NULL,NULL,'1'),('1285870939701616641','fe','ee','1285870910567981057',NULL,1,NULL,NULL,NULL,'1'),('1285870981359443970','ww','ww','1285870939701616641',NULL,1,NULL,NULL,NULL,'1'),('1285871006823063554','rr亲亲亲亲亲亲亲亲亲亲亲亲亲亲亲','rrb','1285870981359443970',NULL,1,NULL,NULL,NULL,'1'),('1286173603685572609','培训学员','2','1279615461841547265',NULL,1,NULL,NULL,NULL,'1279615460717473793'),('1286591290148495361','佳伟课程','1','1279615461841547265',NULL,1,NULL,NULL,NULL,'1279615460717473793'),('1306074629498585090','角色','role','0',NULL,0,'2020-09-16 03:37:12',NULL,'0','1306074625434304513'),('1306074629695717378','管理员','admin_system','1306074629498585090',NULL,1,'2020-09-16 03:37:12',NULL,'0','1306074625434304513'),('1306074629871878146','所有人','EVERYONE','1306074629498585090',NULL,2,'2020-09-16 03:37:12',NULL,'0','1306074625434304513'),('1306074629930598401','测试人员','role_test','1306074629498585090',NULL,3,'2020-09-16 03:37:12',NULL,'0','1306074625434304513'),('1310453454823079938','远程实习','01','1279615461841547265',NULL,1,NULL,NULL,NULL,'1279615460717473793'),('1416944542441541634','填报管理员','admin_form','1',NULL,1,NULL,NULL,NULL,'1');

UNLOCK TABLES;

/*Table structure for table `t_role_permission` */

DROP TABLE IF EXISTS `t_role_permission`;

CREATE TABLE `t_role_permission` (
  `id` varchar(32) NOT NULL,
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  `permission_id` varchar(32) DEFAULT NULL COMMENT '权限id',
  `code` varchar(255) DEFAULT NULL COMMENT '角色编码',
  `create_time` datetime DEFAULT NULL COMMENT '授权时间',
  `creater` varchar(32) DEFAULT NULL COMMENT '授权人',
  `tenant_id` varchar(32) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色权限关联表';

/*Data for the table `t_role_permission` */

LOCK TABLES `t_role_permission` WRITE;

insert  into `t_role_permission`(`id`,`resource_id`,`role_id`,`permission_id`,`code`,`create_time`,`creater`,`tenant_id`) values ('1065450457606995969','1065129399192440834','3','3','view',NULL,NULL,'1'),('1065475756876972033','1050571891880472577','4','3','view',NULL,NULL,'1'),('1065498706690744322','1055661719364423681','2','3','view',NULL,NULL,'1'),('1065499293545177090','1062283595385495554','2','3','view',NULL,NULL,'1'),('1075761545825243138','1073576650801758210','1074675379281620994','2','view',NULL,NULL,'1'),('1075764887498543105','1064469370349142018','3','1','view',NULL,NULL,'1'),('1075764891990642690','1064469435402797058','3','1','view',NULL,NULL,'1'),('1075955819938336769','1075955649481822209','1074675379281620994','1','view',NULL,NULL,'1'),('1078184413037084673','1064464184058691585','1074675379281620994','1','view',NULL,NULL,'1'),('1083602094485782530','1073582214285967362','1074675379281620994','10408049924679148002','log',NULL,NULL,'1'),('1083602094502559745','1073582214285967362','1074675379281620994','2','view',NULL,NULL,'1'),('1083602102735978498','1073582214285967362','1074675379281620994','10408049924679148002','log',NULL,NULL,'1'),('1119176888363016194','1064469370349142018','6','1','view',NULL,NULL,'1'),('1119176888371404801','1064469370349142018','6','10408049924679148001','log',NULL,NULL,'1'),('1119176888371404802','1064469370349142018','6','245040849338109952','permission',NULL,NULL,'1'),('1119176892322439169','1064469435402797058','6','1','view',NULL,NULL,'1'),('1119176892322439170','1064469435402797058','6','10408049924679148001','log',NULL,NULL,'1'),('1119176892322439171','1064469435402797058','6','245040849338109952','permission',NULL,NULL,'1'),('1133663861085581313','1133652599098322945','3','1','view','2019-05-29 09:18:22','1068339153872150529','1'),('1136476156509577218','1133652599098322945','1074503869703208961','1','view',NULL,NULL,'1'),('1136476156517965825','1133652599098322945','1074503869703208961','10408049924679148001','log',NULL,NULL,'1'),('1136476156517965826','1133652599098322945','1074503869703208961','245040849338109952','permission',NULL,NULL,'1'),('1136483405873307650','1064475233273102338','3','1','view',NULL,NULL,'1'),('1136483405881696258','1064475233273102338','3','10408049924679148001','log',NULL,NULL,'1'),('1136483405881696259','1064475233273102338','3','245040849338109952','permission',NULL,NULL,'1'),('1136484114878455809','1050572011216809986','3','1','view',NULL,NULL,'1'),('1136484114886844418','1050572011216809986','3','10408049924679148001','log',NULL,NULL,'1'),('1136484114886844419','1050572011216809986','3','245040849338109952','permission',NULL,NULL,'1'),('1136484117931909122','1054602708361367554','3','1','view',NULL,NULL,'1'),('1136484117940297729','1054602708361367554','3','10408049924679148001','log',NULL,NULL,'1'),('1136484117940297730','1054602708361367554','3','245040849338109952','permission',NULL,NULL,'1'),('1136484120779841539','1055661719364423681','3','10408049924679148001','log',NULL,NULL,'1'),('1136484120779841540','1055661719364423681','3','245040849338109952','permission',NULL,NULL,'1'),('1136484124160450561','1062283595385495554','3','1','view',NULL,NULL,'1'),('1136484124168839170','1062283595385495554','3','10408049924679148001','log',NULL,NULL,'1'),('1136484124168839171','1062283595385495554','3','245040849338109952','permission',NULL,NULL,'1'),('1136484127570419714','1064463981406699522','3','1','view',NULL,NULL,'1'),('1136484127570419715','1064463981406699522','3','10408049924679148001','log',NULL,NULL,'1'),('1136484127570419716','1064463981406699522','3','245040849338109952','permission',NULL,NULL,'1'),('1136484131517259777','1064464184058691585','3','1','view',NULL,NULL,'1'),('1136484131517259778','1064464184058691585','3','10408049924679148001','log',NULL,NULL,'1'),('1136484131517259779','1064464184058691585','3','245040849338109952','permission',NULL,NULL,'1'),('1136484157844905986','1064826764988104705','3','1','view',NULL,NULL,'1'),('1136484157853294593','1064826764988104705','3','10408049924679148001','log',NULL,NULL,'1'),('1136484157853294594','1064826764988104705','3','245040849338109952','permission',NULL,NULL,'1'),('1136484164161527810','1069777342193270786','3','1','view',NULL,NULL,'1'),('1136484164169916418','1069777342193270786','3','10408049924679148001','log',NULL,NULL,'1'),('1136484164169916419','1069777342193270786','3','245040849338109952','permission',NULL,NULL,'1'),('1136484168427134978','1070853405942022146','3','1','view',NULL,NULL,'1'),('1136484168427134979','1070853405942022146','3','10408049924679148001','log',NULL,NULL,'1'),('1136484168427134980','1070853405942022146','3','245040849338109952','permission',NULL,NULL,'1'),('1136484177767849986','1071954344129777666','3','1','view',NULL,NULL,'1'),('1136484177776238594','1071954344129777666','3','10408049924679148001','log',NULL,NULL,'1'),('1136484177776238595','1071954344129777666','3','245040849338109952','permission',NULL,NULL,'1'),('1136484181127487490','1071954504381550593','3','1','view',NULL,NULL,'1'),('1136484181135876097','1071954504381550593','3','10408049924679148001','log',NULL,NULL,'1'),('1136484181135876098','1071954504381550593','3','245040849338109952','permission',NULL,NULL,'1'),('1136484183937671169','1071954658257981441','3','1','view',NULL,NULL,'1'),('1136484183946059778','1071954658257981441','3','10408049924679148001','log',NULL,NULL,'1'),('1136484183946059779','1071954658257981441','3','245040849338109952','permission',NULL,NULL,'1'),('1136484186542333954','1071954765858656257','3','1','view',NULL,NULL,'1'),('1136484186542333955','1071954765858656257','3','10408049924679148001','log',NULL,NULL,'1'),('1136484186542333956','1071954765858656257','3','245040849338109952','permission',NULL,NULL,'1'),('1136484189457375234','1072445122713583618','3','1','view',NULL,NULL,'1'),('1136484189465763842','1072445122713583618','3','10408049924679148001','log',NULL,NULL,'1'),('1136484189465763843','1072445122713583618','3','245040849338109952','permission',NULL,NULL,'1'),('1136484215424311298','1074616855952437250','3','1','view',NULL,NULL,'1'),('1136484215424311299','1074616855952437250','3','10408049924679148001','log',NULL,NULL,'1'),('1136484215424311300','1074616855952437250','3','245040849338109952','permission',NULL,NULL,'1'),('1136484220453281794','1075234002399424513','3','1','view',NULL,NULL,'1'),('1136484220453281795','1075234002399424513','3','10408049924679148001','log',NULL,NULL,'1'),('1136484220453281796','1075234002399424513','3','245040849338109952','permission',NULL,NULL,'1'),('1136484226480496642','1075262650833727490','3','1','view',NULL,NULL,'1'),('1136484226480496643','1075262650833727490','3','10408049924679148001','log',NULL,NULL,'1'),('1136484226488885250','1075262650833727490','3','245040849338109952','permission',NULL,NULL,'1'),('1136484229856911361','1075297887101915138','3','1','view',NULL,NULL,'1'),('1136484229856911362','1075297887101915138','3','10408049924679148001','log',NULL,NULL,'1'),('1136484229856911363','1075297887101915138','3','245040849338109952','permission',NULL,NULL,'1'),('1136484232239276033','1075298052449767425','3','1','view',NULL,NULL,'1'),('1136484232239276034','1075298052449767425','3','10408049924679148001','log',NULL,NULL,'1'),('1136484232239276035','1075298052449767425','3','245040849338109952','permission',NULL,NULL,'1'),('1136484235158511617','1075955649481822209','3','1','view',NULL,NULL,'1'),('1136484235166900225','1075955649481822209','3','10408049924679148001','log',NULL,NULL,'1'),('1136484235166900226','1075955649481822209','3','245040849338109952','permission',NULL,NULL,'1'),('1136484306742697985','28','3','1','view',NULL,NULL,'1'),('1136484306751086594','28','3','10408049924679148001','log',NULL,NULL,'1'),('1136484306751086595','28','3','245040849338109952','permission',NULL,NULL,'1'),('1136484325696757761','31','3','1','view',NULL,NULL,'1'),('1136484325696757762','31','3','10408049924679148001','log',NULL,NULL,'1'),('1136484325696757763','31','3','245040849338109952','permission',NULL,NULL,'1'),('1136484328615993345','36','3','1','view',NULL,NULL,'1'),('1136484328624381953','36','3','10408049924679148001','log',NULL,NULL,'1'),('1136484328624381954','36','3','245040849338109952','permission',NULL,NULL,'1'),('1136484331111604226','4','3','1','view',NULL,NULL,'1'),('1136484331119992834','4','3','10408049924679148001','log',NULL,NULL,'1'),('1136484331119992835','4','3','245040849338109952','permission',NULL,NULL,'1'),('1136484333049372674','5','3','1','view',NULL,NULL,'1'),('1136484333049372675','5','3','10408049924679148001','log',NULL,NULL,'1'),('1136484333049372676','5','3','245040849338109952','permission',NULL,NULL,'1'),('1136484334949392385','6','3','1','view',NULL,NULL,'1'),('1136484334957780994','6','3','10408049924679148001','log',NULL,NULL,'1'),('1136484334957780995','6','3','245040849338109952','permission',NULL,NULL,'1'),('1136515027410640898','1064469435402797058','1074503869703208961','1','view',NULL,NULL,'1'),('1136550435213303809','1064468887446339585','3','1','view',NULL,NULL,'1'),('1136550435221692418','1064468887446339585','3','10408049924679148001','log',NULL,NULL,'1'),('1136550435221692419','1064468887446339585','3','245040849338109952','permission',NULL,NULL,'1'),('1137901178096738306','1050571891880472577','1073594031590686721','1','view',NULL,NULL,'1'),('1137901178105126913','1050571891880472577','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901178105126914','1050571891880472577','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901186489540610','1050572011216809986','1073594031590686721','1','view',NULL,NULL,'1'),('1137901186489540611','1050572011216809986','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901186489540612','1050572011216809986','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901189786263554','1054602708361367554','1073594031590686721','1','view',NULL,NULL,'1'),('1137901189786263555','1054602708361367554','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901189794652161','1054602708361367554','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901193565331457','1055661719364423681','1073594031590686721','1','view',NULL,NULL,'1'),('1137901193573720066','1055661719364423681','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901193573720067','1055661719364423681','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901197340205058','1062283595385495554','1073594031590686721','1','view',NULL,NULL,'1'),('1137901197340205059','1062283595385495554','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901197340205060','1062283595385495554','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901201639366658','1064463981406699522','1073594031590686721','1','view',NULL,NULL,'1'),('1137901201647755265','1064463981406699522','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901201647755266','1064463981406699522','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901205221302274','1064464184058691585','1073594031590686721','1','view',NULL,NULL,'1'),('1137901205221302275','1064464184058691585','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901205221302276','1064464184058691585','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901218617909250','1064468887446339585','1073594031590686721','1','view',NULL,NULL,'1'),('1137901218626297857','1064468887446339585','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901218626297858','1064468887446339585','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901240801583105','1064475233273102338','1073594031590686721','1','view',NULL,NULL,'1'),('1137901240809971713','1064475233273102338','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901240809971714','1064475233273102338','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901249357963266','1064826764988104705','1073594031590686721','1','view',NULL,NULL,'1'),('1137901249357963267','1064826764988104705','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901249357963268','1064826764988104705','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901256333090818','1069777342193270786','1073594031590686721','1','view',NULL,NULL,'1'),('1137901256341479425','1069777342193270786','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901256341479426','1069777342193270786','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901260237987841','1070853405942022146','1073594031590686721','1','view',NULL,NULL,'1'),('1137901260237987842','1070853405942022146','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901260237987843','1070853405942022146','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901271231258626','1071954344129777666','1073594031590686721','1','view',NULL,NULL,'1'),('1137901271239647234','1071954344129777666','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901271239647235','1071954344129777666','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901281788321793','1071954504381550593','1073594031590686721','1','view',NULL,NULL,'1'),('1137901281788321794','1071954504381550593','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901281788321795','1071954504381550593','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901288985747458','1071954658257981441','1073594031590686721','1','view',NULL,NULL,'1'),('1137901288985747459','1071954658257981441','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901288985747460','1071954658257981441','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901294211850242','1071954765858656257','1073594031590686721','1','view',NULL,NULL,'1'),('1137901294211850243','1071954765858656257','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901294211850244','1071954765858656257','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901306304028673','1072445122713583618','1073594031590686721','1','view',NULL,NULL,'1'),('1137901306304028674','1072445122713583618','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901306304028675','1072445122713583618','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901319281205250','1074616855952437250','1073594031590686721','1','view',NULL,NULL,'1'),('1137901319281205251','1074616855952437250','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901319281205252','1074616855952437250','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901323475509250','1075234002399424513','1073594031590686721','1','view',NULL,NULL,'1'),('1137901323475509251','1075234002399424513','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901323475509252','1075234002399424513','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901330815541250','1075262650833727490','1073594031590686721','1','view',NULL,NULL,'1'),('1137901330815541251','1075262650833727490','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901330815541252','1075262650833727490','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901334745604098','1075297887101915138','1073594031590686721','1','view',NULL,NULL,'1'),('1137901334745604099','1075297887101915138','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901334745604100','1075297887101915138','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901360238583809','1075298052449767425','1073594031590686721','1','view',NULL,NULL,'1'),('1137901360238583810','1075298052449767425','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901360238583811','1075298052449767425','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901376428597250','1075955649481822209','1073594031590686721','1','view',NULL,NULL,'1'),('1137901376428597251','1075955649481822209','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901376428597252','1075955649481822209','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901526832144385','28','1073594031590686721','1','view',NULL,NULL,'1'),('1137901526840532994','28','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901526840532995','28','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901532272156673','31','1073594031590686721','1','view',NULL,NULL,'1'),('1137901532272156674','31','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901532272156675','31','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901553730215937','36','1073594031590686721','1','view',NULL,NULL,'1'),('1137901553738604545','36','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901553738604546','36','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901563502944258','4','1073594031590686721','1','view',NULL,NULL,'1'),('1137901563502944259','4','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901563502944260','4','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901568976510978','5','1073594031590686721','1','view',NULL,NULL,'1'),('1137901568984899586','5','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901568984899587','5','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1137901572185153538','6','1073594031590686721','1','view',NULL,NULL,'1'),('1137901572193542146','6','1073594031590686721','10408049924679148001','log',NULL,NULL,'1'),('1137901572193542147','6','1073594031590686721','245040849338109952','permission',NULL,NULL,'1'),('1149619059679322114','1133652599098322945','1074503869703208961','1140538857262886914','comment','2019-07-12 09:58:37','1068339153872150529','1'),('1149619062879576066','1133652599098322945','1074503869703208961','1140538857002840065','share','2019-07-12 09:58:38','1068339153872150529','1'),('1158569158031740929','1064464184058691585','1074503869703208961','1','view','2019-08-06 02:43:07','1068339153872150529','1'),('1158569164109287425','1064464184058691585','1074503869703208961','1140538857262886914','comment','2019-08-06 02:43:08','1068339153872150529','1'),('1163337899294613506','4','3','1140538857262886914','comment','2019-08-19 06:32:24','1067370861682520065','1'),('1136484120779841538','1055661719364423681','3','1','view','2020-12-11 10:35:51','1067371912724770817','1'),('1364123742814969858','1064468887446339585','3','1140538857002840065','share','2021-02-23 08:03:39','1067371912724770817','1'),('1367305658540019714','1064469370349142018','6','1140538857262886914','comment','2021-03-04 02:47:26','1067371912724770817','1'),('1368753590996840449','1064468887446339585','3','052cf2af7d9e11eb9ad','subscribe','2021-03-08 02:51:40','1318743103500828673','1'),('1368759779591729154','1064469370349142018','3','052cf2af7d9e11eb9ad','subscribe','2021-03-08 03:05:36','1067371912724770817','1'),('1368759794104020993','1050572011216809986','3','052cf2af7d9e11eb9ad','subscribe','2021-03-08 03:05:39','1067371912724770817','1'),('1391593929910628354','1064464184058691585','3','1140538857262886914','comment','2021-05-10 03:20:22','1067371912724770817','1'),('1410074823675719681','1064469435402797058','1073594031590686721','1','view','2021-06-30 03:23:52','1380028906788724737','1'),('1410077256799858689','1064469435402797058','1285871006823063554','1','view','2021-06-30 03:26:30','1380028906788724737','1');

UNLOCK TABLES;

/*Table structure for table `t_role_user` */

DROP TABLE IF EXISTS `t_role_user`;

CREATE TABLE `t_role_user` (
  `id` varchar(32) NOT NULL,
  `role_id` varchar(32) DEFAULT NULL COMMENT '角色id',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `role_code` varchar(255) DEFAULT NULL COMMENT '角色编码',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色人员关联表';

/*Data for the table `t_role_user` */

LOCK TABLES `t_role_user` WRITE;

insert  into `t_role_user`(`id`,`role_id`,`user_id`,`role_code`,`tenant_id`) values ('1080340226041966593','4','1','role_everyone','1'),('14','1','1','role','1'),('15','3','1','admin_super','1');

UNLOCK TABLES;

/*Table structure for table `t_service_state` */

DROP TABLE IF EXISTS `t_service_state`;

CREATE TABLE `t_service_state` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '服务名称',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  `port` varchar(255) DEFAULT NULL COMMENT '端口',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `state` int(1) DEFAULT '1' COMMENT '状态',
  `remarks` varchar(255) DEFAULT NULL COMMENT '备注',
  `refresh_time` int(11) DEFAULT NULL COMMENT '刷新时间(单位:分)',
  `record_type` int(1) DEFAULT NULL COMMENT '报告记录方式',
  `retention_time` int(11) DEFAULT NULL COMMENT '记录保留时间(单位:天)',
  `mail_frequency` int(11) DEFAULT NULL COMMENT '发送邮件频率',
  `mail_count` int(11) DEFAULT NULL COMMENT '发送邮件次数',
  `is_sent_mail` int(11) DEFAULT '0' COMMENT '异常时是否发送过邮件：0.未发送  1.已发送',
  `tenant_id` int(11) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_service_state` */

LOCK TABLES `t_service_state` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_service_state_record` */

DROP TABLE IF EXISTS `t_service_state_record`;

CREATE TABLE `t_service_state_record` (
  `service_id` varchar(32) DEFAULT NULL COMMENT '服务监控id',
  `create_time` datetime DEFAULT NULL COMMENT '服务监控时间',
  `service_state` int(1) DEFAULT NULL COMMENT '服务状态',
  `tenant_id` int(11) DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_service_state_record` */

LOCK TABLES `t_service_state_record` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_sys_log` */

DROP TABLE IF EXISTS `t_sys_log`;

CREATE TABLE `t_sys_log` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `table_id` int(11) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '操作类型：1增2删3改',
  `method` varchar(255) DEFAULT NULL COMMENT '操作方法',
  `normal` int(11) DEFAULT NULL COMMENT '正常状态',
  `ip` varchar(255) DEFAULT NULL COMMENT '创建人的ip',
  `operation` varchar(255) DEFAULT NULL COMMENT '操作',
  `organization` varchar(255) DEFAULT NULL COMMENT '创建人组织',
  `content` varchar(1000) DEFAULT NULL COMMENT '操作内容',
  `resource_type` varchar(32) DEFAULT NULL COMMENT '资源类型id',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源ID',
  `return_result` varchar(1000) DEFAULT NULL COMMENT '返回结果/异常信息',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统日志表';

/*Data for the table `t_sys_log` */

LOCK TABLES `t_sys_log` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_sys_logo` */

DROP TABLE IF EXISTS `t_sys_logo`;

CREATE TABLE `t_sys_logo` (
  `id` varchar(32) NOT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT 'Logo名称',
  `file_id` varchar(32) DEFAULT NULL COMMENT '文件id',
  `path_url` varchar(255) DEFAULT NULL COMMENT '文件地址',
  `creater` varchar(32) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `is_enable` int(5) DEFAULT NULL COMMENT '是否启用。只能启用一个',
  `introduce` varchar(255) DEFAULT NULL COMMENT '介绍',
  `type` varchar(255) DEFAULT NULL COMMENT 'LOGO类型：login登录页（展示位置）；main 内页',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统logo设置';

/*Data for the table `t_sys_logo` */

LOCK TABLES `t_sys_logo` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_sys_release` */

DROP TABLE IF EXISTS `t_sys_release`;

CREATE TABLE `t_sys_release` (
  `id` varchar(32) NOT NULL COMMENT '主键',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `publisher` varchar(50) DEFAULT NULL COMMENT '发布人',
  `release_time` datetime DEFAULT NULL COMMENT '发布时间',
  `release_version` varchar(255) DEFAULT NULL COMMENT '发布版本',
  `content` varchar(640) DEFAULT NULL COMMENT '发布内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发布记录表';

/*Data for the table `t_sys_release` */

LOCK TABLES `t_sys_release` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_tenant` */

DROP TABLE IF EXISTS `t_tenant`;

CREATE TABLE `t_tenant` (
  `id` varchar(32) NOT NULL COMMENT '租户ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `tel` varchar(255) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  `customer` varchar(255) DEFAULT NULL COMMENT '联系人',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `manager` varchar(255) DEFAULT NULL,
  `is_delete` int(11) NOT NULL DEFAULT '1' COMMENT '0：删除  1：存在',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='租户表';

/*Data for the table `t_tenant` */

LOCK TABLES `t_tenant` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_third_app_param` */

DROP TABLE IF EXISTS `t_third_app_param`;

CREATE TABLE `t_third_app_param` (
  `id` varchar(255) NOT NULL COMMENT '主键',
  `app_name` varchar(255) DEFAULT NULL COMMENT '集成的第三方应用名称',
  `app_type` varchar(255) DEFAULT NULL COMMENT '第三方类型（例如：wechat_cp,line，dingding等）',
  `app_id` varchar(255) DEFAULT NULL COMMENT '应用唯一标识id（微信，line，钉钉 等这些应用都有唯一标识用用id）',
  `app_secret` varchar(255) DEFAULT NULL COMMENT '应用密钥（line、微信、钉钉 集成中需要用到参数）',
  `app_token` varchar(255) DEFAULT NULL COMMENT '应用token（line、微信cp 集成中用到）',
  `app_aeskey` varchar(255) DEFAULT NULL COMMENT '应用AES编码key （微信cp 集成中使用）',
  `app_corpid` varchar(255) DEFAULT NULL COMMENT '应用企业ID （微信cp 、 钉钉 中集成使用）',
  `callback_url` varchar(3600) DEFAULT NULL COMMENT '回调链接头',
  `relation_param` varchar(255) DEFAULT NULL COMMENT '关系参数Mobile（应用手机号关联）-与phone；UserID 应用用户id关联-与username',
  `logo_url` varchar(3600) DEFAULT NULL COMMENT '应用图标URL 。从应用中读取。如果没有每种类型都有默认URL',
  `is_enable` int(1) DEFAULT '0' COMMENT '是否启用（1,：启用。0：禁用）',
  `creater` varchar(255) DEFAULT NULL COMMENT '创建人id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `updater` varchar(255) DEFAULT NULL COMMENT '修改人id',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `tenant_id` varchar(255) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='第三方办公app集成（目前特指：微信、line、钉钉。）';

/*Data for the table `t_third_app_param` */

LOCK TABLES `t_third_app_param` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_user` */

DROP TABLE IF EXISTS `t_user`;

CREATE TABLE `t_user` (
  `id` varchar(32) NOT NULL COMMENT '用户ID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `PASSWORD` char(32) DEFAULT NULL COMMENT '密码',
  `register_time` datetime DEFAULT NULL COMMENT '注册时间',
  `register_ip` varchar(50) DEFAULT NULL COMMENT '注册IP',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `last_login_ip` varchar(50) DEFAULT '' COMMENT '最后登录IP',
  `login_count` int(11) DEFAULT '0' COMMENT '登录次数',
  `reset_key` char(32) DEFAULT '0' COMMENT '0:第一次登录 1:重置密码 2:已修改密码',
  `reset_pwd` varchar(10) DEFAULT NULL COMMENT '重置密码VALUE',
  `reset_key_lasttime` datetime DEFAULT NULL COMMENT '最后一次修改密码的时间',
  `error_time` datetime DEFAULT NULL COMMENT '登录错误时间',
  `error_count` int(11) DEFAULT '0' COMMENT '登录错误次数',
  `error_ip` varchar(50) DEFAULT NULL COMMENT '登录错误IP',
  `activation` tinyint(1) DEFAULT '1' COMMENT '激活状态',
  `activation_code` char(32) DEFAULT NULL COMMENT '激活码',
  `wx_openid` varchar(255) DEFAULT NULL COMMENT '微信openid',
  `state` int(255) DEFAULT '0' COMMENT '状态：0正常1禁用',
  `activate_start_time` datetime DEFAULT NULL COMMENT '有效期开始',
  `activate_end_time` datetime DEFAULT NULL COMMENT '有效期截止',
  `is_delete` int(4) DEFAULT '0' COMMENT '是否删除0否',
  `pwd_expire_time` datetime DEFAULT NULL COMMENT '密码过期时间',
  `status` int(11) DEFAULT '1' COMMENT '用户类型 1：数窗用户   2：域用户',
  `ext_id` varchar(32) DEFAULT NULL COMMENT '外部关联标识',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账户表';

/*Data for the table `t_user` */

LOCK TABLES `t_user` WRITE;

insert  into `t_user`(`id`,`username`,`PASSWORD`,`register_time`,`register_ip`,`last_login_time`,`last_login_ip`,`login_count`,`reset_key`,`reset_pwd`,`reset_key_lasttime`,`error_time`,`error_count`,`error_ip`,`activation`,`activation_code`,`wx_openid`,`state`,`activate_start_time`,`activate_end_time`,`is_delete`,`pwd_expire_time`,`status`,`ext_id`,`tenant_id`) values ('1','admin','e10adc3949ba59abbe56e057f20f883e',NULL,NULL,'2021-10-07 16:01:30','169.254.41.135',51,'1',NULL,'2021-03-12 11:24:58',NULL,0,NULL,1,NULL,'001',0,NULL,NULL,0,NULL,1,NULL,'1');

UNLOCK TABLES;

/*Table structure for table `t_user_collect` */

DROP TABLE IF EXISTS `t_user_collect`;

CREATE TABLE `t_user_collect` (
  `id` varchar(32) NOT NULL COMMENT 'id',
  `parent_id` varchar(32) NOT NULL DEFAULT '0' COMMENT '父级id',
  `user_id` varchar(32) NOT NULL COMMENT '用户ID',
  `collect_name` varchar(240) DEFAULT NULL COMMENT '名称（文件夹、报表、课件）',
  `collect_type` varchar(32) DEFAULT NULL COMMENT '文件类型（文件夹-folder，报表-report，课件-course，案例-case）',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '报表id',
  `col_create_time` datetime DEFAULT NULL COMMENT '收藏时间',
  `col_sort` int(4) DEFAULT NULL COMMENT '排序',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户收藏表';

/*Data for the table `t_user_collect` */

LOCK TABLES `t_user_collect` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_user_data_permission` */

DROP TABLE IF EXISTS `t_user_data_permission`;

CREATE TABLE `t_user_data_permission` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `dp_id` varchar(32) DEFAULT NULL COMMENT '数据权限id',
  `state` int(3) DEFAULT NULL COMMENT '状态，是否组织权限。组织权限共有，用户权限特有',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user_data_permission` */

LOCK TABLES `t_user_data_permission` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_user_info` */

DROP TABLE IF EXISTS `t_user_info`;

CREATE TABLE `t_user_info` (
  `id` varchar(32) NOT NULL,
  `org_code` varchar(255) DEFAULT NULL COMMENT '组织机构代码',
  `realname` varchar(50) DEFAULT NULL COMMENT '姓名',
  `nickname` varchar(100) DEFAULT NULL COMMENT '昵称',
  `gender` int(4) DEFAULT '1' COMMENT '性别:1-男 2-女',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `intro` varchar(255) DEFAULT NULL COMMENT '个人介绍',
  `department` varchar(150) DEFAULT NULL COMMENT '来自:中国',
  `qq` varchar(100) DEFAULT NULL COMMENT 'QQ',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) DEFAULT NULL COMMENT '电话',
  `avatar` mediumtext COMMENT '头像',
  `signature` varchar(255) DEFAULT NULL COMMENT '用户个性签名',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `code` varchar(255) DEFAULT NULL COMMENT '用户编号',
  `secret_key` varchar(50) DEFAULT NULL COMMENT '忘记密码时随机生成的key',
  `mstr_user` int(11) DEFAULT '0' COMMENT '是否MSTR用户',
  `out_date` datetime DEFAULT NULL COMMENT '忘记密码时过期时间',
  `locale` varchar(255) DEFAULT NULL COMMENT '语言',
  `org_id` varchar(32) DEFAULT NULL COMMENT '机构id',
  `preference_type` varchar(255) DEFAULT NULL COMMENT '首选项类型 orgHome：组织首页；favorites：收藏夹；catalogue：目录；docObject',
  `preference_value` varchar(255) DEFAULT NULL COMMENT '当选择目录和文档对象时记录用户勾选的文档或者目录的id',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户信息表';

/*Data for the table `t_user_info` */

LOCK TABLES `t_user_info` WRITE;

insert  into `t_user_info`(`id`,`org_code`,`realname`,`nickname`,`gender`,`birthday`,`intro`,`department`,`qq`,`email`,`phone`,`avatar`,`signature`,`address`,`code`,`secret_key`,`mstr_user`,`out_date`,`locale`,`org_id`,`preference_type`,`preference_value`,`tenant_id`) values ('1','001','admin','admin',1,NULL,NULL,'',NULL,NULL,'','data:image/png;base64,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','',NULL,NULL,NULL,0,NULL,'zh_CN','1',NULL,NULL,'1');

UNLOCK TABLES;

/*Table structure for table `t_user_module` */

DROP TABLE IF EXISTS `t_user_module`;

CREATE TABLE `t_user_module` (
  `user_id` varchar(32) DEFAULT NULL,
  `content_id` varchar(32) DEFAULT NULL COMMENT '面板内容id',
  `tenant_id` varchar(32) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户模板表';

/*Data for the table `t_user_module` */

LOCK TABLES `t_user_module` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_user_organization` */

DROP TABLE IF EXISTS `t_user_organization`;

CREATE TABLE `t_user_organization` (
  `id` varchar(32) NOT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `org_id` varchar(32) DEFAULT NULL COMMENT '组织id',
  `is_deputy` int(1) DEFAULT NULL COMMENT '是否兼职',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user_organization` */

LOCK TABLES `t_user_organization` WRITE;

insert  into `t_user_organization`(`id`,`user_id`,`org_id`,`is_deputy`,`tenant_id`) values ('e9bf925982dd11eb9ad','1','1',0,'1');

UNLOCK TABLES;

/*Table structure for table `t_user_record` */

DROP TABLE IF EXISTS `t_user_record`;

CREATE TABLE `t_user_record` (
  `id` varchar(32) NOT NULL,
  `type` int(255) DEFAULT NULL COMMENT '类型：1收藏2历史纪录',
  `owner` varchar(32) DEFAULT NULL COMMENT '所属人',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '菜单id',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `url` varchar(255) DEFAULT NULL COMMENT '地址',
  `params` varchar(255) DEFAULT NULL COMMENT '参数',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `tenant_id` varchar(32) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户历史记录';

/*Data for the table `t_user_record` */

LOCK TABLES `t_user_record` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_user_setting` */

DROP TABLE IF EXISTS `t_user_setting`;

CREATE TABLE `t_user_setting` (
  `id` varchar(255) NOT NULL,
  `user_id` varchar(255) DEFAULT NULL COMMENT '用户id',
  `naviga_style` varchar(255) DEFAULT NULL COMMENT '导航风格',
  `theme_style` varchar(255) DEFAULT NULL COMMENT '主题风格',
  `system_msg` int(11) NOT NULL DEFAULT '0' COMMENT '是否发送系统消息',
  `comment_msg` int(11) NOT NULL DEFAULT '0' COMMENT '是否发送评论消息',
  `notice_msg` int(11) NOT NULL DEFAULT '0' COMMENT '是否发送通知消息',
  `issue_msg` int(11) NOT NULL DEFAULT '0' COMMENT '是否发送问题回复消息',
  `tenant_id` varchar(255) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `t_user_setting` */

LOCK TABLES `t_user_setting` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_user_wx_work` */

DROP TABLE IF EXISTS `t_user_wx_work`;

CREATE TABLE `t_user_wx_work` (
  `id` varchar(32) NOT NULL,
  `open_id` varchar(50) DEFAULT NULL,
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `app_id` varchar(32) DEFAULT NULL COMMENT '第三方集成应用的id',
  `agent_id` int(11) DEFAULT NULL,
  `wx_userid` varchar(64) DEFAULT NULL COMMENT '成员UserID',
  `name` varchar(255) DEFAULT NULL COMMENT '成员名称',
  `mobile` varchar(255) DEFAULT NULL COMMENT '通讯录手机号码',
  `department` varchar(255) DEFAULT NULL COMMENT '成员所属部门id列表，仅返回该应用有查看权限的部门id',
  `sort` varchar(255) DEFAULT NULL COMMENT '部门内的排序值，默认为0。数量必须和department一致，数值越大排序越前面。值范围是[0, 2^32)',
  `position` varchar(255) DEFAULT NULL COMMENT '职务信息；第三方仅通讯录应用可获取',
  `gender` varchar(255) DEFAULT NULL COMMENT '性别。0表示未定义，1表示男性，2表示女性',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱，第三方仅通讯录应用可获取',
  `is_leader_in_dept` varchar(255) DEFAULT NULL COMMENT '表示在所在的部门内是否为上级。；第三方仅通讯录应用可获取',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像url。注：如果要获取小图将url最后的”/0”改成”/100”即可。第三方仅通讯录应用可获取',
  `telephone` varchar(255) DEFAULT NULL COMMENT '座机。第三方仅通讯录应用可获取',
  `is_enable` varchar(255) DEFAULT NULL COMMENT '成员启用状态。1表示启用的成员，0表示被禁用。注意，服务商调用接口不会返回此字段',
  `alias` varchar(255) DEFAULT NULL COMMENT '别名；第三方仅通讯录应用可获取',
  `extattr` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL COMMENT '激活状态: 1=已激活，2=已禁用，4=未激活。',
  `qr_code` varchar(255) DEFAULT NULL COMMENT '员工个人二维码，扫描可添加为外部联系人；第三方仅通讯录应用可获取',
  `external_profile` varchar(255) DEFAULT NULL COMMENT '成员对外属性，字段详情见对外属性；第三方仅通讯录应用可获取',
  `external_position` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='在通讯录同步助手中此接口可以读取企业通讯录的所有成员信息，而自建应用可以读取该应用设置的可见范围内的成员信息。\r\n\r\n请求方式：GET（HTTPS）\r\n请求地址：https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID';

/*Data for the table `t_user_wx_work` */

LOCK TABLES `t_user_wx_work` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_wx_push` */

DROP TABLE IF EXISTS `t_wx_push`;

CREATE TABLE `t_wx_push` (
  `id` varchar(32) NOT NULL,
  `agent_id` int(11) DEFAULT NULL,
  `app_id` varchar(32) DEFAULT NULL COMMENT '当创建第三方应用任务时关联的应用id（t_third_app_param表的id）',
  `type` varchar(32) DEFAULT NULL COMMENT '推送类型',
  `task_type` varchar(32) DEFAULT NULL COMMENT '任务类型(推送任务、执行脚本）',
  `push_type` varchar(32) DEFAULT NULL COMMENT '推送方式(站内信，邮件，微信)',
  `create_type` int(11) DEFAULT NULL COMMENT '创建类型（表示任务创建的类型 1、普通创建（普通用户或者管理在“我的”中创建的）  2、管理员创建（管理员用户在管理系统中创建的）',
  `user_type` varchar(32) DEFAULT NULL COMMENT '使用任务的用户类型（common：普通用户；manage：管理员）',
  `task_name` varchar(300) DEFAULT NULL COMMENT '任务名称',
  `task_desc` varchar(500) DEFAULT NULL COMMENT '任务描述',
  `to_type` varchar(32) DEFAULT NULL COMMENT '推送范围',
  `to_content` varchar(500) DEFAULT NULL COMMENT '推送范围值',
  `msg_type` varchar(32) DEFAULT NULL COMMENT '消息类型',
  `msg_content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `msg_title` varchar(300) DEFAULT NULL COMMENT '消息标题',
  `msg_descript` varchar(100) DEFAULT NULL COMMENT '消息描述',
  `msg_url` varchar(200) DEFAULT NULL COMMENT '消息链接',
  `msg_pic_url` varchar(200) DEFAULT NULL COMMENT '消息图片',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `job_name` varchar(500) DEFAULT NULL COMMENT '任务名称（在quartz中是唯一的可以看作id）',
  `job_group` varchar(500) DEFAULT NULL COMMENT '任务组名称',
  `trigger_name` varchar(500) DEFAULT NULL COMMENT '触发器名（在quartz中是唯一的可以看作id）',
  `trigger_group` varchar(500) DEFAULT NULL COMMENT '触发器组名',
  `job_time` datetime DEFAULT NULL,
  `job_cron` varchar(50) DEFAULT NULL,
  `job_state` varchar(50) DEFAULT NULL COMMENT '任务执行状态',
  `job_last_run_time` datetime DEFAULT NULL COMMENT '任务上次执行时间',
  `job_next_run_time` datetime DEFAULT NULL COMMENT '任务下次执行时间',
  `job_curr_run_time` datetime DEFAULT NULL COMMENT '任务本次执行时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `creater` varchar(32) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `updater` varchar(32) DEFAULT NULL COMMENT '修改人',
  `tenant_id` varchar(32) DEFAULT NULL COMMENT '租户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信推送表';

/*Data for the table `t_wx_push` */

LOCK TABLES `t_wx_push` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_wx_push_file` */

DROP TABLE IF EXISTS `t_wx_push_file`;

CREATE TABLE `t_wx_push_file` (
  `id` varchar(32) NOT NULL,
  `push_id` varchar(32) DEFAULT NULL COMMENT '推送id',
  `resource_id` varchar(32) DEFAULT NULL COMMENT '资源id',
  `file_id` varchar(32) DEFAULT NULL COMMENT '文件id',
  `file_code` varchar(32) DEFAULT NULL COMMENT '转化后文件code（多个文件可以使用一个code，代表一组）',
  `file_path` varchar(640) DEFAULT NULL COMMENT '文件路径',
  `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '推送时间-转化时间',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='推送资源转换文件表';

/*Data for the table `t_wx_push_file` */

LOCK TABLES `t_wx_push_file` WRITE;

UNLOCK TABLES;

/*Table structure for table `t_wx_push_record` */

DROP TABLE IF EXISTS `t_wx_push_record`;

CREATE TABLE `t_wx_push_record` (
  `id` varchar(32) NOT NULL,
  `wx_push_id` varchar(32) DEFAULT NULL COMMENT '推送id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `result_code` int(11) DEFAULT NULL COMMENT '1成功0失败',
  `result_msg` varchar(255) DEFAULT NULL,
  `to_user` varchar(500) DEFAULT NULL COMMENT '接收人id',
  `to_user_error` varchar(500) DEFAULT NULL COMMENT '发送接收人失败',
  `tenant_id` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='微信推送记录';

/*Data for the table `t_wx_push_record` */

LOCK TABLES `t_wx_push_record` WRITE;

UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
