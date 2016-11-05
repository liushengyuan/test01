package com.enation.app.b2b2c.core.tag.store;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;


import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
public class SellerInfoDetailTag extends BaseFreeMarkerTag {
	private IMemberManager memberManager;
	

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Member member = UserConext.getCurrentMember();
		String ctx = this.getRequest().getContextPath();
		HttpServletResponse response= ThreadContextHolder.getHttpResponse();
		if(member!=null){
			member=memberManager.getmember(member.getMember_id());
		}
		if(member==null){
				try {
					response.sendRedirect(ctx+"/login.html");
				} catch (IOException e) {
					
					e.printStackTrace();
				}
		}
		return member;
	}


	public IMemberManager getMemberManager() {
		return memberManager;
	}


	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	
}
