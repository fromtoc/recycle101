package com.xin.portal.system.service;

import java.util.List;

import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.SysLog;
import com.xin.portal.system.model.UserCollect;

public interface SysLogService {

	PageModel<SysLog> page(SysLog query);
	
	Integer addLog(SysLog myLog);

	List<SysLog> getDistinctOp();

	Integer addCollectLog(UserCollect userCollect);

}
