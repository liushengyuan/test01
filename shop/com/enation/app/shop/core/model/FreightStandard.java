package com.enation.app.shop.core.model;
/**
 * 运费标准
 * @author lxy
 *
 */
public class FreightStandard {
 
 private Integer freight_id;
 private String logis_name;//物流商
 private String product_name;//销售产品
 private double max_weight;//最大重量限制 
 private double min_weight;//最小重量限制
 //价格规则
 private double start_price;//起步价
 private double every_price_kg;//超出起步价中包含的重量收取的费用
 private double start_price_kg;//起步价中包含的重量kg
 private double extra_price;//附加单费
 
 private double start_price_ru;//俄起步价
 private double every_price_kg_ru;//俄每千克重量费用
 private double start_price_kg_ru;//俄起步价中包含的重量kg
 private double extra_price_ru;//俄附加单费
 //尺寸规定
 private double max_long;//最大长
 private double min_long;//最小长
 private double max_width;//最大宽
 private double min_width;//最小宽
 private double max_high;//最大高
 private double min_high;//最小高
 private String validatedays;//时效
public String getLogis_name() {
	return logis_name;
}
public void setLogis_name(String logis_name) {
	this.logis_name = logis_name;
}
public String getProduct_name() {
	return product_name;
}
public void setProduct_name(String product_name) {
	this.product_name = product_name;
}
public double getMax_weight() {
	return max_weight;
}
public void setMax_weight(double max_weight) {
	this.max_weight = max_weight;
}
public double getMin_weight() {
	return min_weight;
}
public void setMin_weight(double min_weight) {
	this.min_weight = min_weight;
}
public double getStart_price() {
	return start_price;
}
public void setStart_price(double start_price) {
	this.start_price = start_price;
}
public double getEvery_price_kg() {
	return every_price_kg;
}
public void setEvery_price_kg(double every_price_kg) {
	this.every_price_kg = every_price_kg;
}
public double getStart_price_kg() {
	return start_price_kg;
}
public void setStart_price_kg(double start_price_kg) {
	this.start_price_kg = start_price_kg;
}
public double getExtra_price() {
	return extra_price;
}
public void setExtra_price(double extra_price) {
	this.extra_price = extra_price;
}
public double getStart_price_ru() {
	return start_price_ru;
}
public void setStart_price_ru(double start_price_ru) {
	this.start_price_ru = start_price_ru;
}
public double getEvery_price_kg_ru() {
	return every_price_kg_ru;
}
public void setEvery_price_kg_ru(double every_price_kg_ru) {
	this.every_price_kg_ru = every_price_kg_ru;
}
public double getStart_price_kg_ru() {
	return start_price_kg_ru;
}
public void setStart_price_kg_ru(double start_price_kg_ru) {
	this.start_price_kg_ru = start_price_kg_ru;
}
public double getExtra_price_ru() {
	return extra_price_ru;
}
public void setExtra_price_ru(double extra_price_ru) {
	this.extra_price_ru = extra_price_ru;
}
public double getMax_long() {
	return max_long;
}
public void setMax_long(double max_long) {
	this.max_long = max_long;
}
public double getMin_long() {
	return min_long;
}
public void setMin_long(double min_long) {
	this.min_long = min_long;
}
public double getMax_width() {
	return max_width;
}
public void setMax_width(double max_width) {
	this.max_width = max_width;
}
public double getMin_width() {
	return min_width;
}
public void setMin_width(double min_width) {
	this.min_width = min_width;
}
public double getMax_high() {
	return max_high;
}
public void setMax_high(double max_high) {
	this.max_high = max_high;
}
public double getMin_high() {
	return min_high;
}
public void setMin_high(double min_high) {
	this.min_high = min_high;
}
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
 
 
}
