package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.MoType;
import com.xin.portal.system.mapper.MoTypeMapper;
import com.xin.portal.system.service.MoTypeService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 * 模块类型表 服务实现类
 *
 * @author xue
 * @since 2018-11-08
 */
@Service
public class MoTypeServiceImpl extends ServiceImpl<MoTypeMapper, MoType> implements MoTypeService {
	@Override
	public PageModel<MoType> page(MoType query, Integer pageNumber, Integer pageSize) {
		Page<MoType> page = new Page<MoType>(pageNumber, pageSize);
		EntityWrapper<MoType> ew=new EntityWrapper<MoType>(query);
		page = selectPage(page, ew);
		return new PageModel<MoType>(page);
	}
}
