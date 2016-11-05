package com.enation.app.b2b2c.core.service.warehouse;

import java.util.Map;

import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.model.Order;
import com.enation.framework.database.Page;
import com.entity.vo.LogisticsInformationVo;
import com.entity.vo.WarehouseVo;
/**
 * 
 * @author zks
 *
 */
public interface IWarehouse {

	/**
	 * 查询仓库信息
	 * 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Page warehouseList(int parseInt, int pageSize, Map result);

	/**
	 * 修改订单状态
	 * 
	 * @param trackingNumber   运单号
	 * @param order_sn	订单号
	 * @param amount	 运费
	 * @param createTime   发货日期
	 */
	public void updateOrder(String trackingNumber, String order_sn,
			String amount, String createTime);

	/**
	 * 查询订单信息
	 * 
	 * @param order_sn
	 * @return
	 */
	public Order queryOrder(String order_sn);

	/**
	 * 查询订单地址
	 * 
	 * @param address_id
	 * @return
	 */
	public MemberAddress queryMemberAddress(Integer address_id);
	
	/**
	 * 创建物流订单信息
	 * @param order_id
	 * @param trackingNumber
	 * @param referenceNumber
	 * @param createTime 
	 * @param warehouse_id 
	 */
	public void createLogistics(Integer order_id, String trackingNumber,
			String referenceNumber, String createTime, String warehouse_id);

	/**
	 * 修改订单状态（通过Ruston下单）
	 * @param trackingNumber
	 * @param order_sn
	 * @param amount
	 * @param createTime
	 * @param logi_name
	 */
	public void updateOrderByRuston(String trackingNumber, String order_sn,
			String amount, String createTime, String logi_name, String weight);
	
	/**
	 * 查询物流信息
	 * @param ship_no
	 * @return
	 */
	public LogisticsInformationVo queryLogistics(String ship_no);
	
	/**
	 * 查询仓库信息
	 * @param warehouse_id
	 * @return
	 */
	public WarehouseVo queryWarehouse(String warehouse_id);
	
	/**
	 * 订单发货  更新物流相关字段
	 * @author WKZ
	 */
	public void updateOrderBySend(Map<String,Object> fields);

}
