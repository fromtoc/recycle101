package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ResourceCommentMapper {

	Integer insertResourceComment(@Param("resourceId")Integer resourceId, @Param("commentId")Integer commentId,@Param("resourceType")Integer resourceType);

}
