package com.enation.app.shop.core.model;

public class CustomerLogi {
	private String  logis_model_id;
	private Integer freight_id;
	private String product_name;
	private String validatedays;
	private Integer Logis_price_type;
	private String model_name;
    public String getValidatedays() {
		return validatedays;
	}

	public void setValidatedays(String validatedays) {
		this.validatedays = validatedays;
	}

	public Integer getFreight_id() {
		return freight_id;
	}

	public void setFreight_id(Integer freight_id) {
		this.freight_id = freight_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public Integer getLogis_price_type() {
		return Logis_price_type;
	}

	public void setLogis_price_type(Integer logis_price_type) {
		Logis_price_type = logis_price_type;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getLogis_model_id() {
		return logis_model_id;
	}

	public void setLogis_model_id(String logis_model_id) {
		this.logis_model_id = logis_model_id;
	}

}
