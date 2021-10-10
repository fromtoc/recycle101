package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.ListManage;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 *  Mapper 接口
 *
 * @author xue
 * @since 2018-12-10
 */
@Mapper
public interface ListManageMapper extends BaseMapper<ListManage> {
	
	boolean insertListManageAllColunmForNewTenant(ListManage listManage);
}
