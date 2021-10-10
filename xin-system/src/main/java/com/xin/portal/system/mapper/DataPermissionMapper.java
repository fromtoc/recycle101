package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.DataPermission;
import com.xin.portal.system.model.OrgDataPermission;

@Mapper
public interface DataPermissionMapper extends BaseMapper<DataPermission>{

	List<DataPermission> findPageList(Page<DataPermission> page, DataPermission query);

	List<OrgDataPermission> findPageListByOrg(Page<OrgDataPermission> page, OrgDataPermission query);

	List<DataPermission> findList(@Param("orgId")String orgId, @Param("dptype")String dbtype, @Param("dpname")String dbname);

	List<DataPermission> selectDPByUserId(@Param("userId")String userId, @Param("typeId")String typeId);

	List<DataPermission> selectDPByUserIdWithTenantId(@Param("userId")String userId, @Param("typeId")String typeId, @Param("tenantId")String tenantId);

	
}