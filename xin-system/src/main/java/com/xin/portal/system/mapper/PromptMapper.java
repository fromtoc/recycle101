package com.xin.portal.system.mapper;

import com.xin.portal.system.model.Prompt;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun
 * @since 2018-01-08
 */
@Mapper
public interface PromptMapper extends BaseMapper<Prompt> {
	
	List<Prompt> findDictByDictCode(String dictCode);
	
	List<Prompt> findDictByDictCode(Page<Prompt> page, String dictCode);
}
