package com.enation.app.tradeease.core.service.chat;

import java.util.List;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.model.chat.Recent;

public interface IRecentManager {
	public void addRecent(Integer member_id,Integer seller_id);
	public Recent findById(Integer id);
	public Recent findByMemberId(Integer member_id);
	public void clearRecent(Integer member_id);
	public List<Member> RecentSelect(Integer id);
	public List<Chat> RecentNewChat(Integer sender);
}
