package com.enation.app.b2b2c.core.action.api.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.dom4j.Attribute;
import org.dom4j.io.SAXReader;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.model.store.StoreFeature;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreFeatureManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Withdrawal;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.ISmtpManager;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺管理API
 * 
 * @author LiFenLong
 * 
 */
@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("storeApi")
public class StoreApiAction extends WWAction {
	private Store store;
	private Member member;
	private String valiDateCode;
	private File id_img;
	private File license_img;
	private String id_imgFileName;
	private String license_imgFileName;
	private String id_number;

	private String fsid_img;
	private String fslicense_img;
	private String status_id_img;
	private String status_license_img;

	private String logo;
	private String storeName;

	private Integer store_id;
	private Integer store_auth;
	private Integer name_auth;
	private IStoreManager storeManager;
	private IStoreMemberManager storeMemberManager;
	private IMemberManager memberManager;
	private IsmsMobileManager smsMobileManager;
	
	private String[] productFeature_description;
	private String[] productCategore_description;
	private IStoreFeatureManager storeFeatureManager;

	private Integer member_id;
	private Integer store_type;
	// 发送邮件
	private EmailProducer mailMessageProducer;
	private ISmtpManager smtpManager;
	//保存草稿
	private Integer is_save;
	private Integer member_prove_type;//会员证件类型
	
