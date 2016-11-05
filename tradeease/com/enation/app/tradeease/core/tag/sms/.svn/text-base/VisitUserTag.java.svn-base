package com.enation.app.tradeease.core.tag.sms;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.tradeease.core.model.sms.SmsMobile;
import com.enation.app.tradeease.core.model.sms.VistUserCount;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
@SuppressWarnings("rawtypes")
public class VisitUserTag extends BaseFreeMarkerTag {

	private IGoodsManager goodsManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String visit_time = request.getParameter("visit_time");
		Page page = this.goodsManager.queryVistUser(this.getPage(), this.getPageSize(), visit_time);
		//List<VistUserCount> visitlist = this.goodsManager.queryVistUser(visit_time);
		return page;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	

}
