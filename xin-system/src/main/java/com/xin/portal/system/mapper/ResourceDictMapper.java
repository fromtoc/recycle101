package com.xin.portal.system.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.ResourceDict;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ResourceDictMapper extends BaseMapper<ResourceDict> {

    List<ResourceDict> findList(String resourceId);

}