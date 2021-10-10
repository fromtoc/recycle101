package com.xin.portal.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.xin.portal.system.bean.BaseApi;
import com.xin.portal.system.bean.BaseController;
import com.xin.portal.system.bean.PageModel;
import com.xin.portal.system.config.sysLog.LogType;
import com.xin.portal.system.config.sysLog.SystemLog;
import com.xin.portal.system.model.Dict;
import com.xin.portal.system.model.JobAndTrigger;
import com.xin.portal.system.service.DictService;
import com.xin.portal.system.service.JobAndTriggerService;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/job")
@ConditionalOnProperty(name = "system.quartz.enabled")
public class JobController extends BaseController {

	private final String DIR = "job/";

	@Autowired
	private JobAndTriggerService jobAndTriggerService;
	@Autowired
	private DictService dictService;

	// 加入Qulifier注解，通过名称注入bean
	@Autowired
	private Scheduler scheduler;

	private static Logger log = LoggerFactory.getLogger(JobController.class);

	@RequestMapping("/index")
	public String index(Model model) {
		List<Dict> jobTypes = dictService.listItem(new Dict("job_type"));
		List<Dict> jobCrons = dictService.listItem(new Dict("job_cron"));
		model.addAttribute("jobTypes", JSONArray.toJSONString(jobTypes));
		model.addAttribute("jobCrons", JSONArray.toJSONString(jobCrons));
		return DIR + "index";
	}

	@PostMapping(value = "/addjob")
	@ResponseBody
	public BaseApi addjob(JobAndTrigger jobAndTrigger) {
		return jobAndTriggerService.save(jobAndTrigger) ? BaseApi.success() : BaseApi.error() ;
	}


	@PostMapping(value = "/pausejob")
	@ResponseBody
	public BaseApi pausejob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
		jobPause(jobClassName, jobGroupName);
		return BaseApi.success();
	}

	public void jobPause(String jobClassName, String jobGroupName) throws Exception {
		scheduler.pauseJob(JobKey.jobKey(jobClassName, jobGroupName));
	}

	@PostMapping(value = "/resumejob")
	@ResponseBody
	public BaseApi resumejob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
		jobresume(jobClassName, jobGroupName);
		return BaseApi.success();
	}

	public void jobresume(String jobClassName, String jobGroupName) throws Exception {
		scheduler.resumeJob(JobKey.jobKey(jobClassName, jobGroupName));
	}
	
	@PostMapping(value = "/reschedulejob")
	@ResponseBody
	public BaseApi rescheduleJob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName,
			@RequestParam(value = "cronExpression") String cronExpression) throws Exception {
		JobAndTrigger jobAndTrigger = new JobAndTrigger();
		jobAndTrigger.setTriggerName(jobClassName);
		jobAndTrigger.setTriggerGroup(jobGroupName);
		jobAndTrigger.setCronExpression(cronExpression);
		jobAndTriggerService.update(jobAndTrigger);
		return BaseApi.success();
	}


	@SystemLog(module="调度任务",operation="删除",type=LogType.delete)
	@PostMapping(value = "/deletejob")
	@ResponseBody
	public BaseApi deletejob(@RequestParam(value = "jobClassName") String jobClassName,
			@RequestParam(value = "jobGroupName") String jobGroupName) throws Exception {
		jobdelete(jobClassName, jobGroupName);
		return BaseApi.success();
	}

	public void jobdelete(String jobClassName, String jobGroupName) throws Exception {
		scheduler.pauseTrigger(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.unscheduleJob(TriggerKey.triggerKey(jobClassName, jobGroupName));
		scheduler.deleteJob(JobKey.jobKey(jobClassName, jobGroupName));
	}
	
	@SystemLog(module="调度任务",operation="列表查询",type=LogType.query)
	@GetMapping(value = "/page")
	@ResponseBody
	public PageModel<JobAndTrigger> queryjob(@RequestParam(value = "pageNum") Integer pageNum,
			@RequestParam(value = "pageSize") Integer pageSize) {
		return jobAndTriggerService.page(pageNum, pageSize);
	}



}
