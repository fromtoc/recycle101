package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mapper.MessageCenterMapper;
import com.xin.portal.system.mapper.UserSettingMapper;
import com.xin.portal.system.model.MessageCenter;
import com.xin.portal.system.model.UserSetting;
import com.xin.portal.system.service.MessageCenterService;
import com.xin.portal.system.util.SessionUtil;

/**
 *  服务实现类
 *
 * @author zhoujun
 * @since 2019-06-21
 */
@Service
public class MessageCenterServiceImpl extends ServiceImpl<MessageCenterMapper, MessageCenter> implements MessageCenterService {
	
	@Autowired
	private MessageCenterMapper mapper;
	@Autowired
	private UserSettingMapper userSettingMapper;
	
	@Override
	public PageModel<MessageCenter> page(MessageCenter query, Integer pageNumber, Integer pageSize) {
		Page<MessageCenter> page = new Page<MessageCenter>(pageNumber, pageSize);
		EntityWrapper<MessageCenter> ew=new EntityWrapper<MessageCenter>(query);
		page = selectPage(page, ew);
		return new PageModel<MessageCenter>(page);
	}

	@Override
	public Map<String, Object> findUnReadMessage() {
		//获取个人设置  由于初始的时候是没有这个设置内容 如果Setting为空，则默认都提示
		UserSetting setting = userSettingMapper.selectByUseId(SessionUtil.getUserId());
		List<Integer> typeList = new ArrayList<>();
		Integer commentMsg = null;
		Integer systemMsg = null;
		Integer issueMsg= null;
		if(setting!=null){
			commentMsg = setting.getCommentMsg();
			systemMsg = setting.getSystemMsg();
			issueMsg = setting.getIssueMsg();
		}
		if(commentMsg!=null&&commentMsg==0){
			typeList.add(MessageCenter.INTERACTIVE_MESSAGE);
		}
		if(systemMsg!=null&&systemMsg==0){
			typeList.add(MessageCenter.NOTICE_MESSAGE);
		}
		if(issueMsg!=null&&issueMsg==0){
			typeList.add(MessageCenter.PROBLEMFEEDBACK_MESSAGE);
		}
		Map<String, Object> map = new HashMap<>();
		List<MessageCenter> readLists = mapper.findList(SessionUtil.getUserId(),"1",typeList);
		List<MessageCenter> unReadLists = mapper.findList(SessionUtil.getUserId(),"0",typeList);
		List<MessageCenter> showLists = new ArrayList<>();
		if(unReadLists!=null && unReadLists.size()>0){
			int count = unReadLists.size();
			map.put("unReadCount", count);
			if(count >= 5){
				showLists = unReadLists.subList(0, 5);
			}else{
				showLists = unReadLists;
				if(readLists!=null && 5-count>=readLists.size()){
					showLists.addAll(readLists);
				}else{
					showLists.addAll(readLists.subList(0, 5-count));
				}
			}
		}else{
			if(readLists.size()>=5){
				showLists = readLists.subList(0, 5);
			}else{
				showLists = readLists;
			}
			map.put("unReadCount", 0);
		}
		map.put("messageList", showLists);
		return map;
	}

	@Override
	public Map<String, Object> findAllUnReadMessageMap() {
		//获取个人设置  由于初始的时候是没有这个设置内容 如果Setting为空，则默认都提示
		UserSetting setting = userSettingMapper.selectByUseId(SessionUtil.getUserId());
		List<Integer> typeList = new ArrayList<>();
		Integer commentMsg = null;
		Integer systemMsg = null;
		if(setting!=null){
			commentMsg = setting.getCommentMsg();
			systemMsg = setting.getSystemMsg();
		}
		if(commentMsg!=null&&commentMsg==0){
			typeList.add(MessageCenter.INTERACTIVE_MESSAGE);
		}
		if(systemMsg!=null&&systemMsg==0){
			typeList.add(MessageCenter.NOTICE_MESSAGE);
		}
		Map<String, Object> map = new HashMap<>();
		List<MessageCenter> readLists = mapper.findList(SessionUtil.getUserId(),"1",typeList);
		List<MessageCenter> unReadLists = mapper.findList(SessionUtil.getUserId(),"0",typeList);
		List<MessageCenter> showLists = new ArrayList<>();

		showLists = unReadLists;
		showLists.addAll(readLists);
		map.put("messageList", showLists);
		return map;
	}


	@Override
	public boolean updateAllMessageIsRead(String userId) {
		try {
			mapper.updateAllMessageIsReadById(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean deleteMessageBySourceId(String sourceId){
		EntityWrapper<MessageCenter> ew = new EntityWrapper<>();
		ew.eq("message_source_id", sourceId);
		return mapper.delete(ew)>0 ? true : false;
	}

	@Override
	public boolean clearAllMessage(String userId) {
		try {
			mapper.clearAllMessageById(userId);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
