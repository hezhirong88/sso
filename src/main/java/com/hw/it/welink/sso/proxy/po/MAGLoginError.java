/**
 * 
 */
package com.hw.it.welink.sso.proxy.po;

/**
 * @author h00276284 2018年3月23日
 *
 */
public class MAGLoginError {
	public String errorCode;
	public String errorMessage;

	public MAGLoginError(String ec){
		errorCode=ec;
		switch(ec){
		case "1101":
			errorMessage="UserName cannot be empty.";
			break;
		case "1102":
			errorMessage="Password cannot been empty.";
			break;
		case "1100":
			errorMessage="Failed to decrypt the data. Log in with a W3 account.";
			break;
		case "1030":
			errorMessage="Failed to decrypt the password due to the invalid public key.";
			break;
		}
		
		
	}
public MAGLoginError(){
		
	}
	

	public MAGLoginError init(SSOError se) {
		MAGLoginError mle = new MAGLoginError();
		switch (se.error) {
		// 非HTTPS请求
		case "10001":
			mle.errorCode = "5000";
			mle.errorMessage = "非HTTPS请求";
			break;
			// 租户不可用
		case "10002":
			mle.errorCode = "5001";
			mle.errorMessage = "租户不可用";
			break;
			// client无效，包括clent不在存，不可用，及无传入clent_id
		case "10003":
			mle.errorCode = "5002";
			mle.errorMessage = "client无效";
			break;
			// 请求无相关必填参数或用户不可用
		case "10004":
			mle.errorCode = "1101";
			mle.errorMessage = "userName or password cannot be empty.";
			break;
			// 认证失败,跟client设置有关)
		case "10005":
			mle.errorCode = "5003";
			mle.errorMessage = "认证失败,跟client设置有关";
			break;
			// token失效或非法,需重新登录或刷新token
		case "10006":
			mle.errorCode = "1000";
			mle.errorMessage = "Log in with a valid account.";
			break;
			// client secret不存在或无效
		case "10007":
			mle.errorCode = "5004";
			mle.errorMessage = "client secret不存在或无效";
			break;
			// 帐号密码错误
		case "10008":
			mle.errorCode = "1040";
			mle.errorMessage = "Enter a valid user name and password.";
			break;
		// 需要修改密码
		case "10009":
			mle.errorCode = "1040";
			mle.errorMessage = "Enter a valid user name and password.";
			break;
			// other
		default:
			mle.errorCode = "1145";
			mle.errorMessage = "Sso system exception.";

		}



		return mle;

	}
}
