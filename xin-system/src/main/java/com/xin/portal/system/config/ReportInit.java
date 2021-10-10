package com.xin.portal.system.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.microstrategy.webapi.EnumDSSXMLObjectTypes;
import com.xin.portal.system.service.BiProjectService;
import com.xin.portal.system.util.Constant;

/**
 * @ClassPath: com.xin.portal.system.config.ReportInit 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月5日 上午10:36:27
 */
@Component
@ConditionalOnProperty(name = "mstr.enabled")
public class ReportInit implements CommandLineRunner{
	

	@Autowired
	private BiProjectService service;
	@Autowired
    protected CacheManager cacheManager;

	@Override
	public void run(String... args) throws Exception {
		Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
		cache.put(Constant.CACHE_MSTR_TYPE+EnumDSSXMLObjectTypes.DssXmlTypeFolder, "Folder");
		cache.put(Constant.CACHE_MSTR_TYPE+EnumDSSXMLObjectTypes.DssXmlTypeReportDefinition, "Report");
		cache.put(Constant.CACHE_MSTR_TYPE+EnumDSSXMLObjectTypes.DssXmlTypeDocumentDefinition, "Document");
//		CacheManager.put(EnumDSSXMLObjectTypes.DssXmlTypeFilter, "Filter");
//		CacheManager.put(EnumDSSXMLObjectTypes.DssXmlTypeTemplate, "Template");
//		service.refresh(null);
	}

}
