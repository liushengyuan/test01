package com.enation.app.tradeease.core.tag.accont;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.service.accont.IAccountDetailManager;
import com.enation.app.tradeease.core.service.accont.IMyAccountManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;
/**
 * 我的账户资金查询
 * 
 * @author jfw
 * 
 */
@Component
@Scope("prototype")
public class MyAccountTag extends BaseFreeMarkerTag {
	
	private IMyAccountManager myAccountManager;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			throw new TemplateModelException("未登录不能使用此标签");
		}
		//获取查询时间
		String start_myaccount_time = request.getParameter("start_myaccount_time");
		String end_myaccount_time = request.getParameter("end_myaccount_time");
		long start_time = 0L;
		long end_time = 0L;
		if (!StringUtil.isEmpty(start_myaccount_time)) {
			start_time = DateUtil.getTimeline(start_myaccount_time);
		}
		if (!StringUtil.isEmpty(end_myaccount_time)) {
			end_time = DateUtil.getTimeline(end_myaccount_time);
		}
		Page page = this.myAccountManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
		
		return page;
	}

	public IMyAccountManager getMyAccountManager() {
		return myAccountManager;
	}

	public void setMyAccountManager(IMyAccountManager myAccountManager) {
		this.myAccountManager = myAccountManager;
	}
	
	
	

}
