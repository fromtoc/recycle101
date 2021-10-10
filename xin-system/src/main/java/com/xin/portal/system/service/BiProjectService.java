package com.xin.portal.system.service;


import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.BiProject;

public interface BiProjectService extends IService<BiProject> {
	
	public PageModel<BiProject> page(BiProject query, Integer pageNumber, Integer pageSize);

	List<BiProject> findList(BiProject query);

	BiProject findById(String id);
	
	int updateProjectIndepend(String id);

}
