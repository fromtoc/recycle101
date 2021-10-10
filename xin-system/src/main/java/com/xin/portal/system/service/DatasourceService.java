package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.Datasource;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2020-04-21
 */
public interface DatasourceService extends IService<Datasource> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2020-04-21 
	 */
	PageModel<Datasource> page(Datasource query, Integer pageNumber, Integer pageSize);
}
