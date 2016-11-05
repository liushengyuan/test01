package com.enation.app.b2b2c.core.action.backend.store;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.model.storeDTO.storeAndMemberDTO;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreLevelManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.Withdrawal;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.component.point.MemeberPointComponent;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.service.accont.IAccountDetailManager;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
		@Result(name = "store_list", type = "freemarker", location = "/b2b2c/admin/store/store_list.html"),
		@Result(name = "store_list1", type = "freemarker", location = "/b2b2c/admin/store/store_list1.html"),
		@Result(name = "store_account_list", type = "freemarker", location = "/b2b2c/admin/store/store_account_list.html"),
		@Result(name = "audit_list", type = "freemarker", location = "/b2b2c/admin/store/audit_list.html"),
		@Result(name = "license_list", type = "freemarker", location = "/b2b2c/admin/store/license_list.html"),
		@Result(name = "disStore_list", type = "freemarker", location = "/b2b2c/admin/store/disStore_list.html"),
		@Result(name = "edit", type = "freemarker", location = "/b2b2c/admin/store/store_edit.html"),
		@Result(name = "edit1", type = "freemarker", location = "/b2b2c/admin/store/store_edit1.html"),
		@Result(name = "add", type = "freemarker", location = "/b2b2c/admin/store/store_add.html"),
		@Result(name = "opt", type = "freemarker", location = "/b2b2c/admin/store/opt_member.html"),
		@Result(name = "pass", type = "freemarker", location = "/b2b2c/admin/store/pass.html"),
		@Result(name = "earnest_money", type = "freemarker", location = "/b2b2c/admin/store/store_earnest_money.html"),
		@Result(name = "storeInfo", type = "freemarker", location = "/b2b2c/admin/store/store_info.html"),
		@Result(name = "earnest_list", type = "freemarker", location = "/b2b2c/admin/store/store_earnest_list.html"),
		@Result(name = "auth_list", type = "freemarker", location = "/b2b2c/admin/store/auth_list.html"),
		@Result(name = "withdrawalaudit_list", type = "freemarker", location = "/b2b2c/admin/store/newWd.html"),
		@Result(name = "withdrawal_list", type = "freemarker", location = "/b2b2c/admin/store/allWd.html"), })
@Action("store")
/**
 * 店铺管理
 * @author LiFenLong
 *
 */
public class StoreAction extends WWAction {
	private IStoreLevelManager storeLevelManager;
	private IStoreManager storeManager;
	private IStoreMemberManager storeMemberManager;
	private IMemberManager memberManager;
	private IAccountDetailManager accountDetailManager;
	private IsmsMobileManager smsMobileManager;
	private Map other;
	private Integer disabled;
	private Integer storeId;
	private Store store;
	private List level_list;

	private Integer member_id;
	private Integer pass;
	private Integer name_auth;
	private Integer store_auth;
	private String storeName;
	private String check_description;
    private Integer store_initiallist;
	private String uname;
	private String password;
	private Integer assign_password;
	private Member member;
	private Double commission;
	private Integer tradeType;// 交易类型

	private Double creditAccount;// 添加保证金账户金额
	private String account_description;// 账户操作描述
	private Integer creditAccountStatus;// 保证金账户状态
	// 发送邮件
	private EmailProducer mailMessageProducer;
	private String commissionStr;
	//高级搜索字段
	private String adv_storeName;
	private String adv_memberName;
	private String start_time;
	private String end_time;
	private String parentStore;
	private String storeStatus;
	private String storeCountry;
	
	private Double account;// 添加平台账户余额
	private String account_manager;//客户经理
	private Integer account_area;//客户经理所属区域,0默认值，1绥芬河，2北京，3华南
	private String modify_persion;//修改人
	private Double 	init_commission1;//浮动保证金比例
	private String init_pic;//初始单图片

	/**
	 * 审核店铺查看详情
	 * 
	 * @return
	 */
	public String getStoreInfo() {
		store = this.storeManager.getStore(storeId);
		member = memberManager.get(store.getMember_id());
		return "storeInfo";
	}

