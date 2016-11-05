package com.enation.app.shop.component.gift.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.component.bonus.service.BonusSession;
import com.enation.app.shop.component.gift.model.Gift;
import com.enation.app.shop.component.gift.service.IGiftManager;

import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;


@Component
@Scope("prototype")
public class GiftListTag extends BaseFreeMarkerTag{
	private IGiftManager giftManager;

	@SuppressWarnings({ "rawtypes" })
	@Override
	public Object exec(Map params) throws TemplateModelException {
		@SuppressWarnings("unused")
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			throw new TemplateModelException("未登录不能使用此标签");
		}
		List<Gift> gift  = giftManager.getGift();
		return gift;
	}

	public IGiftManager getGiftManager() {
		return giftManager;
	}

	public void setGiftManager(IGiftManager giftManager) {
		this.giftManager = giftManager;
	}
}
