package com.enation.app.b2b2c.core.model.order;

import com.enation.app.shop.core.model.Order;
import com.enation.framework.database.NotDbField;

/**
 * 店铺订单
 * @author LiFenLong
 *
 */
public class StoreOrder extends Order{
	private Integer store_id;//店铺Id
	private Integer parent_id;//订单父类Id
	private String [] storeids;
	private Double commission;	//订单佣金价格
	private String store_name;
	
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public Integer getParent_id() {
		return parent_id;
	}
	public void setParent_id(Integer parent_id) {
		this.parent_id = parent_id;
	}
	@NotDbField
	public String getOrderType() {
		return "b";
	}
	@NotDbField
	public String[] getStoreids() {
		return storeids;
	}
	public void setStoreids(String[] storeids) {
		this.storeids = storeids;
	}
	@NotDbField
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	
}
