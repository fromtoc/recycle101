package com.xin.portal.system.service;

import java.util.List;

import com.xin.portal.system.model.Menu;
import com.xin.portal.system.model.Resource;

import com.baomidou.mybatisplus.service.IService;
import com.xin.portal.system.bean.PageModel;

/**
 * 模块表 服务类
 *
 * @author zhoujun
 * @since 2018-10-31
 */
public interface MenuService extends IService<Menu> {
	/**
	 * @Title: page 
	 * @Description:  分页查询
	 * @param query
	 * @param pageNumber 页数
	 * @param pageSize 每页条数
	 * @return PageModel
	 * @author zhoujun
	 * @Date 2018-10-31 
	 */
	PageModel<Menu> page(Menu query, Integer pageNumber, Integer pageSize);

	List<Menu> selectChildById(String relate,String userId);

	List<Menu> findUserMenus(String userId);
	List<Menu> findList(Menu query);
	/**查询所有的菜单通过type和name筛选内容*/
	List<Menu> findMenusByType(Menu record);
	/**获取某个用户菜单下的子节点*/
	List<Menu> getChildByUserId(Menu record, String userId);

	List<Menu> findUserResourceMenus(String userId);

	List<Menu> findUserMobileMenus(String userId);
	/**查询出菜单子节点下的有权限资源*/
	List<Resource> selectMenuResourceByPermission(String userId, String menuId);

}
