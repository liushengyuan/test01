package com.enation.app.shop.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.RowMapper;

import com.enation.app.b2b2c.core.model.StoreProduct;
import com.enation.app.shop.core.model.GoodsCatSpec;
import com.enation.app.shop.core.model.GoodsStores;
import com.enation.app.shop.core.service.IGoodsCatSpecManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

/**
 * 商品分类和商品规格业务实现
 * 
 * @author liujisong 
 * edited by liujisong
 */
public class GoodsCatSpecManager extends BaseSupport implements IGoodsCatSpecManager {

	@Override
	public void saveAdd(GoodsCatSpec catSpec) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.insert("goods_type_spec", catSpec);
		Integer id = this.baseDaoSupport.getLastId("goods_type_spec");
		catSpec.setId(id);
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		String sql = "delete  from goods_type_spec  where id ="+id;
		this.baseDaoSupport.execute(sql);
	}

	@Override
	public void delete(Integer cat_id, Integer spec_id) {
		// TODO Auto-generated method stub
		String sql = "delete  from goods_type_spec  where cat_id ="+cat_id+" and spec_id="+spec_id;
		this.baseDaoSupport.execute(sql);
	}

	@Override
	public Page findByCatId(Integer cat_id,int page,int pageSize) {
		// TODO Auto-generated method stub
		String sql = "select l.id,l.cat_id,l.spec_id,s.spec_name from "
				+ this.getTableName("goods_type_spec")
				+ " l left join "
				+ this.getTableName("specification")
				+ " s on l.spec_id=s.spec_id where cat_id="+cat_id; 
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}

	@Override
	public List<GoodsCatSpec> findSpecList(Integer cat_id) {
		// TODO Auto-generated method stub
		String sql ="select * from es_goods_type_spec where cat_id=?";
		List<GoodsCatSpec> catSpecList  =this.daoSupport.queryForList(sql, GoodsCatSpec.class, cat_id);
		return catSpecList;
	}

}
