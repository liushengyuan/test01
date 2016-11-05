package com.enation.app.tradeease.core.service.cordova.impl;

import java.util.List;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.tradeease.core.service.cordova.IStoreManagerApp;
import com.enation.eop.sdk.database.BaseSupport;


public class StoreManagerApp extends BaseSupport implements IStoreManagerApp {
	/**
	 * 获取店铺详细信息
	 * 
	 */
	@Override
	public List<Store> getStore(Integer store_id) {
		try {
			String sql="select * from es_store where store_id= ?";
		    List<Store> store= this.daoSupport.queryForList(sql, store_id);
			return  store;
		} catch (Exception e) {
			return null;
		}
	}
}
