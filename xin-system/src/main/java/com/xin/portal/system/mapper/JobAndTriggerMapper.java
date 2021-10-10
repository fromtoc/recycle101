package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.JobAndTrigger;

@Mapper
public interface JobAndTriggerMapper {
	public List<JobAndTrigger> getJobAndTriggerDetails(Page<JobAndTrigger> page);
}
