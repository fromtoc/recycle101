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

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HtmlHelper {
	private static final String FUFFIX = ".html";
	private static final String MODEL_NAME = "courseNotice";
	private static final String[] FILES = {"index_layui"};
	private static final String TPL_PATH = "D:/Programs/eclipse/workspaces/support/xin-root/xin-developer/src/main/resources/tpl/";
	private static final String OUT_PATH = "D:/Programs/eclipse/workspaces/support/xin-root/xin-edu/src/main/resources/templates/"+MODEL_NAME+"/";
	
	public static void main(String[] args) {
		//System.out.println(HtmlHelper.class.getClassLoader().getResource(""));
		Map<String, String> names = new HashMap<>();
		names.put("index", "index_layui");
		for (String fileName : names.keySet()) {
			createHtmlFromStringList(fileName,names.get(fileName));
		}
	}
	
	/**
     * 根据String模板生成HTML，模板中存在List循环
     */
    public static void createHtmlFromStringList(String fileName,String tplName) {
        BufferedInputStream in = null;
        FileWriter out = null;
        try {
            //模板文件
            File file = new File(TPL_PATH + tplName + ".html");
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
            loader.putTemplate(tplName, content.toString());
            //把StringTemplateLoader添加到Configuration中
            configuration.setTemplateLoader(loader);
            
            //构造Model
            Map<String, Object> dataMap = new HashMap<>();
            dataMap.put("modelName", MODEL_NAME);
            //获取模板
            Template template = configuration.getTemplate(tplName);
            //构造输出路
            if (!new File(OUT_PATH).exists()) {
            	new File(OUT_PATH).mkdirs();
            }
            out = new FileWriter(OUT_PATH + fileName  + FUFFIX);
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
