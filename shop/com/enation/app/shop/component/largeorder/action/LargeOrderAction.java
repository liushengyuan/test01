package com.enation.app.shop.component.largeorder.action;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;

import com.enation.app.shop.component.largeorder.model.BigOrder;
import com.enation.app.shop.component.largeorder.service.ILargeOrderManager;
import com.enation.app.shop.core.model.LargeOrder;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.utils.DateUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.StringUtil;

/**
 * 大宗订单交易
 * @author jfw
 *2015-10-14上午09:14:39
 */
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Scope("prototype")
@Action("largeOrder")
@Results({
	 @Result(name="list", type="freemarker", location="/com/enation/app/shop/component/largeorder/action/html/large_mind_list.html") ,
	 @Result(name="edit", type="freemarker", location="/com/enation/app/shop/component/largeorder/action/html/large_mind_edit.html"),
	 @Result(name="listOrder", type="freemarker", location="/com/enation/app/shop/component/largeorder/action/html/large_order_list.html") ,
	 @Result(name="addOrder", type="freemarker", location="/com/enation/app/shop/component/largeorder/action/html/large_order_add.html"),
	 @Result(name="eidtOrder", type="freemarker", location="/com/enation/app/shop/component/largeorder/action/html/large_order_edit.html")
})
public class LargeOrderAction extends WWAction{
	private LargeOrder largeOrder;
	private BigOrder bigOrder;
	private Map mindMap;
	private Integer status=null;
	private Integer mind_id;
	private Integer order_id;
	private String keyword;
	private String start_time;
	private String end_time;
	private String store_name;
	private String request_time;
	private ILargeOrderManager largeOrderManager;
	
	private Map orderMap;
	private Map statusMap;
	private Map payStatusMap;
	private Map shipMap;
	private String status_Json;
	private String payStatus_Json;
	private String ship_Json;
	/**
	 * 添加一笔大宗交易
	 * @return
	 */
	public String list(){
		
//		long request_time = com.enation.framework.util.DateUtil.getDateline();
//		largeOrder.setRequest_time(request_time);
//		this.largeOrderManager.add(largeOrder);
//		this.showSuccessJson("添加成功");
		return "list";
	}
	
