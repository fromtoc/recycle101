package com.xin.portal.system.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.job.BaseJob;
import com.xin.portal.system.mapper.JobAndTriggerMapper;
import com.xin.portal.system.model.JobAndTrigger;
import com.xin.portal.system.model.WxPush;
import com.xin.portal.system.service.JobAndTriggerService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;


@Service
public class JobAndTriggerImpl implements JobAndTriggerService {

	private static final Logger logger = LoggerFactory.getLogger(JobAndTriggerImpl.class);
	
	@Autowired
	private Scheduler scheduler;

	@Autowired
	private JobAndTriggerMapper jobAndTriggerMapper;
	
	public PageModel<JobAndTrigger> page(int pageNum, int pageSize) {
		Page<JobAndTrigger> page = new Page<JobAndTrigger>(pageNum,pageSize);
		List<JobAndTrigger> list = jobAndTriggerMapper.getJobAndTriggerDetails(page);
		page.setRecords(list);
		return new PageModel<>(page);
	}

	@Override
	public boolean save(JobAndTrigger jobAndTrigger) {
		try {
			// 启动调度器
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobAndTrigger.getJobClassName()).getClass())
					.withIdentity(jobAndTrigger.getJobName(), jobAndTrigger.getJobGroup())
					.withDescription(jobAndTrigger.getJobDescription()).build();
			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobAndTrigger.getCronExpression());


			// 按新的cronExpression表达式构建一个新的trigger
			TriggerBuilder<CronTrigger> triggerBuilder = newTrigger()
					.withIdentity(jobAndTrigger.getTriggerName(), jobAndTrigger.getJobGroup())
					.withSchedule(scheduleBuilder)
					.withDescription(jobAndTrigger.getTriggerDescription());

			if (jobAndTrigger.getDataMap()!=null && jobAndTrigger.getDataMap().size() > 0) {
				JobDataMap jobDataMap = new JobDataMap();
				for (Map.Entry<String, Object> dataKey : jobAndTrigger.getDataMap().entrySet()) {
					if (!StringUtils.isEmpty(dataKey.getKey())) {
						jobDataMap.put(dataKey.getKey(), dataKey.getValue());
					}
				}
				triggerBuilder.usingJobData(jobDataMap);
			}

			CronTrigger trigger = triggerBuilder.build();
			logger.info("定时调度 save-JobAndTrigger, {} 的时区是 {} ",jobAndTrigger.getJobName(), trigger.getTimeZone());
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public boolean save(WxPush record) {
		JobAndTrigger jobAndTrigger = buildJobAndTriggerModel(record);
		try {
			// 启动调度器
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}

			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobAndTrigger.getJobClassName()).getClass())
					.withIdentity(jobAndTrigger.getJobName(), jobAndTrigger.getJobGroup())
					.withDescription(jobAndTrigger.getJobDescription()).build();
			// 表达式调度构建器(即任务执行的时间)
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobAndTrigger.getCronExpression());


			// 按新的cronExpression表达式构建一个新的trigger
			TriggerBuilder<CronTrigger> triggerBuilder = newTrigger()
					.withIdentity(jobAndTrigger.getTriggerName(), jobAndTrigger.getJobGroup())
					.withSchedule(scheduleBuilder)
					.withDescription(jobAndTrigger.getTriggerDescription());

			if (jobAndTrigger.getDataMap()!=null && jobAndTrigger.getDataMap().size() > 0) {
				JobDataMap jobDataMap = new JobDataMap();
				for (Map.Entry<String, Object> dataKey : jobAndTrigger.getDataMap().entrySet()) {
					if (!StringUtils.isEmpty(dataKey.getKey())) {
						jobDataMap.put(dataKey.getKey(), dataKey.getValue());
					}
				}
				triggerBuilder.usingJobData(jobDataMap);
			}

			CronTrigger trigger = triggerBuilder.build();
			logger.info("定时调度  save-WxPush, {} 的时区是 {} ",jobAndTrigger.getJobName(), trigger.getTimeZone());
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	
	private JobAndTrigger buildJobAndTriggerModel(WxPush record) {
		JobAndTrigger jobAndTrigger = new JobAndTrigger();
		if(WxPush.TASK_TYPE_PUSH.equals(record.getTaskType())){//推送
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
				break;
			case WxPush.PUSH_TYPE_EMAIL:
				jobAndTrigger.setJobClassName("com.xin.portal.system.job.SendEmail");
				break;
			case WxPush.PUSH_TYPE_WECHAT:
				jobAndTrigger.setJobClassName("com.xin.weixin.cp.job.CpMsgJob");
				break;
			case WxPush.PUSH_TYPE_LINE:
				jobAndTrigger.setJobClassName("com.xin.line.job.LineMsgJob");
				break;
			default:
				break;
			}
		}else if(WxPush.TASK_TYPE_RUN.equals(record.getTaskType())){//
			//暂时不作实现	
			return null;
		}
		return jobAndTrigger;
	}

	@Override
	public boolean saveOnce(JobAndTrigger jobAndTrigger){
		try {
			// 启动
			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
			//如Job名字、描述、关联监听器等信息，JobDetail承担了这一角色,JobDetail要求指定Job的实现类，以及任务在Scheduler中的组名和Job名称
			// 构建job信息
			JobDetail jobDetail = JobBuilder.newJob(getClass(jobAndTrigger.getJobClassName()).getClass())
					.withIdentity(jobAndTrigger.getJobName(), jobAndTrigger.getJobGroup())
					.withDescription(jobAndTrigger.getJobDescription()).build();

			// 触发器
			TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
			// 触发器名,触发器组
			triggerBuilder.withIdentity(jobAndTrigger.getTriggerName(), jobAndTrigger.getTriggerGroup());

			if (jobAndTrigger.getDataMap()!=null && jobAndTrigger.getDataMap().size() > 0) {
				JobDataMap jobDataMap = new JobDataMap();
				for (Map.Entry<String, Object> dataKey : jobAndTrigger.getDataMap().entrySet()) {
					if (!StringUtils.isEmpty(dataKey.getKey())) {
						jobDataMap.put(dataKey.getKey(), dataKey.getValue());
					}
				}
				triggerBuilder.usingJobData(jobDataMap);
			}

			triggerBuilder.startAt(new Date(jobAndTrigger.getStartTime().longValue()));
			// 触发器时间设定
			triggerBuilder.withSchedule(simpleSchedule().withIntervalInMinutes(10).withRepeatCount(0));
			// 创建Trigger对象
			SimpleTrigger trigger = (SimpleTrigger) triggerBuilder.build();
			
			// 调度容器设置JobDetail和Trigger
			scheduler.scheduleJob(jobDetail, trigger);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/**修改任务*/
	@Override
	public boolean update(JobAndTrigger jobAndTrigger) {
		try {
			TriggerKey triggerKey = TriggerKey.triggerKey(jobAndTrigger.getTriggerName(), jobAndTrigger.getTriggerGroup());

			Trigger  trigger = scheduler.getTrigger(triggerKey);
			// 触发器
			TriggerBuilder triggerBuilder = trigger.getTriggerBuilder();

			if (jobAndTrigger.getDataMap()!=null && jobAndTrigger.getDataMap().size() > 0) {
				JobDataMap jobDataMap = new JobDataMap();
				for (Map.Entry<String, Object> dataKey : jobAndTrigger.getDataMap().entrySet()) {
					if (!StringUtils.isEmpty(dataKey.getKey())) {
						jobDataMap.put(dataKey.getKey(), dataKey.getValue());
					}
				}
				triggerBuilder.usingJobData(jobDataMap);
			}

			if (jobAndTrigger.getStartTime()!=null) {
				trigger = (SimpleTrigger) triggerBuilder.startAt(new Date(jobAndTrigger.getStartTime().longValue()))
						.withSchedule(simpleSchedule().withIntervalInMinutes(10).withRepeatCount(0)).build();
			} else {
				trigger = (CronTrigger) trigger;
				// 表达式调度构建器(即任务执行的时间)
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(jobAndTrigger.getCronExpression());
				trigger = triggerBuilder.withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			}
			// 按新的cronExpression表达式重新构建trigger

			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return true;
	}


	public static BaseJob getClass(String classname) throws Exception {
		Class<?> class1 = Class.forName(classname);
		return (BaseJob) class1.newInstance();
	}
	
	/**获取job的状态
	 * NONE:0 NORMAL:1 PAUSED:2 COMPLETE:3 ERROR:4 BLOCKED:5
	 * */
	@Override
	public String getJobState(String TriggerName, String TriggerGroup) throws SchedulerException {             
         TriggerKey triggerKey = new TriggerKey(TriggerName, TriggerGroup);    
         return scheduler.getTriggerState(triggerKey).name();
    }
	
	/**暂停所有任务*/
	@Override
    public void pauseAllJob() throws SchedulerException {            
        scheduler.pauseAll();
    }
	
	/**暂停任务*/
	@Override
    public String pauseJob(String jobName, String jobGroup) throws SchedulerException {            
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
             return "fail";
        }else {
             scheduler.pauseJob(jobKey);
             return "success";
        }
                                     
    }
	
	/**恢复所有任务*/
	@Override
    public void resumeAllJob() throws SchedulerException {            
        scheduler.resumeAll();
    }
	
	/**恢复某个任务*/
	@Override
    public String resumeJob(String jobName, String jobGroup) throws SchedulerException {
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null) {
            return "fail";
        }else {               
            scheduler.resumeJob(jobKey);
            return "success";
        }
    }
	
	/**删除某个任务*/
	@Override
    public String  deleteJob(String jobName, String jobGroup) throws SchedulerException {            
        JobKey jobKey = new JobKey(jobName, jobGroup);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null ) {
             return "jobDetail is null";
        }else if(!scheduler.checkExists(jobKey)) {
            return "jobKey is not exists";
        }else {
             scheduler.deleteJob(jobKey);
             return "success";
        }  
    }
	
	/**删除某个任务*/
	@Override
	public void deleteJob(String triggerName,String triggerGroupName,String jobName,String jobGroup) throws SchedulerException{
		scheduler.pauseTrigger(TriggerKey.triggerKey(triggerName, triggerGroupName));// 停止触发器  
		scheduler.unscheduleJob(TriggerKey.triggerKey(triggerName, triggerGroupName));// 移除触发器  
		scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));// 删除任务  
	}

}