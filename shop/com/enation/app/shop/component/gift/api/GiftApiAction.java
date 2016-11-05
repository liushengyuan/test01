package com.enation.app.shop.component.gift.api;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.CurrencyUtil;

@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("member")
@Results({
	@Result(name="gift", type="freemarker", location="/shop/admin/gift/gift_list.html"),
	@Result(name="edit_lv", type="freemarker", location="/shop/admin/member/lv_edit.html"),
	@Result(name="list_lv", type="freemarker", location="/shop/admin/member/lv_list.html"),
})
@SuppressWarnings({ "rawtypes", "unchecked", "serial","static-access" })
public class GiftApiAction extends WWAction{
	private Integer id;
	private String name;
	private Long start_time;
	private Long end_time;
	private String rule;   //活动规则
	
	/**
	 * 跳转至添加优惠活动页面
	 * @return 添加优惠等级页面
	 */
	public String add_gift() {
		return "add_gift";
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getStart_time() {
		return start_time;
	}
	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}
	public Long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	public String getRule() {
		return rule;
	}
	public void setRule(String rule) {
		this.rule = rule;
	}
}
