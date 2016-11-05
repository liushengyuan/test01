package com.enation.app.tradeease.core.tag.chat;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.service.chat.IRecentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/*
 * 返回每个联系人最新一条消息list
 * 用于判断消息是否已读的标识
 * 1为已读2为未读
 * 遍历出2时在名字后面加（新）用于提醒
 * 
 */

@Component
@Scope("prototype")
public class MsgNewTag extends BaseFreeMarkerTag {
	private IRecentManager recentManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = (Member)ThreadContextHolder.getSessionContext().getAttribute("curr_member");
		return this.recentManager.RecentNewChat(member.getMember_id());
	}
	public IRecentManager getRecentManager() {
		return recentManager;
	}
	public void setRecentManager(IRecentManager recentManager) {
		this.recentManager = recentManager;
	}

}
