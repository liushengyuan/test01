package com.enation.app.shop.component.bonus.model;

import com.enation.framework.database.PrimaryKeyField;

/**
 * 红包类型实体
 * 
 * @author kingapex 2013-8-13下午2:39:27
 */
public class BonusType {

	private int type_id;
	private String type_name;
	private Double type_money;
	private int send_type;
	private Long send_start_date;
	private Long send_end_date;
	private Long use_start_date;
	private Long use_end_date;
	private Double min_goods_amount;
	private String recognition;
	private int create_num;
	private int use_num;
	private String type_name_ru;//新增红包国际化
	private Double type_money_ru;
	private Double min_goods_amount_ru;//商品最低卢布金额
	private Integer sendplat;
	
	@PrimaryKeyField
	public int getType_id() {
		return type_id;
	}
	public void setType_id(int type_id) {
		this.type_id = type_id;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String type_name) {
		this.type_name = type_name;
	}
	public Double getType_money() {
		return type_money;
	}
	public void setType_money(Double type_money) {
		this.type_money = type_money;
	}
	public int getSend_type() {
		return send_type;
	}
	public void setSend_type(int send_type) {
		this.send_type = send_type;
	}

	public void setMin_goods_amount(Double min_goods_amount) {
		this.min_goods_amount = min_goods_amount;
	}
	public String getRecognition() {
		return recognition;
	}
	public void setRecognition(String recognition) {
		this.recognition = recognition;
	}
	public int getCreate_num() {
		return create_num;
	}
	public void setCreate_num(int create_num) {
		this.create_num = create_num;
	}
	public int getUse_num() {
		return use_num;
	}
	public void setUse_num(int use_num) {
		this.use_num = use_num;
	}
	public Long getSend_start_date() {
		return send_start_date;
	}
	public void setSend_start_date(Long send_start_date) {
		this.send_start_date = send_start_date;
	}
	public Long getSend_end_date() {
		return send_end_date;
	}
	public void setSend_end_date(Long send_end_date) {
		this.send_end_date = send_end_date;
	}
	public Long getUse_start_date() {
		return use_start_date;
	}
	public void setUse_start_date(Long use_start_date) {
		this.use_start_date = use_start_date;
	}
	public Long getUse_end_date() {
		return use_end_date;
	}
	public void setUse_end_date(Long use_end_date) {
		this.use_end_date = use_end_date;
	}
	public Double getMin_goods_amount() {
		return min_goods_amount;
	}
	public String getType_name_ru() {
		return type_name_ru;
	}
	public void setType_name_ru(String type_name_ru) {
		this.type_name_ru = type_name_ru;
	}
	public Double getType_money_ru() {
		return type_money_ru;
	}
	public void setType_money_ru(Double type_money_ru) {
		this.type_money_ru = type_money_ru;
	}
	public Double getMin_goods_amount_ru() {
		return min_goods_amount_ru;
	}
	public void setMin_goods_amount_ru(Double min_goods_amount_ru) {
		this.min_goods_amount_ru = min_goods_amount_ru;
	}
	public Integer getSendplat() {
		return sendplat;
	}
	public void setSendplat(Integer sendplat) {
		this.sendplat = sendplat;
	}
	
}
