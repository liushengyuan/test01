package com.enation.app.tradeease.core.model.trade;

import com.enation.framework.database.DynamicField;

/**
 * 订单实体
 * 
 * @author kingapex 2010-4-6上午11:11:27
 */

public class Trade extends DynamicField implements java.io.Serializable {
	private Integer trade_id; // 交易ID
	private Integer order_id; // 订单Id
	private String unique_id; // 交易流水号
	private String order_sn; // 订单号
	private Integer member_id; // 会员ID
	private String store_name; // 商户名称
	private Integer status; // 交易状态(预留字段,目前只有正常状态)
	private Integer type; // 交易类型 （年费、保证金、入驻费,放款,提现,快递费,易支付转账（进出）,补偿等）
	private Integer txNo; // 交易序列号,用于记录商户交易顺序
	private Double order_amount_ru;// 订单金额（卢布）
	private Double exchange_rate;// 汇率
	private Double brokerage;// 佣金
	private Double actual_account;// 订单实收金额（人民币）
	private Double balance;// 平台卖家账户余额
	private Integer bill_id; // 关联单据ID
	private Integer oper_id; // 操作员ID
	private Long business_time; // 业务日(对账结算日) 放款时间
	private Long record_time; // 交易生成时间(进入后台系统时间)
	private Long occur_time; // 订单交易时间(原交易时间)
	private String remark; // 备注

	public Integer getTrade_id() {
		return trade_id;
	}

	public void setTrade_id(Integer trade_id) {
		this.trade_id = trade_id;
	}

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public String getUnique_id() {
		return unique_id;
	}

	public void setUnique_id(String unique_id) {
		this.unique_id = unique_id;
	}

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getTxNo() {
		return txNo;
	}

	public void setTxNo(Integer txNo) {
		this.txNo = txNo;
	}

	public Double getOrder_amount_ru() {
		return order_amount_ru;
	}

	public void setOrder_amount_ru(Double order_amount_ru) {
		this.order_amount_ru = order_amount_ru;
	}

	public Double getExchange_rate() {
		return exchange_rate;
	}

	public void setExchange_rate(Double exchange_rate) {
		this.exchange_rate = exchange_rate;
	}

	public Double getBrokerage() {
		return brokerage;
	}

	public void setBrokerage(Double brokerage) {
		this.brokerage = brokerage;
	}

	public Double getActual_account() {
		return actual_account;
	}

	public void setActual_account(Double actual_account) {
		this.actual_account = actual_account;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Integer getBill_id() {
		return bill_id;
	}

	public void setBill_id(Integer bill_id) {
		this.bill_id = bill_id;
	}

	public Integer getOper_id() {
		return oper_id;
	}

	public void setOper_id(Integer oper_id) {
		this.oper_id = oper_id;
	}

	public Long getBusiness_time() {
		return business_time;
	}

	public void setBusiness_time(Long business_time) {
		this.business_time = business_time;
	}

	public Long getRecord_time() {
		return record_time;
	}

	public void setRecord_time(Long record_time) {
		this.record_time = record_time;
	}

	public Long getOccur_time() {
		return occur_time;
	}

	public void setOccur_time(Long occur_time) {
		this.occur_time = occur_time;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

}