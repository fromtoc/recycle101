package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.SysLog;

@Mapper
public interface SysLogMapper extends BaseMapper<SysLog>{
    int deleteByPrimaryKey(Integer id);

    SysLog selectByPrimaryKey(Integer id);

    List<SysLog> findList(SysLog record);


	List<SysLog> findPageList(Page<SysLog> page, SysLog query);

	List<SysLog> getDistinctOp();
}