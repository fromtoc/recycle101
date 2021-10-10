package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.DataPermissionType;

@Mapper
public interface DataPermissionTypeMapper extends BaseMapper<DataPermissionType>{

	int insertDataPermissionTypeAllColunmForNewTenant(DataPermissionType dataPermissionType);
	
}