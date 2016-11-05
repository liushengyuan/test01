package com.enation.app.tradeease.core.tag.chat;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/*
 * 登录用户信息
 * 
 */

@Component
@Scope("prototype")
public class ChatUserIdTag extends BaseFreeMarkerTag {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = (Member) ThreadContextHolder.getSessionContext().getAttribute("curr_member");
		return member;
	}

}
