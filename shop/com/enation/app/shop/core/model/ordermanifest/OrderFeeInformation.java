package com.enation.app.shop.core.model.ordermanifest;
/**
 * 订单报关单-表体
 * @author Administrator
 *
 */
public class OrderFeeInformation {
private Integer id;
private Integer order_id;
private Integer parent_id;
//订单费用
private Double CHARGE;//总费用
private Double GOODSVALUE;//商品货款
private Double OTHERVALUE;//其他杂费
private Double TAX;//海关税费
//收货人信息节点
private String CONSIGNEE;//收货人名称
private String CONSIGNEEADDRESS;//收货人地址
private String CONSIGNEETELEPHONE;//收货人电话
private String CONSIGNEECOUNTRY;//收货人所在国家(地区)代码
//支付信息节点
private String PAYMENTCODE;//支付企业代码,海关注册代码
private String PAYMENTNAME;//支付企业名称
private String PAYMENTNO;//支付交易号
public Integer getOrder_id() {
	return order_id;
}
public String getCONSIGNEE() {
	return CONSIGNEE;
}
public String getCONSIGNEEADDRESS() {
	return CONSIGNEEADDRESS;
}
public String getCONSIGNEETELEPHONE() {
	return CONSIGNEETELEPHONE;
}
public String getCONSIGNEECOUNTRY() {
	return CONSIGNEECOUNTRY;
}
public String getPAYMENTCODE() {
	return PAYMENTCODE;
}
public String getPAYMENTNAME() {
	return PAYMENTNAME;
}
public String getPAYMENTNO() {
	return PAYMENTNO;
}
public void setOrder_id(Integer order_id) {
	this.order_id = order_id;
}
public void setCONSIGNEE(String cONSIGNEE) {
	CONSIGNEE = cONSIGNEE;
}
public void setCONSIGNEEADDRESS(String cONSIGNEEADDRESS) {
	CONSIGNEEADDRESS = cONSIGNEEADDRESS;
}
public void setCONSIGNEETELEPHONE(String cONSIGNEETELEPHONE) {
	CONSIGNEETELEPHONE = cONSIGNEETELEPHONE;
}
public void setCONSIGNEECOUNTRY(String cONSIGNEECOUNTRY) {
	CONSIGNEECOUNTRY = cONSIGNEECOUNTRY;
}
public void setPAYMENTCODE(String pAYMENTCODE) {
	PAYMENTCODE = pAYMENTCODE;
}
public void setPAYMENTNAME(String pAYMENTNAME) {
	PAYMENTNAME = pAYMENTNAME;
}
public void setPAYMENTNO(String pAYMENTNO) {
	PAYMENTNO = pAYMENTNO;
}
public Integer getId() {
	return id;
}
public void setId(Integer id) {
	this.id = id;
}
public Double getCHARGE() {
	return CHARGE;
}
public void setCHARGE(Double cHARGE) {
	CHARGE = cHARGE;
}
public Double getGOODSVALUE() {
	return GOODSVALUE;
}
public void setGOODSVALUE(Double gOODSVALUE) {
	GOODSVALUE = gOODSVALUE;
}
public Double getOTHERVALUE() {
	return OTHERVALUE;
}
public void setOTHERVALUE(Double oTHERVALUE) {
	OTHERVALUE = oTHERVALUE;
}
public Double getTAX() {
	return TAX;
}
public void setTAX(Double tAX) {
	TAX = tAX;
}
public Integer getParent_id() {
	return parent_id;
}
public void setParent_id(Integer parent_id) {
	this.parent_id = parent_id;
}
}
