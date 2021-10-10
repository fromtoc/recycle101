package com.xin.portal.system.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.BiServer;
import com.xin.portal.system.model.BiUser;
/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zhoujun123
 * @since 2018-01-22
 */
public interface BiUserService extends IService<BiUser> {
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
	PageModel<BiUser> page(BiUser query, Integer pageNumber, Integer pageSize);

	boolean del(BiUser record);

	boolean saveBatch(BiServer biServer,List<BiUser> biUserListUniqueDb);

	/**
	 * 检查Mstr 账户
	 * @param query
	 */
	void check(BiUser query);

	List<BiUser> findList(BiUser query);

	BiUser findBiUser(String serverId,String userId);
}
