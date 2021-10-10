package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.SysRelease;
import com.xin.portal.system.mapper.SysReleaseMapper;
import com.xin.portal.system.service.SysReleaseService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2018-10-23
 */
@Service
public class SysReleaseServiceImpl extends ServiceImpl<SysReleaseMapper, SysRelease> implements SysReleaseService {
	@Override
	public PageModel<SysRelease> page(SysRelease query, Integer pageNumber, Integer pageSize) {
		Page<SysRelease> page = new Page<SysRelease>(pageNumber, pageSize);
		EntityWrapper<SysRelease> ew=new EntityWrapper<SysRelease>(query);
		ew.orderBy("release_time", false);
		page = selectPage(page, ew);
		return new PageModel<SysRelease>(page);
	}
}
