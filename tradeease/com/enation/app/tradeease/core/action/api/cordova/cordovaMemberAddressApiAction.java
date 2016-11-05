package com.enation.app.tradeease.core.action.api.cordova;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.eop.processor.core.Request;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("cordovaMemberAddress")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class cordovaMemberAddressApiAction extends WWAction {
	@SuppressWarnings("unused")
	private IMemberAddressManager memberAddressManager;

	private String name;
	private String tel;
	private String mobile;
	private String country;
	private String province;
	private String city;
	private String addr;
	private String zip;
	private Integer addr_id;
	private int def_addr;
	private Integer province_id;
	private Integer city_id;
	private Integer region_id;
	
	public Integer getProvince_id() {
		return province_id;
	}

	public void setProvince_id(Integer province_id) {
		this.province_id = province_id;
	}

	public Integer getCity_id() {
		return city_id;
	}

	public void setCity_id(Integer city_id) {
		this.city_id = city_id;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public int getDef_addr() {
		return def_addr;
	}

	public void setDef_addr(int def_addr) {
		this.def_addr = def_addr;
	}

	public Integer getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(Integer addr_id) {
		this.addr_id = addr_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}

	public void setMemberAddressManager(
			IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}
//查询买家的地址
	public String getAddressManager1() {

		List<MemberAddress> memberAddress = this.memberAddressManager
				.listAddress();

		showGridJson(memberAddress);
		return this.JSON_MESSAGE;

	}
//查询买家默认地址
	public String getDefaultAddress(){
		
		List<MemberAddress> memberAddress = this.memberAddressManager
				.listDefaultAddress();
		
		showGridJson(memberAddress);
		return this.JSON_MESSAGE;
	}
//设置默认地址
	public String updateDefualtAddress(){
		String addr=addr_id+"";
		this.memberAddressManager.updateAddressByMer();
		this.memberAddressManager.addressDefult(addr);
		
		this.showSuccessJson("修改成功");
		return this.JSON_MESSAGE;
	}
//添加地址
	public String updateAddressManager1() {
//		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
//		Locale locale = (Locale) session.getAttribute("locale");

		MemberAddress memberaddress = new MemberAddress();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
//		String registerip = request.getRemoteAddr();

		if (StringUtil.isEmpty(name)) {
			this.showErrorJson("姓名不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(mobile)) {
			this.showErrorJson("手机不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(country)) {
			this.showErrorJson("国家不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(province)) {
			this.showErrorJson("省份不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(city)) {
			this.showErrorJson("城市不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(addr)) {
			this.showErrorJson("详细地址不能为空！");
			return this.JSON_MESSAGE;

		}
		if (StringUtil.isEmpty(zip)) {
			this.showErrorJson("邮编不能为空！");
			return this.JSON_MESSAGE;

		}
		if(def_addr==0){
		}else{
			this.memberAddressManager.updateAddressByMer();
		}
		memberaddress.setType(0);
		memberaddress.setDef_addr(def_addr);
		memberaddress.setAddr(addr);
		memberaddress.setCity(city);
		memberaddress.setTel(tel);
		memberaddress.setName(name);
		memberaddress.setProvince(province);
		memberaddress.setZip(zip);
		memberaddress.setCountry(country);
		memberaddress.setMobile(mobile);
		int result = memberAddressManager.addAddress(memberaddress);
		this.showSuccessJson("添加成功");
		return this.JSON_MESSAGE;
		
	}
	//删除地址
	public String delete(){
		this.memberAddressManager.deleteAddress(addr_id);
		this.showSuccessJson("删除成功");
		return this.JSON_MESSAGE;
	}
	public String updateAddressManager2(){
		MemberAddress memberaddress = memberAddressManager.getAddress(addr_id);
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		if (StringUtil.isEmpty(name)) {
			this.showErrorJson("姓名不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(mobile)) {
			this.showErrorJson("手机不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(country)) {
			this.showErrorJson("国家不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(province)) {
			this.showErrorJson("省份不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(city)) {
			this.showErrorJson("城市不能为空！");
			return this.JSON_MESSAGE;
		}
		if (StringUtil.isEmpty(addr)) {
			this.showErrorJson("详细地址不能为空！");
			return this.JSON_MESSAGE;

		}
		if (StringUtil.isEmpty(zip)) {
			this.showErrorJson("邮编不能为空！");
			return this.JSON_MESSAGE;

		}
		if(def_addr==0){
		}else{
			this.memberAddressManager.updateAddressByMer();
		}
		memberaddress.setCity_id(city_id);
		memberaddress.setProvince_id(province_id);
		memberaddress.setRegion_id(region_id);
		memberaddress.setDef_addr(def_addr);
		memberaddress.setAddr(addr);
		memberaddress.setCity(city);
		memberaddress.setTel(tel);
		memberaddress.setName(name);
		memberaddress.setProvince(province);
		memberaddress.setZip(zip);
		memberaddress.setCountry(country);
		memberaddress.setMobile(mobile);
		memberAddressManager.updateAddress(memberaddress);
		this.showSuccessJson("修改成功");
		return this.JSON_MESSAGE;
	}
	public String getAddressById(){
		MemberAddress memberaddress = memberAddressManager.getAddress(addr_id);
		List<MemberAddress> list = new ArrayList<MemberAddress>();
		list.add(memberaddress);
		showGridJson(list);
		return this.JSON_MESSAGE;
	}
	
}
