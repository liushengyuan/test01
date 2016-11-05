package com.enation.app.tradeease.core.tag.trade;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.service.trade.ITradeManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 放款详情
 * 
 * @author jfw
 * 
 */
@Component
@Scope("prototype")
public class TradeTag extends BaseFreeMarkerTag {
	private ITradeManager tradeManager;

	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			throw new TemplateModelException("未登录不能使用此标签");
		}
		String start_trade_time = request.getParameter("start_trade_time");
		String end_trade_time = request.getParameter("end_trade_time");
		String searchTime = request.getParameter("searchTime");
		long start_time = 0L;
		long end_time = 0L;
		if(searchTime==null || searchTime.equals("0")){
			if (!StringUtil.isEmpty(start_trade_time)) {
				start_time = DateUtil.getTimeline(start_trade_time);
			}
			if (!StringUtil.isEmpty(end_trade_time)) {
				end_time = DateUtil.getTimeline(end_trade_time);
			}
			
			
			Page page = this.tradeManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
			return page;
			
		}else if (searchTime.equals("1")) {
			end_time = new Date().getTime() / 1000L;
			start_time = end_time - 2678400;
			
			Page page = this.tradeManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
			return page;
			
		}else if (searchTime.equals("3")) {
			end_time = new Date().getTime() / 1000L;
			start_time = end_time - 2678400*3;

			Page page = this.tradeManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
			return page;
			
		}else if (searchTime.equals("6")) {
			end_time = new Date().getTime() / 1000L;
			start_time = end_time - 2678400*6;
			
			Page page = this.tradeManager.list(member.getMember_id(), this.getPage(), this.getPageSize(), start_time, end_time);
			return page;
			
		}else{
			//System.out.println("查询出错了*****************************");
			return null;
		}
		
	}

	public ITradeManager getTradeManager() {
		return tradeManager;
	}

	public void setTradeManager(ITradeManager tradeManager) {
		this.tradeManager = tradeManager;
	}
	
	
	
}
