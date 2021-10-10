package com.xin.portal.system.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.mapper.DataImportMapper;
import com.xin.portal.system.mapper.ImportLogMapper;
import com.xin.portal.system.model.DataImport;
import com.xin.portal.system.model.ImportLog;
import com.xin.portal.system.service.DataImportService;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-04-19
 */
@Service
public class DataImportServiceImpl extends ServiceImpl<DataImportMapper, DataImport> implements DataImportService {

	@Autowired
	private ImportLogMapper importMapper;
	@Autowired
	private DataImportMapper mapper;
	
	@Override
	public int saveBatch(ImportLog importLog ,List<Map<Integer, String>> list) {
		int result = 0;
		try {
			result = mapper.saveBatch(importLog.getTableName(), list);
			//importLog.setState(result>0?1:0);
		} catch (Exception e) {
			//importLog.setState(0);
		}
		//importMapper.insert(importLog);
		return result;
	}
}
