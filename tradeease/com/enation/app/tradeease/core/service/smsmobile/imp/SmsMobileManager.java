package com.enation.app.tradeease.core.service.smsmobile.imp;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.stereotype.Component;

import com.enation.app.shop.component.payment.plugin.payease.Md5Sms;
import com.enation.app.tradeease.core.model.sms.SmsMobile;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
@Component
@SuppressWarnings("rawtypes")
public class SmsMobileManager extends BaseSupport implements
		IsmsMobileManager {
	
	@Override
	public void sendMobile( String v_mobile,String v_content1) {
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
		String status = result.substring(result.indexOf("<status>")+8, result.indexOf("</status>"));
		if(status.contains("1")){
			//System.out.println("发送失败");
			mobile.setV_status("发送失败");
		}else{
			//System.out.println("发送成功");
			mobile.setV_status("发送成功");
		}
		this.add(mobile);
		//System.out.println("******************************************");
		//System.out.println(result);
		//System.out.println("******************************************");
		post.releaseConnection();
		
	}


	/**
	 * 数字校验MD5
	 */
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
	
	
	/**
	 * 添加短信实体
	 */
	@Override
	public void add(SmsMobile sms){
		this.baseDaoSupport.insert("es_smsmobile", sms);
	}
	@Override
	public List<SmsMobile> qurerySmsList(String v_mobile){
		StringBuffer sql = new StringBuffer();
		if(v_mobile==null){
			sql.append(" SELECT * from es_smsmobile ");
		}else{
			sql.append(" SELECT * from es_smsmobile where v_mobile like '%" +v_mobile+ "%'");
		}
		sql.append(" order by sendtime desc ");
		List<SmsMobile> smsList = this.baseDaoSupport.queryForList(sql.toString());
		return smsList;
	}
	@Override
	public Page qurerySmsList(int page, int pageSize, String v_mobile) {
		StringBuffer sql = new StringBuffer();
		if(v_mobile==null){
			sql.append(" SELECT * from es_smsmobile ");
		}else{
			sql.append(" SELECT * from es_smsmobile where v_mobile like '%" +v_mobile+ "%'");
		}
		sql.append(" order by sendtime desc ");
		Page smspage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		return smspage;
	}
}
