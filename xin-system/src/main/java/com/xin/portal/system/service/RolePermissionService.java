package com.xin.portal.system.service;

import com.xin.portal.system.model.RolePermission;
import com.xin.portal.system.model.RoleUser;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2018-11-15
 */
public interface RolePermissionService extends IService<RolePermission> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-11-15 
	 */
	PageModel<RolePermission> page(RolePermission query, Integer pageNumber, Integer pageSize);

	boolean saveAll(RolePermission record);

	PageModel<RolePermission> pagePermissionUser(RolePermission query);

	void insertRolePserssionToMsgCenter(String userId, RolePermission record, String type);
}
