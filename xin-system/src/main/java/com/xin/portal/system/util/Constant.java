package com.xin.portal.system.util;

public class Constant {

	public static final String THEME = "theme";
	public static final Object COLOR = "color";
	public static final String ROOT = "1";
	public static final String LOGIN = "login/login";
	public static final String DEFAULT_SALT = "Ruby";
	
	public static final String CACHE_DEFAULT = "default_cache";
	public static final String CACHE_MSTR_PROJECT = "CACHE_MSTR_PROJECT_";
	public static final String CACHE_MSTR_TYPE = "CACHE_MSTR_TYPE_";
	public static final String CACHE_USERAMOUNT="CACHE_USERAMOUNT";
	public static final String CACHE_UPLOAD = "CACHE_UPLOAD_";
	public static final String CACHE_THIRD_APP = "CACHE_THIRD_APP";

	
	public static class ConfigKeys {
		public static final String SYS_HOME_INDEX = "SYS_HOME_INDEX";
		public static final String SYS_THEME= "SYS_THEME";
		public static final String SYS_BANNER_SIZE = "SYS_BANNER_SIZE";
		public static final String SYS_COLOR = "SYS_COLOR";
		public static final String SYS_LOGIN_PAGE = "SYS_LOGIN_PAGE";
		public static final String SYS_NAME = "SYS_NAME";
		public static final String SYS_UPLOAD_PATH = "SYS_UPLOAD_PATH";
		public static final String SYS_LOGO = "SYS_LOGO";
		public static final String SYS_COPYRIGHT = "SYS_COPYRIGHT";
		public static final String SYS_CHECK_CODE = "SYS_CHECK_CODE";
		public static final String SYS_LOCALE = "SYS_LOCALE";//用户语言，存入session的key
		public static final String SYS_USER_EMAIL = "SYS_USER_EMAIL";
		public static final String SYS_FIRST_UPDATE_PWD = "SYS_FIRST_UPDATE_PWD";
		public static final String SYS_MENU_ICON = "SYS_MENU_ICON";
		public static final String SYS_URL = "SYS_URL";
		public static final String SYS_SYNC_CREATE_MSTR_USER = "SYS_SYNC_CREATE_MSTR_USER";
		
		public static final String REPEAT_LOGIN = "REPEAT_LOGIN";//重复登录
		public static final String NOT_ALLOW_REPEAT_LOGIN_WAY = "NOT_ALLOW_REPEAT_LOGIN_WAY";//不同意重复登录选择
		public static final String LOGIN_PROMPT = "LOGIN_PROMPT";//登录提示信息
		public static final String FORGET_PASSWORD_PROMPT = "FORGET_PASSWORD_PROMPT";//是否开启忘记密码提示
		public static final String PASSWORD_UPDATE_REGULARLY = "PASSWORD_UPDATE_REGULARLY";//是否开启密码定期更新提示
		public static final String PASSWORD_UPDATE_REGULARLY_TIME = "PASSWORD_UPDATE_REGULARLY_TIME";//开启密码定期更新提示时间
		public static final String PASSWORD_STRENGTH_REQUIRE = "PASSWORD_STRENGTH_REQUIRE";//密码强度要求
		public static final String PASSWORD_ERROR_LOCK="PASSWORD_ERROR_LOCK";//密码错误用户锁定
		public static final String PASSWORD_ERROR_COUNT="PASSWORD_ERROR_COUNT";//密码错误次数
		public static final String LEAST_LENGTH = "LEAST_LENGTH";//长度至少
		public static final String LENGTH_VALUE = "LENGTH_VALUE";//长度值
		public static final String CONTAIN_NUMBER = "CONTAIN_NUMBER";//包含数字
		public static final String CONTAINT_LETTERS = "CONTAINT_LETTERS";//包含字母
		public static final String CONTAINT_SYMBOLS = "CONTAINT_SYMBOLS";//包含符号
		public static final String SYS_LOGIN_NAME = "SYS_LOGIN_NAME";//登录页标题
		public static final String SYS_MAIN_LOGO = "SYS_MAIN_LOGO";//系统顶部LOGO
		
		public static final String MAIL_PASSWORD = "MAIL_PASSWORD";//密码
		public static final String MAIL_PORT = "MAIL_PORT";//端口
		public static final String MAIL_FROM = "MAIL_FROM";//发件人地址
		public static final String MAIL_ACCOUNT = "MAIL_ACCOUNT";//显示名称
		public static final String MAIL_HOST = "MAIL_HOST";//邮件服务器

		public static final String SYS_HOME_PAGE_NAME = "SYS_HOME_PAGE_NAME";//系统首页名称
		public static final String PREFERENCE_TYPE = "PREFERENCE_TYPE";//系统首页类型
		public static final String PREFERENCE_VALUE = "PREFERENCE_VALUE";//系统首页对象值
		public static final String CONFIG_LOCALE = "CONFIG_LOCALE";//系统默认语言

		public static final String DOMAIN_IP = "DOMAIN_IP";//AD域IP
		public static final String DOMAIN_PASSWORD = "DOMAIN_PASSWORD";//AD域管理员密码
		public static final String DOMAIN_NAME = "DOMAIN_NAME";//AD域名
		public static final String LOGIN_OPTION = "LOGIN_OPTION";//系统登录选项

		
	}
	
	public static class SessionKeys {
		public static final String USER = "user";
		public static final String USER_MAPPING_MSTR = "user_mapping_mstr";
		public static final String USER_MAPPING_BO = "user_mapping_bo";
		public static final String USER_ROLES = "user_roles";
		public static final String USER_PERMS = "user_perms";
		public static final String USER_RESOURCE = "user_resources";
		public static final String USER_MENUS = "user_menus";
		public static final String USER_RECORDS = "user_records";
		public static final String USER_MENUS_JSON = "user_menus_json";
		public static final String SESSION_FORCE_LOGOUT_KEY = "session_logout";
		public static final String IP = "ip";
		public static final String TENANT_ID = "tenant_id";
		public static final String BO_TOKEN = "bo_token";
		public static final String BO_PROJECT = "bo_project";
	}
	
	public static class BiType {
		public static final String MSTR = "mstr";
		public static final String TABLEAU = "tableau";
		public static final String FINE = "fine";
		public static final String BO = "bo";
		
	}

}
