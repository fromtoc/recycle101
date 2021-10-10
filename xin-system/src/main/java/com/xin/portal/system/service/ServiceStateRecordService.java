package com.xin.portal.system.service;

import com.xin.portal.system.model.ServiceStateRecord;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author songyi
 * @since 2018-12-11
 */
public interface ServiceStateRecordService extends IService<ServiceStateRecord> {
	
	PageModel<ServiceStateRecord> findlist(ServiceStateRecord record);
	
	boolean deleteRecord(ServiceStateRecord record);

}
