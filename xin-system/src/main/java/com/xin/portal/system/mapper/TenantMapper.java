package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.Tenant;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 租户表 Mapper 接口
 *
 * @author zhoujun
 * @since 2018-09-27
 */
@Mapper
public interface TenantMapper extends BaseMapper<Tenant> {

    List<Tenant> selectLists();
}
