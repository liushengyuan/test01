package com.enation.app.shop.core.service.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.ordermanifest.Goodsinformation;
import com.enation.app.shop.core.model.ordermanifest.OrderPaymentInformation;
import com.enation.app.shop.core.model.ordermanifest.Savexml;
import com.enation.app.shop.core.service.IOrderInformationManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

public class OrderInformationManager extends BaseSupport implements IOrderInformationManager {

	@Override
	public Page getInformation(Map map, Integer page, Integer pageSize) {
		  String sql = this.createInformationSql(map);
		  List list1 = this.daoSupport.queryForList(sql);
		  List <Object>list2 = this.daoSupport.queryForListPage(sql.toString(), page, pageSize);
		  Page webpage = new Page();
		  webpage.setParam(0, list1.size(), 1, list1);
	     return webpage;
	}

	private String createInformationSql(Map map) {
		String sql="select *,e.order_id as ziorder_id from es_orderinformation AS o INNER  JOIN es_orderfeeinformation AS e ON o.order_id=e.parent_id INNER JOIN es_goodsinformation AS g ON g.order_id=e.order_id INNER JOIN es_order AS k ON k.order_id=e.order_id order by k.create_time desc";
		return sql;
	}
	@Override
	public Page getOffineInformation(Map map, Integer page, Integer pageSize) {
		String sql = this.createOffineInformationSql(map);
		List list1 = this.daoSupport.queryForList(sql);
		List <Object>list2 = this.daoSupport.queryForListPage(sql.toString(), page, pageSize);
		Page webpage = new Page();
		webpage.setParam(0, list1.size(), 1, list2);
		return webpage;
	}
	
	private String createOffineInformationSql(Map map) {
		Integer order_id=Integer.parseInt(map.get("order_id").toString());
		String sql="SELECT * from es_orderpaymentinformation i LEFT JOIN es_order o ON o.order_id=i.order_id WHERE i.order_id="+order_id;
		return sql;
	}

	@Override
	public List getInformtionByXml(Integer id) {
		String sql="select *,e.order_id as ziorder_id from es_orderinformation AS o INNER  JOIN es_orderfeeinformation AS e ON o.order_id=e.parent_id INNER JOIN es_goodsinformation AS g ON g.order_id=e.order_id INNER JOIN es_order AS k ON k.order_id=e.order_id where e.order_id=?";
		return this.baseDaoSupport.queryForList(sql, id);
	}

	@Override
	public List<Goodsinformation> getGoodsInformation(Integer order_id) {
		String sql="select * from es_goodsinformation WHERE order_id=?";
		List<Goodsinformation> list=this.baseDaoSupport.queryForList(sql, Goodsinformation.class, order_id);
		return list;
	}

	@Override
	public OrderPaymentInformation getPaymentInformation(Integer order_id) {
		OrderPaymentInformation orderPaymentInformation=null;
		String sql="select * from es_orderpaymentinformation where order_id=?";
		List<OrderPaymentInformation> list=this.baseDaoSupport.queryForList(sql, OrderPaymentInformation.class, order_id);
		if(list.size()>0){
			orderPaymentInformation=list.get(0);
		}
		return orderPaymentInformation;
	}

	@Override
	public Page getXmlInformation(Map map, Integer page, Integer pageSize) {
		String sql = this.createXmlInformationSql(map);
		List list1 = this.daoSupport.queryForList(sql);
		List <Object>list2 = this.daoSupport.queryForListPage(sql.toString(), page, pageSize);
		Page webpage = new Page();
		webpage.setParam(0, list1.size(), 1, list2);
		return webpage;
	}

	private String createXmlInformationSql(Map map) {
		Integer order_id=Integer.parseInt(map.get("order_id").toString());
		String sql="select * from es_savexml where order_id="+order_id;
		sql+=" order by create_time desc";
		return sql.toString();
	}

	@Override
	public Savexml getXmlInformation(Integer id) {
		String sql="select * from es_savexml where id=? order by create_time desc";
		return (Savexml) this.baseDaoSupport.queryForObject(sql, Savexml.class, id);
	}

	@Override
	public void deleteXmlInformation(Integer id) {
		String sql="delete from es_savexml where id=?";
		this.baseDaoSupport.execute(sql, id);
		
	}

	@Override
	public void saveXmlInformation(Savexml savexml) {
		this.baseDaoSupport.insert("es_savexml", savexml);
		
	}

	@Override
	public Integer getXmlCount() {
		String sql="select MAX(is_count) from es_savexml";
		Integer count=this.baseDaoSupport.queryForInt(sql);
		return count;
	}
    
}
