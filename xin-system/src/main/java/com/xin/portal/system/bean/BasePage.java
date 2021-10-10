package com.xin.portal.system.bean;

import com.baomidou.mybatisplus.annotations.TableField;
/**
 * @ClassPath: com.xin.portal.system.bean.BasePage 
 * @Description: TODO
 * @author zhoujun
 * @date 2018年1月5日 上午10:35:40
 */
public class BasePage {
	@TableField(exist = false)
	private int pageNumber;
	@TableField(exist = false)
	private int pageSize;

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
	
	

}
