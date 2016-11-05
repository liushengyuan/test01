package com.enation.app.shop.core.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Brand;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class GetBrandByBrandMemberTag extends BaseFreeMarkerTag{
	private IBrandManager brandManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer brand_id = (Integer.parseInt(params.get("brand_id").toString()));
		  Brand brand=brandManager.get(brand_id);
		return brand;
	}

	public IBrandManager getBrandManager() {
		return brandManager;
	}

	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}
}
