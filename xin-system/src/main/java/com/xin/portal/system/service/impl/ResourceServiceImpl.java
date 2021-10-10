package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.CommentMapper;
import com.xin.portal.system.mapper.ListManageResourceMapper;
import com.xin.portal.system.mapper.MenuMapper;
import com.xin.portal.system.mapper.PromptRelMapper;
import com.xin.portal.system.mapper.ResourceLogMapper;
import com.xin.portal.system.mapper.ResourceMapper;
import com.xin.portal.system.mapper.RolePermissionMapper;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.FileService;
import com.xin.portal.system.service.PermissionService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.service.RoleService;
import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper,Resource> implements ResourceService {
	
	@Autowired
	private ResourceMapper mapper;
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private ResourceLogMapper resourceLogMapper;
	@Autowired
	private FileService fileService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private PermissionService permissionService;
	@Autowired
	private PromptRelMapper promptRelMapper;
	@Autowired
	private ListManageResourceMapper listManageResourceMapper;
	@Autowired
	private CommentMapper commentMapper;


	@Override
	@Transactional
	public Resource save(Resource query) {
		boolean result ;
		Permission permission = new Permission();
		permission.setCode("view");
		EntityWrapper wrapper = new EntityWrapper();
		wrapper.setEntity(permission);
		permission = permissionService.selectOne(wrapper);
		if (StringUtils.isEmpty(query.getId())){
			query.setCreateTime(new Date());
			query.setUpdateTime(query.getCreateTime());
			result = query.insert();
		} else {
			query.setUpdateTime(new Date());
			if (StringUtils.isEmpty(query.getEveryone())){
				query.setEveryone("off");
			}
			result = query.updateById();
		}
		if ("on".equals(query.getEveryone())) {
			RolePermission rolePermission = new RolePermission();
			rolePermission.setCode("view");
			rolePermission.setResourceId(query.getId());
			EntityWrapper ew = new EntityWrapper();
			Role role = new Role();
			role.setCode("EVERYONE");
			ew.setEntity(role);
			//获得“所有人EVERYONE”的角色role信息
			role = roleService.selectOne(ew);
			rolePermission.setRoleId(role.getId());
			rolePermission.setPermissionId(permission.getId());
			RolePermission rp = rolePermissionMapper.selectOne(rolePermission);
			if(rp==null){
				rolePermission.insert();
			}
		} else {
			EntityWrapper<RolePermission> ew = new EntityWrapper<>();
			ew.eq("resource_id", query.getId());
			ew.eq("permission_id",permission.getId());
			EntityWrapper entityWrapper = new EntityWrapper();
			Role role = new Role();
			role.setCode("EVERYONE");
			entityWrapper.setEntity(role);
			role = roleService.selectOne(entityWrapper);
			ew.eq("role_id", role.getId());
			rolePermissionMapper.delete(ew);//删权限
		}
		
		return result ? query : null;
	}
	
	@Override
	@Transactional
	public boolean saveBatch(List<Resource> list) {
//		int result = mapper.saveBatch(list);
//		if (result>0) {
//			resourceReportMapper.saveBatch(list);
//		}
		int count = 0;
		for (Resource rec : list) {
			if(save(rec)!=null) {
				count++;
			};
		}
		return count==list.size() ? true : false;
	}

	@Override
	@Transactional
	public Resource update(Resource query) {
		query.setUpdateTime(new Date());
		return query.updateById() ? query : null;
	}

	@Override
	@Transactional
	public boolean delete(Resource query) {
		EntityWrapper<RolePermission> ew = new EntityWrapper<>();
		ew.eq("resource_id", query.getId());
		rolePermissionMapper.delete(ew);//删权限
		EntityWrapper<Menu> ewMenu = new EntityWrapper<>();
		ewMenu.eq("resource_id", query.getId());
		menuMapper.delete(ewMenu);//删菜单
		EntityWrapper<ResourceLog> ewResourceLog = new EntityWrapper<>();
		ewResourceLog.eq("resource_id", query.getId());
		resourceLogMapper.delete(ewResourceLog);//删记录
		EntityWrapper<PromptRel> ewPrompt = new EntityWrapper<>();
		ewPrompt.eq("resource_id", query.getId());
		promptRelMapper.delete(ewPrompt);//删提示
		EntityWrapper<ListManageResource> ewManageResouorce = new EntityWrapper<>();
		ewManageResouorce.eq("resource_id", query.getId());
		listManageResourceMapper.delete(ewManageResouorce);//删列表
		EntityWrapper<Comment> comment = new EntityWrapper<>();
		comment.eq("resource_id", query.getId());
		commentMapper.delete(comment);//删评论
		//删评论
		if (ResourceType.DOCUMENT.equals(query.getResourceType1())) {
			fileService.delete(query.getFileId());//删文档
			//fileService.delete(query.getThumbnailId());//删缩略图文件--修改为图片管理功能管理
		}
		
		return query.deleteById();
	}

	@Override
	public int updateIntroduce(Map map) {
		return mapper.updateIntroduce(map);
	}

	@Override
	public boolean updateNum(String id, String column, String methodType) {
		return mapper.updateNum(id,column,methodType)>0?true:false;
	}

	@Override
	public Resource findSharResource(Resource query) {
		return mapper.findSharResource(query);
	}


	@Override
	public PageModel page(Resource query, Integer pageNumber, Integer pageSize) {
		Page<Resource> page = new Page<Resource>(pageNumber, pageSize);
		EntityWrapper<Resource> ew=new EntityWrapper<Resource>();
		ew.orderBy("sort", true).orderBy("update_time", false).orderBy("create_time", false);
		if (StringUtils.isNotEmpty(query.getName())) {
			ew.like("name", query.getName());
		}
		if (StringUtils.isNotEmpty(query.getIntroduce())) {
			ew.like("introduce", query.getIntroduce());
		}
		if (StringUtils.isNotEmpty(query.getCode())) {
			ew.like("code", query.getCode());
		}
		if (StringUtils.isNotEmpty(query.getResourceType1())) {
			ew.eq("resource_type1", query.getResourceType1());
			
		}
		if (StringUtils.isNotEmpty(query.getResourceType2())) {
			ew.eq("resource_type2", query.getResourceType2());
			
		}
		if (query.getIsMobile() != null) {
			ew.eq("is_mobile", query.getIsMobile());
		}
		if (query.getManage()!=null && query.getManage()) {
			EntityWrapper<RolePermission> ewRolePermission = new EntityWrapper<>();
			ewRolePermission.eq("code",Permission.MANAGE);
			ewRolePermission.in("role_id", (List<String>)SessionUtil.getSession(Constant.SessionKeys.USER_ROLES));
			List<RolePermission> rolePermissionIds = rolePermissionMapper.selectList(ewRolePermission);
			List<String> ids = rolePermissionIds.stream().map(RolePermission::getResourceId).collect(Collectors.toList());
			ew.in("id",ids);

		}
		page = selectPage(page, ew);
		return new PageModel<Resource>(page);
	}

	@Override
	public PageModel listPage(Resource query, Integer pageNumber, Integer pageSize,List<String> resourceId) {
		Page<Resource> page = new Page<Resource>(pageNumber, pageSize);
		EntityWrapper<Resource> ew=new EntityWrapper<Resource>();
		ew.orderBy("update_time", false).orderBy("create_time", false);
		if (StringUtils.isNotEmpty(query.getName())) {
			ew.like("name", query.getName());
		}
		if (StringUtils.isNotEmpty(query.getResourceType1())) {
			ew.eq("resource_type1", query.getResourceType1());
			
		}
		if (StringUtils.isNotEmpty(query.getResourceType2())) {
			ew.eq("resource_type2", query.getResourceType2());
			
		}
		if(resourceId.size()>0){
			ew.notIn("id", resourceId);
		}
		ew.eq("is_mobile", "0");
		page = selectPage(page, ew);
		return new PageModel<Resource>(page);
	}

	@Override
	public PageModel pageRoleResource(Resource query, Integer pageNumber, Integer pageSize) {
		Page<Resource> page = new Page<Resource>(pageNumber, pageSize);
		page.setRecords(mapper.findRoleResourceList(page, query));
		return new PageModel<Resource>(page);
	}


	@Override
	public PageModel pageResourcePerm(ResourcePerm query, Integer pageNumber, Integer pageSize) {
		Page<ResourcePerm> page = new Page<ResourcePerm>(pageNumber, pageSize);
		int firstIndex = (pageNumber - 1) * pageSize;
		page.setRecords(mapper.findResourcePermList(firstIndex,pageSize,query));
//		EntityWrapper<Resource> ew = new EntityWrapper<>();
//		if (StringUtils.isNotEmpty(query.getResourceType1())) {
//			ew.eq("resource_type1", query.getResourceType1());
//		}else if(StringUtils.isNotEmpty(query.getResourceType2())){
//			ew.eq("resource_type2", query.getResourceType2());
//		}
//		page.setTotal(selectCount(ew));
		page.setTotal(mapper.findResourcePermListCount(query));
		return new PageModel<ResourcePerm>(page);
	}


	@Override
	public List<Resource> selectListResource(String relate,String userId) {
		// TODO Auto-generated method stub
		return mapper.selectListResource(relate,userId);
	}


	@Override
	public List<Resource> selectOperationLog(int num) {
		// TODO Auto-generated method stub
		return mapper.selectOperationLog(num);
	}

	@Override
	public List<Resource> findReourceLog(String userId) {
		// TODO Auto-generated method stub
		return mapper.findReourceLog(userId);
	}

	@Override
	public Resource selectResource(String relate, String userId) {
		// TODO Auto-generated method stub
		return mapper.selectResource(relate,userId);
	}

	@Override
	public int resourceData() {
		return mapper.resourceData();
	}

	@Override
	public int resourceActive() {
		return mapper.resourceActive();
	}

	@Override
	public List<Resource> chart() {
		return mapper.chart();
	}
	@Override
	public boolean updateEveryoneById(String resourceId,String value){
		return mapper.updateEveryoneById(resourceId,value);
	}

}
