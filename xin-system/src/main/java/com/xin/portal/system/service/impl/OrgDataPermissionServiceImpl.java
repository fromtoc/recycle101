package com.xin.portal.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.DataPermissionMapper;
import com.xin.portal.system.mapper.OrgDataPermissionMapper;
import com.xin.portal.system.mapper.OrganizationMapper;
import com.xin.portal.system.model.DataPermission;
import com.xin.portal.system.model.OrgDataPermission;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.service.DataPermissionService;
import com.xin.portal.system.service.OrgDataPermissionService;

@Service
public class OrgDataPermissionServiceImpl  extends ServiceImpl<OrgDataPermissionMapper, OrgDataPermission>  implements OrgDataPermissionService {

    @Autowired
    private OrgDataPermissionMapper mapper;
    
	@Override
	public boolean saveOrgDataPermission(String orgId, String[] dataPermissions) {
		List<OrgDataPermission> list = Lists.newArrayList();
		OrgDataPermission odp = null;
		for (String dpId : dataPermissions) {
			odp = new OrgDataPermission();
			odp.setOrgId(orgId);
			odp.setDpId(dpId);
			list.add(odp);
		}
		return insertBatch(list);
	}



}
