package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.BiIndependent;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */

@Mapper
public interface BiIndependentMapper extends BaseMapper<BiIndependent> {
	
	List<BiIndependent> find(BiIndependent biIndependent);
	
	List<BiIndependent> find(Page<BiIndependent> page,BiIndependent query);
	
	List<BiIndependent> findByQuery(BiIndependent query);

}
