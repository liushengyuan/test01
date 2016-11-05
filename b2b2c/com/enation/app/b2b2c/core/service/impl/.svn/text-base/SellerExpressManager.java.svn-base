package com.enation.app.b2b2c.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.SellerExpress;
import com.enation.app.b2b2c.core.service.ISellerExpressManager;
import com.enation.eop.sdk.database.BaseSupport;

/**
 * 配送方式
 * @author xulipeng
 *
 */

@Component
public class SellerExpressManager extends BaseSupport<SellerExpress> implements ISellerExpressManager {

	@Override
	public List<SellerExpress> list(String store_id) {
		String sql = "SELECT * FROM seller_express where express_storeid=? ";
		return this.baseDaoSupport.queryForList(sql, SellerExpress.class,store_id);
	}

	@Override
	public void add(SellerExpress express) {
		this.baseDaoSupport.insert("seller_express", express);
	}

	@Override
	public void delete(Integer id) {
		String sql = "DELETE FROM seller_express WHERE express_id = ?";
		this.baseDaoSupport.execute(sql, id);
	}

	@Override
	public void update(SellerExpress express) {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("express_name", express.getExpress_name());
		fields.put("express_deliver_time", express.getExpress_deliver_time());
		
		this.baseDaoSupport.update("seller_express", fields, " express_id = " + express.getExpress_id());
	}

	@Override
	public SellerExpress getSingle(Integer id) {
		String sql = "SELECT * FROM seller_express WHERE express_id = ?";
		return this.baseDaoSupport.queryForObject(sql, SellerExpress.class, id);
	}

	@Override
	public Integer check(String express_name, int storeid) {
		String sql = "SELECT COUNT(*) FROM es_seller_express e WHERE  e.express_name = '"+express_name+"' AND e.express_storeid = ? ";
		return this.baseDaoSupport.queryForInt(sql, storeid);
	}

	
	

}
