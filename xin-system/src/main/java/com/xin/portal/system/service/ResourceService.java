package com.xin.portal.system.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.ResourcePerm;

public interface ResourceService extends IService<Resource> {
	
	public Resource save(Resource query);

	public Resource update(Resource query);

	public boolean delete(Resource query);


	public boolean saveBatch(List<Resource> list);
	/** 修改报表的设计说明 **/
	public int updateIntroduce(Map map);
	/**修改报表的收藏数目**/
	public boolean updateNum(String id, String column, String methodType);
	/**查询分享的报表**/
	Resource findSharResource(Resource query);

	public PageModel page(Resource query, Integer pageNumber, Integer pageSize);
	
	public PageModel listPage(Resource query, Integer pageNumber, Integer pageSize, List<String> resourceId);

	public PageModel pageRoleResource(Resource query, Integer pageNumber, Integer pageSize);
	
	
	public PageModel pageResourcePerm(ResourcePerm query,Integer pageNumber, Integer pageSize);

	public List<Resource> selectListResource(String relate, String userId);

	public List<Resource> selectOperationLog(int num);

	public List<Resource> findReourceLog(String userId);

	public Resource selectResource(String relate, String userId);


    int resourceData();

	int resourceActive();

    List<Resource> chart();

	boolean updateEveryoneById(String resourceId,String value);

}
