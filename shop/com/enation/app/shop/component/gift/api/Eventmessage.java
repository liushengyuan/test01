package com.enation.app.shop.component.gift.api;


import com.enation.framework.database.PrimaryKeyField;

public class Eventmessage {
	private	Integer event_id; //主键 买赠活动管理关联id
	private Integer member_id;//卖家ID
	private Integer gift_id;	//买赠活动ID
	private Integer state;		//状态
	private Long send_time;		//发送时间
	private String eventname;		//活动名称
	private String rule; 		//活动规则

	
	public String getEventname() {
		return eventname;
	}
	public void setEventname(String eventname) {
		this.eventname = eventname;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	@PrimaryKeyField
	public Integer getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Integer getGift_id() {
		return gift_id;
	}
	public void setGift_id(Integer gift_id) {
		this.gift_id = gift_id;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Long getSend_time() {
		return send_time;
	}
	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}
	
}
