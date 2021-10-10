package com.xin.portal.system.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.LicenseMapper;
import com.xin.portal.system.model.License;
import com.xin.portal.system.service.LicenseService;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-05-08
 */
@Service
public class LicenseServiceImpl extends ServiceImpl<LicenseMapper, License> implements LicenseService {
	
	@Override
	public PageModel<License> page(License query, Integer pageNumber, Integer pageSize) {
		Page<License> page = new Page<License>(pageNumber, pageSize);
		EntityWrapper<License> ew=new EntityWrapper<License>();
		if (!StringUtils.isEmpty(query.getCompany())) {
			ew.like("company", query.getCompany());
		}
		if (!StringUtils.isEmpty(query.getCreater())) {
			ew.like("creater", query.getCreater());
		}
		if (!StringUtils.isEmpty(query.getVersion())) {
			ew.like("version", query.getVersion());
		}
		ew.orderBy("apply_time", false); 
		//ew.orderBy("state", true);
		page = selectPage(page, ew);
		return new PageModel<License>(page);
	}
	
}
