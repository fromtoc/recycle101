package com.xin.portal.system.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.DatasourceMapper;
import com.xin.portal.system.mapper.DictMapper;
import com.xin.portal.system.model.Datasource;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.util.datasource.DataSourceDaoUtil;

@Service
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {
	
	@Autowired
	private DictMapper mapper;
	@Autowired
	private DatasourceMapper datasourceMapper;
	

	@Override
	public PageModel<Dict> page(Dict query, Integer pageNumber, Integer pageSize) {
		Page<Dict> page = new Page<Dict>(pageNumber, pageSize);
		//List<Dict> list = mapper.findPageList(query);
		page.setRecords(mapper.findPageList(page, query));
		return new PageModel<Dict>(page);
	}

	@Override
	public List<Dict> listItem(Dict query) {
		if(query.getIsSource() != null && query.getIsSource() == 1){
			//获取信息
			Dict dict = mapper.find(query);
			//获取数据源信息
			Datasource datasource = datasourceMapper.selectById(dict.getSourceId());
			//链接数据源获取数据
			List<Dict> list = DataSourceDaoUtil.getListDictBySql(datasource, dict.getQuerySql());
			return list;
		}
		return mapper.findItemList(query);
	}

	@Override
	public List<Dict> list(Dict query) {
		return mapper.findList(query);
	}

	@Override
	public int delByCode(String dictCode) {
		return mapper.delByCode(dictCode);
	}

	@Override
	public int delById(String id) {
		return mapper.delById(id);
	}

	@Override
	public int update(Dict dict) {
		return mapper.update(dict);
	}

	@Override
	public List<Dict> findListByName(Dict query) {
		return mapper.findListByName(query);
	}

	@Override
	public Dict find(Dict query) {
		return mapper.find(query);
	}

	@Override
	public List<Dict> findBytype() {
		return mapper.findBytype();
	}
}
