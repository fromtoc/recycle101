package com.xin.portal.system.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

/**
 * @ClassPath: com.xin.portal.system.bean.TreeNode
 * @Description:
 * @author zhoujun
 * @date 2017-11-29 下午4:29:56
 */
public class TreeNode {
	
	// 用作节点的唯一标识
	public String id;
	// 表示当前的节点名称
	public String name;
	public String title;
	// 和id互相对应表示当前节点是哪一个节点的子节点(在简单JSON格式中使用)
	public String pid;
	public String parentId;
	// 里面的数据表示当前数据节点的子节点(在标准JSON格式中使用)
	public List<TreeNode> children;
	// 表示当前节点的图标
	public String icon;
	// 节点在收缩时的图标
	public String iconClose;
	// 节点在展开时的图标
	public String iconOpen;
	// 表示当前节点前面的radio/checkbox是否显示 true表示不显示
	public boolean nocheck;
	private Integer state;
	private boolean isParent;
	private boolean open;
	private int level;
	private String rootId;
	private Boolean leaf;
	private Map<String, String> attribute = new HashMap<String, String>();
	
	public TreeNode(String id, String name, String pid) {
		super();
		this.id = id;
		this.name = name;
		this.pid = pid;
		this.parentId = pid;
		this.title = name;
	}
	
	
	public TreeNode() {
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getParentId() {
		return parentId;
	}


	public void setParentId(String parentId) {
		this.parentId = parentId;
	}


	public String getRootId() {
		return rootId;
	}


	public void setRootId(String rootId) {
		this.rootId = rootId;
	}


	public int getLevel() {
		return level;
	}


	public void setLevel(int level) {
		this.level = level;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}
	public List<TreeNode> getChildren() {
		return children;
	}
	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIconClose() {
		return iconClose;
	}
	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}
	public String getIconOpen() {
		return iconOpen;
	}
	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}
	public boolean getNocheck() {
		return nocheck;
	}
	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}

	public boolean isIsParent() {
		return isParent;
	}


	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}


	public Map<String, String> getAttribute() {
		return attribute;
	}

	public void setAttribute(Map<String, String> attribute) {
		this.attribute = attribute;
	}
	
	public void putAttribute(String key, String value) {
		this.attribute.put(key,value);
	}


	public Boolean getLeaf() {
		return leaf;
	}


	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
}
