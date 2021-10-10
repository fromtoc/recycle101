package com.xin.portal.system.service;

import com.xin.portal.system.model.ResourceType;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

import java.util.List;

/**
 * 资源类型表 服务类
 *
 * @author zhoujun
 * @since 2018-10-31
 */
public interface ResourceTypeService extends IService<ResourceType> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	PageModel<ResourceType> page(ResourceType query, Integer pageNumber, Integer pageSize);

	/**
	 *
	 * @param userId
	 * @return
	 */
	public List<ResourceType> findResourceTypeList(String userId);
}
