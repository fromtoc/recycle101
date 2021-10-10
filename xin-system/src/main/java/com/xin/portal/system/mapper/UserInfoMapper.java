package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.UserInfo;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper  extends BaseMapper<UserInfo>{

	List<UserInfo> findList(Page<UserInfo> page, UserInfo query);

	List<UserInfo> findAdUserInfoList(Page<UserInfo> page, UserInfo query);
	
	List<UserInfo> findList(UserInfo query);
	
	List<UserInfo> findRoleUserList(Page<UserInfo> page, UserInfo query);

	UserInfo findUserInfo(UserInfo query);

	List<UserInfo> selectUser(UserInfo query);

	int insertList(List<UserInfo> userInfoList);
	
	boolean insertUserInfoAllColunmForNewTenant(UserInfo userInfo);

	boolean updatePersonnalRecordById(UserInfo userInfo);
	
	List<UserInfo> findListWithTenantId(UserInfo userInfo);

	List<UserInfo> selectBatchIdsWithTenantId(@Param("userIds")List<String> userIds, @Param("tenantId")String tenantId);

	boolean updateADUser(UserInfo userInfo);

	Integer  insertUserInfoTD(UserInfo userInfo);
}