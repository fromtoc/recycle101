package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.xin.portal.system.model.Module;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 模板基础信息表 Mapper 接口
 *
 * @author xue
 * @since 2018-11-08
 */
@Mapper
public interface ModuleMapper extends BaseMapper<Module> {

	List<Module> selectPage(Page<Module> page, Module query);

}
