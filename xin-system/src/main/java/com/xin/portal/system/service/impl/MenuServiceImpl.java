package com.xin.portal.system.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.xin.portal.system.model.Menu;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.mapper.MenuMapper;
import com.xin.portal.system.mapper.ResourceMapper;
import com.xin.portal.system.service.MenuService;
import com.xin.portal.system.util.SessionUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 * 模块表 服务实现类
 *
 * @author zhoujun
 * @since 2018-10-31
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
	@Autowired
	private MenuMapper mapper;
	@Autowired
	private ResourceMapper resourceMapper;
	
	@Override
	public PageModel<Menu> page(Menu query, Integer pageNumber, Integer pageSize) {
		Page<Menu> page = new Page<Menu>(pageNumber, pageSize);
		EntityWrapper<Menu> ew=new EntityWrapper<Menu>(query);
		page = selectPage(page, ew);
		return new PageModel<Menu>(page);
	}

	@Override
	public List<Menu> selectChildById(String relate,String userId) {
		// TODO Auto-generated method stub
		return mapper.selectChildById(relate,userId);
	}

	@Override
	public List<Menu> findUserMenus(String userId) {
		return mapper.findUserMenus(userId,0);
	}
	@Override
	public List<Menu> findUserMobileMenus(String userId) {
		// TODO Auto-generated method stub
		return mapper.findUserMenus(userId,1);
	}
	@Override
	public List<Menu> findList(Menu query) {
		return mapper.findList(query);
	}

	@Override
	public List<Menu> findMenusByType(Menu record) {
		String userId = SessionUtil.getUserId();
		List<Menu> list = mapper.findUserMenus(userId,0);
		list.sort((a, b) -> a.getSort() - b.getSort());
		String type = record.getType();//1 菜单（父节点） 2报表（叶子节点）
		String name = record.getName();
		List<Menu> parentList = new ArrayList<>();
		List<Menu> childList = new ArrayList<>();
		for (Menu menu : list) {
			String id = menu.getId();
			boolean isChild = false;
			for (Menu menu2 : list) {
				if (menu2.getParentId().equals(id)) {//含有子节点，说明是父节点
					parentList.add(menu);
					isChild = false;
					break;
				}else if(!menu2.getParentId().equals(id)){//没有子节点  说明是叶子节点
					isChild = true;
				}
			}
			if(isChild){
				childList.add(menu);
			}
		}
		if(type!=null && type.length()>0 && type.equals("1")){//1 菜单（父节点）
			if (name!=null && name.length()>0) {
				List<Menu> nameLikeList = new ArrayList<>();
				for (Menu menup : parentList) {
					if(menup.getName().contains(name)){
						nameLikeList.add(menup);
					}
				}
				return nameLikeList;
			}
			return parentList;
		}else if (type!=null && type.length()>0 && type.equals("2")){// 2报表（叶子节点）
			if (name!=null && name.length()>0) {
				List<Menu> nameLikeList = new ArrayList<>();
				for (Menu menuc : childList) {
					if(menuc.getName().contains(name)){
						nameLikeList.add(menuc);
					}
				}
				return nameLikeList;
			}
			return childList;
		}
		return null;
	}

	@Override
	public List<Menu> getChildByUserId(Menu record, String userId) {
		String parentId = record.getParentId();
		List<Menu> list = mapper.findUserMenus(userId,0);
		List<Menu> childList = new ArrayList<>();
		for (Menu menu : list) {
			if((menu.getParentId()).equals(parentId)){
				childList.add(menu);
			}
		}
		if(childList!=null && childList.size()>0){
			childList.sort((a, b) -> a.getSort() - b.getSort());
		}
		return childList;
	}
	
	@Override
	public List<Menu> findUserResourceMenus(String userId){
		return mapper.findUserResourceMenus(userId,0);
	}

	@Override
	public List<Resource> selectMenuResourceByPermission(String userId, String menuId) {
		return resourceMapper.selectMenuResourceByPermission(userId,menuId);
	}
}
