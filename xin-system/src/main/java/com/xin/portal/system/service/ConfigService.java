package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.model.Config;

import java.util.List;

public interface ConfigService extends IService<Config> {

	Config findByCode(String code, String tenantId);

	List<Config> findListByCode(String tenantId);

	boolean updateByCode(String code,String value,String tenantId);

}
