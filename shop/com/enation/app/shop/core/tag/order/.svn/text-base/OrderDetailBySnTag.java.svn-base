package com.enation.app.shop.core.tag.order;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class OrderDetailBySnTag extends BaseFreeMarkerTag{
	private IOrderManager orderManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		// TODO Auto-generated method stub
		String ordersn  = params.get("ordersn").toString();
		
		Order order  =null;
		order=	orderManager.get(ordersn);
		return order;
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	

}
