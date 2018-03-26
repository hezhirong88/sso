//package com.hw.it.welink.sso.proxy.action;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.apache.http.Header;
//import org.htmlparser.NodeFilter;
//import org.htmlparser.Parser;
//import org.htmlparser.filters.HasAttributeFilter;
//import org.htmlparser.tags.FormTag;
//import org.htmlparser.util.NodeList;
//import org.htmlparser.util.ParserException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.google.gson.Gson;
//import com.hw.it.welink.sso.proxy.po.JWSInput;
//import com.hw.it.welink.sso.proxy.po.KeyCloakTokenVo;
//import com.hw.it.welink.sso.proxy.po.UserInfoJson;
//import com.hw.it.welink.sso.proxy.util.ConfigConstant;
//import com.hw.it.welink.sso.proxy.util.HttpClientUtils;
//
//@Controller
//@RequestMapping("/vold")
//public class SSOLoginPorxyAction_old {
//	private static final Logger logger = LoggerFactory.getLogger(SSOLoginPorxyAction_old.class);
//
//	private static final String login_action_url = "http://w3-beta.huawei.com/auth/realms/welink/login-actions/authenticate?code=";
//	private static final String auth_url = "http://w3-beta.huawei.com/auth/realms/welink/login-actions/authenticate";
//	private static String service_url = "http://localhost:8080/sso.proxy";
//	@RequestMapping(value = "/pclogin", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		Map<String, Object> result = new HashMap<String, Object>();
//		request.setCharacterEncoding("UTF-8");
//		response.setCharacterEncoding("UTF-8");
//		String userName = request.getParameter("userName");
//		String password = request.getParameter("password");
//		String redirect_url = request.getParameter("redirect_url");
//		service_url =request.getScheme() +"://" + request.getServerName()  
//        + ":" +request.getServerPort() + request.getContextPath(); 
//		/*
//		 * String url = ConfigConstant.SSO_HOST +
//		 * "/realms/welink/protocol/openid-connect/token"; Map<String, String> param =
//		 * new HashMap<String, String>();
//		 * 
//		 * param.put("client_id", "welink"); param.put("client_secret",
//		 * ConfigConstant.CLIENT_SECRET);
//		 * 
//		 * param.put("username", userName.trim()); param.put("password",
//		 * password.trim());
//		 */
//
//		Map<String, String> param = new HashMap<String, String>();
//		param.put("username", userName);
//		param.put("password", password);
//		String login_action = getLoginActionUrl(redirect_url);
//		if(login_action==null) {
//			//权限验证失败
//			response.sendRedirect(service_url+"/v1/loginProxy?"+redirect_url+"errorCode=001");
//			response.getWriter().println("========SSO System maintenance!");
//			result.put("result", "2");
//			result.put("errcode", "123");
//			result.put("url", java.net.URLDecoder.decode(redirect_url.replace("redirect_uri=", ""), "UTF-8"));
//			return result;
//		}
//		Header[] headers = HttpClientUtils.doPostToHeaders(login_action, param);
//		String code = null;
//		String redUrl = null;
//		if (headers == null) {
//
////			result.put("result", "1");
////			result.put("url", "http://localhost:9905/sso/v1/loginProxy?" + redirect_url);
//			// TODO 登入失败 重新到登入页面 并将login_action redirect_url带入到登入页面
//			response.sendRedirect(service_url+"/v1/loginProxy?"+redirect_url+"errorCode=001");
//			logger.info("========login fail,redirect_url:"+redirect_url);
//			return result;
//		}
//		for (Header header : headers) {
//			if (header.getName().equals("Location")) {
//				String value = header.getValue();
//				code = value.substring(value.indexOf("code=") + 5, value.length());
//				redUrl = value.substring(0, value.indexOf("code=") - 1);
//				break;
//			}
//		}
//		if (code == null) {
//			// TODO 登入失败 重新到登入页面 并将login_action redirect_url带入到登入页面
//			response.sendRedirect(service_url+"/v1/loginProxy?"+redirect_url+"errorCode=001");
//			logger.info("========login fail,redirect_url:"+redirect_url);
//			return result;
//		}
//
//		Map<String, String> params = new HashMap<String, String>();
//		params.put("grant_type", "authorization_code");
//		params.put("code", code);
//		params.put("redirect_uri", redUrl);
//		params.put("client_id", "welink");
//		params.put("client_secret", ConfigConstant.CLIENT_SECRET);
//		String url = ConfigConstant.SSO_HOST + "/realms/welink/protocol/openid-connect/token";
//		String tokenResult = HttpClientUtils.doPost(url, params);
//		Gson gson = new Gson();
//		KeyCloakTokenVo tokenVo = gson.fromJson(tokenResult, KeyCloakTokenVo.class);
//		JWSInput jwsInput=new JWSInput(tokenVo.access_token);
//		byte content[]=jwsInput.getContent();
//		String sContent=new String(content);
//		UserInfoJson info=gson.fromJson(sContent, UserInfoJson.class);
//		String admin = new String("企业管理员1".getBytes("UTF-8"));
//		String admin2 = new String("企业管理员2".getBytes("UTF-8"));
//
//		String role="welink,WeLink,"+admin+","+admin2+",enterpriseAdmin";
//		String role2="welink2,WeLink2,"+admin+","+admin2+",enterpriseAdmin2";
//
////		String admin = new String("企业管理员1".getBytes("UTF-8"));
////		String admin2 = new String("企业管理员2".getBytes("UTF-8"));
////		String role="welink,WeLink,"+admin+",enterpriseAdmin";
////		String role2="welink2,WeLink2,"+admin2+",enterpriseAdmin2";
//		
//		role=new String(role.getBytes(),"UTF-8");
//		role2=new String(role2.getBytes(),"UTF-8");
//
//		List<String> rolesList=new ArrayList<String>();
//		rolesList.add(role);
//		rolesList.add(role2);
//
//		
//		if(info.preferred_username.equalsIgnoreCase("test3")||info.preferred_username.equalsIgnoreCase("test4")) {
//			info.realm_access.roles=rolesList;
//		}
//		String info_json = gson.toJson(info, UserInfoJson.class);
//		HttpSession session=request.getSession(true);
////		Cookie cookies[] = request.getCookies();
////		if (cookies != null) {
////			for (Cookie c : cookies) {
////				logger.info("cookie----->name:" + c.getName() + ",value:"
////						+ c.getValue() + ",domain:" + c.getDomain() + ",path:"
////						+ c.getPath());
////			}
////		}
//		logger.info("UserInfo----->info_json:" + info_json);
//		logger.info("UserInfo----->sContent:" + sContent);
//		
//		session.setAttribute("sso.user.session", info_json);
////		System.out.println(tokenResult);
//		logger.info("========login sucess,info.name="+info.name+",info.tenantid="+info.jti+",tokenResult="+tokenResult);
//
//		if (tokenResult != null) {
//			result.put("result", "2");
//			result.put("url", java.net.URLDecoder.decode(redirect_url.replace("redirect_uri=", ""), "UTF-8"));
//			
//		}
//		return result;
//	}
//
//	@RequestMapping(value = "/loginProxy", method = { RequestMethod.POST, RequestMethod.GET })
//	public String loginProxy(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws IOException {
//		String redirect_url = request.getQueryString();
//		String urls[] = redirect_url.split("redirect_uri");
//		if (urls.length > 2) {
//			redirect_url = "redirect_uri" + urls[1];
//			redirect_url = redirect_url.replace("?", "");
//		}
//		if(redirect_url.contains("errorCode=")) {
//			map.addAttribute("errorCode", redirect_url.split("errorCode=")[1]);
//			redirect_url= redirect_url.split("errorCode=")[0];
//		}
//		map.addAttribute("redirect_url", redirect_url);
//		logger.info("========loginProxy sucess,redirect_url:"+redirect_url);
//		
//		return "login";
//	}
//	
//	public String getLoginActionUrl(String redirect_url) {
//		String loginActionUrl=null;	
//		try {
//			StringBuffer sb = new StringBuffer();
//			sb.append("&response_type=code").append("&client_id=welink");
//			String auht_redirect = redirect_url + sb.toString();
//			String authPage = HttpClientUtils
//					.doGet(ConfigConstant.SSO_HOST + "/realms/welink/protocol/openid-connect/auth?" + auht_redirect);
//
//			Parser parser = new Parser(authPage);
//			NodeFilter filter = new HasAttributeFilter("class", "form-horizontal");
//			NodeList nodes = parser.extractAllNodesThatMatch(filter);
//			FormTag fromTag = (FormTag) nodes.elementAt(0);
//			if(fromTag==null) {
//				//权限验证失败
//				logger.info("========getLoginActionUrl fail!");
//				return null;
//			}
//			loginActionUrl = fromTag.getAttribute("action");
//			logger.info("========getLoginActionUrl sucess,redirect_url:"+redirect_url);
//		} catch (ParserException e) {
//			logger.error("========getLoginActionUrl fail!",e);
//		}
//		return loginActionUrl;
//	}
//}
