package com.enation.app.b2b2c.core.service.order;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.shop.core.model.LargeOrder;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.framework.database.Page;

/**
 * 店铺订单管理类
 * 
 * @author LiFenLong
 * 
 */
public interface IStoreOrderManager {
	/**
	 * 查看店铺订单
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param storeid
	 * @param map
	 * @return 店铺订单列表
	 */
	public Page storeOrderList(Integer pageNo, Integer pageSize,
			Integer storeid, Map map);

	/**
	 * 查看店铺子订单
	 * 
	 * @param parent_id
	 * @return 店铺子订单列表
	 */
	public List<StoreOrder> storeOrderList(Integer parent_id);

	/**
	 * 获取一个订单
	 * 
	 * @param orderId
	 * @return StoreOrder
	 */
	public StoreOrder get(Integer orderId);

	/**
	 * 修改收货人信息
	 * 
	 * @param remark
	 * @param ship_day
	 * @param ship_name
	 * @param ship_tel
	 * @param ship_mobile
	 * @param ship_zip
	 * @param orderid
	 * @return boolean
	 */
	public boolean saveShipInfo(String remark, String ship_day,
			String ship_name, String ship_tel, String ship_mobile,
			String ship_zip, int orderid);

	/**
	 * 查看店铺会员订单
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @param status
	 * @param keyword
	 * @return Page
	 */
	public Page pageOrders(int pageNo, int pageSize, String status,
			String keyword,String language);

	/**
	 * 根据订单编号查看订单
	 * 
	 * @param ordersn
	 * @return StoreOrder
	 */
	public StoreOrder get(String ordersn);

	/**
	 * 根据订单状态获取店铺订单数量
	 * 
	 * @param struts
	 * @author LiFenLong
	 * @return
	 */
	public int getStoreOrderNum(int struts);

	/**
	 * 查询订单列表
	 * 
	 * @author LiFenLong
	 * @param map
	 * @param page
	 * @param pageSize
	 * @param other
	 * @param sort
	 * @return
	 */
	public Page listOrder(Map map, int page, int pageSize, String sort,
			String order);

	/**
	 * 获取订单状态的json
	 * 
	 * @return 订单状态Json
	 */
	public Map getStatusJson();

	/**
	 * 获取付款状态的json
	 * 
	 * @return 付款状态Json
	 */
	public Map getpPayStatusJson();

	/**
	 * 获取配送状态的json
	 * 
	 * @return 配送状态Json
	 */
	public Map getShipJson();

	/**
	 * 发货
	 * 
	 * @param order_id
	 *            订单ID
	 * @param shipping_id
	 *            配送方式ID
	 * @param shipNo
	 *            运单号
	 */
	public void ship(Integer order_id, Integer shipping_id, String shipNo);

	/**
	 * 获得该会员订单在各个状态的个数
	 * 
	 */
	public Integer orderStatusNum(Integer status);

	/**
	 * 获得该店铺订单在各个状态的个数
	 * 
	 */
	public Integer storeOrderStatusNum(Integer status);

	/**
	 * 通过商铺ID，获得该商铺下的商品个数
	 * 
	 * @param store_id
	 * @return
	 */
	public Integer getStoreGoodsNum(int store_id);
	/**
	 * 添加大宗交易意向
	 * 
	 * @param largeOrder
	 * @return
	 */
	public void addLargeOrder(LargeOrder largeOrder);
	/**
	 * 通过商品id得到店铺名称
	 * 
	 * @param goods_id
	 * @return
	 */
	public  Order getOrder(String ordersn);
	public String getStoreName(Integer goods_id);
	public List<OrderItem>  getOrderItem(Integer id);
	/*
	 * 获取订单的List
	 */
	public List<StoreOrder> listOrder(Map orderMap);
}
