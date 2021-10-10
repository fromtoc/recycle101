package com.xin.portal.system.service;

import com.xin.portal.system.model.SysRelease;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2018-10-23
 */
public interface SysReleaseService extends IService<SysRelease> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-10-23 
	 */
	PageModel<SysRelease> page(SysRelease query, Integer pageNumber, Integer pageSize);
}
