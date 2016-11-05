package com.enation.app.shop.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



import com.enation.app.shop.core.model.Route;
import com.enation.app.shop.core.model.RouteOrder;
import com.enation.app.shop.core.model.RouteRu;
import com.enation.app.shop.core.model.ordermanifest.OrderPaymentInformation;
import com.enation.app.shop.core.service.IRouteManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.app.shop.core.service.SnDuplicateException;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.entity.vo.LogisticsInformationVo;

/**
 * Goods业务管理
 * 
 * @author kingapex 2010-1-13下午12:07:07
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RouteManager extends BaseSupport implements IRouteManager {

	@Override
	public List getList( String mailno ) {
		String  sql  =  "select * from  es_route  where mailno=? ";
	//	 mailno="234324234";
		List<Route>  list =this.daoSupport.queryForList(sql, Route.class,mailno);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveroute(Route route) {
	
		this.daoSupport.insert("es_route", route);
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saverouteorder(RouteOrder routeorder) {
	
		this.daoSupport.insert("es_route_order", routeorder);
	}
	public void updateOrderByRuston(String trackingNumber, String order_sn,
			String weight) {
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("ship_status", OrderStatus.SHIP_YES);
		fields.put("ship_time", DateUtil.getDateline());
		fields.put("sale_cmpl_time", DateUtil.getDateline());
		fields.put("weight", weight);
		fields.put("ship_no", trackingNumber);
	//	fields.put("shipping_amount", amount);
		
		fields.put("status", OrderStatus.ORDER_SHIP);
		fields.put("logi_name", "SF");
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("sn", order_sn);
		daoSupport.update("es_order", fields, where);
	}
	public void updateOrderBySf(String trackingNumber, String order_sn, String weight,Double price){
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("ship_status", OrderStatus.SHIP_YES);
		fields.put("ship_time", DateUtil.getDateline());
		fields.put("sale_cmpl_time", DateUtil.getDateline());
		fields.put("weight", weight);
		fields.put("ship_no", trackingNumber);
	//	fields.put("shipping_amount", amount);
		fields.put("shiping_freight",price);
		fields.put("status", OrderStatus.ORDER_SHIP);
		fields.put("logi_name", "SF");
		Map<String, Object> where = new HashMap<String, Object>();
		where.put("sn", order_sn);
		fields.put("shiping_freight",price);
		daoSupport.update("es_order", fields, where);
	}

	@Override
	public List<RouteOrder>  getRoute() {
		String sql="select * from  es_route_order";
		return this.daoSupport.queryForList(sql, RouteOrder.class);
	}

	@Override
	public int getnum(Route route) {
		String  sql ="select  count(*) from  es_route where mailno =? and accept_time=?" ;
		return this.daoSupport.queryForInt(sql, route.getMailno(),route.getAccept_time());
	}

	@Override
	public List<LogisticsInformationVo> getlistlogi() {
		String  sqlString="select * from  es_logisticsinformation ORDER BY logistics_id DESC";
		return this.daoSupport.queryForList(sqlString,LogisticsInformationVo.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	
	public void saverouteru(RouteRu routeRu) {
		
		
		this.daoSupport.insert("es_route_ru", routeRu);
		
		
	}

	@Override
	public int getnumru(RouteRu routeRu) {
		String  sql="select count(*) from  es_route_ru where tracking_number=? and occur_date=?  ";
		return this.daoSupport.queryForInt(sql,routeRu.getTracking_number(),routeRu.getOccur_date());
	}

	@Override
	public List getListru(String mailno) {
		String sql="select * from es_route_ru where  reference_Number=? ORDER BY id DESC";
		
		List<RouteRu>  list =this.daoSupport.queryForList(sql, RouteRu.class,mailno);
		return list;
	}

	@Override
	public List<LogisticsInformationVo> getlistlogiETS() {
		// TODO Auto-generated method stub
		String  sqlString="select * from  es_logisticsinformation WHERE trackingNumber LIKE '%TS%' ORDER BY logistics_id DESC";
		return this.daoSupport.queryForList(sqlString,LogisticsInformationVo.class);
	}

	@Override
	public void saveAddPaymentInformation(
			OrderPaymentInformation orderPaymentInformation) {
		this.baseDaoSupport.insert("es_orderPaymentInformation", orderPaymentInformation);
		
	}
	
	

	/**
	 * 添加商品，同时激发各种事件
	 */
//	@Transactional(propagation = Propagation.REQUIRED)
	
}
