package com.xin.portal.system.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.BannerMapper;
import com.xin.portal.system.mapper.FileModelMapper;
import com.xin.portal.system.model.Banner;
import com.xin.portal.system.model.FileModel;
import com.xin.portal.system.service.BannerService;

@Service
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements BannerService {
	
	@Autowired 
	private BannerMapper mapper;

	@Override
	public List<Banner> findList(Banner query) {
		return mapper.findList(null,query);
	}

	@Override
	@Transactional
	public int delete(Banner query) {
		//不需要删除上传的图片，由图片管理删除
		return query.deleteById()?1:0;
	}

	@Override
	public Banner find(Banner query) {
		return mapper.find(query);
	}
	
	@Override
	public PageModel<Banner> page(Banner query) {
		Page<Banner> page = new Page<Banner>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findList(page,query));
		return new PageModel<Banner>(page);
	}

	@Override
	public List<Banner> findForHome(int bannerSize) {
		return mapper.findForHome(bannerSize);
	}


	@Override
	public List<Banner> findAll(Banner banner) {
		// TODO Auto-generated method stub
		return mapper.findAll(banner);
	}
	


}
