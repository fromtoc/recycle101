package com.xin.portal.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.BiProjectMapper;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.service.BiProjectService;

@Service
public class BiProjectServiceImpl extends ServiceImpl<BaseMapper<BiProject>, BiProject> implements BiProjectService {
	
	@Autowired
	private BiProjectMapper mapper;
	
	@Override
	public PageModel<BiProject> page(BiProject query, Integer pageNumber, Integer pageSize) {
		Page<BiProject> page = new Page<BiProject>(pageNumber, pageSize);
		page.setRecords(mapper.findList(page,query));
		return new PageModel<BiProject>(page);
	}

	@Override
	public List<BiProject> findList(BiProject query) {
		return mapper.findList(query);
	}

	@Override
	public BiProject findById(String id) {
		return mapper.findById(id);
	}

	@Override
	public int updateProjectIndepend(String id) {
		return mapper.updateProjectIndepend(id);
	}

}
