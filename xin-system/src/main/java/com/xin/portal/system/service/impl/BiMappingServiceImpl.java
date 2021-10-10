package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.BiMapping;
import com.xin.portal.system.mapper.BiMappingMapper;
import com.xin.portal.system.service.BiMappingService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 * 用户映射表 服务实现类
 *
 * @author zhoujun
 * @since 2018-12-07
 */
@Service
public class BiMappingServiceImpl extends ServiceImpl<BiMappingMapper, BiMapping> implements BiMappingService {
	
	@Autowired
	private BiMappingMapper mapper;
	
	@Override
	public PageModel<BiMapping> page(BiMapping query, Integer pageNumber, Integer pageSize) {
		Page<BiMapping> page = new Page<BiMapping>(pageNumber, pageSize);
//		EntityWrapper<BiMapping> ew=new EntityWrapper<BiMapping>(query);
//		page = selectPage(page, ew);
		page.setRecords(mapper.findList(page,query));
		return new PageModel<BiMapping>(page);
	}

	@Override
	public List<BiMapping> findList(BiMapping query) {
		return mapper.findList(query);
	}

	@Override
	public List<BiMapping> selectBiUserBySysUserAndServerId(BiMapping query) {
		return mapper.selectBiUserBySysUserAndServerId(query);
	}

	@Override
	public int selectUserMappingCountByType(String type) {
		return mapper.selectUserMappingCountByType(type); 
	}
}
