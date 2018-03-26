package com.hw.it.welink.sso.proxy.po;

import java.io.Serializable;
import java.util.List;

public class SerializableSSOAccount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8879764598321791563L;
	private List<String> roles;
	private String email;
	private String tenantid;
	private String accountid;
	private String userName;
	private String displayName;

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTenantid() {
		return tenantid;
	}

	public void setTenantid(String tenantid) {
		this.tenantid = tenantid;
	}

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
