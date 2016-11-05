package com.enation.app.b2b2c.component.plugin.order;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.StoreBonus;
import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.service.IStoreBonusManager;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.component.bonus.model.MemberBonus;
import com.enation.app.shop.component.bonus.service.BonusSession;
import com.enation.app.shop.component.receipt.Receipt;
import com.enation.app.shop.component.receipt.service.IReceiptManager;
import com.enation.app.shop.core.model.AllActivity;
import com.enation.app.shop.core.model.CheckBonus;
import com.enation.app.shop.core.model.CheckMemberLogin;
import com.enation.app.shop.core.model.FreezePoint;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.OrderLog;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.plugin.cart.ICountPriceEvent;
import com.enation.app.shop.core.plugin.order.IAfterOrderCreateEvent;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IMemberPointManger;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.impl.AllActivityManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.DateUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.StringUtil;
/**
 * @author LiFenLong
 *b2b2c订单插件
 */
@Component
public class B2b2cOrderPlugin extends AutoRegisterPlugin implements IAfterOrderCreateEvent,ICountPriceEvent{
	private IDaoSupport daoSupport;
	private IStoreCartManager storeCartManager;
	private ICartManager cartManager;
	private IDlyTypeManager dlyTypeManager;
	private IOrderFlowManager orderFlowManager;
	private IStoreMemberManager storeMemberManager;
	private IReceiptManager receiptManager;
	private IStoreBonusManager storeBonusManager;
	private  final String discount_key ="bonusdiscount";
	private IMemberManager memberManager;
	private IMemberPointManger memberPointManger;
	private AllActivityManager allActivityManager;
	@Override
	
	
	/***
	 * 主订单创建完成后添加子订单信息
	 */
	public void onAfterOrderCreate(Order order, List<CartItem> itemList,String sessionid) {

		StoreMember member = storeMemberManager.getStoreMember();
		//获取购物车列表
		List<Map> storeGoodsList=storeCartManager.storeListGoodsCheckOut(sessionid);
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		StoreOrder store_order = new StoreOrder();
		Double goodsPrice=0.0; //商品价格，经过优惠过的
		Double orderprice=0.0;//订单总价，优惠过的，包含商品价格和配置费用
		Double shippingPrice=0.0; //配送费用
		Double needPayMoney=0.0; //需要支付的金额
		try {
			BeanUtils.copyProperties(store_order,order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int num=0;
		//获取配送方式数组
		String[] shippingIds=request.getParameterValues("shippingId");
		//获取优惠劵数组
		String[] bonusid=request.getParameterValues("bonusid");
	    List<CartItem> cartItemList = cartManager.listGoods(sessionid);
		CartItem cartItem=cartItemList.get(0);
		MemberBonus memberbonus = BonusSession.getOneBonus(cartItem.getCurrency());		
		for (Map map : storeGoodsList) {
			int store_id = Integer.parseInt(map.get("store_id").toString());
			Integer datacount=0;
			if(memberbonus.getSendplat()!=null){
				 datacount=cartManager.getCartCountForGoods(sessionid, store_id,memberbonus.getSendplat());
			}
			Integer shippingId = Integer.parseInt(shippingIds[num]);
			store_order.setStore_id(store_id);
			store_order.setCurrency(order.getCurrency());
			store_order.setParent_id(order.getOrder_id());
			//获取子订单的购物车货品列表以及计算子订单的订单价格
			List<StoreCartItem> list=(List) map.get("goodslist");
			
			Map storemap = new HashMap();
			storemap.put("bonusid", Integer.parseInt(bonusid[num]));
			storemap.put("storeid", store_id);
			
			/*************************计算价格，重量，积分*************/
			OrderPrice orderPrice=storeCartManager.countPrice(list,order.getRegionid()+"",shippingId,false,storemap);
			
			store_order.setGoods_amount( orderPrice.getGoodsPrice());
			store_order.setWeight(orderPrice.getWeight());		
			
			store_order.setDiscount(orderPrice.getDiscountPrice());
			store_order.setOrder_amount(orderPrice.getOrderPrice());
			store_order.setProtect_price(orderPrice.getProtectPrice());
			store_order.setShipping_amount(orderPrice.getShippingPrice());
			store_order.setGainedpoint(orderPrice.getPoint());
			store_order.setNeed_pay_money(orderPrice.getNeedPayMoney());
			
			shippingPrice+=orderPrice.getShippingPrice();
			needPayMoney+=orderPrice.getNeedPayMoney();
			orderprice+=orderPrice.getOrderPrice();
			goodsPrice+=orderPrice.getGoodsPrice();
			
			String shipName=null;
			if(shippingId!=null && shippingId!=0){
				shipName= dlyTypeManager.getDlyTypeById(shippingId).getName();
			}else{
				shipName = "";
			}
			store_order.setShipping_type(shipName);
			
			//添加子订单
			store_order.setSn(num==0?store_order.getSn()+num:store_order.getSn().substring(0, store_order.getSn().length()-1)+num);
			if(memberbonus.getSendplat()!=null){
				if(list.size()==datacount){
					store_order.setIs_bonus(1);
				}else{
					store_order.setIs_bonus(0);
				}
			}else{
				store_order.setIs_bonus(0);
			}	
			daoSupport.insert("es_order", store_order);
			//添加子订单商品
			store_order.setOrder_id(this.daoSupport.getLastId("es_order"));
			if(store_order.getIs_bonus()==1){
				CheckBonus checkBonus=new CheckBonus();
				Member member1 = UserConext.getCurrentMember();
			    String addressid=request.getRemoteAddr();
			    Long nowtime=com.enation.framework.util.DateUtil.getDateline();
			    checkBonus.setAddress_id(addressid);
			    checkBonus.setMember_id(member1.getMember_id());
			    checkBonus.setOrder_id(store_order.getOrder_id());
			    checkBonus.setUsetime(nowtime);
			    checkBonus.setBonus_money(memberbonus.getBonus_money());
			    checkBonus.setBonus_id(memberbonus.getBonus_id());
			    checkBonus.setBonus_name(memberbonus.getType_name());
			    checkBonus.setIs_bonus(1);
			    checkBonus.setMin_bonus_money(memberbonus.getMin_goods_amount());
			    checkBonus.setReason("优惠券已经使用");
			    checkBonus.setIs_cancle(0);
			    checkBonus.setPlatform(memberbonus.getSendplat());
			    daoSupport.insert("es_check_bonus",checkBonus);
			}
			this.saveGoodsItem(list,store_order.getOrder_id(),store_order.getCurrency());
			this.onAfterOrderCreatepoint( order,   itemList,  sessionid);
			/************ 写入订单日志 ************************/
			OrderLog log = new OrderLog();
			log.setMessage("订单创建");
			log.setOp_name(member.getName());
			log.setOrder_id(store_order.getOrder_id());
			this.addLog(log);
			//如果不为货到库款则自动确认订单
			if(!store_order.getIsCod()){
				orderFlowManager.confirmOrder(store_order.getOrder_id());
			}
			//保存发票信息
			saveReceipt(store_order);
			
			//修改为已使用,并增加已使用数量
			//新增used_time,order_id,order_sn,member_name
			//long used_time = new Date().getTime()/1000;
			this.storeBonusManager.setBonusUsed(Integer.parseInt(bonusid[num]),member.getMember_id());
			
			num+=1;
		}
		this.updateOrderPrice(order.getOrder_id(), goodsPrice, orderprice, shippingPrice, needPayMoney);
	}
	/**
	 * 修改订单价格
	 */
	private void updateOrderPrice(Integer order_id,Double goodsPrice,Double orderprice,Double shippingPrice,Double needPayMoney){
		Map map=new HashMap();
		map.put("goods_amount", goodsPrice);
		map.put("order_amount", orderprice);
		map.put("shipping_amount", shippingPrice);
		map.put("need_pay_money", needPayMoney);
		this.daoSupport.update("es_order", map, "order_id="+order_id);
	}
	 public void onAfterOrderCreatepoint(Order order,List<CartItem>   itemList, String sessionid) {
			
			//如果是会员购买发放积分
			if(order.getMember_id()!=null){
				Member member  = memberManager.get(order.getMember_id());
				/**
				 * --------------------------------------
				 * 增加会员积分--商品价格*倍数(倍数在设置处指定)
				 * --------------------------------------
				 */
				
					Double point=0.0;
					Double mp=0.0;
					if(order.getCurrency().equals("CNY")){
						//System.out.println(memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num"));
						 point = memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num");
						//System.out.println(point+"kkkkkkkkkkkk");
						 mp =  memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num_mp");
						//System.out.println(memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_numru")+"俄文");
						//System.out.println(memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num_mpru")+"俄文");
						//System.out.println(mp+"lllllllllll");
					}else if (order.getCurrency().equals("RUB")) {
				
	                	//System.out.println(memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_numru"));
	    				 point = memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_numru");
	    				//System.out.println(point+"kkkkkkkkkkkk");
	    				 mp =  memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num_mpru");
	    				//System.out.println(mp+"lllllllllll");
					}
						
					/*//System.out.println(memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num"));
					Double point = memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num");
					//System.out.println(point+"kkkkkkkkkkkk");
					Double mp =  memberPointManger.getItemPoint2(IMemberPointManger.TYPE_BUYGOODS+"_num_mp");
					//System.out.println(mp+"lllllllllll");*/
					List<Order>  orderlist   = memberPointManger.getItemorder(order.getOrder_id());
					for (Order  order3:    orderlist) {
					int	point2 =Integer.parseInt(String.valueOf(Math.round(order3.getGoods_amount().intValue() * point)));
						//System.out.println(Math.round(order3.getGoods_amount().intValue() * point));
					int	mp2 = Integer.parseInt(String.valueOf(Math.round(order3.getGoods_amount().intValue() * mp)));
					//System.out.println(Math.round(order3.getGoods_amount().intValue() * mp));
						//System.out.println("point " +point2);
						//System.out.println("mp"+mp2);
						FreezePoint freezePoint= new FreezePoint();
						freezePoint.setMemberid(order3.getMember_id());
						freezePoint.setPoint(point2);
						freezePoint.setMp(mp2);
						freezePoint.setType(IMemberPointManger.TYPE_BUYGOODS);
						freezePoint.setOrderid(order3.getOrder_id());
						//System.out.println(order3.getOrder_id());
						if (point2==0&&mp2==0) {
							
						}else {
							String   sqlString="select count(*) from  es_freeze_point where orderid=? and type=?";
							
						Integer  pInteger=	this.daoSupport.queryForInt(sqlString, order3.getOrder_id(),"buygoods");
							if(pInteger<1){
							this.memberPointManger.addFreezePoint(freezePoint,member.getName());
							}
						}
							
					}
			}}
	/***
	 * 保存订单项
	 * @param itemList
	 * @param order_id
	 */
	private void saveGoodsItem(List<StoreCartItem> itemList, Integer order_id,String currency) {
		  HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		for (int i = 0; i < itemList.size(); i++) {

			OrderItem orderItem = new OrderItem();

			CartItem cartItem = (CartItem) itemList.get(i);
			orderItem.setPrice(cartItem.getCoupPrice());
			orderItem.setName(cartItem.getName());
			orderItem.setNum(cartItem.getNum());
			orderItem.setSendprice(cartItem.getSendprice());
			orderItem.setFreight_id(cartItem.getFreight_id());
			orderItem.setGoods_id(cartItem.getGoods_id());
			orderItem.setShip_num(0);
			orderItem.setProduct_id(cartItem.getProduct_id());
			orderItem.setOrder_id(order_id);
			if(cartItem.getPoint()!=null){
				orderItem.setGainedpoint(cartItem.getPoint());
			}else{
				orderItem.setGainedpoint(0);
			}
			orderItem.setAddon(cartItem.getAddon());
			 if(allActivityManager.checkGoodsIsExists(cartItem.getGoods_id()).size()>0){
         	    orderItem.setIs_skill(1);
	         }else{
	        	 orderItem.setIs_skill(0);
	         }
			//3.0新增的三个字段
			orderItem.setSn(cartItem.getSn());
			orderItem.setImage(cartItem.getImage_default());
			orderItem.setCat_id(cartItem.getCatid());
			
			orderItem.setCurrency(cartItem.getCurrency());//设置订单项货币币种
			
			orderItem.setUnit(cartItem.getUnit());
			this.daoSupport.insert("es_order_items", orderItem);
			Boolean flag=false;
			if( allActivityManager.checkGoodsIsExists(cartItem.getGoods_id()).size()>0){
            	    flag=true;
	         }
			if(flag){
				addCheckLoginMember(cartItem.getGoods_id(),request,order_id);
			}
		}
	}
	/**
	 * 添加订单日志
	 * 
	 * @param log
	 */
	private void addLog(OrderLog log) {
		log.setOp_time(com.enation.framework.util.DateUtil.getDateline());
		this.daoSupport.insert("es_order_log", log);
	}
	/**
	 * 保存发票
	 * @param order 子订单
	 */
	private void saveReceipt(StoreOrder order){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		
		String havereceipt = request.getParameter("receipt");
		if(StringUtil.isEmpty(havereceipt)) return ;
		
		
		//保存发票信息
		String appi=request.getParameter("receiptType");
		int invoice_app=0;
		if(!StringUtil.isEmpty(appi)){
			invoice_app = Integer.parseInt(appi);
		}
		if(invoice_app==1){
			String invoice_title = "个人";
			String invoice_content = request.getParameter("receiptContent");
			if(!StringUtil.isEmpty(invoice_content)){
				Receipt receipt= new Receipt();
				receipt.setOrder_id(order.getOrder_id());
				receipt.setTitle(invoice_title);
				receipt.setContent(invoice_content);
				this.receiptManager.add(receipt);
			}
		}else if(invoice_app==2){
			//单位
			String invoice_title = request.getParameter("receiptTitle");
			String invoice_content = request.getParameter("receiptContent");
			if(!StringUtil.isEmpty(invoice_title) && !StringUtil.isEmpty(invoice_content)){
				Receipt invoice= new Receipt();
				invoice.setOrder_id(order.getOrder_id());
				invoice.setTitle(invoice_title);
				invoice.setContent(invoice_content);
				this.receiptManager.add(invoice);
			}
		}
	}
	
	@Override
	public OrderPrice countPrice(Integer store_id,OrderPrice orderprice,Map map) {
		if(map!=null && ((Integer)map.get("bonusid")).intValue()!=0){
			Integer bonusid = (Integer) map.get("bonusid");
			
			StoreBonus bonus = this.storeBonusManager.get(bonusid);
			Map<String,Object> disItems  = orderprice.getDiscountItem();
			//加入优惠券国际化的判断
			HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
			String sessionid = request.getSession().getId();
			List<CartItem> cartItemList = cartManager.listGoodsOrder(sessionid);
			//获取购物车中的第一件商品的信息
			CartItem cartItem=cartItemList.get(0);
			//订单优惠项			
			double moneyCount = 0.0;
			if(cartItem.getCurrency().equals("RUB")){
				moneyCount = bonus.getType_money_ru();//优惠券卢布的金额
			}else if (cartItem.getCurrency().equals("CNY")) {
				moneyCount = bonus.getType_money(); //优惠券人民币金额
			}
			
			disItems.put(discount_key, moneyCount);//记录红包优惠金额
			
			//处理订单金额
			double order_price =orderprice.getOrderPrice();
			order_price= CurrencyUtil.sub(order_price, moneyCount);
			
			orderprice.setDiscountPrice(CurrencyUtil.add(orderprice.getDiscountPrice(),moneyCount));//优惠总金额
			if(order_price<0){
				orderprice.setOrderPrice(0.0);
				orderprice.setNeedPayMoney(0.0);
			}else{
				orderprice.setOrderPrice(orderprice.getOrderPrice());
				orderprice.setNeedPayMoney(order_price);
			}
		}
		
		return orderprice;
	}
	
	public void addCheckLoginMember(Integer goods_id, HttpServletRequest request,Integer order_id) {
		CheckMemberLogin memberLogin=new CheckMemberLogin();
		Member member = UserConext.getCurrentMember();
	    String addressid=request.getRemoteAddr();
	    Long nowtime=com.enation.framework.util.DateUtil.getDateline();
	    List<Map> list=(List<Map>)allActivityManager.checkGoodsIsExists(goods_id);
	    Integer active_id=0;
	    for (Map map : list) {
	    	 active_id=(Integer)map.get("allactivity_id");
		}
	    memberLogin.setAddress_id(addressid);
	    memberLogin.setOrder_id(order_id);
	    memberLogin.setGoods_id(goods_id);
	    memberLogin.setActive_id(active_id);
	    AllActivity allActivity=this.allActivityManager.getForId(active_id);
	    memberLogin.setActive_name(allActivity.getName());
	    Map<String,Object> map=new HashMap<String, Object>();
	    map.put("active_start_time",com.enation.framework.util.DateUtil.toString(allActivity.getStart_time(),"yyyy-MM-dd hh:mm:ss"));
	    map.put("active_end_time",com.enation.framework.util.DateUtil.toString(allActivity.getEnd_time(),"yyyy-MM-dd hh:mm:ss"));
	    map.put("buy_now_time",com.enation.framework.util.DateUtil.toString(com.enation.framework.util.DateUtil.getDateline(),"yyyy-MM-dd hh:mm:ss"));
	    String json_time=JSONArray.fromObject(map).toString();
	    memberLogin.setBetween_time(json_time);
	    memberLogin.setReason("订单已生效");
	    memberLogin.setNowtime(nowtime);
	    memberLogin.setMember_id(member.getMember_id());
	    memberLogin.setIs_order(0);
	    allActivityManager.addMemberCheckLogin(memberLogin);
	    
	}
	//set get 
	
	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}
	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}
	public ICartManager getCartManager() {
		return cartManager;
	}
	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}
	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}
	public IOrderFlowManager getOrderFlowManager() {
		return orderFlowManager;
	}
	public void setOrderFlowManager(IOrderFlowManager orderFlowManager) {
		this.orderFlowManager = orderFlowManager;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public IReceiptManager getReceiptManager() {
		return receiptManager;
	}
	public void setReceiptManager(IReceiptManager receiptManager) {
		this.receiptManager = receiptManager;
	}
	public IStoreBonusManager getStoreBonusManager() {
		return storeBonusManager;
	}
	public void setStoreBonusManager(IStoreBonusManager storeBonusManager) {
		this.storeBonusManager = storeBonusManager;
	}
	public IMemberManager getMemberManager() {
		return memberManager;
	}
	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
	public IMemberPointManger getMemberPointManger() {
		return memberPointManger;
	}
	public void setMemberPointManger(IMemberPointManger memberPointManger) {
		this.memberPointManger = memberPointManger;
	}
	public AllActivityManager getAllActivityManager() {
		return allActivityManager;
	}
	public void setAllActivityManager(AllActivityManager allActivityManager) {
		this.allActivityManager = allActivityManager;
	}
	
}
