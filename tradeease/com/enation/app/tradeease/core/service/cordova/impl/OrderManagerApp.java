package com.enation.app.tradeease.core.service.cordova.impl;

import java.io.File;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.sf.json.JSONArray;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.service.warehouse.Impl.Warehouse;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.base.core.service.auth.IPermissionManager;
import com.enation.app.base.core.service.auth.IRoleManager;
import com.enation.app.base.core.service.auth.impl.PermissionConfig;
import com.enation.app.shop.core.model.Delivery;
import com.enation.app.shop.core.model.DepotUser;
import com.enation.app.shop.core.model.DlyType;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.OrderLog;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.model.Promotion;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IDepotManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IOrderAllocationManager;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.app.tradeease.core.service.cordova.IOrderManagerApp;
import com.enation.eop.SystemSetting;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.database.DoubleMapper;
import com.enation.framework.database.Page;
import com.enation.framework.database.StringMapper;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.ExcelUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;

/**
 * 订单管理
 * 
 * @author kingapex 2010-4-6上午11:16:01
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class OrderManagerApp extends BaseSupport implements IOrderManagerApp {

	private ICartManager cartManager;
	private IDlyTypeManager dlyTypeManager;
	private IPaymentManager paymentManager;
	private IMemberManager memberManager;
	
	private IPromotionManager promotionManager;
	private OrderPluginBundle orderPluginBundle;
	private IPermissionManager permissionManager;
	private IAdminUserManager adminUserManager;
	private IRoleManager roleManager;
	private IGoodsManager goodsManager;
	private IOrderAllocationManager orderAllocationManager;
	private IDepotManager depotManager;

	public IOrderAllocationManager getOrderAllocationManager() {
		return orderAllocationManager;
	}

	public void setOrderAllocationManager(
			IOrderAllocationManager orderAllocationManager) {
		this.orderAllocationManager = orderAllocationManager;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IDepotManager getDepotManager() {
		return depotManager;
	}

	public void setDepotManager(IDepotManager depotManager) {
		this.depotManager = depotManager;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void savePrice(double price, int orderid) {
		Order order = this.get(orderid);
		double amount = order.getOrder_amount();
		// double discount= amount-price;
		double discount = CurrencyUtil.sub(amount, price);
		this.baseDaoSupport
				.execute(
						"update order set order_amount=?,need_pay_money=? where order_id=?",
						price, price, orderid);
		// 修改收款单价格
		String sql = "update es_payment_logs set money=? where order_id=?";
		this.daoSupport.execute(sql, price, orderid);

		this.baseDaoSupport.execute(
				"update order set discount=discount+? where order_id=?",
				discount, orderid);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public double saveShipmoney(double shipmoney, int orderid) {
		Order order = this.get(orderid);
		double currshipamount = order.getShipping_amount();
		// double discount= amount-price;
		double shortship = CurrencyUtil.sub(shipmoney, currshipamount);
		double discount = CurrencyUtil.sub(currshipamount, shipmoney);
		// 2014-9-18 配送费用修改 @author LiFenLong
		this.baseDaoSupport
				.execute(
						"update order set order_amount=order_amount+?,need_pay_money=need_pay_money+?,shipping_amount=?,discount=discount+? where order_id=?",
						shortship, shortship, shipmoney, discount, orderid);
		// 2014-9-12 LiFenLong 修改配送金额同时修改收款单
		this.daoSupport.execute(
				"update es_payment_logs set money=money+? where order_id=?",
				shortship, orderid);
		return this.get(orderid).getOrder_amount();
	}

	/**
	 * 记录订单操作日志
	 * 
	 * @param order_id
	 * @param message
	 * @param op_id
	 * @param op_name
	 */
	public void log(Integer order_id, String message, Integer op_id,
			String op_name) {
		OrderLog orderLog = new OrderLog();
		orderLog.setMessage(message);
		orderLog.setOp_id(op_id);
		orderLog.setOp_name(op_name);
		orderLog.setOp_time(com.enation.framework.util.DateUtil.getDateline());
		orderLog.setOrder_id(order_id);
		this.baseDaoSupport.insert("order_log", orderLog);
	}
	
	/*
	 * 根据sn获取product_id,store_id,num ,e.address_id,e.payment_id,e.shipping_id,i.product_id 
	 * 
	 */
    public List listGoodsBySn(String sn){
        
        String sql = "select i.goods_id,i.num,e.store_id,e.address_id,e.payment_id,e.shipping_id,i.product_id  from es_order as e LEFT JOIN es_order_items as i ON e.order_id=i.order_id where e.sn = ?";
        List list= this.baseDaoSupport.queryForList(sql, sn);
        return list;
                 
    }

	public int getcount(Integer Member_id){
		String sql ="select count(*) from es_order WHERE  member_id=?";
		

		return   this.baseDaoSupport.queryForInt(sql, Member_id);
		
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Order add(Order order, String sessionid,Integer member_id,String productIds) {
		String opname = "游客";

		if (order == null)
			throw new RuntimeException("error: order is null");

		/************************** 用户信息 ****************************/
		Member member = memberManager.get(member_id);
		// 非匿名购买
		if (member != null) {
			order.setMember_id(member.getMember_id());
			opname = member.getUname();
		}

		/************************** 计算价格、重量、积分 ****************************/
		List<CartItem> cartItemList = cartManager.listGoods(sessionid);//ok
		OrderPrice orderPrice = cartManager.countPrice(cartItemList,
				order.getShipping_id(), "" + order.getRegionid());
		CartItem cartItem=cartItemList.get(0);//获取购物车中的第一件商品的信息
		order.setGoods_amount(orderPrice.getGoodsPrice());
		order.setWeight(orderPrice.getWeight());
		order.setCurrency(cartItem.getCurrency());//添加币种（从购物车中获取）
		order.setDiscount(orderPrice.getDiscountPrice());
		order.setOrder_amount(orderPrice.getOrderPrice());
		order.setProtect_price(orderPrice.getProtectPrice());
		order.setShipping_amount(orderPrice.getShippingPrice());
		order.setGainedpoint(orderPrice.getPoint());
		// 2014年0417新增订单还需要支付多少钱的字段-LiFenLong
		order.setNeed_pay_money(orderPrice.getNeedPayMoney());
		// 配送方式名称
		DlyType dlyType = new DlyType();
		if (dlyType != null && order.getShipping_id() != 0) {
			dlyType = dlyTypeManager.getDlyTypeById(order.getShipping_id());
		} else {
			dlyType.setName("");
		}
		//order.setShipping_type(dlyType.getName());

		/************ 支付方式价格及名称 ************************/
		PayCfg payCfg = this.paymentManager.get(order.getPayment_id());
		order.setPaymoney(this.paymentManager.countPayPrice(order.getOrder_id()));
		order.setPayment_name(payCfg.getName());

		order.setPayment_type(payCfg.getType());

		//Integer goodId = cartItemList.get(0).getGoods_id();
		/************ 创建订单 ************************/
		order.setCreate_time(com.enation.framework.util.DateUtil.getDateline());
//			order.setSn(this.createSn());
		order.setStatus(OrderStatus.ORDER_NOT_CONFIRM);
		order.setDisabled(0);
		order.setPay_status(OrderStatus.PAY_NO);
		order.setShip_status(OrderStatus.SHIP_NO);
		order.setOrderStatus("订单已生效");

		// 给订单添加仓库 ------仓库为默认仓库
//		Integer depotId = this.baseDaoSupport
//				.queryForInt("select id from depot where choose=1");
		//添加立博仓库
//		Integer depotId = this.baseDaoSupport
//				.queryForInt("select h.warehouse_id from es_warehouse h left join es_warehouse_type t on h.warehouse_type_id = t.warehouse_type_id where t.warehouse_type_name = '线上发货仓库' ");
//		order.setDepotid(depotId);
//		线上发货仓可以设置多个
		/************ 写入订单货物列表 ************************/
		List<CartItem> itemList = this.cartManager.listGoods(sessionid);//ok
		order.setSn(this.createSn(this.getstoreId(itemList.get(0).getGoods_id())));
		this.orderPluginBundle.onBeforeCreate(order, itemList, sessionid);//...ok
		this.baseDaoSupport.insert("order", order);

		if (itemList.isEmpty())
			throw new RuntimeException("创建订单失败，购物车为空");

		Integer orderId = this.baseDaoSupport.getLastId("order");

		order.setOrder_id(orderId);

		this.saveGoodsItem(itemList, order);

		/**
		 * 应用订单优惠，送出优惠劵及赠品，并记录订单优惠方案
		 */
		if (member != null) {
			this.promotionManager.applyOrderPmt(orderId,
					orderPrice.getOrderPrice(), member.getLv_id());
			List<Promotion> pmtList = promotionManager.list(
					orderPrice.getOrderPrice(), member.getLv_id());
			for (Promotion pmt : pmtList) {
				String sql = "insert into order_pmt(pmt_id,order_id,pmt_describe)values(?,?,?)";
				this.baseDaoSupport.execute(sql, pmt.getPmt_id(), orderId,
						pmt.getPmt_describe());
			}

		}

		/************ 写入订单日志 ************************/
		OrderLog log = new OrderLog();
		log.setMessage("订单创建");
		log.setOp_name(opname);
		log.setOrder_id(orderId);
		this.addLog(log);
		order.setOrder_id(orderId);
		order.setOrderprice(orderPrice);
		try {
			this.orderPluginBundle.onAfterCreate(order, itemList, sessionid);//ok
		} catch (Exception e) {
			//System.out.println(e);
		}

		// 因为在orderFlowManager中已经注入了orderManager，不能在这里直接注入
		// 将来更好的办法是将订单创建移到orderFlowManager中
		// 下单则自动改为已确认
		IOrderFlowManager flowManager = SpringContextHolder
				.getBean("orderFlowManager");
		flowManager.confirmOrder(orderId);
		cartManager.clean(sessionid);
		// HttpCacheManager.sessionChange();

		return order;
	}

	/**
	 * 添加订单日志
	 * 
	 * @param log
	 */
	private void addLog(OrderLog log) {
		log.setOp_time(com.enation.framework.util.DateUtil.getDateline());
		this.baseDaoSupport.insert("order_log", log);
	}

	/**
	 * 保存商品订单项
	 * 
	 * @param itemList
	 * @param order_id
	 */
	private void saveGoodsItem(List<CartItem> itemList, Order order) {

		List<OrderItem> orderItemList = new ArrayList<OrderItem>();

		Integer order_id = order.getOrder_id();
		for (int i = 0; i < itemList.size(); i++) {

			OrderItem orderItem = new OrderItem();

			CartItem cartItem = (CartItem) itemList.get(i);
			orderItem.setPrice(cartItem.getCoupPrice());
			orderItem.setName(cartItem.getName());
			orderItem.setNum(cartItem.getNum());

			orderItem.setGoods_id(cartItem.getGoods_id());
			orderItem.setShip_num(0);
			orderItem.setProduct_id(cartItem.getProduct_id());
			orderItem.setOrder_id(order_id);
			orderItem.setGainedpoint(cartItem.getPoint());
			orderItem.setAddon(cartItem.getAddon());

			// 3.0新增的三个字段
			orderItem.setSn(cartItem.getSn());
			orderItem.setImage(cartItem.getImage_default());
			orderItem.setCat_id(cartItem.getCatid());

			orderItem.setUnit(cartItem.getUnit());
			
			orderItem.setCurrency(cartItem.getCurrency());//设置订单项货币币种
			
			this.baseDaoSupport.insert("order_items", orderItem);
			int itemid = this.baseDaoSupport.getLastId("order_items");
			orderItem.setItem_id(itemid);
			orderItemList.add(orderItem);
			this.orderPluginBundle.onItemSave(order, orderItem);
		}

		String itemsJson = JSONArray.fromObject(orderItemList).toString();
		this.daoSupport.execute(
				"update es_order set items_json=? where order_id=?", itemsJson,
				order_id);

	}

	/**
	 * 保存赠品项
	 * 
	 * @param itemList
	 * @param orderid
	 * @throws IllegalStateException
	 *             会员尚未登录,不能兑换赠品!
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	private void saveGiftItem(List<CartItem> itemList, Integer orderid,Integer member_id) {
		Member member = memberManager.get(member_id);
		if (member == null) {
			throw new IllegalStateException("会员尚未登录,不能兑换赠品!");
		}

		int point = 0;
		for (CartItem item : itemList) {
			point += item.getSubtotal().intValue();
			this.baseDaoSupport
					.execute(
							"insert into order_gift(order_id,gift_id,gift_name,point,num,shipnum,getmethod)values(?,?,?,?,?,?,?)",
							orderid, item.getProduct_id(), item.getName(),
							item.getPoint(), item.getNum(), 0, "exchange");
		}
		if (member.getPoint().intValue() < point) {
			throw new IllegalStateException("会员积分不足,不能兑换赠品!");
		}
		member.setPoint(member.getPoint() - point); // 更新session中的会员积分
		this.baseDaoSupport.execute(
				"update member set point=? where member_id=? ",
				member.getPoint(), member.getMember_id());

	}

	public Page listbyshipid(int pageNo, int pageSize, int status,
			int shipping_id, String sort, String order) {
		order = " ORDER BY " + sort + " " + order;
		String sql = "select * from order where disabled=0 and status="
				+ status + " and shipping_id= " + shipping_id;
		sql += " order by " + order;
		Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize,
				Order.class);
		return page;
	}
	
	

	public Page listConfirmPay(int pageNo, int pageSize, String sort,
			String order) {
		order = " order_id";
		String sql = "select * from order where disabled=0 and ((status = "
				+ OrderStatus.ORDER_SHIP
				+ " and payment_type = 'cod') or status= "
				+ OrderStatus.ORDER_PAY + "  )";
		sql += " order by " + order;
		// //System.out.println(sql);
		Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize,
				Order.class);
		return page;
	}

	public Order get(Integer orderId) {
		String sql = "select * from order where order_id=?";
		Order order = (Order) this.baseDaoSupport.queryForObject(sql,
				Order.class, orderId);
		return order;
	}

	public Order get(String ordersn) {
		String sql = "select * from es_order where sn='" + ordersn + "'";
		Order order = (Order) this.baseDaoSupport.queryForObject(sql,
				Order.class);
		return order;

	}
	public String getOrdersn(Integer id) {
		String sql = "select ordersn from es_sellback_list where id='" + id + "'";
		String ordersn = this.baseDaoSupport.queryForString(sql);
		return ordersn;

	}
	public List<OrderItem> listGoodsItems(Integer orderId) {

		String sql = "select * from " + this.getTableName("order_items");
		sql += " where order_id = ?";
		List<OrderItem> itemList = this.daoSupport.queryForList(sql,
				OrderItem.class, orderId);
		this.orderPluginBundle.onFilter(orderId, itemList);
		return itemList;
	}

	public List listGiftItems(Integer orderId) {
		String sql = "select * from order_gift where order_id=?";
		return this.baseDaoSupport.queryForList(sql, orderId);
	}

	/**
	 * 读取订单日志
	 */

	public List listLogs(Integer orderId) {
		String sql = "select * from order_log where order_id=?";
		return this.baseDaoSupport.queryForList(sql, orderId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void clean(Integer[] orderId) {
		String ids = StringUtil.arrayToString(orderId, ",");
		String sql = "delete from order where order_id in (" + ids + ")";
		this.baseDaoSupport.execute(sql);

		sql = "delete from order_items where order_id in (" + ids + ")";
		this.baseDaoSupport.execute(sql);

		sql = "delete from order_log where order_id in (" + ids + ")";
		this.baseDaoSupport.execute(sql);

		sql = "delete from payment_logs where order_id in (" + ids + ")";
		this.baseDaoSupport.execute(sql);

		sql = "delete from " + this.getTableName("delivery_item")
				+ " where delivery_id in (select delivery_id from "
				+ this.getTableName("delivery") + " where order_id in (" + ids
				+ "))";
		this.daoSupport.execute(sql);

		sql = "delete from delivery where order_id in (" + ids + ")";
		this.baseDaoSupport.execute(sql);

		orderAllocationManager.clean(orderId);

		/**
		 * ------------------- 激发订单的删除事件 -------------------
		 */
		this.orderPluginBundle.onDelete(orderId);

	}

	private boolean exec(Integer[] orderId, int disabled) {
		if (cheack(orderId)) {
			String ids = StringUtil.arrayToString(orderId, ",");
			String sql = "update order set disabled = ? where order_id in ("
					+ ids + ")";
			this.baseDaoSupport.execute(sql, disabled);
			return true;
		} else {
			return false;
		}
	}

	private boolean cheack(Integer[] orderId) {
		boolean i = true;
		for (int j = 0; j < orderId.length; j++) {
			if (this.baseDaoSupport.queryForInt(
					"select status from es_order where order_id=?", orderId[j]) != OrderStatus.ORDER_CANCELLATION) {
				i = false;
			}
		}
		return i;
	}

	public boolean delete(Integer[] orderId) {
		return exec(orderId, 1);

	}

	public void revert(Integer[] orderId) {
		exec(orderId, 0);

	}

	/**
	 * 创建订单号（SO1+店铺号+日期+四位随机数）
	 * @param goodId 
	 */
	public String createSn(Integer storeId) {
//		Integer storeId = this.getstoreId();
		StringBuffer sn = new StringBuffer("SO1");
		if(storeId!=null){
			sn.append( storeId );
		}
		sn.append(DateUtil.toString(DateUtil.getDateline(), "yyMMddHHmmss"));
		//StringBuffer sn = new StringBuffer(DateUtil.getDateline() + "");
		sn.append(getRandom());
		return sn.toString();
	}

	/**
	 * 获取随机数
	 * 
	 * @return
	 */
	public int getRandom() {
		Random random = new Random();
		int num = Math.abs(random.nextInt()) % 10000;
		if (num < 1000) {
			num = getRandom();
		}
		return num;
	}

	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}

	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IPaymentManager getPaymentManager() {
		return paymentManager;
	}

	public void setPaymentManager(IPaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}

	public List listOrderByMemberId(int member_id) {
		String sql = "select * from order where member_id = ? order by create_time desc";
		List list = this.baseDaoSupport.queryForList(sql, Order.class,
				member_id);
		return list;
	}

	/*
	 * 根据店铺ID获得订单列表
	 * 
	 * @see
	 * com.enation.app.shop.core.service.IOrderManager#listOrderByStoreId(int)
	 */
	public List listOrderByStoreId(int store_id) {
		String sql = "select * from order where store_id = ? order by create_time desc";
		List list = this.baseDaoSupport
				.queryForList(sql, Order.class, store_id);
		return list;
	}

	public Map mapOrderByMemberId(int memberId) {
		Integer buyTimes = this.baseDaoSupport.queryForInt(
				"select count(0) from order where member_id = ?", memberId);
		Double buyAmount = (Double) this.baseDaoSupport.queryForObject(
				"select sum(paymoney) from order where member_id = ?",
				new DoubleMapper(), memberId);
		Map map = new HashMap();
		map.put("buyTimes", buyTimes);
		map.put("buyAmount", buyAmount);
		return map;
	}

	public IPromotionManager getPromotionManager() {
		return promotionManager;
	}

	public void setPromotionManager(IPromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}

	public void edit(Order order) {
		this.baseDaoSupport.update("order", order,
				"order_id = " + order.getOrder_id());

	}

	public List<Map> listAdjItem(Integer orderid) {
		String sql = "select * from order_items where order_id=? and addon!=''";
		return this.baseDaoSupport.queryForList(sql, orderid);
	}

	/**
	 * 统计订单状态
	 */
	public Map censusState() {

		// 构造一个返回值Map，并将其初始化：各种订单状态的值皆为0
		Map<String, Integer> stateMap = new HashMap<String, Integer>(7);
		String[] states = { "cancel_ship", "cancel_pay", "pay", "ship",
				"complete", "allocation_yes" };
		for (String s : states) {
			stateMap.put(s, 0);
		}

		// 分组查询、统计订单状态
		String sql = "select count(0) num,status  from "
				+ this.getTableName("order")
				+ " where disabled = 0 group by status";
		List<Map<String, Integer>> list = this.daoSupport.queryForList(sql,
				new RowMapper() {

					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						Map<String, Integer> map = new HashMap<String, Integer>();
						map.put("status", rs.getInt("status"));
						map.put("num", rs.getInt("num"));
						return map;
					}
				});
		//
		// // 将list转为map
		for (Map<String, Integer> state : list) {
			stateMap.put(this.getStateString(state.get("status")),
					state.get("num"));
		}

		sql = "select count(0) num  from " + this.getTableName("order")
				+ " where disabled = 0  and status=0 ";
		int count = this.daoSupport.queryForInt(sql);
		stateMap.put("wait", 0);

		sql = "select count(0) num  from " + this.getTableName("order")
				+ " where disabled = 0  ";
		sql += " and ( ( payment_type!='cod' and  status="
				+ OrderStatus.ORDER_NOT_PAY + ") ";// 非货到付款的，未付款状态的可以结算
		sql += " or ( payment_id=8 and status!=" + OrderStatus.ORDER_NOT_PAY
				+ "  and  pay_status!=" + OrderStatus.PAY_CONFIRM + ")";
		sql += " or ( payment_type='cod' and  (status="
				+ OrderStatus.ORDER_SHIP + " or status="
				+ OrderStatus.ORDER_ROG + " )  ) )";// 货到付款的要发货或收货后才能结算
		count = this.daoSupport.queryForInt(sql);
		stateMap.put("not_pay", count);

		sql = "select count(0) from es_order where disabled=0  and ( ( payment_type!='cod' and payment_id!=8  and  status=2)  or ( payment_type='cod' and  status=0))";
		count = this.baseDaoSupport.queryForInt(sql);
		stateMap.put("allocation_yes", count);

		return stateMap;
	}

	/**
	 * 根据订单状态值获取状态字串，如果状态值不在范围内反回null。
	 * 
	 * @param state
	 * @return
	 */
	private String getStateString(Integer state) {
		String str = null;
		switch (state.intValue()) {
		case -2:
			str = "cancel_ship";
			break;
		case -1:
			str = "cancel_pay";
			break;
		case 1:
			str = "pay";
			break;
		case 2:
			str = "ship";
			break;
		case 4:
			str = "allocation_yes";
			break;
		case 7:
			str = "complete";
			break;
		default:
			str = null;
			break;
		}
		return str;
	}

	public OrderPluginBundle getOrderPluginBundle() {
		return orderPluginBundle;
	}

	public void setOrderPluginBundle(OrderPluginBundle orderPluginBundle) {
		this.orderPluginBundle = orderPluginBundle;
	}

	@Override
	public String export(Date start, Date end) {
		String sql = "select * from order where disabled=0 ";
		if (start != null) {
			sql += " and create_time>" + start.getTime();
		}

		if (end != null) {
			sql += "  and create_timecreate_time<" + end.getTime();
		}

		List<Order> orderList = this.baseDaoSupport.queryForList(sql,
				Order.class);

		// 使用excel导出流量报表
		ExcelUtil excelUtil = new ExcelUtil();

		// 流量报表excel模板在类包中，转为流给excelutil
		InputStream in = FileUtil
				.getResourceAsStream("com/enation/app/shop/core/service/impl/order.xls");

		excelUtil.openModal(in);
		int i = 1;
		for (Order order : orderList) {

			excelUtil.writeStringToCell(i, 0, order.getSn()); // 订单号
			excelUtil.writeStringToCell(i, 1, DateUtil.toString(
					new Date(order.getCreate_time()), "yyyy-MM-dd HH:mm:ss")); // 下单时间
			excelUtil.writeStringToCell(i, 2, order.getOrderStatus()); // 订单状态
			excelUtil.writeStringToCell(i, 3, "" + order.getOrder_amount()); // 订单总额
			excelUtil.writeStringToCell(i, 4, order.getShip_name()); // 收货人
			excelUtil.writeStringToCell(i, 5, order.getPayStatus()); // 付款状态
			excelUtil.writeStringToCell(i, 6, order.getShipStatus()); // 发货状态
			excelUtil.writeStringToCell(i, 7, order.getShipping_type()); // 配送方式
			excelUtil.writeStringToCell(i, 8, order.getPayment_name()); // 支付方式
			i++;
		}
		// String target= EopSetting.IMG_SERVER_PATH;
		// saas 版导出目录用户上下文目录access文件夹
		String filename = "/order";
		String static_server_path = SystemSetting.getStatic_server_path();
		File file = new File(static_server_path + filename);
		if (!file.exists())
			file.mkdirs();

		filename = filename + "/order"
				+ com.enation.framework.util.DateUtil.getDateline() + ".xls";
		excelUtil.writeToFile(static_server_path + filename);
		String static_server_domain = SystemSetting.getStatic_server_domain();

		return static_server_domain + filename;
	}

	@Override
	public OrderItem getItem(int itemid) {

		String sql = "select items.*,p.store as store from "
				+ this.getTableName("order_items") + " items ";
		sql += " left join " + this.getTableName("product")
				+ " p on p.product_id = items.product_id ";
		sql += " where items.item_id = ?";

		OrderItem item = (OrderItem) this.daoSupport.queryForObject(sql,
				OrderItem.class, itemid);

		return item;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}

	public IPermissionManager getPermissionManager() {
		return permissionManager;
	}

	public void setPermissionManager(IPermissionManager permissionManager) {
		this.permissionManager = permissionManager;
	}

	public IRoleManager getRoleManager() {
		return roleManager;
	}

	public void setRoleManager(IRoleManager roleManager) {
		this.roleManager = roleManager;
	}

	/**
	 * 
	 */
	public int getMemberOrderNum(int member_id, int payStatus) {
		return this.baseDaoSupport
				.queryForInt(
						"SELECT COUNT(0) FROM order WHERE member_id=? AND pay_status=?",
						member_id, payStatus);
	}

	/**
	 * by dable
	 */
	@Override
	public List<Map> getItemsByOrderid(Integer order_id) {
		String sql = "select * from order_items where order_id=?";
		return this.baseDaoSupport.queryForList(sql, order_id);
	}
	@Override
	public List<OrderItem> getItemsByid(Integer order_id) {
		String sql = "select * from order_items where order_id=?";
		List<OrderItem> list=this.baseDaoSupport.queryForList(sql,OrderItem.class ,order_id);
		return	list;
	}

	public  Delivery   getdl(Integer id  ){
		
		String sql  ="select * from es_delivery where order_id=? ";
		Delivery delivery =(Delivery) this.baseDaoSupport.queryForObject(sql,Delivery.class,id);
		return	delivery;
	}
	
	
	
	@Override
	public void refuseReturn(String orderSn) {
		this.baseDaoSupport.execute("update order set state = -5 where sn = ?",
				orderSn);
	}

	/**
	 * 更新订单价格
	 */
	@Override
	public void updateOrderPrice(double price, int orderid) {
		this.baseDaoSupport
				.execute(
						"update order set order_amount = order_amount-?,goods_amount = goods_amount- ? where order_id = ?",
						price, price, orderid);
	}

	/**
	 * 根据id查询物流公司
	 */
	@Override
	public String queryLogiNameById(Integer logi_id) {
		return (String) this.baseDaoSupport.queryForObject(
				"select name from logi_company where id=?", new StringMapper(),
				logi_id);
	}

	/**
	 * 游客订单查询
	 */
	public Page searchForGuest(int pageNo, int pageSize, String ship_name,
			String ship_tel) {
		String sql = "select * from order where ship_name=? AND (ship_mobile=? OR ship_tel=?) and member_id is null ORDER BY order_id DESC";
		Page page = this.baseDaoSupport.queryForPage(sql.toString(), pageNo,
				pageSize, Order.class, ship_name, ship_tel, ship_tel);
		return page;
	}

	public Page listByStatus(int pageNo, int pageSize, int status, int memberid) {
		String filedname = "status";
		if (status == 0) {
			// 等待付款的订单 按付款状态查询
			filedname = " status!=" + OrderStatus.ORDER_CANCELLATION
					+ " AND pay_status";
		}
		String sql = "select * from order where " + filedname
				+ "=? AND member_id=? ORDER BY order_id DESC";
		Page page = this.baseDaoSupport.queryForPage(sql.toString(), pageNo,
				pageSize, Order.class, status, memberid);
		return page;
	}

	public List<Order> listByStatus(int status, int memberid) {
		String filedname = "status";
		if (status == 0) {
			// 等待付款的订单 按付款状态查询
			filedname = " status!=" + OrderStatus.ORDER_CANCELLATION
					+ " AND pay_status";
		}
		String sql = "select * from order where " + filedname
				+ "=? AND member_id=? ORDER BY order_id DESC";

		return this.baseDaoSupport.queryForList(sql, status, memberid);

	}

	public int getMemberOrderNum(int member_id) {
		return this.baseDaoSupport.queryForInt(
				"SELECT COUNT(0) FROM order WHERE member_id=?", member_id);
	}

	@Override
	public Page search(int pageNO, int pageSize, int disabled, String sn,
			String logi_no, String uname, String ship_name, int status,
			Integer paystatus) {

		StringBuffer sql = new StringBuffer("select * from "
				+ this.getTableName("order") + " where disabled=?  ");
		if (status != -100) {
			if (status == -99) {
				/*
				 * 查询未处理订单
				 */
				sql.append(" and ((payment_type='cod' and status=0 )  or (payment_type!='cod' and status=1 )) ");
			} else
				sql.append(" and status = " + status + " ");

		}
		if (paystatus != null && paystatus != -100) {
			sql.append(" and pay_status = " + paystatus + " ");
		}

		if (!StringUtil.isEmpty(sn)) {
			sql.append(" and sn = '" + sn + "' ");
		}
		if (!StringUtil.isEmpty(uname)) {
			sql.append(" and member_id  in ( SELECT  member_id FROM "
					+ this.getTableName("member") + " where uname = '" + uname
					+ "' )  ");
		}
		if (!StringUtil.isEmpty(ship_name)) {
			sql.append(" and  ship_name = '" + ship_name + "' ");
		}
		if (!StringUtil.isEmpty(logi_no)) {
			sql.append(" and order_id in (SELECT order_id FROM "
					+ this.getTableName("delivery") + " where logi_no = '"
					+ logi_no + "') ");
		}
		sql.append(" order by create_time desc ");
		Page page = this.daoSupport.queryForPage(sql.toString(), pageNO,
				pageSize, Order.class, disabled);
		return page;

	}

	@Override
	public Page search(int pageNO, int pageSize, int disabled, String sn,
			String logi_no, String uname, String ship_name, int status) {
		return search(pageNO, pageSize, disabled, sn, logi_no, uname,
				ship_name, status, null);
	}

	public Order getNext(String next, Integer orderId, Integer status,
			int disabled, String sn, String logi_no, String uname,
			String ship_name) {
		StringBuffer sql = new StringBuffer("select * from "
				+ this.getTableName("order") + " where  1=1  ");

		StringBuffer depotsql = new StringBuffer("  ");
		AdminUser adminUser = UserConext.getCurrentAdminUser();
		if (adminUser.getFounder() != 1) { // 非超级管理员加过滤条件
			boolean isShiper = permissionManager.checkHaveAuth(PermissionConfig
					.getAuthId("depot_admin")); // 检测是否是发货员

			boolean haveOrder = this.permissionManager
					.checkHaveAuth(PermissionConfig.getAuthId("order"));// 订单管理员权限
			if (isShiper && !haveOrder) {
				DepotUser depotUser = (DepotUser) adminUser;
				int depotid = depotUser.getDepotid();
				depotsql.append(" and depotid=" + depotid + "  ");
			}
		}

		StringBuilder sbsql = new StringBuilder("  ");
		if (status != null && status != -100) {
			sbsql.append(" and status = " + status + " ");
		}
		// if (!StringUtil.isEmpty(sn)) {
		// sbsql.append(" and sn = '" + sn.trim() + "' ");
		// }
		if (!StringUtil.isEmpty(uname) && !uname.equals("undefined")) {
			sbsql.append(" and member_id  in ( SELECT  member_id FROM "
					+ this.getTableName("member") + " where uname = '" + uname
					+ "' )  ");
		}
		if (!StringUtil.isEmpty(ship_name)) {
			sbsql.append("  and  ship_name = '" + ship_name.trim() + "'  ");
		}
		if (!StringUtil.isEmpty(logi_no) && !logi_no.equals("undefined")) {
			sbsql.append("  and order_id in (SELECT order_id FROM "
					+ this.getTableName("delivery") + " where logi_no = '"
					+ logi_no + "')  ");
		}
		if (next.equals("previous")) {
			sql.append("  and order_id IN (SELECT CASE WHEN SIGN(order_id - "
					+ orderId
					+ ") < 0 THEN MAX(order_id)  END AS order_id FROM "
					+ this.getTableName("order") + " WHERE order_id <> "
					+ orderId + depotsql.toString() + " and disabled=? "
					+ sbsql.toString() + " GROUP BY SIGN(order_id - " + orderId
					+ ") ORDER BY SIGN(order_id - " + orderId + "))   ");
			// TODO MAX 及SIGN 函数经试验均可在mysql及oracle中通过，但mssql未验证
		} else if (next.equals("next")) {
			sql.append("  and  order_id in (SELECT CASE WHEN SIGN(order_id - "
					+ orderId
					+ ") > 0 THEN MIN(order_id) END AS order_id FROM "
					+ this.getTableName("order") + " WHERE order_id <> "
					+ orderId + depotsql.toString() + " and disabled=? "
					+ sbsql.toString() + " GROUP BY SIGN(order_id - " + orderId
					+ ") ORDER BY SIGN(order_id - " + orderId + "))   ");
		} else {
			return null;
		}
		sql.append(" order by create_time desc ");
		// ////System.out.println(sql);
		Order order = (Order) this.daoSupport.queryForObject(sql.toString(),
				Order.class, disabled);
		return order;
	}

	/**
	 * 获取订单中商品的总价格
	 * 
	 * @param sessionid
	 * @return
	 */
	private double getOrderTotal(String sessionid) {
		List goodsItemList = cartManager.listGoods(sessionid);
		double orderTotal = 0d;
		if (goodsItemList != null && goodsItemList.size() > 0) {
			for (int i = 0; i < goodsItemList.size(); i++) {
				CartItem cartItem = (CartItem) goodsItemList.get(i);
				orderTotal += cartItem.getCoupPrice() * cartItem.getNum();
			}
		}
		return orderTotal;
	}

	private OrderItem getOrderItem(Integer itemid) {
		return (OrderItem) this.baseDaoSupport.queryForObject(
				"select * from order_items where item_id = ?", OrderItem.class,
				itemid);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean delItem(Integer itemid, Integer itemnum) {// 删除订单货物
		OrderItem item = this.getOrderItem(itemid);
		Order order = this.get(item.getOrder_id());
		boolean flag = false;
		int paymentid = order.getPayment_id();
		int status = order.getStatus();
		if ((paymentid == 1 || paymentid == 3 || paymentid == 4 || paymentid == 5)
				&& (status == 0 || status == 1 || status == 2 || status == 3 || status == 4)) {
			flag = true;
		}
		if ((paymentid == 2)
				&& (status == 0 || status == 9 || status == 3 || status == 4)) {
			flag = true;
		}
		if (flag) {
			try {
				if (itemnum.intValue() <= item.getNum().intValue()) {
					Goods goods = goodsManager.getGoods(item.getGoods_id());
					double order_amount = order.getOrder_amount();
					double itemprice = item.getPrice().doubleValue()
							* itemnum.intValue();
					double leftprice = CurrencyUtil
							.sub(order_amount, itemprice);
					int difpoint = (int) Math.floor(leftprice);
					Double[] dlyprice = this.dlyTypeManager
							.countPrice(
									order.getShipping_id(),
									order.getWeight()
											- (goods.getWeight().doubleValue() * itemnum
													.intValue()), leftprice,
									order.getShip_regionid().toString());
					double sumdlyprice = dlyprice[0];
					this.baseDaoSupport
							.execute(
									"update order set goods_amount = goods_amount- ?,shipping_amount = ?,order_amount =  ?,weight =  weight - ?,gainedpoint =  ? where order_id = ?",
									itemprice, sumdlyprice, leftprice,
									(goods.getWeight().doubleValue() * itemnum
											.intValue()), difpoint, order
											.getOrder_id());
					this.baseDaoSupport
							.execute(
									"update freeze_point set mp =?,point =?  where orderid = ? and type = ?",
									difpoint, difpoint, order.getOrder_id(),
									"buygoods");
					if (itemnum.intValue() == item.getNum().intValue()) {
						this.baseDaoSupport.execute(
								"delete from order_items where item_id = ?",
								itemid);
					} else {
						this.baseDaoSupport
								.execute(
										"update order_items set num = num - ? where item_id = ?",
										itemnum.intValue(), itemid);
					}

				} else {
					return false;
				}

			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		}
		return flag;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveAddrDetail(String addr, int orderid) {
		if (addr == null || StringUtil.isEmpty(addr)) {
			return false;
		} else {
			this.baseDaoSupport.execute(
					"update order set ship_addr=?  where order_id=?", addr,
					orderid);
			return true;
		}
	}

	@Override
	public boolean saveShipInfo(String remark, String ship_day,
			String ship_name, String ship_tel, String ship_mobile,
			String ship_zip, int orderid) {
		Order order = this.get(orderid);
		try {
			if (ship_day != null && !StringUtil.isEmpty(ship_day)) {
				this.baseDaoSupport.execute(
						"update order set ship_day=?  where order_id=?",
						ship_day, orderid);
				if (remark != null && !StringUtil.isEmpty(remark)
						&& !remark.equals("undefined")) {
					StringBuilder sb = new StringBuilder("");
					sb.append("【配送时间：");
					sb.append(remark.trim());
					sb.append("】");
					this.baseDaoSupport.execute(
							"update order set remark= concat(remark,'"
									+ sb.toString() + "')   where order_id=?",
							orderid);
				}
				return true;
			}
			if (ship_name != null && !StringUtil.isEmpty(ship_name)) {
				this.baseDaoSupport.execute(
						"update order set ship_name=?  where order_id=?",
						ship_name, orderid);
				return true;
			}
			if (ship_tel != null && !StringUtil.isEmpty(ship_tel)) {
				this.baseDaoSupport.execute(
						"update order set ship_tel=?  where order_id=?",
						ship_tel, orderid);
				return true;
			}
			if (ship_mobile != null && !StringUtil.isEmpty(ship_mobile)) {
				this.baseDaoSupport.execute(
						"update order set ship_mobile=?  where order_id=?",
						ship_mobile, orderid);
				return true;
			}
			if (ship_zip != null && !StringUtil.isEmpty(ship_zip)) {
				this.baseDaoSupport.execute(
						"update order set ship_zip=?  where order_id=?",
						ship_zip, orderid);
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void updatePayMethod(int orderid, int payid, String paytype,
			String payname) {

		this.baseDaoSupport
				.execute(
						"update order set payment_id=?,payment_type=?,payment_name=? where order_id=?",
						payid, paytype, payname, orderid);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.enation.javashop.core.service.IOrderManager#checkProInOrder(int)
	 */
	@Override
	public boolean checkProInOrder(int productid) {
		String sql = "select count(0) from order_items where product_id=?";
		return this.baseDaoSupport.queryForInt(sql, productid) > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.enation.javashop.core.service.IOrderManager#checkGoodsInOrder(int)
	 */
	@Override
	public boolean checkGoodsInOrder(int goodsid) {
		String sql = "select count(0) from order_items where goods_id=?";
		return this.baseDaoSupport.queryForInt(sql, goodsid) > 0;
	}

	@Override
	public List listByOrderIds(Integer[] orderids, String order) {
		try {
			StringBuffer sql = new StringBuffer(
					"select * from es_order where disabled=0 ");

			if (orderids != null && orderids.length > 0)
				sql.append(" and order_id in ("
						+ StringUtil.arrayToString(orderids, ",") + ")");

			if (StringUtil.isEmpty(order)) {
				order = "create_time desc";
			}
			sql.append(" order by  " + order);
			return this.daoSupport.queryForList(sql.toString(), Order.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Page list(int pageNO, int pageSize, int disabled, String order) {

		StringBuffer sql = new StringBuffer(
				"select * from order where disabled=? ");

		AdminUser adminUser = UserConext.getCurrentAdminUser();
		if (adminUser.getFounder() != 1) { // 非超级管理员加过滤条件
			boolean isShiper = permissionManager.checkHaveAuth(PermissionConfig
					.getAuthId("depot_ship")); // 检测是否是发货员
			boolean haveAllo = this.permissionManager
					.checkHaveAuth(PermissionConfig.getAuthId("allocation")); // 配货下达权限
			boolean haveOrder = this.permissionManager
					.checkHaveAuth(PermissionConfig.getAuthId("order"));// 订单管理员权限
			if (isShiper && !haveAllo && !haveOrder) {
				DepotUser depotUser = (DepotUser) adminUser;
				int depotid = depotUser.getDepotid();
				sql.append(" and depotid=" + depotid);
			}
		}

		order = StringUtil.isEmpty(order) ? "order_id desc" : order;
		sql.append(" order by " + order);
		return this.baseDaoSupport.queryForPage(sql.toString(), pageNO,
				pageSize, Order.class, disabled);
	}

	public Page list(int pageNo, int pageSize, int status, int depotid,
			String order) {
		order = StringUtil.isEmpty(order) ? "order_id desc" : order;
		String sql = "select * from order where disabled=0 and status="
				+ status;
		if (depotid > 0) {
			sql += " and depotid=" + depotid;
		}
		sql += " order by " + order;
		Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize,
				Order.class);
		return page;
	}

	@Override
	public Page listOrder(Map map, int page, int pageSize, String other,
			String order) {

		String sql = createTempSql(map, other, order);
		Page webPage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webPage;
	}

	@SuppressWarnings("unused")
	private String createTempSql(Map map, String other, String order) {

		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String orderstate = (String) map.get("order_state");// 订单状态
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		Integer status = (Integer) map.get("status");
		String sn = (String) map.get("sn");
		String ship_name = (String) map.get("ship_name");
		Integer paystatus = (Integer) map.get("paystatus");
		Integer shipstatus = (Integer) map.get("shipstatus");
		Integer shipping_type = (Integer) map.get("shipping_type");
		Integer payment_id = (Integer) map.get("payment_id");
		Integer depotid = (Integer) map.get("depotid");
		String complete = (String) map.get("complete");

		StringBuffer sql = new StringBuffer();
		sql.append("select * from order where disabled=0 ");

		if (stype != null && keyword != null) {
			if (stype == 0) {
				sql.append(" and (sn like '%" + keyword + "%'");
				sql.append(" or ship_name like '%" + keyword + "%')");
			}
		}

		if (status != null) {
			sql.append("and status=" + status);
		}

		if (sn != null && !StringUtil.isEmpty(sn)) {
			sql.append(" and sn like '%" + sn + "%'");
		}

		if (ship_name != null && !StringUtil.isEmpty(ship_name)) {
			sql.append(" and ship_name like '" + ship_name + "'");
		}

		if (paystatus != null) {
			sql.append(" and pay_status=" + paystatus);
		}

		if (shipstatus != null) {
			sql.append(" and ship_status=" + shipstatus);
		}

		if (shipping_type != null) {
			sql.append(" and shipping_id=" + shipping_type);
		}

		if (payment_id != null) {
			sql.append(" and payment_id=" + payment_id);
		}

		if (depotid != null && depotid > 0) {
			sql.append(" and depotid=" + depotid);
		}

		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(
					start_time + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
			sql.append(" and create_time>" + stime);
		}
		if (end_time != null && !StringUtil.isEmpty(end_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(
					end_time + " 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql.append(" and create_time<" + etime);
		}
		if (!StringUtil.isEmpty(orderstate)) {
			if (orderstate.equals("wait_ship")) { // 对待发货的处理
				sql.append(" and ( ( payment_type!='cod' and payment_id!=8  and  status="
						+ OrderStatus.ORDER_PAY_CONFIRM + ") ");// 非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  status="
						+ OrderStatus.ORDER_NOT_PAY + ")) ");// 货到付款的，新订单（已确认的）就可以发货
			} else if (orderstate.equals("wait_pay")) {
				sql.append(" and ( ( payment_type!='cod' and  status="
						+ OrderStatus.ORDER_NOT_PAY + ") ");// 非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_id=8 and status!="
						+ OrderStatus.ORDER_NOT_PAY + "  and  pay_status!="
						+ OrderStatus.PAY_CONFIRM + ")");
				sql.append(" or ( payment_type='cod' and  (status="
						+ OrderStatus.ORDER_SHIP + " or status="
						+ OrderStatus.ORDER_ROG + " )  ) )");// 货到付款的要发货或收货后才能结算

			} else if (orderstate.equals("wait_rog")) {
				sql.append(" and status=" + OrderStatus.ORDER_SHIP);
			} else {
				sql.append(" and status=" + orderstate);
			}

		}

		if (!StringUtil.isEmpty(complete)) {
			sql.append(" and status=" + OrderStatus.ORDER_COMPLETE);
		}

		sql.append(" ORDER BY " + other + " " + order);

		// //System.out.println(sql.toString());
		return sql.toString();
	}

	public void saveDepot(int orderid, int depotid) {
		this.orderPluginBundle.onOrderChangeDepot(this.get(orderid), depotid,
				this.listGoodsItems(orderid));
		this.daoSupport.execute(
				"update es_order set depotid=?  where order_id=?", depotid,
				orderid);
	}

	public void savePayType(int orderid, int paytypeid) {
		PayCfg cfg = this.paymentManager.get(paytypeid);
		String typename = cfg.getName();
		String paytype = cfg.getType();
		this.daoSupport
				.execute(
						"update es_order set payment_id=?,payment_name=?,payment_type=? where order_id=?",
						paytypeid, typename, paytype, orderid);
	}

	public void saveShipType(int orderid, int shiptypeid) {
		String typename = this.dlyTypeManager.getDlyTypeById(shiptypeid)
				.getName();
		this.daoSupport
				.execute(
						"update es_order set shipping_id=?,shipping_type=? where order_id=?",
						shiptypeid, typename, orderid);
	}

	@Override
	public void add(Order order) {
		this.baseDaoSupport.insert("es_order", order);
	}

	@Override
	public void saveAddr(int orderId, int ship_provinceid, int ship_cityid,
			int ship_regionid, String Attr) {
		this.daoSupport
				.execute(
						"update es_order set ship_provinceid=?,ship_cityid=?,ship_regionid=?,shipping_area=? where order_id=?",
						ship_provinceid, ship_cityid, ship_regionid, Attr,
						orderId);
	}

	@Override
	public Integer getOrderGoodsNum(int order_id) {
		String sql = "select count(0) from order_items where order_id =?";
		return this.baseDaoSupport.queryForInt(sql, order_id);
	}
	/**
	 * 以后有可能会是多个店铺
	 * 通过商品Id获取店铺ID
	 * @param goodid
	 * @return
	 */
	public Integer getstoreId(Integer goodid){
		String sql = "select store_id from es_goods where goods_id =?";
		Integer storeId = this.baseDaoSupport.queryForInt(sql, goodid);
		return storeId;
	}
	/**
	 * 目前临时获取一家店铺
	 * @return
	 */
	@Override
	public Integer getstoreId(){
		String sql = "select store_id from es_store where store_name =?";
//		Integer storeId = this.baseDaoSupport.queryForInt(sql, "Maria");
		Integer storeId = this.baseDaoSupport.queryForInt(sql, "珠海实体店");
		return storeId;
	}
	
	/**
	 * 根据order_id查询order_items
	 * @param order_id
	 * @return
	 */
	public List<OrderItem> getGoodsName(Integer order_id){
		List<OrderItem> list = this.baseDaoSupport.queryForList(
				"SELECT * FROM es_order_items WHERE order_id='"+order_id+"'",OrderItem.class);
		return list;
	}
	
	public List<Goods> getGoods(Integer goods_id){
		List<Goods> list = this.baseDaoSupport.queryForList(
				"SELECT * FROM es_goods WHERE goods_id='"+goods_id+"'",Goods.class);
		return list;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	@Override
	public List getStore(String ordersn) {
		String sql = "select o.payment_name,o.store_id,o.ship_mobile,o.ship_name,o.need_pay_money,o.sn,o.create_time,o.status,o.address_id,s.store_name from es_order as o,es_store as s where sn='" + ordersn + "'and s.store_id=o.store_id";
		List order =  this.baseDaoSupport.queryForList(sql);
		return order;
	}

	@Override
	public String updateOrderSn(Integer order_id) {
		int index = 0;
		Order orderOld = this.get(order_id);
		String[] snStr = orderOld.getSn().split("-");
		String snOld =  snStr[0];
		
		if(snStr.length>1){
			index = Integer.parseInt(snStr[1]);
			index++;
		}
		String snNew = snOld+"-"+index;
		String sql = "update es_order set sn=? where order_id="+order_id;
		this.daoSupport.execute(sql,snNew);
//		String sqlInfor = "update es_orderinformation set ORDERNO=? where order_id="+order_id;
//		this.daoSupport.execute(sqlInfor,snNew);
		return snNew;
	}
	
}
