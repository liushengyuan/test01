package com.enation.app.tradeease.core.service.cordova;

import java.util.List;

import com.enation.app.b2b2c.core.model.store.Store;

public interface IStoreManagerApp {
	public List<Store> getStore(Integer store_id);
}
