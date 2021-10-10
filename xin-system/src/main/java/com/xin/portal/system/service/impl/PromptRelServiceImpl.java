package com.xin.portal.system.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.DatasourceMapper;
import com.xin.portal.system.mapper.DictMapper;
import com.xin.portal.system.mapper.PromptMapper;
import com.xin.portal.system.mapper.PromptRelMapper;
import com.xin.portal.system.model.Datasource;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.Prompt;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.service.PromptRelService;
import com.xin.portal.system.util.datasource.DataSourceDaoUtil;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-09
 */
@Service
public class PromptRelServiceImpl extends ServiceImpl<PromptRelMapper, PromptRel> implements PromptRelService {
	
	@Autowired
	private PromptRelMapper mapper;
	@Autowired
	private PromptMapper promptMapper;
	@Autowired
	private DictMapper dictMapper;
	@Autowired
	private DatasourceMapper datasourceMapper;

	@Override
	public List<PromptRel> findList(PromptRel query) {
		return mapper.findList(query);
	}



	@Override
	public PromptRel find(PromptRel query) {
		return mapper.find(query);
	}

	@Override
	public List<PromptRel> check(PromptRel query) {
		return mapper.check(query);
	}

	@Override
	public boolean saveBatch(String resourceId, List<PromptRel> list) {
		boolean result = false;
		EntityWrapper<PromptRel> ew = new EntityWrapper<>();
		ew.eq("resource_id", resourceId);
		List<PromptRel> dataList= selectList(ew);
		if(dataList!=null && dataList.size()>0){
			Iterator<PromptRel> it = list.iterator();
			while(it.hasNext()){
				PromptRel pr = it.next();
				for(PromptRel data :dataList){
					String id1 = data.getPromptId();
					String id2 = pr.getPromptId();
					if(data.getPromptId().equals(pr.getPromptId())){
						it.remove();
					}
				}
			}
		}
		if (list!=null && list.size()>0) {
				result = insertBatch(list);
		}
		return result;
	}



	@Override
	public BaseApi getPromptDict(String promptId) {
		//获取dictcode
		Prompt prompt = promptMapper.selectById(promptId);
		//获取dict信息
		String dictCode = prompt.getDictCode();
		if (dictCode != null && dictCode.trim().length() > 0) {
			EntityWrapper<Dict> ew = new EntityWrapper<>();
			ew.eq("dict_code", dictCode);
			List<Dict> dictList = dictMapper.selectList(ew);
			if(dictList!=null && dictList.size() > 0){
				List<Dict> list = null;
				if(dictList.get(0).getIsSource() != null && 1 == dictList.get(0).getIsSource()){
					//获取数据源
					Datasource datasource = datasourceMapper.selectById(dictList.get(0).getSourceId());
					String sql = dictList.get(0).getQuerySql();
					list = DataSourceDaoUtil.getListDictBySql(datasource, sql);
				}else{
					list = dictList.stream().filter(item -> item.getItemName() !=null && !"".equals(item.getItemName().trim())).collect(Collectors.toList());
				}
				return BaseApi.data(list);
			}
			return BaseApi.error("-3");//未找到字典值
		}
		return BaseApi.error("-2");//筛选未设置字典值
	}



	@Override
	public PageModel<Resource> findResourceList(PromptRel record, Integer pageNumber, Integer pageSize) {
		Page<Resource> page = new Page<Resource>(pageNumber,pageSize);
		page.setRecords(mapper.findResourceList(page, record));
		return new PageModel<Resource>(page);
	}
}
