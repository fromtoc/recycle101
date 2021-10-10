package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.UserSetting;

/**
 *  服务类
 *
 * @author zhoujun
 * @since 2019-07-11
 */
public interface UserSettingService extends IService<UserSetting> {

	int updateUserSetting(UserSetting userSetting);

	UserSetting selectByUserIdAndInsert(String userId);
	
	UserSetting selectByUserId(String userId);
}
