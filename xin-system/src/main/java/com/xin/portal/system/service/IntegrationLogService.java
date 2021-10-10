package com.xin.portal.system.service;

import com.xin.portal.system.model.IntegrationLog;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2018-11-27
 */
public interface IntegrationLogService extends IService<IntegrationLog> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-11-27 
	 */
	PageModel<IntegrationLog> page(IntegrationLog query, Integer pageNumber, Integer pageSize);
}
