package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.ModuleContent;
import com.xin.portal.system.mapper.ModuleContentMapper;
import com.xin.portal.system.service.ModuleContentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author xue
 * @since 2018-11-08
 */
@Service
public class ModuleContentServiceImpl extends ServiceImpl<ModuleContentMapper, ModuleContent> implements ModuleContentService {
	@Override
	public PageModel<ModuleContent> page(ModuleContent query, Integer pageNumber, Integer pageSize) {
		Page<ModuleContent> page = new Page<ModuleContent>(pageNumber, pageSize);
		EntityWrapper<ModuleContent> ew=new EntityWrapper<ModuleContent>(query);
		page = selectPage(page, ew);
		return new PageModel<ModuleContent>(page);
	}
}
