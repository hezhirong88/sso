package com.hw.it.welink.sso.proxy.action;

import com.google.gson.Gson;
import com.hw.it.welink.sso.proxy.po.AccessToken;
import com.hw.it.welink.sso.proxy.po.JSONWebKeySet;
import com.hw.it.welink.sso.proxy.po.JWK;
import com.hw.it.welink.sso.proxy.po.JWKParser;
import com.hw.it.welink.sso.proxy.po.JWSHeader;
import com.hw.it.welink.sso.proxy.po.JWSInput;
import com.hw.it.welink.sso.proxy.po.UserInfo;
import com.hw.it.welink.sso.proxy.po.WeLinkRole;
import com.hw.it.welink.sso.proxy.util.ConfigConstant;
import com.hw.it.welink.sso.proxy.util.HttpClientUtils;
import com.hw.it.welink.sso.proxy.util.HttpRequestor;
import java.io.PrintStream;
import java.security.PublicKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping({ "/v1" })
public class SSOLoginUtil {
	private static final Logger logger = LoggerFactory.getLogger(SSOLoginUtil.class);
	private static Map<String, PublicKey> publicKeyMap = new HashMap();
	private static Gson gson = new Gson();

	@RequestMapping(value = { "/oidc/token" }, method = { org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String getAccessToken(@RequestParam("username") String username, @RequestParam("password") String password)
			throws SSOServiceException {
		String url = ConfigConstant.SSO_URL + ConfigConstant.TOKEN_URL;
		System.out.println("Get token url : " + url);
		HashMap<String, String> parameterMap = new HashMap();
		parameterMap.put("client_id", "welink");
		parameterMap.put("client_secret", ConfigConstant.CLIENT_SECRET);
		parameterMap.put("username", username);
		parameterMap.put("password", password);
		parameterMap.put("grant_type", "password");
		HttpRequestor req = new HttpRequestor();
		String tokneInfo;
		try {
			tokneInfo = req.doPost(url, parameterMap);
		} catch (Exception e) {
			logger.error("=== getAccessToken fail!", e);
			throw new SSOServiceException(e.getMessage());
		}

		return tokneInfo;
	}

	@RequestMapping(value = { "/oidc/userinfo" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public String getUserInfoByToken(@RequestHeader("Authorization") String Authorization) throws SSOServiceException {
		String url = ConfigConstant.SSO_URL + ConfigConstant.USER_URL;
		String userInfo;
		try {
			userInfo = new HttpRequestor().doGetFUserInfo(url, Authorization);
		} catch (Exception e) {

			logger.error("=== getUserInfoByToken fail!", e);
			throw new SSOServiceException(e.getMessage());
		}
		UserInfo info = (UserInfo) gson.fromJson(userInfo, UserInfo.class);

		String url_role = ConfigConstant.URL_ROLE + info.accountid;
		logger.info("getUserInfoByToken url_role----->url_role:" + url_role);
		String result_role = HttpClientUtils.doGet(url_role);
		logger.info("getUserInfoByToken result_role----->result_role:" + result_role);
		if ((result_role != null) && (result_role.length() > 2)) {
			List<WeLinkRole> rolesList = new ArrayList();

			JSONArray jsonArray = new JSONArray(result_role);
			for (int i = 0; i < jsonArray.length(); i++) {
				String role = String.valueOf(jsonArray.get(i)).replace("tenantId", "tenantid");
				System.out.println("role:" + role);
				WeLinkRole weLinkRole = (WeLinkRole) gson.fromJson(role, WeLinkRole.class);
				rolesList.add(weLinkRole);
			}

			info.roles = rolesList;
		}
		String info_json = gson.toJson(info, UserInfo.class);

		return info_json;
	}

	@RequestMapping(value = { "/oidc/token/introspect" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public boolean checkToken(@RequestParam("accessToken") String token) throws SSOServiceException {
		logger.info("Receive checkToken request----->url_role:" + token);
		String url = ConfigConstant.SSO_URL + ConfigConstant.JWKS_URL;
		JWSInput input = new JWSInput(token);
		PublicKey pKey = (PublicKey) publicKeyMap.get(input.getHeader().getKid());
		String tenantid = null;
		if (pKey == null) {

			HttpRequestor req = new HttpRequestor();
			String jwkResult;
			try {
				UserInfo userInfo = getBaseUserInfo(token);

				if ((userInfo != null) && (userInfo.tenantid != null)) {
					tenantid = userInfo.tenantid;
				}
				jwkResult = req.doGetFPublicKey(url, null);
			} catch (Exception e) {

				logger.error("=== checkToken fail!", e);
				throw new SSOServiceException(e.getMessage());
			}
			Gson gson = new Gson();

			JSONWebKeySet keySet = (JSONWebKeySet) gson.fromJson(jwkResult, JSONWebKeySet.class);
			for (JWK jwk : keySet.getKeys()) {
				JWKParser parser = JWKParser.create(jwk);

				PublicKey publicKey = parser.toPublicKey();
				publicKeyMap.put(jwk.getKid(), publicKey);
			}

			pKey = (PublicKey) publicKeyMap.get(input.getHeader().getKid());
		}

		if (pKey == null) {
			throw new SSOServiceException("no valid pKey!");
		}
		boolean result = verify(input, pKey);
		return result;
	}

	private boolean verify(JWSInput input, PublicKey publicKey) {
		try {
			Signature verifier = Signature.getInstance("SHA256withRSA");
			verifier.initVerify(publicKey);
			verifier.update(input.getEncodedSignatureInput().getBytes("UTF-8"));
			return verifier.verify(input.getSignature());
		} catch (Exception e) {
			logger.error("=== verify fail!", e);
		}
		return false;
	}

	@RequestMapping(value = { "/oidc/token/refresh" }, method = {
			org.springframework.web.bind.annotation.RequestMethod.POST })
	@ResponseBody
	public String refreshToken(@RequestParam("refreshtoken") String refreshtoken) throws SSOServiceException {
		String url = ConfigConstant.SSO_URL + ConfigConstant.TOKEN_URL;
		HashMap<String, String> parameterMap = new HashMap<String, String>();
		parameterMap.put("grant_type", "refresh_token");
		parameterMap.put("refresh_token", refreshtoken);
		parameterMap.put("client_id", "welink");
		parameterMap.put("client_secret", ConfigConstant.CLIENT_SECRET);
		HttpRequestor req = new HttpRequestor();
		AccessToken token = null;
		String result = "";
		try {
			result = req.doPost(url, parameterMap);
		} catch (Exception e) {
			logger.error("=== refreshToken fail!", e);
			throw new SSOServiceException(e.getMessage());
		} finally {
			if (token == null) {
				token = new AccessToken();
				token.isActive = false;
			}
		}

		return result;
	}

	public static UserInfo getBaseUserInfo(String accessToken) throws SSOServiceException {
		JWSInput input = new JWSInput(accessToken);
		byte[] content = input.getContent();
		String sContent = new String(content);
		UserInfo info = (UserInfo) gson.fromJson(sContent, UserInfo.class);
		return info;
	}

	@RequestMapping(value = { "/oidc/certs" }, method = { org.springframework.web.bind.annotation.RequestMethod.GET })
	@ResponseBody
	public String getCerts() throws Exception {
		return new HttpRequestor().doGetFPublicKey(ConfigConstant.SSO_URL + ConfigConstant.JWKS_URL, null);

	}
}
