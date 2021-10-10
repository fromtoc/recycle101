package com.xin.portal.system.service;

import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.PromptRel;
import com.xin.portal.system.model.Resource;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-09
 */
public interface PromptRelService extends IService<PromptRel> {

	/**  @Title: findList 
	 * @Description:  TODO
	 * @param query
	 * @return List<PromptRel>
	 * @author zhoujun
	 * @Date 2018年1月9日 下午1:33:59 
	 */
	List<PromptRel> findList(PromptRel query);

	/**  @Title: find 
	 * @Description:  TODO
	 * @param query
	 * @return Object
	 * @author zhoujun
	 * @Date 2018年1月9日 下午4:18:22 
	 */
	PromptRel find(PromptRel query);
	
	/**
	 * @Title: check 
	 * @Description:  检查是否已经绑定报表
	 * @param query
	 * @return PromptRel
	 * @author zhoujun
	 * @Date 2018年1月22日 上午11:32:47
	 */
	List<PromptRel> check(PromptRel query);

	boolean saveBatch(String resourceId, List<PromptRel> list);

	BaseApi getPromptDict(String promptId);

	PageModel<Resource> findResourceList(PromptRel record, Integer pageNumber, Integer pageSize);



}
