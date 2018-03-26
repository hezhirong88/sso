package com.hw.it.welink.sso.proxy.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigConstant {
	public static String SSO_HOST = "http://w3-beta.huawei.com/auth";
	public static String CLIENT_SECRET = "2f4e0573-07b0-4665-a00c-05e0ad0798f3";

	public static String SSO_URL = SSO_HOST+"/realms";
	public static String TOKEN_URL = "/welink/protocol/openid-connect/token";
	public static String USER_URL = "/welink/protocol/openid-connect/userinfo";
	public static String JWKS_URL = "/welink/protocol/openid-connect/certs";
	public static String URL_ROLE ="http://w3-beta.huawei.com/mmservice/pmiddle/api/v1/welink/roles?accountId=";

	public static String IDAAS_HOST = "http://100.101.31.248";
	public static String IDAAS_CLIENT_ID = "2a4d8ef2-e1c7-4682-85a3-fd86b0810dbd";
	public static String IDAAS_CLIENT_SECRET = "yBfviN8Hmcie4j7J2allDc9UALJNdYRz9jvPLfABuT7DzahCsxwG59Lkx90gUJEGSX7eqxKrLWiP-zVeeTaOyQ";
	public static String IDAAS_LOGIN_URL = "/avatar/login";
	public static String IDAAS_TOKEN_URL = "/avatar/token";
	public static String IDAAS_REDIRECT_URI = "https://authexample1.huawei.com:8443/";
	
	public static String CONFIG_SSO_TYPE = "WELINK"; //OR IDAAS
	
	@Value("${sso.info.SSO_HOST}")
	public void setSSO_HOST(String sSO_HOST) {
		SSO_HOST = sSO_HOST;
		SSO_URL = SSO_HOST+"/realms";
		System.out.println("SSO_HOST:"+SSO_HOST);
		System.out.println("SSO_URL:"+SSO_URL);
	}

	@Value("${sso.info.secret}")
	public void setCLIENT_SECRET(String cLIENT_SECRET) {
		CLIENT_SECRET = cLIENT_SECRET;
	}
	
	@Value("${sso.info.URL_ROLE}")
	public void setURL_ROLE(String uRL_ROLE) {
		URL_ROLE = uRL_ROLE;
	}
	@Value("${sso.info.CONFIG_SSO_TYPE}")
	public void setCONFIG_SSO_TYPE(String cONFIG_SSO_TYPE) {
		CONFIG_SSO_TYPE = cONFIG_SSO_TYPE;
	}	
}
