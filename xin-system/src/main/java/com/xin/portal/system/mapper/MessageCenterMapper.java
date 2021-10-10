package com.xin.portal.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.xin.portal.system.model.MessageCenter;

import java.util.List;

/**
 *  Mapper 接口
 *
 * @author zhoujun
 * @since 2019-06-21
 */
@Mapper
public interface MessageCenterMapper extends BaseMapper<MessageCenter> {

	int updateAllMessageIsReadById(@Param("userId")String userId);

	List<MessageCenter> findList(@Param("receiveUser")String receiveUser,@Param("isRead")String isRead,@Param("typeList")List<Integer> typeList);

    int clearAllMessageById(@Param("userId")String userId);
    
    int insertWithTenantId(MessageCenter messageCenter);
}
