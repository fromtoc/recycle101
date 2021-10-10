package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.Permission;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 *  Mapper 接口
 *
 * @author zhoujun
 * @since 2018-11-15
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
	
	boolean insertPermissionAllColunmForNewTenant(Permission permission);
}
