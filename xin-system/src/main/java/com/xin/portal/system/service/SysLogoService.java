package com.xin.portal.system.service;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.SysLogo;

/**
 * 系统logo设置 服务类
 *
 * @author zhoujun
 * @since 2019-01-04
 */
public interface SysLogoService extends IService<SysLogo> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2019-01-04 
	 */
	PageModel<SysLogo> page(SysLogo query);

	BaseApi updateSysLogo(SysLogo record);

	BaseApi saveSysLog(SysLogo record);
}
