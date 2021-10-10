package com.xin.portal.developer.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import com.xin.portal.developer.model.AutoCode;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GeneratorUtil {
	
	public static boolean createJava(AutoCode autoCode) {
		boolean result = true;
		try {
			AutoGenerator mpg = new AutoGenerator();
			// 全局配置
			GlobalConfig gc = new GlobalConfig();
			gc.setOutputDir(autoCode.getOutputDir());
			gc.setFileOverride(true);
			gc.setActiveRecord(true);
			gc.setEnableCache(false);// XML 二级缓存
			gc.setBaseResultMap(true);// XML ResultMap
			gc.setBaseColumnList(true);// XML columList
			gc.setOpen(false);
			gc.setAuthor(autoCode.getAuthor());
			// 自定义文件命名，注意 %s 会自动填充表实体属性！
			
			gc.setMapperName("%sMapper");
			gc.setXmlName("%sMapper");
			gc.setServiceName("%sService");
			gc.setServiceImplName("%sServiceImpl");
			gc.setControllerName("%sController");
			mpg.setGlobalConfig(gc);
			// 数据源配置
			DataSourceConfig dsc = new DataSourceConfig();
			if (DbType.MYSQL.getValue().equals(autoCode.getDbType())) {
				dsc.setDbType(DbType.MYSQL);
				dsc.setDriverName("com.mysql.jdbc.Driver");
			} else if (DbType.ORACLE.getValue().equals(autoCode.getDbType())) {
				dsc.setDbType(DbType.ORACLE);
				dsc.setDriverName("oracle.jdbc.driver.OracleDriver");
			}
			dsc.setUsername(autoCode.getDbUsername());
			dsc.setPassword(autoCode.getDbPassword());
			dsc.setUrl(autoCode.getDbUrl());
			mpg.setDataSource(dsc);
			// 策略配置
			StrategyConfig strategy = new StrategyConfig();
			strategy.setTablePrefix(autoCode.getTablePrefix());// 此处可以修改为您的表前缀
			strategy.setInclude(autoCode.getTableName()); // 需要生成的表
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
			strategy.setSuperControllerClass(autoCode.getSuperController());
			// 【实体】是否生成字段常量（默认 false）
			// public static final String ID = "test_id";
			// strategy.setEntityColumnConstant(true);
			// 【实体】是否为构建者模型（默认 false）
			// public User setName(String name) {this.name = name; return this;}
			// strategy.setEntityBuliderModel(true);
			mpg.setStrategy(strategy);
			// 包配置
			PackageConfig pc = new PackageConfig();
			pc.setParent(autoCode.getPackageDir());
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
			map.put("isPage", autoCode.isPage());
			cfg.setMap(map);
			
			mpg.setCfg(cfg);
			// 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/template 下面内容修改，
			// 放置自己项目的 src/main/resources/template 目录下, 默认名称一下可以不配置，也可以自定义模板名称
			TemplateConfig tc = new TemplateConfig();
			tc.setEntity("tpl/entity.java.vm");
			tc.setMapper("tpl/mapper.java.vm");
			tc.setXml("tpl/mapper.xml.vm");
			tc.setService("tpl/service.java.vm");
			tc.setServiceImpl("tpl/serviceImpl.java.vm");
			tc.setController("tpl/controller.java.vm");
			mpg.setTemplate(tc);
			
			
			autoCode.setWebDir(mpg.getConfig().getTableInfoList().get(0).getEntityPath());
			// 执行生成
			mpg.execute();
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}

		return result;
	}
	
	
	/**
     * 根据String模板生成HTML，模板中存在List循环
     */
    public static void createHtml(String fileName,String modelName, String webPath, String fuffix) {
        BufferedInputStream in = null;
        FileWriter out = null;
        try {
            //模板文件
            File file = new File(HtmlHelper.class.getClassLoader().getResource("/") + "tpl/" + fileName + ".html");
            //构造输入流
            in = new BufferedInputStream(new FileInputStream(file));
            int len;
            byte[] bytes = new byte[1024];
            //模板内容
            StringBuilder content = new StringBuilder();
            while((len = in.read(bytes)) != -1) {
                content.append(new String(bytes, 0, len, "utf-8"));
            }
            
            //构造Configuration
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
            //构造StringTemplateLoader
            StringTemplateLoader loader = new StringTemplateLoader();
            //添加String模板
            loader.putTemplate(fileName, content.toString());
            //把StringTemplateLoader添加到Configuration中
            configuration.setTemplateLoader(loader);
            
            //构造Model
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("modelName", modelName);
            //获取模板
            Template template = configuration.getTemplate(fileName);
            //构造输出路
            if (!new File(webPath).exists()) {
            	new File(webPath).mkdirs();
            }
            out = new FileWriter(webPath + fileName  + fuffix);
            //生成HTML
            template.process(dataMap, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }

}
