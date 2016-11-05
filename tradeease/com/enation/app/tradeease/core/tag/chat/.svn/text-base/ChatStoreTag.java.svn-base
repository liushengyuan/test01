package com.enation.app.tradeease.core.tag.chat;
/*
 * 
 * 
 * 获取聊天对象的详细member信息
 * 
 * 
 */
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class ChatStoreTag extends BaseFreeMarkerTag {
	private IMemberManager memberManager;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String id = request.getParameter("storeId");
		Member member = null;
		if(id!=""&&id!=null&&id.length()!=0){
			member = this.memberManager.get(Integer.parseInt(id));
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
