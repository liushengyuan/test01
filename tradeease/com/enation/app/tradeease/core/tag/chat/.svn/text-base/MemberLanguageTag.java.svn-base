package com.enation.app.tradeease.core.tag.chat;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.service.chat.IChatManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/*
 * 登录用户最近一条消息的对象
 * 用于判断select中默认语言
 * 
 */

@Component
@Scope("prototype")
public class MemberLanguageTag extends BaseFreeMarkerTag {
	private IChatManager chatManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = (Member)ThreadContextHolder.getSessionContext().getAttribute("curr_member");
		return this.chatManager.selectFromLanguage(member.getMember_id());
	}
	public IChatManager getChatManager() {
		return chatManager;
	}
	public void setChatManager(IChatManager chatManager) {
		this.chatManager = chatManager;
	}

}
