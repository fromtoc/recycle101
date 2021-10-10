package com.xin.portal.system.mapper;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.model.User;
import com.xin.portal.system.model.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import com.xin.portal.system.model.UserRecord;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-12
 */

@Mapper
public interface UserRecordMapper extends BaseMapper<UserRecord> {

	List<UserRecord> findList(UserRecord query);

	List<UserRecord> findShareList(UserRecord query);

	int updateIntroduce(Map map);















}
