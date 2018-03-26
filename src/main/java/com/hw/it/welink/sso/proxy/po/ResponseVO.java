package com.hw.it.welink.sso.proxy.po;



/**
 * @author h00276284 2018年3月23日
 *
 */

import java.lang.reflect.Field;

import com.google.gson.Gson;

public class ResponseVO {

	public String access_token;
	public long expires_in;
	public long refresh_expires_in;
	public String refresh_token;
	public String token_type;
	public String id_token;
	public String userType;//tenantid
	public String userCN;//username
	public String email;
	public String uid;//accountid
	public String  userNameZH;//displayName

	public String dynamicPublicRsaKey;
	//public String warning;
	public Boolean isSFReg;
	public String login;

	public ResponseVO init(KeyCloakTokenVo vo) {
		ResponseVO rvo = new ResponseVO();
		
		
		rvo.access_token = vo.access_token;
		rvo.expires_in = vo.expires_in;
		rvo.refresh_expires_in = vo.refresh_expires_in;
		rvo.refresh_token = vo.refresh_token;
		rvo.token_type = vo.token_type;
		rvo.id_token = vo.id_token;


		rvo.userType = vo.tenantid;
		rvo.userCN = vo.username;
		rvo.email = vo.email;
		rvo.uid = vo.accountid;
		rvo.userNameZH = vo.displayName;
		
		rvo.dynamicPublicRsaKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCRBD1wjArlqhsFr76TKkRHV8LZA5G50iDVCd23L56kcMUUH8dQoo9OpeZZhAufc1fIfDOKauKLQMidNF6g2hzA4rATJo5EMovTURs7c6TBUfLvO3KNaasnJKEVP5SQZzAJNEDV34goYjxaag56yKZtG2Wwp28o1gNmGRneXduJdQIDAQAB";
		rvo.login = "successed";
		//rvo.warning = "";
		rvo.isSFReg = Boolean.TRUE;
		
		return rvo;

	}
	
	


}
