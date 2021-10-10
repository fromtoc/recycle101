package com.xin.portal.system.service;


import org.quartz.SchedulerException;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.model.JobAndTrigger;
import com.xin.portal.system.model.WxPush;

public interface JobAndTriggerService {
	public PageModel<JobAndTrigger> page(int pageNum, int pageSize);

	public boolean save(JobAndTrigger record);

    boolean saveOnce(JobAndTrigger jobAndTrigger);

	public boolean update(JobAndTrigger jobAndTrigger);

	String deleteJob(String jobName, String jobGroup) throws SchedulerException;

	String resumeJob(String jobName, String jobGroup) throws SchedulerException;

	void resumeAllJob() throws SchedulerException;

	String pauseJob(String jobName, String jobGroup) throws SchedulerException;

	void pauseAllJob() throws SchedulerException;

	String getJobState(String jobName, String jobGroup) throws SchedulerException;

	void deleteJob(String triggerName, String triggerGroupName, String jobName, String jobGroup)
			throws SchedulerException;

	public boolean save(WxPush record);
	
}
