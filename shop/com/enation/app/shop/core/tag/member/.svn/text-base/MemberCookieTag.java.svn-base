package com.enation.app.shop.core.tag.member;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.EncryptionUtil1;
import com.enation.framework.util.HttpUtil;

import freemarker.template.TemplateModelException;
/**
 * 登录页面通过判断Cookie中是否存在JavaShopUser内容是否存在，来进行显示。
 * （记住我功能）
 * 2016/2/24
 * @author zmm
 *
 */
@Component
@Scope("prototype")
public class MemberCookieTag extends BaseFreeMarkerTag {
	private IMemberManager memberManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map result = new HashMap();
		String username=null;
		String password=null;
		Member member = new Member();
		String cookieValue = HttpUtil.getCookieValue(ThreadContextHolder.getHttpRequest(), "JavaShopUser");
		try {
			if (cookieValue != null && !cookieValue.equals("")) {
				cookieValue = URLDecoder.decode(cookieValue, "UTF-8");
				cookieValue = EncryptionUtil1.authcode(cookieValue, "DECODE","", 0);
			}
			if (cookieValue != null && !cookieValue.equals("")) {

				Map map = (Map) JSONObject.toBean(
						JSONObject.fromObject(cookieValue),
						Map.class);
				if (map != null) {
					username = map.get("username")
							.toString();
					password = map.get("password")
							.toString();
					member = this.memberManager.getmemberWithCookie(username,password);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		result.put("username", username);
		result.put("password",password);
		return result;
	}
	public IMemberManager getMemberManager() {
		return memberManager;
	}
	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	
}
