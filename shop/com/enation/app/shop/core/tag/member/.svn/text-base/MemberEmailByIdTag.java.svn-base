package com.enation.app.shop.core.tag.member;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.EncryptionUtil1;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class MemberEmailByIdTag extends BaseFreeMarkerTag {
	private IMemberManager memberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result = new HashMap();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		
			String s = ThreadContextHolder.getHttpRequest().getParameter("s");
			String str = EncryptionUtil1.authcode(s, "DECODE","",0);
			String[] array = StringUtils.split(str,",");
			if(language.equals("zh")){
				if(array.length!=2) throw new RuntimeException("验证字串不正确");
			}else{
				if(array.length!=2) throw new RuntimeException("Слова проверки неправильные");
			}
			int memberid  = Integer.valueOf(array[0]);
			long regtime = Long.valueOf(array[1]);
			Member member = memberManager.get(memberid);
			if(member==null){
				member=new Member();
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
