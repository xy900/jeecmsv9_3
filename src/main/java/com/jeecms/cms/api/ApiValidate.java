package com.jeecms.cms.api;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jeecms.cms.entity.main.ApiAccount;
import com.jeecms.common.util.PayUtil;
import com.jeecms.common.web.RequestUtils;
import com.jeecms.core.web.WebErrors;

public class ApiValidate {
	private static final Logger log = LoggerFactory.getLogger(ApiValidate.class);
	
	public static WebErrors validateRequiredParams(
			HttpServletRequest request,
			WebErrors errors,Object... params){
//		int i=0;
		for(Object p:params){
			//空字符串或者其他类型对象为空
			if((p instanceof String&&StringUtils.isBlank((String) p)||p==null)){
				errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
			}//else if(p.getClass().isArray()){
//				Object[] p_array = (Object[])p;
//				if(p_array.length==0||Arrays.asList(p_array).contains("")){
//					errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
//				}
//			}else if(p instanceof Collection){
//				 List p_list = Arrays.asList(p);
//				 if(p_list.size()==0||p_list.contains("")){
//					 errors.addErrorString(Constants.API_MESSAGE_PARAM_REQUIRED);
//				 }
//			}
		}
		return errors;
	}
	
	public static WebErrors validateApiAccount(HttpServletRequest request,
			WebErrors errors,ApiAccount apiAccount){
		if(apiAccount==null||apiAccount.getDisabled()){
			errors.addErrorString(Constants.API_MESSAGE_APP_PARAM_ERROR);
			log.error(RequestUtils.getIpAddr(request)+Constants.API_MESSAGE_APP_PARAM_ERROR);
		}
		return errors;
	}
	
	public static WebErrors validateSign(HttpServletRequest request,
			WebErrors errors,ApiAccount apiAccount,String sign){
		if(apiAccount!=null&&!apiAccount.getDisabled()){
			// 获取非签名,空字符,文件的参数名和参数值
			Map<String,String>param=RequestUtils.getSignMap(request);
			// 密匙
			String appKey=apiAccount.getAppKey();
			String validateSign=PayUtil.createSign(param, appKey);
			if(StringUtils.isBlank(sign)||!sign.equals(validateSign)){
				errors.addErrorString("sign_validate_error");
				log.error(RequestUtils.getIpAddr(request)+" sign validate error");
			}
		}else{
			errors.addErrorString(Constants.API_MESSAGE_APP_PARAM_ERROR);
			log.error(RequestUtils.getIpAddr(request)+Constants.API_MESSAGE_APP_PARAM_ERROR);
		}
		return errors;
	}
	
}
