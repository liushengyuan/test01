package com.enation.app.tradeease.core.tag.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.tradeease.core.model.message.MessageCenter;
import com.enation.app.tradeease.core.model.message.MessageFlag;
import com.enation.app.tradeease.core.service.message.IBuyerMessageManager;
import com.enation.app.tradeease.core.service.message.ISellerMessageManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 买家中心站内信 收到的消息标签
 * 
 * @author yanpeng 2015-6-12 下午6:17:17
 * 
 */
@Component
@Scope("prototype")
public class AcceptBuyerTag extends BaseFreeMarkerTag {
	private ISellerMessageManager sellerMessageManager;
	private IStoreMemberManager storeMemberManager;
	private IStoreManager storeManager;
	private IOrderManager orderManager;
	private IMemberManager memberManager;
	private IAdminUserManager adminUserManager;

	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		StoreMember storeMember = storeMemberManager.getStoreMember();
		Integer member_id = storeMember.getMember_id();
		// 根据会员ID获得店铺
		Store store = storeManager.getStoreByMember(member_id);
		// 根据店铺ID获得相应的买家
		List<Member> members = orderManager.listMemberByStoreId(store.getStore_id());
		return members;
		
	}

	public ISellerMessageManager getSellerMessageManager() {
		return sellerMessageManager;
	}

	public void setSellerMessageManager(ISellerMessageManager sellerMessageManager) {
		this.sellerMessageManager = sellerMessageManager;
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

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	

}
