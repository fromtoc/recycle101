package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.UserSettingMapper;
import com.xin.portal.system.model.UserSetting;
import com.xin.portal.system.service.UserSettingService;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2019-07-11
 */
@Service
public class UserSettingServiceImpl extends ServiceImpl<UserSettingMapper, UserSetting> implements UserSettingService {
	
	@Autowired
	private UserSettingMapper mapper;
	
	@Override
	public int updateUserSetting(UserSetting userSetting) {
		return mapper.updateById(userSetting);
	}

	@Override
	public UserSetting selectByUserIdAndInsert(String userId) {
		UserSetting userSetting = mapper.selectByUseId(userId);
		UserSetting setting = new UserSetting();
		if(userSetting==null){
			setting.setUserId(userId);
			setting.setSystemMsg(1);
			setting.setCommentMsg(1);
			setting.setNoticeMsg(1);
			setting.setIssueMsg(1);
			int count = mapper.insert(setting);
			if(count>0){
				userSetting = setting;
			}
		}
		return userSetting;
	}

	@Override
	public UserSetting selectByUserId(String userId) {
		EntityWrapper<UserSetting> ew = new EntityWrapper<>();
		ew.eq("user_id", userId);
		List<UserSetting> list = mapper.selectList(ew);
		if(list!=null && list.size()>0){
			return list.get(0);
		}
		return null;
	}
}
