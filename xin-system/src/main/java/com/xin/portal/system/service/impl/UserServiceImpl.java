package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.ResourceLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.bi.mstr.api.UserApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.BiUserMapper;
import com.xin.portal.system.mapper.UserMapper;
import com.xin.portal.system.model.BiProject;
import com.xin.portal.system.model.BiUser;
import com.xin.portal.system.model.User;
import com.xin.portal.system.service.UserService;
import com.xin.portal.system.util.Constant;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	
	@Autowired
	private UserMapper mapper;
	@Autowired
	private BiUserMapper biUsermapper;
	@Autowired
    protected CacheManager cacheManager;

	@Override
	public User findByUsername(String username) {
		return mapper.findByName(username);
	}

	@Override
	public PageModel<User> page(User query) {
		Page<User> page = new Page<User>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.selectPage(page, query));
		return new PageModel<User>(page);
	}

	@Override
	public boolean deleteUser(User user) {
		/*BiUser query = new BiUser();
		query.setLoginName(user.getUsername());
		BiUser mstrUser = biUsermapper.selectOne(query);
		if (mstrUser!=null) {
			Cache cache = cacheManager.getCache(Constant.CACHE_DEFAULT);
			BiProject biProject = cache.get(Constant.CACHE_MSTR_PROJECT+0,BiProject.class);
			UserManager.enable(biProject,mstrUser.getMstrId(), false);
		}
		return user.updateById();*/
		return true;
	}

	@Override
	public boolean updateTimeById(User u) {
		// TODO Auto-generated method stub
		return mapper.updateTimeById(u);
	}
@Override
	public Integer heabCount() {
		return mapper.headCount();
	}

	@Override
	public Integer numberActive() {
		return mapper.numberActive();
	}

	@Override
	public Integer monthActive() {
		return mapper.monthActive();
	}

	@Override
	public Integer dailyActive() {
		return mapper.dailyActive();
	}

	@Override
	public void updateIsDeleteForTenantDelete(String tenantId) {
		mapper.updateIsDeleteForTenantDelete(tenantId);
	}

	@Override
	public User findByUsernameAndTenantIds(String username, String tenantId) {
		return mapper.findByUsernameAndTenantIds(username,tenantId);
	}

	@Override
	public Integer updateByName(User u) {
		return mapper.updateByName(u);
	}


}
