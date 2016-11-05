package com.enation.app.shop.core.tag.detail;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class SearchLogisNameTag extends BaseFreeMarkerTag {
	private static ResourceBundle bundle = ResourceBundle.getBundle("wuliuchanpin");
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String name=params.get("product").toString();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String nameproduct="";
		if(language=="zh"){
			nameproduct= bundle.getString(name);
		}
		return nameproduct;
	}

}
