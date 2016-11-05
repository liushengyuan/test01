package com.enation.app.b2b2c.core.action.api.member;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.IStoreMemberAddressManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

/**
 * 店铺会员收货地址 Action
 * @author XuLiPeng
 */
@Component
@SuppressWarnings("serial")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("memberAddress")
public class StoreMemberAddressApiAction extends WWAction {
	private IMemberAddressManager memberAddressManager;
	private IStoreMemberAddressManager storeMemberAddressManager;
	private IStoreMemberManager storeMemberManager;
	private Integer memberid;
	private Integer addrid;
	/**
	 * 添加店铺会员收货地址
	 * @param address 接收地址,MemberAddress
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String addNewAddress() {
		MemberAddress address = new MemberAddress();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String success=this.getText("storeMember.addsuccess");
		String fail=this.getText("storeMember.addfail");
		String no=this.getText("storeMember.notianxie");
		try {
			address = this.createAddress();
			address.setDef_addr(Integer.valueOf(1));  
			address.setType(0);//应该是让当钱的member_address的addr_id的def_add值是1.如果是这个意思，那么执行顺序做了，应该是先变成0，然后再执行本句，为什么这么写也对呢
			memberAddressManager.updateAddressDefult();           //让member_address的def_addr值变成0.大事上一句话是啥意思。
			
			this.memberAddressManager.addAddress(address);
				this.showSuccessJson(success);
			return JSON_MESSAGE;
		} catch (Exception e) {
			this.logger.error("前台添加地址错误", e);
		}
		if (address.getMobile()==null||address.getMobile()=="") {
			this.showErrorJson(no);
		}
			this.showErrorJson(fail);
		return JSON_MESSAGE;
	}
	/**
	 * 创建收货地址
	 * @param shipName 收货人名称
	 * @param shipTel 收货人电话
	 * @param shipMobile 收货人手机号
	 * @param province_id 收货——省Id
	 * @param city_id 收货——城市Id
	 * @param region_id 收货——区Id
	 * @param province 收货——省
	 * @param city 收货——城市
	 * @param region 收货——区
	 * @param shipAddr 详细地址
	 * @param shipZip 收货邮编
	 * @return 收货地址
	 */
	private MemberAddress createAddress() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();

		MemberAddress address = new MemberAddress();

		String name = request.getParameter("shipName");
		address.setName(name);

		
		String tel1 = request.getParameter("shipTel");
		
		String tel2 = request.getParameter("shipTel");
		if (StringUtil.isEmpty(tel1)&&StringUtil.isEmpty(tel2)) {
			String tel=tel1+tel2;
			address.setTel(tel1);
		}else {
			
			String tel=7+tel1+tel2;
			address.setTel(tel);
		}
		
		/*address.setTel(tel);*/
		String mobile1 = request.getParameter("shipMobile1");
		
		String mobile2 = request.getParameter("shipMobile2");
		String mobile3 = request.getParameter("shipMobile3");
		String mobile4 = request.getParameter("shipMobile4");
		String mobile6=mobile1+mobile2+mobile3+mobile4;
		if (mobile6==null||mobile6=="") {
			address.setMobile(mobile6);
		}else {
			String  mobile=7+mobile1+mobile2+mobile3+mobile4;
			address.setMobile(mobile);
		}
		

/*		String province_id = request.getParameter("province_id");
		if (province_id != null) {
			address.setProvince_id(Integer.valueOf(0));
		}*/
		address.setProvince_id(0);
/*		String city_id = request.getParameter("city_id");
		if (city_id != null) {
			address.setCity_id(Integer.valueOf(0));
		}*/
		address.setCity_id(0);
/*		String region_id = request.getParameter("region_id");
		if (region_id != null) {
			address.setRegion_id(Integer.valueOf(0));
		}*/
		address.setRegion_id(0);
		String province = request.getParameter("province");
		address.setProvince(province);

		String city = request.getParameter("city");
		address.setCity(city);

		//String region = request.getParameter("region");
		address.setRegion("");

		String addr = request.getParameter("shipAddr");
		address.setAddr(addr);

		String zip = request.getParameter("shipZip");
		address.setZip(zip);

		return address;
	}
	/**
	 * 设置会员默认收货地址
	 * @param member 店铺会员,StoreMember
	 * @param addrid 收货地址Id,Integer
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String setDefAddress(){
		String nouse=this.getText("storeMember.noUse");
		String success=this.getText("storeMember.shezhi");
		String fail=this.getText("storeMember.changFail");
		try {
			StoreMember member=storeMemberManager.getStoreMember();
			if(member==null){
				throw new TemplateModelException(nouse+"[ConsigneeListTag]");
			}
			this.storeMemberAddressManager.updateMemberAddress(member.getMember_id(),addrid);
			this.showSuccessJson(success);
		} catch (Exception e) {
			this.showErrorJson(fail);
		}
		return JSON_MESSAGE;
	}

	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}

	public void setMemberAddressManager(IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}

	public IStoreMemberAddressManager getStoreMemberAddressManager() {
		return storeMemberAddressManager;
	}

	public void setStoreMemberAddressManager(
			IStoreMemberAddressManager storeMemberAddressManager) {
		this.storeMemberAddressManager = storeMemberAddressManager;
	}

	public Integer getMemberid() {
		return memberid;
	}

	public void setMemberid(Integer memberid) {
		this.memberid = memberid;
	}

	public Integer getAddrid() {
		return addrid;
	}

	public void setAddrid(Integer addrid) {
		this.addrid = addrid;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	
}
