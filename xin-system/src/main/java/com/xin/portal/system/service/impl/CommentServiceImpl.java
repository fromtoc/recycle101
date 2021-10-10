package com.xin.portal.system.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.xin.portal.system.mapper.*;
import com.xin.portal.system.model.*;
import com.xin.portal.system.service.CommentService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.xin.portal.system.service.RoleService;

import com.xin.portal.system.util.webSocketLog.WebSocketServer;

import com.xin.portal.system.util.Constant;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author xue
 * @since 2018-12-03
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
	
	private Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
	
	@Autowired 
	private CommentMapper mapper;
	@Autowired
	private RoleService roleService;
	@Autowired
	private ResourceMapper resourceMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private MessageCenterMapper messageCenterMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private IssueMapper issueMapper;
	
	
	@Override
	public PageModel<Comment> page(Comment query, Integer pageNumber, Integer pageSize) {
		Page<Comment> page = new Page<Comment>(pageNumber, pageSize);
		EntityWrapper<Comment> ew=new EntityWrapper<Comment>(query);
		page = selectPage(page, ew);
		return new PageModel<Comment>(page);
	}

	@Override
	public List<Comment> selectByResourceId(String resourceId) {
		// TODO Auto-generated method stub
		return mapper.selectByResourceId(resourceId);
	}

	@Override
	public PageModel<Comment> selectCommentMore(Comment query , Integer pageNumber , Integer pageSize) {
		Page<Comment> page = new Page<Comment>(pageNumber, pageSize);
		page = page.setRecords(mapper.selectCommentMore(query,page));
		return new PageModel<Comment>(page);
	}

	@Override
	public void addMessageAfterSaveComment(String userId, Comment comment) {
		try {
			if(comment.getRemindUser()!=null && !"".equals(comment.getRemindUser())){
				List<String> remindUsers = new ArrayList<String>();
				if(comment.getRemindUser().contains(",")){
					remindUsers = Arrays.asList(comment.getRemindUser().split(","));
				}else{
					remindUsers.add(comment.getRemindUser());
				}
				//获取到资源以及当前用户的信息
				Resource res = resourceMapper.selectById(comment.getResourceId());
				UserInfo userInfo = userInfoMapper.selectById(userId);
				WebSocketServer web = new WebSocketServer();
				//通知@的用户
				if(remindUsers!=null&&!"".equals(remindUsers)){
					for (String remindUser : remindUsers) {
						MessageCenter msg = new MessageCenter();
						msg.setCreateTime(new Date());
						msg.setIsRead(0);
						msg.setTitle(userInfo.getRealname()+"对【"+res.getName()+"】进行了评论");
						msg.setType(MessageCenter.INTERACTIVE_MESSAGE);
						msg.setResourceId(comment.getResourceId());
						msg.setMessageSourceId(comment.getId());//存放来源id
						msg.setProduceUser(userId);
						String content = comment.getContent();
						msg.setContent(content);
						web.setonMessage("3",remindUser);
						if(!userId.equals(remindUser)){
							msg.setReceiveUser(remindUser);
							messageCenterMapper.insert(msg);
						}
					}
				}
			}
			/*EntityWrapper<RolePermission> ewp = new EntityWrapper<>();
			ewp.eq("code", Permission.COMMENT);
			ewp.eq("resource_id", comment.getResourceId());
			List<RolePermission> listPermission = rolePermissionMapper.selectList(ewp);
			if(listPermission!=null && listPermission.size()>0){
				List<String> roleIds = listPermission.stream().map(RolePermission::getRoleId).collect(Collectors.toList());
				//获取到角色的用户
				List<RoleUser> roleUserList = roleUserMapper.findUsersByRoleIds(roleIds);
				if(roleUserList!=null && roleUserList.size()>0){
					//通知所有的用户
					for (String remindUser : userIds) {
						MessageCenter msg = new MessageCenter();
						msg.setCreateTime(new Date());
						msg.setIsRead(0);
						msg.setTitle(userInfo.getRealname()+"对【"+res.getName()+"】进行了评论。");
						msg.setType(MessageCenter.INTERACTIVE_MESSAGE);
						msg.setResourceId(comment.getResourceId());
						msg.setProduceUser(userId);
						String content = comment.getContent();
						msg.setContent(content);
						if(!userId.equals(remindUser)){
							msg.setReceiveUser(remindUser);
							messageCenterMapper.insert(msg);
						}
					}
				}
			}*/
		} catch (Exception e) {
			logger.error("评论后添加消息中心信息失败");
			e.printStackTrace();
		}
	}
	
	@Override
	public void addIssueMessageAfterSaveComment(String userId, IssueRecord issueRecord, Issue issue) {
		try {
				//获取当前用户的信息
				UserInfo userInfo = userInfoMapper.selectById(userId);
				WebSocketServer web = new WebSocketServer();
				//通知的用户
				MessageCenter msg = new MessageCenter();
				msg.setCreateTime(new Date());
				msg.setIsRead(0);
				msg.setTitle(userInfo.getRealname()+"对【"+issue.getTitle()+"】进行了回复");
				msg.setType(MessageCenter.PROBLEMFEEDBACK_MESSAGE);
				//msg.setResourceId(comment.getResourceId());
				msg.setIssueId(issueRecord.getIssueId());
				msg.setMessageSourceId(issueRecord.getId());//存放来源id
				msg.setProduceUser(userId);
				String content = issueRecord.getContent();
				msg.setContent(content);
				web.setonMessage("3",issue.getCreater());
				if(!userId.equals(issue.getCreater())){
					msg.setReceiveUser(issue.getCreater());
					messageCenterMapper.insert(msg);
				}
		} catch (Exception e) {
			logger.error("回复后添加消息中心信息失败");
			e.printStackTrace();
		}
	}

	@Override
	@Transactional
	public List<AtUser> remindUser(String resourceId, String tenantId){
		//筛选所有具有该资源view权限和comment权限的用户
		//将这些用户区分为：“管理员” 与 “其他人员”
		String code = "admin";
		if(!tenantId.equals("1"))//通过tenantId判断管理员角色的code是否时admin或
		code="admin_system";
		EntityWrapper<Role> ew = new EntityWrapper<>();
		ew.eq("code",code);
		String parentId= roleService.selectOne(ew).getId();
		EntityWrapper<Role> entityWrapper = new EntityWrapper<>();
		entityWrapper.eq("parent_id",parentId);
		List<Role> roleList = roleService.selectList(entityWrapper);//通过管理员的id查询到所有管理员下的“子管理员”
		List<String> roleIds = roleList.stream().map(Role::getId).collect(Collectors.toList());
		roleIds.add(parentId);//将管理员id以及属于管理员的下的子节点管理员id放到roleIds中
		String locale =( (Locale)SessionUtil.getSession(Constant.ConfigKeys.SYS_LOCALE)).toString();
		String admin= LangTransform.getLocaleLang(locale,LanguageParam.COMMENT_ADMIN);
		String other=LangTransform.getLocaleLang(locale,LanguageParam.COMMENT_OTHER);
		List<AtUser> remindList = mapper.adminUser(resourceId,roleIds,tenantId);
		List<String> adminIds = new ArrayList<>();
		for(AtUser user:remindList){
			user.setGroupName(admin);
			adminIds.add(user.getId());//得到所有有管理员角色的用户的ID;
		}
		List<AtUser> otherUser = mapper.otherUser(resourceId,roleIds,tenantId,adminIds);//将有管理员角色的用户全部过滤掉得到“其他用户”
		for(AtUser user:otherUser){
			user.setGroupName(other);
			user.setGroupId("other");
			remindList.add(user);
		}
		return remindList;
	}


}
