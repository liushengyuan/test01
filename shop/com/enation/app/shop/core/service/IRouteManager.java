package com.enation.app.shop.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.GoodsStores;
import com.enation.app.shop.core.model.Route;
import com.enation.app.shop.core.model.RouteOrder;
import com.enation.app.shop.core.model.RouteRu;
import com.enation.app.shop.core.model.ordermanifest.OrderPaymentInformation;
import com.enation.app.shop.core.model.support.GoodsEditDTO;
import com.enation.framework.database.Page;
import com.entity.vo.LogisticsInformationVo;

/**
 * 商品管理接口
 * 
 * @author kingapex
 * 
 */
public interface IRouteManager {

	public 	List getList(String  mailno);

	public void saveroute(Route  route);

	public  void saverouteorder(RouteOrder routeorder);
	public  void saveAddPaymentInformation(OrderPaymentInformation orderPaymentInformation);

	public void updateOrderByRuston(String mailno, String sn, String totalw);
	public void updateOrderBySf(String mailno, String sn, String totalw,Double price);

	public List<RouteOrder> getRoute();

	public int getnum(Route route);
	
	public List<LogisticsInformationVo> getlistlogi();

	public void saverouteru(RouteRu routeRu);

	public int getnumru(RouteRu routeRu);

	public List getListru(String mailno);
	/**
	 * 获取所有俄通收的物流信息
	 * @return
	 */
	public List<LogisticsInformationVo> getlistlogiETS();
}