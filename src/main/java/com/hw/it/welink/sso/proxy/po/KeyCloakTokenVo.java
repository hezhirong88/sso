package com.hw.it.welink.sso.proxy.po;

public class KeyCloakTokenVo {
	public String access_token;
	public long expires_in;
	public long refresh_expires_in;
	public String refresh_token;
	public String token_type;
	public String id_token;
	public String tenantid;
	public String username;
	public String email;
	public String accountid;
	public String uid;
	public String displayName;
	
	
	public void init() {
		try {
			JWSInput input=new JWSInput(access_token);
			AccessTokeContentVo vo=input.getTenantId();
			tenantid=vo.jti;
			username=vo.preferred_username;
			email=vo.email;
			uid=vo.sub;
			accountid=vo.sub;
			displayName=vo.displayName;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
