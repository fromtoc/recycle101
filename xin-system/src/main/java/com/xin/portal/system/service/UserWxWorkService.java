package com.xin.portal.system.service;

import com.xin.portal.system.model.ThirdAppParam;
import com.xin.portal.system.model.UserWxWork;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;

/**
 * 在通讯录同步助手中此接口可以读取企业通讯录的所有成员信息，而自建应用可以读取该应用设置的可见范围内的成员信息。

请求方式：GET（HTTPS）
请求地址：https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token=ACCESS_TOKEN&userid=USERID 服务类
 *
 * @author zhoujun
 * @since 2019-01-07
 */
public interface UserWxWorkService extends IService<UserWxWork> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2019-01-07 
	 */
	PageModel<UserWxWork> page(UserWxWork query);

	boolean synchRelationUser(ThirdAppParam record, List<UserWxWork> insertWxUser);
}
