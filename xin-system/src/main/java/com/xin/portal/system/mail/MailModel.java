package com.xin.portal.system.mail;

import java.util.List;

import com.xin.portal.system.bean.AttrModel;

public class MailModel {
	
	private String fromAddress;
	private String fromName;
	private String toAddress;
	private String subject;
	private String content;
	private List<String> imgs;
	private List<AttrModel> attrs;
	
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public String getFromName() {
		return fromName;
	}
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	public String getToAddress() {
		return toAddress;
	}
	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<String> getImgs() {
		return imgs;
	}
	public void setImgs(List<String> imgs) {
		this.imgs = imgs;
	}
	public List<AttrModel> getAttrs() {
		return attrs;
	}
	public void setAttrs(List<AttrModel> attrs) {
		this.attrs = attrs;
	}

}
