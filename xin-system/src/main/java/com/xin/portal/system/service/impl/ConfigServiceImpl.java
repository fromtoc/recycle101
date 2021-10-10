package com.xin.portal.system.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.mapper.ConfigMapper;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.service.ConfigService;

import java.util.List;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

	@Autowired
	private ConfigMapper mapper;

//	@Override
//	public void refresh(String code) {
//		List<Config> records = findList(null);
//		Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
//		for (Config record : records) {
//			cache.put(record.getCode(), record.getValue());
//		}
//	}


	@Override
	//@CachePut(value = ConfigKeys.SYS_LOGIN_PAGE, key = "#tenantId")
	public Config findByCode(String code, String tenantId) {
		return mapper.findByCode(code,tenantId);
	}

	@Override
	public List<Config> findListByCode(String tenantId) {
		return mapper.findListByCode(tenantId);
	}

	@Override
	public boolean updateByCode(String code, String value, String tenantId) {
		try{
			Integer i = mapper.updateByCode(code,value,tenantId);
			if(i==1)
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

}
