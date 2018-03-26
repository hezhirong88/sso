package com.hw.it.welink.sso.proxy.action;

import com.google.gson.Gson;

import com.hw.it.welink.sso.proxy.po.KeyCloakTokenVo;
import com.hw.it.welink.sso.proxy.po.MAGLoginError;
import com.hw.it.welink.sso.proxy.po.ResponseVO;
import com.hw.it.welink.sso.proxy.po.SSOError;
import com.hw.it.welink.sso.proxy.util.ConfigConstant;
import com.hw.it.welink.sso.proxy.util.HttpClientUtils;
import com.hw.it.welink.sso.proxy.util.RSA;

import it.sauronsoftware.base64.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({ "/v1/" })
public class SSOLoginProxy {
	private static final Logger logger = LoggerFactory.getLogger(SSOLoginProxy.class);
	private static final String ENCODING = "UTF-8";

	@RequestMapping(value = { "mlogin" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	public String ssoLogin(@RequestParam(value="username",required = false) String userName, @RequestParam(value="password",required = false) String password,
			@RequestParam(value = "eflag", required = false) String eflag) {
		
		logger.info("receive sso login request,username:" + userName);
		
		/*
		 * 判断用户名和密码是否必填 ，保持和mag逻辑一致
		 */
		Gson g = new Gson();
		if(userName==null||userName.length()==0){		
			MAGLoginError mle = new MAGLoginError("1101");
			return g.toJson(mle);
		}else if(password==null||password.length()==0) {		
			MAGLoginError mle = new MAGLoginError("1102");
			return g.toJson(mle);
		}
		
		


		Map<String, String> param = new HashMap<String, String>();
		param.put("client_id", "welink");
		param.put("client_secret", ConfigConstant.CLIENT_SECRET);
		if ((eflag == null) || (eflag.isEmpty()) || (eflag.equals("0"))) {
			//eflag为空或者0为静态公钥
			
			/*
			 * 解密用户和密码
			 */		
			
			String d_userName=RSA.decryptInfo("0",userName);
			String d_password=RSA.decryptInfo("0",password);
			
			//解密失败的处理 
			if(d_userName==null||d_userName.length()==0){
				
				MAGLoginError mle = new MAGLoginError("1100");
				return g.toJson(mle);
			}else if(d_password==null||d_password.length()==0) {
				
				MAGLoginError mle = new MAGLoginError("1030");
				return g.toJson(mle);
			}
			
			
			param.put("username", d_userName);
			param.put("password", d_password);
			logger.info("解密后登陆");
		} else if (eflag.equals("1")) {
			//eflag为1是动态公钥
			/*
			 * 解密用户和密码
			 */		
			
			String d_userName=RSA.decryptInfo("1",userName);
			String d_password=RSA.decryptInfo("1",password);
			
			//解密失败的处理 
			if(d_userName==null||d_userName.length()==0){
				
				MAGLoginError mle = new MAGLoginError("1100");
				return g.toJson(mle);
			}else if(d_password==null||d_password.length()==0) {
				
				MAGLoginError mle = new MAGLoginError("1030");
				return g.toJson(mle);
			}
			param.put("username", d_userName);
			param.put("password", d_password);
		} else {
			//eflag为其他，是明文
			param.put("username", userName);
			param.put("password", password);
			logger.info("明文登陆");
		}

		param.put("grant_type", "password");
		logger.info("sso login request,client_id:HW_Cloud_WeLink,username:" + userName + ",password:" + password);
		
		String url = ConfigConstant.SSO_HOST + "/realms/welink/protocol/openid-connect/token";
		
		
		String result = HttpClientUtils.doPost(url, param);
		logger.info("sso login result:" + result);
		if (result.contains("\"error\":")) {
			logger.info("sso login error:" + result);
		    //	Gson g = new Gson();
			SSOError ssoError = (SSOError) g.fromJson(result, SSOError.class);
			
			/*			
			if (ssoError.error.equalsIgnoreCase("10009")) {
				return g.toJson(ssoError);
			}
			*/
			MAGLoginError mle = new MAGLoginError().init(ssoError);
			return g.toJson(mle);
			
			//return "{\r\n    \"error\": \"10008\",\r\n    \"error_description\": \"帐号或密码错误，请重新填写\"\r\n}";
		}

		Gson gson = new Gson();
		KeyCloakTokenVo vo = (KeyCloakTokenVo) gson.fromJson(result, KeyCloakTokenVo.class);
		vo.init();
		ResponseVO rvo = new ResponseVO().init(vo);
		return gson.toJson(rvo);
	}

	@RequestMapping(value = { "ssoHost" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	public String getSSOHost() {
		return ConfigConstant.SSO_HOST;
	}

}
