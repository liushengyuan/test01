package com.enation.app.tradeease.core.tag.chat;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.chat.Recent;
import com.enation.app.tradeease.core.service.chat.IRecentManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/*
 * 获取最近联系人的list
 * 
 */

@Component
@Scope("prototype")
public class RecentTag extends BaseFreeMarkerTag {
	private IRecentManager recentManager;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = (Member) ThreadContextHolder.getSessionContext().getAttribute("curr_member");
		return this.recentManager.RecentSelect(member.getMember_id());
	}
	public IRecentManager getRecentManager() {
		return recentManager;
	}
	public void setRecentManager(IRecentManager recentManager) {
		this.recentManager = recentManager;
	}

}
