package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.UserLikes;
import com.xin.portal.system.mapper.UserLikesMapper;
import com.xin.portal.system.service.UserLikesService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-06-14
 */
@Service
public class UserLikesServiceImpl extends ServiceImpl<UserLikesMapper, UserLikes> implements UserLikesService {

	@Override
	public boolean save(UserLikes userLikes) {
		return userLikes.insert();
	}
}
