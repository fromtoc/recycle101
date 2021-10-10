package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.WxPushRecord;

import java.util.List;

/**
 * 微信推送记录 服务类
 *
 * @author zhoujun
 * @since 2019-02-27
 */
public interface WxPushRecordService extends IService<WxPushRecord> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2019-02-27 
	 */
	PageModel<WxPushRecord> page(WxPushRecord query, Integer pageNumber, Integer pageSize);

	List<WxPushRecord> findList(WxPushRecord query);
}
