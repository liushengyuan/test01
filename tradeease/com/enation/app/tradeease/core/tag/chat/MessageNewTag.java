package com.enation.app.tradeease.core.tag.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.service.chat.IChatManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class MessageNewTag extends BaseFreeMarkerTag {
	private IChatManager chatManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List<Chat> list = new ArrayList<Chat>();
		if(UserConext.getCurrentMember()!=null){
			list = this.chatManager.topHTMLNewMsg(UserConext.getCurrentMember().getMember_id());
		}
		return list;
	}
	public IChatManager getChatManager() {
		return chatManager;
	}
	public void setChatManager(IChatManager chatManager) {
		this.chatManager = chatManager;
	}

}
