package com.xin.portal.system.service;

import com.xin.portal.system.model.ListManageResource;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务类
 *
 * @author xue
 * @since 2018-12-11
 */
public interface ListManageResourceService extends IService<ListManageResource> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author xue
	 * @Date 2018-12-11 
	 */
	PageModel<ListManageResource> page(ListManageResource query, Integer pageNumber, Integer pageSize);
}
