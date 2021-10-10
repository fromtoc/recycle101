package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.ResourceType;
import com.xin.portal.system.mapper.ResourceTypeMapper;
import com.xin.portal.system.service.ResourceTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

import java.util.List;

/**
 * 资源类型表 服务实现类
 *
 * @author zhoujun
 * @since 2018-10-31
 */
@Service
public class ResourceTypeServiceImpl extends ServiceImpl<ResourceTypeMapper, ResourceType> implements ResourceTypeService {

	@Autowired
	private ResourceTypeMapper mapper;

	@Override
	public PageModel<ResourceType> page(ResourceType query, Integer pageNumber, Integer pageSize) {
		Page<ResourceType> page = new Page<ResourceType>(pageNumber, pageSize);
		EntityWrapper<ResourceType> ew=new EntityWrapper<ResourceType>(query);
		page = selectPage(page, ew);
		return new PageModel<ResourceType>(page);
	}

	@Override
	public List<ResourceType> findResourceTypeList(String userId) {
		return mapper.findResourceTypeList(userId);
	}
}
