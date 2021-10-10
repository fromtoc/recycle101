package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.Dict;


public interface DictService extends IService<Dict> {

	public PageModel<Dict> page(Dict query, Integer pageNumber, Integer pageSize);

	public List<Dict> listItem(Dict query);
	
	public List<Dict> findListByName(Dict query);

	public int delByCode(String dictCode);

	public int delById(String id);
	public List<Dict> list(Dict query);
	public int update(Dict dict);

	public Dict find(Dict query);


	List<Dict> findBytype();
}
