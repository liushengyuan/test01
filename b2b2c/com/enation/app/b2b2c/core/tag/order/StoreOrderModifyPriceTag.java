package com.enation.app.b2b2c.core.tag.order;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 获取店铺单个订单
 * @author WKZ
 * @date 2015-9-4 上午9:50:18
 */
@Component
public class StoreOrderModifyPriceTag extends BaseFreeMarkerTag{
	
	@Autowired
	private IStoreOrderManager storeOrderManager;
	
	@Autowired
	private IStoreMemberManager storeMemberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		//session中获取会员信息,判断用户是否登录
		StoreMember member=storeMemberManager.getStoreMember();
		if(member==null){
			HttpServletResponse response= ThreadContextHolder.getHttpResponse();
			try {
				response.sendRedirect("login.html");
			} catch (IOException e) {
				throw new UrlNotFoundException();
			}
		}
		StoreOrder order = null;
		String orderSn = params.get("order_sn").toString();
		order=storeOrderManager.get(orderSn);
		return order;
	}
}
