package com.enation.app.shop.core.tag;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IBrandManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
/**
 * 
 * @author zmm by 2016-06-09
 *
 */
@Component
@Scope("prototype")
public class BrandFindGoodsTag extends BaseFreeMarkerTag {
	private IBrandManager brandManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer pageSize = (Integer)params.get("pageSize");
		if(pageSize==null) pageSize = this.getPageSize();
		int page=this.getPage();//使支持？号传递
		Integer catid = null;
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		String catId =request.getParameter("catId");
		if(catId==null || StringUtil.isEmpty(catId)){
			catid = null;
		}else{
			catid = Integer.parseInt(catId);
		}
		Page webPage  =  brandManager.queryGoodsbyCatId(page, pageSize,catid);
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
