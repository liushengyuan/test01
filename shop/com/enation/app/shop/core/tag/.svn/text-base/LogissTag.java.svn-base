package com.enation.app.shop.core.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.CustomerLogi;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.service.ILogiManager;

import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class LogissTag extends BaseFreeMarkerTag {
 private ILogiManager logiManager;
 

	public ILogiManager getLogiManager() {
	return logiManager;
}


public void setLogiManager(ILogiManager logiManager) {
	this.logiManager = logiManager;
}


	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List<CustomerLogi> loogis=this.logiManager.getLogiModel();
		return loogis;
	}

}
