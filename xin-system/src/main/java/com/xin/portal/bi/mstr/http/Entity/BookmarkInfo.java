package com.xin.portal.bi.mstr.http.Entity;

public class BookmarkInfo{
	private boolean resetManipulation;
	public BookmarkInfo(boolean resetManipulation) {
		super();
		this.resetManipulation = resetManipulation;
	}
	public boolean isResetManipulation() {
		return resetManipulation;
	}
	public void setResetManipulation(boolean resetManipulation) {
		this.resetManipulation = resetManipulation;
	}
}
