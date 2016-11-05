package com.enation.app.shop.component.gift.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.enation.app.base.core.model.MemberLv;
import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.gift.api.Eventmessage;
import com.enation.app.shop.component.gift.model.Gift;
import com.enation.app.shop.component.gift.service.IGiftManager;
import com.enation.app.shop.core.model.SpecValue;

import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
@Component

public class GiftManager extends BaseSupport implements IGiftManager{

	
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(Gift gift) {
		gift.setSave_time(DateUtil.getDateline());
		this.baseDaoSupport.insert("es_gift", gift);
		int giftid = this.baseDaoSupport.getLastId("gift");
		gift.setGift_id(giftid);
	}
	/**
	 * 保存发布信息
	 */
	@Override
	public void save(Eventmessage event) {
		this.baseDaoSupport.insert("es_eventmessage", event);
	}
	@Override
	public void edit(Gift gift) {
		this.baseDaoSupport.update("es_gift", gift, "gift_id=" + gift.getGift_id());
		
	}
	@Override
	public void delete(Integer[] id) {
		if (id == null || id.equals(""))
			return;
		String id_str = StringUtil.arrayToString(id, ",");
		String sql = "delete from es_gift where gift_id in (" + id_str + ")";
		this.baseDaoSupport.execute(sql);
		
	}


	

	@Override
	public List<Gift> getGift() {
		String sql = " select * from es_gift "; 
		List<Gift> studentList = this.baseDaoSupport.queryForList(sql,Gift.class);
		return studentList;
	}

	@Transactional(propagation = Propagation.REQUIRED)  
	public List list() {
		String sql ="select * from es_gift order by gift_id desc";
		return this.baseDaoSupport.queryForList(sql);
	}

	@Override
	public Gift get(Integer giftId) {
		if(giftId!=null&&giftId!=0){
			String sql = "select * from es_gift where gift_id= ?";
			Gift gift = (Gift) this.baseDaoSupport.queryForObject(sql,Gift.class,giftId);
			return gift;
		}else{
			return null;
		}
	}

	@Override
	public Page list(String order, int page, int pageSize) {
		order = order == null ? " gift_id desc" : order;
		String sql = "select * from es_gift ";
		sql += " order by  " + order;
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}
	/**
	 * 当后台反悔发布，需对之前发布的消息撤销
	 * @param gift_id
	 * @return
	 */
	@Override
	public List<Eventmessage> geteventList(Integer gift_id) {
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" select * from es_eventmessage e where e.gift_id=? ");
		List<Eventmessage>eventlist=this.baseDaoSupport.queryForList(sqlBuffer.toString(), Eventmessage.class,gift_id);
		if(eventlist.size()>0){
			return eventlist;
		}else{
			return null;
		}
		
	}
	/**
	 * 删除已经发布过的消息
	 */
	@Override
	public void deleteEventMessage(Integer gift_id) {
		StringBuffer sqldel = new StringBuffer();
		sqldel.append(" delete from  es_eventmessage where gift_id=? ");
		this.baseDaoSupport.execute(sqldel.toString(), gift_id);
		
	}
	@Override
	public List<Eventmessage> geteventForarray(Integer[] gift_id) {
		String id_str = StringUtil.arrayToString(gift_id, ",");
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append(" select * from es_eventmessage e where e.gift_id in ( " );
		sqlBuffer.append( id_str +") ");
		List<Eventmessage>eventlist=this.baseDaoSupport.queryForList(sqlBuffer.toString(), Eventmessage.class);
		if(eventlist.size()>0){
			return eventlist;
		}else{
			return null;
		}
		
	}
	@Override
	public void deleteEventMessage1(Integer[] gift_id) {
		String id_str = StringUtil.arrayToString(gift_id, ",");
		StringBuffer sqldel = new StringBuffer();
		sqldel.append(" delete from  es_eventmessage where gift_id  in ( " );
		sqldel.append( id_str +") ");
		this.baseDaoSupport.execute(sqldel.toString());
		
	}
	@Override
	public void updateEventMessage(Eventmessage ev) {
		
		this.baseDaoSupport.update("es_eventmessage", ev,"event_id="+ev.getEvent_id());
	}
/*	@Override
	public Page accList(Integer member_id, int page, int pageSize,
			long send_time, String rule, String eventname) {
		String sql="SELECT * from es_eventmessage  where member_id=?";
		Page page1=this.daoSupport.queryForPage(sql, page, pageSize, member_id);
		return page1;
	}*/
	@Override
	public Page accList(String member_id, int page, int pageSize,
			String send_time, String rule, String eventname) {
		String sql="SELECT * from es_eventmessage  where member_id=?";
		Page page1=this.daoSupport.queryForPage(sql, page, pageSize, member_id);
		return page1;
	}
	
	
	

	

}
