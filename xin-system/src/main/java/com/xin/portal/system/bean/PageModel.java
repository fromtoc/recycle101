package com.xin.portal.system.bean;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;

/**
 * 
 * @author dod123456
 *
 * @param <T>
 */
public class PageModel<T> {
	
	private int pageNumber = 1;
	private int pageSize = 10;
	private int total=0;
	private List<T> rows;
	
	public PageModel(Page<T> page){
		this.pageNumber = page.getCurrent();
		this.pageSize = page.getSize();
		this.rows = page.getRecords();
		this.total = (int)page.getTotal();
	}
	

	public int getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<T> getRows() {
		return rows;
	}
	public void setRows(List<T> rows) {
		this.rows = rows;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
