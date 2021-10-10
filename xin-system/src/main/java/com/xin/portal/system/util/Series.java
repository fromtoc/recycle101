package com.xin.portal.system.util;

import java.util.List;

public class Series<T> {
	public String name;
	public String type;
	public String stack;
	public String label;
	public String areaStyle;
	public List<T> data;
	
	public Series(String name, String type, String stack, String label, String areaStyle, List<T> data) {
		super();
		this.name = name;
		this.type = type;
		this.stack = stack;
		this.label = label;
		this.areaStyle = areaStyle;
		this.data = data;
	}
	
	public Series(String name, List<T> data) {
		super();
		this.name = name;
		this.type = "bar";
		this.stack = "total";
		this.label = "{" + "normal: {" + "show: true," + "position: 'insideRight'}}";
		this.data = data;
	}
	
	public Series(String name, String type, List<T> data) {
		super();
		this.name = name;
		this.type = type;
		this.data = data;
	}

}
