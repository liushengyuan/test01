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
import com.enation.app.shop.core.service.impl.MemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.EncryptionUtil1;

import freemarker.template.TemplateModelException;
/**
 *  邮箱验证标签
 * @author lina
 *2014-6-11下午5:54:05
 */
@Component
@Scope("prototype")
public class MemberEmailCheckTag extends BaseFreeMarkerTag {
	private IMemberManager memberManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result = new HashMap();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try{
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
			if(member.getRegtime() != regtime){
				result.put("result", 0);
				if(language.equals("zh")){
					result.put("message", "验证字串不正确");
				}else{
					result.put("message", "Слова проверки неправильные");
				}
				return result;
			}
			if(member.getIs_cheked()==0){
				memberManager.checkEmailSuccess( member);
				result.put("result", 1);
				if(language.equals("zh")){
					result.put("message", member.getUname() +"您好，您的邮箱验证成功!");
				}else{
					result.put("message", member.getUname() +" Здравствуйте, ваш почтовый ящик проверить успешно!");
				}
			}else{
				result.put("result", 0);
				if(language.equals("zh")){
					result.put("message", member.getUname() +"您好，验证失败，您已经验证过该邮箱!");
				}else{
					result.put("message", member.getUname() +" Здравствуйте, проверить не успешно, вы уже поверили этот почтовый ящик!");
				}
			}
		}catch(RuntimeException e){
			result.put("result", 0);
			e.printStackTrace();
			if(language.equals("zh")){
				result.put("message", "验证地址不正确");
			}else{
				result.put("message", "Неправильный адрес проверки");
			}
		}
		return result;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

}
