package com.enation.app.tradeease.core.tag.message;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.tradeease.core.service.message.IBuyerMessageManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 买家中心站内信 收到的消息标签
 * 
 * @author yanpeng 2015-6-12 下午6:17:17
 * 
 */
@Component
@Scope("prototype")
public class AcceptMessageTag extends BaseFreeMarkerTag {
	private IBuyerMessageManager buyerMessageManager;
	private IMemberManager memberManager;

	@SuppressWarnings("rawtypes")
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Member member = UserConext.getCurrentMember();
		//Integer buyMemberid = 0;
		if (member == null) {
			throw new TemplateModelException("未登录不能使用此标签");
		}
		String start_message_time = request.getParameter("start_message_time");
		String end_message_time = request.getParameter("end_message_time");
		String message_state = request.getParameter("message_state");
//		String goods_id = request.getParameter("goods_id");
		String goods_id = null;
//		String buyer_name = request.getParameter("buyer_name");
		String buyer_name = null;
		/*if (!StringUtil.isEmpty(buyer_name)) {
			Member buyerMember = memberManager.getMemberByUname(buyer_name);
			if (buyerMember != null) {
				buyMemberid = buyerMember.getMember_id();
			}else{
				buyMemberid=-1;
			}
		}*/
		long start_time = 0l;
		long end_time = 0l;
		if (!StringUtil.isEmpty(start_message_time)) {
			start_time = DateUtil.getTimeline(start_message_time);
		}
		if (!StringUtil.isEmpty(end_message_time)) {
			end_time = DateUtil.getTimeline(end_message_time);
		}
		Page page = buyerMessageManager.acceptList(member.getMember_id(),
				this.getPage(), this.getPageSize(), start_time, end_time,
				message_state, goods_id, buyer_name);
		return page;
	}

	public IBuyerMessageManager getBuyerMessageManager() {
		return buyerMessageManager;
	}

	public void setBuyerMessageManager(IBuyerMessageManager buyerMessageManager) {
		this.buyerMessageManager = buyerMessageManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

}
