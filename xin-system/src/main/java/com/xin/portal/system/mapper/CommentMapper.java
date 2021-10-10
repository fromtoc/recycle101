package com.xin.portal.system.mapper;

import java.util.List;

import com.xin.portal.system.model.AtUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.xin.portal.system.model.Comment;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 *  Mapper 接口
 *
 * @author xue
 * @since 2018-12-03
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {

	List<Comment> selectByResourceId(@Param("resourceId") String resourceId);
	
	List<Comment> selectCommentMore(Comment query, Page<Comment> page);

	List<AtUser> otherUser(@Param("resourceId") String resourceId,@Param("roleIds") List<String> roleIds,@Param("tenantId")String tenant_id,@Param("adminIds") List<String> adminIds);

	List<AtUser> adminUser(@Param("resourceId") String resourceId,@Param("roleIds") List<String> roleIds,@Param("tenantId")String tenant_id);
}
