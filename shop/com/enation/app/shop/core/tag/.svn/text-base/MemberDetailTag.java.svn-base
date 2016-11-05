package com.enation.app.shop.core.tag;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class MemberDetailTag extends BaseFreeMarkerTag {
	private IMemberManager memberManager;

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletResponse response=ThreadContextHolder.getHttpResponse();
         Member member = UserConext.getCurrentMember();
         Map map = new HashMap();
		if( member==null){
			try {
				response.sendRedirect("/login.html?forward=brand_follow.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
		
			int memberid =member.getMember_id();
			Member member1= this.memberManager.getmember(memberid);
			int count = this.memberManager.getMemberBrandCount(memberid);
			map.put("member", member1);
			map.put("count", count);
			return map;
		}
		return map;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
}
