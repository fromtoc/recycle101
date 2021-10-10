package com.xin.portal.bi.mstr.http.Entity;

public class ElementAnswers{	
	/*private String id;*/
	private String name;
	/*public final String getId() {
		return id;
	}
	public final void setId(String id) {
		this.id = id;
	}*/
	public final String getName() {
		return name;
	}
	public final void setName(String name) {
		this.name = name;
	}
	/*public ElementAnswers(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}*/
	public ElementAnswers(String name) {
		super();
		this.name = name;
	}
	/*public ElementAnswers(String id) {
		super();
		this.id = id;
	}*/
	public ElementAnswers() {
		super();
	}
}
