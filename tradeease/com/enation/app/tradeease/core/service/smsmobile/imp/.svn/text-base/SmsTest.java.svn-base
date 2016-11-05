package com.enation.app.tradeease.core.service.smsmobile.imp;


/**
 * @Author dengsilinming
 * @Date 2012-9-18
 * 
 */

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.enation.app.shop.component.payment.plugin.payease.Md5;
public class SmsTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String v_servicecode = "0014";
		String v_mobile = "13522163282";
		String v_content = "������ͨ��"+"������Ϣ,����ظ���"; 
		String v_smstype = "01";
		String md5Source = v_servicecode + v_mobile + v_content;
		String v_mac=getMd5Info(md5Source, "test");
		//System.out.println(v_mac);
		HttpClient client = new HttpClient();
		PostMethod post = new PostMethod("http://api.yizhifubj.com/merchant/ack/serviceSmsApi.jsp");
		post.addRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");
		NameValuePair[] data = { new NameValuePair("v_servicecode", v_servicecode),
				new NameValuePair("v_mobile", v_mobile),
				new NameValuePair("v_content", v_content),
				new NameValuePair("v_smstype", v_smstype),
				new NameValuePair("v_mac", v_mac)         
				};
		post.setRequestBody(data);
		//System.out.println(post.getPath());
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
			result = new String(post.getResponseBodyAsString().getBytes(
					"gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String status = result.substring(result.indexOf("<status>")+8, result.indexOf("</status>"));
		if(status.contains("1")){
			//System.out.println("�����쳣");
		}
		//System.out.println("******************************************");
		//System.out.println(result);
		//System.out.println("******************************************");
		post.releaseConnection();
	}
	public static String getMd5Info(String text, String key) {
		 Md5 md5 = new Md5("");
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
}
