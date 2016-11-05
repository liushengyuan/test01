package com.enation.app.b2b2c.core.action.api.member;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.member.IBuyerMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("buyerMember")
@SuppressWarnings({ "rawtypes", "unchecked", "serial", "static-access" })
public class BuyerMemberApiAction extends WWAction {

	private IBuyerMemberManager buyerMemberManager;

	private String email;

	/**
	 * @Description: 修改会员邮箱
	 * @param email
	 *            :会员设置的新邮箱
	 * @return json字串 result 为1表示修改成功，0表示失败 ，int型 message 为提示信息 ，String型
	 */
	@SuppressWarnings("unused")
	public String changeEmail() {
		Member member = UserConext.getCurrentMember();
		Integer memberid = member.getMember_id();
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String wuquan=this.getText("buyerMember.noquan");
		if (member == null) {
			this.showErrorJson(wuquan);
			return this.JSON_MESSAGE;
		}
		String EmailNotNull =this.getText("buyerMember.EmailNotNull");
		String EmailFormatErr = this.getText("buyerMember.EmailFormatErr");
		String FixEmailSucess = this.getText("buyerMember.FixEmailSucess");
		String FixEmailErro =this.getText("buyerMember.FixEmailErro");
		/*if(language=="zh"){
			 EmailNotNull ="注册邮箱不能为空！";
			 EmailFormatErr = "注册邮箱格式不正确！";
			 FixEmailSucess = "修改邮箱成功！";
			 FixEmailErro = "修改邮箱失败！";
		}else{
			 EmailNotNull ="Строка 'Email' должна быть заполнена";
			 EmailFormatErr = "Неправильный email";
			 FixEmailSucess = "Email был успешно изменен";
			 FixEmailErro = "Изменения почта не";
		}*/
		String email = this.getEmail();
		if (StringUtil.isEmpty(email)) {
			this.showErrorJson(EmailNotNull);
			return this.JSON_MESSAGE;
		}
		if (!StringUtil.validEmail(email)) {
			this.showErrorJson(EmailFormatErr);
			return this.JSON_MESSAGE;
		}
		try {
			buyerMemberManager.updateEmail(memberid, email);
			this.showSuccessJson(FixEmailSucess);
		} catch (Exception e) {
			if (this.logger.isDebugEnabled()) {
				logger.error(e.getStackTrace());
			}
			this.showErrorJson(FixEmailErro);
		}

		return WWAction.JSON_MESSAGE;
	}

	public IBuyerMemberManager getBuyerMemberManager() {
		return buyerMemberManager;
	}

	public void setBuyerMemberManager(IBuyerMemberManager buyerMemberManager) {
		this.buyerMemberManager = buyerMemberManager;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
