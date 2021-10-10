package com.xin.portal.system.dto;

/**
 * @ClassPath: com.xin.portal.system.dto.PageDto 
 * @Description: TODO
 * @author zhoujun
 * @date 2017-7-14 下午5:02:12
 */
public class PageDto {
	
	private int pageNumber;
	private int pageSize;
	private String order;
	
	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber==0 ? 1 : pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize==0 ? 10 : pageSize;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	
	
	
	

}
