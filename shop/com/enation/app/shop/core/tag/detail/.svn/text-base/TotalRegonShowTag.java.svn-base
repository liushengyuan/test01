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

import com.enation.app.shop.core.model.RegionUrl;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.tool.IpToCity;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class TotalRegonShowTag extends BaseFreeMarkerTag{
	private ILogiManager logiManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		String ip=request.getRemoteAddr();
		String result=IpToCity.GetAddressByIp(ip);
		RegionUrl regionUrl=null;
		if(result.equalsIgnoreCase("未分配或者内网IP--")){
			regionUrl=new RegionUrl();
		}else if(result.indexOf("--")>0){
			regionUrl=new RegionUrl();
			String[] str=result.split("--");
			regionUrl.setCity(str[1]);
			regionUrl.setPrivince(str[0]);
		}else{
			regionUrl=new RegionUrl();
		}
		return regionUrl;
	}
	public ILogiManager getLogiManager() {
		return logiManager;
	}
	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

}
