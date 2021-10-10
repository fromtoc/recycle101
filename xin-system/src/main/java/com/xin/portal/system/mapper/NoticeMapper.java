package com.xin.portal.system.mapper;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.Notice;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-23
 */

@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {


	int publish(Notice record);
	
	List<Notice> findList(Page<Notice> page,Notice query);

	List<Notice> findListRead(Page<Notice> page,Map query);

	List<Notice> findListUnread(Page<Notice> page,Map query);
	
	int updateNum(String id);
	
	List<Notice> findAllList(Page<Notice> page,Notice query);

	Notice findOneNotice(String id);

	int update(Notice record);

	List<Notice> findListByNum(Notice notice);

	List<Notice> selectAllNoticeNotReadAndValidByUserId(@Param("userId")String userId,@Param("nowDate")Date nowDate);
	
}
