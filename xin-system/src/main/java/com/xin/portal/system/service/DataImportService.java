package com.xin.portal.system.service;

import com.xin.portal.system.model.DataImport;
import com.xin.portal.system.model.ImportLog;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-04-19
 */
public interface DataImportService extends IService<DataImport> {

	int saveBatch(ImportLog importLog,List<Map<Integer, String>> list);
}
