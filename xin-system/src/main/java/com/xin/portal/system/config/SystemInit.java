package com.xin.portal.system.config;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.mapper.ConfigMapper;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;

/**
 * @ClassPath: com.xin.portal.system.config.SystemInit 
 * @Description: 初始化系统配置
 * @author zhoujun
 * @date 2017-12-8 上午10:38:21
 */
//@Component
public class SystemInit implements CommandLineRunner{
	
	private static Logger logger = LoggerFactory.getLogger(SystemInit.class);
	
	@Autowired
	private ConfigMapper configMapper;

	@Autowired
    private CacheManager cacheManager;

	@Override
	public void run(String... args) throws Exception {
		EntityWrapper<Config> ew = new EntityWrapper<>();
		List<Config> records = configMapper.selectList(ew);
		Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
		for (Config record : records) {
			cache.put(record.getCode(), record.getValue());
			logger.info("put cache [{}]:[{}]",record.getCode(),record.getValue());
			
		}
		
	}

}
