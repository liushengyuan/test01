package com.enation.app.shop.core.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Tag;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class GoodsRelTag extends BaseFreeMarkerTag{
	private IGoodsManager goodsManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String tag_id=params.get("tag_id").toString();
		Tag tag=this.goodsManager.getTagByTagId(Integer.parseInt(tag_id));
		return tag;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
}