	public String listMindJson(){
		mindMap = new HashMap();
		mindMap.put("keyword", keyword);
		mindMap.put("start_time", start_time);
		mindMap.put("end_time", end_time);
		mindMap.put("status", status);
		mindMap.put("store_name", store_name);
		this.webpage = this.largeOrderManager.listMind(mindMap, this.getPage(),this.getPageSize(), this.getSort(),this.getOrder());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	
	public String edit(){
		this.largeOrder = this.largeOrderManager.getLargeOrder(mind_id);
		return "edit";
	}
	public String saveEdit(){
		long handle_time = com.enation.framework.util.DateUtil.getDateline();
		largeOrder.setHandle_time(handle_time);
		largeOrder.setRequest_time(com.enation.framework.util.DateUtil.getDateline(request_time));
		largeOrder.setStatus(1);
		try {
			this.largeOrderManager.update(largeOrder);
			this.showSuccessJson("保存大宗交易意向成功");
		} catch (Throwable e) {
			this.logger.error("保存大宗交易意向出错", e);
			this.showErrorJson("保存大宗交易意向出错"+e.getMessage());
		}
		return this.JSON_MESSAGE;		
	}
	/**
	 * 查询大宗交易订单
	 * @return
	 */
	public String listOrder(){
		if(statusMap==null){
			statusMap = new HashMap();
			statusMap = getStatusJson();
			String p= JSONArray.fromObject(statusMap).toString();
			status_Json=p.replace("[", "").replace("]", "");
		}
		
		if(payStatusMap==null){
			payStatusMap = new HashMap();
			payStatusMap = getpPayStatusJson();
			String p= JSONArray.fromObject(payStatusMap).toString();
			payStatus_Json=p.replace("[", "").replace("]", "");
			
		}
		
		if(shipMap ==null){
			shipMap = new HashMap();
			shipMap = getShipJson();
			String p= JSONArray.fromObject(shipMap).toString();
			ship_Json = p.replace("[", "").replace("]", "");
			
		}
		//shipTypeList = dlyTypeManager.list();配送方式
		//payTypeList = paymentManager.list();支付方式

		return "listOrder";
	}
	/**
	 * 查询大宗交易订单(过滤条件)
	 * @return
	 */
	public String listOrderJson(){
		orderMap = new HashMap();
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("status", status);
		orderMap.put("store_name", store_name);
		this.webpage = this.largeOrderManager.listOrder(orderMap, this.getPage(),this.getPageSize(), this.getSort(),this.getOrder());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 跳转至添加大宗订单页面
	 * @return
	 */
	public String addOrder() {
		if(statusMap==null){
			statusMap = new HashMap();
			statusMap = getStatusJson();
			String p= JSONArray.fromObject(statusMap).toString();
			status_Json=p.replace("[", "").replace("]", "");
		}
		
		if(payStatusMap==null){
			payStatusMap = new HashMap();
			payStatusMap = getpPayStatusJson();
			String p= JSONArray.fromObject(payStatusMap).toString();
			payStatus_Json=p.replace("[", "").replace("]", "");
			
		}
		
		if(shipMap ==null){
			shipMap = new HashMap();
			shipMap = getShipJson();
			String p= JSONArray.fromObject(shipMap).toString();
			ship_Json = p.replace("[", "").replace("]", "");
			
		}
		return "addOrder";
	}
	/**
	 * 保存新增的大宗订单
	 * @return
	 */
	public String saveAddOrder(){
		if( bigOrder.getStatus()==null || bigOrder.getStatus()==100){
			this.showErrorJson("请输入订单状态");
			return this.JSON_MESSAGE;
		}
		if( bigOrder.getStatus()==null || bigOrder.getPay_status()==100){
			this.showErrorJson("请输入付款状态");
			return this.JSON_MESSAGE;
		}
		if( bigOrder.getStatus()==null || bigOrder.getShip_status()==100){
			this.showErrorJson("请输入货运状态");
			return this.JSON_MESSAGE;
		}
		bigOrder.setSn(com.enation.framework.util.DateUtil.getDateline()+"");
		bigOrder.setCreate_time(com.enation.framework.util.DateUtil.getDateline());
		this.largeOrderManager.addOrder(bigOrder);
		this.showSuccessJson("添加大宗交易订单成功");
		return this.JSON_MESSAGE;
	}
	/**
	 * 跳转至修改大宗订单页面
	 * @return
	 */
	public String editOrder() {
		if(statusMap==null){
			statusMap = new HashMap();
			statusMap = getStatusJson();
			String p= JSONArray.fromObject(statusMap).toString();
			status_Json=p.replace("[", "").replace("]", "");
		}
		
		if(payStatusMap==null){
			payStatusMap = new HashMap();
			payStatusMap = getpPayStatusJson();
			String p= JSONArray.fromObject(payStatusMap).toString();
			payStatus_Json=p.replace("[", "").replace("]", "");
			
		}
		
		if(shipMap ==null){
			shipMap = new HashMap();
			shipMap = getShipJson();
			String p= JSONArray.fromObject(shipMap).toString();
			ship_Json = p.replace("[", "").replace("]", "");
			
		}
		this.bigOrder = this.largeOrderManager.getBigOrder(order_id);
		return "eidtOrder";
	}
	/**
	 * 保存修改大宗订单页面
	 * @return
	 */
	public String saveEditOrder(){
		if( bigOrder.getStatus()==null || bigOrder.getStatus()==100){
			this.showErrorJson("请输入订单状态");
			return this.JSON_MESSAGE;
		}
		if( bigOrder.getStatus()==null || bigOrder.getPay_status()==100){
			this.showErrorJson("请输入付款状态");
			return this.JSON_MESSAGE;
		}
		if( bigOrder.getStatus()==null || bigOrder.getShip_status()==100){
			this.showErrorJson("请输入货运状态");
			return this.JSON_MESSAGE;
		}
		try {
			this.largeOrderManager.updateBigOrder(bigOrder);
			this.showSuccessJson("修改大宗交易订单成功");
		} catch (Throwable e) {
			this.logger.error("保存大宗交易订单出错", e);
			this.showErrorJson("保存大宗交易订单出错"+e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取订单状态的json
	 * @return
	 */
	private Map getStatusJson(){
		Map orderStatus = new  HashMap();
		
		orderStatus.put(""+OrderStatus.ORDER_NOT_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_PAY));
		orderStatus.put(""+OrderStatus.ORDER_NOT_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_PAY_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_ALLOCATION_YES, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ALLOCATION_YES));
		orderStatus.put(""+OrderStatus.ORDER_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_ROG, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_COMPLETE, OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_PAY));
		orderStatus.put(""+OrderStatus.ORDER_CANCELLATION, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCELLATION));
		orderStatus.put(""+OrderStatus.ORDER_CHANGED, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CHANGED));
		orderStatus.put(""+OrderStatus.ORDER_CHANGE_APPLY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CHANGE_APPLY));
		orderStatus.put(""+OrderStatus.ORDER_RETURN_APPLY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_RETURN_APPLY));
		orderStatus.put(""+OrderStatus.ORDER_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY));
		return orderStatus;
	}
	
	
	private Map getpPayStatusJson(){
		
		Map pmap = new HashMap();
		pmap.put(""+OrderStatus.PAY_NO, OrderStatus.getPayStatusText(OrderStatus.PAY_NO));
	//	pmap.put(""+OrderStatus.PAY_YES, OrderStatus.getPayStatusText(OrderStatus.PAY_YES));
		pmap.put(""+OrderStatus.PAY_CONFIRM, OrderStatus.getPayStatusText(OrderStatus.PAY_CONFIRM));
		pmap.put(""+OrderStatus.PAY_CANCEL, OrderStatus.getPayStatusText(OrderStatus.PAY_CANCEL));
		pmap.put(""+OrderStatus.PAY_PARTIAL_PAYED, OrderStatus.getPayStatusText(OrderStatus.PAY_PARTIAL_PAYED));

		return pmap;
	}
	private Map getShipJson(){
		Map map = new HashMap();
		map.put(""+OrderStatus.SHIP_ALLOCATION_NO, OrderStatus.getShipStatusText(OrderStatus.SHIP_ALLOCATION_NO));
		map.put(""+OrderStatus.SHIP_ALLOCATION_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_ALLOCATION_YES));
		map.put(""+OrderStatus.SHIP_NO, OrderStatus.getShipStatusText(OrderStatus.SHIP_NO));
		map.put(""+OrderStatus.SHIP_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_YES));
		map.put(""+OrderStatus.SHIP_CANCEL, OrderStatus.getShipStatusText(OrderStatus.SHIP_CANCEL));
		map.put(""+OrderStatus.SHIP_PARTIAL_SHIPED, OrderStatus.getShipStatusText(OrderStatus.SHIP_PARTIAL_SHIPED));
		map.put(""+OrderStatus.SHIP_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_YES));
		map.put(""+OrderStatus.SHIP_CANCEL, OrderStatus.getShipStatusText(OrderStatus.SHIP_CANCEL));
		map.put(""+OrderStatus.SHIP_CHANED, OrderStatus.getShipStatusText(OrderStatus.SHIP_CHANED));
		map.put(""+OrderStatus.SHIP_ROG, OrderStatus.getShipStatusText(OrderStatus.SHIP_ROG));
		return map;
	}
	public LargeOrder getLargeOrder() {
		return largeOrder;
	}
	public void setLargeOrder(LargeOrder largeOrder) {
		this.largeOrder = largeOrder;
	}
	public ILargeOrderManager getLargeOrderManager() {
		return largeOrderManager;
	}
	public void setLargeOrderManager(ILargeOrderManager largeOrderManager) {
		this.largeOrderManager = largeOrderManager;
	}

	public Map getMindMap() {
		return mindMap;
	}

	public void setMindMap(Map mindMap) {
		this.mindMap = mindMap;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
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

	public String getStore_name() {
		return store_name;
	}

	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}

	public Integer getMind_id() {
		return mind_id;
	}

	public void setMind_id(Integer mind_id) {
		this.mind_id = mind_id;
	}

	public String getRequest_time() {
		return request_time;
	}

	public void setRequest_time(String request_time) {
		this.request_time = request_time;
	}

	public Map getOrderMap() {
		return orderMap;
	}

	public void setOrderMap(Map orderMap) {
		this.orderMap = orderMap;
	}

	public Map getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map statusMap) {
		this.statusMap = statusMap;
	}

	public Map getPayStatusMap() {
		return payStatusMap;
	}

	public void setPayStatusMap(Map payStatusMap) {
		this.payStatusMap = payStatusMap;
	}

	public Map getShipMap() {
		return shipMap;
	}

	public void setShipMap(Map shipMap) {
		this.shipMap = shipMap;
	}

	public String getStatus_Json() {
		return status_Json;
	}

	public void setStatus_Json(String status_Json) {
		this.status_Json = status_Json;
	}

	public String getPayStatus_Json() {
		return payStatus_Json;
	}

	public void setPayStatus_Json(String payStatus_Json) {
		this.payStatus_Json = payStatus_Json;
	}

	public String getShip_Json() {
		return ship_Json;
	}

	public void setShip_Json(String ship_Json) {
		this.ship_Json = ship_Json;
	}

	public BigOrder getBigOrder() {
		return bigOrder;
	}

	public void setBigOrder(BigOrder bigOrder) {
		this.bigOrder = bigOrder;
	}

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	
	
}
