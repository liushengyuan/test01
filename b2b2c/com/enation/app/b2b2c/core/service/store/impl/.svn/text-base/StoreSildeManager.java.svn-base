package com.enation.app.b2b2c.core.service.store.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.model.store.StoreSilde;
import com.enation.app.b2b2c.core.service.store.IStoreSildeManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
@Component
public class StoreSildeManager extends BaseSupport implements IStoreSildeManager {
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreSildeManager#list(java.lang.Integer)
	 */
	@Override
	public List<StoreSilde> list(Integer store_id) {
		//获取幻灯片列表
		String sql="select * from es_store_silde where store_id=?";
		List<StoreSilde> list=this.daoSupport.queryForList(sql, StoreSilde.class, store_id);
		//更替图片路径
		//this.editImg(list);
		return list;
	}
	/**
	 * 修改店铺轮播图
	 * @param list
	 */
	private void editImg(List<StoreSilde> list){
		for (StoreSilde storeSilde : list) {
			storeSilde.setSildeImg( UploadUtil.replacePath(storeSilde.getImg()));
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreSildeManager#edit(java.lang.Integer[], java.lang.String[], java.lang.String[])
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(Integer[] silde_id,String[] fsImg,String[] silde_url) {
		for (int i = 0; i < silde_id.length; i++) {
			StoreSilde storeSilde=new StoreSilde();
			storeSilde.setImg(fsImg[i]);
			storeSilde.setSilde_id(silde_id[i]);
			storeSilde.setSilde_url(silde_url[i]);
			this.editSilde(storeSilde);
		}
		
	}
	/*
	 * 修改店铺轮播图
	 */
	private void editSilde(StoreSilde storeSilde){
		this.daoSupport.update("es_store_silde", storeSilde, "silde_id="+storeSilde.getSilde_id());
	}
	@Override
	public void initializeStoreSilde() {
		// TODO Auto-generated method stub
		List<Store> stores=this.daoSupport.queryForList("select s.store_id from es_store s",Store.class);
		List<StoreSilde> sildeList=new ArrayList<StoreSilde>();
		for(Store store:stores){
			sildeList=this.list(store.getStore_id());
			if(sildeList.size()==0){
				Map map=new HashMap();
				for(int i=0;i<5;i++){
/*					StoreSilde storeSilde=new StoreSilde();
					storeSilde.setStore_id(store.getStore_id());
					storeSilde.setSildeImg("fs:/images/s_side.jpg");
					this.baseDaoSupport.insert("es_store_silde",storeSilde);
					storeSilde.setSilde_id(this.daoSupport.getLastId("es_store_silde"));*/
					
					map.put("store_id", store.getStore_id());
					map.put("img","fs:/images/s_side.jpg");
					this.daoSupport.insert("es_store_silde",map);
				}
			}
		}
		
	}

}
