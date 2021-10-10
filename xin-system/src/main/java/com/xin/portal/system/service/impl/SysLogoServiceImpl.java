package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.SysLogoMapper;
import com.xin.portal.system.model.SysLogo;
import com.xin.portal.system.service.SysLogoService;
import com.xin.portal.system.util.SessionUtil;

/**
 * 系统logo设置 服务实现类
 *
 * @author zhoujun
 * @since 2019-01-04
 */
@Service
public class SysLogoServiceImpl extends ServiceImpl<SysLogoMapper, SysLogo> implements SysLogoService {
	
	@Autowired
	private SysLogoMapper mapper;
	
	@Override
	public PageModel<SysLogo> page(SysLogo query) {
		Page<SysLogo> page = new Page<SysLogo>(query.getPageNumber(), query.getPageSize());
		page.setRecords(mapper.findLogoPage(page, query));
		return new PageModel<SysLogo>(page);
	}

	@Override
	public BaseApi updateSysLogo(SysLogo record) {
		String userId = SessionUtil.getUserId();
		record.setUpdater(userId);
		record.setUpdateTime(new Date());
		if(record.getIsEnable() == null || record.getIsEnable()!=1){
			record.setIsEnable(0);
		}else if(record.getIsEnable()!=null && record.getIsEnable()==1){
			Wrapper<SysLogo> ws = new EntityWrapper<>();
			ws.eq("type", record.getType());
			ws.ne("id", record.getId());
			SysLogo logo = new SysLogo();
			logo.setIsEnable(0);
			mapper.update(logo, ws);
		}
		return record.updateById() ? BaseApi.success() : BaseApi.error();
	}

	@Override
	public BaseApi saveSysLog(SysLogo record) {
		String userId = SessionUtil.getUserId();
		record.setCreater(userId);
		record.setCreateTime(new Date());
		if(record.getIsEnable() == null || record.getIsEnable()!=1){
			record.setIsEnable(0);
		}else if(record.getIsEnable()!=null && record.getIsEnable()==1){
			Wrapper<SysLogo> ws = new EntityWrapper<>();
			ws.eq("type", record.getType());
			//ws.ne("id", record.getId());
			SysLogo logo = new SysLogo();
			logo.setIsEnable(0);
			mapper.update(logo, ws);
		}
		return record.insert() ? BaseApi.success() : BaseApi.error();
	}
}
