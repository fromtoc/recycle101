package com.xin.portal.system.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.ThirdAppParamMapper;
import com.xin.portal.system.model.ThirdAppParam;
import com.xin.portal.system.service.ThirdAppParamService;
import com.xin.portal.system.util.line.bean.LineBotProperties;

@Service
public class ThirdAppParamServiceImpl extends ServiceImpl<ThirdAppParamMapper, ThirdAppParam> implements ThirdAppParamService {

    @Autowired
	private ThirdAppParamMapper mapper;

	@Override
	public PageModel<ThirdAppParam> page(ThirdAppParam record) {
		Page<ThirdAppParam> page = new Page<ThirdAppParam>(record.getPageNumber(), record.getPageSize());
		record.setIsEnable("1");
		page.setRecords(mapper.findPageList(page, record));
		return new PageModel<ThirdAppParam>(page);
	}

	@Override
	public List<ThirdAppParam> findeListByParam(ThirdAppParam record) {
		record.setIsEnable("1");
		return mapper.findeListByParam(record);
	}

	@Override
	public boolean save(ThirdAppParam record) {
		return mapper.insert(record)==1?true:false;
	}

	@Override
	public boolean update(ThirdAppParam record) {
		return mapper.updateById(record)==1?true:false;
	}
	
	@Override
	public boolean delete(ThirdAppParam record) {
        return record.deleteById();
	}

	@Override
	public Map<String, LineBotProperties> getAllLineProperties() {
		Map<String, LineBotProperties> map = new HashMap<String, LineBotProperties>();
		EntityWrapper<ThirdAppParam> ew = new EntityWrapper<>();
		ew.eq("app_type", ThirdAppParam.APP_TYPE_LINE);
		List<ThirdAppParam> selectList = mapper.selectList(ew);
		LineBotProperties lineBotProperties = null;
		if (selectList != null && selectList.size() > 0) {
			for (ThirdAppParam thirdAppParam : selectList) {
				lineBotProperties = new LineBotProperties();
				lineBotProperties.setChannelId(thirdAppParam.getAppId());
				lineBotProperties.setChannelSecret(thirdAppParam.getAppSecret());
				lineBotProperties.setChannelToken(thirdAppParam.getAppToken());
				map.put(thirdAppParam.getTenantId() + "_" + thirdAppParam.getAppId(), lineBotProperties);
			}
		}
		return map;
	}

}
