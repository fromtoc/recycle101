package com.xin.portal.system.model;

import java.util.Date;

public class ServerComputer {
	
	private String name;
	private String ip;
	private String domain;
	private String hostName;
	private String osName;
	private String osArch;
	private String osVersion;
	private Long memoryTotal;
	private Long memoryUsed;
	private Long memoryFree;
	private String memoryUsedRate;
	private int cpuNum;
	private double cpuUsedRate;
	private double cpuSysRate;
	private double cpuIdleRate;
	private int cpuTotal;
	private String javaVersion;
	private String jvmName;
	private Long jvmTotal;
	private Long jvmFree;
	private Long jvmUsed;
	private String jvmUsedRate;
	private String jvmRunTime;
	private Date jvmStartTime;
	private long currentTime;

	public ServerComputer(){
		this.currentTime = System.currentTimeMillis();
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public String getOsArch() {
		return osArch;
	}
	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}
	public String getOsVersion() {
		return osVersion;
	}
	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}
	public Long getMemoryTotal() {
		return memoryTotal;
	}
	public void setMemoryTotal(Long memoryTotal) {
		this.memoryTotal = memoryTotal;
	}
	public Long getMemoryUsed() {
		return memoryUsed;
	}
	public void setMemoryUsed(Long memoryUsed) {
		this.memoryUsed = memoryUsed;
	}
	public Long getMemoryFree() {
		return memoryFree;
	}
	public void setMemoryFree(Long memoryFree) {
		this.memoryFree = memoryFree;
	}
	public String getMemoryUsedRate() {
		return memoryUsedRate;
	}
	public void setMemoryUsedRate(String memoryUsedRate) {
		this.memoryUsedRate = memoryUsedRate;
	}
	public int getCpuNum() {
		return cpuNum;
	}
	public void setCpuNum(int cpuNum) {
		this.cpuNum = cpuNum;
	}
	public double getCpuUsedRate() {
		return cpuUsedRate;
	}
	public void setCpuUsedRate(double cpuUsedRate) {
		this.cpuUsedRate = cpuUsedRate;
	}
	public double getCpuSysRate() {
		return cpuSysRate;
	}
	public void setCpuSysRate(double cpuSysRate) {
		this.cpuSysRate = cpuSysRate;
	}
	public double getCpuIdleRate() {
		return cpuIdleRate;
	}
	public void setCpuIdleRate(double cpuIdleRate) {
		this.cpuIdleRate = cpuIdleRate;
	}
	public String getJavaVersion() {
		return javaVersion;
	}
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
	public String getJvmName() {
		return jvmName;
	}
	public void setJvmName(String jvmName) {
		this.jvmName = jvmName;
	}
	public Long getJvmTotal() {
		return jvmTotal;
	}
	public void setJvmTotal(Long jvmTotal) {
		this.jvmTotal = jvmTotal;
	}
	public Long getJvmFree() {
		return jvmFree;
	}
	public void setJvmFree(Long jvmFree) {
		this.jvmFree = jvmFree;
	}
	public Long getJvmUsed() {
		return jvmUsed;
	}
	public void setJvmUsed(Long jvmUsed) {
		this.jvmUsed = jvmUsed;
	}
	public String getJvmUsedRate() {
		return jvmUsedRate;
	}
	public void setJvmUsedRate(String jvmUsedRate) {
		this.jvmUsedRate = jvmUsedRate;
	}
	public String getJvmRunTime() {
		return jvmRunTime;
	}
	public void setJvmRunTime(String jvmRunTime) {
		this.jvmRunTime = jvmRunTime;
	}
	public Date getJvmStartTime() {
		return jvmStartTime;
	}
	public void setJvmStartTime(Date jvmStartTime) {
		this.jvmStartTime = jvmStartTime;
	}
	public int getCpuTotal() {
		return cpuTotal;
	}
	public void setCpuTotal(int cpuTotal) {
		this.cpuTotal = cpuTotal;
	}

	public long getCurrentTime() {
		return currentTime;
	}

}
