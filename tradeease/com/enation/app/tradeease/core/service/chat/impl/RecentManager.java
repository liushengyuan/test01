package com.enation.app.tradeease.core.service.chat.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.chat.Chat;
import com.enation.app.tradeease.core.model.chat.Recent;
import com.enation.app.tradeease.core.service.chat.IRecentManager;
import com.enation.eop.sdk.database.BaseSupport;
@SuppressWarnings("rawtypes")
public class RecentManager extends BaseSupport implements IRecentManager {

	@Override
	public void addRecent(Integer member_id,Integer seller_id) {
		if(this.findByMemberId(member_id)!=null&&this.findByMemberId(member_id).getRecent_id()!=""&&this.findByMemberId(member_id).getRecent_id().length()!=0){
			int judge=0;
			Recent recent = this.findByMemberId(member_id);
			String[] a = recent.getRecent_id().split(",");
			for(int i=0; i<a.length;i++){
				if(seller_id==Integer.parseInt(a[i])){
					judge=1;
				}	
			}
			if(judge==0){
				String s = recent.getRecent_id();
				s+=","+seller_id;
				recent.setRecent_id(s);
				this.baseDaoSupport.update("es_recent", recent, "member_id="+member_id);
			}
		}
		else if(this.findByMemberId(member_id)!=null){
			Recent recent = this.findByMemberId(member_id);
			recent.setRecent_id(seller_id.toString());
			this.baseDaoSupport.update("es_recent", recent, "member_id="+member_id);
		}
		else{
			Recent recent = new Recent();
			recent.setMember_id(member_id);
			recent.setRecent_id(seller_id.toString());
			this.baseDaoSupport.insert("es_recent", recent);
		}
	}

	@Override
	public Recent findById(Integer id) {
		String sql = "select * from es_recent where id=?";
		Recent recent = (Recent)this.baseDaoSupport.queryForObject(sql,Recent.class,id);
		return recent;
	}

	@Override
	public Recent findByMemberId(Integer member_id) {
		String sql = "select * from es_recent where member_id=?";
		Recent recent = (Recent)this.baseDaoSupport.queryForObject(sql, Recent.class, member_id);
		return recent;
	}

	@Override
	public void clearRecent(Integer member_id) {
		Recent recent = this.findByMemberId(member_id);
		recent.setRecent_id("");
		this.baseDaoSupport.update("es_recent", recent, "member_id="+member_id.toString());
	}

	@Override
	public List<Member> RecentSelect(Integer id) {
		int member_id = 0;
		List<Member> members = new ArrayList<Member>();
		List<Chat> chats = new ArrayList<Chat>();
		Recent recent = this.findByMemberId(id);
		if(recent!=null){
			if(recent.getRecent_id().length()!=0&&recent.getRecent_id()!=null){
				String recent_ids = recent.getRecent_id();
				String[] s = recent_ids.split(",");
	/*			for(int i=0;i<s.length;i++){
					String sql = "select * from es_member where member_id=?";
					Member member = (Member)this.baseDaoSupport.queryForObject(sql, Member.class, s[i]);
					
				}*/
				for(int i = 0;i<s.length;i++){
					String sql = "select * from es_chat where sendtime = (select MAX(sendtime) from es_chat where sender = "+id+" and sendee = ? or sender = ? and sendee = "+id+")";
					List<Chat> chat = this.baseDaoSupport.queryForList(sql,Chat.class,s[i],s[i]);
					if(chat.size()!=0){
						chats.add(chat.get(0));
					}
				}
			}
		}
		Chat[] chat = new Chat[chats.size()];
		for(int i = 0; i<chats.size();i++){
			chat[i] = chats.get(i);
		}
		for (int i = 0; i < chat.length - 1; i++) {   
			for (int j = 0; j < chat.length - 1; j++) {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date_one;
				Date date_two;
				try {
					date_one = sf.parse(chat[j].getSendtime());
					date_two = sf.parse(chat[j+1].getSendtime());
					if (date_one.getTime() < date_two.getTime()){
						Chat temp = chat[j];   
						chat[j] = chat[j + 1];   
						chat[j + 1] = temp;
					}   
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
		for(int i = 0;i<chat.length;i++){
			String sql = "select * from es_member where member_id=?";
			if((member_id = chat[i].getSender())==id){
				Member member = (Member)this.baseDaoSupport.queryForObject(sql, Member.class, chat[i].getSendee());
				members.add(member);
			}else if((member_id = chat[i].getSendee())==id){
				Member member = (Member)this.baseDaoSupport.queryForObject(sql, Member.class, chat[i].getSender());
				members.add(member);
			}
		}
		return members;
		/*for(Member m : members){
			String sql = "select * from es_chat where sender=? and sendee=?";
			this.baseDaoSupport.queryForObject(sql, Chat.class, id,)
		}*/
	}

	@Override
	public List<Chat> RecentNewChat(Integer sender) {
		String sql = "select * from es_recent where member_id = ?";
		List<Recent> recent = this.baseDaoSupport.queryForList(sql, Recent.class, sender);
		String recent_ids="";
		List<Chat> chats = new ArrayList<Chat>();
		if(recent!=null&&recent.size()!=0){
			recent_ids = recent.get(0).getRecent_id();
			String[] recents = recent_ids.split(",");
			for(int i = 0; i<recents.length ; i++){
				String sqls = "select * from es_chat where sendtime = (select MAX(sendtime) from es_chat where sender = ? and sendee = "+sender+")";
				if(this.baseDaoSupport.queryForList(sqls, Chat.class, recents[i])!=null){
					List<Chat> chat = this.baseDaoSupport.queryForList(sqls, Chat.class, recents[i]);//报错的是这句话！！！！！！！
					if(chat.size()!=0&&chat!=null)
						chats.add(chat.get(0));
				}
			}
		}
		return chats;
	}
}
