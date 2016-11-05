package com.enation.app.tradeease.core.tag.accont;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.tradeease.core.service.accont.ISellerCardManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 卖家中心 银行卡账户查询标签
 * 
 * @author yanpeng 2015-6-24 下午1:27:29
 * 
 */
@Component
@Scope("prototype")
public class MyWithdrawAccountListTag extends BaseFreeMarkerTag {
	private IStoreMemberManager storeMemberManager;
	private ISellerCardManager sellerCardManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		StoreMember storeMember = storeMemberManager.getStoreMember();
		if (storeMember == null) {
			HttpServletResponse response = ThreadContextHolder
					.getHttpResponse();
			try {
				response.sendRedirect("login.html");
			} catch (IOException e) {
				throw new UrlNotFoundException();
			}
		}
		Integer storeId = storeMember.getStore_id();
		Page page = sellerCardManager.list(storeId, null, null, this.getPage(),
				this.getPageSize());
		return page;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public ISellerCardManager getSellerCardManager() {
		return sellerCardManager;
	}

	public void setSellerCardManager(ISellerCardManager sellerCardManager) {
		this.sellerCardManager = sellerCardManager;
	}

}
