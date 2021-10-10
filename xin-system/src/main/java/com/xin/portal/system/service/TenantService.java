package com.xin.portal.system.service;

import com.xin.portal.system.model.Tenant;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

import java.util.List;

/**
 * 租户表 服务类
 *
 * @author zhoujun
 * @since 2018-09-27
 */
public interface TenantService extends IService<Tenant> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-09-27 
	 */
	PageModel<Tenant> page(Tenant query, Integer pageNumber, Integer pageSize);

	boolean save(Tenant record);

	List<Tenant> selectLists();
}
