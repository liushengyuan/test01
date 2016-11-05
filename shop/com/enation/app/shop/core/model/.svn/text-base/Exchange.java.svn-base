package com.enation.app.shop.core.model;

import com.enation.framework.database.PrimaryKeyField;

/**
 *RMB汇率类
 */

public class Exchange implements java.io.Serializable {

	private Integer id;

	private String currency;//对应的币种
	
	private double exchange_price;//价格
	private Integer disabled;     //币种状态，0不可用，1可用

	@PrimaryKeyField
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public double getExchange_price() {
		return exchange_price;
	}

	public void setExchange_price(double exchange_price) {
		this.exchange_price = exchange_price;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	

}