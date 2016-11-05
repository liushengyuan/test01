package com.enation.app.tradeease.core.tag.accont;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.service.accont.IAccountDetailManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 交易明细标签
 * 
 * @author jfw
 * 
 */
@Component
@Scope("prototype")
public class AccountDetailTag extends BaseFreeMarkerTag {
	
	private IAccountDetailManager accountDetailManager;
	
	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			throw new TemplateModelException("未登录不能使用此标签");
		}
		//获取查询时间
		String start_accountdetail_time = request.getParameter("start_accountdetail_time");
		String end_accountdetail_time = request.getParameter("end_accountdetail_time");
		String searchTime = request.getParameter("searchTime");
//		//System.out.println(new Date().getTime()/1000L);
//		//System.out.println(searchTime);
		long start_time = 0L;
		long end_time = 0L;
		if(searchTime==null || searchTime.equals("0")){
			if (!StringUtil.isEmpty(start_accountdetail_time)) {
				start_time = DateUtil.getTimeline(start_accountdetail_time);
			}
			if (!StringUtil.isEmpty(end_accountdetail_time)) {
				end_time = DateUtil.getTimeline(end_accountdetail_time);
			}

			Page page = this.accountDetailManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
		
			return page;
			
		}else if(searchTime.equals("1")){
			end_time = new Date().getTime() / 1000L;
			start_time = end_time - 2678400;
			////System.out.println("查询的1"+start_time);    
			
			Page page = this.accountDetailManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
			
			return page;
		}else if(searchTime.equals("3")){
			end_time = new Date().getTime() / 1000L;
			start_time = end_time - 2678400*3;
			////System.out.println("查询的3"+start_time);  
			Page page = this.accountDetailManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
			
			return page;
		}else if(searchTime.equals("6")){
			end_time = new Date().getTime() / 1000L;
			start_time = end_time - 2678400*6;
			////System.out.println("查询的6"+start_time);  
			Page page = this.accountDetailManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
			
			return page;
		}else{
			//System.out.println("查询出错了*****************************");
			return null;
		}
	}

	public IAccountDetailManager getAccountDetailManager() {
		return accountDetailManager;
	}

	public void setAccountDetailManager(IAccountDetailManager accountDetailManager) {
		this.accountDetailManager = accountDetailManager;
	}
	

}
