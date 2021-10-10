package com.xin.portal.system.service.impl;

import java.util.List;

import com.xin.portal.system.model.MoContent;
import com.xin.portal.system.mapper.MoContentMapper;
import com.xin.portal.system.service.MoContentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
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
public class MoContentServiceImpl extends ServiceImpl<MoContentMapper, MoContent> implements MoContentService {
	@Autowired
	private MoContentMapper moContentMapper;
	@Override
	public PageModel<MoContent> page(MoContent query, Integer pageNumber, Integer pageSize) {
		Page<MoContent> page = new Page<MoContent>(pageNumber, pageSize);
		EntityWrapper<MoContent> ew=new EntityWrapper<MoContent>(query);
		page = selectPage(page, ew);
		return new PageModel<MoContent>(page);
	}

	@Override
	public List selectByModuleId(String moduleId) {
		// TODO Auto-generated method stub
		return moContentMapper.selectByModuleId(moduleId);
	}

	@Override
	public int selectMaxSort(String moduleId) {
		// TODO Auto-generated method stub
		return moContentMapper.selectMaxSort(moduleId);
	}

	@Override
	public void deleteByModuleId(String id) {
		// TODO Auto-generated method stub
		moContentMapper.deleteByModuleId(id);
	}

	@Override
	public boolean update(MoContent record) {
		Integer flag = moContentMapper.updateById(record);
		if(flag==1){
			return true;
		}
		return false;
	}

}
