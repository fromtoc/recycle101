package com.xin.portal.system.service;

import com.xin.portal.system.model.MoType;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 * 模块类型表 服务类
 *
 * @author xue
 * @since 2018-11-08
 */
public interface MoTypeService extends IService<MoType> {
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
	PageModel<MoType> page(MoType query, Integer pageNumber, Integer pageSize);
}
