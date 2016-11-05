package com.enation.app.b2b2c.core.tag.store;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.SellBackList;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.ISellBackManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 退货申请标签
 * @author fenlongli
 *
 */
@Component
public class WithdrawalTag extends BaseFreeMarkerTag {
	private IStoreManager storeManager;
	public IStoreManager getStoreManager() {
		return storeManager;
	}
	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		int pageSize=10;
		
		Map map=new HashMap();
		
	//	Page List = this.storeManager.getWithdrawalListAll(Integer.parseInt(page), pageSize, map);
	//Long totalCount = List.getTotalCount();
		
		map.put("page", page);
		map.put("pageSize", pageSize);
	//	map.put("totalCount", totalCount);
	//	map.put("List", List);
		return map;
	}
	
}
