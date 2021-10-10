package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.Organization;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrganizationMapper extends BaseMapper<Organization>{

	Organization selectByExtId(@Param("extId") String extId,@Param("tenantId") String tenantId);
	
	boolean insertOrganizationAllColunmForNewTenant(Organization organization);

	Integer insertOrgForLdap(Organization organization);

	Integer updateOrgForLdap(Organization organization);
	/**查询组织内容不限制租户id*/
	List<Organization> selectListWithNoTenant(Organization orgEntity);

	void delByLdapIds(List<String> ids);
}