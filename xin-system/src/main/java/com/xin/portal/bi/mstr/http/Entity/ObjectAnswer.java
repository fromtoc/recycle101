package com.xin.portal.bi.mstr.http.Entity;

import java.util.List;

public class ObjectAnswer extends MstrAnswer{
	private List<ObjectAnswers> answers;
	public ObjectAnswer(String key, String type, boolean useDefault) {
		super(key, type, useDefault);
	}
	public ObjectAnswer(String key, boolean useDefault) {
		super(key, "OBJECTS", useDefault);
	}
	public final List<ObjectAnswers> getAnswers() {
		return answers;
	}
	public final void setAnswers(List<ObjectAnswers> answers) {
		this.answers = answers;
	}
	public ObjectAnswer(String key, String type, boolean useDefault, List<ObjectAnswers> answers) {
		super(key, type, useDefault);
		this.answers = answers;
	}
	public ObjectAnswer(String key, boolean useDefault, List<ObjectAnswers> answers) {
		super(key, "OBJECTS", useDefault);
		this.answers = answers;
	}
}
