package com.enation.app.shop.component.gift.model;

import com.enation.framework.database.PrimaryKeyField;

public class Gift {
	private Integer gift_id;
	private String name;
	private Long start_time;//活动开始时间
	private Long end_time;//活动结束时间
	
	private String rule;   //活动规则
	private String atturl;
	private String url;
	private Integer state; //信息发送状态
	private Integer type;//活动类型
	private Long save_time;//保存时间
	private Long send_time;//发送时间
	
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getAtturl() {
		return atturl;
	}
	public void setAtturl(String atturl) {
		this.atturl = atturl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public String getName() {
		return name;
	}
	@PrimaryKeyField
	public Integer getGift_id() {
		return gift_id;
	}
	public void setGift_id(Integer gift_id) {
		this.gift_id = gift_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getStart_time() {
		return start_time;
	}
	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}
	public Long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Long getSave_time() {
		return save_time;
	}
	public void setSave_time(Long save_time) {
		this.save_time = save_time;
	}
	public Long getSend_time() {
		return send_time;
	}
	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}
	
}
