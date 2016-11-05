package com.enation.app.tradeease.core.action.api.trade;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.model.trade.Trade;
import com.enation.app.tradeease.core.service.trade.ITradeManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 订单放款
 * 
 * @author yanpeng 2015-6-29 上午11:30:17
 * 
 */

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("trade")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class TradeApiAction extends WWAction {

	private IStoreManager storeManager;
	private ITradeManager tradeManager;

	/**
	 * 查询已完成的订单且订单的放款状态为1
	 * 
	 */
	private List completedOrders() {
		List<Order> orders = tradeManager.listByStatus();
		return orders;
	}

	/**
	 * 根据订单号查询该订单是否收到人民币
	 * 
	 */
	private Boolean getRMBOrderAmount(String sn) {
		return true;
	}

	/**
	 * 放款
	 * 
	 * @param
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	public String createLoan() {
		try {
			List<Order> orders = this.completedOrders();
			// // 存在已完成的订单且该订单收到人民币
			if (orders.size() == 0) {
				this.showErrorJson("没有已完成的订单！");
				return this.JSON_MESSAGE;
			}
			for (Order order : orders) {
				if (getRMBOrderAmount(order.getSn())) {
					List<Map> lists = tradeManager.itemsByOrderId(order
							.getOrder_id());
					Map<Integer, Double> itemMap = new HashMap<Integer, Double>();
					// 所有满足条件的结果放到了一个List中，遍历这个list即可得到所有的结果
					for (Map map : lists) {

						int store_id = (Integer) map.get("store_id");
						double coupprice = (Double) map.get("coupprice");
						//System.out.println("store_id= " + store_id
							//	+ " and coupprice= " + coupprice + "合并前");
						// 将store_id相同的订单明细的价格相加（合并同一订单中相同店铺的金额）
						if (itemMap.containsKey(store_id)) {
							itemMap.put(store_id,
									(double) itemMap.get(store_id) + coupprice);
						} else {
							itemMap.put(store_id, coupprice);
						}
					}
					// 遍历放有不同店铺的订单金额的map，进行放款
					for (Integer store_id : itemMap.keySet()) {
						//System.out.println("store_id= " + store_id
								//+ " and coupprice= " + itemMap.get(store_id)
							//	+ "合并后");
						double order_amount_ru = itemMap.get(store_id);
						Store store = storeManager.getStore(store_id);
						// 获得放款对象实例
						Trade trade = this.createTrade(order, store,
								order_amount_ru);
						// 获得商户交易明细实例
						AccountDetail accountDetail = this.createAccountDetail(
								order, store, order_amount_ru);
						// 汇率
						double exchange_rate = (Double) this.tradeManager
								.queryExchange_rate(order.getSn()).get(
										"exchange_rate");
						// 修改商户表的账户
						double store_balance = store.getAccount()
								+ order_amount_ru * exchange_rate
								- order_amount_ru * exchange_rate * 0.05;
						store.setAccount(store_balance);
						store.setCredit_account(store.getCredit_account());
						//System.out.println("store_id= " + store_id
								//+ " and coupprice= " + store_balance + "余额（元）");
						// 修改放款标识位 0：未放款 1：已放款
						order.setLoan_status(1);
						if (this.getUniqueIds().contains(trade.getUnique_id())) {
							this.showErrorJson("放款失败，该放款记录已存在！");
							return this.JSON_MESSAGE;
						}
						this.tradeManager.addTrade(trade, accountDetail, store,
								order);

						this.showSuccessJson("放款成功！");

					}
					//System.out.println("订单结束");
				} else {
					this.showErrorJson("该订单尚未收到人民币！");
					return this.JSON_MESSAGE;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("api调用失败" + e.getMessage());
			this.logger.error("放款出错", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 创建交易实体
	 * 
	 */
	private Trade createTrade(Order order, Store store, double order_amount_ru) {
		Trade trade = new Trade();
		trade.setUnique_id("F"
				+ DateUtil.toString(new Date(), "yyyyMMddhhmmss")
				+ UUID.randomUUID().toString().substring(0, 7));
		trade.setMember_id(store.getMember_id());
		trade.setOrder_id(order.getOrder_id());
		trade.setOrder_sn(order.getSn());
		trade.setStore_name(store.getStore_name());
		trade.setType(1);
		// 设置同一客户的交易次数
		int count = this.tradeManager.getTradeCountByMemberId(store
				.getMember_id());
		trade.setTxNo(count + 1);
		trade.setOrder_amount_ru(order_amount_ru);
		double exchange_rate = (Double) this.tradeManager.queryExchange_rate(
				order.getSn()).get("exchange_rate");
		trade.setExchange_rate(exchange_rate);
		trade.setBrokerage(order_amount_ru * exchange_rate * 0.05);
		trade.setActual_account(order_amount_ru * exchange_rate
				- order_amount_ru * exchange_rate * 0.05);
		if (store.getAccount() == null) {
			store.setAccount(0.00);
		}
		trade.setBalance(store.getAccount() + order_amount_ru * exchange_rate
				- order_amount_ru * exchange_rate * 0.05);
		trade.setBusiness_time(DateUtil.getDateline());
		trade.setOccur_time(order.getCreate_time());
		trade.setRemark("放款");
		return trade;
	}

	/**
	 * 创建商户交易明细实例
	 * 
	 */
	private AccountDetail createAccountDetail(Order order, Store store,
			double order_amount_ru) {
		AccountDetail accountDetail = new AccountDetail();
		accountDetail.setUnique_id("F"
				+ DateUtil.toString(new Date(), "yyyyMMddhhmmss")
				+ UUID.randomUUID().toString().substring(0, 7));
		accountDetail.setMember_id(store.getMember_id());
		accountDetail.setMember_name(store.getMember_name());
		accountDetail.setStore_id(store.getStore_id());
		accountDetail.setStore_name(store.getStore_name());
		accountDetail.setType(1);
		accountDetail.setAmount_type(1);
		double exchange_rate = (Double) this.tradeManager.queryExchange_rate(
				order.getSn()).get("exchange_rate");
		accountDetail.setIncome_amount(order_amount_ru * exchange_rate
				- order_amount_ru * exchange_rate * 0.05);
		accountDetail.setOutlay_amount(0.00);
		if (store.getAccount() == null) {
			store.setAccount(0.00);
		}
		accountDetail.setBalance(store.getAccount() + order_amount_ru
				* exchange_rate - order_amount_ru * exchange_rate * 0.05);
		if (store.getCredit_account() == null) {
			store.setCredit_account(0.00);
		}
		accountDetail.setCredit_balance(store.getCredit_account());
		// 设置同一客户的交易次数
		int count = this.tradeManager.getTradeCountByMemberId(store
				.getMember_id());
		accountDetail.setTx_number(count + 1);
		accountDetail.setNote("放款明细");
		accountDetail.setCreate_time(DateUtil.getDateline());
		accountDetail.setLast_time(DateUtil.getDateline());
		return accountDetail;
	}

	/**
	 * 查询交易表中的流水号
	 * 
	 * @Description: TODO
	 * @param
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private List<String> getUniqueIds() {
		List<Map> lists = this.tradeManager.getUniqueIds();
		List<String> uniqueIdList = new ArrayList<String>();
		for (Map map : lists) {
			uniqueIdList.add((String) map.get("unique_id"));
		}
		return uniqueIdList;
	}

	/**
	 * 设置汇率
	 * 
	 * @Description: TODO
	 * @param
	 * @return
	 */
	public String setExchange_rate() {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String exchange_rate = request.getParameter("exchange_rate");
			String order_sn = request.getParameter("order_sn");
			if (!StringUtil.isEmpty(exchange_rate)) {
				this.tradeManager.setExchange_rate(exchange_rate, order_sn);
				this.showSuccessJson("设置汇率成功！");
			} else {
				this.showErrorJson("输入的汇率为空，请重新输入！");
				return this.JSON_MESSAGE;
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("汇率设置出错！" + e.getMessage());
			this.logger.error("汇率设置出错", e);
		}

		return this.JSON_MESSAGE;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public ITradeManager getTradeManager() {
		return tradeManager;
	}

	public void setTradeManager(ITradeManager tradeManager) {
		this.tradeManager = tradeManager;
	}

}
