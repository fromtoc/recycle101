package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.mapper.BiServerMapper;
import com.xin.portal.system.service.BiServerService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2018-12-04
 */
@Service
public class BiServerServiceImpl extends ServiceImpl<BiServerMapper, BiServer> implements BiServerService {
	@Override
	public PageModel<BiServer> page(BiServer query, Integer pageNumber, Integer pageSize) {
		Page<BiServer> page = new Page<BiServer>(pageNumber, pageSize);
		EntityWrapper<BiServer> ew=new EntityWrapper<BiServer>(query);
		page = selectPage(page, ew);
		return new PageModel<BiServer>(page);
	}
}
