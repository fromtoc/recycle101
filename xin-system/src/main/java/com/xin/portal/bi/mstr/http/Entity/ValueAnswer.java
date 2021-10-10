package com.xin.portal.bi.mstr.http.Entity;

public class ValueAnswer extends MstrAnswer{
	private String answers;
	public ValueAnswer(String key, String type, boolean useDefault) {
		super(key, type, useDefault);
	}
	public ValueAnswer(String key, boolean useDefault) {
		super(key, "VALUE", useDefault);
	}
	public final String getAnswers() {
		return answers;
	}
	public final void setAnswers(String answers) {
		this.answers = answers;
	}
	public ValueAnswer(String key, String type, boolean useDefault, String answers) {
		super(key, type, useDefault);
		this.answers = answers;
	}
	public ValueAnswer(String key, boolean useDefault, String answers) {
		super(key, "VALUE", useDefault);
		this.answers = answers;
	}
}
