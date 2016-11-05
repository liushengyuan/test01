package com.enation.app.b2b2c.core.service.OrderReport.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.OrderReport.IStoreOrderReportManager;
import com.enation.app.shop.core.model.SellBackList;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.ISellBackManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.framework.database.IDaoSupport;

@Component
public class StoreOrderReportManager implements IStoreOrderReportManager{
	private IDaoSupport daoSupport;
	private ISellBackManager sellBackManager;
	private IOrderManager orderManager;
	@Override
	public void saveAuth(Integer status, Integer id, String seller_remark) {
		String sql = "update es_sellback_list set tradestatus=?,seller_remark=? where id=?";
		this.daoSupport.execute(sql, status,seller_remark,id);
		if(status==4){
			SellBackList sellBackList=sellBackManager.get(id);
			Integer orderid = this.orderManager.get(sellBackList.getOrdersn()).getOrder_id();
			daoSupport.execute("update es_order set status=? where order_id=?",OrderStatus.ORDER_COMPLETE, orderid);
		}
		this.update(id);
	}
	
	public void update(Integer id){
		SellBackList sellBackList= sellBackManager.get(id);
		List<Map> goodsList = this.sellBackManager.getGoodsList(id,sellBackList.getOrdersn());
		for (Map map : goodsList) {
			this.sellBackManager.editStorageNum(id,Integer.parseInt(map.get("goods_id").toString()),Integer.parseInt(map.get("return_num").toString()));//修改入库数量
		}
	}
	public ISellBackManager getSellBackManager() {
		return sellBackManager;
	}

	public void setSellBackManager(ISellBackManager sellBackManager) {
		this.sellBackManager = sellBackManager;
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
}
