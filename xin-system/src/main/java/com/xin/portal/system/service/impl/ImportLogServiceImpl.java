package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.ImportLog;
import com.xin.portal.system.mapper.ImportLogMapper;
import com.xin.portal.system.service.ImportLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-04-20
 */
@Service
public class ImportLogServiceImpl extends ServiceImpl<ImportLogMapper, ImportLog> implements ImportLogService {
	@Override
	public PageModel<ImportLog> page(ImportLog query) {
		Page<ImportLog> page = new Page<ImportLog>(query.getPageNumber(),query.getPageSize());
		EntityWrapper<ImportLog> ew=new EntityWrapper<ImportLog>(query);
		ew.orderBy("create_time", false);
		page = selectPage(page, ew);
		return new PageModel<ImportLog>(page);
	}
}
