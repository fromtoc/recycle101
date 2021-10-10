package com.xin.portal.system.service;

import com.xin.portal.system.model.Module;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 * 模板基础信息表 服务类
 *
 * @author xue
 * @since 2018-11-08
 */
public interface ModuleService extends IService<Module> {
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
	PageModel<Module> page(Module query, Integer pageNumber, Integer pageSize);
}
