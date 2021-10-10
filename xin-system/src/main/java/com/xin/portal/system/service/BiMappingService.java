package com.xin.portal.system.service;

import com.xin.portal.system.model.BiMapping;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 * 用户映射表 服务类
 *
 * @author zhoujun
 * @since 2018-12-07
 */
public interface BiMappingService extends IService<BiMapping> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-12-07 
	 */
	PageModel<BiMapping> page(BiMapping query, Integer pageNumber, Integer pageSize);

	List<BiMapping> findList(BiMapping query);
	
	List<BiMapping> selectBiUserBySysUserAndServerId(BiMapping query);
	/**
	 * 根据类型查询用户映射数量
	 * @param type
	 * @return
	 */
	int selectUserMappingCountByType(String type);
}
