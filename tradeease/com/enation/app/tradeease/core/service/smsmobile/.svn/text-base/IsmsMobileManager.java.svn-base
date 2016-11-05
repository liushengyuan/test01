package com.enation.app.tradeease.core.service.smsmobile;

import java.util.List;

import com.enation.app.tradeease.core.model.sms.SmsMobile;
import com.enation.framework.database.Page;

public interface IsmsMobileManager {

	public void sendMobile(String v_mobile,String v_content);
	
	public String getMd5Info(String text,String key);
	
	public void add(SmsMobile sms);

	public List<SmsMobile> qurerySmsList(String v_content);

	public Page qurerySmsList(int page, int pageSize, String v_mobile);
	
}
