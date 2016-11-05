package com.enation.app.shop.core.model;

/**
 * 商家模板
 * 
 * @author Administrator
 * 
 */
public class LogisModel {
	private String logis_model_id;
	private Integer freight_id;// 运费模板id
	private String model_name;// 模板的名称
	private Integer logis_price_type;// 模板的计费类型     1.包邮 0.标准	 
	private Integer store_id;// 店铺模板id  ai 0.平台   商家
	private Integer is_name;

	public String getLogis_model_id() {
		return logis_model_id;
	}

	public void setLogis_model_id(String logis_model_id) {
		this.logis_model_id = logis_model_id;
	}

	public Integer getFreight_id() {
		return freight_id;
	}

	public void setFreight_id(Integer freight_id) {
		this.freight_id = freight_id;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public Integer getLogis_price_type() {
		return logis_price_type;
	}

	public void setLogis_price_type(Integer logis_price_type) {
		this.logis_price_type = logis_price_type;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public Integer getIs_name() {
		return is_name;
	}

	public void setIs_name(Integer is_name) {
		this.is_name = is_name;
	}

}
