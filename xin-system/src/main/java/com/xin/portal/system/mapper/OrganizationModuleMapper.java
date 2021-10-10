package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xin.portal.system.model.OrganizationModule;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 *  Mapper 接口
 *
 * @author xue
 * @since 2018-11-08
 */
@Mapper
public interface OrganizationModuleMapper extends BaseMapper<OrganizationModule> {

	List<OrganizationModule> getModuleByOrgId(String id);

}
