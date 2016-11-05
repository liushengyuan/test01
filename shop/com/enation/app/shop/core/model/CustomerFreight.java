package com.enation.app.shop.core.model;

public class CustomerFreight {
 private Integer freight_id;
 private String logis_model_id;
 private Double freightprice;
 private String logis_name;//物流商
 private String validatedays;
 private String productname;
public Double getFreightprice() {
	return freightprice;
}

public void setFreightprice(Double freightprice) {
	this.freightprice = freightprice;
}

public String getLogis_name() {
	return logis_name;
}

public String getProductname() {
	return productname;
}

public void setProductname(String productname) {
	this.productname = productname;
}

public void setLogis_name(String logis_name) {
	this.logis_name = logis_name;
}

public String getValidatedays() {
	return validatedays;
}

public void setValidatedays(String validatedays) {
	this.validatedays = validatedays;
}

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

}
