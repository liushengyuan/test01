package com.enation.app.b2b2c.core.tag.goods;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
public class FindOtherLikeGoodsTag extends BaseFreeMarkerTag{
	private IGoodsCatManager goodsCatManager ;
	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}
	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String cat_id=params.get("catid").toString();
		List<Goods> goods=this.goodsCatManager.otherLookGoods(Integer.parseInt(cat_id));
		return goods;
	}

}
