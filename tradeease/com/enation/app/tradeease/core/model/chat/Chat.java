package com.enation.app.tradeease.core.model.chat;

import java.sql.Timestamp;


public class Chat {
	private Integer id;
	private Integer sender;//发件人
	private Integer sendee;//收件人
	private String sendtime;//发送时间
	private Integer state;//发送状态 1已查看 2未查看
	private String message_begin;//翻译前聊天内容
	private String message_end;//翻译后聊天内容
	private String translation_front;//翻译前语言
	private String translation_back;//翻译后语言
	private Integer goods_id;
	public Chat(Integer sender, Integer sendee, 
			Integer state, String message_begin, String message_end,
			String translation_front, String translation_back, Integer goods_id) {
		super();
		this.sender = sender;
		this.sendee = sendee;
		this.state = state;
		this.message_begin = message_begin;
		this.message_end = message_end;
		this.translation_front = translation_front;
		this.translation_back = translation_back;
		this.goods_id = goods_id;
	}
	public Chat() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Chat(Integer sender, Integer sendee, Integer state, String message_begin, String message_end,
			String translation_front) {
		super();
		this.sender = sender;
		this.sendee = sendee;
		this.state = state;
		this.message_begin = message_begin;
		this.message_end = message_end;
		this.translation_front = translation_front;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSender() {
		return sender;
	}
	public void setSender(Integer sender) {
		this.sender = sender;
	}
	public Integer getSendee() {
		return sendee;
	}
	public void setSendee(Integer sendee) {
		this.sendee = sendee;
	}
	public String getSendtime() {
		return sendtime;
	}
	public void setSendtime(String sendtime) {
		this.sendtime = sendtime;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getMessage_begin() {
		return message_begin;
	}
	public void setMessage_begin(String message_begin) {
		this.message_begin = message_begin;
	}
	public String getMessage_end() {
		return message_end;
	}
	public void setMessage_end(String message_end) {
		this.message_end = message_end;
	}
	public String getTranslation_front() {
		return translation_front;
	}
	public void setTranslation_front(String translation_front) {
		this.translation_front = translation_front;
	}
	public String getTranslation_back() {
		return translation_back;
	}
	public void setTranslation_back(String translation_back) {
		this.translation_back = translation_back;
	}
	public Chat(Integer sender, Integer sendee, Integer state,
			String message_begin, String message_end, String translation_front,
			String translation_back) {
		super();
		this.sender = sender;
		this.sendee = sendee;
		this.state = state;
		this.message_begin = message_begin;
		this.message_end = message_end;
		this.translation_front = translation_front;
		this.translation_back = translation_back;
	}
	public Integer getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}
	
}
