package com.enation.app.tradeease.core.tag.chat;

import java.io.IOException;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.service.chat.IChatManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/*
 * 进入页面时显示的5条聊天记录
 * 
 */
@Component
@Scope("prototype")
public class ChatTag extends BaseFreeMarkerTag {
	private IChatManager chatManager;
	private List<Chat> chatList;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		if(ThreadContextHolder.getSessionContext().getAttribute("curr_member")!=null){
			Member member = (Member) ThreadContextHolder.getSessionContext().getAttribute("curr_member");
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			chatList = this.chatManager.findBySender(member.getMember_id(),Integer.parseInt(request.getParameter("storeId")));
		}
		else{
			try {
				HttpServletRequest request = ThreadContextHolder.getHttpRequest();
				String uri = request.getRequestURI().toString();
				String storeId = request.getParameter("storeId");
				String goodsid = request.getParameter("goodsid");
				String forward = uri+"?storeId="+storeId;
				if(goodsid!=null){
					forward = forward+"&goodsid="+goodsid;
				}
				ThreadContextHolder.getHttpResponse().sendRedirect("../login.html?forward="+forward);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return chatList;
	}
	public IChatManager getChatManager() {
		return chatManager;
	}
	public void setChatManager(IChatManager chatManager) {
		this.chatManager = chatManager;
	}
	
}
