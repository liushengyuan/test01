package com.enation.app.shop.component.gift.service;

import java.util.List;


import com.enation.app.base.core.model.MemberLv;
import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.gift.api.Eventmessage;
import com.enation.app.shop.component.gift.model.Gift;
import com.enation.app.shop.core.model.SpecValue;
import com.enation.framework.database.Page;

public interface IGiftManager {
	/**
	 * 添加优惠活动
	 */
	public void add(Gift gift);
	/**
	 * 修改优惠活动
	 * @param gift
	 */
	public void edit(Gift gift);
	/**
	 * 删除优惠活动
	 */
	public void delete(Integer[] id);
	
	/**
	 * 分页读取优惠活动
	 */
	public Page list(String order,int page,int pageSize);
	public List<Gift> getGift();
	
	public List list();
	/**
	 * 获得一个活动类别
	 * @param lv_id
	 * @return
	 */
	public Gift get(Integer giftId);
	/**
	 * 保存发布信息
	 */
	public void save(Eventmessage event); 
	/** * 当后台反悔发布，需对之前发布的消息撤销
	 * @param gift_id
	 * @return
	 */
	public List<Eventmessage> geteventList(Integer gift_id);
	/**
	 * 当后台将发布后的活动修改为“保存”系统讲自动删除es_eventmessage表中发布过的活动。
	 * 活动内容仅保存在es_gift表中
	 * @param gift_id
	 */
	public void deleteEventMessage(Integer gift_id);
	public List<Eventmessage> geteventForarray(Integer[] gift_id);
	public void deleteEventMessage1(Integer[] gift_id);
	public void updateEventMessage(Eventmessage ev);
	
	/**
	 * 分页显示活动条数
	 * SELECT * from es_eventmessage  where member_id=10000
	 */
	public Page accList(String member_id, int page, int pageSize,
			String send_time,  String rule,String eventname);
}
