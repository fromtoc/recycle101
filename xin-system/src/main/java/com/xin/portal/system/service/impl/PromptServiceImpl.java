package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.Prompt;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.mapper.PromptMapper;
import com.xin.portal.system.mapper.PromptRelMapper;
import com.xin.portal.system.service.PromptService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun
 * @since 2018-01-08
 */
@Service
public class PromptServiceImpl extends ServiceImpl<PromptMapper, Prompt> implements PromptService {
	
	@Autowired
	private PromptRelMapper promptRelMapper;
	@Autowired
	private PromptMapper mapper;
	
	@Override
	public PageModel<Prompt> page(Prompt query) {
		Page<Prompt> page = new Page<Prompt>(query.getPageNumber(),query.getPageSize());
		EntityWrapper<Prompt> ew=new EntityWrapper<Prompt>();
		if (StringUtils.isNotEmpty(query.getName())) {
			ew.like("name", query.getName());
		}
		if (StringUtils.isNotEmpty(query.getCode())) {
			ew.like("code", query.getCode());
		}
		ew.orderBy("update_time", false);
		page = selectPage(page, ew);
		return new PageModel<Prompt>(page);
	}

	@Override
	public PageModel<Prompt> pageSmartBi(Prompt query) {
		Page<Prompt> page = new Page<Prompt>(query.getPageNumber(),query.getPageSize());
		EntityWrapper<Prompt> ew=new EntityWrapper<Prompt>();
		if (StringUtils.isNotEmpty(query.getName())) {
			ew.like("name", query.getName());
		}
		if (StringUtils.isNotEmpty(query.getCode())) {
			ew.like("code", query.getCode());
		}
		ew.where("prompt_type='1'");
		ew.or("prompt_type='4'");
		page = selectPage(page, ew);
		return new PageModel<Prompt>(page);
	}

	@Transactional
	@Override
	public boolean del(Prompt record) {
		PromptRel promptRel = new PromptRel();
		promptRel.setPromptId(record.getId());
		promptRelMapper.delete(new EntityWrapper<PromptRel>(promptRel));
		return record.deleteById();
	}
	
	@Override
	public List<Prompt> findDictByDictCode(String dictCode) {
		return mapper.findDictByDictCode(dictCode);
	}
	
	@Override
	public PageModel<Prompt> findPromptDictInfo(Prompt record) {
		Page<Prompt> page = new Page<Prompt>(record.getPageNumber(),record.getPageSize());
		page.setRecords(mapper.findDictByDictCode(page, record.getDictCode()));
		return new PageModel<Prompt>(page);
	}
}
