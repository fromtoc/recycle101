package com.xin.portal.system.service.impl;

import com.xin.portal.system.mapper.*;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.TenantService;
import com.xin.portal.system.util.Constant.SessionKeys;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.PasswordUtils;
import com.xin.portal.system.util.SessionUtil;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.google.common.collect.Lists;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.xin.portal.system.util.i18n.LanguageParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;
import org.springframework.transaction.annotation.Transactional;

/**
 * 租户表 服务实现类
 *
 * @author zhoujun
 * @since 2018-09-27
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements TenantService {
	
	@Value("${system.default_password:123456}")
	private String defaultPassword;

	@Autowired
	private  TenantMapper mapper;
	@Autowired
	private ConfigMapper configMapper;
	@Autowired
	private DictMapper dictMapper;
	@Autowired
	private OrganizationMapper organizationMapper;
	@Autowired
	private ResourceMapper resourceMapper;

	@Autowired
	private ResourceTypeMapper resourceTypeMapper;
	@Autowired
	private RoleUserMapper roleUserMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private MenuMapper menuMapper;
	@Autowired
	private ListManageMapper listManageMapper;
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	@Autowired
	private DataPermissionTypeMapper dataPermissionTypeMapper;
	@Autowired
	private UserOrganizationMapper userOrganizationMapper;
	
	@Override
	public PageModel<Tenant> page(Tenant query, Integer pageNumber, Integer pageSize) {
		Page<Tenant> page = new Page<Tenant>(pageNumber, pageSize);
		query.setIsDelete(1);
		EntityWrapper<Tenant> ew=new EntityWrapper<Tenant>(query);
		page = selectPage(page, ew);
		return new PageModel<Tenant>(page);
	}

	@Override
	@Transactional
	public boolean save(Tenant record) {
		int count = selectCount(null);
		record.setIsDelete(1);
		record.setManager("admin");
		record.insert();
		//获取初始的语言配置值
		Config  cg = new Config();
		cg.setCode("CONFIG_LOCALE");
		String CONFIG_LOCALE = configMapper.selectOne(cg).getValue();

		//获取组织code
		String orgCode = getNewOrgCode();
		Organization org = new Organization();//需要指定一下code、status=0
		org.setName(record.getName());
		org.setParentId("0");
		org.setCode(orgCode);
		org.setSort(0);
		org.setStatus(0);
		org.setCreateTime(new Date());
		org.setTenantId(record.getId());
		organizationMapper.insertOrganizationAllColunmForNewTenant(org);
		
		User user = new User();
		user.setUsername("admin");
		user.setPassword(PasswordUtils.hash(defaultPassword));
		user.setRegisterTime(new Date());
		user.setLoginCount(0);
		user.setErrorCount(0);
		user.setActivation(true);
		user.setState(0);
		user.setStatus("1");
		user.setIsDelete(0);
		user.setTenantId(record.getId());
		boolean result = userMapper.insertUserAllColunmForNewTenant(user);
		
		UserInfo userInfo = new UserInfo();
		userInfo.setRealname("admin");
		userInfo.setGender(1);
		userInfo.setId(user.getId());
		userInfo.setOrgCode(org.getCode());
		userInfo.setAvatar("/images/avatar.jpg");
		userInfo.setLocale(CONFIG_LOCALE);
		userInfo.setEmail(record.getEmail());
		userInfo.setOrgId(org.getId());
		userInfo.setCode(user.getUsername());
		userInfo.setMstrUser(0);
		userInfo.setTenantId(record.getId());
		boolean resultInfo = userInfoMapper.insertUserInfoAllColunmForNewTenant(userInfo);
		
		//添加用户与组织关系
		UserOrganization userOrg = new UserOrganization();
		userOrg.setUserId(user.getId());
		userOrg.setOrgId(org.getId());
		userOrg.setIsDeputy(0);//主组织
		userOrg.setTenantId(record.getId());
		userOrganizationMapper.insertUserOrganizationForNewTenant(userOrg);
		
		//设置角色
		Role role1 = insertRoleByParams(/*"角色"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ROLE), "role", "0", null, 0, 0, record.getId());
		Role role2 = insertRoleByParams(/*"管理员"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ADMIN), "admin_system", role1.getId(), null, 1, 0, record.getId());
		Role role3 = insertRoleByParams(/*"所有人"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EVERYONE), "EVERYONE", role1.getId(), null, 2, 0, record.getId());
		Role role4 = insertRoleByParams(/*"测试人员"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_TEST), "role_test", role1.getId(), null, 3, 0, record.getId());
		
		//还需要一个role_permission的数据。role_permission需要有一个基本的定义
		RoleUser roleUser = new RoleUser();
		roleUser.setRoleId(role2.getId());
		roleUser.setUserId(user.getId());
		roleUser.setRoleCode("admin_system");
		roleUser.setTenantId(record.getId());
		roleUserMapper.insertRoleUserAllColunmForNewTenant(roleUser);
		
		//权限表t_permission
	/*	Permission permission1=null;
		List<Permission> permission = permissionMapper.selectList(new EntityWrapper<Permission>());
		for (Permission p : permission) {
		*//*	p.setId(null);
			p.setTenantId(null);
			p.setTenantId(record.getId());
			permissionMapper.insertPermissionAllColunmForNewTenant(p);*//*
			if("1".equals(p.getResourceTypeId())&&"view".equals(p.getCode())){
				permission1=InsertPermissionByParams(p.getResourceTypeId(), p.getName(), p.getCode(), "", record.getId());
			}else{
				InsertPermissionByParams(p.getResourceTypeId(), p.getName(), p.getCode(), "", record.getId());
			}
		}*/

		/*//可见权限
		Permission viewPermission1  = InsertPermissionByParams("1",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission2  = InsertPermissionByParams("2",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission3  = InsertPermissionByParams("3",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission4  = InsertPermissionByParams("4",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission5  = InsertPermissionByParams("5",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission6  = InsertPermissionByParams("6",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission7  = InsertPermissionByParams("7",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission8  = InsertPermissionByParams("8",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW),  "view", "", record.getId(),1);
		Permission viewPermission9  = InsertPermissionByParams("9",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW),  "view", "", record.getId(),1);
		Permission viewPermission10 = InsertPermissionByParams("10", LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW),  "view", "", record.getId(),1);
		//评论权限
		Permission commentPermission1  = InsertPermissionByParams("1",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission2  = InsertPermissionByParams("2",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission3  = InsertPermissionByParams("3",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission4  = InsertPermissionByParams("4",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission5  = InsertPermissionByParams("5",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission6  = InsertPermissionByParams("6",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission7  = InsertPermissionByParams("7",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission8  = InsertPermissionByParams("8",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission9  = InsertPermissionByParams("9",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission10 = InsertPermissionByParams("10", LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		//权限用户查看权限
		Permission permission1  = InsertPermissionByParams("1",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission2  = InsertPermissionByParams("2",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission3  = InsertPermissionByParams("3",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission4  = InsertPermissionByParams("4",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission5  = InsertPermissionByParams("5",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission6  = InsertPermissionByParams("6",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission7  = InsertPermissionByParams("7",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission8  = InsertPermissionByParams("8",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission9  = InsertPermissionByParams("9",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission10 = InsertPermissionByParams("10", LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		//分享权限
		Permission sharePermission1  = InsertPermissionByParams("1",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission2  = InsertPermissionByParams("2",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission3  = InsertPermissionByParams("3",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission4  = InsertPermissionByParams("4",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission5  = InsertPermissionByParams("5",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission6  = InsertPermissionByParams("6",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission7  = InsertPermissionByParams("7",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission8  = InsertPermissionByParams("8",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission9  = InsertPermissionByParams("9",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission10 = InsertPermissionByParams("10", LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		//日志权限
		Permission logPermission1  = InsertPermissionByParams("1",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission2  = InsertPermissionByParams("2",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission3  = InsertPermissionByParams("3",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission4  = InsertPermissionByParams("4",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission5  = InsertPermissionByParams("5",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission6  = InsertPermissionByParams("6",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission7  = InsertPermissionByParams("7",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission8  = InsertPermissionByParams("8",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission9  = InsertPermissionByParams("9",  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission10 = InsertPermissionByParams("10", LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		//导出权限
		Permission exportPermission5  = InsertPermissionByParams("5",   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EXPORT), "export", "", record.getId(),6);
		Permission exportPermission4  = InsertPermissionByParams("4",   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EXPORT), "export", "", record.getId(),6);
		Permission exportPermission3  = InsertPermissionByParams("3",   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EXPORT), "export", "", record.getId(),6);
		//下载权限
		Permission downloadPermission9  = InsertPermissionByParams("9",   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DOWNLOAD), "download", "", record.getId(),7);


*/


		/*//资源要有一个基本功能菜单的表可以直接从中复制到数据给新的租户设置基本菜单--未实现。或者直接在代码中设置也可以。
		Resource resource0 = InsertResourceByParams(*//*"问题反馈"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_QUESTION_FEEDBACK), "/issue/singleIndex", "wtfk", "", "fa-cog", "", "/images/thumbnail.png", 1, "1074135544087265281", 1, 1, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource1 = InsertResourceByParams(*//*"我的收藏"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MY_COLLECTION), "/collect/index", null, "", "fa-cog", "", "/images/thumbnail.png", 1, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource2 = InsertResourceByParams(*//*"用户映射"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_USER_MAPPING), "/biUser/index", null, "", "fa-cog", "", "/images/thumbnail.png", 2, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource3 = InsertResourceByParams(*//*"筛选管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PROMPT_MANAGEMENT), "/prompt/index", null, "", "fa-cog", "", "/images/thumbnail.png", 3, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource4 = InsertResourceByParams(*//*"自助分析"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SELF_ANALYSIS), "/userReport/index", null, "", "fa-cog", "", "/images/thumbnail.png", 4, "1074135544087265281", 1, 0, 1, "德昂多维自主分析，英文VI，用户可随意将各种维度组合，实现数据展现", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource5 = InsertResourceByParams(*//*"集成系统"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATE_SYSTEM), "/biServer/index", null, "", "fa-cog", "", "/images/thumbnail.png", 5, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource6 = InsertResourceByParams(*//*"集成项目"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATE_PROJECT), "/biProject/index", null, "", "fa-cog", "", "/images/thumbnail.png", 6, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource7 = InsertResourceByParams(*//*"BI账户映射"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ACCOUNT_MAPPING), "/biMapping/index", null, "", "fa-cog", "", "/images/thumbnail.png", 7, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource8 = InsertResourceByParams(*//*"BI账户集成"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ACCOUNT_INTEGRATION), "/biUser/index", null, "", "fa-cog", "", "/images/thumbnail.png", 8, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource9 = InsertResourceByParams(*//*"集成自助分析"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATED_SELF_ANALYSIS), "/biIndependent/index", null, "", "fa-cog", "", "/images/thumbnail.png", 9, "1074135544087265281", 1, 1, null, "配置自助分析的地址的后台管理功能", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource11 = InsertResourceByParams(*//*"轮播图管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_BANNER), "/banner/index", null, "", "fa-cog", "", "/images/thumbnail.png", 11, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource12 = InsertResourceByParams(*//*"问题管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PROBLEM_MANAGEMENT), "/issue/index", null, "", "fa-cog", "", "/images/thumbnail.png", 12, "1074135544087265281", 1, 0, null, "管理用户提出的问题", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource13 = InsertResourceByParams(*//*"字典管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DICT_MANAGEMENT), "/dict/index", null, "", "fa-cog", "", "/images/thumbnail.png", 13, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource14 = InsertResourceByParams(*//*"系统公告"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_NOTICE), "/notice/index", null, "", "fa-cog", "", "/images/thumbnail.png", 14, "1074135544087265281", 1, 1, null, "这是系统公告哦", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource15 = InsertResourceByParams(*//*"运维服务"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_OPERATIONS_AND_MAINTENANCE_SERVICES), "", null, "", "fa-cog", "", "/images/thumbnail.png", 15, "1074135544087265281", 1, 0, null, "这是运维服务", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource16 = InsertResourceByParams(*//*"资源日志"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_RESOURCE_LOG), "/resourceLog/toIndex", null, "", "fa-cog", "", "/images/thumbnail.png", 16, "1074135544087265281", 1, 0, null, "资源日志查看", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource17 = InsertResourceByParams(*//*"行为日志"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_BEHAVIOR_LOG), "/sysLog/index", null, "", "fa-cog", "", "/images/thumbnail.png", 17, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource18 = InsertResourceByParams(*//*"版本管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VERSION_MANAGEMENT), "/sysRelease/index", null, "", "fa-cog", "", "/images/thumbnail.png", 18, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource19 = InsertResourceByParams(*//*"产品信息"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PRODUCT_INFORMATION), "/activate/info", null, "", "fa-cog", "", "/images/thumbnail.png", 19, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource20 = InsertResourceByParams(*//*"日志服务"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOGGING_SERVICE), "/sysLog/sysLog", "RZFW", "", "fa-cog", "", "/images/thumbnail.png", 20, "1074135544087265281", 1, 1, null, "日志服务", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource21 = InsertResourceByParams(*//*"实时日志"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_REAL_TIME_LOG), "/sysLog/webLog", "SSRZ", "", "fa-cog", "", "/images/thumbnail.png", 21, "1074135544087265281", 1, 1, null, "实时日志", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource22 = InsertResourceByParams(*//*"系统设置"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_SETTING), "/config/index", null, "", "fa-cog", "", "/images/thumbnail.png", 22, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource23 = InsertResourceByParams(*//*"资源管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_RESOURCE_MANAGEMENT), "/resource/index", "ZYGL", "", "fa-cog", "", "/images/thumbnail.png", 23, "1074135544087265281", 1, 0, null, "这是资源管理，非常重要的关键功能，请务必小心操作。", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource24 = InsertResourceByParams(*//*"权限管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION_MANAGEMENT), "/rolePermission/index", null, "", "fa-cog", "", "/images/thumbnail.png", 24, "1074135544087265281", 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource25 = InsertResourceByParams(*//*"组织管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ORGANIZATION_MANAGEMENT), "/organization/index", "org", "", "fa-cog", "", "/images/thumbnail.png", 25, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource26 = InsertResourceByParams(*//*"角色管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ROLE_MANAGEMENT), "/role/index", "role", "", "fa-cog", "", "/images/thumbnail.png", 26, "1074135544087265281", 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource27 = InsertResourceByParams(*//*"菜单管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MENU_MANAGEMENT), "/menu/index", null, "", "fa-cog", "", "/images/thumbnail.png", 27, "1074135544087265281", 1, 0, 2, "这个是菜单管理功能，用于进行菜单的编辑与修改，菜单全部来自于系统资源。", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource28 = InsertResourceByParams(*//*"模板管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MODULE_MANAGEMENT), "/module/index", null, "", "fa-cog", "", "/images/thumbnail.png", 28, "1074135544087265281", 1, 0, null, "这是模板管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource29 = InsertResourceByParams(*//*"列表管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LIST_MANAGEMENT), "/listManage/index", null, "", "fa-cog", "", "/images/thumbnail.png", 29, "1074135544087265281", 1, 0, null, "列表管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource30 = InsertResourceByParams(*//*"在线用户管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ONLINE_USER), "/onlineUserManagement/index", null, "", "fa-cog", "", "/images/thumbnail.png", 30, "1074135544087265281", 1, 1, null, "在线用户管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource31 = InsertResourceByParams(*//*"用户分析"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_USER_ANALYSIS), "/userAnalysis/one", null, "", "fa-cog", "", "/images/thumbnail.png", 31, "1074135544087265281", 1, 1, null, "用户分析", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource32 = InsertResourceByParams(*//*"评论管理"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT_MANAGEMENT), "/comment/management", null, "", "fa-cog", "", "/images/thumbnail.png", 32, "1074135544087265281", 1, 1, null, "评论管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
		Resource resource33 = InsertResourceByParams(*//*"微信推送任务"*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK), "/wxPush/index", null, "", "fa-cog", "", "/images/thumbnail.png", 33, "1074135544087265281", 1, 1, null, "定时任务推送", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", "1", "1074135544087265281", null, record.getId());
*/
		//资源类型t_resource_type
		ResourceType resourceType1 = insertResourceTypeByParams(/*系统功能*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.SYSTEM_FUNCTION), 1,"0", 1, null, record.getId());
		ResourceType resourceType2 = insertResourceTypeByParams(/*链接*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LINK), 2,"0", 2, null, record.getId());
        ResourceType resourceType3=insertResourceTypeByParams(/*MSTR-Dossier*/"MSTR-Dossier",3,"0",1,null,record.getId());
        ResourceType resourceType4=insertResourceTypeByParams(/*MSTR-Report*/"MSTR-Report",4,"0",1,null,record.getId());
        ResourceType resourceType5=insertResourceTypeByParams(/*MSTR-Document*/"MSTR-Document",5,"0",1,null,record.getId());
        ResourceType resourceType6=insertResourceTypeByParams(/*Tableau*/"Tableau",6,"0",1,null,record.getId());
        ResourceType resourceType7=insertResourceTypeByParams(/*帆软*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.FINE_REPORT),7,"0",1,null,record.getId());
        ResourceType resourceType8=insertResourceTypeByParams(/*BO*/"BO",8,"0",1,null,record.getId());
        ResourceType resourceType9=insertResourceTypeByParams(/*SmartBI*/"SmartBI",10,"0",1,null,record.getId());
		ResourceType resourceType10=insertResourceTypeByParams(/*文档*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DOCUMENT),9,"0",3,null,record.getId());
		ResourceType resourceType11=insertResourceTypeByParams(/*系统内置*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.THE_BUILT_IN),resourceType1.getCode(),resourceType1.getId(),1,null,record.getId());
        /*ResourceType resourceType5=insertResourceTypeByParams(*//*MSTR-Dossier*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK),"0",1,null,record.getId());
        ResourceType resourceType6=insertResourceTypeByParams(*//*MSTR-Report*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK),"0",1,null,record.getId());
        ResourceType resourceType7=insertResourceTypeByParams(*//*MSTR-Document*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK),"0",1,null,record.getId());
        ResourceType resourceType8=insertResourceTypeByParams(*//*Tableau*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK),"0",1,null,record.getId());
        ResourceType resourceType9=insertResourceTypeByParams(*//*帆软*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK),"0",1,null,record.getId());
        ResourceType resourceType1110=insertResourceTypeByParams(*//*BO*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK),"0",1,null,record.getId());
        ResourceType resourceType11=insertResourceTypeByParams(*//*SmartBI*//*LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK),"0",1,null,record.getId());
*/

		//可见权限
		Permission viewPermission1  = InsertPermissionByParams(resourceType1.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission2  = InsertPermissionByParams(resourceType2.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission3  = InsertPermissionByParams(resourceType3.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission4  = InsertPermissionByParams(resourceType4.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission5  = InsertPermissionByParams(resourceType5.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission6  = InsertPermissionByParams(resourceType6.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission7  = InsertPermissionByParams(resourceType7.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW), "view", "", record.getId(),1);
		Permission viewPermission8  = InsertPermissionByParams(resourceType8.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW),  "view", "", record.getId(),1);
		Permission viewPermission9  = InsertPermissionByParams(resourceType9.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW),  "view", "", record.getId(),1);
		Permission viewPermission10 = InsertPermissionByParams(resourceType10.getId(), LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW),  "view", "", record.getId(),1);
		//评论权限
		Permission commentPermission1  = InsertPermissionByParams(resourceType1.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission2  = InsertPermissionByParams(resourceType2.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission3  = InsertPermissionByParams(resourceType3.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission4  = InsertPermissionByParams(resourceType4.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission5  = InsertPermissionByParams(resourceType5.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission6  = InsertPermissionByParams(resourceType6.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission7  = InsertPermissionByParams(resourceType7.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission8  = InsertPermissionByParams(resourceType8.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission9  = InsertPermissionByParams(resourceType9.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		Permission commentPermission10 = InsertPermissionByParams(resourceType10.getId(), LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT), "comment", "", record.getId(),2);
		//权限用户查看权限
		Permission permission1  = InsertPermissionByParams(resourceType1.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission2  = InsertPermissionByParams(resourceType2.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission3  = InsertPermissionByParams(resourceType3.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission4  = InsertPermissionByParams(resourceType4.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission5  = InsertPermissionByParams(resourceType5.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission6  = InsertPermissionByParams(resourceType6.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission7  = InsertPermissionByParams(resourceType7.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission8  = InsertPermissionByParams(resourceType8.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission9  = InsertPermissionByParams(resourceType9.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		Permission permission10 = InsertPermissionByParams(resourceType10.getId(), LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION), "permission", "", record.getId(),3);
		//分享权限
		Permission sharePermission1  = InsertPermissionByParams(resourceType1.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission2  = InsertPermissionByParams(resourceType2.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission3  = InsertPermissionByParams(resourceType3.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission4  = InsertPermissionByParams(resourceType4.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission5  = InsertPermissionByParams(resourceType5.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission6  = InsertPermissionByParams(resourceType6.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission7  = InsertPermissionByParams(resourceType7.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission8  = InsertPermissionByParams(resourceType8.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission9  = InsertPermissionByParams(resourceType9.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		Permission sharePermission10 = InsertPermissionByParams(resourceType10.getId(), LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SHARE), "share", "", record.getId(),4);
		//日志权限
		Permission logPermission1  = InsertPermissionByParams(resourceType1.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission2  = InsertPermissionByParams(resourceType2.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission3  = InsertPermissionByParams(resourceType3.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission4  = InsertPermissionByParams(resourceType4.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission5  = InsertPermissionByParams(resourceType5.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission6  = InsertPermissionByParams(resourceType6.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission7  = InsertPermissionByParams(resourceType7.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission8  = InsertPermissionByParams(resourceType8.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission9  = InsertPermissionByParams(resourceType9.getId(),  LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		Permission logPermission10 = InsertPermissionByParams(resourceType10.getId(), LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG), "log", "", record.getId(),5);
		//导出权限
		Permission exportPermission5  = InsertPermissionByParams(resourceType5.getId(),   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EXPORT), "export", "", record.getId(),6);
		Permission exportPermission4  = InsertPermissionByParams(resourceType4.getId(),   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EXPORT), "export", "", record.getId(),6);
		Permission exportPermission3  = InsertPermissionByParams(resourceType3.getId(),   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EXPORT), "export", "", record.getId(),6);
		//下载权限
		Permission downloadPermission9  = InsertPermissionByParams(resourceType9.getId(),   LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DOWNLOAD), "download", "", record.getId(),7);



		//资源要有一个基本功能菜单的表可以直接从中复制到数据给新的租户设置基本菜单--未实现。或者直接在代码中设置也可以。
		Resource resource0 = InsertResourceByParams(/*"问题反馈"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_QUESTION_FEEDBACK), "/issue/singleIndex", "wtfk", "", "fa-cog", "", "/images/thumbnail.png", 1, resourceType11.getId(), 1, 1, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource1 = InsertResourceByParams(/*"我的收藏"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MY_COLLECTION), "/collect/index", null, "", "fa-cog", "", "/images/thumbnail.png", 1, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource2 = InsertResourceByParams(/*"用户映射"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_USER_MAPPING), "/biUser/index", null, "", "fa-cog", "", "/images/thumbnail.png", 2, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource3 = InsertResourceByParams(/*"筛选管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PROMPT_MANAGEMENT), "/prompt/index", null, "", "fa-cog", "", "/images/thumbnail.png", 3, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource4 = InsertResourceByParams(/*"自助分析"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SELF_ANALYSIS), "/userReport/index", null, "", "fa-cog", "", "/images/thumbnail.png", 4, resourceType11.getId(), 1, 0, 1, "德昂多维自主分析，英文VI，用户可随意将各种维度组合，实现数据展现", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "",resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource5 = InsertResourceByParams(/*"集成系统"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATE_SYSTEM), "/biServer/index", null, "", "fa-cog", "", "/images/thumbnail.png", 5, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource6 = InsertResourceByParams(/*"集成项目"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATE_PROJECT), "/biProject/index", null, "", "fa-cog", "", "/images/thumbnail.png", 6, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "",resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource7 = InsertResourceByParams(/*"BI账户映射"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ACCOUNT_MAPPING), "/biMapping/index", null, "", "fa-cog", "", "/images/thumbnail.png", 7, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "",resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource8 = InsertResourceByParams(/*"BI账户集成"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ACCOUNT_INTEGRATION), "/biUser/index", null, "", "fa-cog", "", "/images/thumbnail.png", 8, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "",resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource9 = InsertResourceByParams(/*"集成自助分析"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATED_SELF_ANALYSIS), "/biIndependent/index", null, "", "fa-cog", "", "/images/thumbnail.png", 9, resourceType11.getId(), 1, 1, null, "配置自助分析的地址的后台管理功能", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource11 = InsertResourceByParams(/*"轮播图管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_BANNER), "/banner/index", null, "", "fa-cog", "", "/images/thumbnail.png", 11, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource12 = InsertResourceByParams(/*"问题管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PROBLEM_MANAGEMENT), "/issue/index", null, "", "fa-cog", "", "/images/thumbnail.png", 12, resourceType11.getId(), 1, 0, null, "管理用户提出的问题", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource13 = InsertResourceByParams(/*"字典管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DICT_MANAGEMENT), "/dict/index", null, "", "fa-cog", "", "/images/thumbnail.png", 13, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource14 = InsertResourceByParams(/*"系统公告"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_NOTICE), "/notice/index", null, "", "fa-cog", "", "/images/thumbnail.png", 14, resourceType11.getId(), 1, 1, null, "这是系统公告哦", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource15 = InsertResourceByParams(/*"运维服务"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_OPERATIONS_AND_MAINTENANCE_SERVICES), "", null, "", "fa-cog", "", "/images/thumbnail.png", 15, resourceType11.getId(), 1, 0, null, "这是运维服务", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource16 = InsertResourceByParams(/*"资源日志"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_RESOURCE_LOG), "/resourceLog/toIndex", null, "", "fa-cog", "", "/images/thumbnail.png", 16, resourceType11.getId(), 1, 0, null, "资源日志查看", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource17 = InsertResourceByParams(/*"行为日志"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_BEHAVIOR_LOG), "/sysLog/index", null, "", "fa-cog", "", "/images/thumbnail.png", 17, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource18 = InsertResourceByParams(/*"版本管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VERSION_MANAGEMENT), "/sysRelease/index", null, "", "fa-cog", "", "/images/thumbnail.png", 18, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource19 = InsertResourceByParams(/*"产品信息"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PRODUCT_INFORMATION), "/activate/info", null, "", "fa-cog", "", "/images/thumbnail.png", 19, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource20 = InsertResourceByParams(/*"日志服务"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOGGING_SERVICE), "/sysLog/sysLog", "RZFW", "", "fa-cog", "", "/images/thumbnail.png", 20, resourceType11.getId(), 1, 1, null, "日志服务", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource21 = InsertResourceByParams(/*"实时日志"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_REAL_TIME_LOG), "/sysLog/webLog", "SSRZ", "", "fa-cog", "", "/images/thumbnail.png", 21, resourceType11.getId(), 1, 1, null, "实时日志", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource22 = InsertResourceByParams(/*"系统设置"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_SETTING), "/config/index", null, "", "fa-cog", "", "/images/thumbnail.png", 22, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource23 = InsertResourceByParams(/*"资源管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_RESOURCE_MANAGEMENT), "/resource/index", "ZYGL", "", "fa-cog", "", "/images/thumbnail.png", 23, resourceType11.getId(), 1, 0, null, "这是资源管理，非常重要的关键功能，请务必小心操作。", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource24 = InsertResourceByParams(/*"权限管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION_MANAGEMENT), "/rolePermission/index", null, "", "fa-cog", "", "/images/thumbnail.png", 24, resourceType11.getId(), 1, 0, null, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource25 = InsertResourceByParams(/*"组织管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ORGANIZATION_MANAGEMENT), "/organization/index", "org", "", "fa-cog", "", "/images/thumbnail.png", 25, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource26 = InsertResourceByParams(/*"角色管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ROLE_MANAGEMENT), "/role/index", "role", "", "fa-cog", "", "/images/thumbnail.png", 26, resourceType11.getId(), 1, 0, 2, "", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource27 = InsertResourceByParams(/*"菜单管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MENU_MANAGEMENT), "/menu/index", null, "", "fa-cog", "", "/images/thumbnail.png", 27, resourceType11.getId(), 1, 0, 2, "这个是菜单管理功能，用于进行菜单的编辑与修改，菜单全部来自于系统资源。", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource28 = InsertResourceByParams(/*"模板管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MODULE_MANAGEMENT), "/module/index", null, "", "fa-cog", "", "/images/thumbnail.png", 28, resourceType11.getId(), 1, 0, null, "这是模板管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource29 = InsertResourceByParams(/*"列表管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LIST_MANAGEMENT), "/listManage/index", null, "", "fa-cog", "", "/images/thumbnail.png", 29, resourceType11.getId(), 1, 0, null, "列表管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource30 = InsertResourceByParams(/*"在线用户管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ONLINE_USER), "/onlineUserManagement/index", null, "", "fa-cog", "", "/images/thumbnail.png", 30, resourceType11.getId(), 1, 0, null, "在线用户管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource31 = InsertResourceByParams(/*"用户分析"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_USER_ANALYSIS), "/userAnalysis/one", null, "", "fa-cog", "", "/images/thumbnail.png", 31, resourceType11.getId(), 1, 1, null, "用户分析", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource32 = InsertResourceByParams(/*"评论管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT_MANAGEMENT), "/comment/management", null, "", "fa-cog", "", "/images/thumbnail.png", 32, resourceType11.getId(), 1, 0, null, "评论管理", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		//Resource resource33 = InsertResourceByParams(/*"微信推送任务"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK), "/wxPush/index", null, "", "fa-cog", "", "/images/thumbnail.png", 33, resourceType11.getId(), 1, 1, null, "定时任务推送", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, record.getId());
		Resource resource33 = InsertResourceByParams(/*"调度任务"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SCHEDULING_TASKS), "/scheduleTask/manage/index", null, "", "fa-cog", "", "/images/thumbnail.png", 33, resourceType11.getId(), 1, 0, null, "定时任务推送", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());
		Resource resource34 = InsertResourceByParams(/*"个人订阅"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERSONAL_TASKS), "/scheduleTask/common/index", null, "", "fa-cog", "", "/images/thumbnail.png", 34, resourceType11.getId(), 1, 0, null, "个人定时任务推送", user.getId(), "", "", "", null, "header,footer,path,dockTop,dockLeft", "0", "0", "0", "0", "", resourceType1.getId(), resourceType11.getId(), null, 0, record.getId());


		//权限t_role_psermission
		RolePermission rolePermission0 = insertRolePermissionByParams(resource0.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission1 = insertRolePermissionByParams(resource1.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission2 = insertRolePermissionByParams(resource2.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission3 = insertRolePermissionByParams(resource3.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission4 = insertRolePermissionByParams(resource4.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission5 = insertRolePermissionByParams(resource5.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission6 = insertRolePermissionByParams(resource6.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission7 = insertRolePermissionByParams(resource7.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission8 = insertRolePermissionByParams(resource8.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission9 = insertRolePermissionByParams(resource9.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission11 = insertRolePermissionByParams(resource11.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission12 = insertRolePermissionByParams(resource12.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission13 = insertRolePermissionByParams(resource13.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission14 = insertRolePermissionByParams(resource14.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission15 = insertRolePermissionByParams(resource15.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission16 = insertRolePermissionByParams(resource16.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission17 = insertRolePermissionByParams(resource17.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission18 = insertRolePermissionByParams(resource18.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission19 = insertRolePermissionByParams(resource19.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission20 = insertRolePermissionByParams(resource20.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission21 = insertRolePermissionByParams(resource21.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission22 = insertRolePermissionByParams(resource22.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission23 = insertRolePermissionByParams(resource23.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission24 = insertRolePermissionByParams(resource24.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission25 = insertRolePermissionByParams(resource25.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission26 = insertRolePermissionByParams(resource26.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission27 = insertRolePermissionByParams(resource27.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission28 = insertRolePermissionByParams(resource28.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission29 = insertRolePermissionByParams(resource29.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission30 = insertRolePermissionByParams(resource30.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission31 = insertRolePermissionByParams(resource31.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission32 = insertRolePermissionByParams(resource32.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		//RolePermission rolePermission33 = insertRolePermissionByParams(resource33.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission33 = insertRolePermissionByParams(resource33.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		RolePermission rolePermission34 = insertRolePermissionByParams(resource34.getId(), role2.getId(), viewPermission1.getId(), "view", record.getId());
		
		//菜单t_menu
		Menu menu1 = insertMenuByParams(/*"我的"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MY), "", "", null, "0", "", 0, "", 1, 1, 1, "", user.getId(), "", 0, record.getId());
		Menu menu2 = insertMenuByParams(/*"系统管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_MANAGEMENT), "", "", null, "0", "", 1, "1", 1, 1, 1, "", user.getId(), "", 0, record.getId());
		Menu menu3 = insertMenuByParams(/*"集成设置"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATION_SETTINGS), "", "", null, "0", "", 2, "2", 1, 1, 0, "", user.getId(), "", 0, record.getId());
		Menu menu4 = insertMenuByParams(/*"系统监控"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_MONITORING), "", "", null, "0", "", 3, "1", 1, 1, 1, "", user.getId(), "", 0, record.getId());

		//系统管理--的子菜单
		Menu menu6 = insertMenuByParams(/*"组织管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ORGANIZATION_MANAGEMENT), "/organization/index", "", null, menu2.getId(), "", 1, "1", 1, 1, 2, "", user.getId(), resource25.getId(), 0, record.getId());
		Menu menu7 = insertMenuByParams(/*"角色管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ROLE_MANAGEMENT), "/role/index", "", null, menu2.getId(), "", 2, "1", 1, 1, 2, "", user.getId(), resource26.getId(), 0, record.getId());
		Menu menu8 = insertMenuByParams(/*"资源管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_RESOURCE_MANAGEMENT), "/resource/index", "", null, menu2.getId(), "", 3, "1", 1, 1, 2, "", user.getId(), resource23.getId(), 0, record.getId());
		Menu menu9 = insertMenuByParams(/*"权限管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERMISSION_MANAGEMENT), "/rolePermission/index", "", null, menu2.getId(), "", 4, "1", 1, 1, 1, "", user.getId(), resource24.getId(), 0, record.getId());
		Menu menu10 = insertMenuByParams(/*"菜单管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MENU_MANAGEMENT), "/menu/index", "", null, menu2.getId(), "", 5, "1", 1, 1, 1, "", user.getId(), resource27.getId(), 0, record.getId());
		Menu menu11 = insertMenuByParams(/*"列表管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LIST_MANAGEMENT), "/listManage/index", "", null, menu2.getId(), "", 6, "1", 1, 1, 1, "", user.getId(), resource29.getId(), 0, record.getId());
		Menu menu12 = insertMenuByParams(/*"模板管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MODULE_MANAGEMENT), "/module/index", "", null, menu2.getId(), "", 7, "1", 1, 1, 2, "", user.getId(), resource28.getId(), 0, record.getId());
		Menu menu13 = insertMenuByParams(/*"系统设置"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_SETTING), "/config/index", "", null, menu2.getId(), "", 8, "1", 1, 1, 1, "", user.getId(), resource22.getId(), 0, record.getId());
		Menu menu14 = insertMenuByParams(/*"产品信息"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PRODUCT_INFORMATION), "/activate/index", "", null, menu2.getId(), "", 9, "1", 1, 1, 1, "", user.getId(), resource19.getId(), 0, record.getId());
		Menu menu27 = insertMenuByParams(/*"轮播图管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_BANNER), "/banner/index", "", null, menu2.getId(), "", 10, "1", 1, 1, 1, "", user.getId(), resource11.getId(), 0, record.getId());
		Menu menu28 = insertMenuByParams(/*"系统公告"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_NOTICE), "/notice/index", "", null, menu2.getId(), "", 11, "1", 1, 1, 1, "", user.getId(), resource14.getId(), 0, record.getId());
		Menu menu31 = insertMenuByParams(/*"评论管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COMMENT_MANAGEMENT), "/comment/management", "", null, menu2.getId(), "", 11, "1", 1, 1, 1, "", user.getId(), resource32.getId(), 0, record.getId());
		Menu menu33 = insertMenuByParams(/*"任务调度"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SCHEDULING_TASKS), "/scheduleTask/manage/index", "", null, menu2.getId(), "", 11, "1", 1, 1, 1, "", user.getId(), resource33.getId(), 0, record.getId());
		//集成设置--的子菜单
		Menu menu15 = insertMenuByParams(/*"集成系统"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATE_SYSTEM), "/biServer/index", "", null, menu3.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource5.getId(), 0, record.getId());
		Menu menu16 = insertMenuByParams(/*"集成项目"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATE_PROJECT), "/biProject/index", "", null, menu3.getId(), "", 2, "1", 1, 1, 1, "", user.getId(), resource6.getId(), 0, record.getId());
		Menu menu17 = insertMenuByParams(/*"BI账户集成"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ACCOUNT_INTEGRATION), "/biUser/index", "", null, menu3.getId(), "", 3, "1", 1, 1, 1, "", user.getId(), resource8.getId(), 0, record.getId());
		Menu menu18 = insertMenuByParams(/*"BI账户映射"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ACCOUNT_MAPPING), "/biMapping/index", "", null, menu3.getId(), "", 4, "1", 1, 1, 2, "", user.getId(), resource7.getId(), 0, record.getId());
		Menu menu19 = insertMenuByParams(/*"筛选管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PROMPT_MANAGEMENT), "/prompt/index", "", null, menu3.getId(), "", 5, "1", 1, 1, 2, "", user.getId(), resource3.getId(), 0, record.getId());
		Menu menu20 = insertMenuByParams(/*"字典管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DICT_MANAGEMENT), "/dict/index", "", null, menu3.getId(), "", 6, "1", 1, 1, 1, "", user.getId(), resource13.getId(), 0, record.getId());
		Menu menu21 = insertMenuByParams(/*"集成自助分析"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATED_SELF_ANALYSIS), "/biIndependent/index", "", null, menu3.getId(), "", 7, "1", 1, 1, 1, "", user.getId(), resource9.getId(), 0, record.getId());
		//Menu menu33 = insertMenuByParams(/*"微信推送任务"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WECHAT_PUSH_TASK), "/wxPush/index", "", null, menu3.getId(), "", 8, "1", 1, 1, 1, "", user.getId(), resource33.getId(), record.getId());
		//系统监控--的子菜单
		Menu menu22 = insertMenuByParams(/*"问题管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PROBLEM_MANAGEMENT), "/issue/index", "", null, menu4.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource12.getId(), 0, record.getId());
		Menu menu34 = insertMenuByParams(/*"日志服务"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOGGING_SERVICE), "/issue/index", "", null, menu4.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource20.getId(), 0, record.getId());
		Menu menu35 = insertMenuByParams(/*"实时日志"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_REAL_TIME_LOG), "/sysLog/webLog", "", null, menu4.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource21.getId(), 0, record.getId());
		Menu menu36 = insertMenuByParams(/*"用户分析"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_USER_ANALYSIS), "/userAnalysis/one", "", null, menu4.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource31.getId(), 0, record.getId());
		Menu menu37 = insertMenuByParams(/*"在线用户管理"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ONLINE_USER), "/onlineUserManagement/index", "", null, menu4.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource30.getId(), 0, record.getId());

	    //我的--的子菜单
		Menu menu29 = insertMenuByParams(/*"我的收藏"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MY_COLLECTION), "/collect/index", "", null, menu1.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource1.getId(), 0, record.getId());
		Menu menu30 = insertMenuByParams(/*"问题反馈"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_QUESTION_FEEDBACK), "/issue/singleIndex", "", null, menu1.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource0.getId(), 0, record.getId());
		Menu menu38 = insertMenuByParams(/*"个人订阅"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PERSONAL_TASKS), "/scheduleTask/common/index", "", null, menu1.getId(), "", 1, "1", 1, 1, 1, "", user.getId(), resource34.getId(), 0, record.getId());



		//字典管理
		/*List<Dict> dicts = dictMapper.selectList(new EntityWrapper<Dict>());
		for (Dict dict : dicts) {
			dict.setId(null);
			dict.setTenantId(null);
			dict.setTenantId(record.getId());
			dictMapper.insertDictAllColunmForNewTenant(dict);
		}*/
		//公告推荐等级
		dictMapper.insertDictAllColunmForNewTenant(new Dict( LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ANNOUNCEMENT_RECOMMENDATION_LEVEL), "notice_level",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "notice_level",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_URGENT),"1",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "notice_level",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PRESSING),"2",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "notice_level",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_WEIGHTY),"3",3,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "notice_level",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_IMPORTANY),"4",4,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "notice_level",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ORDINARY),"5",5,record.getId(),1,0));
		//菜单类型
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MENU_TYPE),"resource_type",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "resource_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_FUNCTIONAL_MENU),"1",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "resource_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MSTR_REPORT),"2",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "resource_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_TABLEAU_REPORT),"3",3,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "resource_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_FINEREPORT),"4",4,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "resource_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_BO_REPORT),"5",5,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "resource_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EXTERNAL_LINKS),"6",6,record.getId(),1,0));
		//产品类型
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_PRODUCT_TYPE),"case_product",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "case_product","mstr","1",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "case_product","portal","2",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "case_product","ETL","3",3,record.getId(),1,0));
		//	邮件服务器
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_MAIL_SERVER),"MAIL_HOST",null,null,null,record.getId(),0,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,        "MAIL_HOST","smtp.exmail.qq.com","smtp.exmail.qq.com",1,record.getId(),0,0));
		//日期默认值-年
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DATE_DEFAULT_VALUE_YEAR), "date_year",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,             "date_year",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_OTHER),"other",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,             "date_year",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_THIS_YEAR),"cur_year",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,             "date_year",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LAST_YEAR),"last_year",3,record.getId(),1,0));
		//日期默认值-日
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DATE_DEFAULT_VALUE_DAY),"date_day",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_day",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_TODAY),"cur_day",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_day",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_YESTERDAY),"last_day",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_day",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_AT_THE_BEGINNING_OF_THE_MONTH),"cur_month_begin",3,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_day",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_THE_DAY_BEFORE_YESTERDAY),"tdb_yesterday",4,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_day",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EARLY_LAST_MONTH),"last_month_begin",5,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_day",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LATE_LAST_MONTH),"last_month_end",6,record.getId(),1,0));
		//日期默认值-月
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DATE_DEFAULT_VALUE_MONTH),"date_month",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_month",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_THIS_MONTH),"cur_month",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_month",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LAST_MONTH),"last_month",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_month",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_THE_SAME_PERIOD_OF_THE_MONTH),"ytb_month",3,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "date_month",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_OTHER),"other",4,record.getId(),1,0));
		//报告记录
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_REPORTING_RECORDS), "record_type",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,       "record_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_RECORD_ONLY_EXCEPTIONS),"1",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,       "record_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ABNORMAL_RECORD_LAST_NORMAL_RECORD),"2",2,record.getId(),1,0));
		//系统日志类型
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SYSTEM_LOG_TYPE),"sys_log_type",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_ADD),"1",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DELETE),"2",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_EDIT),"3",3,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_QUERY),"4",4,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOG_ON),"5",5,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LOGOUT),"6",6,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_VIEW_PREVIEW),"7",7,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_UPLOAD),"8",8,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DOWNLOAD),"9",9,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,          "sys_log_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COLLECTION),"10",10,record.getId(),1,0));
		//集成日志类型
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INTEGRATED_LOG_TYPE),"integration_type",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "integration_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_FUNCTION_DEVELOPEMENT),"1",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,           "integration_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_REPAIR_DEFECT),"2",2,record.getId(),1,0));
		//输入类型
		dictMapper.insertDictAllColunmForNewTenant(new Dict(LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_INPUT_TYPE),"input_type",null,null,null,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_TEXT),"1",1,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_NUMBER),"2",2,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DATE),"3",3,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_SELECTION_LIST),"4",4,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_RADIO_BUTTON),"5",5,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_CHECK_BOX),"6",6,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_NUMBER_INTERVAL),"7",7,record.getId(),1,0));
		dictMapper.insertDictAllColunmForNewTenant(new Dict(null,      "input_type",LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DATE_INTERVAL),"8",8,record.getId(),1,0));




		//列表管理初始化
		ListManage listManage = new ListManage();//name,parentId,creater,sort,introduce,tanantId
		listManage.setName(/*"列表"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_LIST));
		listManage.setParentId("0");
		listManage.setCreater(user.getId());
		listManage.setSort(0);
		listManage.setIntroduce("");
		listManage.setTenantId(record.getId());
		listManageMapper.insertListManageAllColunmForNewTenant(listManage);
		
		//系统参数的复制------------------------------这部分由于时复制数据，需要手动初始化数据库中的多语言
		EntityWrapper<Config> ew = new EntityWrapper<>();
		List<Config> configs = configMapper.selectList(ew);

		for (Config config : configs) {
			if("SYS_SYNC_CREATE_MSTR_USER".equals(config.getCode()))
				config.setValue("0");
			if("SYS_THEME".equals(config.getCode()))
				config.setValue("theme_left_admin");
			if("SYS_FIRST_UPDATE_PWD".equals(config.getCode()))
				config.setValue("1");
			if("REPEAT_LOGIN".equals(config.getCode()))
				config.setValue("1");
			if("LOGIN_PROMPT".equals(config.getCode()))
				config.setValue("0");
			if("FORGET_PASSWORD_PROMPT".equals(config.getCode()))
				config.setValue("0");
			if("PASSWORD_UPDATE_REGULARLY".equals(config.getCode()))
				config.setValue("0");
			if("PASSWORD_STRENGTH_REQUIRE".equals(config.getCode()))
				config.setValue("0");
			if("SYS_LOGIN_NAME".equals(config.getCode()))
				config.setValue(/*"数窗平台"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DATAWINDOW));
			if("SYS_MAIN_LOGO".equals(config.getCode()))
				config.setValue("");
			if("SYS_HOME_PAGE_NAME".equals(config.getCode()))
				config.setValue(/*"首页"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_HOME_PAGE));
			if("PREFERENCE_TYPE".equals(config.getCode()))
				config.setValue("orgHome");
			if("PREFERENCE_VALUE".equals(config.getCode()))
				config.setValue("");
			if("SYS_MENU_ICON".equals(config.getCode()))
				config.setValue("1");
			if("SYS_SYNC_CREATE_MSTR_USER".equals(config.getCode()))
				config.setValue("1");
			if("SYS_CHECK_CODE".equals(config.getCode()))
				config.setValue("0");
			if("SYS_NAME".equals(config.getCode()))
				config.setValue(/*"数窗平台"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DATAWINDOW));
			if("MAIL_ACCOUNT".equals(config.getCode()))
				config.setValue(/*"数窗平台"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_DEANG_DATAWINDOW_PLATFORM));
			if("SYS_LOGIN_PAGE".equals(config.getCode()))
				config.setValue("login/login_xdf");
			if("SYS_LOGO".equals(config.getCode()))
				config.setValue("");
			if("SYS_COPYRIGHT".equals(config.getCode()))
				config.setValue(/*"©2018 信息技术（北京）有限公司    京 ICP 备14011400号-20 "*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.TENANT_COPYRIGHT));
			if("SYS_USER_EMAIL".equals(config.getCode()))
				config.setValue("1");
			if("SYS_HOME_INDEX".equals(config.getCode()))
				config.setValue("index");
			if("SESSION_TIME".equals(config.getCode()))
				config.setValue("30");
			if("CONFIG_LOCALE".equals(config.getCode()))
				config.setValue(CONFIG_LOCALE);
			if("SYS_USER_TENANT".equals(config.getCode()))
				config.setValue("1");
			if("PASSWORD_ERROR_LOCK".equals(config.getCode()))
				config.setValue("0");
			if("PASSWORD_ERROR_COUNT".equals(config.getCode()))
				config.setValue("5");
			config.setId(null);
			config.setTenantId(record.getId());
			config.insert();
		}
		
		//数据权限类型初始话数据插入
		DataPermissionType dataPermissionType = new DataPermissionType();//name,parentId,creater,sort,introduce,tanantId
		dataPermissionType.setName(/*"数据权限类型"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.DATA_PERMISSION_TYPE));
		dataPermissionType.setParentId("0");
		dataPermissionType.setExtcode("data_permission_type");
		dataPermissionType.setSort(1);
		dataPermissionType.setCreateTime(new Date());
		dataPermissionType.setRemark(/*"数据权限类型"*/LangTransform.getLocaleLang(CONFIG_LOCALE, LanguageParam.DATA_PERMISSION_TYPE));
		dataPermissionType.setTenantId(record.getId());
		dataPermissionTypeMapper.insertDataPermissionTypeAllColunmForNewTenant(dataPermissionType);
		
		return true;
	}
	
	private Resource InsertResourceByParams(String name,String linkUrl,String code,
			String parentId,String iconName,String thumbnailId,String thumbnail,Integer sort,
			String type,Integer linkType,Integer state,Integer lv,String introduce,String creater,
			String projectId,String reportId,String typeName,Integer typeValue,String hiddenSections,
			String viewNum,String downloadNum,String collectNum,String commentNum,String path,
			String resourceType1,String resourceType2,String fileId,Integer isMobile,String tenantId){
		Resource resource = new Resource();
		resource.setName(name);
		resource.setLinkUrl(linkUrl);
		resource.setCode(code);
		resource.setParentId(parentId);
		resource.setCreateTime(new Date());
		resource.setIconName(iconName);
		resource.setThumbnailId(thumbnailId);
		resource.setThumbnail(thumbnail);
		resource.setSort(sort);
		resource.setType(type);
		resource.setLinkType(linkType);
		resource.setState(state);
		resource.setLv(lv);
		resource.setIntroduce(introduce);
		resource.setCreater(creater);
		resource.setProjectId(projectId);
		resource.setReportId(reportId);
		resource.setTypeName(typeName);
		resource.setTypeValue(typeValue);
		resource.setHiddenSections(hiddenSections);
		resource.setViewNum(viewNum);
		resource.setDownloadNum(downloadNum);
		resource.setCollectNum(collectNum);
		resource.setCommentNum(commentNum);
		resource.setPath(path);
		resource.setResourceType1(resourceType1);
		resource.setResourceType2(resourceType2);
		resource.setFileId(fileId);
		resource.setIsMobile(isMobile);
		resource.setTenantId(tenantId);
		resourceMapper.insertResourceAllColunmForNewTenant(resource);
		return resource;
	}

	private ResourceType insertResourceTypeByParams(String name,Integer code, String parentId,Integer sort,String remark,String tenantId){
		ResourceType resourceType=new ResourceType();
		resourceType.setCode(code);
		resourceType.setName(name);
		resourceType.setParentId(parentId);
		resourceType.setSort(sort);
		resourceType.setRemark(remark);
		resourceType.setTenantId(tenantId);
		resourceTypeMapper.insertResourceTypeAll(resourceType);
		return resourceType;

	}

	private Permission InsertPermissionByParams(String resourceTypeId,String name,String code,String resourceId,String tanantId,Integer sort){
		Permission permission = new Permission<>();
		permission.setResourceTypeId(resourceTypeId);
		permission.setName(name);
		permission.setCode(code);
		permission.setResourceId(resourceId);
		permission.setTenantId(tanantId);
		permission.setSort(sort);
		permissionMapper.insertPermissionAllColunmForNewTenant(permission);
		return permission;
	}
	
	private RolePermission insertRolePermissionByParams(String resourceId,String roleId,String permissionId,
			String code,String tenantId){
		RolePermission rolePermission = new RolePermission();
		rolePermission.setResourceId(resourceId);
		rolePermission.setRoleId(roleId);
		rolePermission.setPermissionId(permissionId);
		rolePermission.setCode(code);
		rolePermission.setTenantId(tenantId);
		rolePermissionMapper.insertRolePermission(rolePermission);
		return rolePermission;
	}
	
	private Menu insertMenuByParams(String name,String linkUrl,String code,Integer status,String parentId,
			String iconName,Integer sort,String type,Integer linkType,Integer state,Integer lv,String introduce,String creater,
			String resourceId, Integer isMobile, String tenantId){
		Menu menu = new Menu();
		menu.setName(name);
		menu.setLinkUrl(linkUrl);
		menu.setCode(code);
		menu.setStatus(status);
		menu.setParentId(parentId);
		menu.setIconName(iconName);
		menu.setSort(sort);
		menu.setType(type);
		menu.setLinkType(linkType);
		menu.setState(state);
		menu.setLv(lv);
		menu.setIntroduce(introduce);
		menu.setCreater(creater);
		menu.setResourceId(resourceId);
		menu.setIsMobile(isMobile);
		menu.setTenantId(tenantId);
		menuMapper.insertMenuAllColunmForNewTenant(menu);
		return menu;
	}
	
	private Role insertRoleByParams(String name,String code,String parentId,String type,Integer sort,Integer state,String tenantId){
		Role role = new Role();
		role.setName(name);
		role.setCode(code);
		role.setParentId(parentId);
		role.setType(type);
		role.setSort(sort);
		role.setCreateTime(new Date());
		role.setState(state);
		role.setTenantId(tenantId);
		roleMapper.insertAllRoleColunmForNewTenant(role);
		return role;
	}
	
	private String getNewOrgCode(){
		Integer MaxCode = getMaxOrgCodeOfParentId();
		String newMaxCodeString = "";
        if(MaxCode != null && MaxCode <= 999){
            int newMaxCodeInt = MaxCode + 1;
            newMaxCodeString = String.format("%03d", newMaxCodeInt);
        }

        return newMaxCodeString;
    }
	
	private Integer getMaxOrgCodeOfParentId(){
		Organization orgEntity = new Organization();
		orgEntity.setParentId("0");
		List<Organization> list = organizationMapper.selectListWithNoTenant(orgEntity);
		List<Integer> listCode = new ArrayList<>();
		Integer MaxCode = 0;
		for (Organization org : list) {
			if(org.getCode()!=null && org.getCode().trim().length()>0){
				listCode.add(Integer.parseInt(org.getCode()));
			}
		}
		if(listCode!=null && listCode.size()>0){
			MaxCode = Collections.max(listCode);
		}
		return MaxCode;
	}


	@Override
	public List<Tenant> selectLists() {
		return mapper.selectLists();
	}
}
