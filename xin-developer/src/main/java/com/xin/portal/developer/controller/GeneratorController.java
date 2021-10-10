package com.xin.portal.developer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.xin.portal.developer.model.FileOutConfigXin;
import com.xin.portal.developer.model.ModelGenerator;

/**
 * @ClassPath: com.xin.developer.controller.GeneratorController 
 * @Description: 
 * @author zhoujun
 * @date 2018年6月29日 下午6:28:36
 */
@Controller
@RequestMapping("/generator")
public class GeneratorController {
	
private static final String DIR = "generator/";
	
	private static Logger logger = LoggerFactory.getLogger(GeneratorController.class);

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return DIR + "index";
	}
	
	
	@PostMapping(value = "/add")
	@ResponseBody
	public Map<String, Object> add(ModelGenerator modelGenerator) {
		Map<String, Object> baseApi = new HashMap<>(); 
		if (create(modelGenerator)) {
			logger.info("=== generator file package: {}",modelGenerator.getPackageDir());
			logger.info("=== generator file path: {}",modelGenerator.getOutputDir());
			baseApi.put("code", 0);
			baseApi.put("msg", "success");
			baseApi.put("data", modelGenerator);
			return baseApi;
		} else {
			baseApi.put("code", -1);
			baseApi.put("msg", "error");
			return baseApi;
		}
	}
	
	public static boolean create(ModelGenerator modelGenerator) {
		boolean result = true;
		try {
			AutoGenerator mpg = new AutoGenerator();
			// 全局配置
			GlobalConfig gc = new GlobalConfig();
			gc.setOutputDir(modelGenerator.getOutputDir());
			gc.setFileOverride(true);
			gc.setActiveRecord(true);
			gc.setEnableCache(false);// XML 二级缓存
			gc.setBaseResultMap(true);// XML ResultMap
			gc.setBaseColumnList(true);// XML columList
			gc.setOpen(false);
			gc.setAuthor(modelGenerator.getAuthor());
			// 自定义文件命名，注意 %s 会自动填充表实体属性！
			
			gc.setMapperName("%sMapper");
			gc.setXmlName("%sMapper");
			gc.setServiceName("%sService");
			gc.setServiceImplName("%sServiceImpl");
			gc.setControllerName("%sController");
			gc.setIdType(IdType.ID_WORKER_STR);
			mpg.setGlobalConfig(gc);
			// 数据源配置
			DataSourceConfig dsc = new DataSourceConfig();
			if (DbType.MYSQL.getValue().equals(modelGenerator.getDbType())) {
				dsc.setDbType(DbType.MYSQL);
				dsc.setDriverName("com.mysql.jdbc.Driver");
			} else if (DbType.ORACLE.getValue().equals(modelGenerator.getDbType())) {
				dsc.setDbType(DbType.ORACLE);
				dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
			}
			dsc.setUsername(modelGenerator.getDbUsername());
			dsc.setPassword(modelGenerator.getDbPassword());
			dsc.setUrl(modelGenerator.getDbUrl());
			mpg.setDataSource(dsc);
			// 策略配置
			StrategyConfig strategy = new StrategyConfig();
			strategy.setTablePrefix(modelGenerator.getTablePrefix());// 此处可以修改为您的表前缀
			strategy.setInclude(modelGenerator.getTableName()); // 需要生成的表
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
			strategy.setSuperControllerClass(modelGenerator.getSuperController());
			// 【实体】是否生成字段常量（默认 false）
			// public static final String ID = "test_id";
			// strategy.setEntityColumnConstant(true);
			// 【实体】是否为构建者模型（默认 false）
			// public User setName(String name) {this.name = name; return this;}
			// strategy.setEntityBuliderModel(true);
			strategy.setRestControllerStyle(modelGenerator.isRestControllerStyle());
			mpg.setStrategy(strategy);
			// 包配置
			PackageConfig pc = new PackageConfig();
			pc.setParent(modelGenerator.getPackageDir());
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
				}
			};
			
			
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("page", modelGenerator.isPage());
			map.put("name", modelGenerator.getName());
			map.put("isApi", modelGenerator.getApi());
			map.put("module", modelGenerator.getModule());
			map.put("hasSave", modelGenerator.isHasSave());
			map.put("hasUpdate", modelGenerator.isHasUpdate());
			map.put("hasDelete", modelGenerator.isHasDelete());
			cfg.setMap(map);
			
			mpg.setCfg(cfg);
			mpg.setTemplateEngine(new FreemarkerTemplateEngine());
			
			// 自定义 生成
			List<FileOutConfig> focList = new ArrayList<FileOutConfig>();
			String entryPath = StringUtils.underlineToCamel(StringUtils.removePrefixAfterPrefixToLower(modelGenerator.getTableName(),modelGenerator.getTablePrefix().length()));
			if (ModelGenerator.TYPE_VUE.equals(modelGenerator.getFrontType())) {
				FileOutConfig fcList = new FileOutConfigXin("/tpl/front/vue/list.ftl",modelGenerator.getFrontDir() + File.separator + entryPath + ".vue");
				if (modelGenerator.isHasSave() || modelGenerator.isHasUpdate()) {
					FileOutConfig fcAdd = new FileOutConfigXin("/tpl/front/vue/add.ftl",modelGenerator.getFrontDir() + File.separator + entryPath + "-add-or-update.vue");
					focList.add(fcAdd);
				}
				focList.add(fcList);
			} else {
				if (modelGenerator.isHasSave()) {
					FileOutConfig fcAdd = new FileOutConfigXin("/tpl/front/"+modelGenerator.getFrontType()+"/add.ftl",modelGenerator.getFrontDir() + File.separator + "templates/" + entryPath + "/add.html");
					focList.add(fcAdd);
				}
				if (modelGenerator.isHasUpdate()) {
					FileOutConfig fcAdd = new FileOutConfigXin("/tpl/front/"+modelGenerator.getFrontType()+"/edit.ftl",modelGenerator.getFrontDir() + File.separator + "templates/" + entryPath + "/edit.html");
					focList.add(fcAdd);
				}
				FileOutConfig fcList = new FileOutConfigXin("/tpl/front/"+modelGenerator.getFrontType()+"/index.ftl",modelGenerator.getFrontDir() + File.separator + "templates/" + entryPath + "/index.html");
				focList.add(fcList);
			} 
	        
	        cfg.setFileOutConfigList(focList);
			
			// 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
			// 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
			TemplateConfig tc = new TemplateConfig();
			
