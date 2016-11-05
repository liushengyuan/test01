package com.enation.app.shop.component.largeorder.service.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.component.largeorder.model.BigOrder;
import com.enation.app.shop.component.largeorder.service.ILargeOrderManager;
import com.enation.app.shop.core.model.LargeOrder;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

@Component
public class LargeOrderManager extends BaseSupport implements ILargeOrderManager{

	@Override
	public void add(LargeOrder largeOrder) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.insert("es_large_order", largeOrder);
	}

	@Override
	public Page listMind(Map mindMap, int page, int pageSize, String sort,
			String order) {
		// TODO Auto-generated method stub
		String sql = createTempSql(mindMap, sort, order);
		Page webPage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webPage;
	}
	private String createTempSql(Map map, String other, String order) {

		
		String keyword = (String) map.get("keyword");
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		Integer status = (Integer) map.get("status");
		String store_name = (String) map.get("store_name");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM es_large_order l WHERE 1=1 ");

		if (keyword != null && !StringUtil.isEmpty(keyword)) {
			//sql.append(" and (l.mind_id = "+ keyword );
			sql.append(" and (l.mind_id = '" + keyword + "'");
			sql.append(" or l.buyer_name like '%" + keyword + "%')");
		}

		if (status != null ) {
			sql.append("and status=" + status);
		}


		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil
					.getDateline(start_time + " 00:00:00");
			sql.append(" and l.request_time>" + stime);
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil
					.getDateline(end_time + " 23:59:59");
			sql.append(" and l.request_time<" + etime);
		}
		

		if (!StringUtil.isEmpty(store_name)) {
			sql.append(" and l.store_name like '%"+store_name+"%'");
		}
		
		sql.append(" ORDER BY " + other + " " + order);

		////System.out.println(sql.toString());
		return sql.toString();
	}

	@Override
	public LargeOrder getLargeOrder(Integer mind_id) {
		String sql= "SELECT * FROM es_large_order WHERE mind_id =?";
		return (LargeOrder) this.baseDaoSupport.queryForObject(sql,LargeOrder.class,mind_id);
	}

	@Override
	public void update(LargeOrder largeOrder) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.update("es_large_order", largeOrder," mind_id="+largeOrder.getMind_id());
	}

	@Override
	public Page listOrder(Map orderMap, int page, int pageSize, String sort,
			String order) {
		// TODO Auto-generated method stub
		String sql = createTempOrderSql(orderMap, sort, order);
		Page webPage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webPage;
	}
	private String createTempOrderSql(Map map, String other, String order) {

		
		String keyword = (String) map.get("keyword");
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		Integer status = (Integer) map.get("status");
		String store_name = (String) map.get("store_name");
		
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * FROM es_big_order b WHERE 1=1 ");

		if (keyword != null && !StringUtil.isEmpty(keyword)) {
			//sql.append(" and (l.mind_id = "+ keyword );
			sql.append(" and (b.sn like '%" + keyword + "%'");
			sql.append(" or b.ship_name like '%" + keyword + "%')");
		}

		if (status != null ) {
			sql.append("and b.status=" + status);
		}


		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil
					.getDateline(start_time + " 00:00:00");
			sql.append(" and b.create_time>" + stime);
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil
					.getDateline(end_time + " 23:59:59");
			sql.append(" and b.create_time<" + etime);
		}
		

		if (!StringUtil.isEmpty(store_name)) {
			sql.append(" and b.store_name like '%"+store_name+"%'");
		}
		
		sql.append(" ORDER BY " + other + " " + order);

		////System.out.println(sql.toString());
		return sql.toString();
	}

	@Override
	public void addOrder(BigOrder bigOrder) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.insert("es_big_order", bigOrder);
	}

	@Override
	public BigOrder getBigOrder(Integer order_id) {
		// TODO Auto-generated method stub
		String sql = "SELECT * FROM es_big_order WHERE order_id = ?";
		return (BigOrder) this.baseDaoSupport.queryForObject(sql, BigOrder.class, order_id);
	}

	@Override
	public void updateBigOrder(BigOrder bigOrder) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.update("es_big_order", bigOrder," order_id="+bigOrder.getOrder_id());
	}

}
