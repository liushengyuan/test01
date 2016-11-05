package com.enation.app.tradeease.core.tag.sms;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.tradeease.core.model.sms.SmsMobile;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class SmsmobileListTag extends BaseFreeMarkerTag {
	private IsmsMobileManager smsMobileManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String v_mobile = request.getParameter("v_mobile");
		Page page = this.smsMobileManager.qurerySmsList(this.getPage(), this.getPageSize(), v_mobile);
		//List<SmsMobile> smsList = this.smsMobileManager.qurerySmsList(v_mobile);
		return page;
	}
	public IsmsMobileManager getSmsMobileManager() {
		return smsMobileManager;
	}
	public void setSmsMobileManager(IsmsMobileManager smsMobileManager) {
		this.smsMobileManager = smsMobileManager;
	}

	
}
