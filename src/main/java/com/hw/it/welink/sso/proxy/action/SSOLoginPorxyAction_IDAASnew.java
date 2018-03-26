//package com.hw.it.welink.sso.proxy.action;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URL;
//import java.net.URLDecoder;
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
//import org.json.JSONArray;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import com.google.gson.Gson;
//import com.hw.it.welink.sso.proxy.po.IDAASTokenVo;
//import com.hw.it.welink.sso.proxy.po.UserInfo;
//import com.hw.it.welink.sso.proxy.po.WeLinkRole;
//import com.hw.it.welink.sso.proxy.util.ConfigConstant;
//import com.hw.it.welink.sso.proxy.util.HttpClientUtils;
//import com.hw.it.welink.sso.proxy.util.VerifyCodeUtils;
//
//@Controller
//@RequestMapping("/v1")
//public class SSOLoginPorxyAction_IDAASnew {
//	private static final Logger logger = LoggerFactory.getLogger(SSOLoginPorxyAction_IDAASnew.class);	
//	@RequestMapping(value = "/pclogout", method = { RequestMethod.POST, RequestMethod.GET })
//	@ResponseBody
//	public Map<String, Object> logout(HttpServletRequest request, HttpServletResponse response) throws IOException  {
//		Map<String, Object> result = new HashMap<String, Object>();
//		String redirect_url = request.getQueryString();
//		if(redirect_url!=null && redirect_url.contains("redirect_uri=")) {
//			redirect_url = redirect_url.replace("redirect_uri=", "");
//		}else {
//			return null;
//		}
//        HttpSession session = request.getSession(false);//防止创建Session  
//		logger.info("========logout session:"+session);
//
//        if(session == null){  
//            response.sendRedirect(redirect_url);  
//        }else {
//    		logger.info("========logout session:"+session.getId());
//        	session.invalidate();
//        }
//          
//        result.put("redirect_url", redirect_url);
//		return result;
//	}
//	@RequestMapping(value = "/pclogin", method = RequestMethod.POST)
//	@ResponseBody
//	public Map<String, Object> login(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException{
//		Map<String, Object> result = new HashMap<String, Object>();
//		String redirect_url ="";
//		HttpSession session=request.getSession(false);
//		if(session==null) {
//			session=request.getSession(true);
//			logger.info("session----->sessionID:" + session.getId());
//		}
//		try {
//			String userName = request.getParameter("userName").trim();
//			String password = request.getParameter("password").trim();
//			redirect_url = request.getParameter("redirect_url");
//	
//			//判断是否需要验证码
//			if(request.getParameter("errorCode")!=null && !"".equals(request.getParameter("errorCode"))) {
//				String vertifyInput = request.getParameter("vertifyInput");
//				String vertifyCode = (String) session.getAttribute("verifyCode");
//				if(vertifyCode!=null && vertifyInput!=null && vertifyCode.equalsIgnoreCase(vertifyInput)) {
//					logger.info("========vertifyCode ok! vertifyCode_session:"+vertifyCode);
//				}else {
//					//验证码验证失败，再次输入验证码
//					logger.info("========vertifyCode fail! vertifyCode_session:"+vertifyCode+", vertifyCode_userInput:"+vertifyInput);
//					result.put("errorCode", "001");
//					result.put("redirect_url", redirect_url);
//					return result;
//				}
//			}		
//	
//			Map<String, String> param = new HashMap<String, String>();
//			param.put("username", userName);
//			param.put("password", password);
//			String login_action = getLoginActionUrl(redirect_url);
//			if(login_action==null) {
//				//权限验证失败
//				response.getWriter().println("========SSO System maintenance! login_action==null");
//				result.put("errorCode", "001");
//				result.put("redirect_url", redirect_url);
//				return result;
//			}
//			Header[] headers = HttpClientUtils.doPostToHeaders(login_action, param);
//			String code = null;
//			if (headers == null) {
//				// 登入失败 	
//				logger.info("========login fail,redirect_url:"+URLDecoder.decode(redirect_url.replace("redirect_uri=", ""), "UTF-8"));
//				result.put("errorCode", "001");
//				result.put("redirect_url", redirect_url);
//				return result;
//			}
//			for (Header header : headers) {
//				
//				if (header.getName().equals("Location")) {
//					String value = header.getValue();
//					URL url = new URL(value);
//					String[] params = url.getQuery().split("&");
//					for (String p : params) {
//						if (p.indexOf("=") >= 0 && p.split("=").length == 2 && "code".equals(p.split("=")[0])) {
//							code = p.split("=")[1];
//							break;
//						} else {
//							//
//							logger.warn("Illegal param, " + param);
//						}
//					}
//				}
//			}
//			if (code == null) {
//				// 登入失败 	
//				logger.info("========login fail,redirect_url:"+URLDecoder.decode(redirect_url.replace("redirect_uri=", ""), "UTF-8"));
//				result.put("errorCode", "001");
//				result.put("redirect_url", redirect_url);
//				return result;
//			}
//	
//			Map<String, String> params = new HashMap<String, String>();
//			params.put("grant_type", "authorization_code");
//			params.put("code", code);
//			params.put("redirect_uri", URLDecoder.decode(redirect_url.replace("redirect_uri=", "")));
////			params.put("client_id", "welink");
////			params.put("client_secret", ConfigConstant.CLIENT_SECRET);
////			String url = ConfigConstant.SSO_HOST + "/realms/welink/protocol/openid-connect/token";
//			String url = "";
//			if(ConfigConstant.CONFIG_SSO_TYPE.equalsIgnoreCase("WELINK")){
//				params.put("client_id", "welink");
//				params.put("client_secret", ConfigConstant.CLIENT_SECRET);
//				url = ConfigConstant.SSO_URL + ConfigConstant.TOKEN_URL;
//			} else if (ConfigConstant.CONFIG_SSO_TYPE.equalsIgnoreCase("IDAAS")){
//				params.put("client_id", ConfigConstant.IDAAS_CLIENT_ID);
//				params.put("client_secret", ConfigConstant.IDAAS_CLIENT_SECRET);
//				url = ConfigConstant.IDAAS_HOST + ConfigConstant.IDAAS_TOKEN_URL;
//			}
//			String tokenResult = HttpClientUtils.doPost(url, params);
//			logger.info("tokenResult----->tokenResult:" + tokenResult);
//
//			Gson gson = new Gson();
//			IDAASTokenVo tokenVo = gson.fromJson(tokenResult, IDAASTokenVo.class);
//			
//			if(tokenResult!=null) {
//				tokenResult = tokenResult.replace("\"name\"", "\"displayName\"");
//				tokenResult = tokenResult.replace("\"tenantid\"", "\"jti\"");
//				tokenResult = tokenResult.replace("\"username\"", "\"name\"");
//
//			}
//			UserInfo info=gson.fromJson(tokenResult, UserInfo.class);
//			String url_role =ConfigConstant.URL_ROLE+info.accountid;
//			logger.info("url_role----->url_role:" + url_role);
//			String result_role =HttpClientUtils.doGet(url_role);
//			logger.info("result_role----->result_role:" + result_role);
//			if(result_role!=null && result_role.length()>2) {//查不到数据的时候为:[]
//				List<WeLinkRole> rolesList=new ArrayList<WeLinkRole>();
//
//				JSONArray jsonArray = new JSONArray (result_role);
//				 for(int i=0;i<jsonArray.length();i++){	    
////					 System.out.println(jsonArray.get(i)) ; 
//					 String role= String.valueOf(jsonArray.get(i)).replace("tenantId", "tenantid");
//					 System.out.println("role:"+role);
//					 WeLinkRole weLinkRole=gson.fromJson(role, WeLinkRole.class);
//					 rolesList.add(weLinkRole);
//				 }
//				
//
//				
//				info.roles=rolesList;
//			}
//			info.realmRole=null;
//			String info_json = gson.toJson(info, UserInfo.class);
//			
//			logger.info("UserInfo----->info_json:" + info_json);
//			session.setAttribute("sso.user.session", info_json);
//			session.setAttribute("sso.user.token", tokenVo.access_token);	
//			logger.info("UserInfo----->token:" + tokenVo.access_token);
//	
//			if (tokenResult != null) {
//				result.put("result", "2");
//				result.put("url", java.net.URLDecoder.decode(redirect_url.replace("redirect_uri=", ""), "UTF-8"));
//				
//			}
//		}catch(Exception e){
//			logger.error("========login fail,redirect_url:"+URLDecoder.decode(redirect_url.replace("redirect_uri=", ""), "UTF-8"),e);
//			result.put("errorCode", "001");
//			result.put("redirect_url", redirect_url);
//			return result;
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
//		
//		if(redirect_url.contains("errorCode=")) {
//			map.addAttribute("errorCode", redirect_url.split("errorCode=")[1]);
//			redirect_url= redirect_url.split("errorCode=")[0];
//		}
//		
//		map.addAttribute("redirect_url", redirect_url);
//
//		logger.info("========loginProxy sucess,redirect_url:"+URLDecoder.decode(redirect_url.replace("redirect_uri=", ""), "UTF-8"));
//		
//
//		return "login";
//	}
//	
//	public String getLoginActionUrl(String redirect_url) {
//
//		String loginActionUrl=null;	
//		try {
//			if(ConfigConstant.CONFIG_SSO_TYPE.equals("IDAAS")){
//				return ConfigConstant.IDAAS_HOST + ConfigConstant.IDAAS_LOGIN_URL + "?client_id=" + ConfigConstant.IDAAS_CLIENT_ID + "&" + redirect_url;
//			}
//			
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
//	
//	@RequestMapping(value = "/getVerify", method = { RequestMethod.POST, RequestMethod.GET })
//	public void verifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		response.setHeader("Pragma", "No-cache");
//		response.setHeader("Cache-Control", "No-cache");
//		response.setDateHeader("Expires", 0);
//		response.setContentType("image/jpeg");
//		//生成随机字典
//		String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
//		HttpSession session=request.getSession(false);
//		if(session==null) {
//			session=request.getSession(true);
//			logger.info("verifyCode session----->sessionID:" + session.getId());
//		}
//		session.setAttribute("verifyCode", verifyCode);
//		//生成图片
//		int w = 200,h = 80;
//		VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
//		
//	}
//}
