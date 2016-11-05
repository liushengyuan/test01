package com.enation.app.shop.core.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;

/**
 * 订单打印接口
 * @author kingapex
 *2014-1-13下午7:25:33
 */
public interface IOrderPrintManager {
	
	/**
	 * 获取发货打印的script
	 * @param orderid
	 * @return
	 */
	public String getShipScript(Integer[] orderid);
	
	/**
	 * 获取快递打印的script
	 * @param orderid
	 * @return
	 */
	public String getExpressScript(Integer[] orderid);
	
	
	/**
	 * 批量保存物流单号
	 * @param orderids 订单号
	 * @param logi_id 物流公司ID
	 * @param logi_name 物流公司名称
	 * @param shipNos 快递号
	 */
	public void saveShopNos(Integer[] orderids,String[] shipNos,String[] logi_id,String[] logi_name);
	
	
	
	/**
	 * 批量发货
	 * @param orderids
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String ship(Integer[] orderids);

	public List<Order> getOrder(Integer[] orderids);

	public OrderItem getItem(int orderid);
	
	public List<OrderItem> getListItem(int orderid);
}
