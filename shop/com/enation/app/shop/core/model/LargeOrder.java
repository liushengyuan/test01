package com.enation.app.shop.core.model;
/**
 * 大宗交易订单
 * @author jfw
 *
 */
public class LargeOrder {

	private Integer mind_id;//意向id
	private String store_name;
	private Integer goods_id;
	private String goods_name;
	private String price;
	
	private Integer goods_num;//商品数量
	private String order_text;//说明
	private String buyer_name;
	private String company;
	private String email;
	private String phone;
	
	private long request_time;
	private long handle_time;//处理时间
	private String handle_person;//处理人
	private String handle_result;//处理结果
	private Integer status;//1已处理 0未处理
	
	public Integer getMind_id() {
		return mind_id;
	}
	public void setMind_id(Integer mind_id) {
		this.mind_id = mind_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	public String getGoods_name() {
		return goods_name;
	}
	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public Integer getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(Integer goods_num) {
		this.goods_num = goods_num;
	}
	public String getOrder_text() {
		return order_text;
	}
	public void setOrder_text(String order_text) {
		this.order_text = order_text;
	}
	public String getBuyer_name() {
		return buyer_name;
	}
	public void setBuyer_name(String buyer_name) {
		this.buyer_name = buyer_name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public long getRequest_time() {
		return request_time;
	}
	public void setRequest_time(long request_time) {
		this.request_time = request_time;
	}
	public long getHandle_time() {
		return handle_time;
	}
	public void setHandle_time(long handle_time) {
		this.handle_time = handle_time;
	}
	public String getHandle_person() {
		return handle_person;
	}
	public void setHandle_person(String handle_person) {
		this.handle_person = handle_person;
	}
	public String getHandle_result() {
		return handle_result;
	}
	public void setHandle_result(String handle_result) {
		this.handle_result = handle_result;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
	
	
	
	
}
