package com.xin.portal.system.job;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;  
/**
 * @ClassPath: com.xin.portal.system.job.DemoJob 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年2月6日 下午4:37:28
 */
public class DemoJob implements BaseJob {  
  
    private static Logger _log = LoggerFactory.getLogger(DemoJob.class);  
     
    public void execute(JobExecutionContext context)  
        throws JobExecutionException {  
        _log.info("Demo Job执行时间: " + new Date());  
    }  
}  
