package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.BiIndependent;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
public interface BiIndependentService extends IService<BiIndependent> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-12-04 
	 */
	PageModel<BiIndependent> page(BiIndependent query);

	List<BiIndependent> findList(BiIndependent query);

	int save(BiIndependent record);

	List<BiIndependent> findByQuery(BiIndependent query);

	BaseApi checkOpen(BiIndependent query);

	String getMstrUrlByPrjectId(String id);

}