	//查询数据库
	private IDaoSupport daoSupport;
	private static ConfigurableApplicationContext  applicationContext;
	/**
	 * 跳转到开户第二步
	 * 
	 * @return
	 */
	public String editMemberForApply() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		String valide= (String) ThreadContextHolder.getSessionContext().getAttribute("mobileCode");
		if(valide==null && StringUtil.isEmpty(valide)){
			this.showErrorJson("验证码输入有误,请重新输入！");
			return this.JSON_MESSAGE;
		}else if(!valide.trim().equalsIgnoreCase(valiDateCode)){
			this.showErrorJson("验证码输入有误,请重新输入！");
			return this.JSON_MESSAGE;
		}
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		member = this.memberManager.get(member_id);
		member = getMemberInfo();
		this.memberManager.edit(member);
		if (language=="zh") {
			this.showSuccessJson("成功啦");
		}else {
			this.showSuccessJson("Успешно");
		}
		this.json = "{\"result\":1,\"store_type\":\"" + store_type + "\"}";
		return this.JSON_MESSAGE;
	}

	/**
	 * 查看用户名是否重复
	 * 
	 * @param storeName
	 *            店铺名称,String
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String checkStoreName() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		if (this.storeManager.checkStoreName(storeName)) {
			if (language=="zh") {
				this.showErrorJson("店铺名称重复");
			}else{
				this.showErrorJson("Имя магазина уже существует");
			}
		} else {
			if (language=="zh") {
				this.showSuccessJson("店铺名称可以使用");
			}else{
				this.showSuccessJson("Имя магазина можно использовать");
			}
		}
		return this.JSON_MESSAGE;
	}
	public void daochuxml2(){
		// 创建根节点 list;    
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		        Element root = new Element("Request");    
		        root.setAttribute("service","OrderService") ;
		        root.setAttribute("lang","zh-CN") ;
		     // 根节点添加到文档中；    
	       Document Doc = new Document(root);    
	       Element Head = new Element("Head");
	       Head.setText("BSPdevelop");
	       root.addContent(Head);
	       Element Body = new Element("Body");
	       //order服务
	       Element Order = new Element("Order");
	       Order.setAttribute("orderid", "yizhifu-nan-1");
	       Order.setAttribute("j_company", "yizhifu");
	       Order.setAttribute("j_contact", "nan");
	       Order.setAttribute("j_tel", "1244323433");
	       Order.setAttribute("j_mobile", "ewr234234");
	       Order.setAttribute("j_shippercode", "SIN01D");
	       Order.setAttribute("j_country", "SG");
	       Order.setAttribute("j_province", "Singapore");
	       Order.setAttribute("j_city", "Singapore");
	       Order.setAttribute("j_county", "Singapore");
	       Order.setAttribute("j_address", "7SixthLokYangRoad#11-11Singapore628105");
	       Order.setAttribute("j_post_code", "628105");
	       Order.setAttribute("d_deliverycode", "852");
	       Order.setAttribute("d_country", "HK");
	       Order.setAttribute("d_company", "Daniel");
	       Order.setAttribute("d_contact", "DanielLi");
	       Order.setAttribute("d_tel", "87654321");
	       Order.setAttribute("d_mobile", "87654321");
	       Order.setAttribute("d_province", "Hong Kong");
	       Order.setAttribute("d_city", "Hong Kong");
	       Order.setAttribute("d_county", "Hong Kong");
	       Order.setAttribute("d_address", "27/Ftestaddress,Kowloon,HongKong");
	       Order.setAttribute("d_post_code", "852852");
	       Order.setAttribute("custid", "0650000001");
	       Order.setAttribute("pay_method", "1");
	       Order.setAttribute("express_type", "23");
	       Order.setAttribute("parcel_quantity", "1");
	       Order.setAttribute("cargo_total_weight", "2.18");
	       Order.setAttribute("declared_value", "1");
	       Order.setAttribute("declared_value_currency", "CNY");
	       Order.setAttribute("sendstarttime", "2014-12-17 07:08:38");
	       Order.setAttribute("remark", "beizhu");
	     
	       
	       //cargo服务
	       Element Cargo = new Element("Cargo");
	       
	       Cargo.setAttribute("name", "nan-ceshi");
	       Cargo.setAttribute("count", "20");
	       Cargo.setAttribute("unit", "pcs");
	       Cargo.setAttribute("weight", "0.003");
	       Cargo.setAttribute("amount", "0.05");
	       Cargo.setAttribute("currency", "CNY");
	       Cargo.setAttribute("source_area", "China");
	       Order.addContent(Cargo);
	       
	       Body.addContent(Order);
	       
	       root.addContent(Body);
		       // 此处 for 循环可替换成 遍历 数据库表的结果集操作;    
	    /*  for (int i = 0; i < 5; i++) {    
	            
		          // 创建节点 user;    
		           Element elements = new Element("Request");    
	              
		           // 给 user 节点添加属性 id;    
		          elements.setAttribute("id", "" + i);    
		              
		           // 给 user 节点添加子节点并赋值；    
		          // new Element("name")中的 "name" 替换成表中相应字段，setText("xuehui")中 "xuehui 替换成表中记录值；    
		           elements.addContent(new Element("name").setText("xuehui"));   
		          elements.addContent(new Element("age").setText("28"));   
		          elements.addContent(new Element("sex").setText("Male"));   
		          elements.addContent(new Element("sex").setText("Male")); 
		           // 给父节点list添加user子节点;   
		          root.addContent(elements);  } */
		          XMLOutputter XMLOut = new XMLOutputter();   
		                 
		                 // 输出 user.xml 文件；   
		               try {
		            	   System.out.println(request.getSession().getServletContext().getRealPath("/"));
						System.out.println(request.getContextPath());
		            	   XMLOut.output(Doc, new FileOutputStream("d:\\user.xml"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}      
		
	
	      }

	/**
	 * 申请开店
	 * 
	 * @param store
	 *            店铺信息,Store
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String apply() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			if (null == storeMemberManager.getStoreMember()) {
				if (language=="zh") {
					this.showErrorJson("您没有登录不能申请开店");
				}else {
					this.showErrorJson("Вы не вошли в интернете не применяются для магазина");
				}
				
			} else if (!storeManager.checkStore()) {
				/*
				 * member=memberManager.get(member_id);
				 * member=this.getMemberInfo(); memberManager.edit(member);
				 */
				int status = 0;
				// 添加店铺地址
				store = this.getStoreInfo();
				store.setStore_initiallist(1);//初次申请店铺初始单为未获取
				if (store.getStore_type() == 1) {
					if (store.getId_img().isEmpty()) {
						status = 1;
					} else if (store.getReverse_id_img().isEmpty()) {
						status = 2;
					} else if (store.getBcard_img().isEmpty()) {
						status = 3;
					}
				} else {
					if (store.getLicense_img().isEmpty()) {
						status = 4;
					} else if (store.getProve_img().isEmpty()) {
						status = 5;
					}
				}
				// 暂时先将店铺等级定为默认等级，以后版本升级更改此处
				if (status == 0 && is_save!=1) {
					store.setStore_level(1);
					store.setCreate_time(DateUtil.getDateline());
					storeManager.apply(store);
				}
				
				// storeFeatureManager.deleteAll(store);
				if (status == 0 &&  is_save!=1) {
					this.sendEmailToAdmin(store);
					if (language=="zh") {
						this.showSuccessJson("申请成功,请等待审核");
					}else {
						this.showSuccessJson("Приложение было успешно, пожалуйста, подождите проверять");
					}
				} else if (status == 1 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("身份证正面没有上传！");
					}else {
						this.showErrorJson("ID карты передней  не загрузить！");
					}
				} else if (status == 2  && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("身份证反面没有上传！");
					}else {
						this.showErrorJson("ID карты задней  не загрузить！");
					}
				} else if (status == 3 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("银行卡正面没有上传！");
					}else {
						this.showErrorJson("Карта банковская передней не загрузить！");
					}
				} else if (status == 4 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("营业执照没有上传！");
					}else {
						this.showErrorJson("Лицензия не загрузить！");
					}
				} else if (status == 5 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("公司开户证明没有上传！");
					}else {
						this.showErrorJson("Сертификат счета компания не загрузить！");
					}
				} else if(is_save==1){
					store.setDisabled(-2);//店铺的状态为草稿
					storeManager.apply(store);
					//storeManager.editStoreInfo(store);//修改店铺的状态
					//this.showSuccessJson("保存成功");
					StoreMember storemember=storeMemberManager.getMember(member_id);
					storemember.setIs_store(0);
					storeMemberManager.edit(storemember);
					if (language=="zh") {
						this.showSuccessJson("保存成功");
					}else {
						this.showSuccessJson("Успешно сохранить");
					}
				}
				if (productFeature_description.length > 0) {
					int m = 0;
					StoreFeature storeFeature;
					for (String name : productFeature_description) {
						if (!StringUtil.isEmpty(name)) {
							storeFeature = new StoreFeature();
							storeFeature.setName(name);
							storeFeature.setStore_id(store.getStore_id());
							storeFeature.setFeature_type(1);
							storeFeature.setFeature_index(m);
							storeFeatureManager.add(storeFeature);
							m++;
						}
					}
				}
				if (productCategore_description.length > 0) {
					int n = 0;
					StoreFeature storeFeature;
					for (String name : productCategore_description) {
						if (!StringUtil.isEmpty(name)) {
							storeFeature = new StoreFeature();
							storeFeature.setName(name);
							storeFeature.setStore_id(store.getStore_id());
							storeFeature.setFeature_type(2);
							storeFeature.setFeature_index(n);
							storeFeatureManager.add(storeFeature);
							n++;
						}
					}
				}
			} else {
				if (language=="zh") {
					this.showErrorJson("您已经申请过了，请不要重复申请");
				}else {
					this.showErrorJson("Вы уже задали заявление, полуйста не повторяйте");
				}
			}
		} catch (Exception e) {
			this.logger.error("申请失败:" + e);
			e.printStackTrace();
			if (language=="zh") {
				this.showErrorJson("申请失败");
			}else {
				this.showErrorJson("Не удалось подавать заявление");
			}
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 重试申请开店
	 * 
	 * @param store
	 *            店铺信息,Store
	 * @return
	 */
	public String reApply() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			if (null == storeMemberManager.getStoreMember()) {
				if (language=="zh") {
					this.showErrorJson("您没有登录不能申请开店");
				}else {
					this.showErrorJson("Вы не вошли в интернете не применяются для магазина");
				}
			} else {
				// 添加店铺地址
				store = this.getStoreInfo();
				store.setStore_initiallist(1);//初次申请店铺初始单为未获取
				int status = 0;
				if (store.getStore_type() == 1) {
					if (store.getId_img().isEmpty()) {
						status = 1;
					} else if (store.getReverse_id_img().isEmpty()) {
						status = 2;
					} else if (store.getBcard_img().isEmpty()) {
						status = 3;
					}
				} else {
					if (store.getLicense_img().isEmpty()) {
						status = 4;
					} else if (store.getProve_img().isEmpty()) {
						status = 5;
					}
				}
				// 暂时先将店铺等级定为默认等级，以后版本升级更改此处
				if (status == 0 && is_save!=1) {
					store.setStore_id(store_id);
					store.setStore_level(1);
					store.setCreate_time(DateUtil.getDateline());
					storeManager.reApply(store);
				}

				// this.showSuccessJson("提交申请成功,请等待审核");
				if (status == 0 && is_save!=1) {
					this.sendEmailToAdmin(store);
					if (language=="zh") {
						this.showSuccessJson("申请成功,请等待审核");
					}else {
						this.showSuccessJson("Приложение было успешно, пожалуйста, подождите проверять");
					}
				} else if (status == 1 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("身份证正面没有上传！");
					}else {
						this.showErrorJson("ID карты передней  не загрузить！");
					}
				} else if (status == 2 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("身份证反面没有上传！");
					}else {
						this.showErrorJson("ID карты задней  не загрузить！");
					}
				} else if (status == 3 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("银行卡正面没有上传！");
					}else {
						this.showErrorJson("Карта банковская передней не загрузить！");
					}
				} else if (status == 4 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("营业执照没有上传！");
					}else {
						this.showErrorJson("Лицензия не загрузить！");
					}
				} else if (status == 5 && is_save!=1) {
					if (language=="zh") {
						this.showErrorJson("公司开户证明没有上传！");
					}else {
						this.showErrorJson("Сертификат счета компания не загрузить！");
					}
				}else if(is_save==1){
					store.setStore_id(store_id);
					store.setDisabled(-2);
					storeManager.reApply(store);
					//store.setDisabled(-2);//店铺的状态为草稿
					//storeManager.editStoreInfo(store);//修改店铺的状态
					//this.showSuccessJson("保存成功");
					StoreMember storemember=storeMemberManager.getMember(member_id);
					storemember.setIs_store(0);
					storeMemberManager.edit(storemember);
					if (language=="zh") {
						this.showSuccessJson("保存成功");
					}else {
						this.showSuccessJson("Успешно сохранения");
					}
				}
				storeFeatureManager.deleteAll(store);
				if (productFeature_description.length > 0) {
					int m = 0;
					StoreFeature storeFeature;
					for (String name : productFeature_description) {
						if (!StringUtil.isEmpty(name)) {
							storeFeature = new StoreFeature();
							storeFeature.setName(name);
							storeFeature.setStore_id(store.getStore_id());
							storeFeature.setFeature_type(1);
							storeFeature.setFeature_index(m);
							storeFeatureManager.add(storeFeature);
							m++;
						}
					}
				}
				if (productCategore_description.length > 0) {
					int n = 0;
					StoreFeature storeFeature;
					for (String name : productCategore_description) {
						if (!StringUtil.isEmpty(name)) {
							storeFeature = new StoreFeature();
							storeFeature.setName(name);
							storeFeature.setStore_id(store.getStore_id());
							storeFeature.setFeature_type(2);
							storeFeature.setFeature_index(n);
							storeFeatureManager.add(storeFeature);
							n++;
						}
					}
				}
			}
		} catch (Exception e) {
			this.logger.error("申请失败:" + e);
			e.printStackTrace();
			if (language=="zh") {
				this.showErrorJson("申请失败");
			}else {
				this.showErrorJson("Не удалось подавать заявление");
			}
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 获取店铺信息
	 * 
	 * @param store
	 * @return Store
	 */
	private Store getStoreInfo() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Store store = new Store();
		store.setStore_name(request.getParameter("store_name"));// 店铺名称
		store.setStore_country(request.getParameter("store_country"));// 店铺所在国
		store.setStore_market(request.getParameter("store_market"));// 店铺市场选择
		store.setStore_banner(request.getParameter("store_banner"));// 店铺推广语
		store.setDescription(request.getParameter("store_description"));// 店铺简介
		store.setStore_location(request.getParameter("store_location"));// 店铺位置编号
		store.setStore_logo(request.getParameter("store_logo_img"));// 店铺logo
		store.setParent_store(Integer.valueOf(request.getParameter("parent_store")));//所属店铺：0独立店铺，1青云商城
		store.setStore_type(Integer.valueOf(request.getParameter("store_type")));// 开店类型，个人，1，公司2
		if (Integer.valueOf(request.getParameter("store_type")) == 2) {
			store.setCompany_name(request.getParameter("com_company_name"));// 公司名称
			store.setRegistration_number(request
					.getParameter("com_registration_number"));// 公司注册编码(营业执照)
			store.setProve_img(request.getParameter("store_prove_img"));// 公司开户证明
			store.setLicense_img(request.getParameter("store_license_img")); // 营业执照证明
			// 店铺银行信息
			store.setBank_account_number(request
					.getParameter("bank_account_number")); // 公司银行账号
			store.setBank_code(request.getParameter("bank_code")); // 支行联行号
			// 财务联系人信息
			store.setProve_name(request.getParameter("prove_name"));// 公司财务联系人
			store.setProve_type(Integer.valueOf(request
					.getParameter("prove_type")));// 联系人的证件类型
			store.setId_number(request.getParameter("prove_number"));// 联系人证件的证件号
			store.setProve_mobile(request.getParameter("prove_mobile"));// 联系人手机号
			String company_tel2 = request.getParameter("company_tel2");//区号
			String company_tel = request.getParameter("company_tel");//电话
			String company_tel3 = company_tel2+"-"+company_tel;
			store.setTel(company_tel3);//公司固定电话
			store.setProve_name2(request.getParameter("prove_name2"));//紧急联系人
			store.setProve_name3(request.getParameter("prove_name3"));//备用联系人
			store.setProve_mobile2(request.getParameter("prove_mobile2"));//紧急联系人手机号
			store.setProve_mobile3(request.getParameter("prove_mobile3"));//备用联系人手机号
		} else {
			// 银行卡所有人信息
			store.setProve_name(request.getParameter("prove_name"));// 银行卡所有人
			store.setProve_type(Integer.valueOf(request
					.getParameter("prove_type")));// 银行卡所有人的证件类型
			store.setId_number(request.getParameter("prove_number"));// 银行卡所有人的证件号
			store.setId_img(request.getParameter("store_id_img"));// 身份证证明图片
			store.setReverse_id_img(request.getParameter("store_id2_img"));// 身份证反面图片
			store.setBcard_img(request.getParameter("store_bcard_img"));// 银行卡证明图片
			store.setBank_account_number(request
					.getParameter("bank_account_number")); // 公司银行账号
		}
		//俄文状态下直接获取，中文状态下根据id查询设置
		HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		// 店铺银行信息
		if(language=="zh"){
			String bank_account_name = request.getParameter("bank_account_name");
			store.setBank_account_name(bank_account_name);//开户银行总行id
			//如果id不为空，则根据id查询对应的总行名称
			if(bank_account_name!=null && !bank_account_name.equals("")){
				Integer bank_account_id = Integer.parseInt(bank_account_name);
				String sql = "select text from es_bankname where id = "+bank_account_id;
				String bank_name = this.daoSupport.queryForString(sql);
				store.setBank_name(bank_name); //开户银行总行名称***********
			}
				 
			//设置银行所在省的id和名称
			Integer bank_province_id=null;
			if(!StringUtil.isEmpty(request.getParameter("bank_province_id"))){
				bank_province_id = Integer.parseInt(request.getParameter("bank_province_id").toString());
				store.setBank_provinceid(bank_province_id);
			}
			if(bank_province_id!=null && !bank_province_id.equals("")){
				String sql = "select name from es_areacode where id = "+bank_province_id;
				String bank_province = this.daoSupport.queryForString(sql);
				store.setBank_province(bank_province);//开户银行所在省
			}
			// //开户银行所在市Id
			Integer bank_city_id=null;
			if(!StringUtil.isEmpty(request.getParameter("bank_city_id"))){
				bank_city_id = Integer.parseInt(request.getParameter("bank_city_id").toString());
				store.setBank_cityid(bank_city_id);
			}
			if(bank_city_id!=null && !bank_city_id.equals("")){
				String sql = "select name from es_areacode where id = "+bank_city_id;
				String bank_city = this.daoSupport.queryForString(sql);
				store.setBank_city(bank_city); //开户银行所在市
			}
			// //开户银行所在区Id
			Integer bank_region_id = null;
			if(!StringUtil.isEmpty(request.getParameter("bank_region_id"))){
				bank_region_id = Integer.parseInt(request.getParameter("bank_region_id").toString());
				store.setBank_regionid(bank_region_id);
			}
			if(bank_region_id!=null && !bank_region_id.equals("")){
				String sql = "select name from es_bankcode where id = "+bank_region_id;
				String bank_region = this.daoSupport.queryForString(sql);
				store.setBank_region(bank_region); //开户银行所在区
			}
		}else{//俄文状态下，设置银行信息
			String bank_account_name = request.getParameter("bank_account_name");
			String bank_province_id = request.getParameter("bank_province_id");
			String bank_city_id = request.getParameter("bank_city_id");
			String bank_region_id = request.getParameter("bank_region_id");
			store.setBank_name(bank_account_name); //开户银行总行名称
			store.setBank_province(bank_province_id);//开户银行所在省
			store.setBank_city(bank_city_id); //开户银行所在市
			store.setBank_region(bank_region_id); //开户银行所在区
		}
		store.setCredit_account(0.00);
		store.setAccount(0.00);
		store.setCredit_account_status(0);// 0,禁用，1，启用
		// 店铺佣金
		store.setCommission(0.0);
		return store;
	}

	/**
	 * 获取店主信息
	 * 
	 * @return
	 */
	private Member getMemberInfo() {
		HttpServletRequest request = this.getRequest();
		member.setMember_country(request.getParameter("member_country"));//卖家所在信息
		member.setName(request.getParameter("member_name"));// 身份证姓名
		member.setId_number(request.getParameter("member_id_number"));// 身份证Id
		member.setMember_prove_type(Integer.parseInt(request.getParameter("member_prove_type")));
		// member.setEmail(request.getParameter("member_email"));//个人邮箱

		// System.out.println(DateUtil.toDate(request.getParameter("member_id_number").substring(6,
		// 14), "yyyyMMdd"));
		if(request.getParameter("member_id_number").length()>12){
			Long birt = DateUtil.getDateline(
					request.getParameter("member_id_number").substring(6, 14),
					"yyyyMMdd");
			// System.out.println(request.getParameter("member_id_number").substring(6,
			// 14));
			member.setBirthday(birt);// 出生日期
			int sex = Integer.valueOf(request.getParameter("member_id_number")
					.substring(16, 17));
			System.out.println(request.getParameter("member_id_number").substring(
					16, 17));
			if (sex % 2 == 0) {
				member.setSex(0);// 性别 女
			} else {
				member.setSex(1);// 性别 男
			}
		}
		
		// member.setSex(Integer.parseInt(request.getParameter("member_sex").toString()));//性别
		String tel = request.getParameter("member_tel");//电话
		String tel2 = request.getParameter("member_tel2");//区号
		String Telephone = tel2+"-"+tel;
		member.setTel(Telephone);// 固定电话
		member.setFax(request.getParameter("member_fax"));// 固定电话
		
		member.setMobile(request.getParameter("member_mobile"));// 手机
		member.setAddress(request.getParameter("member_address"));// 店主住址
		member.setZip(request.getParameter("member_zip"));// 邮政编码
		return member;
	}

	/**
	 * 修改店铺设置
	 * 
	 * @param store
	 *            店铺信息,Store
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String edit() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("store_id", request.getParameter("store_id"));// 店铺id
			// store.setStore_name(request.getParameter("store_name"));// 店铺名称
			map.put("store_name", request.getParameter("store_name"));
			// store.setStore_banner(request.getParameter("store_banner"));//
			// 店铺推广语
			map.put("store_banner", request.getParameter("store_banner"));
			/* map.put("attr", request.getParameter("attr")); */
			/*
			 * map.put("zip", request.getParameter("zip")); map.put("tel",
			 * request.getParameter("tel"));
			 */
			map.put("qq", request.getParameter("qq"));
			map.put("description", request.getParameter("store_description"));
			map.put("store_logo", request.getParameter("store_logo"));
			/*
			 * map.put("store_banner", request.getParameter("store_banner"));
			 * map.put("store_provinceid",
			 * Integer.parseInt(request.getParameter(
			 * "province_id").toString())); map.put("store_cityid",
			 * Integer.parseInt(request.getParameter( "city_id").toString()));
			 * map.put("store_regionid", Integer.parseInt(request.getParameter(
			 * "region_id").toString())); map.put("store_province",
			 * request.getParameter("province")); map.put("store_city",
			 * request.getParameter("city")); map.put("store_region",
			 * request.getParameter("region"));
			 */
			this.storeManager.editStore(map);
			Store store2 = new Store();
			store2.setStore_id(Integer.valueOf(request.getParameter("store_id")));
			storeFeatureManager.deleteAll(store2);// 删除店铺的所有关键字和主营类别，之后再加入
			if (productFeature_description.length > 0) {
				int m = 0;
				StoreFeature storeFeature;
				for (String name : productFeature_description) {
					if (!StringUtil.isEmpty(name)) {
						storeFeature = new StoreFeature();
						storeFeature.setName(name);
						storeFeature.setStore_id(store2.getStore_id());
						storeFeature.setFeature_type(1);
						storeFeature.setFeature_index(m);
						storeFeatureManager.add(storeFeature);
						m++;
					}
				}
			}
			if (productCategore_description.length > 0) {
				int n = 0;
				StoreFeature storeFeature;
				for (String name : productCategore_description) {
					if (!StringUtil.isEmpty(name)) {
						storeFeature = new StoreFeature();
						storeFeature.setName(name);
						storeFeature.setStore_id(store2.getStore_id());
						storeFeature.setFeature_type(2);
						storeFeature.setFeature_index(n);
						storeFeatureManager.add(storeFeature);
						n++;
					}
				}
			}
			
			if (language=="zh") {
				this.showSuccessJson("修改店铺信息成功");
			}else
			{
				this.showSuccessJson("Успешно изменить информацию о магазине");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (language=="zh") {
				this.showErrorJson("修改店铺信息失败");
			}else
			{
				this.showErrorJson("Не удалось изменить информацию о магазине");
			}
			this.logger.error("修改店铺信息失败:" + e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * alt+shift+j
	 * 
	 * @return
	 */
	public String sellerInfoEdit() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Map map = new HashMap();
			map.put("name", request.getParameter("name"));
			map.put("email", request.getParameter("email"));
			map.put("member_id", request.getParameter("member_id"));
			map.put("id_number", request.getParameter("id_number"));
/*			map.put("birthday",
					DateUtil.getDateline(request.getParameter("birthday")));
			map.put("sex", Integer.valueOf(request.getParameter("sex")));*/
			map.put("tel", request.getParameter("tel"));
			map.put("mobile", request.getParameter("mobile"));
			map.put("address", request.getParameter("address"));
			map.put("zip", request.getParameter("zip"));
			map.put("fax", request.getParameter("fax"));
			this.storeManager.editSeller(map);
			if (language=="zh") {
				this.showSuccessJson("修改店主信息成功");
			}else {
				this.showSuccessJson("Успешно изменить информацию о продавце");
			}
		} catch (Exception e) {
			if (language=="zh") {
				this.showErrorJson("修改店主信息失败");
			}else {
				this.showErrorJson("Не удалось изменить информацию о продавце");
			}
			e.printStackTrace();
			this.logger.error("修改店主信息失败:" + e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 检测身份证
	 * 
	 * @param id_number
	 *            身份证号
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String checkIdNumber() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		Member member=storeMemberManager.getStoreMember();
		int member_id=member.getMember_id();
		int result = storeManager.checkIdNumber(id_number,member_id);
		if (result == 0) {
			if (language=="zh") {
				this.showErrorJson("0");
			}else {
				this.showSuccessJson("ID карты доступны");
			}
		} else {
			if (language=="zh") {
				this.showSuccessJson("1");
			}else {
				this.showErrorJson("ID уже существует");
			}
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 修改店铺Logo
	 * 
	 * @param logo
	 *            Logo,String
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String editStoreLogo() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			storeManager.editStoreOnekey("store_logo", logo);
			if (language=="zh") {
				this.showSuccessJson("店铺Logo修改成功");
			}else {
				this.showSuccessJson("Успешно изменить Logo магазина");
			}
		} catch (Exception e) {
			this.logger.error("修改店铺Logo失败:" + e);
			if (language=="zh") {
				this.showSuccessJson("店铺Logo修改成功");
			}else {
				this.showErrorJson("Не удалось изменить Logo магазина");
			}
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 提交店铺认证信息
	 * 
	 * @param store_id
	 *            店铺Id,Integer
	 * @param fsid_img
	 *            身份证图片,String
	 * @param fslicense_img
	 *            营业执照图片,String
	 * @param store_auth
	 *            店铺认证,Integer
	 * @param name_auth
	 *            店主认证,Integer
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String store_auth() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			storeManager.saveStoreLicense(store_id, fsid_img, fslicense_img,
					store_auth, name_auth);
			if (language=="zh") {
				this.showSuccessJson("提交成功，等待审核");
			}else {
				this.showSuccessJson("Успешно передавали, подождите проверять");
			}
		} catch (Exception e) {
			if (language=="zh") {
				this.showErrorJson("提交失败，请重试");
			}else {
				this.showErrorJson(" удалось передавать, пожалуйста, попробуйте снова");
			}
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 发送审核店铺邮件给管理员
	 * 
	 * @Description: TODO
	 * @param
	 * @return String
	 */

	@SuppressWarnings("unchecked")
	public String sendEmailToAdmin(Store store) {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			member = this.memberManager.get(member_id);
			// 获取站点信息
			EopSite site = EopSite.getInstance();
			// 编辑邮件信息发送邮件
			EmailModel emailModel = new EmailModel();
			emailModel.getData().put("logo", site.getLogofile());
			emailModel.getData().put("sitename", site.getSitename());
			emailModel.getData().put("store_name", store.getStore_name());
			emailModel.getData().put("store_member", member.getName());
			emailModel.getData().put("store_id", store.getStore_id());
			emailModel.getData().put("send_time",
					DateUtil.toString(new Date(), "yyyy年MM月dd日  hh:mm:ss"));
			emailModel.setTitle("审核店铺--" + site.getSitename());
			String toEmail = null;
			if (this.smtpManager.list().size() > 1) {
				toEmail = this.smtpManager.get(2).getUsername();
			}
			if (toEmail == null || StringUtil.isEmpty(toEmail)) {
				this.showErrorJson("请设置开店审核人邮箱！");
				if (language=="zh") {
					this.showErrorJson("请设置开店审核人邮箱！");
				}else {
					this.showErrorJson("Установите почту проверяющей магазина！");
				}
				return this.JSON_MESSAGE;
			}

			emailModel.setEmail(this.smtpManager.get(2).getUsername());
			emailModel.setTemplate("verifyshop_email_template.html");
			emailModel.setEmail_type("审核店铺");
			mailMessageProducer.send(emailModel);
			if (language=="zh") {
				this.showSuccessJson("请登录查收邮件并完成店铺审核。");
			}else {
				this.showSuccessJson("Пожалуйста, войдите вашу электронную почту чтобы выполнять проверять магазин。");
			}
		} catch (Exception e) {
			if (language=="zh") {
				this.showErrorJson("店铺审核邮件发送失败！");
			}else {
				this.showErrorJson("Не удалось отправить письмо проверки магазина！");
			}
		}
		return this.JSON_MESSAGE;
	}

	public String withdrawalInfoEdit() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Double store_account=Double.valueOf(request.getParameter("s_account"));
		if(StringUtil.isEmpty(request.getParameter("number"))){
			if (language=="zh") {
				this.showErrorJson("提现金额不能为空，请重新填写");
			}else {
				this.showErrorJson("Снять наличные не может быть бустым, пожалуйста заполните еще раз");
			}
			return this.JSON_MESSAGE;
		} 
		Pattern p = Pattern.compile("^[0-9]+(.[0-9]{1,3})?$");//^[0-9]+(.[0-9]{1,3})?$
		Matcher m = p.matcher(request.getParameter("number"));

		if (!m.matches()) {
			if (language=="zh") {
				this.showErrorJson("提现数目填写不正确，请重新填写");
			}else {
				this.showErrorJson("Количество снять наличные не заполняется правильно, пожалуйста, заполните еще раз");
			}
			return this.JSON_MESSAGE;
		}
	// if(number){
		 
	// }
		Double number=Double.valueOf(request.getParameter("number"));
		if(store_account-number<0){
			if (language=="zh") {
				this.showErrorJson("提现金额不能大于可用金额");
			}else {
				this.showErrorJson("Сумма вывода не может быть больше, чем доступная сумма");
			}
			return this.JSON_MESSAGE;
		}
		if(store_account-number>=0 && number>0){
			Withdrawal withdrawal = new Withdrawal();
			withdrawal.setCapital_channel(request
				.getParameter("capital_channel"));
			withdrawal.setCard(request.getParameter(
				"card"));
			withdrawal.setNote(request.getParameter("note"));
			withdrawal.setMember_id(Integer.valueOf(request
				.getParameter("member_id")));
			withdrawal.setCreate_date(DateUtil.getDateline());
			withdrawal
				.setNumber(Double.valueOf(request.getParameter("number")));
			withdrawal.setDetails(request.getParameter("details"));
			withdrawal.setType(1);
			withdrawal.setState(0);

			withdrawal.setStore_id(Integer.valueOf(request
				.getParameter("store_id")));
			this.storeManager.insertWithdrawal(withdrawal);
			if (language=="zh") {
				this.showSuccessJson("提现申请成功");
			}else {
				this.showSuccessJson("Успешно подавать заявление для снятия деньги");
			}
		}
		return this.JSON_MESSAGE;
	}

	public String wdEdit() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String id = request.getParameter("id");
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		try {
			this.storeManager.deleteWd(id);
			if (language=="zh") {
				this.showSuccessJson("删除成功");
			}else {
				this.showSuccessJson("Успешно удален");
			}

		} catch (Exception e) {
			if (language=="zh") {
				this.showErrorJson("删除失败" + e.getMessage());
			}else {
				this.showErrorJson("Не удается удалить" + e.getMessage());
			}
			this.logger.error("删除失败", e);
		}

		return this.JSON_MESSAGE;

	}

	public String wdAudit() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String id = request.getParameter("id");
		String store_id = request.getParameter("store_id");
		Map map = new HashMap();
		map.put("store_id", Integer.valueOf(store_id));
		map.put("member_id", Integer.valueOf(member_id));
		System.out.println(store_id);
		map.put("number", Double.valueOf(request.getParameter("number")));
		map.put("state", 1);
		map.put("id", Integer.valueOf(id));
		Store store=(Store)this.daoSupport.queryForObject("select * from es_store  where store_id=? ",Store.class,Integer.valueOf(store_id));
		double account=store.getAccount();
		double account2=account-Double.valueOf(request.getParameter("number"));
		try {
			this.storeManager.wdAuditok(map);
			if (language=="zh") {
				if(account2>0){
					this.showSuccessJson("放款成功");
				}else{
					this.showSuccessJson("余额不足");
				}
				
			}else {
				this.showSuccessJson("Успешно выдавать");
			}
			StoreMember storeMember = this.storeMemberManager.getStoreMember(Integer.parseInt(store_id));
			String mobile = storeMember.getMobile();
			if(account2>0){
				this.smsMobileManager.sendMobile(mobile, "您的平台账户收到提现金额:"+request.getParameter("number")+"元");
			}else{
				this.smsMobileManager.sendMobile(mobile, "您的平台账户提现金额不足:"+request.getParameter("number")+"元");
			}
			
		} catch (Exception e) {
			if (language=="zh") {
				this.showErrorJson("放款失败" + e.getMessage());
			}else {
				this.showErrorJson("Не удалось выдавать" + e.getMessage());
			}
			this.logger.error("放款失败", e);
		}
		return this.JSON_MESSAGE;
	}

	public String wdNoAgree() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String id = request.getParameter("id");
		try {
			this.storeManager.Nowd(id);
			if (language=="zh") {
				this.showSuccessJson("确定不批准成功");
			}else {
				this.showSuccessJson("Определяется не предоставлять успеха");
			}

		} catch (Exception e) {
			if (language=="zh") {
				this.showErrorJson("确定不批准失败" + e.getMessage());
			}else {
				this.showErrorJson("Не удалось определить не одобрить" + e.getMessage());
			}
			this.logger.error("确定不批准除失败", e);
		}
		return this.JSON_MESSAGE;
	}
	public String wdokAgree() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String id = request.getParameter("id");
		try {
			this.storeManager.okwd(id);
			if (language=="zh") {
				this.showSuccessJson("确定同意批准成功");
			}else {
				this.showSuccessJson("Успешно определить утверждения");
			}

		} catch (Exception e) {
			if (language=="zh") {
				this.showErrorJson("确定同意批准失败" + e.getMessage());
			}else {
				this.showErrorJson("Не удалось определить утверждения" + e.getMessage());
			}
			this.logger.error("确定同意批准除失败", e);
		}
		return this.JSON_MESSAGE;
	}
	
	//此方法过时，转移到StoreAction
	public void daochu(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		 // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("提现放款明细表");  
      //  sheet.setColumnWidth((short)2, (short) 7000);  
     //   sheet.setColumnWidth((short)4, (short) 7000);  
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
  //style。set
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("店主");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 1);  
        cell.setCellValue("店名");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 2);  
        cell.setCellValue("状态");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 3);  
        cell.setCellValue("卡号");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 4);  
        cell.setCellValue("数额");  
        cell.setCellStyle(style);  
        cell = row.createCell((short) 5);  
        cell.setCellValue("申请时间");  
        cell.setCellStyle(style);  
       
        
  
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
        List<Withdrawal> wlist=this.storeManager.getwdaa();
      
        	for(int i=0;i<wlist.size();i++){

        		row = sheet.createRow( i + 1);
        		System.out.println(i);
           
           
			try {
				Store store=this.storeManager.getStore(wlist.get(i).getStore_id());
				Member member=this.memberManager.getmember(wlist.get(i).getMember_id());
				 row.createCell((short) 0).setCellValue(member.getName());  
		            row.createCell((short) 1).setCellValue( store.getStore_name()); 
		            if( wlist.get(i).getState()==4){
		            row.createCell((short) 2).setCellValue("同意"); 
		            }
		            row.createCell((short) 3).setCellValue( wlist.get(i).getCard()); 
		            row.createCell((short) 4).setCellValue(""+ wlist.get(i).getNumber()); 
		           
		            Long time=wlist.get(i).getCreate_date();
		            String time1=DateUtil.toString(time,"yyyy-MM-dd");
		            row.createCell((short) 5).setCellValue(time1); 
			} catch (Exception e) {
				// TODO: handle exception
				
				 e.printStackTrace();
				 if(language=="zh"){
					 this.showErrorJson("导出数据失败" + e.getMessage());
		        	}else{
		        		 this.showErrorJson("производный тип данных не удалось" + e.getMessage()); 
		        	}
					this.logger.error("导出数据失败", e);
			}  
        	}
         
        // 第六步，将文件存到指定位置  
        try{          	
            FileOutputStream fout = new FileOutputStream("tixian.xls"); 
            wb.write(fout);
            fout.close(); 
        }catch (Exception e){  
        	if(language=="zh"){
        		this.showErrorJson("导出失败" + e.getMessage());
        	}else{
        		this.showErrorJson("Не удалось производен" + e.getMessage()); 
        	}
			this.logger.error("导出失败", e);
          
        }  

	}	
	
	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getLogo() {
		return logo;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public File getId_img() {
		return id_img;
	}

	public void setId_img(File id_img) {
		this.id_img = id_img;
	}

	public File getLicense_img() {
		return license_img;
	}

	public void setLicense_img(File license_img) {
		this.license_img = license_img;
	}

	public String getId_imgFileName() {
		return id_imgFileName;
	}

	public void setId_imgFileName(String id_imgFileName) {
		this.id_imgFileName = id_imgFileName;
	}

	public String getLicense_imgFileName() {
		return license_imgFileName;
	}

	public void setLicense_imgFileName(String license_imgFileName) {
		this.license_imgFileName = license_imgFileName;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getFsid_img() {
		return fsid_img;
	}

	public void setFsid_img(String fsid_img) {
		this.fsid_img = fsid_img;
	}

	public String getFslicense_img() {
		return fslicense_img;
	}

	public void setFslicense_img(String fslicense_img) {
		this.fslicense_img = fslicense_img;
	}

	public String getStatus_id_img() {
		return status_id_img;
	}

	public void setStatus_id_img(String status_id_img) {
		this.status_id_img = status_id_img;
	}

	public String getStatus_license_img() {
		return status_license_img;
	}

	public void setStatus_license_img(String status_license_img) {
		this.status_license_img = status_license_img;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public Integer getStore_auth() {
		return store_auth;
	}

	public void setStore_auth(Integer store_auth) {
		this.store_auth = store_auth;
	}

	public Integer getName_auth() {
		return name_auth;
	}

	public void setName_auth(Integer name_auth) {
		this.name_auth = name_auth;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String[] getProductFeature_description() {
		return productFeature_description;
	}

	public void setProductFeature_description(
			String[] productFeature_description) {
		this.productFeature_description = productFeature_description;
	}

	public String[] getProductCategore_description() {
		return productCategore_description;
	}

	public void setProductCategore_description(
			String[] productCategore_description) {
		this.productCategore_description = productCategore_description;
	}

	public IStoreFeatureManager getStoreFeatureManager() {
		return storeFeatureManager;
	}

	public void setStoreFeatureManager(IStoreFeatureManager storeFeatureManager) {
		this.storeFeatureManager = storeFeatureManager;
	}

	public Integer getStore_type() {
		return store_type;
	}

	public void setStore_type(Integer store_type) {
		this.store_type = store_type;
	}

	public EmailProducer getMailMessageProducer() {
		return mailMessageProducer;
	}

	public void setMailMessageProducer(EmailProducer mailMessageProducer) {
		this.mailMessageProducer = mailMessageProducer;
	}

	public ISmtpManager getSmtpManager() {
		return smtpManager;
	}

	public void setSmtpManager(ISmtpManager smtpManager) {
		this.smtpManager = smtpManager;
	}

	public Integer getIs_save() {
		return is_save;
	}

	public void setIs_save(Integer is_save) {
		this.is_save = is_save;
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public Integer getMember_prove_type() {
		return member_prove_type;
	}

	public void setMember_prove_type(Integer member_prove_type) {
		this.member_prove_type = member_prove_type;
	}

	public IsmsMobileManager getSmsMobileManager() {
		return smsMobileManager;
	}

	public void setSmsMobileManager(IsmsMobileManager smsMobileManager) {
		this.smsMobileManager = smsMobileManager;
	}

	public String getValiDateCode() {
		return valiDateCode;
	}

	public void setValiDateCode(String valiDateCode) {
		this.valiDateCode = valiDateCode;
	}
	
	
	
	
}
