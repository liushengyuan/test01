package com.enation.app.shop.core.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IsmsMobileManager;
import com.enation.app.shop.component.payment.plugin.payease.Md5Sms;
import com.enation.app.tradeease.core.model.sms.SmsMobile;
import org.apache.commons.httpclient.Header;

import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.util.DateUtil;

public class SmsMoblieManager extends BaseSupport<Member> implements IsmsMobileManager{

	@Override
	public void sendMobile(String v_mobile, String v_content1) {
		// TODO Auto-generated method stub
		String v_smstype = "01";
		String v_servicecode="0014";
		String v_content="【绥易通】"+v_content1;	
		String md5Source = v_servicecode + v_mobile + v_content;
		String v_mac=this.getMd5Info(md5Source, "test");
		//System.out.println(v_mac);
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://api.yizhifubj.com/merchant/ack/serviceSmsApi.jsp");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=GBK");
		NameValuePair[] data = { new NameValuePair("v_servicecode", v_servicecode),// 短信业务编码
				new NameValuePair("v_mobile", v_mobile),// 发送手机号
				new NameValuePair("v_content", v_content),// 发送短信内容
				new NameValuePair("v_smstype", v_smstype),//发送短信类型
				new NameValuePair("v_mac", v_mac)         //数字校验
		                                            
				};
		post.setRequestBody(data);
		
		//发送短信实体
		SmsMobile mobile = new SmsMobile();
		mobile.setV_content(v_content);
		mobile.setV_mobile(v_mobile);
		mobile.setV_servicecode(v_servicecode);
		mobile.setV_smstype(v_smstype);
		mobile.setSendtime(DateUtil.getDateline());
		try {
			client.executeMethod(post);
		} catch (HttpException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		//System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			//System.out.println("---" + h.toString());
		}
		String result = null;
		try {
			result = new String(post.getResponseBodyAsString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	// 校验数字MD5
	@Override
	public String getMd5Info(String text, String key) {
		
		 Md5Sms md5 = new Md5Sms("");
		 String md5Info=null;
	        try {
	            md5.hmac_Md5(text, key);
	            byte b[] = md5.getDigest();
	            md5Info = md5.stringify(b);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return md5Info; 
	}
	
	/*public  void sendMobile( String v_mobile,String v_content1) {
		String v_smstype = "01";
		String v_servicecode="0014";
		String v_content="【绥易通】"+v_content1;	
		String md5Source = v_servicecode + v_mobile + v_content;
		String v_mac=this.getMd5Info(md5Source, "test");
		//System.out.println(v_mac);
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://api.yizhifubj.com/merchant/ack/serviceSmsApi.jsp");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=GBK");
		NameValuePair[] data = { new NameValuePair("v_servicecode", v_servicecode),// 短信业务编码
				new NameValuePair("v_mobile", v_mobile),// 发送手机号
				new NameValuePair("v_content", v_content),// 发送短信内容
				new NameValuePair("v_smstype", v_smstype),//发送短信类型
				new NameValuePair("v_mac", v_mac)         //数字校验
		                                            
				};
		post.setRequestBody(data);
		
		//发送短信实体
		SmsMobile mobile = new SmsMobile();
		mobile.setV_content(v_content);
		mobile.setV_mobile(v_mobile);
		mobile.setV_servicecode(v_servicecode);
		mobile.setV_smstype(v_smstype);
		mobile.setSendtime(DateUtil.getDateline());
		try {
			client.executeMethod(post);
		} catch (HttpException e) {	
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Header[] headers = post.getResponseHeaders();
		int statusCode = post.getStatusCode();
		//System.out.println("statusCode:" + statusCode);
		for (Header h : headers) {
			//System.out.println("---" + h.toString());
		}
		String result = null;
		try {
			result = new String(post.getResponseBodyAsString().getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}*/
	//数字校验MD5
	/*public  String getMd5Info(String text, String key) {
		 Md5Sms md5 = new Md5Sms("");
		 String md5Info=null;
	        try {
	            md5.hmac_Md5(text, key);
	            byte b[] = md5.getDigest();
	            md5Info = md5.stringify(b);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        return md5Info;     
	}*/

}
