package com.enation.app.tradeease.core.tag.goods;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.tradeease.core.service.goods.ISellerGoodsListManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 卖家中心 产品列表查询标签
 * 
 * @author yanpeng 2015-6-24 下午1:27:29
 * 
 */
@Component
@Scope("prototype")
public class MySellerGoodsListTag extends BaseFreeMarkerTag {
	private ISellerGoodsListManager sellerGoodsListManager;
	private IStoreManager storeManager;
	private IStoreMemberManager storeMemberManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
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
		Integer member_id = storeMember.getMember_id();
		Store store = storeManager.getStoreByMember(member_id);

		Map map = new HashMap();
		String goods_name = request.getParameter("goods_name");
		String goods_cat = request.getParameter("goods_cat");
		String goods_store = request.getParameter("goods_store");
		String deliveryregion = request.getParameter("deliveryregion");
		if (!StringUtil.isEmpty(goods_name)) {
			goods_name = goods_name.trim();
		}
		if (!StringUtil.isEmpty(goods_cat)) {
			goods_cat = goods_cat.trim();
		}
		String audit_state = request.getParameter("audit_state");
		String c = request.getParameter("c");
		String market_enable = request.getParameter("market_enable");
		String audit_status = request.getParameter("audit_status");//获取audit_status
		String sort = request.getParameter("sort");
		int store_id=store.getStore_id();
		map.put("store_id", store_id);
		map.put("goods_name", goods_name);
		map.put("goods_cat", goods_cat);
		map.put("audit_state", audit_state);
		map.put("market_enable", market_enable);
		map.put("audit_status", audit_status);//添加audit_status到map
		map.put("deliveryregion", deliveryregion);
		map.put("goods_store", goods_store);
		map.put("sort", sort);
		map.put("c", c);
		long webPages=this.sellerGoodsListManager.queryListall(store_id);
		long OneAll=this.sellerGoodsListManager.queryListAllOne(store_id);
		long TwoAll=this.sellerGoodsListManager.queryListAllTwo(store_id);
		long Unapprove=this.sellerGoodsListManager.queryListUnapprove(store_id);
		long Out=this.sellerGoodsListManager.queryListOut(store_id);
		long Draft=this.sellerGoodsListManager.queryListDraft(store_id);
		Page webpage = sellerGoodsListManager.queryList(this.getPage(),
				this.getPageSize(), map);
		Long totalCount = webpage.getTotalCount();
		Map result = new HashMap();
		result.put("totalCountSelect", webPages);//所有
		result.put("OneAll",OneAll );//已上架
		result.put("TwoAll", TwoAll);//待审核
		result.put("Unapprove", Unapprove);//审核未通过
		result.put("Out", Out);
		result.put("Draft", Draft);
		result.put("page", this.getPage());
		result.put("pageSize", this.getPageSize());
		result.put("totalCount", totalCount);
		result.put("storeGoodsList", webpage);
		return result;
	}

	public ISellerGoodsListManager getSellerGoodsListManager() {
		return sellerGoodsListManager;
	}

	public void setSellerGoodsListManager(
			ISellerGoodsListManager sellerGoodsListManager) {
		this.sellerGoodsListManager = sellerGoodsListManager;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

}
