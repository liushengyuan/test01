package com.enation.app.shop.core.action.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import com.enation.framework.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.component.bonus.service.BonusSession;
import com.enation.app.shop.component.bonus.service.IBonusManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.GoodLogisDetail;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.ordermanifest.Goodsinformation;
import com.enation.app.shop.core.model.ordermanifest.OrderFeeInformation;
import com.enation.app.shop.core.model.ordermanifest.Orderinformation;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.app.tradeease.core.service.smsmobile.IsmsMobileManager;
import com.enation.eop.processor.core.RemoteRequest;
import com.enation.eop.processor.core.Request;
import com.enation.eop.processor.core.Response;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * 订单api
 * @author kingapex
 *2013-7-24下午9:27:47
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("order")
@Results({
	@Result(name="kuaidi", type="freemarker", location="/themes/default/member/order_kuaidi.html") 
})
public class OrderApiAction extends WWAction {
	private ILogiManager logiManager;
	private IStoreManager storeManager;
	private IBonusManager bonusManager;
	
	public IBonusManager getBonusManager() {
		return bonusManager;
	}

	public void setBonusManager(IBonusManager bonusManager) {
		this.bonusManager = bonusManager;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
	private IGoodsManager goodsManager;
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	private IOrderFlowManager orderFlowManager;
	private IOrderManager orderManager;
	private IMemberAddressManager memberAddressManager;
	private Map kuaidiResult;
	private EmailProducer mailMessageProducer;
	private IStoreMemberManager storeMemberManager;
	private IMemberManager memberManager;
	private IsmsMobileManager smsMobileManager;
	private IStoreOrderManager storeOrderManager;
	private IPaymentManager paymentManager;
	private ICartManager cartManager;
	private IAllianceCountManager allianceCountManager;
	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public EmailProducer getMailMessageProducer() {
		return mailMessageProducer;
	}

	public void setMailMessageProducer(EmailProducer mailMessageProducer) {
		this.mailMessageProducer = mailMessageProducer;
	}

	/**
	 * 创建订单，需要购物车中有商品
	 * @param address_id:收货地址id.int型，必填项
	 * @param payment_id:支付方式id，int型，必填项
	 * @param shipping_id:配送方式id，int型，必填项
	 * @param shipDay：配送时间，String型 ，可选项
	 * @param shipTime，String型 ，可选项
	 * @param remark，String型 ，可选项
	 * 
	 * @return 返回json串
	 * result  为1表示添加成功0表示失败 ，int型
	 * message 为提示信息
	 * 
	 */
	public String create(){
		try{
			Order order  = this.createOrder();
			//获取订单列表的多个店铺store_id
//			String storeids = ThreadContextHolder.getHttpRequest().getParameter("storeids");
//			String [] storeid = storeids.split(",");
			//遍历store_id,发送通知短信
//			for (int i = 0; i < storeid.length; i++) {
//				Integer store_id = Integer.parseInt(storeid[i]);
//				StoreMember storeMember = this.storeMemberManager.getStoreMember(store_id);
//				String mobile = storeMember.getMobile();
//				this.smsMobileManager.sendMobile(mobile, "有客户在您的店铺购物下了订单，请及时查看！");
//			}
			//获取子订单列表,给每个店铺的发送短信通知
			List<StoreOrder> cOrderList= storeOrderManager.storeOrderList(order.getOrder_id());
			Double storebonusMoney=(double) 0.0;
			Map<Integer,Double>Storecoupons=new HashMap<Integer,Double>();
			for (StoreOrder storeOrder : cOrderList) {
				Integer store_id = storeOrder.getStore_id();
				Store store=this.storeManager.getStore(store_id);
				storebonusMoney=storebonusMoney+storeOrder.getDiscount();
				//记录每个店铺所拥有的优惠券
				Storecoupons.put(store_id, storeOrder.getDiscount());
				StoreMember storeMember = this.storeMemberManager.getStoreMember(store_id);
				String mobile = storeMember.getMobile();
				if(store.getStore_name().equals("")){
					this.smsMobileManager.sendMobile(mobile, "有客户在您的店铺购物下了订单，请及时查看！订单编号:"+storeOrder.getSn());
				}else{
					this.smsMobileManager.sendMobile(mobile, "有客户在您的店铺("+store.getStore_name()+")购物下了订单，请及时查看！订单编号:"+storeOrder.getSn());
				}
				}
			////System.out.println(storeid[0]+"店铺id*****");
			this.json = JsonMessageUtil.getObjectJson(order,"order");
			this.sendEmailToAdmin(order);
			//System.out.println("优惠的总金额:"+order.getDiscount());
			double integralprice = order.getIntegralprice();
			int cc=1;//专用于物流
			//根据订单使用的平台优惠券金额，改变订单的金额
			//优惠券的使用金额
			double bonusMoney = order.getBonusMoney();
			////System.out.println("完成订单后优惠券金额"+bonusMoney);
			Double needPayMoney = order.getOrder_amount();
			if(bonusMoney==0.0){//若没有平台优惠券，而Storecoupons.size>0则用到的是店铺优惠券
				if(Storecoupons.size()>0){
					//1、根据优惠金额，首先改变主订单中的金额。
					needPayMoney = CurrencyUtil.sub(order.getOrder_amount() , storebonusMoney);
					//根据订单id，修改需要支付的金额
					this.orderManager.editNeedPayMoney(order.getOrder_id(), needPayMoney);
					//修改日志表中的金额
					this.paymentManager.editNeedPayMoney(order.getOrder_id(), needPayMoney);
					//6、通过循环，减去每个子订单对应的金额
					for (StoreOrder storeOrder : cOrderList) {
						this.orderManager.subNeedPayMoney2(storeOrder.getOrder_id(), Storecoupons.get(storeOrder.getStore_id()));
						//this.paymentManager.subNeedPayMoney(storeOrder.getOrder_id(), Storecoupons.get(storeOrder.getStore_id()));
						this.paymentManager.subNeedPayMoney1(storeOrder.getOrder_id(), needPayMoney);
					}
					
				}
			}
			if(bonusMoney > 0){
				 Integer bonuscount=0;
                  for (StoreOrder storeOrder : cOrderList) {
					  if(storeOrder.getIs_bonus()==0){
						  bonuscount++;
					  }
				  }
				//1、根据优惠金额，首先改变主订单中的金额。
				needPayMoney = CurrencyUtil.sub(order.getOrder_amount() , bonusMoney);
				//根据订单id，修改需要支付的金额
				this.orderManager.editNeedPayMoney(order.getOrder_id(), needPayMoney);
				//修改日志表中的金额
				this.paymentManager.editNeedPayMoney(order.getOrder_id(), needPayMoney);
				//2、根据主订单，查询出所有子订单。
				//3、获取子订单列表
				//List<StoreOrder> cOrderList= storeOrderManager.storeOrderList(order.getOrder_id());
				//4、子订单的个数
				Integer count = cOrderList.size();
				//6、通过循环，减去每个子订单对应的金额
				for (StoreOrder storeOrder : cOrderList) {
					if(count-bonuscount==count){
						//5、平均每个订单需要减少的金额
						Double everyOrderNeed = CurrencyUtil.div(bonusMoney, count);
						this.orderManager.subNeedPayMoney(storeOrder.getOrder_id(), everyOrderNeed);
						this.paymentManager.subNeedPayMoney(storeOrder.getOrder_id(), everyOrderNeed);
					}else{
						if(storeOrder.getIs_bonus()==1){
							Double everyOrderNeedBonus = CurrencyUtil.div(bonusMoney, count-bonuscount);
							this.orderManager.subNeedPayMoney(storeOrder.getOrder_id(), everyOrderNeedBonus);
							this.paymentManager.subNeedPayMoney(storeOrder.getOrder_id(), everyOrderNeedBonus);
						 }else{
							 this.orderManager.subNeedPayMoney(storeOrder.getOrder_id(),0.0);
								this.paymentManager.subNeedPayMoney(storeOrder.getOrder_id(),0.0);
						 }
					}
				}
			}
			if(integralprice>0){
				//1、根据优惠金额，首先改变主订单中的金额。
				//Double needPayMoney = CurrencyUtil.sub(order.getOrder_amount() , order.getDiscount());
				needPayMoney = CurrencyUtil.sub(needPayMoney , integralprice);
				//根据订单id，修改需要支付的金额
				this.orderManager.editNeedPayMoney(order.getOrder_id(), needPayMoney);
				this.paymentManager.editNeedPayMoney(order.getOrder_id(), needPayMoney);
				//2、根据主订单，查询出所有子订单。
				//3、获取子订单列表
				//List<StoreOrder> cOrderList= storeOrderManager.storeOrderList(order.getOrder_id());
				//4、子订单的个数
				Integer count = cOrderList.size();
				//5、平均每个订单需要减少的金额
				Double everyOrderNeed = CurrencyUtil.div(order.getIntegralprice(), count);
				//6、通过循环，减去每个子订单对应的金额
				for (StoreOrder storeOrder : cOrderList) {
					/*if(bonusMoney==0.0){*/
						this.orderManager.subNeedPayMoneyDiscount(storeOrder.getOrder_id(),everyOrderNeed);
						//this.paymentManager.subNeedPayMoney1(storeOrder.getOrder_id(), needPayMoney);
						this.paymentManager.subNeedPayMoney(storeOrder.getOrder_id(), everyOrderNeed);
				/*	}*/
					/*if(bonusMoney > 0){
						this.orderManager.subNeedPayMoney2(storeOrder.getOrder_id(), everyOrderNeed);
					}*/
				}
			 this.orderManager.subTotalDiscount(order.getOrder_id(), integralprice);	
			}
			HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
			String sessionid = request.getSession().getId();
			if(cc==1){
				//List<StoreOrder> cOrderList= storeOrderManager.storeOrderList(order.getOrder_id());
				Double sumtotal=0.0;
				Double sumpaymoney=0.0;
				for (StoreOrder storeOrder : cOrderList) {
					Integer stores=storeOrder.getStore_id();
					Integer orderiid=storeOrder.getOrder_id();
					List<GoodLogisDetail> goodLogisDetails=this.logiManager.findLogisDetail(stores, sessionid);
					Double sendprice=0.0;
					for (GoodLogisDetail goodLogisDetail : goodLogisDetails) {
						 List<Cart> list=this.goodsManager.getCartById(goodLogisDetail.getGoods_id(), goodLogisDetail.getSession_id(), goodLogisDetail.getStore_id());
						 if(list.size()>0){
							 sendprice+=goodLogisDetail.getSendprice();
						 }
					}
					sumtotal+=sendprice;
					this.orderManager.updateShippingMoney(orderiid, sendprice);
					Order order2=this.orderManager.get(orderiid);
					//System.out.println("减去积分的金额："+storeOrder.getOrder_amount());
					Double needPayMoney2 = CurrencyUtil.add(storeOrder.getOrder_amount(),order2.getShipping_amount());
					Double needpay=CurrencyUtil.sub(needPayMoney2, order2.getDiscount());
					sumpaymoney+=needpay;
					//根据订单id，修改需要支付的金额
					this.orderManager.editNeedPayMoney(orderiid, needpay);
				    this.paymentManager.editNeedPayMoney(orderiid, needpay);
				    
				}
				//System.out.println(sumtotal);
				//System.out.println(sumpaymoney);
				this.orderManager.updateShippingMoney(order.getOrder_id(), sumtotal);
				//根据订单id，修改需要支付的金额
				this.orderManager.editNeedPayMoney(order.getOrder_id(), sumpaymoney);
			 	this.paymentManager.editNeedPayMoney(order.getOrder_id(), sumpaymoney);
			 	//记录用户member_id, session_id, order_id
			 	Integer member_id = this.allianceCountManager.getMember_id(order.getOrder_id());
			 	this.allianceCountManager.addOrderCount(member_id, sessionid, order.getOrder_id());
			}
			
			addPayMethodInformation(cOrderList,order);
			List<CartItem> itemListClean = this.cartManager.listGoodsOrder(sessionid);
			for (CartItem cartItem2 : itemListClean) {
				Integer goods_id=cartItem2.getGoods_id();
				cartManager.cleanIsCheck(sessionid, goods_id);
				this.logiManager.deleteLogisBySessiong(sessionid,goods_id);//根据当前sessionId删除物流详情记录
			}
			BonusSession.cleanAll();
		}catch(RuntimeException e){
			e.printStackTrace();
			this.logger.error("创建订单出错", e);
			this.showErrorJson(e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	private void addPayMethodInformation(List<StoreOrder> cOrderList,Order order) {
		for (StoreOrder storeOrder : cOrderList) {
			OrderFeeInformation orderFeeInformation=new OrderFeeInformation();
			orderFeeInformation.setOrder_id(storeOrder.getOrder_id());
			orderFeeInformation.setCHARGE(storeOrder.getNeed_pay_money());
			orderFeeInformation.setGOODSVALUE(storeOrder.getOrder_amount()+storeOrder.getShipping_amount());
			orderFeeInformation.setOTHERVALUE(storeOrder.getShipping_amount());
			orderFeeInformation.setTAX(0.0);
			orderFeeInformation.setCONSIGNEE(storeOrder.getShip_name());
			orderFeeInformation.setCONSIGNEEADDRESS(storeOrder.getShip_addr());
			orderFeeInformation.setCONSIGNEETELEPHONE(storeOrder.getShip_mobile());
			orderFeeInformation.setCONSIGNEECOUNTRY("142");
			orderFeeInformation.setPAYMENTCODE("1102961396");
			orderFeeInformation.setPAYMENTNAME(storeOrder.getPayment_name());
			String sn = DateUtil.toString(storeOrder.getCreate_time(), "yyyyMMdd") + "-" + 8819 + "-" + order.getSn();
			orderFeeInformation.setPAYMENTNO(sn);
			orderFeeInformation.setParent_id(order.getOrder_id());
			this.orderManager.addOrderFeeInformation(orderFeeInformation);
			List<OrderItem> list=this.orderManager.listGoodsItems(storeOrder.getOrder_id());
			for (OrderItem orderItem : list) {
				Goodsinformation goodsinformation=new Goodsinformation();
				goodsinformation.setOrder_id(orderItem.getOrder_id());
				StoreGoods storeGoods=this.goodsManager.getStoreGoods(orderItem.getGoods_id());
				goodsinformation.setGOODSNO(storeGoods.getSn());
				goodsinformation.setGOODSNAME(orderItem.getName());
				String specname=orderItem.getAddon();
				String sql="";
				if(specname!=null && !StringUtil.isEmpty(specname)){
					 List<Map> map=JSONArray.fromObject(specname);
					 for (Map map2 : map) {
						sql+=map2.get("name")+":"+map2.get("value");
					}
				   goodsinformation.setGOODSMODEL(orderItem.getName()+","+sql);
				}else{
					goodsinformation.setGOODSMODEL(orderItem.getName());
				}
				goodsinformation.setCODETS("1208900000");
				goodsinformation.setCOUNTRY("116");
				goodsinformation.setCURRENCY("142");
				goodsinformation.setUNIT("011");
				goodsinformation.setQUANTITY(orderItem.getNum());
				goodsinformation.setPRICE(orderItem.getPrice());
				goodsinformation.setFLAG("N");
				this.orderManager.addGoodInformationo(goodsinformation);
			}
		}
		Orderinformation orderinformation=new Orderinformation();
		orderinformation.setCUSTOMSCODE("0127");
		orderinformation.setBIZTYPE("N");
		orderinformation.setBIZTIME(DateUtil.getDateline());
		orderinformation.setIEFLAG("I");
		orderinformation.setECPCODE("DP06374253");
		orderinformation.setECPNAME("北京易智付电子商务有限公司");
		orderinformation.setCBECODE("DP06374253");
		orderinformation.setCBENAME("北京易智付电子商务有限公司");
		orderinformation.setORDERNO(order.getSn());
		orderinformation.setOrder_id(order.getOrder_id());
		this.orderManager.addInformationOrder(orderinformation);
	}

	@SuppressWarnings("unchecked")
	public  void  sendEmailToAdmin(Order order){
		
		Member member = UserConext.getCurrentMember();
		EopSite site = EopSite.getInstance();
		EmailModel emailModel = new EmailModel();
		String domain =RequestUtil.getDomain();
		String checkUrl =domain+"/login.html?forward=/member/infot.html";
		emailModel.getData().put("logo", site.getLogofile());
		emailModel.getData().put("sitename", site.getSitename());
		emailModel.getData().put("username", member.getName());
		emailModel.getData().put("send_time",
				DateUtil.toString(new Date(), "yyyy年MM月dd日  hh:mm:ss"));
		emailModel.getData().put("checkurl", checkUrl);
		//System.out.println(emailModel.getData().get("time"));
		emailModel.getData().put("domain", domain);
		emailModel.getData().put("order", order.getSn());
		emailModel.setTitle(member.getUname()+"您好，"+site.getSitename()+"会员下订单成功!");
		emailModel.setEmail(member.getEmail());
		if(member.getIs_mobile()!=1){
			emailModel.setTemplate("dingdan_email_template_point.html");
		}else{
			emailModel.setTemplate("dingdan_email_template.html");
		}
//	emailModel.setEmail(this.smtpManager.get(2).getUsername());
		/*emailModel.setTemplate("dingdan_email_template.html");*/
		emailModel.setEmail_type("下订单成功");
		mailMessageProducer.send(emailModel);
	}

	
	
	
	
	
	/**
	 * 取消订单
	 * @param sn:订单序列号.String型，必填项
	 * 
	 * @return 返回json串
	 * result  为1表示添加成功0表示失败 ，int型
	 * message 为提示信息
	 */
	
	public String cancel() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		try {
			String sn = request.getParameter("sn");
			String reason = request.getParameter("reason");
			Member member = UserConext.getCurrentMember();
			if (member == null) {
				this.showErrorJson("取消订单失败：登录超时");
			} else {
				this.orderFlowManager.cancel(sn, reason);
				if(sn!=null && !StringUtil.isEmpty(sn)){
					//取消订单归还积分
					this.orderFlowManager.restitutionPoints(sn);
					List <Order> orderlist=this.orderManager.queryforOrder(sn);
					Order order=this.orderManager.get(sn);
					if(order.getIs_bonus()==1){//是多个子订单时只取消一个不退还优惠券
						if(orderlist.size()==1){
							this.bonusManager.returnmember(member,sn,order.getOrder_id());//取消订单归还优惠券
						}
					}
				}
				this.showSuccessJson("取消订单成功");
			}
		} catch (RuntimeException re) {
			this.showErrorJson(re.getMessage());
		}
		return WWAction.JSON_MESSAGE;
	}
	
	/**
	 * 确认收货
	 * @param orderId:订单id.String型，必填项
	 * 
	 * @return 返回json串
	 * result  为1表示添加成功0表示失败 ，int型
	 * message 为提示信息
	 */
	public String rogConfirm() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		try {
			String orderId = request.getParameter("orderId");
			Member member = UserConext.getCurrentMember();
			if(member==null){
				this.showErrorJson("取消订单失败：登录超时");
			} else {
				this.orderFlowManager.rogConfirm(Integer.parseInt(orderId), member.getMember_id(), member.getUname(), member.getUname(),DateUtil.getDateline());
				this.showSuccessJson("确认收货成功");
			}
		} catch (Exception e) {
			this.showErrorJson("数据库错误");
		}
		return WWAction.JSON_MESSAGE;
	}
	
	/**************以下非api，不用书写文档**************/
	
	private Order createOrder(){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		HttpSession session = ServletActionContext.getRequest().getSession();
		String  pointprice= request.getParameter("pointprice");
		/*Locale locale = (Locale)session.getAttribute("locale");
		String language=locale.getLanguage();
		String currency;
		if(language.equals("zh")){
			currency="CNY";
		}else if(language.equals("ru")){
			currency="RUB";
		}else{
			currency="USD";
		}*/
		Integer shippingId = StringUtil.toInt(request.getParameter("typeId"),null);
 		if(shippingId==null ) throw new RuntimeException("配送方式不能为空");
		
		Integer paymentId = StringUtil.toInt(request.getParameter("paymentId"),0);
 		if(paymentId==0 ) throw new RuntimeException("支付方式不能为空");
		
		Order order = new Order() ;
		//order.setCurrency(currency);//设置货币币种
		order.setShipping_id(shippingId); //配送方式
		order.setPayment_id(paymentId);//支付方式
		Integer addressId = StringUtil.toInt(request.getParameter("addressId"), false);
		MemberAddress address = new MemberAddress();

		//address = this.createAddress();	
		address = this.memberAddressManager.getAddress(addressId);	
		order.setShip_provinceid(address.getProvince_id());
		order.setShip_cityid(address.getCity_id());
		order.setShip_regionid(address.getRegion_id());
		
		order.setShip_addr(address.getAddr());
		order.setShip_mobile(address.getMobile());
		order.setShip_tel(address.getTel());
		order.setShip_zip(address.getZip());
		order.setShipping_area(address.getProvince()+"-"+ address.getCity());//+"-"+ address.getRegion()
		order.setShip_name(address.getName());
		order.setRegionid(address.getRegion_id());
		
//		if (addressId.intValue()==0) {
		//新的逻辑：只要选中了“保存地址”，就会新增一条收货地址，即使数据完全没有修改
	 	if ("yes".equals(request.getParameter("saveAddress"))) {
	 		Member member = UserConext.getCurrentMember();
			if (member != null) {
					address.setAddr_id(null);
					addressId= this.memberAddressManager.addAddress(address);
			}
		}
//		}
	 	
 	 	address.setAddr_id(addressId);
	 	order.setMemberAddress(address);
		order.setShip_day(request.getParameter("shipDay"));
		order.setShip_time(request.getParameter("shipTime"));
		order.setRemark(request.getParameter("remark"));
		order.setAddress_id(address.getAddr_id());//保存本订单的会员id
		return	this.orderManager.add(order,request.getSession().getId());
		
	}
	
	private MemberAddress createAddress(){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		
		MemberAddress address = new MemberAddress();
 

		String name = request.getParameter("shipName");
		address.setName(name);

		String tel = request.getParameter("shipTel");
		address.setTel(tel);

		String mobile = request.getParameter("shipMobile");
		address.setMobile(mobile);

		String province_id = request.getParameter("province_id");
		if(province_id!=null){
			address.setProvince_id(Integer.valueOf(province_id));
		}

		String city_id = request.getParameter("city_id");
		if(city_id!=null){
			address.setCity_id(Integer.valueOf(city_id));
		}

		String region_id = request.getParameter("region_id");
		if(region_id!=null){
			address.setRegion_id(Integer.valueOf(region_id));
		}

		String province = request.getParameter("province");
		address.setProvince(province);

		String city = request.getParameter("city");
		address.setCity(city);

		String region = request.getParameter("region");
		address.setRegion(region);

		String addr = request.getParameter("shipAddr");
		address.setAddr(addr);

		String zip = request.getParameter("shipZip");
		address.setZip(zip);
	
		return address;
	}

	public String orderKuaidi(){
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String logino = request.getParameter("logino");//物流号
			String code = request.getParameter("code");//物流公司代码
			if(logino==null || logino.length()<5){
				Map result = new HashMap();
				result.put("status", "-1");
				this.showErrorJson("请输入正确的运单号");
				return "";
			}
			if(code == null || code.equals("")){
				code = "yuantong";
			}
			Request remoteRequest  = new RemoteRequest();
			String kuaidiurl="http://www.kuaidiapi.cn/rest/?uid=34210&key=ceea91ab561640979b75e78a0cbd5128&order="+logino+"&id="+code;
			//System.out.println(kuaidiurl);	
			Response remoteResponse = remoteRequest.execute(kuaidiurl);
			String content  = remoteResponse.getContent();
			kuaidiResult = (Map)JSONObject.toBean( JSONObject.fromObject(content) ,Map.class);
			
		} catch (Exception e) {
			this.logger.error("查询货运状态", e);
		}
		return "kuaidi";
	}

	//set get
	public IOrderManager getOrderManager() {
		return orderManager;
	}


	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}


	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}


	public void setMemberAddressManager(IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}

	public IOrderFlowManager getOrderFlowManager() {
		return orderFlowManager;
	}

	public void setOrderFlowManager(IOrderFlowManager orderFlowManager) {
		this.orderFlowManager = orderFlowManager;
	}

	public Map getKuaidiResult() {
		return kuaidiResult;
	}

	public void setKuaidiResult(Map kuaidiResult) {
		this.kuaidiResult = kuaidiResult;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public IsmsMobileManager getSmsMobileManager() {
		return smsMobileManager;
	}

	public void setSmsMobileManager(IsmsMobileManager smsMobileManager) {
		this.smsMobileManager = smsMobileManager;
	}

	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}

	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}

	public IPaymentManager getPaymentManager() {
		return paymentManager;
	}

	public void setPaymentManager(IPaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}

	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}

	public IAllianceCountManager getAllianceCountManager() {
		return allianceCountManager;
	}

	public void setAllianceCountManager(IAllianceCountManager allianceCountManager) {
		this.allianceCountManager = allianceCountManager;
	}
	
	
	
}
