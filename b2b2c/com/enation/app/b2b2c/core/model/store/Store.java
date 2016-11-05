package com.enation.app.b2b2c.core.model.store;

import com.enation.framework.database.NotDbField;
import com.enation.framework.database.PrimaryKeyField;

/**
 * 店铺
 * @author LiFenLong
 *
 */
public class Store {

	private int store_id;	//店铺Id
	private String store_name;	//店铺名称
	private int  store_provinceid;	//省
	private int  store_cityid;	//市
	private int  store_regionid;	//区
	
	private String  store_province;	//省
	private String  store_city;	//市
	private String  store_region;	//区
	private String  store_location;	//店铺位置编号
	
	private String  attr;	//详细地址
	private String zip;		//邮编
	private String  tel;	//联系方式
	private int store_level;//店铺等级
	private int  member_id;	//会员Id
	private String  member_name;	//会员名称
	private String  id_number;	//身份证号(证件号)
	private String  id_img;		//身份证照片(正面)
	private String  license_img;//执照照片(公司开户才有)
	private int  disabled;		//店铺状态  0待审核  1审核通过  -1审核未通过 -2所有  2关闭中
	private Long  create_time;	//创建时间
	private Long  end_time;		//关闭时间
	private String  store_logo;	//店铺logo
	private String store_banner;//店铺横幅
	private String  description;//店铺简介
	private int  store_recommend;//是否推荐
	private int  store_theme;	//店铺主题
	private int store_credit;	//店铺信用
	private double  praise_rate;	//店铺好评率
	private double  store_desccredit;	//描述相符度
	private double  store_servicecredit;	//服务态度分数
	private double store_deliverycredit;	//发货速度分数
	private int  store_collect;	//店铺收藏数量
	private int store_auth;	//店铺认证
	private int name_auth;	//店主认证
	private int goods_num; //店铺商品数量
	private String qq;		//店铺客服QQ
	
	private String prove_name;//财务联系人、银行卡所有人
	private int prove_type;// 证件类型，1，身份证，2，护照，3，军官证
	private String prove_mobile;//财务联系人、银行卡所有人的联系手机
	private String bank_address;//开户所在的银行地址
	private String  prove_img;//公司开户时提交的开户证明图片地址
	private String  reverse_id_img;//身份证反面图片地址
	private String  bcard_img;//银行卡正面图片地址
	
	
	private Double 	commission;	//店铺佣金比例
	private String 	bank_account_name;		//银行开户名   
	private String 	bank_account_number;		//公司银行账号
	private String  	bank_name;				//开户银行支行名称
	private String  	bank_code;				//支行联行号
	private Integer 	bank_provinceid;		//开户银行所在省Id
	private Integer 	bank_cityid;			//开户银行所在市Id
	private Integer 	bank_regionid;			//开户银行所在区Id
	private String  	bank_province;			//开户银行所在省
	private String  	bank_city;				//开户银行所在市
	private String  	bank_region;				//开户银行所在区
	
	private String check_description;
	private Long check_time;
	
	private int store_type;//店铺类型，1个人，2公司
	private String company_name;//公司名称
	private String enterprise_legal;//企业法人

	private String registration_number;//公司注册号
	private Long  company_create_time;	//公司创建日期
	private int company_size;//公司规模 1,2,3,4，5,6
	private String main_products;//公司主营产品
	private String email;//公司邮箱
	private String fax_number;//公司传真号
	private String company_url;//公司网址
	private String  company_description;//公司简介
	
	private Double account;//卖家平台账户
	private Double credit_account;//卖家保证金账户
	private  int credit_account_status;//卖家保证金账户状态：0不可用，1可用
	
	private int parent_store;//所属店铺id，0表示独立店铺，1表示青云商城
	
	private String prove_name2;//紧急联系人
	private String prove_mobile2;//紧急联系人手机
	private String prove_name3;//备用联系人
	private String prove_mobile3;//备用联系人手机
	
	private String store_country;//店铺所在国
	private String store_market;//店铺投放市场 RUS为俄罗斯，CHN中国
	
	private Integer sales;//过去一个月的销售量
	private String store_rate;//店铺评级
	
