package com.xin.portal.system.service;

import com.xin.portal.system.model.UserLikes;
import com.baomidou.mybatisplus.service.IService;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-06-14
 */
public interface UserLikesService extends IService<UserLikes> {

	boolean save(UserLikes userLikes);
}
