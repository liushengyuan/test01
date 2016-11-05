package com.enation.app.shop.component.gift.action;

import java.io.File;
import java.util.Date;
import java.util.EventListener;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.component.gift.api.Eventmessage;
import com.enation.app.shop.component.gift.model.Gift;
import com.enation.app.shop.component.gift.service.IGiftManager;
import com.enation.app.shop.component.spec.service.ISpecManager;
import com.enation.app.shop.component.spec.service.ISpecValueManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;

@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("gift")
@Results({
	@Result(name="list", type="freemarker", location="/com/enation/app/shop/component/gift/action/html/gift_list.html") ,
	@Result(name="add_gift", type="freemarker", location="/com/enation/app/shop/component/gift/action/html/gift_add.html") ,
	@Result(name="edit_gift", type="freemarker", location="/com/enation/app/shop/component/gift/action/html/gift_edit.html")
})
public class GiftAction extends WWAction{
	private static final Eventmessage event = null;
	private IGiftManager giftManager;
	private ISpecManager specManager;
	private IRegionsManager regionsManager;
	private ISpecValueManager specValueManager;
	private String name;	//活动名称
	private Date start_time;
	private Date end_time;
	private String rule;   //活动规则
	private List giftList;
	private int valueid;
	private Gift gift;
	private Member member;
	private Integer giftId;
	private List provinceList;
	private Integer[] gift_id;
	private Integer state; //信息发送状态
	
	private Integer type;//活动类型
	private Long save_time;//保存时间
	private Long send_time;//发送时间
	
	private IMemberManager memberManager;
	
	private Integer event_id;//关联卖家ID
	private Integer member_id;//卖家ID
	

	/**
	 * 跳转至修改优惠活动页面
	 * @param 
	 * @param 
	 * @return 
	 */
	public String edit_gift() {
		 gift= this.giftManager.get(giftId);
		return "edit_gift";
	}
	
	/**
	 * 检测优惠活动管理是否被使用
	 * 
	 * @param features_id
	 *            优惠活动管理Id数组,Integer[]
	 * @return json result 1.使用.0.未使用
	 */
	public String checkUsed() {
		if (this.specManager.checkUsed(gift_id)) {
			this.json = "{result:1}";
		} else {
			this.json = "{result:0}";
		}

		return this.JSON_MESSAGE;
	}
	
	
	/**
	 * 检测某个优惠活动管理值 是否被使用
	 * 
	 * @param valueid
	 *   
	 * @return json result 1.使用.0.未使用
	 */
	public String checkValueUsed() {

		boolean isused = this.specManager.checkUsed(valueid);

		if (isused) {
			this.json = "{result:1}";
		} else {
			this.json = "{result:0}";
		}

		return this.JSON_MESSAGE;

	}
	
	/**
	 * 跳转至添加优惠活动页面
	 * @return 添加优惠等级页面
	 */

	public String list() {
		return "list";
	}
	/**
	 * 跳转至添加优惠活动页面
	 * @return 添加优惠活动页面
	 */
	public String add_gift() {
		return "add_gift";
	}
	
	/**
	 * 跳转至添加优惠活动页面
	 * 
	 * @return 添加产品特点页面
	 */
	public String add() {
		return this.INPUT;
	}
	
	


