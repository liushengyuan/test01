package com.enation.app.shop.core.action.api;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.base.core.model.Regions;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;


@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("memberAddress")
/**
 * 会员地址api
 * @author kingapex
 *2013-7-26上午9:47:19
 */
public class MemberAddressApiAction extends WWAction {
	private IMemberAddressManager memberAddressManager;
	private Integer addr_id;
	
	
	public  void   jiexixml(){
		  SAXReader reader = new SAXReader();
		  File file = new File("d:\\user.xml");
		   
		try {
			Document	document = (Document) reader.read(file);
			 Element root = document.getRootElement();
			  List<Element> childElements = root.elements();
			  for (Element child : childElements) {
			   //未知属性名情况下
			   List<Attribute> attributeList = child.attributes();
			   for (Attribute attr : attributeList) {
			    //System.out.println(attr.getName() + ": " + attr.getValue());
			    //System.out.println("asadadad");
			   }

			 
			   //未知子元素名情况下
			   List<Element> elementList = child.elements();
			   
			   for (Element ele : elementList) {
				   List<Attribute> attributeList2 = ele.attributes();
			    //System.out.println(ele.getName() + ": " + ele.getText());
			    for (Attribute attr2 : attributeList2) {
			    //System.out.println(attr2.getName() + ": " + attr2.getValue());
			   }
			 
			   }
			  }
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  
		
	}
	
	
	
	/**
	 * 买家 获取会员地址
	 * 
	 * @param 无
	 * 
	 * @return json字串 result 为1表示调用正确，0表示失败 ，int型 data: 地址列表
	 * 
	 *         {@link MemberAddress} 如果没有登录返回空数组
	 */
	public String list() {
		List<MemberAddress> addressList = null;
		MemberAddress defaultAddress = null;
		Member member = UserConext.getCurrentMember();
		if (member != null) {
			// 读取此会员的收货地址列表
			addressList = memberAddressManager.listAddress();
			defaultAddress = this.getDefaultAddress(addressList);
		} else {
			addressList = new ArrayList();
		}

		Map data = new HashMap();
		data.put("addressList", addressList);
		data.put("defaultAddress", defaultAddress);
		this.json = JsonMessageUtil.getObjectJson(data);
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 卖家 获取会员地址
	 * 
	 * @param 无
	 * 
	 * @return json字串 result 为1表示调用正确，0表示失败 ，int型 data: 地址列表
	 * 
	 *         {@link MemberAddress} 如果没有登录返回空数组
	 */
	public String list2() {
		List<MemberAddress> addressList = null;
		MemberAddress defaultAddress = null;
		Member member = UserConext.getCurrentMember();
		if (member != null) {
			// 读取此会员的收货地址列表
			addressList = memberAddressManager.listAddress2();
			defaultAddress = this.getDefaultAddress(addressList);
		} else {
			addressList = new ArrayList();
		}

		Map data = new HashMap();
		data.put("addressList", addressList);
		data.put("defaultAddress", defaultAddress);
		this.json = JsonMessageUtil.getObjectJson(data);
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 买家 添加一会员地址
	 * 
	 * @param name
	 *            ：收货人姓名,String型，必填
	 * @param province
	 *            :所在省,String型，必填
	 * @param province_id
	 *            :所在省id,int型，参见：{@link Regions.region_id}，必填
	 * @param city
	 *            ：所在城市,String型，必填
	 * @param city_id
	 *            : 所在城市id,int型，参见：{@link Regions.region_id}，必填
	 * @param region
	 *            ：所在地区,String型，必填
	 * @param region_id
	 *            : 所在地区id,int型，参见：{@link Regions.region_id} ，必填
	 * @param addr
	 *            ：详细地址,String型 ，必填
	 * @param zip
	 *            ：邮编,String型 ，必填
	 * @param tel
	 *            ：固定电话,String型 ，手机和电话必填一项
	 * @param mobile
	 *            ：手机,String型 ，手机和电话必填一项
	 * @param def_addr
	 *            ：是否是默认地址,如果传递"1"则为默认地址，如果传递"0"为非默认地址 whj
	 *            14-03-07,修改(107-111行).
	 * @param remark
	 *            ：备注,String型,可选项
	 * @return json字串 result 为1表示添加成功，0表示失败 ，int型 message 为提示信息 ，String型
	 *         {@link MemberAddress}
	 */
	public String add() {

		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
*/
		String AddErrorReason = this.getText("MAddress.AddErrorReason");
		String AddSuccess = this.getText("MAddress.AddSuccess");
		String AddError =this.getText("MAddress.AddError");
		String WuQuan =this.getText("MAddress.WuQuan");

	/*	if (language == "zh") {
			AddErrorReason = "添加失败。原因：最多可以维护10个收货地址。";
			AddSuccess = "添加成功";
			AddError = "添加失败";

		} else {
			AddErrorReason = "Добавить не успешно.Причина：максимально можно сохранять 10 адресов получения товаров";
			AddSuccess = "Добавить успешно";
			AddError = "Добавить не успешно";

		}*/

		Member member = UserConext.getCurrentMember();
		if (member == null) {
			this.showErrorJson(WuQuan);
			return WWAction.JSON_MESSAGE;
		}

		if (memberAddressManager.addressCount(member.getMember_id()) >= 10) {
			this.showErrorJson(AddErrorReason);
		} else {
			MemberAddress address = new MemberAddress();
			try {
				address = this.fillAddressFromReq(address);
				HttpServletRequest request = ThreadContextHolder
						.getHttpRequest();
				String def_addr = request.getParameter("def_addr");
				if ("1".equals(def_addr)) {
					address.setDef_addr(Integer.valueOf(def_addr)); // 应该是让当钱的member_address的addr_id的def_add值是1.如果是这个意思，那么执行顺序做了，应该是先变成0，然后再执行本句，为什么这么写也对呢
					memberAddressManager.updateAddressDefult(); // 让member_address的def_addr值变成0.大事上一句话是啥意思。
				}

				memberAddressManager.addAddress(address);
				this.showSuccessJson(AddSuccess);
			} catch (Exception e) {
				if (this.logger.isDebugEnabled()) {
					logger.error(e.getStackTrace());
				}
				this.showErrorJson(AddError + "[" + e.getMessage() + "]");
			}
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 卖家
	 * 
	 * @return
	 */
	public String add2() {
/*
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/


		String AddErrorReason = this.getText("MAddress.AddErrorReason");
		String AddSuccess = this.getText("MAddress.AddSuccess");
		String AddError =this.getText("MAddress.AddError");
		String WuQuan =this.getText("MAddress.WuQuan");

		/*if (language == "zh") {
			AddErrorReason = "添加失败。原因：最多可以维护10个收货地址。";
			AddSuccess = "添加成功";
			AddError = "添加失败";
		} else {
			AddErrorReason = "Добавить не успешно.Причина：максимально можно сохранять 10 адресов получения товаров";
			AddSuccess = "Добавить успешно";
			AddError = "Добавить не успешно";
		}
*/
		Member member = UserConext.getCurrentMember();
		if (member == null) {
			this.showErrorJson(WuQuan);
			return WWAction.JSON_MESSAGE;
		}
		
		//检测地址数不能超过10个
		if (memberAddressManager.addressCount2(member.getMember_id()) >= 10) {
			this.showErrorJson(AddErrorReason);
		} else {
			MemberAddress address = new MemberAddress();
			try {
				address = this.fillAddressFromReqStore(address);
				HttpServletRequest request = ThreadContextHolder
						.getHttpRequest();
				String def_addr = request.getParameter("def_addr");
				if ("1".equals(def_addr)) {
					address.setDef_addr(Integer.valueOf(def_addr)); // 应该是让当钱的member_address的addr_id的def_add值是1.如果是这个意思，那么执行顺序做了，应该是先变成0，然后再执行本句，为什么这么写也对呢
					memberAddressManager.updateAddressDefultByType(address.getAdd_type()); // 让member_address的def_addr值变成0.大事上一句话是啥意思。
				}
				
				memberAddressManager.addAddress2(address);
				this.showSuccessJson(AddSuccess);
			} catch (Exception e) {
				if (this.logger.isDebugEnabled()) {
					logger.error(e.getStackTrace());
				}
				this.showErrorJson(AddError + "[" + e.getMessage() + "]");
			}
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 买家修改收货地址
	 * 
	 * @param addr_id
	 *            ：要修改的收货地址id,int型，必填
	 * @param name
	 *            ：收货人姓名,String型，必填
	 * @param province
	 *            :所在省,String型，必填
	 * @param province_id
	 *            :所在省id,int型，参见：{@link Regions.region_id}，必填
	 * @param city
	 *            ：所在城市,String型，必填
	 * @param city_id
	 *            : 所在城市id,int型，参见：{@link Regions.region_id}，必填
	 * @param region
	 *            ：所在地区,String型，必填
	 * @param region_id
	 *            : 所在地区id,int型，参见：{@link Regions.region_id} ，必填
	 * @param addr
	 *            ：详细地址,String型 ，必填
	 * @param zip
	 *            ：邮编,String型 ，必填
	 * @param tel
	 *            ：固定电话,String型 ，手机和电话必填一项
	 * @param mobile
	 *            ：手机,String型 ，手机和电话必填一项
	 * @param def_addr
	 *            ：是否是默认地址,Integer型 ,可选项，如果传递"1"则为默认地址，如果传递"0"为非默认地址 whj
	 *            14-03-07,修改(158-161行).
	 * @param remark
	 *            ：备注,String型,可选项
	 * @return json字串 result 为1表示添加成功，0表示失败 ，int型 message 为提示信息 ，String型
	 */
	public String edit() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/

		String ChangeSuccess = this.getText("MAddress.ChangeSuccess");
		String ChangeFail = this.getText("MAddress.ChangeFail");
		String WuQuan =this.getText("MAddress.WuQuan");

		/*if (language == "zh") {

			ChangeSuccess = "修改成功";
			ChangeFail = "修改失败";

		} else {

			ChangeSuccess = "Исправить успешно";
			ChangeFail = "Исправить не успешно";
		}*/

		Member member = UserConext.getCurrentMember();

		if (member == null) {
			this.showErrorJson(WuQuan);
			return WWAction.JSON_MESSAGE;
		}

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String addr_id = request.getParameter("addr_id");
		MemberAddress address = memberAddressManager.getAddress(Integer
				.valueOf(addr_id));
		try {
			address = this.fillAddressFromReq(address);
			String def_addr = request.getParameter("def_addr");
			if ("1".equals(def_addr)) {
				address.setDef_addr(Integer.valueOf(def_addr));
				memberAddressManager.updateAddressDefult();
			}

			if ("0".equals(def_addr)) {
				address.setDef_addr(Integer.valueOf(def_addr));
			}

			this.memberAddressManager.updateAddress(address);
			this.showSuccessJson(ChangeSuccess);
		} catch (Exception e) {
			if (this.logger.isDebugEnabled()) {
				logger.error(e.getStackTrace());
			}
			this.showErrorJson(ChangeFail + "[" + e.getMessage() + "]");
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 卖家家修改退货地址
	 * 
	 * @param addr_id
	 *            ：要修改的收货地址id,int型，必填
	 * @param name
	 *            ：收货人姓名,String型，必填
	 * @param province
	 *            :所在省,String型，必填
	 * @param province_id
	 *            :所在省id,int型，参见：{@link Regions.region_id}，必填
	 * @param city
	 *            ：所在城市,String型，必填
	 * @param city_id
	 *            : 所在城市id,int型，参见：{@link Regions.region_id}，必填
	 * @param region
	 *            ：所在地区,String型，必填
	 * @param region_id
	 *            : 所在地区id,int型，参见：{@link Regions.region_id} ，必填
	 * @param addr
	 *            ：详细地址,String型 ，必填
	 * @param zip
	 *            ：邮编,String型 ，必填
	 * @param tel
	 *            ：固定电话,String型 ，手机和电话必填一项
	 * @param mobile
	 *            ：手机,String型 ，手机和电话必填一项
	 * @param def_addr
	 *            ：是否是默认地址,Integer型 ,可选项，如果传递"1"则为默认地址，如果传递"0"为非默认地址 whj
	 *            14-03-07,修改(158-161行).
	 * @param remark
	 *            ：备注,String型,可选项
	 * @return json字串 result 为1表示添加成功，0表示失败 ，int型 message 为提示信息 ，String型
	 */
	public String edit2() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/


		String ChangeSuccess = this.getText("MAddress.ChangeSuccess");
		String ChangeFail = this.getText("MAddress.ChangeFail");
		String WuQuan =this.getText("MAddress.WuQuan");
		
		/*String ChangeSuccess = "";
		String ChangeFail = "";
		if (language == "zh") {

			ChangeSuccess = "修改成功";
			ChangeFail = "修改失败";

		} else {

			ChangeSuccess = "Исправить успешно";
			ChangeFail = "Исправить не успешно";
		}
*/
		Member member = UserConext.getCurrentMember();

		if (member == null) {
			this.showErrorJson(WuQuan);
			return WWAction.JSON_MESSAGE;
		}

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String addr_id = request.getParameter("addr_id");
		MemberAddress address = memberAddressManager.getAddress(Integer
				.valueOf(addr_id));
		try {
			address = this.fillAddressFromReqStore(address);
			String def_addr = request.getParameter("def_addr");
			if ("1".equals(def_addr)) {
				address.setDef_addr(Integer.valueOf(def_addr));
				memberAddressManager.updateAddressDefultByType(address.getAdd_type());
			}

			if ("0".equals(def_addr)) {
				address.setDef_addr(Integer.valueOf(def_addr));
			}

			memberAddressManager.updateAddress(address);
			this.showSuccessJson(ChangeSuccess);
		} catch (Exception e) {
			if (this.logger.isDebugEnabled()) {
				logger.error(e.getStackTrace());
			}
			this.showErrorJson(ChangeFail + "[" + e.getMessage() + "]");
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 设置当前地址为默认地址
	 */
	public String isdefaddr() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/

		String ChangeSuccess = this.getText("MAddress.ChangeSuccess");
		String ChangeFail = this.getText("MAddress.ChangeFail");
		String WuQuan =this.getText("MAddress.WuQuan");
		
		/*String ChangeSuccess = "";
		String ChangeFail = "";
		if (language == "zh") {

			ChangeSuccess = "修改成功";
			ChangeFail = "修改失败";

		} else {

			ChangeSuccess = "Исправить успешно";
			ChangeFail = "Исправить не успешно";
		}*/
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String addr_id = request.getParameter("addr_id");
			memberAddressManager.updateAddressDefult();
			memberAddressManager.addressDefult(addr_id);
			this.showSuccessJson(ChangeSuccess);

		} catch (Exception e) {
			if (this.logger.isDebugEnabled()) {
				logger.error(e.getStackTrace());
			}
			this.showErrorJson(ChangeFail + "[" + e.getMessage() + "]");
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 删除一个收货地址
	 * 
	 * @param addr_id
	 *            ：要删除的收货地址id,int型 result 为1表示添加成功，0表示失败 ，int型 message 为提示信息
	 *            ，String型
	 */
	public String delete() {
	/*	HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/

		String DelSuccess = this.getText("MAddress.DelSuccess");
		String DelFail = this.getText("MAddress.DelFail");

		/*if (language == "zh") {

			DelSuccess = "删除成功";
			DelFail = "删除失败";

		} else {

			DelSuccess = "Исключить успешно";
			DelFail = "Исключить не успешно";

		}*/
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String addr_id = request.getParameter("addr_id");
		try {
			memberAddressManager.deleteAddress(Integer.valueOf(addr_id));
			this.showSuccessJson(DelSuccess);
		} catch (Exception e) {
			if (this.logger.isDebugEnabled()) {
				logger.error(e.getStackTrace());
			}
			this.showErrorJson(DelFail + "[" + e.getMessage() + "]");
		}
		return WWAction.JSON_MESSAGE;
	}

	/************ 以下方法非api，不需要书写api文档 *************/

	/**
	 * 设置默认地址
	 * 
	 * @param addr_id
	 *            :要设置为默认收货地址的id,int型 result 为1表示设置功，0表示失败 ，int型 message 为提示信息
	 *            ，String型
	 */
	public String defaddr() {

		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/

		String SetSuccess = this.getText("MAddress.SetSuccess");
		String SetFail = this.getText("MAddress.SetFail");
		String WuQuan =this.getText("MAddress.WuQuan");
		/*if (language == "zh") {

			SetSuccess = "设置成功";
			SetFail = "设置失败";

		} else {

			SetSuccess = "Поставить успешно";
			SetFail = "Поставить не успешно";

		}*/

		Member member = UserConext.getCurrentMember();

		if (member == null) {
			this.showErrorJson(WuQuan);
			return WWAction.JSON_MESSAGE;
		}

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String addr_id = request.getParameter("addr_id");
		MemberAddress address = memberAddressManager.getAddress(Integer
				.valueOf(addr_id));
		address.setDef_addr(1);
		try {
			memberAddressManager.updateAddressDefult();
			memberAddressManager.updateAddress(address);
			this.showSuccessJson(SetSuccess);
		} catch (Exception e) {
			if (this.logger.isDebugEnabled()) {
				logger.error(e.getStackTrace());
			}
			this.showErrorJson(SetFail + "[" + e.getMessage() + "]");
		}
		return WWAction.JSON_MESSAGE;
	}

	/**
	 * 获取会员的默认收货地址
	 * 
	 * @param 无
	 */
	private MemberAddress getDefaultAddress(List<MemberAddress> addressList) {
		if (addressList != null && !addressList.isEmpty()) {
			for (MemberAddress address : addressList) {
				if (address.getDef_addr() != null
						&& address.getDef_addr().intValue() == 1) {
					address.setDef_addr(1);
					return address;
				}
			}

			MemberAddress defAddress = addressList.get(0);
			defAddress.setDef_addr(1);
			return defAddress;
		}
		return null;
	}

	/**
	 * 买家从request中填充address信息
	 * 
	 * @param address
	 * @return
	 */
	private MemberAddress fillAddressFromReq(MemberAddress address) {

		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/

		String TelAndPhoneMustOne = this.getText("MAddress.TelAndPhoneMustOne");
		String TelError =  this.getText("MAddress.TelError");
		String PhoneError =  this.getText("MAddress.PhoneError");
		String PhoneError2 =  this.getText("MAddress.PhoneError2");
		String AddCantNull =  this.getText("MAddress.AddCantNull");
		String NameCanTBeNull =  this.getText("MAddress.NameCanTBeNull");
		String ZipCantNull =  this.getText("MAddress.ZipCantNull");
		String ZipError =  this.getText("MAddress.ZipError");

		/*if (language == "zh") {

			TelAndPhoneMustOne = "联系电话和联系手机必须填写一项！";
			TelError = "电话格式不对！";
			PhoneError = "手机不能为空！";
			AddCantNull = "地址不能为空！";
			NameCanTBeNull = "姓名不能为空！";
			ZipCantNull = "邮编不能为空！";
			ZipError = "邮编格式错误,请输入6位数字！";

		} else {

			TelAndPhoneMustOne = "Необходимо заполнить телефон или мобильник";
			TelError = "Форма телефона неправильная";
			PhoneError = "Форма мобильника неправильная";
			AddCantNull = "Адрес не можно быть пустым";
			NameCanTBeNull = "Имя не можно быть пустым";
			ZipCantNull = "Почтовый индекс не можно быть пустым";
			ZipError = "Форма почтового индекса неправильная，ввозите 6 цифр，пожалуйста!";
		}
*/
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String def_addr = request.getParameter("def_addr");
		if ("yes".equals(def_addr)) {
			address.setDef_addr(Integer.valueOf(def_addr));
		}
		String name = request.getParameter("name");
		address.setName(name);
		if (name == null || name.equals("")) {
			throw new RuntimeException(NameCanTBeNull);
		}
		// Pattern p = Pattern.compile("^[0-9A-Za-z一-龥]{0,20}$");
		// Matcher m = p.matcher(name);

		// if (!m.matches()) {
		// throw new RuntimeException("收货人格式不正确！");
		// }
		//System.out.println(request.getParameter("type"));
		if (request.getParameter("type") != null
				|| request.getParameter("type") != "") {
			int type = Integer.parseInt(request.getParameter("type"));
			address.setType(type);
		}
		String tel = request.getParameter("tel");
		if (StringUtil.isEmpty(tel)) {// 为空时拼接数据
			String tel1 = request.getParameter("tel1");
			String tel2 = request.getParameter("tel2");
			if (StringUtil.isEmpty(tel1) && StringUtil.isEmpty(tel2)) {
				address.setTel("");
			} else {
				// String tel3=7+tel1+tel2;
				address.setTel("7(" + tel1 + ")" + tel2);
			}
		} else {
			address.setTel(tel);// 不为空时，直接用获取到的值赋值给对象
		}
		String mobile = request.getParameter("mobile");
		if (StringUtil.isEmpty(mobile)) {// 为空时拼接数据
			String mobile1 = request.getParameter("mobile1");
			String mobile2 = request.getParameter("mobile2");
			String mobile3 = request.getParameter("mobile3");
			String mobile4 = request.getParameter("mobile4");
			// String mobile6= mobile1+ mobile2+ mobile3+ mobile4;
			if (StringUtil.isEmpty(mobile1)) {
				// throw new RuntimeException(PhoneError);
				address.setMobile("");
			} else {
				// String mobile5=7+ mobile1+ mobile2+ mobile3+ mobile4;
				address.setMobile("7(" + mobile1 + ")" + mobile2 + "-"
						+ mobile3 + "-" + mobile4);
			}
		} else {
			address.setMobile(mobile);// 不为空时，直接用获取到的值赋值给对象
		}

		String province_id = request.getParameter("province_id");
		if (province_id == null || province_id.equals("")
				|| province_id.equals("0")) {
			throw new RuntimeException("请选择地区中的省！");
		}
		address.setProvince_id(Integer.valueOf(province_id));

		String city_id = request.getParameter("city_id");
		if (city_id == null || city_id.equals("") || province_id.equals("0")) {
			throw new RuntimeException("请选择地区中的市！");
		}
		address.setCity_id(Integer.valueOf(city_id));

		String region_id = request.getParameter("region_id");
		if (region_id == null || region_id.equals("")
				|| province_id.equals("0")) {
			throw new RuntimeException("请选择地区中的县！");
		}
		address.setRegion_id(Integer.valueOf(region_id));

		String country = request.getParameter("country");
		address.setCountry(country);// 设置国家

		String province = request.getParameter("province");
		address.setProvince(province);

		String city = request.getParameter("city");
		address.setCity(city);

		String region = request.getParameter("region");
		address.setRegion(region);

		String addr = request.getParameter("addr");
		if (addr == null || addr.equals("")) {
			throw new RuntimeException(AddCantNull);
		}
		/*
		 * Comment by Liuzy 校验导至 4-2401 即4单元2401室这样的写法不能通过 Pattern p1 =
		 * Pattern.compile("^[0-9A-Za-z一-]{0,50}$"); Matcher m1 =
		 * p1.matcher(addr); if(!m1.matches()){ throw new
		 * RuntimeException("地址格式不正确！");
		 * 
		 * }
		 */
		address.setAddr(addr);

		String zip = request.getParameter("zip");
		if (zip == null || zip.equals("")) {
			throw new RuntimeException(ZipCantNull);
		}
		if (!isZipStore(zip)) {
			throw new RuntimeException(ZipError);
		}
		address.setZip(zip);

		String remark = request.getParameter("remark");
		address.setRemark(remark);

		return address;
	}

	/**
	 * 卖家从request中填充address信息
	 * 
	 * @param address
	 * @return
	 */
	private MemberAddress fillAddressFromReqStore(MemberAddress address) {

		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String AddTypeCanTNull = this.getText("MAddress.AddTypeCanTNull");
		String TelAndPhoneMustOne = this.getText("MAddress.TelAndPhoneMustOne");
		String TelError =  this.getText("MAddress.TelError");
		String PhoneError =  this.getText("MAddress.PhoneError");
		String PhoneError2 =  this.getText("MAddress.PhoneError2");
		String AddCantNull =  this.getText("MAddress.AddCantNull");
		String NameCanTBeNull =  this.getText("MAddress.NameCanTBeNull");
		String ZipCantNull =  this.getText("MAddress.ZipCantNull");
		/*String AddTypeCanTNull = "";
		String TelAndPhoneMustOne = "";
		String TelError = "";
		String PhoneError = "";
		String AddCantNull = "";
		String NameCanTBeNull = "";
		String ZipCantNull = "";
*/
	/*	if (language == "zh") {
			AddTypeCanTNull = "地址类型错误";
			TelAndPhoneMustOne = "联系电话和联系手机必须填写一项！";
			TelError = "电话格式不对！";
			PhoneError = "手机格式不对！";
			AddCantNull = "地址不能为空！";
			NameCanTBeNull = "姓名不能为空！";
			ZipCantNull = "邮编不能为空！";
		} else {
			AddTypeCanTNull = "Ошибка типа адреса";
			TelAndPhoneMustOne = "Необходимо заполнить телефон или мобильник";
			TelError = "Форма телефона неправильная";
			PhoneError = "Форма мобильника неправильная";
			AddCantNull = "Адрес не можно быть пустым";
			NameCanTBeNull = "Имя не можно быть пустым";
			ZipCantNull = "Почтовый индекс не можно быть пустым";
		}*/
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		String add_type = request.getParameter("add_type");
		if(StringUtil.isEmpty(add_type) || !NumberUtils.isNumber(add_type) ) {
			throw new RuntimeException(AddTypeCanTNull);
		}
		address.setAdd_type(Integer.parseInt(add_type));
		
		String def_addr = request.getParameter("def_addr");
		if ("yes".equals(def_addr)) {
			address.setDef_addr(Integer.valueOf(def_addr));
		}
		String name = request.getParameter("name");
		address.setName(name);
		if (name == null || name.equals("")) {
			throw new RuntimeException(NameCanTBeNull);
		}
		// Pattern p = Pattern.compile("^[0-9A-Za-z一-龥]{0,20}$");
		// Matcher m = p.matcher(name);

		// if (!m.matches()) {
		// throw new RuntimeException("收货人格式不正确！");
		// }
		
		if (request.getParameter("type") != null
				|| request.getParameter("type") != "") {
			int type = Integer.parseInt(request.getParameter("type"));
			address.setType(type);
		}
		String tel = request.getParameter("tel");
		address.setTel(tel);

		String mobile = request.getParameter("mobile");
		address.setMobile(mobile);
		
		if (StringUtil.isEmpty(tel) && StringUtil.isEmpty(mobile)) {
			throw new RuntimeException(TelAndPhoneMustOne);
		} else if (!StringUtil.isEmpty(tel) && isMobileStore(tel) == false) {
			throw new RuntimeException(TelError);
		} else if (!StringUtil.isEmpty(mobile) && isPhoneStore(mobile) == false) {
			throw new RuntimeException(PhoneError2);
		}

		String province_id = request.getParameter("province_id");
		if (province_id == null || province_id.equals("")
				|| province_id.equals("0")) {
			throw new RuntimeException("请选择地区中的省！");
		}
		address.setProvince_id(Integer.valueOf(province_id));

		String city_id = request.getParameter("city_id");
		if (city_id == null || city_id.equals("") || province_id.equals("0")) {
			throw new RuntimeException("请选择地区中的市！");
		}
		address.setCity_id(Integer.valueOf(city_id));

		String region_id = request.getParameter("region_id");
		if (region_id == null || region_id.equals("")
				|| province_id.equals("0")) {
			throw new RuntimeException("请选择地区中的县！");
		}
		address.setRegion_id(Integer.valueOf(region_id));
		// 添加地址时保留省市区的id
		// address.setProvince_id(0);
		// address.setCity_id(0);
		// address.setRegion_id(0);

		String province = request.getParameter("province");
		address.setProvince(province);

		String city = request.getParameter("city");
		address.setCity(city);

		String region = request.getParameter("region");
		address.setRegion(region);

		String addr = request.getParameter("addr");
		if (addr == null || addr.equals("")) {
			throw new RuntimeException(AddCantNull);
		}
		/*
		 * Comment by Liuzy 校验导至 4-2401 即4单元2401室这样的写法不能通过 Pattern p1 =
		 * Pattern.compile("^[0-9A-Za-z一-]{0,50}$"); Matcher m1 =
		 * p1.matcher(addr); if(!m1.matches()){ throw new
		 * RuntimeException("地址格式不正确！");
		 * 
		 * }
		 */
		address.setAddr(addr);

		String zip = request.getParameter("zip");
		if (zip == null || zip.equals("")) {
			throw new RuntimeException(ZipCantNull);
		}
		if (!isZipStore(zip)) {
			throw new RuntimeException("邮编格式有误！");
		}
		address.setZip(zip);

		String remark = request.getParameter("remark");
		address.setRemark(remark);

		return address;
	}

	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}

	public void setMemberAddressManager(
			IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}

	private static boolean isPhone(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		// p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		p = Pattern.compile("^[0-9]*$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	private static boolean isMobile(String str) {
		// Pattern p1 = null,p2 = null;
		Pattern p;
		Matcher m = null;
		boolean b = false;
		// p1 = Pattern.compile("0\\d{2,3}-\\d{7,8}"); // 验证带区号的 whj
		// 2015-05-22修改对带区号的固定电话的验证，验证格式为“0467-8888888”
		// p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		// if(str.length() >9)
		// { m = p1.matcher(str);
		// b = m.matches();
		// }else{
		// m = p2.matcher(str);
		// b = m.matches();
		// }
		p = Pattern.compile("^[0-9]*$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	// 验证卖家添加退货地址时的手机号码
	private static boolean isPhoneStore(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		// p = Pattern.compile("^[0-9]*$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	// 验证卖家添加退货地址时的电话号码
	private static boolean isMobileStore(String str) {
		Pattern p1 = null, p2 = null;
		Pattern p;
		Matcher m = null;
		boolean b = false;
		p1 = Pattern.compile("0\\d{2,3}-\\d{7,8}"); // 验证带区号的 whj
													// 2015-05-22修改对带区号的固定电话的验证，验证格式为“0467-8888888”
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
		if (str.length() > 9) {
			m = p1.matcher(str);
			b = m.matches();
		} else {
			m = p2.matcher(str);
			b = m.matches();
		}
		// p = Pattern.compile("^[0-9]*$");
		// m = p.matcher(str);
		// b = m.matches();
		return b;
	}

	// 验证卖家添加及修改退货地址时的邮编
	private static boolean isZipStore(String str) {
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[0-9]\\d{5}$"); // 验证邮编
		// p = Pattern.compile("^[0-9]*$");
		m = p.matcher(str);
		b = m.matches();
		return b;
	}

	public Integer getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(Integer addr_id) {
		this.addr_id = addr_id;
	}

}
