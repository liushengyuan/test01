package com.enation.app.b2b2c.core.service.warehouse.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.service.warehouse.IWarehouse;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.entity.vo.LogisticsInformationVo;
import com.entity.vo.WarehouseVo;

/**
 * 
 * @author zks
 * 
 */
@SuppressWarnings("rawtypes")
public class Warehouse extends BaseSupport implements IWarehouse {

	@SuppressWarnings("unchecked")
	@Override
	public Page warehouseList(int pageNo, int pageSize, Map map) {
		StringBuffer sql = new StringBuffer("select * from es_warehouse o ");
		sql.append(" order by o.warehouse_id desc");
		// //System.out.println(sql.toString());
		Page rpage = this.daoSupport.queryForPage(sql.toString(), pageNo,
				pageSize, WarehouseVo.class);

		return rpage;
	}

	/**
	 * 修改订单信息
	 */
	@Override
	public void updateOrder(String trackingNumber, String order_sn,
			String amount, String createTime) {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("ship_status", OrderStatus.SHIP_YES);
		fields.put("ship_time", createTime);
		fields.put("ship_no", trackingNumber);
		fields.put("shipping_amount", amount);
		fields.put("status", OrderStatus.ORDER_SHIP);
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("sn", order_sn);
		daoSupport.update("es_order", fields, where);
	}

	/**
	 * 查询订单信息
	 */
	@Override
	public Order queryOrder(String order_sn) {
		String sql = "select * from es_order where sn = ?";
		Order order = (Order) daoSupport.queryForObject(sql, Order.class,
				order_sn);
		return order;
	}

	/**
	 * 查询订单地址
	 */
	@Override
	public MemberAddress queryMemberAddress(Integer address_id) {
		String sql = "select * from es_member_address where addr_id = ?";
		MemberAddress address = (MemberAddress) daoSupport.queryForObject(sql,
				MemberAddress.class, address_id);
		return address;
	}

	@Override
	public void createLogistics(Integer order_id, String trackingNumber,
			String referenceNumber, String createtime, String warehouse_id) {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("order_id", order_id);
		fields.put("referenceNumber", referenceNumber);
		fields.put("trackingNumber", trackingNumber);
		fields.put("createtime", createtime);
		fields.put("warehouse_id", warehouse_id);
		daoSupport.insert("es_logisticsinformation", fields);
	}

	/**
	 * 修改订单状态（通过Ruston下单）
	 */
	@Override
	public void updateOrderByRuston(String trackingNumber, String order_sn,
			String amount, String createTime, String logi_name,String weight) {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("ship_status", OrderStatus.SHIP_YES);
		fields.put("ship_time", createTime);
		fields.put("sale_cmpl_time", DateUtil.getDateline());
		fields.put("weight", weight);
		fields.put("ship_no", trackingNumber);
		fields.put("shipping_amount", amount);
		fields.put("status", OrderStatus.ORDER_SHIP);
		fields.put("logi_name", logi_name);
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("sn", order_sn);
		daoSupport.update("es_order", fields, where);
	}

	@Override
	public LogisticsInformationVo queryLogistics(String ship_no) {
		String sql = "select * from es_logisticsinformation where trackingNumber = ? ";
		return (LogisticsInformationVo) this.daoSupport.queryForObject(sql,
				LogisticsInformationVo.class, ship_no);
	}

	@Override
	public WarehouseVo queryWarehouse(String warehouse_id) {
		String sql = "select * from es_warehouse where warehouse_id = ? ";
		return (WarehouseVo) this.daoSupport.queryForObject(sql,
				WarehouseVo.class, warehouse_id);
	}
	
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void updateOrderBySend(Map<String, Object> fields) {
		String sn = fields.get("sn").toString();
		fields.remove("sn");
		daoSupport.update("es_order", fields, " sn = " + sn );
		
	}

}
