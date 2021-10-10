package com.xin.portal.bi.service;

import com.xin.portal.bi.model.UserReport;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.UserShare;
import com.baomidou.mybatisplus.service.IService;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-06-05
 */
public interface UserReportService extends IService<UserReport> {

	PageModel<UserReport> page(UserReport query);
	
	public PageModel<UserReport> pageShare(UserReport query);

	boolean updateNum(String id, String string, String methodType);

	UserShare findReportConmmont(UserShare uquery);

}