//			tc.setEntity("tpl/entity.java.vm");
//			tc.setMapper("tpl/mapper.java.vm");
//			tc.setXml("tpl/mapper.xml.vm");
//			tc.setService("tpl/service.java.vm");
//			tc.setServiceImpl("tpl/serviceImpl.java.vm");
//			tc.setController("tpl/controller.java.vm");
			if (ModelGenerator.TYPE_VUE.equals(modelGenerator.getFrontType())) {
				tc.setEntity("tpl/java/rest/entity.java");
				tc.setMapper("tpl/java/rest/mapper.java");
				tc.setXml("tpl/java/rest/mapper.xml");
				tc.setService("tpl/java/rest/service.java");
				tc.setServiceImpl("tpl/java/rest/serviceImpl.java");
				tc.setController("tpl/java/rest/controller.java");
			} else {
				tc.setEntity("tpl/java/html/entity.java");
				tc.setMapper("tpl/java/html/mapper.java");
				tc.setXml("tpl/java/html/mapper.xml");
				tc.setService("tpl/java/html/service.java");
				tc.setServiceImpl("tpl/java/html/serviceImpl.java");
				tc.setController("tpl/java/html/controller.java");
				
			}
			
			mpg.setTemplate(tc);
			
			// 执行生成
			mpg.execute();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}
	
	public static void main (String[] args) {
		ModelGenerator generator = new ModelGenerator();
		generator.setName("模块名称");
		generator.setOutputDir("D:\\Programs\\eclipse\\workspaces\\default\\xin-root\\xin-cms\\src\\main\\java");
		generator.setAuthor("zhoujun");
		generator.setDbType("mysql");
		generator.setDbUrl("jdbc:mysql://127.0.0.1:3306/db_xin?useUnicode=true&characterEncoding=utf-8");
		generator.setDbUsername("root");
		generator.setDbPassword("root");
		generator.setTableName("t_cms_channel");
		generator.setTablePrefix("t_");
		generator.setSuperController("com.xin.core.bean.BaseController");
		generator.setPackageDir("com.xin.cms");
		generator.setRestControllerStyle(true);
		generator.setApi(true);
		generator.setPage(true);
		generator.setFrontDir("D:\\Programs\\vue\\xin-web\\src\\views\\system\\cms");
		generator.setFrontType(ModelGenerator.TYPE_VUE);
		//create(generator);
	}

}
