package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.Banner;
import com.xin.portal.system.model.Resource;

public interface BannerService  extends IService<Banner> {
	
	public List<Banner> findList(Banner query);

	public int delete(Banner query);

	public Banner find(Banner query);

	public PageModel<Banner> page(Banner query);

	public List<Banner> findForHome(int bannerSize);

	public List<Banner> findAll(Banner banner);

}
