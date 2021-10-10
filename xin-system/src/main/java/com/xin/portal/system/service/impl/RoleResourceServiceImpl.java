package com.xin.portal.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xin.portal.system.mapper.RoleResourceMapper;
import com.xin.portal.system.model.RoleResource;
import com.xin.portal.system.service.RoleResourceService;

@Service
public class RoleResourceServiceImpl extends ServiceImpl<RoleResourceMapper,RoleResource> implements RoleResourceService {
	
	@Autowired
	private RoleResourceMapper mapper;

	@Transactional
	@Override
	public int saveByRoleId(String roleId, String[] ids) {
		EntityWrapper<RoleResource> ew = new EntityWrapper<>();
		ew.eq("role_id", roleId);
		ew.in("resource_id", ids);
//		mapper.delete(ew);
		List<RoleResource> records = selectList(ew);
		boolean resultSave = true;
		if (ids!=null && ids.length>0) {
			//resultSave = mapper.saveByRoleId(roleId,ids);
			List<RoleResource> list = Lists.newArrayList();
			for (String id : ids) {
				RoleResource rec = new RoleResource(roleId, id);
				if(!records.contains(rec)){
					list.add(rec);
				}
			}
			if (list.size()>0) {
				resultSave = insertBatch(list);
			}
		}
		return resultSave ? 1 : 0;
	}

	@Transactional
	@Override
	public int saveByResId(String resId, String[] resIds, String[] delRoleIds, String[] saveRoleIds) {
		EntityWrapper<RoleResource> ew = new EntityWrapper<>();
		ew.eq("resource_id", resId);
		Integer resultDelRes = mapper.delete(ew);
		
		boolean resultSave = true;
		
		
		if (saveRoleIds!=null && saveRoleIds.length>0) {
			List<RoleResource> list = Lists.newArrayList();
			for (String roleId : saveRoleIds) {
				list.add(new RoleResource(roleId, resId));
			}
			resultSave = insertBatch(list);
		}
		return resultDelRes + (resultSave?1:0);
	}


}
