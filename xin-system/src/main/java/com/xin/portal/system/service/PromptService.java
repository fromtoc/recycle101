package com.xin.portal.system.service;

import com.xin.portal.system.model.Prompt;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun
 * @since 2018-01-08
 */
public interface PromptService extends IService<Prompt> {
	PageModel<Prompt> page(Prompt query);

	PageModel<Prompt> pageSmartBi(Prompt query);

	/**  @Title: del 
	 * @Description:  TODO
	 * @param record
	 * @return boolean
	 * @author zhoujun
	 * @Date 2018年1月22日 上午11:06:02 
	 */
	boolean del(Prompt record);
	
	PageModel<Prompt> findPromptDictInfo(Prompt record);

	List<Prompt> findDictByDictCode(String dictCode);
}
