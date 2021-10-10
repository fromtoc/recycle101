package com.xin.portal.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.mapper.RoleMapper;
import com.xin.portal.system.model.Role;
import com.xin.portal.system.service.RoleService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper,Role> implements RoleService {
	
	@Autowired
	private RoleMapper mapper;
	
	@Override
	public List<Role> selectRoleByResourceId(String resourceId) {
		return mapper.findRolesByResource(resourceId,"view");
	}

	@Override
	public boolean checkUnique(Role query,String operationType) {
		Role role = new Role();
		role.setCode(query.getCode());
		Role result= mapper.selectOne(role);
		boolean flag = true;
		if(operationType.equals("add")){
			flag = (result == null);
		}else if(operationType.equals("update")){
			if(result!=null){
				if(!result.getId().equals(query.getId())){
					flag = false;
				}else{
					flag = true;
				}
			}else{
				flag = true;
			}
		}
		return flag;
	}

	public Role  selectByCode(String code,String tenantId){
		return mapper.selectByCode(code,tenantId);
	}


}
