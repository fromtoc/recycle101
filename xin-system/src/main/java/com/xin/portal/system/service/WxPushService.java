package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.WxPush;

/**
 * 微信推送表 服务类
 *
 * @author zhoujun
 * @since 2019-02-11
 */
public interface WxPushService extends IService<WxPush> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2019-02-11 
	 */
	PageModel<WxPush> page(WxPush query, Integer pageNumber, Integer pageSize);

    boolean save(WxPush record);

	boolean updatePush(WxPush record);

	boolean sendEmail(WxPush record);

	boolean sendSysMessage(WxPush record);

	BaseApi startTask(WxPush record);

	BaseApi pauseTask(WxPush record);

	BaseApi restoreTask(WxPush record);

	boolean runTaskOnce(WxPush record);
}
