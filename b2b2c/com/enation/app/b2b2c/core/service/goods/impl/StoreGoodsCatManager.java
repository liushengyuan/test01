package com.enation.app.b2b2c.core.service.goods.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.store.StoreCat;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsCatManager;
import com.enation.eop.sdk.database.BaseSupport;


@Component
public class StoreGoodsCatManager extends BaseSupport implements IStoreGoodsCatManager{

	
	@Override
	public List storeCatList(Integer storeId) {
		String sql = "select * from es_store_cat where store_id="+storeId;
		List list = this.baseDaoSupport.queryForList(sql);
		return list;
	}

	@Override
	public void addStoreCat(StoreCat storeCat) {
		if(storeCat.getStore_cat_pid()==null){
			storeCat.setStore_cat_pid(0);
		}
		this.baseDaoSupport.insert("es_store_cat", storeCat);
	}

	@Override
	public void editStoreCat(StoreCat storeCat) {
		this.baseDaoSupport.update("es_store_cat", storeCat, " store_cat_id="+storeCat.getStore_cat_id());
	}

	@Override
	public void deleteStoreCat(Integer storeCatId) {
		Integer num = this.baseDaoSupport.queryForInt("select count(0) from es_store_cat s where s.store_cat_pid="+storeCatId);
		if(num>=1){
			throw new RuntimeException("删除失败，分类*有下级分类！");
		}
		
		int goodsnum = this.baseDaoSupport.queryForInt("select count(0) from es_goods where store_cat_id=?", storeCatId);
		if(goodsnum>=1){
			throw new RuntimeException("删除失败，分类*下有商品！");
		}
		
		String sql="delete from es_store_cat where store_cat_id="+storeCatId;
		this.baseDaoSupport.execute(sql);
	}

	@Override
	public List getStoreCatList(Integer catid,Integer storeId) {
		String sql = "select * from es_store_cat where store_cat_pid=0 and store_id="+storeId;
		if(catid!=null){
			sql+=" and store_cat_id!="+catid;
		}
		return this.baseDaoSupport.queryForList(sql);
	}

	@Override
	public StoreCat getStoreCat(Map map) {
		Integer storeid = (Integer) map.get("storeid");
		Integer strore_catid = (Integer) map.get("store_catid");
		String sql = "select * from es_store_cat where store_id=? and store_cat_id=?";
		List<StoreCat> list = this.baseDaoSupport.queryForList(sql,StoreCat.class,storeid,strore_catid);
		StoreCat storeCat =new StoreCat();
		if(!list.isEmpty()){
			storeCat= (StoreCat) list.get(0);
		}
		return storeCat;
	}

	@Override
	public Integer is_children(Integer catid) {
		String sql ="select store_cat_pid from es_store_cat where store_cat_id=?";
		Integer pid=this.baseDaoSupport.queryForInt(sql, catid);
		return pid;
	}

}
