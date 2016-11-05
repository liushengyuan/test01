package com.enation.app.tradeease.core.tag.accont;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.account.SellerCard;
import com.enation.app.tradeease.core.service.accont.IAccountDetailManager;
import com.enation.app.tradeease.core.service.accont.IMyAccountManager;
import com.enation.app.tradeease.core.service.accont.ISellerCardManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
/**
 * 查询单个银行卡信息
 * @author WKZ
 * @date 2015-8-30 下午4:08:23
 */
@Component
@Scope("prototype")
public class SingleBankCardTag extends BaseFreeMarkerTag {
	
	@Autowired
	private ISellerCardManager sellerCardManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = UserConext.getCurrentMember();
		String card_num = request.getParameter("card_num");
		SellerCard card = sellerCardManager.getSingleBankCard(member.getUname(), card_num);
		return card;
	}

	
	
	

}
