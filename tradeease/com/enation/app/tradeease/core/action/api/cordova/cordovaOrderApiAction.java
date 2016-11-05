package com.enation.app.tradeease.core.action.api.cordova;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.PaymentDetail;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IOrderReportManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.app.tradeease.core.service.cordova.ICartManagerApp;
import com.enation.app.tradeease.core.service.cordova.IMemberOrderManagerApp;
import com.enation.app.tradeease.core.service.cordova.IOrderManagerApp;
import com.enation.eop.processor.core.RemoteRequest;
import com.enation.eop.processor.core.Request;
import com.enation.eop.processor.core.Response;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * 购物车api
 * 
 * @author kingapex 2013-7-19下午12:58:43
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/cordova")
@Action("cordovaOrderApiAction")
public class cordovaOrderApiAction extends WWAction {
	private IStoreCartManager storeCartManager;
	private IOrderFlowManager orderFlowManager;
	private IOrderManagerApp orderManagerApp;
	private IMemberAddressManager memberAddressManager;
	private IMemberManager memberManager;
	private ICartManagerApp cartManagerApp;
	private IMemberOrderManagerApp memberOrderManagerApp;
	private IOrderReportManager orderReportManager;
	private IDaoSupport daoSupport;
	private IStoreOrderManager storeOrderManager;
	private ICartManager cartManager;
	private Integer member_id;
	private String sn;
	private Map kuaidiResult;
	private String productIds;
	private Integer order_id;
	/**
	 * 创建订单，需要购物车中有商品
	 * @param address_id:收货地址id.int型，必填项 addressId
	 * @param payment_id:支付方式id，int型，必填项 paymentId
	 * @param shipping_id:配送方式id，int型，必填项  typeId
	 * @param product_id
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
			
			this.json = JsonMessageUtil.getObjectJson(order,"order");
			
		}catch(RuntimeException e){
			e.printStackTrace();
			this.logger.error("该订单已提交", e);
			
			this.showErrorJson("该订单已提交");
		}
		return this.JSON_MESSAGE;
	}
	

/**
     * 再来一单获取相对应的参数
     * @param sn:订单序列号.String型，必填项，
     * @param member_id:
     * 
     * @return 返回json串
     * result  为1表示添加成功0表示失败 ，int型
     * message 为提示信息
     */
    public String oneMore(){
        List list = this.orderManagerApp.listGoodsBySn(sn);
        this.json = JsonMessageUtil.getListJson(list);
        return this.JSON_MESSAGE;
    }

