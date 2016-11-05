package com.enation.app.b2b2c.core.action.api.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.component.bonus.service.BonusSession;
import com.enation.app.shop.core.model.LargeOrder;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IOrderPrintManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 店铺订单API
 * @author LiFenlong
 *
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/store")
@Action("storeOrder")
public class StoreOrderApiAction extends WWAction{
	private IOrderManager orderManager;
	private IStoreOrderManager storeOrderManager;
	private IOrderFlowManager orderFlowManager;
	private IMemberAddressManager memberAddressManager;
	private IOrderPrintManager orderPrintManager;
	private IStoreCartManager storeCartManager;
	private IStoreMemberManager storeMemberManager;
	private ICartManager cartManager;
	private Integer orderId;
	private Integer[] order_id;
	private Integer paymentId;
	private Double payMoney;
	private Double shipmoney;
	private String remark;
	private String ship_day;
	private String ship_name;
	private String ship_tel;
	private String ship_mobile;
	private String ship_zip;
	private String storeids;
	private String shippingids;
	private Integer regionid;
	private String addr; 
	private String[] shipNos;
	private String bonusid;
	private Integer shipping_id;
	private Double pointprice;
	private LargeOrder largeOrder;
	private Integer goodsid;
	
	/**
	 * 获取订单收货地址
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
	/**
	 * 订单确认
	 * @param orderId 订单Id,Integer
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String confirm(){
		String dingdan=this.getText("storeOrder.dingdan");
		String querenSuccess=this.getText("storeOrder.querenSuccess");
		String querenFail=this.getText("storeOrder.querenFail");
		try{
			this.orderFlowManager.confirmOrder(orderId);
			Order order = this.orderManager.get(orderId);
			//this.orderFlowManager.addCodPaymentLog(order);
			this.showSuccessJson(dingdan+"[" + order.getSn()+"]"+querenSuccess);
		}catch(RuntimeException e){
			if (logger.isDebugEnabled()) {
				logger.debug(e);
			}
			this.showErrorJson(querenFail+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 订单支付
	 * @param orderId 订单Id,Integer
	 * @param member 店铺会员,StoreMember
	 * @param paymentId 结算单Id,Integer
	 * @param payMoney 付款金额,Double
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String pay(){
		String dingdan=this.getText("storeOrder.dingdan");
		String shoukuanSuccess=this.getText("storeOrder.shoukuanSuccess");
		String shoukuanFail=this.getText("storeOrder.shoukuanFail");
		String queren=this.getText("storeOrder.querenFail");
		try{
			//获取当前操作者
			StoreMember member=storeMemberManager.getStoreMember();
			Order order = this.orderManager.get(orderId);
			// 调用执行添加收款详细表
			if (orderFlowManager.pay(paymentId, orderId, payMoney,member.getUname())) {
				showSuccessJson(dingdan+"[" + order.getSn() + "]"+shoukuanSuccess);
			} else {
				showErrorJson(dingdan+"[" + order.getSn() + "]"+shoukuanFail);
			}
		}catch(RuntimeException e){
			if(logger.isDebugEnabled()){
				logger.debug(e);
			}
			showErrorJson(queren+":"+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 订单发货
	 * @param order_id 订单Id,Integer[]
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String ship(){
		String fahuo=this.getText("storeOrder.sendsuccess");
		try{
			storeOrderManager.ship(order_id[0], shipping_id, shipNos[0]);
			String is_ship= orderPrintManager.ship(order_id);
			if(is_ship.equals("true")){
				this.showSuccessJson(fahuo);
			}else{
				this.showErrorJson(is_ship);
			}
		}catch(Exception e){
			e.printStackTrace();
			this.showErrorJson(e.getMessage());
			this.logger.error("发货出错", e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 修改配送费用
	 * @param orderId 订单Id,Integer
	 * @param currshipamount 修改前价格,Double
	 * @param member 店铺会员,StoreMember
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String saveShipPrice(){
		String saveSuccess=this.getText("storeOrder.saveSuccess");
		String saveFail=this.getText("storeOrder.saveFail");
		try{
			//修改前价格
			double currshipamount=orderManager.get(this.orderId).getShipping_amount();
			double price = this.orderManager.saveShipmoney(shipmoney, this.orderId);
			//获取操作人，记录日志
			StoreMember member=storeMemberManager.getStoreMember();
		//	this.orderManager.log(this.orderId, "运费从"+currshipamount+"修改为" + price, null, member.getUname());
			this.showSuccessJson(saveSuccess);
		}catch(RuntimeException e){
			this.logger.error(e.getMessage(), e);
			this.showErrorJson(saveFail);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 修改订单金额
	 * @param orderId 订单Id,Integer
	 * @param amount 修改前价格,Double
	 * @param member 店铺会员,StoreMember
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String savePrice(){
		String changPrice=this.getText("storeOrder.ChangeDingdanSuccess");
		String changPriceFail=this.getText("storeOrder.ChangeDingdanFail");
		try {
			//修改前价格
			double amount = orderManager.get(this.orderId).getOrder_amount();
			this.orderManager.savePrice(payMoney, this.orderId);
			//获取操作人，记录日志
			StoreMember member=storeMemberManager.getStoreMember();
			//orderManager.log(this.orderId, "运费从"+amount+"修改为" + payMoney, null, member.getUname());
			this.showSuccessJson(changPrice);
		} catch (Exception e) {
			this.showErrorJson(changPriceFail);
			this.logger.error(e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 修改收货人信息
	 * @param orderId 订单Id,Integer
	 * @param member 店铺会员,StoreMember
	 * @param oldShip_day 修改前收货日期,String
	 * @param oldship_name 修改前收货人姓名,String
	 * @param oldship_tel 修改前收货人电话
	 * @param oldship_mobile 修改前收货人手机号
	 * 
	 * @param remark 订单备注,String
	 * @param ship_day 收货时间,String
	 * @param ship_name 收货人姓名,String
	 * @param ship_tel 收货人电话,String
	 * @param ship_mobile 收货人手机号,String
	 * @param ship_zip 邮政编号
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String saveConsigee(){
		String changeSuccess=this.getText("storeOrder.ChangeSuccess");
		String changeFail=this.getText("storeOrder.ChangeFail");
		try {
			Order order = this.orderManager.get(this.getOrderId());
			StoreMember member=storeMemberManager.getStoreMember();
			String oldShip_day = order.getShip_day();
			String oldship_name = order.getShip_name();
			String oldship_tel = order.getShip_tel();
			String oldship_mobile = order.getShip_mobile();
			String oldship_zip = order.getShip_zip();
			
			boolean addr = this.storeOrderManager.saveShipInfo(remark,ship_day, ship_name, ship_tel, ship_mobile, ship_zip, this.getOrderId());
			//记录日志
			/*if(ship_day !=null && !StringUtil.isEmpty(ship_day)){
				this.orderManager.log(this.getOrderId(), "收货日期从['"+oldShip_day+"']修改为['" + ship_day+"']", null, member.getUname());
			}if(ship_name !=null && !StringUtil.isEmpty(ship_name)){
				this.orderManager.log(this.getOrderId(), "收货人姓名从['"+oldship_name+"']修改为['" + ship_name+"']", null,member.getUname());
			}if(ship_tel !=null && !StringUtil.isEmpty(ship_tel)){
				this.orderManager.log(this.getOrderId(), "收货人电话从['"+oldship_tel+"']修改为['" + ship_tel+"']", null,member.getUname());
			}if(ship_mobile !=null && !StringUtil.isEmpty(ship_mobile)){
				this.orderManager.log(this.getOrderId(), "收货人手机从['"+oldship_mobile+"']修改为['" + ship_mobile+"']", null,member.getUname());
			}if(ship_zip !=null && !StringUtil.isEmpty(ship_zip)){
				this.orderManager.log(this.getOrderId(), "收货人邮编从['"+oldship_zip+"']修改为['" + ship_zip+"']", null,member.getUname());
			}*/
			this.saveAddr();
			this.showSuccessJson(changeSuccess);
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson(changeFail);
			logger.error(e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 修改配送地区
	 * @param province 省,String
	 * @param city 城市,String
	 * @param region 区,String
	 * @param Attr 详细地址,String
	 * 
	 * @param province_id 省Id,String
	 * @param city_id 城市Id,String
	 * @param region_id 区Id,String
	 * 
	 * @param oldAddr 修改前详细地址,String
	 * @param orderId 订单Id,Integer
	 * @return void
	 */
	private void saveAddr(){
		//获取地区
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String region = request.getParameter("region");
		String Attr=province+"-"+city+"-"+region;
		//获取地区Id
		String province_id = request.getParameter("province_id");
		String city_id = request.getParameter("city_id");
		String region_id = request.getParameter("region_id");
		//记录日志，获取当前操作人
		String oldAddr = this.orderManager.get(this.orderId).getShip_addr();
		StoreMember member=storeMemberManager.getStoreMember();
		this.orderManager.saveAddr(this.orderId, StringUtil.toInt(province_id,true),StringUtil.toInt(city_id,true) ,StringUtil.toInt(region_id, true),Attr);
		this.orderManager.saveAddrDetail(this.getAddr(), this.getOrderId());
		
		//this.orderManager.log(this.orderId, "收货人详细地址从['"+oldAddr+"']修改为['" + this.getAddr()+"']", null, member.getUname());
	}
	
	/**
	 * 会员地址
	 * @param address_id 地址Id,String
	 * @return MemberAddress
	 */
	private MemberAddress memberAddress(){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String address_id = request.getParameter("addressId");
		MemberAddress address = this.memberAddressManager.getAddress(Integer.valueOf(address_id));
		return address;
	}
	/**
	 * 获取订单价格信息
	 * @param storeid 店铺Id,String[]
	 * @param typeid 类型,String[]
	 * @param storeGoodsList 购物车商品列表, List<Map>
	 * @param goodsprice 商品价格,Double
	 * @param shippingprice 配送费用,Double
	 * @param totleprice 打折费用,Double
	 * @return json
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getOrderPrice(){
		
		String [] storeid = storeids.split(",");
		String [] typeid = shippingids.split(",");
		String [] bonus =  bonusid.split(",");
		typeid[0]="0";
		List<Map> storeGoodsList= new ArrayList<Map>();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		storeGoodsList=storeCartManager.storeListGoods(sessionid);
		
		List<CartItem> cartItemList = cartManager.listGoods(sessionid);
		//获取购物车中的第一件商品的信息
		CartItem cartItem=cartItemList.get(0);
		//如果使用了平台优惠券，则不能使用店铺优惠券		
		double money = BonusSession.getUseMoney(cartItem.getCurrency());
		Locale locale = (Locale) request.getSession().getAttribute("locale");
		String language = locale.getLanguage();
		String alert = "";
		if(language=="zh"){
			 alert = "只能使用一种优惠券";
		}else{
			 alert = "Вы можете использовать только один купон"; 
		}
		if(money>0){
			this.showErrorJson(alert);
			return this.JSON_MESSAGE;
		}
		Double mp=0.0d;
		mp=CurrencyUtil.div(pointprice, storeid.length);
		String storeprices="";
		String discountprice="";
		Double totle_discountprice=0.0;
		Double totleprice=0.0d;
		Double goodsprice=0.0d;
		Double shippingprice=0.0d;
		for(Map map : storeGoodsList){
			Integer store_id=  (Integer) map.get("store_id");
			List list = (List) map.get("goodslist");
			for(int i=0;i<storeid.length;i++){
				if(store_id.equals(Integer.valueOf(storeid[i]))){
					Map maps = new HashMap();
					maps.put("storeid", store_id);
					maps.put("bonusid", Integer.valueOf(bonus[i]));
					OrderPrice orderPrice =  this.storeCartManager.countPrice(list, regionid+"", Integer.valueOf(typeid[i]), false,maps);
					storeprices= storeprices+","+(orderPrice.getNeedPayMoney()-mp);
					discountprice = discountprice+","+orderPrice.getDiscountPrice();
					totle_discountprice = CurrencyUtil.add(totle_discountprice, orderPrice.getDiscountPrice());
					totleprice = CurrencyUtil.add(totleprice, orderPrice.getOrderPrice());
					shippingprice= CurrencyUtil.add(shippingprice, orderPrice.getShippingPrice());
					goodsprice = CurrencyUtil.add(goodsprice,orderPrice.getGoodsPrice());
					break;
				}
			}
		}
		storeprices = storeprices.substring(1, storeprices.length());
		discountprice = discountprice.substring(1, discountprice.length());
		Map pricemap = new HashMap();
		pricemap.put("result", 1);
		pricemap.put("storeprice", storeprices);
		pricemap.put("totleprice", totleprice);
		pricemap.put("goodsprice", goodsprice);
		pricemap.put("shippingprice", shippingprice);
		pricemap.put("discountprice", discountprice);
		pricemap.put("totle_discountprice", totle_discountprice);
		
		JSONArray jsons = JSONArray.fromObject(pricemap);
		this.json=jsons.toString().substring(1, jsons.toString().length()-1);
		
		return JSON_MESSAGE;
	}
	
	/**
	 * TODO
	 * 卖家中心修改未付款订单价格  目前只支持修改订单价格和运费，不支持修改单项商品价格
	 * @author WKZ
	 */
	public String modifyPrice(){
		Order order = new Order();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String ordersn = request.getParameter("ordersn");
		if(StringUtil.isEmpty(ordersn)) {
			this.showErrorJson("修改失败,缺少订单号,请重试");
			return this.JSON_MESSAGE;
		}
		String goods_amount = request.getParameter("goods_amount");
		if(!NumberUtils.isNumber(goods_amount)) {
			this.showErrorJson("修改失败,订单价格填写错误");
			return this.JSON_MESSAGE;
		}
		String shipping_amount = request.getParameter("shipping_amount");
		if(!NumberUtils.isNumber(goods_amount)) {
			this.showErrorJson("修改失败,运费填写错误");
			return this.JSON_MESSAGE;
		}
		try {
			order.setSn(ordersn);
			order.setGoods_amount(Double.parseDouble(goods_amount));
			order.setShipping_amount(Double.parseDouble(shipping_amount));
			this.orderManager.modifyOrderPrice(order);
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("修改失败,请重试");
			return this.JSON_MESSAGE;
		}
		this.showSuccessJson("修改成功");
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 添加一笔大宗交易
	 * @return
	 */
	public String addLargeOrder(){
		
		long request_time = DateUtil.getDateline();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		System.out.println(request.getParameter("goods_id"));
		Integer goods_num = Integer.valueOf(request.getParameter("goods_num"));
		String goods_name = request.getParameter("goods_name");
		String order_text = request.getParameter("order_text");
		String store_name = this.storeOrderManager.getStoreName(goodsid);
		largeOrder.setStore_name(store_name);
		largeOrder.setOrder_text(order_text);
		largeOrder.setGoods_name(goods_name);
		largeOrder.setGoods_id(goodsid);
		largeOrder.setGoods_num(goods_num);
		largeOrder.setRequest_time(request_time);
		largeOrder.setStatus(0);
		//this.largeOrderManager.add(largeOrder);
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String success = this.getText("storeOrder.saveSuccess");
		/*if(language=="zh"){
			success = "保存成功";
		}else{
			success = "Сохранять успешно";
		}*/
		try {
			this.storeOrderManager.addLargeOrder(largeOrder);
			this.showSuccessJson(success);
		} catch (Throwable e) {
			this.logger.error("保存大宗交易意向出错", e);
			this.showErrorJson("Failed"+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	//set get
	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}
	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
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
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	public IOrderPrintManager getOrderPrintManager() {
		return orderPrintManager;
	}
	public void setOrderPrintManager(IOrderPrintManager orderPrintManager) {
		this.orderPrintManager = orderPrintManager;
	}
	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}
	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer[] getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer[] order_id) {
		this.order_id = order_id;
	}
	public Integer getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Integer paymentId) {
		this.paymentId = paymentId;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public Double getShipmoney() {
		return shipmoney;
	}
	public void setShipmoney(Double shipmoney) {
		this.shipmoney = shipmoney;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getShip_day() {
		return ship_day;
	}
	public void setShip_day(String ship_day) {
		this.ship_day = ship_day;
	}
	public String getShip_name() {
		return ship_name;
	}
	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}
	public String getShip_tel() {
		return ship_tel;
	}
	public void setShip_tel(String ship_tel) {
		this.ship_tel = ship_tel;
	}
	public String getShip_mobile() {
		return ship_mobile;
	}
	public void setShip_mobile(String ship_mobile) {
		this.ship_mobile = ship_mobile;
	}
	public String getShip_zip() {
		return ship_zip;
	}
	public void setShip_zip(String ship_zip) {
		this.ship_zip = ship_zip;
	}
	public String getStoreids() {
		return storeids;
	}
	public void setStoreids(String storeids) {
		this.storeids = storeids;
	}
	public String getShippingids() {
		return shippingids;
	}
	public void setShippingids(String shippingids) {
		this.shippingids = shippingids;
	}
	public Integer getRegionid() {
		return regionid;
	}
	public void setRegionid(Integer regionid) {
		this.regionid = regionid;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String[] getShipNos() {
		return shipNos;
	}
	public void setShipNos(String[] shipNos) {
		this.shipNos = shipNos;
	}
	public String getBonusid() {
		return bonusid;
	}
	public void setBonusid(String bonusid) {
		this.bonusid = bonusid;
	}
	public Integer getShipping_id() {
		return shipping_id;
	}
	public void setShipping_id(Integer shipping_id) {
		this.shipping_id = shipping_id;
	}
	public ICartManager getCartManager() {
		return cartManager;
	}
	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}
	public Double getPointprice() {
		return pointprice;
	}
	public void setPointprice(Double pointprice) {
		this.pointprice = pointprice;
	}
	public LargeOrder getLargeOrder() {
		return largeOrder;
	}
	public void setLargeOrder(LargeOrder largeOrder) {
		this.largeOrder = largeOrder;
	}
	public Integer getGoodsid() {
		return goodsid;
	}
	public void setGoodsid(Integer goodsid) {
		this.goodsid = goodsid;
	}
}
