package com.xin.portal.bi.mstr.http.Entity;

public class MstrAnswer{
	private String key;
	private String type;
	private boolean useDefault = false;
	public final String getKey() {
		return key;
	}
	public final void setKey(String key) {
		this.key = key;
	}
	public final String getType() {
		return type;
	}
	public final void setType(String type) {
		this.type = type;
	}
	public final boolean isUseDefault() {
		return useDefault;
	}
	public final void setUseDefault(boolean useDefault) {
		this.useDefault = useDefault;
	}
	public MstrAnswer(String key, String type, boolean useDefault) {
		super();
		this.key = key;
		this.type = type;
		this.useDefault = useDefault;
	}
}