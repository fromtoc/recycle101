/**   
* @Title: CpMsgJob.java 
* @Package com.xin.weixin.cp 
* @Description: TODO
* @author zhoujun 
* @date 2019年1月9日 下午5:03:45 
* @version V1.0   
*/
package com.xin.portal.system.job;

import com.xin.portal.system.job.BaseJob;
import com.xin.portal.system.model.WxPush;
import com.xin.portal.system.service.WxPushService;

import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @ClassPath: com.xin.weixin.cp.job.CpMsgJob 
 * @Description: TODO
 * @author zhoujun
 * @date 2019年1月10日 下午4:09:27
 */
public class SendEmail implements BaseJob {  
  
    private static Logger logger = LoggerFactory.getLogger(SendEmail.class);
	@Autowired
	private WxPushService wxPushService;

    public void execute(JobExecutionContext context)  {
    	JobDataMap dataMap = context.getTrigger().getJobDataMap();
    	logger.info("job params : {}",dataMap.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " - " + entry.getValue())
                .collect(Collectors.joining(", ")));

    	String wxPushId = dataMap.getString("wxPushId");
		WxPush record = wxPushService.selectById(wxPushId);
		
		Date lastRunTime = context.getPreviousFireTime();// 上次执行时间
    	Date currRunTime = context.getFireTime();// 本次执行时间
    	Date nextRunTime = context.getNextFireTime();// 下一次执行时间

		if (record!=null) {
			boolean result = wxPushService.sendEmail(record);
			WxPush wp = new WxPush();
			wp.setId(wxPushId);
			wp.setJobLastRunTime(lastRunTime!=null?lastRunTime:null);
			wp.setJobCurrRunTime(currRunTime!=null?currRunTime:null);
			wp.setJobNextRunTime(nextRunTime!=null?nextRunTime:null);
			if(nextRunTime==null){
				wp.setJobState(WxPush.JOB_STATE_OVER);
			}
			if(nextRunTime!=null){
				wp.setJobState(WxPush.JOB_STATE_RUN);
			}
			wp.updateById();
			logger.info(" Job run Result[{}] - {}: ",new Date(),record.getMsgTitle()+"-->success:"+result);
		}


    }
}
