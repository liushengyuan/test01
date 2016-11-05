package com.enation.app.shop.core.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Logi;
import com.enation.app.shop.core.service.IlogisManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 配送商标签
 * @author lxy
 *2015-11-11
 */
@Component
@Scope("prototype")
public class LogisDetailTag extends BaseFreeMarkerTag {
 private IlogisManager ilogisManager;

@SuppressWarnings("rawtypes")
@Override
protected Object exec(Map params) throws TemplateModelException {
	  List<Logi> logidetail=this.ilogisManager.find();
	return logidetail ;
}

public IlogisManager getIlogisManager() {
	return ilogisManager;
}

public void setIlogisManager(IlogisManager ilogisManager) {
	this.ilogisManager = ilogisManager;
}
 
}
