package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.mail.MailModel;
import com.xin.portal.system.mail.MailService;
import com.xin.portal.system.mapper.ConfigMapper;
import com.xin.portal.system.mapper.MessageCenterMapper;
import com.xin.portal.system.mapper.ResourceMapper;
import com.xin.portal.system.mapper.UserInfoMapper;
import com.xin.portal.system.mapper.WxPushMapper;
import com.xin.portal.system.mapper.WxPushRecordMapper;
import com.xin.portal.system.model.Config;
import com.xin.portal.system.model.JobAndTrigger;
import com.xin.portal.system.model.MessageCenter;
import com.xin.portal.system.model.Resource;
import com.xin.portal.system.model.UserInfo;
import com.xin.portal.system.model.WxPush;
import com.xin.portal.system.model.WxPushRecord;
import com.xin.portal.system.service.JobAndTriggerService;
import com.xin.portal.system.service.ResourceService;
import com.xin.portal.system.service.WxPushService;
import com.xin.portal.system.util.Constant.ConfigKeys;
import com.xin.portal.system.util.DateUtil;
import com.xin.portal.system.util.LangTransform;
import com.xin.portal.system.util.SessionUtil;
import com.xin.portal.system.util.i18n.LanguageParam;
import com.xin.portal.system.util.webSocketLog.WebSocketServer;

import me.chanjar.weixin.common.api.WxConsts.KefuMsgType;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 微信推送表 服务实现类
 *
 * @author zhoujun
 * @since 2019-02-11
 */
@Service
public class WxPushServiceImpl extends ServiceImpl<WxPushMapper, WxPush> implements WxPushService {

//    private static Logger log = LoggerFactory.getLogger(WxPushServiceImpl.class);

    @Autowired
    private JobAndTriggerService jobAndTriggerService;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private MailService mailService;
    @Autowired
    private MessageCenterMapper messageCenterMapper;
    @Autowired
    private WxPushMapper wxPushMapper;
    @Autowired
    private WxPushRecordMapper wxPushRecordMapper;
    @Autowired
    private ConfigMapper configMapper; 
    @Autowired
    private ResourceMapper resourceMapper;

    @Override
	public PageModel<WxPush> page(WxPush query, Integer pageNumber, Integer pageSize) {
    	if(WxPush.TASK_USER_COMMON.equals(query.getUserType())){
    		query.setCreater(SessionUtil.getUserId());
    		query.setUserType(WxPush.TASK_USER_COMMON);
    	}
		Page<WxPush> page = new Page<WxPush>(pageNumber, pageSize);
		EntityWrapper<WxPush> ew=new EntityWrapper<WxPush>();
		ew.like(!StringUtils.isEmpty(query.getMsgTitle()), "msg_title", query.getMsgTitle());
		ew.eq(!StringUtils.isEmpty(query.getUserType()), "user_type", query.getUserType());
		ew.eq(!StringUtils.isEmpty(query.getCreater()), "creater", query.getCreater());
		ew.eq(!StringUtils.isEmpty(query.getTenantId()), "tenant_id", query.getTenantId());
		page = selectPage(page, ew);
		return new PageModel<WxPush>(page);
	}
    
