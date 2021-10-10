package com.xin.portal.developer.controller;

import java.util.HashMap;
import java.util.Map;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;



/**
 * 
 * 自动生成映射工具类
 * 
 * @author hubin
 * 
 */
public class AutoGeneratorHelper {
	
	//生成文件目录
	private static String outputDir = "D:\\Programs\\eclipse\\workspaces\\demand\\xin-root\\xin-shen\\src\\main\\java";
//	private static String outputDir = "D:\\1";
	//作者
	private static String author = "zhoujun";
	//数据库类型
	private static DbType dbType = DbType.MYSQL;
	//数据库驱动
	private static String driverName = "com.mysql.jdbc.Driver";
	//数据库用户名
	private static String dbUsername = "root";
	//数据库密码
	private static String dbPassword = "root";
	//数据库连接地址
	private static String dbUrl = "jdbc:mysql://127.0.0.1:3306/db_portal?useUnicode=true&characterEncoding=utf-8";
	//要生成的表名
	private static String[] tables ={ "upload_log" };
	//数据库表前缀
	private static String tablePrefix = "";
	//是否分页
	private static boolean isPage = true;
	//package目录
	private static String packageDir = "com.xin.shen";
	//controller继承的父类
	private static String superController = "com.xin.portal.system.bean.BaseController";
	
	public static void main(String[] args) {
		AutoGenerator mpg = new AutoGenerator();
		// 全局配置
		GlobalConfig gc = new GlobalConfig();
		gc.setOutputDir(outputDir);
		gc.setFileOverride(true);
		gc.setActiveRecord(true);
		gc.setEnableCache(false);// XML 二级缓存
		gc.setBaseResultMap(true);// XML ResultMap
		gc.setBaseColumnList(true);// XML columList
		gc.setOpen(true);
		gc.setAuthor(author);
		// 自定义文件命名，注意 %s 会自动填充表实体属性！

		gc.setMapperName("%sMapper");
		gc.setXmlName("%sMapper");
		gc.setServiceName("%sService");
		gc.setServiceImplName("%sServiceImpl");
		gc.setControllerName("%sController");
		mpg.setGlobalConfig(gc);
		// 数据源配置
		DataSourceConfig dsc = new DataSourceConfig();
		dsc.setDbType(dbType);
		dsc.setDriverName(driverName);
		dsc.setUsername(dbUsername);
		dsc.setPassword(dbPassword);
		dsc.setUrl(dbUrl);
		mpg.setDataSource(dsc);
		// 策略配置
		StrategyConfig strategy = new StrategyConfig();
		strategy.setTablePrefix(tablePrefix);// 此处可以修改为您的表前缀
		strategy.setInclude(tables); // 需要生成的表
		// strategy.setExclude(new String[]{"test"}); // 排除生成的表
		// 字段名生成策略
		strategy.setNaming(NamingStrategy.underline_to_camel);
		// 自定义实体父类
		strategy.setSuperEntityClass("");
		// 自定义实体，公共字段
		// strategy.setSuperEntityColumns(
		// new String[] { "id_", "enable_", "remark_", "create_by",
		// "create_time", "update_by", "update_time" });
		// 自定义 mapper 父类
		strategy.setSuperMapperClass("com.baomidou.mybatisplus.mapper.BaseMapper");
		// 自定义 service 父类
		strategy.setSuperServiceClass("com.baomidou.mybatisplus.service.IService");
		// 自定义 service 实现类父类
		strategy.setSuperServiceImplClass("com.baomidou.mybatisplus.service.impl.ServiceImpl");
		// 自定义 controller 父类
		strategy.setSuperControllerClass(superController);
		// 【实体】是否生成字段常量（默认 false）
		// public static final String ID = "test_id";
		// strategy.setEntityColumnConstant(true);
		// 【实体】是否为构建者模型（默认 false）
		// public User setName(String name) {this.name = name; return this;}
		// strategy.setEntityBuliderModel(true);
		mpg.setStrategy(strategy);
		// 包配置
		PackageConfig pc = new PackageConfig();
		pc.setParent(packageDir);
		pc.setEntity("model");
		pc.setMapper("mapper");
		pc.setXml("mapper.xml");
		pc.setServiceImpl("service.impl");
		pc.setService("service");
		pc.setController("controller");
		mpg.setPackageInfo(pc);
		// 注入自定义配置，可以在 VM 中使用 cfg.abc 设置的值
		InjectionConfig cfg = new InjectionConfig() {
			public void initMap() {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("isPage", isPage);
				this.setMap(map);
			}
		 };
		 mpg.setCfg(cfg);
		// 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
		// 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
		TemplateConfig tc = new TemplateConfig();
		 tc.setEntity("tpl/entity.java.vm");
		 tc.setMapper("tpl/mapper.java.vm");
		//tc.setXml("tpl/mapper.xml.vm");
		 tc.setService("tpl/service.java.vm");
		 tc.setServiceImpl("tpl/serviceImpl.java.vm");
		 tc.setController("tpl/controller.java.vm");
		mpg.setTemplate(tc);
		
		// 执行生成
		mpg.execute();
	}

}