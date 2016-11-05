package com.enation.app.b2b2c.core.model.storeDTO;

public class storeAndMemberDTO {
private Integer member_id;//用户id
private String uname;//用户登录名
private Long regtime;//用户注册时间
private String realname;//真实姓名
private Integer store_id;//店铺id
private String store_name;//店铺名称
private String  attr;	//详细地址
private int store_type;//店铺类型，1个人，2公司
private Long  create_time;	//创建时间
private String  description;//店铺简介
private String prove_name;//财务联系人、银行卡所有人 第一联系人
private String prove_mobile;//财务联系人、银行卡所有人的联系手机
private Long check_time;//审核时间
private String check_description;//审核理由
private Double 	commission;	//店铺佣金比例
private int  disabled;//店铺状态  0待审核  1审核通过  -1审核未通过 -2所有  2关闭中

public Integer getMember_id() {
	return member_id;
}
public void setMember_id(Integer member_id) {
	this.member_id = member_id;
}

public String getUname() {
	return uname;
}
public void setUname(String uname) {
	this.uname = uname;
}
public Long getRegtime() {
	return regtime;
}
public void setRegtime(Long regtime) {
	this.regtime = regtime;
}
public String getRealname() {
	return realname;
}
public void setRealname(String realname) {
	this.realname = realname;
}
public Integer getStore_id() {
	return store_id;
}
public void setStore_id(Integer store_id) {
	this.store_id = store_id;
}
public String getStore_name() {
	return store_name;
}
public void setStore_name(String store_name) {
	this.store_name = store_name;
}

public String getAttr() {
	return attr;
}
public void setAttr(String attr) {
	this.attr = attr;
}
public int getStore_type() {
	return store_type;
}
public void setStore_type(int store_type) {
	this.store_type = store_type;
}
public Long getCreate_time() {
	return create_time;
}
public void setCreate_time(Long create_time) {
	this.create_time = create_time;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public String getProve_name() {
	return prove_name;
}
public void setProve_name(String prove_name) {
	this.prove_name = prove_name;
}
public String getProve_mobile() {
	return prove_mobile;
}
public void setProve_mobile(String prove_mobile) {
	this.prove_mobile = prove_mobile;
}
public Long getCheck_time() {
	return check_time;
}
public void setCheck_time(Long check_time) {
	this.check_time = check_time;
}
public String getCheck_description() {
	return check_description;
}
public void setCheck_description(String check_description) {
	this.check_description = check_description;
}
public Double getCommission() {
	return commission;
}
public void setCommission(Double commission) {
	this.commission = commission;
}
public int getDisabled() {
	return disabled;
}
public void setDisabled(int disabled) {
	this.disabled = disabled;
}

}
