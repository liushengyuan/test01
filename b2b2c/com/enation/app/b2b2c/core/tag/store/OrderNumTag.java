package com.enation.app.b2b2c.core.tag.store;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx.Snapshot;

import freemarker.template.TemplateModelException;



/**
 * 获得该会员订单对应的积分情况
 * @author jfw
 *
 */
@Component
@Scope("prototype")
public class OrderNumTag extends BaseFreeMarkerTag{
	
	private  IOrderManager  orderManager;
	
	@Override
	public Object exec(Map params) throws TemplateModelException {
		Integer  order_id=(Integer) params.get("order_id");
		if (order_id==0) {
			return  "-";
		}
		Order  order  = orderManager.get(order_id);
		return order;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	
}