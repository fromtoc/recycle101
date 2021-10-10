package com.xin.portal.system.service;

import com.xin.portal.system.model.ServiceState;
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
public interface ServiceStateService extends IService<ServiceState> {
	
	PageModel<ServiceState> page(ServiceState query);
	
	PageModel<ServiceState> statePage(ServiceState query);
	
	boolean updateState(ServiceState record);

	boolean updateIsSentMail(Integer isSentMail,String id);
	
	boolean updateServiceState(ServiceState record);
	
	boolean updateConfig(ServiceState record);
	
	boolean insertRecord(ServiceState record);
	
	boolean deleteRecord(ServiceState record);

}
