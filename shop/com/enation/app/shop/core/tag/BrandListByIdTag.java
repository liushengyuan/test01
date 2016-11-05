package com.enation.app.shop.core.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Attration;
import com.enation.app.shop.core.model.Brand;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class BrandListByIdTag extends BaseFreeMarkerTag {
	private IBrandManager brandManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id = (Integer.parseInt(params.get("goods_id").toString()));
		Brand brand=brandManager.getGoodBrand(goods_id);
		Attration attention = null;
		if(brand!=null){
			attention = this.brandManager.querymemberbrand(brand.getBrand_id());
		}
		Map map = new HashMap();
		map.put("brand", brand);
		map.put("attention", attention);
		return map;
	}
	public IBrandManager getBrandManager() {
		return brandManager;
	}
	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}

}
