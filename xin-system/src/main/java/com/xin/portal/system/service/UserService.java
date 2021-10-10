package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.ResourceLog;
import com.xin.portal.system.model.User;

import java.util.List;

public interface UserService  extends IService<User> {
	
	public User findByUsername(String username);

	public PageModel<User> page(User query);

	public boolean deleteUser(User user);

	public boolean updateTimeById(User u);
	Integer heabCount();

	Integer numberActive();

	Integer monthActive();
	Integer dailyActive();

	void updateIsDeleteForTenantDelete(String tenantId);


	User findByUsernameAndTenantIds(String username, String tenantId);

	Integer updateByName(User u);


}
