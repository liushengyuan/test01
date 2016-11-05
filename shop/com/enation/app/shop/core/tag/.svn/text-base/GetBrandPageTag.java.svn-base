package com.enation.app.shop.core.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IBrandManager;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class GetBrandPageTag extends BaseFreeMarkerTag{
	private IBrandManager brandManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer pageSize = (Integer)params.get("pageSize");
		if(pageSize==null) pageSize = this.getPageSize();
		int page=this.getPage();//使支持？号传递
		Page webPage  =  brandManager.searchBrand(page, pageSize);
		webPage.setCurrentPageNo(page);
		return webPage;
		
	}

	public IBrandManager getBrandManager() {
		return brandManager;
	}

	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}
}
