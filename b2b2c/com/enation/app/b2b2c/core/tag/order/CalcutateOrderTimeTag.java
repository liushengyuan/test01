package com.enation.app.b2b2c.core.tag.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IOrderManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
public class CalcutateOrderTimeTag extends BaseFreeMarkerTag {
 private IOrderManager orderManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String order_id=params.get("order_id").toString();
		List<Map> list=orderManager.getOrderIsSkill(Integer.parseInt(order_id));
		if(list.size()>0){
			for (Map map : list) {
				Long start_time=(Long) map.get("create_time");
				Long end_time=start_time+1200;
				map.put("start_time", start_time);
				map.put("end_time", end_time);
			}
		}else{
			list=new ArrayList<Map>();
		}
		return list;
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

}
