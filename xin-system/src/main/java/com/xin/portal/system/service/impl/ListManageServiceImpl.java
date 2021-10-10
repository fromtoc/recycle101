package com.xin.portal.system.service.impl;

import java.util.List;

import com.xin.portal.system.model.ListManage;
import com.xin.portal.system.model.ListManageResource;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.mapper.ListManageMapper;
import com.xin.portal.system.mapper.ResourceMapper;
import com.xin.portal.system.service.ListManageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author xue
 * @since 2018-12-10
 */
@Service
public class ListManageServiceImpl extends ServiceImpl<ListManageMapper, ListManage> implements ListManageService {
	@Autowired
	private ListManageMapper mapper;
	@Autowired
	private ResourceMapper resourceMapper;
	public PageModel<Resource> page(ListManageResource query, Integer pageNumber, Integer pageSize) {
  		Page<Resource> page = new Page<Resource>(pageNumber, pageSize);
		//page = selectPage(page, ew);
  		//列表管理中直接生成URL方便用户发布到资源中
  		List<Resource> list = resourceMapper.selectPageByListId(page, query);
        page.setRecords(list);
		return new PageModel<Resource>(page);
	}
	@Override
	public List<Resource> selectByListId(String id,String userId) {
		return resourceMapper.selectByListId(id,userId);
	}
}
