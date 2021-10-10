package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.Datasource;

/**
 *  Mapper 接口
 *
 * @author zhoujun
 * @since 2020-04-21
 */
@Mapper
public interface DatasourceMapper extends BaseMapper<Datasource> {

	Datasource selectWithTenantId(String id, String tenantId);
	
}