	/**
	 * 店铺列表
	 * 
	 * @return
	 */
	public String store_list() {
		return "store_list";
	}
	public String store_list1() {
		return "store_list1";
	}
	/**
	 * 店铺账户列表
	 * 
	 * @return
	 */
	public String store_account_list() {
		return "store_account_list";
	}

	/**
	 * 查询店铺列表页面
	 * 
	 * @return
	 */
	public String store_account_listJson() {
		other = new HashMap();
		other.put("type", tradeType);
		other.put("name", storeName);
		this.showGridJson(accountDetailManager.store_account_list(other,
				this.getPage(), this.getPageSize()));
		return this.JSON_MESSAGE;
	}

	/**
	 * 开店申请
	 * 
	 * @return
	 */
	public String audit_list() {
		return "audit_list";
	}

	/**
	 * 店铺认证审核列表
	 * 
	 * @return
	 */
	public String license_list() {
		return "license_list";
	}

	/**
	 * 店铺保证金列表
	 * 
	 * @return
	 */
	public String earnest_list() {
		return "earnest_list";
	}

	/**
	 * 禁用店铺列表
	 * 
	 * @return
	 */
	public String disStore_list() {
		return "disStore_list";
	}

	/**
	 * 审核店铺
	 * 
	 * @return
	 */
	public String pass() {
		store = this.storeManager.getStore(storeId);
		if (store.getName_auth() == 2) {
			store.setId_img(UploadUtil.replacePath(store.getId_img()));
		}
		if (store.getStore_auth() == 2) {
			store.setLicense_img(UploadUtil.replacePath(store.getLicense_img()));
		}
		return "pass";
	}

	/**
	 * 跳转到保证金修改和保证金账户修改页面
	 * 
	 * @return
	 */
	public String earnest_money() {
		store = this.storeManager.getStore(storeId);
		return "earnest_money";
	}

	/**
	 * 保存保证金
	 * 
	 * @return
	 */
	/**
	 * 鎻愮幇瀹℃牳
	 * 
	 * @return
	 */
	public String withdrawalaudit_list() {
		return "withdrawalaudit_list";
	}

