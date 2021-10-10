package com.xin.portal.system.model;

import java.math.BigInteger;
import java.util.Map;

public class JobAndTrigger {
	
	private String jobName;
	private String jobGroup;
	private String jobClassName;
	private String jobDescription;
	private String triggerName;
	private String triggerGroup;
	private String triggerState;
	private String triggerDescription;
	private BigInteger repeatInterval;
	private BigInteger timesTriggered;
	private String cronExpression;
	private String timeZoneId;
	private BigInteger nextFireTime;
	private BigInteger prevFireTime;
	private BigInteger startTime;
	private BigInteger endTime;
	
	private Map<String,Object> dataMap;
	
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobGroup() {
		return jobGroup;
	}
	public void setJobGroup(String jobGroup) {
		this.jobGroup = jobGroup;
	}
	public String getJobClassName() {
		return jobClassName;
	}
	public void setJobClassName(String jobClassName) {
		this.jobClassName = jobClassName;
	}
	public String getTriggerName() {
		return triggerName;
	}
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
	public String getTriggerGroup() {
		return triggerGroup;
	}
	public void setTriggerGroup(String triggerGroup) {
		this.triggerGroup = triggerGroup;
	}
	public String getTriggerState() {
		return triggerState;
	}
	public void setTriggerState(String triggerState) {
		this.triggerState = triggerState;
	}
	public String getJobDescription() {
		return jobDescription;
	}
	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}
	public String getTriggerDescription() {
		return triggerDescription;
	}
	public void setTriggerDescription(String triggerDescription) {
		this.triggerDescription = triggerDescription;
	}
	public BigInteger getRepeatInterval() {
		return repeatInterval;
	}
	public void setRepeatInterval(BigInteger repeatInterval) {
		this.repeatInterval = repeatInterval;
	}
	public BigInteger getTimesTriggered() {
		return timesTriggered;
	}
	public void setTimesTriggered(BigInteger timesTriggered) {
		this.timesTriggered = timesTriggered;
	}
	public String getCronExpression() {
		return cronExpression;
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getTimeZoneId() {
		return timeZoneId;
	}
	public void setTimeZoneId(String timeZoneId) {
		this.timeZoneId = timeZoneId;
	}
	public BigInteger getNextFireTime() {
		return nextFireTime;
	}
	public void setNextFireTime(BigInteger nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	public BigInteger getPrevFireTime() {
		return prevFireTime;
	}
	public void setPrevFireTime(BigInteger prevFireTime) {
		this.prevFireTime = prevFireTime;
	}
	public BigInteger getStartTime() {
		return startTime;
	}
	public void setStartTime(BigInteger startTime) {
		this.startTime = startTime;
	}
	public BigInteger getEndTime() {
		return endTime;
	}
	public void setEndTime(BigInteger endTime) {
		this.endTime = endTime;
	}
	public Map<String, Object> getDataMap() {
		return dataMap;
	}
	public void setDataMap(Map<String, Object> dataMap) {
		this.dataMap = dataMap;
	}
	
	
}
