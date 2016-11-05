package com.enation.app.tradeease.core.action.api.chat;



import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.service.chat.IChatManager;
import com.enation.app.tradeease.core.service.translation.MicroTranslate;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.memetix.mst.language.Language;
import com.opensymphony.xwork2.ModelDriven;


@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("chat")
@SuppressWarnings({ "rawtypes", "unchecked", "serial", "static-access" })
public class ChatApiAction extends WWAction{
	private MicroTranslate microTranslate;
	private IChatManager chatManager;
	private Integer storeId;
	private Chat chat;
	private String sender;
	private String sendee;
	private String message;
	private Member member = (Member) ThreadContextHolder.getSessionContext().getAttribute("curr_member");
	private List<Chat> messages;
	
	//发送消息方法
//	public String addMessage(){
//		try {
//			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
//			String translation_begin = this.message;
//			int fromval = Integer.parseInt(request.getParameter("from"));
////			int toval = Integer.parseInt(request.getParameter("to"));
//			String translation_end = null;
//			Language from = null;
//			Language to = null;
//			switch(fromval){
//				case 1: from = Language.CHINESE_SIMPLIFIED;break;
//				case 2: from = Language.RUSSIAN;break;
//			}
//			List<Chat> chat_sendee = this.chatManager.selectFromLanguage(this.storeId);
//			if(chat_sendee!=null&&chat_sendee.size()!=0){
//				to = Language.fromString(chat_sendee.get(0).getTranslation_front());
//				if(to.toString().equals(from.toString())){
//					translation_end = translation_begin;
//				}else{
//					translation_end = microTranslate.translate(translation_begin, from, to);
//				}
//			}else{
//				translation_end = translation_begin;
//				to = from;
//			}
//			Chat chat = new Chat(member.getMember_id(), this.storeId, 1, translation_begin, translation_end, from.toString(), to.toString());
//			this.chatManager.addMessage(chat);
//			this.showSuccessJson("发送成功");
//		} catch (Exception e) {
//			this.showErrorJson("发送失败");
//			e.printStackTrace();
//		}
//		return this.JSON_MESSAGE;
//	}
	
	public String updateChatState(){
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			int store_id = Integer.parseInt(request.getParameter("store_id"));
			this.chatManager.updateState(this.member.getMember_id(),store_id);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	public String updateStateNotRead(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int member_id = Integer.parseInt(request.getParameter("member_id"));
		int store_id = Integer.parseInt(request.getParameter("store_id"));
		this.chatManager.updateStateToNotRead(member_id, store_id);
		return this.JSON_MESSAGE;
	}
	
	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public List<Chat> getMessages() {
		return messages;
	}

	public void setMessages(List<Chat> messages) {
		this.messages = messages;
	}
	
	public IChatManager getChatManager() {
		return chatManager;
	}
	
	public void setChatManager(IChatManager chatManager) {
		this.chatManager = chatManager;
	}
	
	public Chat getChat() {
		return chat;
	}
	
	public void setChat(Chat chat) {
		this.chat = chat;
	}
	
	public String getSender() {
		return sender;
	}
	
	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String getSendee() {
		return sendee;
	}
	
	public void setSendee(String sendee) {
		this.sendee = sendee;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
