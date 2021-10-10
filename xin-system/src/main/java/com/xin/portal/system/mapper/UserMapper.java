package com.xin.portal.system.mapper;

import java.util.List;

import com.xin.portal.system.model.ResourceLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.dto.UserDto;
import com.xin.portal.system.model.User;

@Mapper
public interface UserMapper extends BaseMapper<User>{
	

    List<User> findList(UserDto record);

    public User findByName(@Param("username")String username);

	List<User> selectPage(Page<User> page, User query);

	boolean updateTimeById(User u);
	
	boolean insertUserAllColunmForNewTenant(User user);

		Integer headCount();

	Integer numberActive();

	Integer monthActive();
	Integer dailyActive();

	void updateIsDeleteForTenantDelete(String tenantId);


	User findByUsernameAndTenantIds(@Param("username")String username,@Param("tenantId") String tenantId);

	boolean updateADUser(@Param("password")String password,@Param("userId")String userId,@Param("tenantId")String tenantId);

	Integer  insertUserTD(User user);

	Integer updateByName(User u);
}