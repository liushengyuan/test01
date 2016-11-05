package com.enation.app.b2b2c.core.tag.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
public class StoreOrderChooseTag extends BaseFreeMarkerTag {
	private IStoreOrderManager storeOrderManager;
	private ILogiManager logiManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		
		    Map<String, Object> map=new HashMap<String, Object>();
		     
		     String sn=String.valueOf(params.get("sn"));
		     Order order=this.storeOrderManager.getOrder(sn);
		     List<OrderItem> listOrderItem=this.storeOrderManager.getOrderItem(order.getOrder_id());
		     for (OrderItem orderItem : listOrderItem) {
				Integer freight_id=orderItem.getFreight_id();
				if(freight_id==null){
					freight_id=0;
				}
				if(freight_id==0 ){
					map.put("0", "0");
				}else{
					FreightStandard freightStandard=this.logiManager.getFreightById(freight_id);
					if(freightStandard!=null){
						map.put("product_name", freightStandard.getProduct_name());
					}
				}
		     }
		     
		      String sum="0";
		      List<String> list=new  ArrayList<String>();
		      for (Map.Entry<String, Object>  s : map.entrySet()) {
				  String key=s.getKey();
				  String value=(String) s.getValue();
				  if(key.contains("product_name")){
					  list.add(value);
				  }
				       sum+=value;
				  
			}
		      String result="";
		      if(sum.equals("0")){
		    	  result="0";
		      }else{
		    	  if(list.size()!=0){
		    		 result= list.get(0);
		    	  }
		    	  
		      }
		return result;
	}
	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}
	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}
	public ILogiManager getLogiManager() {
		return logiManager;
	}
	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

}
