package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.ServiceState;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author songyi
 * @since 2018-12-11
 */

@Mapper
public interface ServiceStateMapper extends BaseMapper<ServiceState> {
	
	List<ServiceState> findList(Page<ServiceState> page,ServiceState query);
	
	List<ServiceState> findStateList(Page<ServiceState> page,ServiceState query);
	
	int updateState(ServiceState record);

	int updateIsSentMail(@Param("isSentMail")Integer isSentMail,@Param("id") String id);
	
	int updateServiceState(ServiceState record);
	
	int updateConfig(ServiceState record);
	
	int insertRecord(ServiceState record);
	
	int deleteRecord(ServiceState record);

}
