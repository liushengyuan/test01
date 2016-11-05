package com.enation.app.base.core.model;

import com.enation.framework.database.PrimaryKeyField;

public class Withdrawal implements java.io.Serializable {
	private Integer member_id;
	private Integer store_id;
	private Integer operator_id;
	//private Integer account;//帐号
	private Integer serial_number;//流水号
	private Long create_date;
	private Long real_date;
	private Long ok_date;
	private String details;
	private String note;
	private Integer state;//0 "待审核",1 "已放款",2 "不审批",3 "已取消",4  "已同意",
	private String capital_channel;
	private Integer type;//1.提现   别的再 定 ，这个字段感觉不应该在这
	private Integer id;
	private Double number;//提现金额
	private String card;
	public String getCard() {
		return card;
	}
	public void setCard(String card) {
		this.card = card;
	}
	public Double getNumber() {
		return number;
	}
	public void setNumber(Double number) {
		this.number = number;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public Integer getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(Integer operator_id) {
		this.operator_id = operator_id;
	}
	/*public Integer getAccount() {
		return account;
	}
	public void setAccount(Integer account) {
		this.account = account;
	}*/
	public Integer getSerial_number() {
		return serial_number;
	}
	public void setSerial_number(Integer serial_number) {
		this.serial_number = serial_number;
	}
	public Long getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Long create_date) {
		this.create_date = create_date;
	}
	public Long getReal_date() {
		return real_date;
	}
	public void setReal_date(Long real_date) {
		this.real_date = real_date;
	}
	public Long getOk_date() {
		return ok_date;
	}
	public void setOk_date(Long ok_date) {
		this.ok_date = ok_date;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getCapital_channel() {
		return capital_channel;
	}
	public void setCapital_channel(String capital_channel) {
		this.capital_channel = capital_channel;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@PrimaryKeyField
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	

}
