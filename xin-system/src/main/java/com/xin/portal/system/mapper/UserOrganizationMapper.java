package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.Organization;
import com.xin.portal.system.model.UserOrganization;

@Mapper
public interface UserOrganizationMapper extends BaseMapper<UserOrganization>{

	List<Organization> selectOrgbyUserId(@Param("userId")String userId);

	int deleteDeputyOrgByUserId(@Param("userId")String userId, @Param("isDeputy")Integer isDeputy);

	int insertUserOrganizationForNewTenant(UserOrganization userOrg);

}
