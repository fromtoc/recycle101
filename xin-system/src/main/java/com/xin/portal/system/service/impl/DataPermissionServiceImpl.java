package com.xin.portal.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.DataPermissionMapper;
import com.xin.portal.system.mapper.OrgDataPermissionMapper;
import com.xin.portal.system.model.DataPermission;
import com.xin.portal.system.model.OrgDataPermission;
import com.xin.portal.system.service.DataPermissionService;

@Service
public class DataPermissionServiceImpl  extends ServiceImpl<DataPermissionMapper, DataPermission>  implements DataPermissionService {

    @Autowired
    private DataPermissionMapper mapper;
    @Autowired
    private OrgDataPermissionMapper orgDataPermissionMapper;
    
	@Override
	public PageModel<DataPermission> page(DataPermission query) {
		Page<DataPermission> page = new Page<DataPermission>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findPageList(page,query));
		return new PageModel<DataPermission>(page);
	}
	@Override
	public PageModel<OrgDataPermission> pageByOrg(OrgDataPermission query) {
		Page<OrgDataPermission> page = new Page<OrgDataPermission>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findPageListByOrg(page,query));
		return new PageModel<OrgDataPermission>(page);
	}
	@Override
	public int deleteByOrg(OrgDataPermission query) {
		return orgDataPermissionMapper.deleteById(query.getId());
	}
	@Override
	public List<DataPermission> findByParams(String orgId, String dbtype, String dbname) {
		return mapper.findList(orgId,dbtype,dbname);
	}

}