	@Override
	public boolean save(WxPush record) {
		if(WxPush.TASK_TYPE_PUSH.equals(record.getTaskType())){//推送
			long systemTime = System.currentTimeMillis();
			//判断执行方式
			switch (record.getPushType()) {
			case WxPush.PUSH_TYPE_SYSMSG:
				record.setJobName("站内消息推送_"+systemTime);
				record.setJobGroup("SysMsgJob_"+systemTime);
                record = setTriggerByTypeOfSave(systemTime, record, "SysMsgJob_Trigger_");
				break;
			case WxPush.PUSH_TYPE_EMAIL:
				record.setJobName("邮箱消息推送_"+systemTime);
				record.setJobGroup("EmailJob_"+systemTime);
				record = setTriggerByTypeOfSave(systemTime, record, "EmailJob_Trigger_");
				break;
			case WxPush.PUSH_TYPE_WECHAT:
				record.setJobName("微信企业号消息推送_"+systemTime);
				record.setJobGroup("CpMsgJob_"+systemTime);
				record = setTriggerByTypeOfSave(systemTime, record, "CpMsgJob_Trigger_");
				break;
			case WxPush.PUSH_TYPE_LINE:
				record.setJobName("line_Business消息推送"+systemTime);
				record.setJobGroup("LineMsgJob_"+systemTime);
				record = setTriggerByTypeOfSave(systemTime, record, "LineMsgJob_Trigger_");
			default:
				break;
			}
		}else if(WxPush.TASK_TYPE_RUN.equals(record.getTaskType())){//
			//暂时不作实现	
			return false;
		}
		if(WxPush.TYPE_NOW.equals(record.getType()) && record.getId()!=null){
			return record.insertOrUpdate();
		}
		return record.insert();
	}
	
	private WxPush setTriggerByTypeOfSave(long systemTime, WxPush record, String prefixTri){
		//判断执行时机
		record.setTriggerName("Trigger_"+systemTime);
		record.setJobState(WxPush.JOB_STATE_NEW);
		switch (record.getType()) {
		case WxPush.TYPE_NOW:
			record.setTriggerGroup(prefixTri+"Now_"+systemTime);
			record.setJobCron(DateUtil.getCron((System.currentTimeMillis()+15000)));//立即执行为15秒后触发
			record.setJobState(WxPush.JOB_STATE_NEW);
			//即时执行的创建时执行
			boolean isSuccess = false;
			if(record.getId()!=null && record.getId().length()>0){//根据id判断是否为编辑
				isSuccess = true;
			}else{//如果没有ID说明是新增,需要先新增以获取ID。在jobAndTriggerService.save 方法中需要用到id
				isSuccess = record.insert();
			}
			if(isSuccess){
				boolean isSave = jobAndTriggerService.save(record);
				if(isSave){
					record.setJobState(WxPush.JOB_STATE_OVER);
				}
			}
			break;
		case WxPush.TYPE_JOB_ONCE:
			record.setTriggerGroup(prefixTri+"Once_"+systemTime);
			record.setJobCron(DateUtil.getCron(record.getJobTime().getTime()));
			break;
		case WxPush.TYPE_JOB:
			record.setTriggerGroup(prefixTri+systemTime);
			record.setJobCron(record.getJobCron());
			break;
		case WxPush.TYPE_CUSTOM:
			record.setTriggerGroup(prefixTri+"Custom"+systemTime);
			record.setJobCron(record.getJobCron());
			break;
		default:
			break;
		}
		return record;
	}
	
	/*private boolean setTriggerByType(JobAndTrigger jobAndTrigger, long systemTime, WxPush record, String prefixTri){
		//判断执行时机
		boolean result = false;
		Map<String,Object> jobDataMap = new HashMap<>();
		switch (record.getType()) {
		case WxPush.TYPE_NOW:
			record.setJobId(jobAndTrigger.getTriggerName());
			record.setJobGroup(prefixTri+"Now_"+systemTime);
			record.setJobCron(DateUtil.getCron(System.currentTimeMillis()+3000));//立即执行为3秒后触发
	        record.insert();
	        jobDataMap.put("wxPushId", record.getId());
	        jobAndTrigger.setDataMap(jobDataMap);
			jobAndTrigger.setTriggerGroup(prefixTri+"Now_"+systemTime);
			jobAndTrigger.setCronExpression(record.getJobCron());
            result = jobAndTriggerService.save(jobAndTrigger);
			break;
		case WxPush.TYPE_JOB_ONCE:
			record.setJobId(jobAndTrigger.getTriggerName());
			record.setJobGroup(prefixTri+"Once_"+systemTime);
			record.setJobCron(DateUtil.getCron(record.getJobTime().getTime()));
	        record.insert();
	        jobDataMap.put("wxPushId", record.getId());
	        jobAndTrigger.setDataMap(jobDataMap);
			jobAndTrigger.setTriggerGroup(prefixTri+"Once_"+systemTime);
			jobAndTrigger.setCronExpression(record.getJobCron());
            result = jobAndTriggerService.save(jobAndTrigger);
			break;
		case WxPush.TYPE_JOB:
			record.setJobId(jobAndTrigger.getTriggerName());
			record.setJobGroup(prefixTri+systemTime);
	        record.insert();
	        jobDataMap.put("wxPushId", record.getId());
	        jobAndTrigger.setDataMap(jobDataMap);
			jobAndTrigger.setTriggerGroup(prefixTri+systemTime);
			jobAndTrigger.setCronExpression(record.getJobCron());
			result = jobAndTriggerService.save(jobAndTrigger);
			break;
		default:
			break;
		}
		return result;
	}*/