	/**
	 * 取消订单
	 * @param sn:订单序列号.String型，必填项，
	 * @param member_id:
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
			Member member = memberManager.get(member_id);
			if (member == null) {
				this.showErrorJson("取消订单失败：登录超时");
			} else {
				this.orderFlowManager.cancel(sn, reason);
				this.showSuccessJson("取消订单成功");
			}
		} catch (RuntimeException re) {
			this.showErrorJson(re.getMessage());
		}
		return WWAction.JSON_MESSAGE;
	}
	
	/**
	 * 确认收货
	 * @param order_id:订单id.String型，必填项
	 * 
	 * @return 返回json串
	 * result  为1表示添加成功0表示失败 ，int型
	 * message 为提示信息
	 */
	public String rogConfirm() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		try {
			String orderId = request.getParameter("order_id");
			Member member = memberManager.get(member_id);
			if(member==null){
				this.showErrorJson("登录超时，请重新登录");
			} else {
				this.orderFlowManager.rogConfirm(Integer.parseInt(orderId), member.getMember_id(), member.getUname(), member.getUname(),DateUtil.getDateline());
				this.showSuccessJson("确认收货成功");
			}
		} catch (Exception e) {
			this.showErrorJson("数据库错误");
		}
		return WWAction.JSON_MESSAGE;
	}
	/**
	 * @param sn 订单编号
	 * @return
	 */
	public String getOrderBySn(){
		try{
			Order order = this.orderManagerApp.get(sn);
			this.json = JsonMessageUtil.getObjectJson(order);
		}catch(Exception e){
			e.printStackTrace();
			this.json = JsonMessageUtil.getErrorJson("获取订单失败");
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取订单详细lsy
	 * @return
	 */
	public String getOrderItem(){
		try{
			List order = this.orderManagerApp.getStore(sn);
			this.showGridJson(order);
		}catch(Exception e){
			e.printStackTrace();
			this.json = JsonMessageUtil.getErrorJson("获取订单失败");
		}
		return this.JSON_MESSAGE;
	}
	//获取订单详细根据订单id
	public String getOrderByOrder(){
		try{
			Order order = this.orderManagerApp.get(order_id);
			this.json = JsonMessageUtil.getObjectJson(order);
		}catch(Exception e){
			e.printStackTrace();
			this.json = JsonMessageUtil.getErrorJson("获取订单失败");
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 
	 * 获取订单list
	 * @param page pageSize 
	 * @param member_id
	 * @param status keyword 非必填
	 * @return
	 */
	public String getOrderList(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		
		Integer page = Integer.parseInt(request.getParameter("page"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String status = request.getParameter("status");
		String keyword = request.getParameter("keyword");
		
		Page ordersPage = memberOrderManagerApp.pageOrders(page, pageSize,status,keyword,member_id);
		this.json = JsonMessageUtil.getObjectJson(ordersPage);
		return this.JSON_MESSAGE;
		
	}
	
	/**
	 * 
	 * 获取订单明细list
	 * @param page pageSize status keyword
	 * @return
	 */
	public String getOrderItemList(){
	
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		Integer order_id = Integer.parseInt(request.getParameter("order_id"));
		List<Map> itemList=orderManagerApp.getItemsByOrderid(order_id);
		
		this.json = JsonMessageUtil.getListJson(itemList);
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 获取订单各种状态的数量（lsy）
	 * @return
	 */
	public String orderCount(){
		
		Map map=this.memberOrderManagerApp.orderCount();
		List<Map> list=new ArrayList<Map>();
		list.add(map);
		this.showGridJson(list);
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取订单list 包含明细
	 * @param member_id
	 * @param status
	 * @param keyword
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public String getOrderAndItem(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String status = request.getParameter("status");
		String keyword = request.getParameter("keyword");
		Integer pageNo= Integer.parseInt(request.getParameter("page"));
		Integer pagesize = Integer.parseInt(request.getParameter("pageSize"));
		List<Map> list = new ArrayList<Map>();
		List<Map> orderList = this.memberOrderManagerApp.listOrder(status, keyword, member_id,pageNo,pagesize);
		for(Map m:orderList){
			Map map = new HashMap();
			List<Map> itemList = orderManagerApp.getItemsByOrderid(Integer.parseInt(m.get("order_id")+""));
			map.put("order", m);
			map.put("item", itemList);
			list.add(map);
		}
		String s = JsonMessageUtil.getListJson(list);
		this.json = s.substring(0, s.length()-1)+",\"currentPageNo\":"+pageNo+"}";
		return this.JSON_MESSAGE;
	}
	/**
	 * 确认付款，改变订单状态
	 * @param order_id
	 * @return
	 */
	public String confirmPay(){
		try{
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String orderId = request.getParameter("order_id");
			StoreOrder order=storeOrderManager.get(Integer.parseInt(orderId));
			
			if( order.getPay_status().intValue()== OrderStatus.PAY_CONFIRM ){ //如果是已经支付的，不要再支付
				this.json = JsonMessageUtil.getErrorJson("该订单已支付") ;
			}
			this.payConfirmOrder(order);
			if(order.getParent_id()==null){
				//获取子订单列表
				List<StoreOrder> cOrderList= storeOrderManager.storeOrderList(order.getOrder_id());
				for (StoreOrder storeOrder : cOrderList) {
					this.payConfirmOrder(storeOrder);
				}
			this.json = JsonMessageUtil.getSuccessJson("订单支付成功");
		}
		}catch(Exception e){
			e.printStackTrace();
			this.json = JsonMessageUtil.getErrorJson("订单状态更新错误");
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 
	 * 获取购物车商品列表 选中
	 * @return
	 */
	public String getCartGoodsListOrder(){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		List goodsList = this.cartManager.listGoodsOrder( sessionid ); //商品列表
		showGridJson(goodsList);
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 获取购物车列表通过商店（lsy）
	 * @return
	 */
	public String getCartGoodsListOrder2(){
		List<Map> storeGoodsList= new ArrayList<Map>();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		storeGoodsList=storeCartManager.storeListGoodsCheckOut(sessionid);
		this.showGridJson(storeGoodsList);
		return this.JSON_MESSAGE;
	}
	/**
	 * 订单确认收款
	 * @param order 订单
	 */
	private void payConfirmOrder(Order order){
		this.orderFlowManager.payConfirm(order.getOrder_id());
		Double needPayMoney= order.getNeed_pay_money(); //在线支付的金额
		int paymentid = orderReportManager.getPaymentLogId(order.getOrder_id());
		
		PaymentDetail paymentdetail=new PaymentDetail();
		
		paymentdetail.setAdmin_user("系统");
		paymentdetail.setPay_date(new Date().getTime());
		paymentdetail.setPay_money(needPayMoney);
		paymentdetail.setPayment_id(paymentid);
		orderReportManager.addPayMentDetail(paymentdetail);
		//修改订单状态为已付款付款
		this.daoSupport.execute("update es_payment_logs set paymoney=paymoney+? where payment_id=?",needPayMoney,paymentid);
		
		//更新订单的已付金额
		this.daoSupport.execute("update es_order set paymoney=paymoney+? where order_id=?",needPayMoney,order.getOrder_id());
	}
	/**************以下非api，不用书写文档**************/
	
	private Order createOrder(){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		HttpSession session = ServletActionContext.getRequest().getSession();
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
		Integer shippingId = StringUtil.toInt(request.getParameter("typeId"),0);
 		if(shippingId==null ) throw new RuntimeException("配送方式不能为空");
		
		Integer paymentId = StringUtil.toInt(request.getParameter("paymentId"),0);
 		if(paymentId==0 ) throw new RuntimeException("支付方式不能为空");
		
		Order order = new Order() ;
		//order.setCurrency(currency);//设置货币币种
		order.setShipping_id(shippingId); //配送方式
		order.setPayment_id(paymentId);//支付方式
		Integer addressId = StringUtil.toInt(request.getParameter("addressId"), false);
		MemberAddress address = new MemberAddress();

		address = this.createAddress();	
    
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
	 		Member member = memberManager.get(member_id);
			if (member != null) {
					address.setAddr_id(null);
					addressId= this.memberAddressManager.addAddress(address);
					
			}
			 
		}
//		}
	 	
 	 	address.setAddr_id(addressId);
 	 	
	 	order.setMemberAddress(address);
	 	order.setShipping_type(request.getParameter("ship_type"));
		order.setShip_day(request.getParameter("shipDay"));
		order.setShip_time(request.getParameter("shipTime"));
		order.setRemark(request.getParameter("remark"));
		order.setAddress_id(address.getAddr_id());//保存本订单的会员id
		String sessionid = cartManagerApp.getSessionId(member_id);
		if(sessionid==null){
			sessionid = session.getId();
		}
		return	this.orderManagerApp.add(order,sessionid,member_id,productIds);
		
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
			Response remoteResponse = remoteRequest.execute(kuaidiurl);
			String content  = remoteResponse.getContent();
			kuaidiResult = (Map)JSONObject.toBean( JSONObject.fromObject(content) ,Map.class);
			
		} catch (Exception e) {
			this.logger.error("查询货运状态", e);
		}
		return "kuaidi";
	}

	//set get


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

	public IOrderManagerApp getOrderManagerApp() {
		return orderManagerApp;
	}

	public void setOrderManagerApp(IOrderManagerApp orderManagerApp) {
		this.orderManagerApp = orderManagerApp;
	}

	public ICartManagerApp getCartManagerApp() {
		return cartManagerApp;
	}

	public void setCartManagerApp(ICartManagerApp cartManagerApp) {
		this.cartManagerApp = cartManagerApp;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public IMemberOrderManagerApp getMemberOrderManagerApp() {
		return memberOrderManagerApp;
	}

	public void setMemberOrderManagerApp(IMemberOrderManagerApp memberOrderManagerApp) {
		this.memberOrderManagerApp = memberOrderManagerApp;
	}

	public IOrderReportManager getOrderReportManager() {
		return orderReportManager;
	}

	public void setOrderReportManager(IOrderReportManager orderReportManager) {
		this.orderReportManager = orderReportManager;
	}

	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}

	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public String getProductIds() {
		return productIds;
	}

	public void setProductIds(String productIds) {
		this.productIds = productIds;
	}


	public Integer getOrder_id() {
		return order_id;
	}


	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}


	public ICartManager getCartManager() {
		return cartManager;
	}


	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}


	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}


	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}
	

	
}
