package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.Role;

@Mapper
public interface RoleMapper extends BaseMapper<Role> {
	
	List<Role> findRolesByResource(@Param("resourceId")String resourceId,@Param("code")String code);
	
	boolean insertAllRoleColunmForNewTenant(Role role);

	List<Role> findByIds (List<String> roleIds);

	Role  selectByCode(@Param("code")String code,@Param("tenantId")String tenantId);
	
}