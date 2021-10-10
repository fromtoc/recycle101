package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.model.UserInfo;

@Mapper
public interface RoleUserMapper extends BaseMapper<RoleUser>{

	List<RoleUser> findRoleCodes(@Param("userId")Integer userId);

	List<String> findRoleByUser(RoleUser query);

	Integer deleteByUserId(Integer userId);

	Integer saveRoleUserByUserId(@Param("userId")Integer userId, @Param("roleIds")String[] roleIds);

	Integer saveRoleUserByRoleId(@Param("roleId")Integer roleId, @Param("roleCode")String roleCode,
			@Param("userIds")Integer[] userIds);

	int delete(RoleUser query);

	void deleteByRoleId(String id);

	List<UserInfo> findUsersByRole(RoleUser roleUser);
	
	boolean insertRoleUserAllColunmForNewTenant(RoleUser roleUser);
	
	List<RoleUser> findUsersByRoleIds(@Param("roleIds")List<String> roleIds);

	Integer insertRoleUserTD(RoleUser  roleUser);
}