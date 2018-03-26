package com.hw.it.welink.sso.proxy;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.hw.it.welink.sso.proxy.util.HttpClientUtils;

public class ConfigConstantService {
	private static String url="http://w3-beta.huawei.com/mmservice/pmiddle/api/v1/welink/license?tenantId=";
	public static void main(String[] args) {
		System.out.println(getConfigConstant("CICNB80_TC3"));
		System.out.println(getConfigConstantToMap("CICNB80_TC3"));
	}
	public static String getConfigConstant(String tenantId) {
		String result = HttpClientUtils.doGet(url+tenantId);
		if(result!=null && result.length()>2) {//查不到数据的时候为:{"message":"租户ID不存在","code":500}
			//{"licenseUsage":"2000","code":0,"registerdUsers":"10000"}
			
		}
		return result;
	}
	public static Map<String,Object > getConfigConstantToMap(String tenantId) {
		Map<String,Object > map = new HashMap<String,Object>();
		String result = HttpClientUtils.doGet(url+tenantId);
//		String result ="{\"licenseUsage\":\"2000\",\"code\":0,\"registerdUsers\":\"10000\"}";
		if(result!=null && result.length()>2) {//查不到数据的时候为:{"message":"租户ID不存在","code":500}
			JSONObject jSONObject = new JSONObject(result);
			map = jSONObject.toMap();
		}
		return map;
	}
}
