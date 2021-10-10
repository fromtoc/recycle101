package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.WxPushRecordMapper;
import com.xin.portal.system.model.WxPushRecord;
import com.xin.portal.system.service.WxPushRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微信推送记录 服务实现类
 *
 * @author zhoujun
 * @since 2019-02-27
 */
@Service
public class WxPushRecordServiceImpl extends ServiceImpl<WxPushRecordMapper, WxPushRecord> implements WxPushRecordService {

	@Autowired
	private WxPushRecordMapper mapper;

	@Override
	public PageModel<WxPushRecord> page(WxPushRecord query, Integer pageNumber, Integer pageSize) {
		Page<WxPushRecord> page = new Page<WxPushRecord>(pageNumber, pageSize);
//		EntityWrapper<WxPushRecord> ew = new EntityWrapper<WxPushRecord>(query);
		List<WxPushRecord> list = mapper.page(page, query);
		page.setRecords(list);
//		page = selectPage(page);
		return new PageModel<WxPushRecord>(page);
	}

	@Override
	public List<WxPushRecord> findList(WxPushRecord query) {
		return mapper.findList(query);
	}
}
