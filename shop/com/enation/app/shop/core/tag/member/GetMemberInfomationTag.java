package com.enation.app.shop.core.tag.member;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class GetMemberInfomationTag extends BaseFreeMarkerTag{
	private IMemberManager memberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		String member_id=request.getParameter("member_id");
		Member member=memberManager.getmember(Integer.parseInt(member_id));
		return member;
	}
	public IMemberManager getMemberManager() {
		return memberManager;
	}
	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

}
