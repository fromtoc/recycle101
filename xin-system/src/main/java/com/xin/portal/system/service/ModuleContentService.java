package com.xin.portal.system.service;

import com.xin.portal.system.model.ModuleContent;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务类
 *
 * @author xue
 * @since 2018-11-08
 */
public interface ModuleContentService extends IService<ModuleContent> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author xue
	 * @Date 2018-11-08 
	 */
	PageModel<ModuleContent> page(ModuleContent query, Integer pageNumber, Integer pageSize);
}
