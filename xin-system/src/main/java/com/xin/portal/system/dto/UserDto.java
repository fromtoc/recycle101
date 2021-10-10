package com.xin.portal.system.dto;


public class UserDto extends PageDto{
	
	private Integer id;
    private String username;
    private String realname;
    private Integer roleId;
    private Integer orgId;
    private Integer roleIdNot;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getRoleIdNot() {
		return roleIdNot;
	}

	public void setRoleIdNot(Integer roleIdNot) {
		this.roleIdNot = roleIdNot;
	}


    
    

    
    
}