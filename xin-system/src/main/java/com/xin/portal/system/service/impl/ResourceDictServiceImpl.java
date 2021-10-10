package com.xin.portal.system.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.ResourceDictMapper;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.ResourceDictService;
import com.xin.portal.system.util.SessionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
public class ResourceDictServiceImpl extends ServiceImpl<ResourceDictMapper,ResourceDict> implements ResourceDictService {
	
	@Autowired
	private ResourceDictMapper mapper;


	@Override
	public PageModel<ResourceDict> page(String resourceId, Integer pageNumber, Integer pageSize,String name) {
		Page<ResourceDict> page = new Page<ResourceDict>(pageNumber, pageSize);
		/*ResourceDict rd = new ResourceDict();*/
		/*rd.setResourceId(resourceId);*/
		EntityWrapper<ResourceDict> ew=new EntityWrapper<ResourceDict>();
		if(name!=null && !"".equals(name)){
			ew.like("name",name);
		}
		ew.eq("resource_id",resourceId);
		page = selectPage(page, ew);
		return new PageModel<ResourceDict>(page);
	}

	@Override
	public List<ResourceDict> findList(String resourceId) {
		return mapper.findList(resourceId);
	}

	@Override
	@Transactional
	public boolean saveBatch(List<ResourceDict> list,String resourceId) {
		EntityWrapper<ResourceDict> ew = new EntityWrapper<>();
		ew.eq("resource_id",resourceId);
		//先删除该资源所有字典数据
		delete(ew);
		UserInfo userInfo = SessionUtil.getUserInfo();
		for(ResourceDict resourceDict:list){
			resourceDict.setModifier(userInfo.getId());
			resourceDict.setModifyTime(new Date());
		}
		if(list.size()>0){
			//将页面的资源字典数据全部插入
			return insertBatch(list);
		}else{
			return true;
		}

	}
}
