package com.xin.portal.system.util;

import java.lang.reflect.Field;


import com.xin.portal.system.bean.AllBean;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.service.impl.ConfigServiceImpl;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.i18n.LanguageAll;
import com.xin.portal.system.util.i18n.LanguageCH;
import com.xin.portal.system.util.i18n.LanguageEN;
import com.xin.portal.system.util.i18n.LanguageTW;

public class LangTransform {
	
	private ConfigServiceImpl configService = (ConfigServiceImpl) AllBean.getBean("configServiceImpl");
	
	private static final String ZNCN = "zh_CN";
	private static final String ZNTW = "zh_TW";
	private static final String ENUS = "en_US";
	
	public static String getLocaleLang(String lang,String value){
		String result = "";
		if((lang!=null&&lang.length()>0) && (value!=null&&value.length()>0)){
			try {
				result = new LangTransform().getLanguageValue(lang, value);
			} catch (NoSuchFieldException e) {
				result = "";
				e.printStackTrace();
			} catch (SecurityException e) {
				result = "";
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				result = "";
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				result = "";
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getLocaleLang(String value){
		String result = "";
		if(value!=null&&value.length()>0){
			try {
				LangTransform trans = new LangTransform();
				String lang = trans.getCurrentLocale();
				result = trans.getLanguageValue(lang, value);
			} catch (NoSuchFieldException e) {
				result = "";
				//e.printStackTrace();
			} catch (SecurityException e) {
				result = "";
				//e.printStackTrace();
			} catch (IllegalArgumentException e) {
				result = "";
				//e.printStackTrace();
			} catch (IllegalAccessException e) {
				result = "";
				//e.printStackTrace();
			}
		}
		return result;
	}
	
	public String getLanguageValue(String lang,String value) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
		String result = "";
		LanguageAll language = initLangaugeBeen(lang);
		Field field = language.getClass().getDeclaredField(value);
		field.setAccessible(true);
		result = field.get(language).toString();
		return result;
	}
	
	private String getCurrentLocale(){
		String lang = "zh_CN";
        Config config = configService.findByCode(ConfigKeys.CONFIG_LOCALE, (String)SessionUtil.getSession(SessionKeys.TENANT_ID));
        if(config!=null){
        	lang = config.getValue();
        }
        return lang;
	}
	
	public LanguageAll initLangaugeBeen(String lang){
		LanguageAll language = null;
		switch (lang) {
		case ZNCN:
			language = new LanguageCH();
			break;
		case ZNTW:
			language = new LanguageTW();
			break;
		case ENUS:
			language = new LanguageEN();
			break;
		default:
			language = new LanguageCH();
			break;
		}
		return language;
	}
	
}
