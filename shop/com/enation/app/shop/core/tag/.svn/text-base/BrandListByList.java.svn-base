package com.enation.app.shop.core.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IBrandManager;
import com.enation.app.shop.core.service.IGoodsSearchManager;
import com.enation.app.shop.core.service.SearchEngineFactory;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BrandListByList extends BaseFreeMarkerTag {
	private IBrandManager brandManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String tagid = (String)params.get("tagid");
		String goodsnum = (String)params.get("goodsnum");
		List goodsList  = brandManager.listGoods(tagid, goodsnum);
		return goodsList;
	}
	public IBrandManager getBrandManager() {
		return brandManager;
	}
	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}

}
