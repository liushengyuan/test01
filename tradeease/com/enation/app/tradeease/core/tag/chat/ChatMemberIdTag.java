package com.enation.app.tradeease.core.tag.chat;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.IMemberManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ChatMemberIdTag extends BaseFreeMarkerTag {
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer member_id = 0;
		if(UserConext.getCurrentMember()!=null){
			member_id = UserConext.getCurrentMember().getMember_id();
		}
		return member_id;
	}

}
