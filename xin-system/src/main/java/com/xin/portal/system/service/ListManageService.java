package com.xin.portal.system.service;

import java.util.List;

import com.xin.portal.system.model.ListManage;
import com.xin.portal.system.model.ListManageResource;
import com.xin.portal.system.model.Resource;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务类
 *
 * @author xue
 * @since 2018-12-10
 */
public interface ListManageService extends IService<ListManage> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author xue
	 * @Date 2018-12-10 
	 */
	PageModel<Resource> page(ListManageResource query, Integer pageNumber, Integer pageSize);

	List<Resource> selectByListId(String id, String userId);
}
