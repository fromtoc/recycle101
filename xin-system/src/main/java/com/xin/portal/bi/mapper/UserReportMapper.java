package com.xin.portal.bi.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xin.portal.bi.model.UserReport;
import com.xin.portal.system.model.UserShare;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun123
 * @since 2018-06-05
 */

@Mapper
public interface UserReportMapper extends BaseMapper<UserReport> {

	List<UserReport> findList(Page<UserReport> page, UserReport query);

	List<UserReport> findShareList(Page<UserReport> page, UserReport query);

	int updateNum(@Param("id")String id, @Param("column")String column, @Param("methodType")String methodType);

	UserShare findReportConmmont(UserShare uquery);
	
}
