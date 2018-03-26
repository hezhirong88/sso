package com.hw.it.welink.sso.proxy.po;
public class AccessToken {
	/**
	 * AccessToken 认证后可信任的凭证
	 */
	public String access_token;
	/**
	 * ID Token 解析可获承用户信息
	 */
	public String id_token;
	/**
	 * 可用于刷新AccessToken
	 */
	public String refresh_token;
	/**
	 * Token 类型
	 */
	public String token_type;
	/**
	 * refresh token 失效时间
	 */
	public String refresh_expires_in;
	/**
	 * access token 失效时间
	 */
	public String expires_in;
	public String not_before_olicy;
	public boolean isActive=true;
	public String session_state;
}
