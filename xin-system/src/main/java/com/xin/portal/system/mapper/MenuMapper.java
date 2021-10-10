package com.xin.portal.system.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.xin.portal.system.model.Menu;
import com.xin.portal.system.model.User;
import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 模块表 Mapper 接口
 *
 * @author zhoujun
 * @since 2018-10-31
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

	List<Menu> selectChildById(@Param("relate")String relate,@Param("userId")String userId);

	List<Menu> findUserMenus(@Param("userId")String userId, @Param("isMobile")Integer isMobile);
	
	List<Menu> findList(Menu query);

	boolean insertMenuAllColunmForNewTenant(Menu menu);
	
	List<Menu> findMenuFromResourceIds(@Param("resourceIds")List<String> resourceIds);
	/**查询用户拥有权限的菜单*/
	List<Menu> selectMenuByUserPermission(@Param("userId")String userId,@Param("code")String code,@Param("resourceId")String resourceId,@Param("noRoleId")String noRoleId);

	List<Menu> findUserResourceMenus(@Param("userId")String userId,@Param("isMobile")Integer isMobile);
}
