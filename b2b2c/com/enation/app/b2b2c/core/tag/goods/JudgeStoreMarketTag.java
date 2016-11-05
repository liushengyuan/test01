package com.enation.app.b2b2c.core.tag.goods;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.GoodsType;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.IGoodsTypeManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 获取店铺投放市场
 * @author WKZ
 * @date 2015-9-11 上午10:47:48
 */
@Component
public class JudgeStoreMarketTag extends BaseFreeMarkerTag {
	
	@Autowired
	private IStoreManager storeManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String ctx = this.getRequest().getContextPath();
		if("/".equals(ctx)){
			ctx="";
		}
		
		StoreMember member = storeMemberManager.getStoreMember();
		if (member == null) {
			HttpServletResponse response = ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect(ctx+"/store/login.html");
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
		String storeMarket = storeManager.getStoreMarket(member.getStore_id());
		storeMarket = storeMarket == null ? "RUS" : storeMarket;//默认是投向俄罗斯市场
		return storeMarket;
	}
}
