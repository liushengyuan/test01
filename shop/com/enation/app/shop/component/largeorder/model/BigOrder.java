package com.enation.app.shop.component.largeorder.model;

import java.util.List;

import net.sf.json.JSONArray;

import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.model.PayEnable;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.framework.database.DynamicField;
import com.enation.framework.database.NotDbField;
import com.enation.framework.database.PrimaryKeyField;
import com.enation.framework.util.StringUtil;

/**
 * 大宗交易订单实体
 * 
 * @author jfw 2015-10-15上午11:14:27
 */
public class BigOrder extends DynamicField implements java.io.Serializable{

	private Integer order_id; // 订单Id
	private String sn; // 订单编号
	private Integer status; // 订单状态
	private Integer pay_status; // 付款状态
	private Integer ship_status; // 货运状态
	private String goods;
	private Integer goods_num; // 商品数量	
	private Long create_time; // 创建时间
	private String ship_name; // 收货人
	private String ship_addr; // 收货地址
	private String ship_email; // 收获人邮箱
	private String ship_tel; // 收货人电话
	private String store_name;
	private String currency;//币种	
	private Double order_amount; // 订单价格
	private String remark; // 订单备注	
	private Integer payment_type;//0为未支付 1为Yandex Money 2为首信易支付
	
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getPay_status() {
		return pay_status;
	}
	public void setPay_status(Integer pay_status) {
		this.pay_status = pay_status;
	}
	public Integer getShip_status() {
		return ship_status;
	}
	public void setShip_status(Integer ship_status) {
		this.ship_status = ship_status;
	}
	public String getGoods() {
		return goods;
	}
	public void setGoods(String goods) {
		this.goods = goods;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public String getShip_name() {
		return ship_name;
	}
	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}
	public String getShip_addr() {
		return ship_addr;
	}
	public void setShip_addr(String ship_addr) {
		this.ship_addr = ship_addr;
	}
	public String getShip_email() {
		return ship_email;
	}
	public void setShip_email(String ship_email) {
		this.ship_email = ship_email;
	}
	public String getShip_tel() {
		return ship_tel;
	}
	public void setShip_tel(String ship_tel) {
		this.ship_tel = ship_tel;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Double getOrder_amount() {
		return order_amount;
	}
	public void setOrder_amount(Double order_amount) {
		this.order_amount = order_amount;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(Integer payment_type) {
		this.payment_type = payment_type;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	
	

	

	

	

	


	

	

	

}