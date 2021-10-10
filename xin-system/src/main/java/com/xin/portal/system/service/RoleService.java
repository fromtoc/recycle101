package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.model.Role;

public interface RoleService extends IService<Role> {

	List<Role> selectRoleByResourceId(String resourceId);

	boolean checkUnique(Role query,String operationType);

	Role  selectByCode(String code,String tenantId);
}
