package com.xin.portal.system.service.impl;

import com.xin.portal.system.model.Menu;
import com.xin.portal.system.model.MessageCenter;
import com.xin.portal.system.model.Permission;
import com.xin.portal.system.model.RolePermission;
import com.xin.portal.system.model.RoleUser;
import com.xin.portal.system.mapper.MenuMapper;
import com.xin.portal.system.mapper.MessageCenterMapper;
import com.xin.portal.system.mapper.PermissionMapper;
import com.xin.portal.system.mapper.RolePermissionMapper;
import com.xin.portal.system.mapper.RoleUserMapper;
import com.xin.portal.system.service.RolePermissionService;
import com.xin.portal.system.util.SessionUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2018-11-15
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {
	
	@Autowired
	private RolePermissionMapper mapper;
	@Autowired
	private RoleUserMapper roleUserMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private MessageCenterMapper messageCenterMapper;
	
	@Override
	public PageModel<RolePermission> page(RolePermission query, Integer pageNumber, Integer pageSize) {
		Page<RolePermission> page = new Page<RolePermission>(pageNumber, pageSize);
		EntityWrapper<RolePermission> ew=new EntityWrapper<RolePermission>(query);
		page = selectPage(page, ew);
		return new PageModel<RolePermission>(page);
	}

	@Transactional
	@Override
	public boolean saveAll(RolePermission record) {
		EntityWrapper<RolePermission> ew = new EntityWrapper<>();
		ew.eq("resource_id", record.getResourceId());
		ew.eq("role_id", record.getRoleId());
		boolean resultDel = delete(ew);
		if (StringUtils.isNoneEmpty(record.getPermissionId())) {
			String[] permissions = record.getPermissionId().split(",");
			List<RolePermission> list = Lists.newArrayList();
			for (String permission : permissions) {
				RolePermission rec = new RolePermission();
				String[] permissionRec = permission.split("_");
				rec.setResourceId(record.getResourceId());
				rec.setRoleId(record.getRoleId());
				rec.setCode(permissionRec[1]);
				rec.setPermissionId(permissionRec[0]);
				list.add(rec);
			}
			boolean resultInsert = list.size()>0?insertBatch(list):true;
			return resultDel && resultInsert;
		}
		
		return resultDel;
		
	}

	@Override
	public PageModel<RolePermission> pagePermissionUser(RolePermission query) {
		Page<RolePermission> page = new Page<RolePermission>(query.getPageNumber(),query.getPageSize());
		page.setRecords(mapper.findPermissionRoleUser(page, query));
		return new PageModel<RolePermission>(page);
	}

	@Override
	public void insertRolePserssionToMsgCenter(String userId, RolePermission record, String type) {
		//type分为saveAll  save  delete
		try {
			//获取资源菜单
			List<Menu> menuListAll = menuMapper.findMenuFromResourceIds(Arrays.asList(new String[]{record.getResourceId()}));
			if(menuListAll!=null && menuListAll.size()>0){
				//获取被操作的角色的所有用户
				List<RoleUser> listRU = roleUserMapper.findUsersByRoleIds(Arrays.asList(new String[]{record.getRoleId()}));
				if(listRU!=null && listRU.size()>0){
					if("saveAll".equals(type)){
						for (RoleUser roleUser : listRU) {
							if(!roleUser.getUserId().equals(SessionUtil.getUserId())){//消息不发送给操作人
								//查看用户对该资源的查看权限是否有表达 菜单(不算当前角色)
								List<Menu> menuList = menuMapper.selectMenuByUserPermission(roleUser.getUserId(), Permission.VIEW, record.getResourceId(),record.getRoleId());
								if(menuList==null || menuList.size()<=0){
									if (StringUtils.isNoneEmpty(record.getPermissionId())) {
										//添加权限  添加消息
										insertMessageFromRolePermission(roleUser.getUserId(), "权限变更通知", "您获得", menuListAll);
									}else{
										//删除权限 添加消息
										insertMessageFromRolePermission(roleUser.getUserId(), "权限变更通知", "您失去", menuListAll);
									}	
								}
							}
						}
					}else if("delete".equals(type)) {
						//删除权限  查看是否为view 权限，如果是
						Permission permission = permissionMapper.selectById(record.getPermissionId());
						if("view".equals(permission.getCode())){
							//查看用户角色中是否有给资源的view 如果无 记录
							for (RoleUser roleUser : listRU) {
								if(!roleUser.getUserId().equals(SessionUtil.getUserId())){
									List<Menu> menuList = menuMapper.selectMenuByUserPermission(roleUser.getUserId(), Permission.VIEW, record.getResourceId(),record.getRoleId());
									if(menuList==null || menuList.size()<=0){
										//删除权限，添加消息
										insertMessageFromRolePermission(roleUser.getUserId(), "权限变更通知", "您失去", menuListAll);
									}
								}
							}
						}
					}else if("save".equals(type)) {
						//添加权限查看是否为view 权限，如果是
						Permission permission = permissionMapper.selectById(record.getPermissionId());
						if("view".equals(permission.getCode())){
							//查看用户角色中是否有给资源的view 如果无 记录
							for (RoleUser roleUser : listRU) {
								if(!roleUser.getUserId().equals(SessionUtil.getUserId())){
									List<Menu> menuList = menuMapper.selectMenuByUserPermission(roleUser.getUserId(), Permission.VIEW, record.getResourceId(),record.getRoleId());
									if(menuList==null || menuList.size()<=0){
										//添加权限 添加消息
										insertMessageFromRolePermission(roleUser.getUserId(), "权限变更通知", "您获得", menuListAll);
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	private void insertMessageFromRolePermission(String userId,String title,String content,List<Menu> menuList){
		for (int i = 0; i < menuList.size(); i++) {
			content += ("[" +menuList.get(i).getName()+ "]");
		}
		MessageCenter msg = new MessageCenter();
		msg.setTitle(title);
		msg.setContent(content+" 菜单的访问权限");
		msg.setReceiveUser(userId);
		msg.setProduceUser(SessionUtil.getUserId());
		msg.setCreateTime(new Date());
		msg.setType(MessageCenter.NOTICE_MESSAGE);
		msg.setIsRead(0);
		messageCenterMapper.insert(msg);
	}
}
