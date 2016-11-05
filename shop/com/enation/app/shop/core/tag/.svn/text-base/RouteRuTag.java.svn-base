package com.enation.app.shop.core.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.AdIndex;
import com.enation.app.base.core.service.IAdIndexManager;
import com.enation.app.shop.core.service.IRouteManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 首页大屏广告
 * @author WKZ
 * @date 2015-9-2 下午2:14:08
 */
@Component
@Scope("prototype")
public class RouteRuTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IRouteManager routeManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String   mailno=(String)params.get("mailno").toString();
		List  list = null;
		if (!StringUtil.isEmpty(mailno)) {
		 list =this.routeManager.getListru(mailno);
		 list = list==null?new ArrayList() : list;
		}
		return list;
	}

	public IRouteManager getRouteManager() {
		return routeManager;
	}

	public void setRouteManager(IRouteManager routeManager) {
		this.routeManager = routeManager;
	}
	
	
	 
}