	private int store_initiallist;//是否获取初始单，未获取为1，已获取为2，全选为0
	private String account_manager;//客户经理
	private Integer account_area;//客户经理所属区域,0默认值，1绥芬河，2北京，3华南
	private String modify_persion;//修改人
	private Double 	init_commission1;//浮动保证金比例
	private String init_pic;//初始单图片
	public int getStore_initiallist() {
		return store_initiallist;
	}
	public void setStore_initiallist(int store_initiallist) {
		this.store_initiallist = store_initiallist;
	}
	@NotDbField
	public Integer getSales() {
		return sales;
	}
	public void setSales(Integer sales) {
		this.sales = sales;
	}
	@NotDbField
	public String getStore_rate() {
		return store_rate;
	}
	public void setStore_rate(String store_rate) {
		this.store_rate = store_rate;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getStore_province() {
		return store_province;
	}
	public String getStore_banner() {
		return store_banner;
	}

	public void setStore_banner(String store_banner) {
		this.store_banner = store_banner;
	}

	public void setStore_province(String store_province) {
		this.store_province = store_province;
	}
	public String getStore_city() {
		return store_city;
	}
	public void setStore_city(String store_city) {
		this.store_city = store_city;
	}
	public String getStore_region() {
		return store_region;
	}
	public void setStore_region(String store_region) {
		this.store_region = store_region;
	}
	@PrimaryKeyField
	public int getStore_id() {
		return store_id;
	}
	public void setStore_id(int store_id) {
		this.store_id = store_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public int getStore_provinceid() {
		return store_provinceid;
	}
	public void setStore_provinceid(int store_provinceid) {
		this.store_provinceid = store_provinceid;
	}
	public int getStore_cityid() {
		return store_cityid;
	}
	public void setStore_cityid(int store_cityid) {
		this.store_cityid = store_cityid;
	}
	public int getStore_regionid() {
		return store_regionid;
	}
	public void setStore_regionid(int store_regionid) {
		this.store_regionid = store_regionid;
	}
	public String getAttr() {
		return attr;
	}
	public void setAttr(String attr) {
		this.attr = attr;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public int getStore_level() {
		return store_level;
	}
	public void setStore_level(int store_level) {
		this.store_level = store_level;
	}
	public int getMember_id() {
		return member_id;
	}
	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}
	public String getMember_name() {
		return member_name;
	}
	public void setMember_name(String member_name) {
		this.member_name = member_name;
	}
	public String getId_number() {
		return id_number;
	}
	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	public String getId_img() {
		return id_img;
	}
	public void setId_img(String id_img) {
		this.id_img = id_img;
	}
	public String getLicense_img() {
		return license_img;
	}
	public void setLicense_img(String license_img) {
		this.license_img = license_img;
	}
	public int getDisabled() {
		return disabled;
	}
	public void setDisabled(int disabled) {
		this.disabled = disabled;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public Long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	public String getStore_logo() {
		return store_logo;
	}
	public void setStore_logo(String store_logo) {
		this.store_logo = store_logo;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getStore_recommend() {
		return store_recommend;
	}
	public void setStore_recommend(int store_recommend) {
		this.store_recommend = store_recommend;
	}
	public int getStore_theme() {
		return store_theme;
	}
	public void setStore_theme(int store_theme) {
		this.store_theme = store_theme;
	}
	public int getStore_credit() {
		return store_credit;
	}
	public void setStore_credit(int store_credit) {
		this.store_credit = store_credit;
	}
	public double getPraise_rate() {
		return praise_rate;
	}
	public void setPraise_rate(double praise_rate) {
		this.praise_rate = praise_rate;
	}
	public double getStore_desccredit() {
		return store_desccredit;
	}
	public void setStore_desccredit(double store_desccredit) {
		this.store_desccredit = store_desccredit;
	}
	public double getStore_servicecredit() {
		return store_servicecredit;
	}
	public void setStore_servicecredit(double store_servicecredit) {
		this.store_servicecredit = store_servicecredit;
	}
	public double getStore_deliverycredit() {
		return store_deliverycredit;
	}
	public void setStore_deliverycredit(double store_deliverycredit) {
		this.store_deliverycredit = store_deliverycredit;
	}
	public int getStore_auth() {
		return store_auth;
	}
	public void setStore_auth(int store_auth) {
		this.store_auth = store_auth;
	}
	public int getName_auth() {
		return name_auth;
	}
	public void setName_auth(int name_auth) {
		this.name_auth = name_auth;
	}
	public int getGoods_num() {
		return goods_num;
	}
	public void setGoods_num(int goods_num) {
		this.goods_num = goods_num;
	}
	public int getStore_collect() {
		return store_collect;
	}
	public void setStore_collect(int store_collect) {
		this.store_collect = store_collect;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	public String getBank_account_name() {
		return bank_account_name;
	}
	public void setBank_account_name(String bank_account_name) {
		this.bank_account_name = bank_account_name;
	}
	public String getBank_account_number() {
		return bank_account_number;
	}
	public void setBank_account_number(String bank_account_number) {
		this.bank_account_number = bank_account_number;
	}
	public String getBank_name() {
		return bank_name;
	}
	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}
	public String getBank_code() {
		return bank_code;
	}
	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}
	public Integer getBank_provinceid() {
		return bank_provinceid;
	}
	public void setBank_provinceid(Integer bank_provinceid) {
		this.bank_provinceid = bank_provinceid;
	}
	public Integer getBank_cityid() {
		return bank_cityid;
	}
	public void setBank_cityid(Integer bank_cityid) {
		this.bank_cityid = bank_cityid;
	}
	public Integer getBank_regionid() {
		return bank_regionid;
	}
	public void setBank_regionid(Integer bank_regionid) {
		this.bank_regionid = bank_regionid;
	}
	public String getBank_province() {
		return bank_province;
	}
	public void setBank_province(String bank_province) {
		this.bank_province = bank_province;
	}
	public String getBank_city() {
		return bank_city;
	}
	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}
	public String getBank_region() {
		return bank_region;
	}
	public void setBank_region(String bank_region) {
		this.bank_region = bank_region;
	}
	public int getStore_type() {
		return store_type;
	}
	public void setStore_type(int store_type) {
		this.store_type = store_type;
	}
	public String getEnterprise_legal() {
		return enterprise_legal;
	}
	public void setEnterprise_legal(String enterprise_legal) {
		this.enterprise_legal = enterprise_legal;
	}
	public String getRegistration_number() {
		return registration_number;
	}
	public void setRegistration_number(String registration_number) {
		this.registration_number = registration_number;
	}
	public Long getCompany_create_time() {
		return company_create_time;
	}
	public void setCompany_create_time(Long company_create_time) {
		this.company_create_time = company_create_time;
	}
	public int getCompany_size() {
		return company_size;
	}
	public void setCompany_size(int company_size) {
		this.company_size = company_size;
	}
	public String getMain_products() {
		return main_products;
	}
	public void setMain_products(String main_products) {
		this.main_products = main_products;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax_number() {
		return fax_number;
	}
	public void setFax_number(String fax_number) {
		this.fax_number = fax_number;
	}
	public String getCompany_url() {
		return company_url;
	}
	public void setCompany_url(String company_url) {
		this.company_url = company_url;
	}
	public String getCompany_description() {
		return company_description;
	}
	public void setCompany_description(String company_description) {
		this.company_description = company_description;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCheck_description() {
		return check_description;
	}
	public void setCheck_description(String check_description) {
		this.check_description = check_description;
	}
	public Long getCheck_time() {
		return check_time;
	}
	public void setCheck_time(Long check_time) {
		this.check_time = check_time;
	}
	public Double getAccount() {
		return account;
	}
	public void setAccount(Double account) {
		this.account = account;
	}
	public Double getCredit_account() {
		return credit_account;
	}
	public void setCredit_account(Double credit_account) {
		this.credit_account = credit_account;
	}
	public int getCredit_account_status() {
		return credit_account_status;
	}
	public void setCredit_account_status(int credit_account_status) {
		this.credit_account_status = credit_account_status;
	}
	public String getProve_name() {
		return prove_name;
	}
	public void setProve_name(String prove_name) {
		this.prove_name = prove_name;
	}
	public int getProve_type() {
		return prove_type;
	}
	public void setProve_type(int prove_type) {
		this.prove_type = prove_type;
	}
	public String getProve_mobile() {
		return prove_mobile;
	}
	public void setProve_mobile(String prove_mobile) {
		this.prove_mobile = prove_mobile;
	}
	public String getBank_address() {
		return bank_address;
	}
	public void setBank_address(String bank_address) {
		this.bank_address = bank_address;
	}
	public String getProve_img() {
		return prove_img;
	}
	public void setProve_img(String prove_img) {
		this.prove_img = prove_img;
	}
	public String getReverse_id_img() {
		return reverse_id_img;
	}
	public void setReverse_id_img(String reverse_id_img) {
		this.reverse_id_img = reverse_id_img;
	}
	public String getBcard_img() {
		return bcard_img;
	}
	public void setBcard_img(String bcard_img) {
		this.bcard_img = bcard_img;
	}
	public int getParent_store() {
		return parent_store;
	}
	public void setParent_store(int parent_store) {
		this.parent_store = parent_store;
	}
	public String getProve_name2() {
		return prove_name2;
	}
	public void setProve_name2(String prove_name2) {
		this.prove_name2 = prove_name2;
	}
	public String getProve_mobile2() {
		return prove_mobile2;
	}
	public void setProve_mobile2(String prove_mobile2) {
		this.prove_mobile2 = prove_mobile2;
	}
	public String getProve_name3() {
		return prove_name3;
	}
	public void setProve_name3(String prove_name3) {
		this.prove_name3 = prove_name3;
	}
	public String getProve_mobile3() {
		return prove_mobile3;
	}
	public void setProve_mobile3(String prove_mobile3) {
		this.prove_mobile3 = prove_mobile3;
	}
	public String getStore_location() {
		return store_location;
	}
	public void setStore_location(String store_location) {
		this.store_location = store_location;
	}
	public String getStore_country() {
		return store_country;
	}
	public void setStore_country(String store_country) {
		this.store_country = store_country;
	}
	public String getStore_market() {
		return store_market;
	}
	public void setStore_market(String store_market) {
		this.store_market = store_market;
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
