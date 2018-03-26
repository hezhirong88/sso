package com.hw.it.welink.sso.proxy.po;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UserInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	// public String sub;
	public String preferred_username;
	//英文名
	public String name;
	//中文名
	public String displayName;
	public String given_name;
	public String family_name;
	public String email;
	@SerializedName("jti")
	public String tenantid;
	@SerializedName("sub")
	public String accountid;
	@SerializedName("realm_access")
	public RealmRole realmRole;

	public List<WeLinkRole> roles;
}
