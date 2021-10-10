package com.xin.portal.system.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.UserDataPermissionMapper;
import com.xin.portal.system.mapper.UserInfoMapper;
import com.xin.portal.system.model.OrgDataPermission;
import com.xin.portal.system.model.UserDataPermission;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.service.UserDataPermissionService;

@Service
public class UserDataPermissionServiceImpl  extends ServiceImpl<UserDataPermissionMapper, UserDataPermission>  implements UserDataPermissionService {

	@Autowired
	private UserDataPermissionMapper mapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
	@Override
	public PageModel<UserDataPermission> pageUserDataPermission(UserDataPermission query) {
		Page<UserDataPermission> page = new Page<UserDataPermission>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findUserDataPermission(page,query));
		return new PageModel<UserDataPermission>(page);
	}

	@Override
	public int deleteByUser(UserDataPermission query) {
		EntityWrapper<UserDataPermission> ew = new EntityWrapper<>();
		ew.eq("user_id", query.getUserId());
		ew.eq("dp_id", query.getDpId());
		return mapper.delete(ew);
	}

	@Override
	public boolean saveUserDataPermission(String userId, String[] dataPermissions) {
		List<UserDataPermission> list = Lists.newArrayList();
		UserDataPermission udp = null;
		for (String dpId : dataPermissions) {
			udp = new UserDataPermission();
			udp.setUserId(userId);
			udp.setDpId(dpId);
			udp.setState(1);
			list.add(udp);
		}
		return insertBatch(list);
	}

	@Override
	public Integer deleteUserPerOfOrg(String orgId, String[] dataPermissions) {
		//获取组织中的所有用户
		EntityWrapper<UserInfo> ewu = new EntityWrapper<>();
		ewu.eq("org_id", orgId);
		List<UserInfo> list =  userInfoMapper.selectList(ewu);
		List<String> userIdList = list.stream().map(UserInfo::getId).collect(Collectors.toList());
		//根据用户和组织数据权限删除用户数据权限的数据
		EntityWrapper<UserDataPermission> ewud = new EntityWrapper<>();
		ewud.in("user_id", userIdList);
		ewud.in("dp_id", dataPermissions);
		return mapper.delete(ewud);
	}

}
