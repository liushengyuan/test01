package com.enation.app.b2b2c.core.model.order;

import com.enation.app.shop.core.model.SellBackList;
/**
 * 店铺退货单
 * @author fenlongli
 *
 */
public class StoreSellBackList extends SellBackList{

	public Integer store_id;	//店铺Id
	public String refund_reason;//退货原因

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public String getRefund_reason() {
		return refund_reason;
	}

	public void setRefund_reason(String refund_reason) {
		this.refund_reason = refund_reason;
	}
	
	
}
