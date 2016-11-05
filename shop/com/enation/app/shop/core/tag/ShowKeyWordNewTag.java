package com.enation.app.shop.core.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.MetalKeywordsLogs;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class ShowKeyWordNewTag extends BaseFreeMarkerTag{
	private IGoodsManager goodsManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id=Integer.parseInt(params.get("goods_id").toString());
	     MetalKeywordsLogs metalKeywordsLogs=	this.goodsManager.showMetalKeywords(goods_id);
		return metalKeywordsLogs;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

}
