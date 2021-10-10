package com.xin.portal.system.mapper;

import com.aspose.slides.Collections.Generic.List;
import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.Config;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ConfigMapper extends BaseMapper<Config>{

    Integer  updateByCode (@Param("code")String code, @Param("value")String value,@Param("tenantId")String tenantId);

    Config findByCode(@Param("code")String code,@Param("tenantId")String tenantId);

    List<Config> findListByCode(@Param("tenantId")String tenantId);

	List<Config> selectEmailParamWithTenant(String tenantId);
}