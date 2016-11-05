package com.enation.app.b2b2c.core.service.store.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.plugin.store.StorePluginBundle;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.model.store.StoreFeature;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreFeatureManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.b2b2c.core.service.store.IStoreSildeManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
@Component
public class StoreFeatureManager  extends BaseSupport implements IStoreFeatureManager{
	
	@Override
	public void add(StoreFeature feature) {
		// TODO Auto-generated method stub
		this.daoSupport.insert("es_store_feature", feature);
		//feature.setStore_id(this.daoSupport.getLastId("es_store_feature"));
		
	}

	@Override
	public void edit(StoreFeature feature) {
		// TODO Auto-generated method stub
		this.daoSupport.update("es_store_feature", feature, "id="+feature.getId());
	}

	@Override
	public StoreFeature get(Integer id) {
		// TODO Auto-generated method stub
		String sql="select * from es_store_feature where id="+id;
		List<StoreFeature> list = this.baseDaoSupport.queryForList(sql,StoreFeature.class);
		StoreFeature feature = (StoreFeature) list.get(0);
		return feature;
	}

	@Override
	public void deleteAll(Store store) {
		// TODO Auto-generated method stub
		String sql = "delete from es_store_feature where store_id=?";
		this.daoSupport.execute(sql, store.getStore_id());
	}

	@Override
	public List<StoreFeature> getAll(Integer id) {
		// TODO Auto-generated method stub
		String sql="select * from es_store_feature where store_id="+id;
		List<StoreFeature> list = this.baseDaoSupport.queryForList(sql,StoreFeature.class);
		return list;
	}
	
	
}
