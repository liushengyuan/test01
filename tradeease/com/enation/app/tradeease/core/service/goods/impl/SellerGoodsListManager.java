package com.enation.app.tradeease.core.service.goods.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.service.goods.ISellerGoodsListManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 卖家中心 产品列表查询
 * 
 * @author yanpeng 2015-6-24 下午1:27:29
 * 
 */

@SuppressWarnings({ "rawtypes" })
public class SellerGoodsListManager extends BaseSupport implements
		ISellerGoodsListManager {

	@Override
	public Page queryList(int pageNo, int pageSize, Map map) {
		Integer store_id = (Integer) map.get("store_id");
		String goods_name = (String) map.get("goods_name");
		String goods_cat = (String) map.get("goods_cat");
		String audit_state = (String) map.get("audit_state");
		String market_enable = (String) map.get("market_enable");
		String goods_store = (String) map.get("goods_store");
		String sort = (String) map.get("sort");
		String c=(String)map.get("c");
		String deliveryregion = (String) map.get("deliveryregion");
		StringBuffer sql = new StringBuffer(
				//添加判断字段不显示隐藏商品(market_enable和audit_status同时为4或5)
				" SELECT g.goods_id, g.is_belong, g.deliveryregion, g.cat_id, g.`name`, g.name_ru, g.buy_count, g.audit_status, g.market_enable, g.enable_store, g.expire_time, g.thumbnail, g.audit_discribe FROM es_goods g LEFT JOIN es_goods_cat c ON g.cat_id = c.cat_id WHERE g.store_id = ? AND audit_status NOT IN (4,5) AND market_enable NOT IN (4,5) ");
	//" SELECT  g.* from es_goods g left join es_goods_cat c on g.cat_id = c.cat_id where g.store_id= ? ");
		if (!StringUtil.isEmpty(goods_name)) {
			sql.append(" and g.name like '%" + goods_name + "%'");
		}
		if (!StringUtil.isEmpty(goods_cat)) {
			sql.append(" and c.name like '%" + goods_cat + "%' ");
		}
		if (!StringUtil.isEmpty(audit_state)) {
			sql.append(" and g.audit_status = " + Integer.valueOf(audit_state));
		}
		if (!StringUtil.isEmpty(goods_store)) {
			sql.append(" and g.enable_store <= " + goods_store );
		}
		if (!StringUtil.isEmpty(c)) {
		if(Integer.parseInt(c)==0){
			sql.append(" and g.goods_id in(select g.goods_id from es_goods g WHERE g.enable_store=(SELECT SUM(p.enable_store) from es_product p WHERE p.goods_id=g.goods_id))" );
	     }else if(Integer.parseInt(c)==1){
	    	 sql.append(" and g.goods_id in(select g.goods_id from es_goods g WHERE g.enable_store!=(SELECT SUM(p.enable_store) from es_product p WHERE p.goods_id=g.goods_id))" );
	     }else{
	    	 sql.append("");
	     }
		}
//		if(c==0){
//			sql.append(" and g.goods_id in(select g.goods_id from es_goods g WHERE g.enable_store!=(SELECT SUM(p.enable_store) from es_product p WHERE p.goods_id=g.goods_id))" );
//		}
		if (!StringUtil.isEmpty(market_enable)) {
			sql.append(" and g.market_enable = "
					+ Integer.valueOf(market_enable));
			if(market_enable.equals("-1")){  //判断是否为草稿 是则执行
				sql.append(" and g.audit_status = 0");
			}
		}
		if (!StringUtil.isEmpty(deliveryregion)) {
			if(Integer.valueOf(deliveryregion)!=8){
				sql.append(" and g.deliveryregion = "
						+ Integer.valueOf(deliveryregion));
			}
		}
		if (!StringUtil.isEmpty(sort)){
			sql.append(sort);
			sql.append(",g.create_time desc ");
		}else{
			sql.append(" order by g.create_time desc ");
		}
		Page webpage = this.daoSupport.queryForPage(sql.toString(), pageNo,
				pageSize, store_id);
		return webpage;
	}
    //查询全部
	@Override
	public long queryListall(int store_id) {
	//显示所有商品总数不显示隐藏商品
	String sql=" SELECT count(1) from es_goods g left join es_goods_cat c on g.cat_id = c.cat_id where g.store_id= ? and market_enable NOT IN (4,5) and audit_status NOT IN (4,5) order by g.create_time desc";
	long wePages=this.baseDaoSupport.queryForLong(sql,store_id);	
	return wePages;
	}

//根据market_enable进行查询
	public long queryListAllOne(int store_id) {
	String sql=" SELECT count(1) from es_goods g left join es_goods_cat c on g.cat_id = c.cat_id where g.store_id= ? and g.market_enable=1 and g.audit_status=1 order by g.create_time desc";
	long wePages=this.baseDaoSupport.queryForLong(sql,store_id);	
	return wePages;
	}
	//根据audit_state查询
	@Override
	public long queryListAllTwo(int store_id) {
		String sql=" SELECT count(1) from es_goods g left join es_goods_cat c on g.cat_id = c.cat_id where g.store_id= ? and g.audit_status=0 and g.market_enable = 2 order by g.create_time desc";
		long wePages=this.baseDaoSupport.queryForLong(sql,store_id);	
		return wePages;
	}
	//查询审核未通过
	@Override
	public long queryListUnapprove(int store_id) {
		String sql=" SELECT count(1) from es_goods g left join es_goods_cat c on g.cat_id = c.cat_id where g.store_id= ? and g.audit_status=2 and g.market_enable=-1 order by g.create_time desc";
		long wePages=this.baseDaoSupport.queryForLong(sql,store_id);	
		return wePages;
	}
	//已下架
	@Override
	public long queryListOut(int store_id) {
		String sql=" SELECT count(1) from es_goods g left join es_goods_cat c on g.cat_id = c.cat_id where g.store_id= ? and g.market_enable = 0 and g.audit_status=1 order by g.create_time desc";
		long wePages=this.baseDaoSupport.queryForLong(sql,store_id);	
		return wePages;
	}
	//草稿
	@Override
	public long queryListDraft(int store_id) {
		String sql=" SELECT count(1) from es_goods g left join es_goods_cat c on g.cat_id = c.cat_id where g.store_id= ? and g.market_enable = -1 and g.audit_status=0 order by g.create_time desc";
		long wePages=this.baseDaoSupport.queryForLong(sql,store_id);	
		return wePages;
		
	}
	

}
