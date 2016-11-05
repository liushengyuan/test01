package com.enation.app.tradeease.core.action.api.trade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.model.trade.Trade;
import com.enation.app.tradeease.core.service.trade.ITradeManager;
import com.enation.app.tradeease.core.util.LoanFtpUtils;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 订单放款
 * 
 * @author yanpeng 2015-7-31 上午11:30:17
 * 
 */

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("mytrade")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class MyTradeApiAction extends WWAction {

	private IStoreManager storeManager;
	private ITradeManager tradeManager;
	private IOrderManager orderManager;
	private IStoreOrderManager storeOrderManager;

	/**
	 * 根据订单号（ordersn）查询已完成的订单且订单的放款状态为0
	 * 
	 */
	private Order completedOrders(String ordersn) {
		Order order = tradeManager.ListByStatusAndSn(ordersn);
		return order;
	}

	public void loan() {
		try {
			//System.out.println("开始执行了**************************");
			List<List> loanList = this.getLoanOrderList();
			if (loanList == null || loanList.size() <= 1) {
				this.logger.info("放款文件没有记录！");
				return;
			}
			for (int i = 1; i < loanList.size(); i++) {
				List myList = loanList.get(i);
				// String ordersn = null;
				String ordersn = (String) myList.get(0);
				// String[] v_oidArr = v_oid.split("-");
				// if (v_oidArr.length == 3) {
				// ordersn = v_oidArr[2];
				// }
				if (StringUtil.isEmpty((String) myList.get(5))
						|| StringUtil.isEmpty((String) myList.get(3))) {
					this.logger.error("放款文件内容为空！");
					return;
				}
				double amount = Double.valueOf((String) myList.get(5));
				double exchange_rate = Double.valueOf((String) myList.get(3));
				//
				StoreOrder parentOrder=storeOrderManager.get(ordersn);
				if(parentOrder!=null&&parentOrder.getParent_id()==null){
					//获取子订单列表
					List<StoreOrder> cOrderList= storeOrderManager.storeOrderList(parentOrder.getOrder_id());
					for (StoreOrder storeOrder : cOrderList) {
						doLoan(storeOrder.getSn(),amount,exchange_rate);
					}
				}else {
					doLoan(ordersn,amount,exchange_rate);
				}
				//
//			
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("放款出错", e);
		}
	}
	
	/**
	 * 
	 * 
	 */
	private void doLoan(String sn,Double amount,Double exchange_rate){

		//
		Order order = this.completedOrders(sn);
		// // 存在已完成的订单且该订单收到人民币
		if (order != null) {
			int store_id = tradeManager
					.getStoreIdByOrder(order.getSn());
			Store store = storeManager.getStore(store_id);
			// 获得放款对象实例
			Trade trade = this.createTrade(order, store, amount,
					exchange_rate);
			// 获得商户交易明细实例
			AccountDetail accountDetail = this.createAccountDetail(
					order, store, amount);
			// 修改商户表的账户
			double store_balance = store.getAccount() + amount * 0.95;
			store.setAccount(store_balance);
			store.setCredit_account(store.getCredit_account());
			// 修改放款标识位 0：未放款 1：已放款
			order.setLoan_status(1);
			// 设置订单状态为已放款
			order.setStatus(11);
			// 根据流水号查询放款记录是否已存在
			if (this.getUniqueIds().contains(trade.getUnique_id())) {
				//System.out.println("放款失败，该放款记录已存在！");
				return;
			}
			this.tradeManager.addTrade(trade, accountDetail, store,
					order);
			this.logger.info("订单号：" + order.getSn() + "放款成功！");
		} else {
			this.logger.error("订单号不存在！");
		}
	
	}

	/**
	 * 创建交易实体
	 * 
	 */
	private Trade createTrade(Order order, Store store, double amount,
			double exchange_rate) {
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
		trade.setOrder_amount_ru(amount * 0.95);
		trade.setExchange_rate(exchange_rate);
		trade.setBrokerage(amount * 0.05);
		trade.setActual_account(amount);
		if (store.getAccount() == null) {
			store.setAccount(0.00);
		}
		trade.setBalance(store.getAccount() + amount * 0.95);
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
			double amount) {
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
		accountDetail.setIncome_amount(amount * 0.95);
		accountDetail.setOutlay_amount(0.00);
		if (store.getAccount() == null) {
			store.setAccount(0.00);
		}
		accountDetail.setBalance(store.getAccount() + amount * 0.95);
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

	public List<List> getLoanOrderList() {
		InputStream is = null;
		BufferedReader br = null;
		String rec = null;// 一行
		String str;// 一个单元格
		LoanFtpUtils loanFtp = new LoanFtpUtils(getFileName());
		is = loanFtp.getStream();
		if (is == null) {
			this.logger.error("文件不存在！");
			return null;
		}
		InputStreamReader fr = new InputStreamReader(is);
		br = new BufferedReader(fr);
		List<List> listFile = new ArrayList();
		try {
			// 读取一行
			while ((rec = br.readLine()) != null) {
				Pattern pCells = Pattern
						.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
				Matcher mCells = pCells.matcher(rec);
				List<String> cells = new ArrayList();// 每行记录一个list
				// 读取每个单元格
				while (mCells.find()) {
					str = mCells.group();
					str = str.replaceAll(
							"(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
					str = str.replaceAll("(?sm)(\"(\"))", "$2");
					cells.add(str);
				}
				listFile.add(cells);

			}
			//System.out.println(listFile);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
				if (fr != null) {
					fr.close();
				}
				if (br != null) {
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return listFile;
	}

	/**
	 * 获取文件名
	 * 
	 * @return
	 */
	private String getFileName() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		String fileName = new SimpleDateFormat("yyyyMMdd")
				.format(cal.getTime()) + ".csv";
		//System.out.println(fileName);
		return fileName;
	}

	public static void main(String[] args) {
		new MyTradeApiAction().getFileName();
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

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}

	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}
	
}
