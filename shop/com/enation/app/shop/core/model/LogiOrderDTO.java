package com.enation.app.shop.core.model;

public class LogiOrderDTO {
	private String order_id;
	private String sn;
	private String store_name;
	private String ship_no;
	private String logi_name;
	private Long create_time; // 创建时间
	private Double shiping_freight; // 配送费用
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public String getShip_no() {
		return ship_no;
	}
	public void setShip_no(String ship_no) {
		this.ship_no = ship_no;
	}
	public String getLogi_name() {
		return logi_name;
	}
	public void setLogi_name(String logi_name) {
		this.logi_name = logi_name;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public Double getShiping_freight() {
		return shiping_freight;
	}
	public void setShiping_freight(Double shiping_freight) {
		this.shiping_freight = shiping_freight;
	}
	
	
	
}
