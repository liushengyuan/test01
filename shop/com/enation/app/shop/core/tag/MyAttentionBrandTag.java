package com.enation.app.shop.core.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Attration;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MyAttentionBrandTag extends BaseFreeMarkerTag{
	private IBrandManager brandManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List<Attration> list=brandManager.getAtteationList();
		return list;
	}

	public IBrandManager getBrandManager() {
		return brandManager;
	}

	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}
}
