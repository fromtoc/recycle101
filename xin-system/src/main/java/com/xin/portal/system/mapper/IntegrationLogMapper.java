package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.IntegrationLog;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 *  Mapper 接口
 *
 * @author zhoujun
 * @since 2018-11-27
 */
@Mapper
public interface IntegrationLogMapper extends BaseMapper<IntegrationLog> {

	List<IntegrationLog> findPage(Page<IntegrationLog> page, IntegrationLog query);

}