    @Override
    public boolean updatePush(WxPush record) {
    	WxPush wxPush = wxPushMapper.selectById(record.getId());
		try {//先删除job任务，将状态改为新建。后续会调用启动方法相当于新建job
			jobAndTriggerService.deleteJob(wxPush.getTriggerName(), wxPush.getTriggerGroup(), wxPush.getJobName(), wxPush.getJobGroup());
			//重新设置record中的任务相关字段
			if(WxPush.TASK_TYPE_PUSH.equals(record.getTaskType())){//推送
				long systemTime = System.currentTimeMillis();
				//判断执行方式
				switch (record.getPushType()) {
				case WxPush.PUSH_TYPE_SYSMSG:
					record.setJobName("站内消息推送_"+systemTime);
					record.setJobGroup("SysMsgJob_"+systemTime);
	                record = setTriggerByTypeOfSave(systemTime, record, "SysMsgJob_Trigger_");
					break;
				case WxPush.PUSH_TYPE_EMAIL:
					record.setJobName("邮箱消息推送_"+systemTime);
					record.setJobGroup("EmailJob_"+systemTime);
					record = setTriggerByTypeOfSave(systemTime, record, "EmailJob_Trigger_");
					break;
				case WxPush.PUSH_TYPE_WECHAT:
					record.setJobName("微信企业号消息推送_"+systemTime);
					record.setJobGroup("CpMsgJob_"+systemTime);
					record = setTriggerByTypeOfSave(systemTime, record, "CpMsgJob_Trigger_");
					break;
				case WxPush.PUSH_TYPE_LINE:
					record.setJobName("line_Business消息推送"+systemTime);
					record.setJobGroup("LineMsgJob_"+systemTime);
					record = setTriggerByTypeOfSave(systemTime, record, "LineMsgJob_Trigger_");
					break;
				default:
					break;
				}
			}else if(WxPush.TASK_TYPE_RUN.equals(record.getTaskType())){//
				//暂时不作实现	
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return record.updateById();
    }

	@Override
	public boolean sendEmail(WxPush record) {
		MailModel mail = new MailModel();
		mail.setSubject(record.getMsgTitle());
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		List<String> emailList = new ArrayList<String>();
		List<String> successUser = new ArrayList<String>();
		List<String> errorUser = new ArrayList<String>();
		//查询租户的系统语言
		Config config = configMapper.findByCode(ConfigKeys.CONFIG_LOCALE, record.getTenantId());
		switch (record.getMsgType()){
        case KefuMsgType.TEXT:
        	mail.setContent("<p>"+record.getMsgContent()+"<p>");
            break ;
        case "report":
        	Resource r=resourceMapper.selectByIdWithTenantId(record.getResourceId(),record.getTenantId());
        	mail.setContent(record.getMsgContent()+"</br>"+"请点击:"+"<a href='"+record.getMsgUrl()+"'  target='_parent'>"+r.getName()+"</a> 查看");
            break;
        default:   //文本
        	mail.setContent("<p>"+record.getMsgContent()+"<p>");
            break;
		}
        if (WxPush.TO_TYPE_USER.equals(record.getToType())) {// 给个人
            String users = record.getToContent();
            List<String> contents = Arrays.asList(users.split(","));
            List<String> userIds = Lists.newArrayList();
            for (String user : contents) {
                userIds.add(user.split("@")[0]);
            }
            userInfos = userInfoMapper.selectBatchIdsWithTenantId(userIds,record.getTenantId());
        } else if (WxPush.TO_TYPE_ROLE.equals(record.getToType())) { //角色
            String roles = record.getToContent();
            List<String> contents = Arrays.asList(roles.split(","));
            StringBuilder sb = new StringBuilder();
            for (String role : contents) {
                sb.append(role.split("@")[0]).append(",");
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setRoleIdIn(sb.substring(0,sb.length()-1));
            userInfo.setTenantId(record.getTenantId());
            userInfos = userInfoMapper.findListWithTenantId(userInfo);
        }else if (WxPush.TO_TYPE_ORG.equals(record.getToType())) {  //组织
            String orgs = record.getToContent();
            List<String> contents = Arrays.asList(orgs.split(","));
            StringBuilder sb = new StringBuilder();
            for (String org : contents) {
                sb.append(org.split("@")[0]).append(",");
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setOrgIdIn(sb.substring(0,sb.length()-1));
            userInfo.setTenantId(record.getTenantId());
            userInfos = userInfoMapper.findListWithTenantId(userInfo);
        }
        for (UserInfo userInfo : userInfos) {
			if(userInfo.getEmail()!=null && userInfo.getEmail().length()>0){
				emailList.add(userInfo.getEmail());
			}
		}
        if(emailList!=null && emailList.size()>0){
        	for (String email : emailList) {
        		mail.setToAddress(email);
			}
        }
        for (UserInfo userInfo : userInfos) {
			if(emailList.contains(userInfo.getEmail())){
				try {
					//获取邮件相关的参数
					List<Config> configList = configMapper.selectEmailParamWithTenant(record.getTenantId());
					boolean sendSucc = mailService.send(mail, configList, true);
					if(sendSucc){
						successUser.add(userInfo.getId());
					}else{
						errorUser.add(userInfo.getId());
					}
				} catch (Exception e) {
					errorUser.add(userInfo.getId());
					e.printStackTrace();
				}
			}else{//没有找到email  待处理
				errorUser.add(userInfo.getId());
			}
		}
        //记录   任务发送记录
		WxPushRecord wxPushRecord = new WxPushRecord();
        wxPushRecord.setToUser(userInfos.stream().map(UserInfo::getId).collect(Collectors.joining(",")));
        wxPushRecord.setWxPushId(record.getId());
        wxPushRecord.setCreateTime(new Date());
        wxPushRecord.setResultCode(((userInfos!=null&&userInfos.size()>0)&&(successUser!=null&&successUser.size()>0))?0:1);
        wxPushRecord.setResultMsg(null);
        wxPushRecord.setToUserError((errorUser!=null&&errorUser.size()>0)?StringUtils.collectionToDelimitedString(errorUser, ","):null);
        wxPushRecord.setTenantId(record.getTenantId());
        return wxPushRecordMapper.insertWithTenantId(wxPushRecord)==1?true:false;
	}
	
	@Override
	public boolean sendSysMessage(WxPush record) {
		MessageCenter msg = new MessageCenter();
		msg.setTitle(record.getMsgTitle());
		msg.setIsRead(0);
		msg.setProduceUser("sysPush");//发送人
		msg.setCreateTime(new Date());
		msg.setType(MessageCenter.NOTICE_MESSAGE);
		msg.setTenantId(record.getTenantId());
		List<UserInfo> userInfos = new ArrayList<UserInfo>();
		List<String> successUser = new ArrayList<String>();
		List<String> errorUser = new ArrayList<String>();
		//查询租户的系统语言
		Config config = configMapper.findByCode(ConfigKeys.CONFIG_LOCALE, record.getTenantId());
		
		switch (record.getMsgType()){
        case KefuMsgType.TEXT:
        	msg.setContent(record.getMsgContent());
            break ;
        case "report":
        	msg.setResourceId(record.getResourceId());
        	Resource r=resourceMapper.selectByIdWithTenantId(record.getResourceId(),record.getTenantId());
        	msg.setContent(record.getMsgContent()+"</br>"+"请点击:"+"<a href='"+record.getMsgUrl()+"'  target='_parent'>"+r.getName()+"</a> 查看");
            break;
        default:   //文本
        	msg.setContent(record.getMsgContent());
            break;
		}
        if (WxPush.TO_TYPE_USER.equals(record.getToType())) {// 给个人
            String users = record.getToContent();
            List<String> contents = Arrays.asList(users.split(","));
//            List<String> userIds = Lists.newArrayList();
            UserInfo ui = new UserInfo();
            for (String user : contents) {
            	ui.setId(user.split("@")[0]);
            	userInfos.add(ui);
            }
//            userInfos = userInfoMapper.selectBatchIds(userIds);
        } else if (WxPush.TO_TYPE_ROLE.equals(record.getToType())) { //角色
            String roles = record.getToContent();
            List<String> contents = Arrays.asList(roles.split(","));
            StringBuilder sb = new StringBuilder();
            for (String role : contents) {
                sb.append(role.split("@")[0]).append(",");
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setRoleIdIn(sb.substring(0,sb.length()-1));
            userInfo.setTenantId(record.getTenantId());//需要自己添加tenantId，如果使用自动添加tenantId的方法会报 no session的问题
            userInfos = userInfoMapper.findListWithTenantId(userInfo);
        }else if (WxPush.TO_TYPE_ORG.equals(record.getToType())) {  //组织-注意兼职组织问题。
            String orgs = record.getToContent();
            List<String> contents = Arrays.asList(orgs.split(","));
            StringBuilder sb = new StringBuilder();
            for (String org : contents) {
                sb.append(org.split("@")[0]).append(",");
            }
            UserInfo userInfo = new UserInfo();
            userInfo.setOrgIdIn(sb.substring(0,sb.length()-1));
            userInfo.setTenantId(record.getTenantId());
            List<UserInfo> AllUserInfos = userInfoMapper.findListWithTenantId(userInfo);
            //上面查询出来的含有重复的数据（兼职组织导致用户重复），需要根据id去重
            userInfos = (AllUserInfos != null && AllUserInfos.size() > 0) ? AllUserInfos.stream()
            		.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> 
            			new TreeSet<>(Comparator.comparing(UserInfo::getId))), ArrayList<UserInfo>::new)): null;
        }
        for (UserInfo userInfo : userInfos) {
        	msg.setReceiveUser(userInfo.getId());//接收人
        	WebSocketServer web=new WebSocketServer();//使用scoket通知在线用户。
        	Integer num = messageCenterMapper.insertWithTenantId(msg);//发送消息
        	web.setonMessage("4",userInfo.getId());
        	if(num!=null && num==1){
        		successUser.add(userInfo.getId());
        	}else{
        		errorUser.add(userInfo.getId());
        	}
		}
        //记录   任务发送记录
		WxPushRecord wxPushRecord = new WxPushRecord();
        wxPushRecord.setToUser(userInfos.stream().map(UserInfo::getId).collect(Collectors.joining(",")));
        wxPushRecord.setWxPushId(record.getId());
        wxPushRecord.setCreateTime(new Date());
        wxPushRecord.setResultCode(((userInfos!=null&&userInfos.size()>0)&&(successUser!=null&&successUser.size()>0))?0:1);
        wxPushRecord.setResultMsg(null);
        wxPushRecord.setToUserError((errorUser!=null&&errorUser.size()>0)?StringUtils.collectionToDelimitedString(errorUser, ","):null);
        wxPushRecord.setTenantId(record.getTenantId());
        return wxPushRecordMapper.insertWithTenantId(wxPushRecord) ==1 ? true : false;
	}

	@Override
	public BaseApi startTask(WxPush record) {
		boolean result = false;
		if(WxPush.TASK_TYPE_PUSH.equals(record.getTaskType())){//推送
			JobAndTrigger jobAndTrigger = new JobAndTrigger();
			Map<String,Object> jobDataMap = new HashMap<>();
			jobDataMap.put("wxPushId", record.getId());
	        jobAndTrigger.setDataMap(jobDataMap);
			jobAndTrigger.setTriggerName(record.getTriggerName());
            jobAndTrigger.setTriggerGroup(record.getTriggerGroup());
            jobAndTrigger.setJobName(record.getJobName());
            jobAndTrigger.setJobGroup(record.getJobGroup());
            jobAndTrigger.setCronExpression(record.getJobCron());
			//判断执行方式
			switch (record.getPushType()) {
			case WxPush.PUSH_TYPE_SYSMSG:
				jobAndTrigger.setJobClassName("com.xin.portal.system.job.SystemMessage");
                result = jobAndTriggerService.save(jobAndTrigger);
				break;
			case WxPush.PUSH_TYPE_EMAIL:
				jobAndTrigger.setJobClassName("com.xin.portal.system.job.SendEmail");
                result = jobAndTriggerService.save(jobAndTrigger);
				break;
			case WxPush.PUSH_TYPE_WECHAT:
				jobAndTrigger.setJobClassName("com.xin.weixin.cp.job.CpMsgJob");
                result = jobAndTriggerService.save(jobAndTrigger);
				break;
			case WxPush.PUSH_TYPE_LINE:
				jobAndTrigger.setJobClassName("com.xin.line.job.LineMsgJob");
                result = jobAndTriggerService.save(jobAndTrigger);
				break;
			default:
				break;
			}
		}else if(WxPush.TASK_TYPE_RUN.equals(record.getTaskType())){//
			//暂时不作实现	
			return BaseApi.error();
		}
		if(result){
			WxPush wp = new WxPush();
			wp.setId(record.getId());
			wp.setJobState(WxPush.JOB_STATE_RUN);
			wp.setUpdater(SessionUtil.getUserId());
			wp.setUpdateTime(new Date());
			wp.updateById();
		}
		return result?BaseApi.success():BaseApi.error();
	}

	@Override
	public BaseApi pauseTask(WxPush record) {
		String result = "fail";
		try {
			result = jobAndTriggerService.pauseJob(record.getJobName(), record.getJobGroup());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		if("success".equals(result)){
			WxPush wp = new WxPush();
			wp.setId(record.getId());
			wp.setJobState(WxPush.JOB_STATE_STOP);
			wp.setUpdater(SessionUtil.getUserId());
			wp.setUpdateTime(new Date());
			wp.updateById();
		}
		return "success".equals(result)? BaseApi.success():BaseApi.error();
	}

	@Override
	public BaseApi restoreTask(WxPush record) {
		// TODO Auto-generated method stub
		String result = "fail";
		try {
			result = jobAndTriggerService.resumeJob(record.getJobName(), record.getJobGroup());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		if("success".equals(result)){
			WxPush wp = new WxPush();
			wp.setId(record.getId());
			wp.setJobState(WxPush.JOB_STATE_RUN);
			wp.setUpdater(SessionUtil.getUserId());
			wp.setUpdateTime(new Date());
			wp.updateById();
		}
		return "success".equals(result)? BaseApi.success():BaseApi.error();
	}

	@Override
	public boolean runTaskOnce(WxPush record) {
		//直接执行send...  方法  // 微信和line 转发到对应包中的接口执行了所以下面不需要了。
		boolean result = false;
		if(WxPush.TASK_TYPE_PUSH.equals(record.getTaskType())){//推送
			//判断执行方式
			switch (record.getPushType()) {
			case WxPush.PUSH_TYPE_SYSMSG:
				result = sendSysMessage(record);
				break;
			case WxPush.PUSH_TYPE_EMAIL:
				result = sendEmail(record);
				break;
//			case WxPush.PUSH_TYPE_WECHAT:
//				result = send(record);
//				break;
			default:
				break;
			}
		}else if(WxPush.TASK_TYPE_RUN.equals(record.getTaskType())){//
			//暂时不作实现	
		}
		return result;
	}
}
