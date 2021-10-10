package com.xin.portal.system.bean;

public class SystemModel {
	
	private String javaVersion;//Java的运行环境版本
	private String javaVendor;//Java的运行环境供应商
	private String javaVendorUrl;//Java供应商的URL
	private String javaHome;//Java的安装路径
	private String jvmVersion;//Java的虚拟机规范版本
	private String jvmVendor;//Java的虚拟机规范供应商
	private String jvmName;//Java的虚拟机规范名称
	private String osName;//操作系统的名称
	private String osArch;//操作系统的构架
	private String osVersion;//操作系统的版本
	private String userName;//用户的账户名称
	
	public SystemModel(){
		this.javaVersion = System.getProperty("java.version");//Java的运行环境版本
		this.javaVendor = System.getProperty("java.vendor");//Java的运行环境供应商
		this.javaVendorUrl = System.getProperty("java.vendor.url");//Java供应商的URL
		this.javaHome = System.getProperty("java.home");//Java的安装路径
		this.jvmVersion = System.getProperty("java.vm.specification.version");//Java的虚拟机规范版本
		this.jvmVendor = System.getProperty("java.vm.specification.vendor");//Java的虚拟机规范供应商
		this.jvmName = System.getProperty("java.vm.specification.name");//Java的虚拟机规范名称
		this.osName = System.getProperty("os.name");//操作系统的名称
		this.osArch = System.getProperty("os.arch");//操作系统的构架
		this.osVersion = System.getProperty("os.version");//操作系统的版本
		this.userName = System.getProperty("user.name");//用户的账户名称
	}
	
	public String getJavaVersion() {
		return javaVersion;
	}
	public void setJavaVersion(String javaVersion) {
		this.javaVersion = javaVersion;
	}
	public String getJavaVendor() {
		return javaVendor;
	}
	public void setJavaVendor(String javaVendor) {
		this.javaVendor = javaVendor;
	}
	public String getJavaVendorUrl() {
		return javaVendorUrl;
	}
	public void setJavaVendorUrl(String javaVendorUrl) {
		this.javaVendorUrl = javaVendorUrl;
	}
	public String getJavaHome() {
		return javaHome;
	}
	public void setJavaHome(String javaHome) {
		this.javaHome = javaHome;
	}
	public String getJvmVersion() {
		return jvmVersion;
	}
	public void setJvmVersion(String jvmVersion) {
		this.jvmVersion = jvmVersion;
	}
	public String getJvmVendor() {
		return jvmVendor;
	}
	public void setJvmVendor(String jvmVendor) {
		this.jvmVendor = jvmVendor;
	}
	public String getJvmName() {
		return jvmName;
	}
	public void setJvmName(String jvmName) {
		this.jvmName = jvmName;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "SystemModel [javaVersion=" + javaVersion + ", javaVendor=" + javaVendor + ", javaVendorUrl="
				+ javaVendorUrl + ", javaHome=" + javaHome + ", jvmVersion=" + jvmVersion + ", jvmVendor=" + jvmVendor
				+ ", jvmName=" + jvmName + ", osName=" + osName + ", osArch=" + osArch + ", osVersion=" + osVersion
				+ ", userName=" + userName + "]";
	}
	
	
	
	
	

}
