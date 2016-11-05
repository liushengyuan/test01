package com.enation.app.b2b2c.core.tag.store;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;



import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
public class StoreWithdrawalDetailTag extends BaseFreeMarkerTag{
	
	private IStoreManager storeManager;
	private IStoreMemberManager storeMemberManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		String page = request.getParameter("page");
		page = (page == null || page.equals("")) ? "1" : page;
		int pageSize=10;
		Member member = UserConext.getCurrentMember();
		Map map=new HashMap();
		StoreMember storeMember = this.storeMemberManager.getStoreMember();
		Page List = this.storeManager.getWithdrawalList(Integer.parseInt(page), pageSize, map, storeMember.getStore_id());
	Long totalCount = List.getTotalCount();
		
		map.put("page", page);
		map.put("pageSize", pageSize);
		map.put("totalCount", totalCount);
		map.put("List", List);
		return map;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
}
