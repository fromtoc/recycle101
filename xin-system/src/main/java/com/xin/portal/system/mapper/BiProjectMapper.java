package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.BiProject;

@Mapper
public interface BiProjectMapper extends BaseMapper<BiProject>{

	List<BiProject> findList(BiProject query);

	BiProject findById(String id);

	int updateProjectIndepend(String id);

	List<BiProject> findList(Page<BiProject> page, BiProject query);

	BiProject findByIdWithTenantId(BiProject biProject);
}