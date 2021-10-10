package com.xin.portal.bi.mstr.http.Entity;

public class FolderCreationBody{
	//private List<Filters> filters;//存放过滤条件，
	private BookmarkInfo bookmarkInfo;
	private boolean persistViewState;
	public FolderCreationBody(BookmarkInfo bookmarkInfo, boolean persistViewState) {
		super();
		this.bookmarkInfo = bookmarkInfo;
		this.persistViewState = persistViewState;
	}
	public BookmarkInfo getBookmarkInfo() {
		return bookmarkInfo;
	}
	public void setBookmarkInfo(BookmarkInfo bookmarkInfo) {
		this.bookmarkInfo = bookmarkInfo;
	}
	public boolean isPersistViewState() {
		return persistViewState;
	}
	public void setPersistViewState(boolean persistViewState) {
		this.persistViewState = persistViewState;
	}
}
