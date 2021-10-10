package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.UserDataPermission;

@Mapper
public interface UserDataPermissionMapper extends BaseMapper<UserDataPermission>{

	List<UserDataPermission> findUserDataPermission(Page<UserDataPermission> page, UserDataPermission query);

	
}