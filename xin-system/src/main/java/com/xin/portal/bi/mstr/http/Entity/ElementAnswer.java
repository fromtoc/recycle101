package com.xin.portal.bi.mstr.http.Entity;

import java.util.List;

public class ElementAnswer extends MstrAnswer{
	private List<ElementAnswers> answers;
	public ElementAnswer(String key, String type, boolean useDefault) {
		super(key, type, useDefault);
	}
	public ElementAnswer(String key, boolean useDefault) {
		super(key, "ELEMENTS", useDefault);
	}
	public final List<ElementAnswers> getAnswers() {
		return answers;
	}
	public final void setAnswers(List<ElementAnswers> answers) {
		this.answers = answers;
	}
	public ElementAnswer(String key, String type, boolean useDefault, List<ElementAnswers> answers) {
		super(key, type, useDefault);
		this.answers = answers;
	}
	public ElementAnswer(String key, boolean useDefault, List<ElementAnswers> answers) {
		super(key, "ELEMENTS", useDefault);
		this.answers = answers;
	}
}
