package com.xin.portal.system.bean;

import java.io.Serializable;

public class CollectTree implements Serializable {
		
	//tree/collect
	private String id;//收藏id
	private String pid;//收藏父ID
	private String name;//收藏名称
	private String userId;
	//tree
	private boolean checked;//节点的 checkBox / radio 的 勾选状态
	private boolean open=true;//节点打开
	private boolean chkDisabled ;//设置节点的 checkbox / radio 是否禁用
	private String iconSkin;//通过CSS设置图标
	private boolean drag;//是否开启拖拽
	private boolean dropRoot=false;//拖拽成为根节点
	private boolean dropInner;//设置为false后使该节点不能成为父节点
	private boolean noEditBtn;//不显示编辑按钮
	private boolean noRemoveBtn;//不显示删除按钮
	//collect
	private String resourceId;//文件的id
	private String collectType;//收藏的类型（报表report、课件course、案例case、文件夹folder）
	private Integer sort;//排序
	
	private Integer folderLevel;//文件夹层级
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public boolean isChkDisabled() {
		return chkDisabled;
	}
	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}
	public boolean isDrag() {
		return drag;
	}
	public void setDrag(boolean drag) {
		this.drag = drag;
	}
	public boolean isDropRoot() {
		return dropRoot;
	}
	public void setDropRoot(boolean dropRoot) {
		this.dropRoot = dropRoot;
	}
	public boolean isDropInner() {
		return dropInner;
	}
	public void setDropInner(boolean dropInner) {
		this.dropInner = dropInner;
	}
	public boolean isNoEditBtn() {
		return noEditBtn;
	}
	public void setNoEditBtn(boolean noEditBtn) {
		this.noEditBtn = noEditBtn;
	}
	public boolean isNoRemoveBtn() {
		return noRemoveBtn;
	}
	public void setNoRemoveBtn(boolean noRemoveBtn) {
		this.noRemoveBtn = noRemoveBtn;
	}
	public String getIconSkin() {
		return iconSkin;
	}
	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getCollectType() {
		return collectType;
	}
	public void setCollectType(String collectType) {
		this.collectType = collectType;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Integer getFolderLevel() {
		return folderLevel;
	}
	public void setFolderLevel(Integer folderLevel) {
		this.folderLevel = folderLevel;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
