package com.enation.app.tradeease.core.service.chat.impl;

import java.util.ArrayList;
import java.util.List;

import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.service.chat.IChatManager;
import com.enation.app.tradeease.core.service.chat.IRecentManager;
import com.enation.eop.sdk.database.BaseSupport;

public class ChatManager extends BaseSupport<Chat> implements IChatManager {
	private IRecentManager recentManager;

	public List<Chat> findBySender(Integer id,Integer userid){
		String sql = "select * from es_chat where sender="+id+" and sendee="+userid+" or sender="+userid+" and sendee="+id+" order by sendtime desc limit 5";
		List<Chat> chats =  this.baseDaoSupport.queryForList(sql, Chat.class);
		List<Chat> chat = new ArrayList<Chat>();
		for(int i = 0 ; i < chats.size() ; i++ ){
			chat.add(chats.get(chats.size()-i-1));
		}
		return chat;
	}
	
	public List<Chat> findByAll(Integer id,Integer userid){
		String sql = "select * from es_chat where sender="+id+" and sendee="+userid+" or sender="+userid+" and sendee="+id+" order by sendtime asc";
		return this.baseDaoSupport.queryForList(sql, Chat.class);
	}

	@Override
	public void addMessage(Chat chat) {
		this.baseDaoSupport.insert("es_chat", chat);
	}
	
	

	@Override
	public Integer findByMember(Integer sender, Integer sendee) {
		Integer size = this.findBySender(sender, sendee).size();
		return size;
	}

	@Override
	public void updateState(Integer sender, Integer sendee) {
		String sql = "update es_chat set state=1 where sender=? and sendee=?";
		this.baseDaoSupport.execute(sql, sendee,sender);
	}

	@Override
	public void updateStateToNotRead(Integer sender, Integer sendee) {
		String sql = "select id from es_chat where sendtime = (select MAX(sendtime) from es_chat where sender = "+sender+" and sendee = "+sendee+")";
		int id = 0;
		if(this.baseDaoSupport.queryForList(sql)!=null&&this.baseDaoSupport.queryForList(sql).size()!=0){
			id = this.baseDaoSupport.queryForInt(sql);
			String sqls = "update es_chat set state=2 where id=?";
			this.baseDaoSupport.execute(sqls, id);
		}
	}

	@Override
	public List<Chat> selectFromLanguage(Integer sender) {
		String sql = "select * from es_chat where sendtime= (select MAX(sendtime) from es_chat where sender = ?)";
		return this.baseDaoSupport.queryForList(sql, Chat.class, sender);
	}

	@Override
	public List<Chat> topHTMLNewMsg(Integer member_id) {
		String sql = "select * from es_chat where sendtime= (select MAX(sendtime) from es_chat where sendee = ? and state=2)";
		return this.baseDaoSupport.queryForList(sql, Chat.class, member_id);
	}

	@Override
	public List<Chat> getGoodsId(Integer sender, Integer sendee) {
		String sql  = "select * from es_chat where sendtime= (select MAX(sendtime) from es_chat where sender = ? and sendee=? or sender=? and sendee=?)";
		return this.baseDaoSupport.queryForList(sql, Chat.class, sender,sendee,sendee,sender);
		
	}
	public IRecentManager getRecentManager() {
		return recentManager;
	}
	
	public void setRecentManager(IRecentManager recentManager) {
		this.recentManager = recentManager;
	}
}
