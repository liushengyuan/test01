package com.enation.app.tradeease.core.service.trade.impl;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.model.trade.Trade;
import com.enation.app.tradeease.core.service.trade.ITradeManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 订单放款实现类
 * 
 * @author yanpeng 2015-6-29 上午11:43:01
 * 
 */

@SuppressWarnings({ "rawtypes" })
public class TradeManager extends BaseSupport implements ITradeManager {

	public List<Order> listByStatus() {
		String filedname = "status";
		// 等待付款的订单 按付款状态查询
		filedname = " status=" + OrderStatus.ORDER_COMPLETE
				+ " AND loan_status";
		String sql = "select * from order where " + filedname
				+ "=0  ORDER BY order_id DESC";

		return this.baseDaoSupport.queryForList(sql, Order.class);

	}
	
	public Order ListByStatusAndSn(String ordersn) {
		Order order = null;
		String filedname = "status";
		// 等待付款的订单 按付款状态查询
		filedname = " status= " + OrderStatus.ORDER_COMPLETE
				+ " AND loan_status = 0 ";
		if(!StringUtil.isEmpty(ordersn)){
			filedname = filedname +" AND sn = "+ordersn ;
		}else{
			this.logger.error("订单号不存在！");
			return null;
		}
		String sql = "select * from order where " + filedname
				+ " ORDER BY order_id DESC";
		List<Order> orders = this.baseDaoSupport.queryForList(sql, Order.class);
		if(orders.size()>0){
			order = orders.get(0);
		}
		return order;
	}
	
	public int getStoreIdByOrder(String ordersn){
		String sql = " select store_id from es_order where sn = "+ordersn ;
		int storeid = this.baseDaoSupport.queryForInt(sql);
		return storeid;
	}

	@Override
	public List<Map> itemsByOrderId(int OrderId) {
		StringBuffer sql = new StringBuffer();

		sql.append("select g.store_id as store_id,oi.price as price,  (oi.num*g.price_ru) as coupPrice from "
				+ this.getTableName("order")
				+ " o,"
				+ this.getTableName("order_items")
				+ " oi,"
				+ this.getTableName("goods") + " g ");
		sql.append("where  oi.order_id=o.order_id and g.goods_id= oi.goods_id and o.order_id=? ");
		List list = this.daoSupport.queryForList(sql.toString(), OrderId);

		return list;
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addTrade(Trade trade, AccountDetail accountDetail, Store store,
			Order order) {
		int trade_id;
		int rec_id;
		this.baseDaoSupport.insert("trade", trade);
		trade_id = this.baseDaoSupport.getLastId("trade");
		trade.setTrade_id(trade_id);
		this.baseDaoSupport.insert("account_detail", accountDetail);
		rec_id = this.baseDaoSupport.getLastId("accountDetail");
		accountDetail.setRec_id(rec_id);
		this.daoSupport.update("es_store", store,
				" store_id=" + store.getStore_id());
		this.daoSupport.update("es_order", order,
				" order_id=" + order.getOrder_id());
	}

	@Override
	public int getTradeCountByMemberId(Integer member_id) {
		String sql = "select count(0) from es_trade where member_id = ? ";
		int count = this.daoSupport.queryForInt(sql, member_id);
		return count;
	}

	@Override
	public List<Map> getUniqueIds() {
		String sql = "select t.unique_id as unique_id from es_trade t where 1=1 ";
		List list = this.daoSupport.queryForList(sql.toString());
		return list;
	}

	@Override
	public void setExchange_rate(String exchange_rate, String order_sn) {
		double exchange = StringUtil.toDouble(exchange_rate);
		String sql = "update es_order set exchange_rate = ? ";
		if (!StringUtil.isEmpty(order_sn)) {
			sql = "update es_order set exchange_rate = ? where sn = '"
					+ order_sn + "'";
		}
		this.daoSupport.execute(sql, exchange);
	}

	@Override
	public Map queryExchange_rate(String orderSn) {
		String sql = "select o.exchange_rate as exchange_rate from es_order o where o.sn = ? ";
		List<Map> list = this.daoSupport.queryForList(sql.toString(), orderSn);
		return list.get(0);
	}

	@Override
	public Page list(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time) {

		String sql = "SELECT tr.* from es_trade tr  where tr.member_id = ? ";

		if (start_time > 0 && end_time > 0) {
			sql += " and tr.business_time>=" + start_time
					+ " and tr.business_time<" + end_time;
		}
		sql += "  order by tr.business_time desc ";

		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize,
				member_id);
		return page;
	}

}
