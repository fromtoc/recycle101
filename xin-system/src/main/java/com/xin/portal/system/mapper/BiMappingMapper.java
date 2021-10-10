package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.BiMapping;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * 用户映射表 Mapper 接口
 *
 * @author zhoujun
 * @since 2018-12-07
 */
@Mapper
public interface BiMappingMapper extends BaseMapper<BiMapping> {

	List<BiMapping> findList(BiMapping query);

	List<BiMapping> findList(Page<BiMapping> page, BiMapping query);
	
	List<BiMapping> selectBiUserBySysUserAndServerId(BiMapping query);

	List<BiMapping> selectBiUserBySysUserAndServerIdWithTenantId(BiMapping bm);

	int selectUserMappingCountByType(String type);
	
	

}