	/**
	 * 获取优惠活动列表Json
	 * @return 获取优惠活动列表Json
	 */
	public String gift_listJson() {
		this.webpage = giftManager.list(this.getSort(), this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	/**
	 * 获取优惠活动列表Json
	 * 
	 * @author b'x'r
	 * @return 获取优惠活动列表Json
	 */
	@SuppressWarnings("unused")
	public String listJson() {
		giftList = giftManager.getGift();
		this.showGridJson(giftList);
		return JSON_MESSAGE;
	}
	
	/**
	 * 保存发布信息
	 * 0为保存，1为发布
	 * @return
	 */
	public String saveAdd() {
		try {	
			gift.setState(state);
		if(state==1){
			if(end_time.getTime()<start_time.getTime()){
				this.showErrorJson("截止时间小于开始时间");
				return JSON_MESSAGE;
			}
			gift.setStart_time(start_time.getTime()/1000);
			gift.setEnd_time(end_time.getTime()/1000);
			this.giftManager.add(gift);
			//查出所有卖家
			List<Member> memberlist = this.memberManager.memberList();
				if(memberlist.size()>0){		
					for(Member m: memberlist){
						Eventmessage event=new Eventmessage();
						event.setGift_id(gift.getGift_id());
						event.setMember_id(m.getMember_id());
						event.setEventname(gift.getName());
						event.setRule(gift.getRule());
						event.setSend_time(gift.getSend_time());
						event.setState(0);
						this.giftManager.save(event);	
					}
				}
		}else if(state==0){
			if(end_time.getTime()<start_time.getTime()){
				this.showErrorJson("截止时间小于开始时间");
				return JSON_MESSAGE;
			}
			gift.setStart_time(start_time.getTime()/1000);
			gift.setEnd_time(end_time.getTime()/1000);
			this.giftManager.add(gift);
		}		
			this.showSuccessJson("优惠活动添加成功");
		} catch (RuntimeException e) {
			this.showErrorJson("新增优惠活动失败");
		}
		return JSON_MESSAGE;
	}
	/**
	 * 保存发布信息
	 * @return
	 */
/*	public String savePublish(){
		gift.setState(1);
		this.giftManager.edit(gift);
		//查出所有卖家
		List<Member> memberlist = this.memberManager.memberList();
		if(memberlist.size()>0){
			Event event=new Event();
			event.setGift_id(gift.getGift_id());
			event.setMember_id(member.getMember_id());
			event.setName(gift.getName());
			event.setRule(gift.getRule());
			event.setSend_time(gift.getSend_time());
			event.setState(0);
			this.giftManager.save(event);	
		}
		return gift_listJson();
	}*/


	
	public String saveEditGift() {
		
		try{
			if(state==1){
				gift.setSend_time(DateUtil.getDateline());
				gift.setState(state);
				giftManager.edit(gift);
				//查出所有已发的消息
				List<Eventmessage> eventList = this.giftManager.geteventList(gift.getGift_id());
					if(eventList!=null &&   eventList.size()>0){		
						for(Eventmessage ev: eventList){
							ev.setEventname(gift.getName());
							ev.setRule(gift.getRule());
							ev.setSend_time(gift.getSend_time());
							ev.setState(0);
							this.giftManager.updateEventMessage(ev);	
						}
					}else{
						List<Member> memberlist = this.memberManager.memberList();
						if(memberlist.size()>0){		
							for(Member m: memberlist){
								Eventmessage event=new Eventmessage();
								event.setGift_id(gift.getGift_id());
								event.setMember_id(m.getMember_id());
								event.setEventname(gift.getName());
								event.setRule(gift.getRule());
								event.setSend_time(gift.getSend_time());
								event.setState(0);
								this.giftManager.save(event);	
							}
						}
					}
					
			}else if(state==0){
				gift.setState(0);
				this.giftManager.edit(gift);
				List<Eventmessage>eventslist = this.giftManager.geteventList(gift.getGift_id());
				if(eventslist!=null && eventslist.size()>0){
						this.giftManager.deleteEventMessage(gift.getGift_id());	
				}
				
			}
			this.showSuccessJson("优惠活动修改成功");
			
		}catch (Exception e) {
			this.showErrorJson("非法参数");
		}
		
		return JSON_MESSAGE;
	}
	
	/**
	 * 删除优惠活动
	 * @param lv_id,会员等级Id,Integer
	 * @return  result
	 * result 1.操作成功.0.操作失败
	 */
	public String deleteGift() {
		try {
			List<Eventmessage>eventslist = this.giftManager.geteventForarray(gift_id);
			if(eventslist!=null && eventslist.size()>0){
					this.giftManager.deleteEventMessage1(gift_id);	
			}
			
			this.giftManager.delete(gift_id);
			this.showSuccessJson("删除成功");
		} catch (RuntimeException e) {
			this.showErrorJson("删除失败");
		}
		return this.JSON_MESSAGE;
	}
	
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
	public Date getStart_time() {
		return start_time;
	}

	public void setStart_time(Date start_time) {
		this.start_time = start_time;
	}

	public Date getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}

	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
	public IGiftManager getGiftManager() {
		return giftManager;
	}

	public void setGiftManager(IGiftManager giftManager) {
		this.giftManager = giftManager;
	}

	public ISpecManager getSpecManager() {
		return specManager;
	}

	public void setSpecManager(ISpecManager specManager) {
		this.specManager = specManager;
	}

	public ISpecValueManager getSpecValueManager() {
		return specValueManager;
	}

	public void setSpecValueManager(ISpecValueManager specValueManager) {
		this.specValueManager = specValueManager;
	}

	public List getGiftList() {
		return giftList;
	}

	public void setGiftList(List giftList) {
		this.giftList = giftList;
	}

	public int getValueid() {
		return valueid;
	}

	public void setValueid(int valueid) {
		this.valueid = valueid;
	}

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	
	public IRegionsManager getRegionsManager() {
		return regionsManager;
	}

	public void setRegionsManager(IRegionsManager regionsManager) {
		this.regionsManager = regionsManager;
	}

	public Integer[] getGift_id() {
		return gift_id;
	}

	public void setGift_id(Integer[] gift_id) {
		this.gift_id = gift_id;
	}

	public Integer getGiftId() {
		return giftId;
	}

	public void setGiftId(Integer giftId) {
		this.giftId = giftId;
	}

	public List getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List provinceList) {
		this.provinceList = provinceList;
	}

	public void setId(Integer[] gift_id) {
		this.gift_id = gift_id;
	}
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getSave_time() {
		return save_time;
	}

	public void setSave_time(Long save_time) {
		this.save_time = save_time;
	}

	public Long getSend_time() {
		return send_time;
	}

	public void setSend_time(Long send_time) {
		this.send_time = send_time;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public Integer getEvent_id() {
		return event_id;
	}

	public void setEvent_id(Integer event_id) {
		this.event_id = event_id;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

}
