package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xin.portal.system.model.UserLineWork;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 
 * line用户信息和portal用户的对应关系
 * @author zhoujun
 * @since 2019-01-07
 */
@Mapper
public interface UserLineWorkMapper extends BaseMapper<UserLineWork> {

	List<UserLineWork> findListForPush(@Param("ids") String ids, @Param("roleIds") String roleIds, @Param("orgIds") String orgIds, @Param("tenantId") String tenantId);
    
}
