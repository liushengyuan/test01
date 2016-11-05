package com.enation.app.tradeease.core.tag.chat;
/*
 * 查询所有登录用户与聊天对象的聊天记录
 * */
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
@Component
@Scope("prototype")
public class ChatAllTag extends BaseFreeMarkerTag {
	private IChatManager chatManager;
	private List<Chat> chatList;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		if(ThreadContextHolder.getSessionContext().getAttribute("curr_member")!=null){
			Member member = (Member) ThreadContextHolder.getSessionContext().getAttribute("curr_member");
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			chatList = this.chatManager.findByAll(member.getMember_id(),Integer.parseInt(request.getParameter("storeId")));
		}
		else{
			try {
				ThreadContextHolder.getHttpResponse().sendRedirect("../login.html");
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
