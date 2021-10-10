package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.ThirdAppParam;
import com.xin.portal.system.model.UserWxWork;
import com.xin.portal.system.mapper.UserWxWorkMapper;
import com.xin.portal.system.service.UserWxWorkService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;

/**
 * 在通讯录同步助手中此接口可以读取企业通讯录的所有成员信息，而自建应用可以读取该应用设置的可见范围内的成员信息。

请求方式：GET（HTTPS）
请求地址：https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID 服务实现类
 *
 * @author zhoujun
 * @since 2019-01-07
 */
@Service
public class UserWxWorkServiceImpl extends ServiceImpl<UserWxWorkMapper, UserWxWork> implements UserWxWorkService {
	
	@Autowired
	private UserWxWorkMapper userWxWorkMapper;
	
	@Override
	public PageModel<UserWxWork> page(UserWxWork query) {
		Page<UserWxWork> page = new Page<UserWxWork>(query.getPageNumber(), query.getPageSize());
		page.setRecords(userWxWorkMapper.selectPageByParam(page, query));
		return new PageModel<UserWxWork>(page);
	}
	
	@Transactional(rollbackFor=Exception.class) 
	@Override
	public boolean synchRelationUser(ThirdAppParam record, List<UserWxWork> insertWxUser) {
		//先删除旧数据
		EntityWrapper<UserWxWork> ew = new EntityWrapper<>();
		ew.eq("app_id", record.getId());
		ew.eq("tenant_id", record.getTenantId());
		userWxWorkMapper.delete(ew);
		//删除空数据，填入新数据
		insertWxUser = insertWxUser.stream().filter(Objects::nonNull).collect(Collectors.toList());
		if(insertWxUser != null && insertWxUser.size() > 0){
			return insertBatch(insertWxUser);
		}
		return true;
	}

}
