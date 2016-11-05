package com.enation.app.shop.core.tag.detail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ValidateAccessTag extends BaseFreeMarkerTag {
	private ILogiManager logiManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		FreightStandard freightStandard=null;
		if(logiManager.getFreightByIdByDays().size()>0){
			freightStandard=logiManager.getFreightByIdByDays().get(0);
		}else{
			freightStandard=new FreightStandard();
		}
		return freightStandard;
	}

	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	} 
}
