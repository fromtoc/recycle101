package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.ThirdAppParam;

/**
 * 
 * 第三方集成的参数mapper
 *
 */
@Mapper
public interface ThirdAppParamMapper extends BaseMapper<ThirdAppParam>{

	List<ThirdAppParam> findeListByParam(ThirdAppParam record);

	List<ThirdAppParam> findPageList(Page<ThirdAppParam> page, ThirdAppParam record);
	
}
