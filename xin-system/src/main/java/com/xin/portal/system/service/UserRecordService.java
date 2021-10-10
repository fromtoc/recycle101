package com.xin.portal.system.service;

import com.xin.portal.system.model.UserRecord;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-03-12
 */
public interface UserRecordService extends IService<UserRecord> {
	PageModel<UserRecord> page(UserRecord query);

	List<UserRecord> findList(UserRecord userRecord);

	PageModel<UserRecord> pageShare(UserRecord query);

	int updateIntroduce(Map map);
}
