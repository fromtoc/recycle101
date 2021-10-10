package com.xin.portal.system.service;

import com.xin.portal.system.model.ImportLog;
import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-04-20
 */
public interface ImportLogService extends IService<ImportLog> {
	PageModel<ImportLog> page(ImportLog query);
}
