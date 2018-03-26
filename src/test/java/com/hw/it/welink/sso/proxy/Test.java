package com.hw.it.welink.sso.proxy;

import org.json.JSONArray;

import com.hw.it.welink.sso.proxy.util.HttpClientUtils;

public class Test {
	public static void main(String[] args) {
		String userName ="test1";
		String url_role ="http://w3-beta.huawei.com/mmservice/pmiddle/api/v1/welink/roles?accountId="+userName;
		String result_role =HttpClientUtils.doGet(url_role);
//		result_role ="[{\"roleCode\":\"liveAdmin\",\"tenantId\":\"WeLink\"},{\"roleCode\":\"enterpriseAdmin\",\"tenantId\":\"WeLink\"},{\"roleCode\":\"platformAdmin\",\"tenantId\":\"system\"}]";

		JSONArray jsonArray = new JSONArray (result_role);
		 for(int i=0;i<jsonArray.length();i++){	    
			 System.out.println(jsonArray.get(i)) ;  
		 }

	}
}
