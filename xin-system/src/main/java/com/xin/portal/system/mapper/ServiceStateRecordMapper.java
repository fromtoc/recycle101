package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.ServiceState;
import com.xin.portal.system.model.ServiceStateRecord;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author songyi
 * @since 2018-12-11
 */

@Mapper
public interface ServiceStateRecordMapper extends BaseMapper<ServiceStateRecord> {
	
	List<ServiceStateRecord> findList(Page<ServiceStateRecord> page,ServiceStateRecord query);
	
	int insertRecord(ServiceStateRecord record);
	
	int deleteRecord(ServiceStateRecord record);

}
