package com.enation.app.tradeease.core.service.trade;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.shop.core.model.Order;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.model.trade.Trade;
import com.enation.framework.database.Page;

/**
 * 订单放款接口
 * 
 * @author yanpeng 2015-6-29 上午11:37:41
 * 
 */

public interface ITradeManager {

	/**
	 * 查询将要放款的订单，1：订单的状态为7（已完成）2：订单的放款状态为0（未放款）
	 * 
	 * @Description: TODO
	 * @param
	 * @return List<Order>
	 */
	public List<Order> listByStatus();
	
	/**
	 *  根据订单号（ordersn）查询已完成的订单且订单的放款状态为0
	 * 
	 * @Description: TODO
	 * @param
	 * @return Order
	 */
	public Order ListByStatusAndSn(String ordersn);
	
	/**
	 *  根据订单查询店铺ID
	 * 
	 * @Description: TODO
	 * @param
	 * @return storeid
	 */
	public int getStoreIdByOrder(String ordersn);

	/**
	 * 获得将要放款的订单的相关信息（卖家店铺，交易的金额等）
	 * 
	 * @Description: TODO
	 * @param
	 * @return List<Map>
	 */
	public List<Map> itemsByOrderId(int OrderId);

	/**
	 * 放款并同时修改相关的记录
	 * 
	 * @Description: TODO
	 * @param
	 * @return void
	 */
	public void addTrade(Trade trade, AccountDetail accountDetail, Store store,
			Order order);

	/**
	 * 根据会员的id查询交易次数
	 * 
	 * @param status
	 * @param memberid
	 * @return
	 */
	public int getTradeCountByMemberId(Integer member_id);

	/**
	 * 查询交易表中的流水号
	 * 
	 * @Description: TODO
	 * @param
	 * @return List<Map>
	 */
	List<Map> getUniqueIds();

	/**
	 * 设置汇率
	 * 
	 * @Description: TODO
	 * @param
	 * @return List<Map>
	 */
	public void setExchange_rate(String exchange_rate, String order_sn);

	/**
	 * 设置汇率
	 * 
	 * @Description: TODO
	 * @param
	 * @return List<Map>
	 */
	public Map queryExchange_rate(String orderSn);

	public Page list(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time);

}