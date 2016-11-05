package com.enation.app.tradeease.core.action.api.chat;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.tradeease.core.model.chat.Recent;
import com.enation.app.tradeease.core.service.chat.IRecentManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("recent")
@SuppressWarnings({ "rawtypes", "unchecked", "serial", "static-access" })
public class RecentApiAction extends WWAction {
	private IRecentManager recentManager;
	private IMemberManager memberManager;
	public IRecentManager getRecentManager() {
		return recentManager;
	}

	public void setRecentManager(IRecentManager recentManager) {
		this.recentManager = recentManager;
	}

	public String deleteRecent(){
		Member member = (Member)ThreadContextHolder.getSessionContext().getAttribute("curr_member");
		try {
			recentManager.clearRecent(member.getMember_id());
			this.showSuccessJson("清空成功");
		} catch (Exception e) {
			this.showSuccessJson("清空失败");
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	public String people(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		int member_id = Integer.parseInt(request.getParameter("member_id"));
		String store_id = request.getParameter("store_id");
		Recent recent = this.recentManager.findByMemberId(member_id);
		if(recent!=null){
			String recent_id = recent.getRecent_id();
			String[] recent_ids = recent_id.split(",");
			for(int i = 0 ; i<recent_ids.length ; i++){
				if(store_id.equals(recent_ids[i])){
					this.showSuccessJson("已经有这个联系人了",1);
					return this.JSON_MESSAGE;
				}
			}
		}
		Member member = this.memberManager.get(Integer.parseInt(store_id));
		this.showSuccessJson(member.getName(),2);
		this.recentManager.addRecent(member_id, Integer.parseInt(store_id));
		return this.JSON_MESSAGE;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
}
