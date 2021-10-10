package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.ResourceType;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * 资源类型表 Mapper 接口
 *
 * @author zhoujun
 * @since 2018-10-31
 */
@Mapper
public interface ResourceTypeMapper extends BaseMapper<ResourceType> {

    public List<ResourceType> findResourceTypeList(String userId);

    boolean insertResourceTypeAll(ResourceType resourceType);

}
