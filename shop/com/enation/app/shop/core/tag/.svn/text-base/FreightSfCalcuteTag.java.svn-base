package com.enation.app.shop.core.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class FreightSfCalcuteTag extends BaseFreeMarkerTag{
	 private ILogiManager logiManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		FreightStandard freightStandard=this.logiManager.getFreightStanddard(1);
		return freightStandard;
	}

	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}
}
