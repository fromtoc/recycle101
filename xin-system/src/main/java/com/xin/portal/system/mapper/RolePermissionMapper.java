package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xin.portal.system.model.RolePermission;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 *  Mapper 接口
 *
 * @author zhoujun
 * @since 2018-11-15
 */
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

	List<RolePermission> findPermissionRoleUser(Page<RolePermission> page, RolePermission query);
	
	List<String> findDistinctResourceIdByRoleId(@Param("roleIds")List<String> roleIds,@Param("code")String permissionCode);
	
	List<String> findDistinctResourceIdByUserIdAndNotEqualRoleId(@Param("roleId")String roleId,@Param("userId")String userId,@Param("code")String permissionCode);

	Integer insertRolePermission(RolePermission  rolePermission);
}