	/**
	 * 鎻愮幇瀹℃牳鏄庣粏
	 * 
	 * @return
	 */
	public String withdrawal_list() {
		return "withdrawal_list";
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public String save_earnest_money() {
		try {
			store = this.storeManager.getStore(storeId);
			store.setCredit_account(store.getCredit_account() + creditAccount);// 添加保证金金额
			store.setAccount(store.getAccount() + account);// 添加保证金金额
			store.setCredit_account_status(creditAccountStatus);
			this.storeManager.editStoreInfo(store);
			store = this.storeManager.getStore(storeId);
			member = memberManager.get(store.getMember_id());
			AccountDetail accountDetail = new AccountDetail();
			accountDetail.setMember_id(member.getMember_id());
			accountDetail.setMember_name(member.getName());
			accountDetail.setStore_id(storeId);
			accountDetail.setStore_name(store.getStore_name());
			accountDetail.setType(1);// 保证金
			accountDetail.setAmount_type(2);// 操作的是保证金账户
			accountDetail.setIncome_amount(creditAccount+account);
			accountDetail.setBalance(store.getAccount());
			accountDetail.setCredit_balance(store.getCredit_account());
			accountDetail.setNote(account_description);
			accountDetail.setOper_id(0);// 管理员默认id
			accountDetail.setOper_name("admin");// 管理员名称
			accountDetail.setUnique_id("B"
					+ DateUtil.toString(new Date(), "yyyyMMddhhmmss")
					+ UUID.randomUUID().toString().substring(0, 7));
			accountDetail.setCreate_time(DateUtil.getDateline());
			accountDetail.setLast_time(DateUtil.getDateline());
			this.accountDetailManager.add(accountDetail);
			this.showSuccessJson("保证金店铺修改成功！");
			String content = "";
			if(creditAccount>=0 && account==0){
				//店铺平台保证金增加
				content = "您店铺的保证金增加了"+creditAccount+"元";
			}else if(creditAccount==0 && account>=0){
				content = "您店铺的平台余额增加"+account+"元";
			}else if(creditAccount>=0 && account>=0){
				content = "您店铺的保证金增加了"+creditAccount+"元"+"您店铺的平台余额增加"+account+"元";
			}else{
				//店铺平台保证金减少
				content = "您店铺的保证金减少了"+Math.abs(creditAccount)+"元";
			}
			StoreMember storeMember = this.storeMemberManager.getStoreMember(storeId);
			String mobile = storeMember.getMobile();
			this.smsMobileManager.sendMobile(mobile, content);
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("保证金店铺修改失败");
			this.logger.error("操作失败:" + e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 查询店铺列表页面
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String store_listJson() {
		other = new HashMap();
		other.put("disabled", disabled);
		other.put("store_initiallist", store_initiallist);
		other.put("name", storeName);
		other.put("adv_storeName", adv_storeName);
		other.put("adv_memberName", adv_memberName);
		other.put("start_time", start_time);
		other.put("end_time", end_time);
		other.put("parentStore", parentStore);
		other.put("storeStatus", storeStatus);
		other.put("storeCountry", storeCountry);
		
		other.put("credit_account_status", creditAccountStatus);
		this.showGridJson(storeManager.store_list(other, disabled,
				this.getPage(), this.getPageSize()));
		return this.JSON_MESSAGE;
	}

	/**
	 * 审核通过
	 * 
	 * @return
	 */
	public String audit_pass() {
		try {
			if(pass==1){
				name_auth=1;
				store_auth=1;
			}
			storeManager.audit_pass(member_id, storeId, pass, name_auth,
					store_auth, commission, check_description);
			store = this.storeManager.getStore(storeId);
			this.sendEmailToSeller(store);
			this.showSuccessJson("操作成功");
		} catch (Exception e) {
			this.showErrorJson("审核失败");
			this.logger.error("操作失败:" + e);
		}
		return this.JSON_MESSAGE;
	}
	//发送邮件给用户进行审核提醒
	@SuppressWarnings("unchecked")
	public String sendEmailToSeller(Store store) {
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
			emailModel.getData().put("send_time",
					DateUtil.toString(new Date(), "yyyy年MM月dd日  hh:mm:ss"));
			emailModel.getData().put("pass", pass);//审核是否通过
			emailModel.getData().put("check_description", check_description);//审核缘由
			emailModel.setTitle("审核店铺结果--" + site.getSitename());
			String toEmail = member.getUname();//用户名就是邮件目的地
			emailModel.setEmail(toEmail);
			emailModel.setTemplate("verifyResult_email_template.html");
			emailModel.setEmail_type("审核店铺");
			mailMessageProducer.send(emailModel);
			this.showSuccessJson("请登录查收邮件并完成店铺审核。");
		} catch (Exception e) {
			e.printStackTrace();
			//this.showErrorJson("店铺审核结果邮件发送失败！");
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 禁用店铺
	 * 
	 * @return
	 */
	public String disStore() {
		try {
			storeManager.disStore(storeId);
			this.showSuccessJson("店铺禁用成功");
		} catch (Exception e) {
			this.showErrorJson("店铺禁用失败");
			this.logger.error("店铺禁用失败:" + e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 店铺恢复使用
	 * 
	 * @return
	 */
	public String useStore() {
		try {
			storeManager.useStore(storeId);
			this.showSuccessJson("店铺恢复使用成功");
		} catch (Exception e) {
			this.showErrorJson("店铺恢复使用失败");
			this.logger.error("店铺恢复使用失败" + e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 添加店铺
	 * 
	 * @return
	 */
	public String save() {
		try {
			store = new Store();
			store = this.assign();
			this.storeManager.save(store);
			this.showSuccessJson("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("保存失败");
		}
		return JSON_MESSAGE;
	}

	// 修改店铺
	public String edit() {
		store = this.storeManager.getStore(storeId);
		commissionStr=String.valueOf(store.getCommission());
		member = memberManager.get(store.getMember_id());
		level_list = storeLevelManager.storeLevelList();

		return "edit";
	}
	// 修改店铺
	public String edit1() {
		store = this.storeManager.getStore(storeId);
		commissionStr=String.valueOf(store.getCommission());
		member = memberManager.get(store.getMember_id());
		level_list = storeLevelManager.storeLevelList();

		return "edit1";
	}
	/**
	 * 修改店铺信息
	 * 
	 * @return
	 */
	public String saveEdit() {
		try {
			store = this.storeManager.getStore(storeId);
			store = this.assign();
			store.setStore_id(storeId);
			this.storeManager.editStoreInfo(store);
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			this.showErrorJson("修改失败，请稍后重试！");
		}
		return JSON_MESSAGE;
	}
	public String saveEdit1() {
		try {
			AdminUser adminUser = UserConext.getCurrentAdminUser();
			store = this.storeManager.getStore(storeId);
			store.setStore_id(storeId);
			if(store_initiallist!=null){
				store.setStore_initiallist(store_initiallist);
			}
			if(account_manager!=null && !StringUtil.isEmpty(account_manager)){
				store.setAccount_manager(account_manager);
			}
			if(account_area!=null){
				store.setAccount_area(account_area);
			}
			if(modify_persion!=null && !StringUtil.isEmpty(modify_persion)){
				store.setModify_persion(store.getModify_persion()+","+adminUser.getUsername());
			}else{
				store.setModify_persion(adminUser.getUsername());
			}
			if(init_commission1!=null){
				store.setInit_commission1(init_commission1);
			}
			if(commission!=null){
				store.setCommission(commission);
			}
			if(init_pic!=null && !StringUtil.isEmpty(init_pic)){
				store.setInit_pic(init_pic);
			}
			this.storeManager.editStoreInfo(store);
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			this.showErrorJson("修改失败，请稍后重试！");
		}
		return JSON_MESSAGE;
	}
	/**
	 * 获取店铺信息
	 * 
	 * @return
	 */
	private Store assign() {
		HttpServletRequest request = this.getRequest();

		store.setMember_name(request.getParameter("member_name"));
		store.setId_number(request.getParameter("id_number"));
		store.setStore_name(request.getParameter("store_name"));

		/*
		 * //店铺地址信息
		 * store.setStore_provinceid(Integer.parseInt(request.getParameter
		 * ("store_province_id").toString())); //店铺省ID
		 * store.setStore_cityid(Integer
		 * .parseInt(request.getParameter("store_city_id").toString())); //店铺市ID
		 * store.setStore_regionid(Integer.parseInt(request.getParameter(
		 * "store_region_id").toString())); //店铺区ID
		 * 
		 * store.setStore_province(request.getParameter("store_province"));
		 * //店铺省 store.setStore_city(request.getParameter("store_city")); //店铺市
		 * store.setStore_region(request.getParameter("store_region")); //店铺区
		 * store.setAttr(request.getParameter("attr")); //店铺详细地址 //店铺银行信息
		 * store.setBank_account_name
		 * (request.getParameter("bank_account_name")); //银行开户名
		 * store.setBank_account_number
		 * (request.getParameter("bank_account_number")); //公司银行账号
		 * store.setBank_name(request.getParameter("bank_name")); //开户银行支行名称
		 * store.setBank_code(request.getParameter("bank_code")); //支行联行号
		 * 
		 * store.setBank_provinceid(Integer.parseInt(request.getParameter(
		 * "bank_province_id").toString())); //开户银行所在省Id
		 * store.setBank_cityid(Integer
		 * .parseInt(request.getParameter("bank_city_id").toString()));
		 * //开户银行所在市Id
		 * store.setBank_regionid(Integer.parseInt(request.getParameter
		 * ("bank_region_id").toString())); //开户银行所在区Id
		 * 
		 * store.setBank_province(request.getParameter("bank_province"));
		 * //开户银行所在省 store.setBank_city(request.getParameter("bank_city"));
		 * //开户银行所在市 store.setBank_region(request.getParameter("bank_region"));
		 * //开户银行所在区
		 * 
		 * store.setAttr(request.getParameter("attr"));
		 * store.setZip(request.getParameter("zip"));
		 * store.setTel(request.getParameter("tel"));
		 */

		store.setCommission(commission);
		store.setStore_level(1);
		store.setDisabled(Integer.valueOf(request.getParameter("disabled")));
		return store;
	}

	/**
	 * 新增店铺验证用户
	 * 
	 * @return
	 */
	public String opt() {
		return "opt";
	}

	/**
	 * 验证用户
	 * 
	 * @param uname
	 *            会员名称
	 * @param password
	 *            密码
	 * @param assign_password
	 *            是否验证密码
	 * @return
	 */
	public String optMember() {
		try {
			StoreMember storeMember = storeMemberManager.getMember(uname);
			// 检测是否为新添加的会员
			if (storeMember.getIs_store() == null) {
				this.showSuccessJson(uname);
				return this.JSON_MESSAGE;
			}
			// 判断用户是否已经拥有店铺
			if (storeMember.getIs_store() == 1) {
				this.showErrorJson("会员已拥有店铺");
				return this.JSON_MESSAGE;
			}
			// 验证会员密码
			if (assign_password != null && assign_password == 1) {
				if (!storeMember.getPassword().equals(StringUtil.md5(password))) {
					this.showErrorJson("密码不正确");
					return this.JSON_MESSAGE;
				}
			}
			if (storeMember.getIs_store() == -1) {
				this.showSuccessJson(uname);
			} else {
				this.showSuccessJson("2");
			}
		} catch (Exception e) {
			this.showErrorJson("没有此用户");
		}
		return this.JSON_MESSAGE;

	}

	public String add() {
		level_list = storeLevelManager.storeLevelList();
		return "add";
	}

	/**
	 * 跳转到申请信息页面
	 * 
	 * @return
	 */
	public String auth_list() {
		return "auth_list";
	}

	public String auth_listJson() {
		
		this.showGridJson(storeManager.auth_list(other, disabled,
				this.getPage(), this.getPageSize()));
		return this.JSON_MESSAGE;
	}

	/**
	 * 审核店铺认证
	 * 
	 * @param storeId
	 *            店铺Id
	 * @param name_auth
	 *            店主认证
	 * @param store_auth
	 *            店铺认证
	 */
	public String auth_pass() {
		try {
			storeManager.auth_pass(storeId, name_auth, store_auth);
			this.showSuccessJson("操作成功");
		} catch (Exception e) {
			this.showErrorJson("操作失败");
			this.logger.error("审核店铺认证失败:" + e);
		}
		return this.JSON_MESSAGE;
	}
	
	public void daochuxml(){
		// 创建根节点 list;    
		        Element root = new Element("list");    
		          
		     // 根节点添加到文档中；    
	       Document Doc = new Document(root);    
		    
		       // 此处 for 循环可替换成 遍历 数据库表的结果集操作;    
	      for (int i = 0; i < 5; i++) {    
	            
		          // 创建节点 user;    
		           Element elements = new Element("user");    
	              
		           // 给 user 节点添加属性 id;    
		          elements.setAttribute("id", "" + i);    
		              
		           // 给 user 节点添加子节点并赋值；    
		          // new Element("name")中的 "name" 替换成表中相应字段，setText("xuehui")中 "xuehui 替换成表中记录值；    
		           elements.addContent(new Element("name").setText("xuehui"));   
		          elements.addContent(new Element("age").setText("28"));   
		          elements.addContent(new Element("sex").setText("Male"));   
		    
		           // 给父节点list添加user子节点;   
		          root.addContent(elements);   
		          XMLOutputter XMLOut = new XMLOutputter();   
		                 
		                 // 输出 user.xml 文件；   
		               try {
						XMLOut.output(Doc, new FileOutputStream("user.xml"));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}      
		
	}
	      }
	public String daochu(){
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		//HttpServletRequest request=ThreadContextHolder.getHttpRequest();
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
				 this.showErrorJson("导出数据失败" + e.getMessage());
				 this.logger.error("导出数据失败", e);
			}  
        }
         
        // 第六步，将文件存到指定位置  
        try{
            String filename = "tixian.xls";//设置下载时客户端Excel的名称    
            // 请见：http://zmx.iteye.com/blog/622529 
            //http://lancijk.iteye.com/blog/1390341   
            //filename = Util.encodeFilename(filename, request);    
            response.setContentType("application/vnd.ms-excel");    
            response.setHeader("Content-disposition", "attachment;filename=" + filename);    
            OutputStream ouputStream = response.getOutputStream();    
            wb.write(ouputStream);    
            ouputStream.flush();    
            ouputStream.close(); 
        }catch (Exception e){  
        	//this.showErrorJson("导出失败" + e.getMessage());
			this.logger.error("导出失败", e);
			e.printStackTrace();
          
        }  
        return null;
	}
	/**
	 * 导出店铺信息
	 * @return
	 */
	public String exportStore(){
		
		other = new HashMap();
		other.put("disabled", disabled);
		other.put("name", storeName);
		other.put("adv_storeName", adv_storeName);
		other.put("adv_memberName", adv_memberName);
		other.put("start_time", start_time);
		other.put("end_time", end_time);
		other.put("parentStore", parentStore);
		other.put("storeStatus", storeStatus);
		other.put("storeCountry", storeCountry);
		
		//other.put("credit_account_status", creditAccountStatus);
		
		//List<Store> storeList = this.storeManager.store_list(other,disabled);
		List<storeAndMemberDTO> storeLists=this.storeManager.store_lists(other, disabled);
		
//		this.showGridJson(storeManager.store_list(other, disabled,
//				this.getPage(), this.getPageSize()));
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook(); 
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("店铺明细表"); 
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("会员ID");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("会员登录名");  
        cell.setCellStyle(style);  
       
        cell = row.createCell((short) 2);  
        cell.setCellValue("注册时间");  
        cell.setCellStyle(style);  
       
        cell = row.createCell((short) 3);  
        cell.setCellValue("真实姓名");  
        cell.setCellStyle(style);  
       
        cell = row.createCell((short) 4);  
        cell.setCellValue("店铺ID");  
        cell.setCellStyle(style);  
       
        cell = row.createCell((short) 5);  
        cell.setCellValue("店铺名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("店铺地址");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("店铺类型");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("店铺注册时间");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 9);  
        cell.setCellValue("店铺描述");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 10);  
        cell.setCellValue("第一联系人");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 11);  
        cell.setCellValue("手机号");  
        cell.setCellStyle(style); 
       
        cell = row.createCell((short) 12);  
        cell.setCellValue("审核时间");  
        cell.setCellStyle(style); 
       
        cell = row.createCell((short) 13);  
        cell.setCellValue("审核理由");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 14);  
        cell.setCellValue("佣金");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 15);  
        cell.setCellValue("审核状态");  
        cell.setCellStyle(style); 
		
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到。
        int count = 0;
        for(storeAndMemberDTO store : storeLists){

        	row = sheet.createRow(count + 1); 
        	try {
				
				row.createCell((short) 0).setCellValue(store.getMember_id());  
		        row.createCell((short) 1).setCellValue(store.getUname()); 
		        row.createCell((short) 2).setCellValue(this.getTime(store.getCheck_time())); 		        
		        row.createCell((short) 3).setCellValue(store.getRealname()); 
		        row.createCell((short) 4).setCellValue(store.getStore_id()); 
		        row.createCell((short) 5).setCellValue(store.getStore_name()); 
		        
		        row.createCell((short) 6).setCellValue(store.getAttr()); 
		        row.createCell((short) 7).setCellValue(this.getStore(store.getStore_type())); 
		        row.createCell((short) 8).setCellValue(this.getTime(store.getCreate_time()));		        
		        row.createCell((short) 9).setCellValue(store.getDescription()); 
		        row.createCell((short) 10).setCellValue(store.getProve_name()); 
		        row.createCell((short) 11).setCellValue(store.getProve_mobile()); 
		        row.createCell((short) 12).setCellValue(this.getTime(store.getCheck_time())); 
		        row.createCell((short) 13).setCellValue(store.getCheck_description()); 
		        row.createCell((short) 14).setCellValue(store.getCommission()); 
		        row.createCell((short) 15).setCellValue(this.getStatusStore(store.getDisabled())); 
//		        row.createCell((short) 10).setCellValue(); 
//		        if(order.getCurrency().equals("CNY")){
//		        	row.createCell((short) 11).setCellValue("人民币"); 
//		        }else {
//		        	row.createCell((short) 11).setCellValue("卢布"); 
//				}
		        
		        
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				 this.showErrorJson("导出数据失败" + e.getMessage());
				 this.logger.error("导出数据失败", e);
			} 
        	
        	
        	count ++;
        	
        }
        
        // 第六步，将文件存到指定位置  
        try{
            String filename = "dianpu.xls";//设置下载时客户端Excel的名称    
            // 请见：http://zmx.iteye.com/blog/622529 
            //http://lancijk.iteye.com/blog/1390341   
            //filename = Util.encodeFilename(filename, request);    
            response.setContentType("application/vnd.ms-excel");    
            response.setHeader("Content-disposition", "attachment;filename=" + filename);    
            OutputStream ouputStream = response.getOutputStream();    
            wb.write(ouputStream);    
            ouputStream.flush();    
            ouputStream.close(); 
        }catch (Exception e){  
        	this.showErrorJson("导出失败" + e.getMessage());
			this.logger.error("导出失败", e);
			e.printStackTrace();
          
        } 
        
        this.showSuccessJson("成功导出！");
        
        
		return null;
	}
	/*
	 * 日期转换
	 */
	public String getTime (Long time){
		String text = "";
		if(time!=null&&time>0){
			text = DateUtil.toString(time,"yyyy-MM-dd");
		}		
		return text;
	}
	/**
	 * 店铺国家、市场转换
	 */
	public String getCountry(String name){
		String text = "";
		if(name.equals("CHN")){
			text = "中国";
		}else {
			text = "俄罗斯";
		}
		return text;
	}
	/**
	 * 店铺归属转换
	 * @return
	 */
	public String getParentStore(Integer status){
		String text = "";
		switch (status) {
		case 0:
			text = "独立店铺";
			break;
		case 1:
			text = "青云商城";
			break;
		case 2:
			text = "雅宝路商城";
			break;
		default:
			text = "错误状态";
			break;
		}
		return text;
	}
	/**
	 * 店铺状态转换
	 * @return
	 */
	public String getStatusStore(Integer status){
		String text = "";
		switch (status) {
		case -1:
			text = "审核未通过";
			break;
		case 0:
			text = "审核中";
			break;
		case 1:
			text = "开启中";
			break;
		case 2:
			text = "关闭中";
			break;
		default:
			text = "错误状态";
			break;
		}
		return text;
	}
	public String getStore(int store){
		String text="";
		if(store==1){
			return text="个人";
		}else if(store==2){
			return text="公司";
		}
		return text;
		 
	}
	public IStoreLevelManager getStoreLevelManager() {
		return storeLevelManager;
	}

	public void setStoreLevelManager(IStoreLevelManager storeLevelManager) {
		this.storeLevelManager = storeLevelManager;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public Map getOther() {
		return other;
	}

	public void setOther(Map other) {
		this.other = other;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}

	public Integer getStoreId() {
		return storeId;
	}

	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List getLevel_list() {
		return level_list;
	}

	public void setLevel_list(List level_list) {
		this.level_list = level_list;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public Integer getPass() {
		return pass;
	}

	public void setPass(Integer pass) {
		this.pass = pass;
	}

	public Integer getName_auth() {
		return name_auth;
	}

	public void setName_auth(Integer name_auth) {
		this.name_auth = name_auth;
	}

	public Integer getStore_auth() {
		return store_auth;
	}

	public void setStore_auth(Integer store_auth) {
		this.store_auth = store_auth;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getAssign_password() {
		return assign_password;
	}

	public void setAssign_password(Integer assign_password) {
		this.assign_password = assign_password;
	}

	public Double getCommission() {
		return commission;
	}

	public void setCommission(Double commission) {
		this.commission = commission;
	}

	public String getCheck_description() {
		return check_description;
	}

	public void setCheck_description(String check_description) {
		this.check_description = check_description;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Integer getCreditAccountStatus() {
		return creditAccountStatus;
	}

	public void setCreditAccountStatus(Integer creditAccountStatus) {
		this.creditAccountStatus = creditAccountStatus;
	}

	public Double getCreditAccount() {
		return creditAccount;
	}

	public void setCreditAccount(Double creditAccount) {
		this.creditAccount = creditAccount;
	}

	public String getAccount_description() {
		return account_description;
	}

	public void setAccount_description(String account_description) {
		this.account_description = account_description;
	}

	public IAccountDetailManager getAccountDetailManager() {
		return accountDetailManager;
	}

	public void setAccountDetailManager(
			IAccountDetailManager accountDetailManager) {
		this.accountDetailManager = accountDetailManager;
	}

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public String wd_listJson() {

		Map map = new HashMap();

		Page webpage = this.storeManager.searchWds(map, this.getPage(),
				this.getPageSize());
		this.showGridJson(webpage);

		return JSON_MESSAGE;
	}

	public String allwd_listJson() {

		Map map = new HashMap();

		Page webpage = this.storeManager.searchAllWds(map, this.getPage(),
				this.getPageSize());
		this.showGridJson(webpage);

		return JSON_MESSAGE;
	}

	public String getCommissionStr() {
		return commissionStr;
	}

	public void setCommissionStr(String commissionStr) {
		this.commissionStr = commissionStr;
	}

	public EmailProducer getMailMessageProducer() {
		return mailMessageProducer;
	}

	public void setMailMessageProducer(EmailProducer mailMessageProducer) {
		this.mailMessageProducer = mailMessageProducer;
	}

	public IsmsMobileManager getSmsMobileManager() {
		return smsMobileManager;
	}

	public void setSmsMobileManager(IsmsMobileManager smsMobileManager) {
		this.smsMobileManager = smsMobileManager;
	}


	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getParentStore() {
		return parentStore;
	}

	public void setParentStore(String parentStore) {
		this.parentStore = parentStore;
	}

	public String getStoreStatus() {
		return storeStatus;
	}

	public void setStoreStatus(String storeStatus) {
		this.storeStatus = storeStatus;
	}

	public String getStoreCountry() {
		return storeCountry;
	}

	public void setStoreCountry(String storeCountry) {
		this.storeCountry = storeCountry;
	}

	public String getAdv_storeName() {
		return adv_storeName;
	}

	public void setAdv_storeName(String adv_storeName) {
		this.adv_storeName = adv_storeName;
	}

	public String getAdv_memberName() {
		return adv_memberName;
	}

	public void setAdv_memberName(String adv_memberName) {
		this.adv_memberName = adv_memberName;
	}

	public Double getAccount() {
		return account;
	}

	public void setAccount(Double account) {
		this.account = account;
	}

	public Integer getStore_initiallist() {
		return store_initiallist;
	}

	public void setStore_initiallist(Integer store_initiallist) {
		this.store_initiallist = store_initiallist;
	}

	public String getAccount_manager() {
		return account_manager;
	}

	public void setAccount_manager(String account_manager) {
		this.account_manager = account_manager;
	}

	public Integer getAccount_area() {
		return account_area;
	}

	public void setAccount_area(Integer account_area) {
		this.account_area = account_area;
	}

	public String getModify_persion() {
		return modify_persion;
	}

	public void setModify_persion(String modify_persion) {
		this.modify_persion = modify_persion;
	}

	public Double getInit_commission1() {
		return init_commission1;
	}

	public void setInit_commission1(Double init_commission1) {
		this.init_commission1 = init_commission1;
	}

	public String getInit_pic() {
		return init_pic;
	}

	public void setInit_pic(String init_pic) {
		this.init_pic = init_pic;
	}
	
	

}
