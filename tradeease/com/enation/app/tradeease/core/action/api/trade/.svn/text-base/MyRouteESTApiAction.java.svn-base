package com.enation.app.tradeease.core.action.api.trade;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;

import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.dom4j.Attribute;
import org.dom4j.Document;

import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;

import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.FreezePoint;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.PointHistory;
import com.enation.app.shop.core.model.Route;
import com.enation.app.shop.core.model.RouteOrder;
import com.enation.app.shop.core.model.RouteRu;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IRouteManager;
import com.enation.app.shop.core.utils.TransUtils;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.model.trade.Trade;
import com.enation.app.tradeease.core.service.trade.ITradeManager;
import com.enation.app.tradeease.core.util.LoanFtpUtils;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.entity.vo.LogisticsInformationVo;
import com.gd.express.util.PropertiesUtil;


import com.orderonline.api.tool.CargoTrackingResponse;
import com.orderonline.api.tool.CargoTrackingServiceResponse;
import com.orderonline.api.tool.ChargeCalculateResponse;
import com.orderonline.api.tool.IOrderOnlineToolService;
import com.orderonline.api.tool.OrderOnlineToolServiceImplService;
import com.orderonline.api.tool.TrackInfo;

import com.orderonline.api.tool.Track;


/**
 * 俄通收轨迹查询
 * @author jfw
 *
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("myetsroute")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class MyRouteESTApiAction extends WWAction {
	/*
	private final QName SERVICE_NAME_TOOL = new QName(
			"entity.orderonlinetool.api.fpx",
			"OrderOnlineToolServiceImplService");
	URL wsdlURL_tool = OrderOnlineToolServiceImplService.WSDL_LOCATION;
	OrderOnlineToolServiceImplService tool = new OrderOnlineToolServiceImplService(
			wsdlURL_tool, SERVICE_NAME_TOOL);
	IOrderOnlineToolService toolPort = tool.getOrderOnlineToolServiceImplPort();
	*/
	// 获取
	private final QName SERVICE_NAME_TOOL = new QName(
			"entity.orderonlinetool.api.fpx",
			"OrderOnlineToolServiceImplService");
	URL wsdlURL_tool = OrderOnlineToolServiceImplService.WSDL_LOCATION;
	OrderOnlineToolServiceImplService tool = new OrderOnlineToolServiceImplService(
			wsdlURL_tool, SERVICE_NAME_TOOL);
	IOrderOnlineToolService toolPort = tool.getOrderOnlineToolServiceImplPort();
	
	private IRouteManager routeManager;
	private IDaoSupport daoSupport;
	//俄通收的token
	static String token2 = PropertiesUtil.getToken2();
	
	
	
	//俄通收轨迹查询。定时任务
	public  void etsroute(){
		////System.out.println("俄通收轨迹定时任务开始执行啦！");
		//查询所有俄通收的物流订单信息。查询中做了设置以TS开头的物流
		List<LogisticsInformationVo> list1=this.routeManager.getlistlogiETS();
		for (LogisticsInformationVo logi : list1) {
			List<CargoTrackingResponse> responseList = new ArrayList<CargoTrackingResponse>();
			List<String> list = new ArrayList<String>();
			////System.out.println(logi.getReferenceNumber());订单号sn
			list.add(logi.getReferenceNumber());
			//list.add("TS001454303E");//测试用的
			//发送请求。获取响应的结果。
			responseList = toolPort.cargoTrackingService(token2, list);
			RouteRu  routeRu = new RouteRu();
			for (CargoTrackingResponse response : responseList) {
				//获取响应中物流轨迹的信息。xml中元素的值。
				List<Track> trackList = response.getTracks();
				for (Track track : trackList) {
					List<TrackInfo> trackinfoList=	track.getTrackInfo();
					//循环获取的信息
					for (TrackInfo info : trackinfoList) {
						routeRu.setTracking_number(logi.getReferenceNumber());
						//if (!StringUtil.isEmpty(info.getOccurAddress())) {
							//服务商跟踪号码
						routeRu.setReference_Number(logi.getTrackingNumber());
						//}
						if (!StringUtil.isEmpty(info.getOccurAddress())) {
							//轨迹发生地点
							routeRu.setOccur_address(info.getOccurAddress());
						}
							////System.out.println(track.getTrackInfo().get(0).getOccurAddress());
						if (!StringUtil.isEmpty(info.getOccurDate())) {
							//轨迹发生时间
							routeRu.setOccur_date(info.getOccurDate());
						}
							////System.out.println(track.getTrackInfo().get(0).getOccurDate());
						if (!StringUtil.isEmpty(info.getTrackCode())) {
							//轨迹代码，参照轨迹代码表
							routeRu.setTrack_code(info.getTrackCode());
						}
							////System.out.println(track.getTrackInfo().get(0).getTrackCode());
						if (!StringUtil.isEmpty(info.getTrackContent())) {
							//轨迹状态描述补充
							routeRu.setTrack_content(info.getTrackContent());
						}
						////System.out.println(track.getTrackInfo().get(0).getTrackContent());
						//routeRu.setTrack_content(track.getTrackInfo().get(0).getTrackContent());
						////System.out.println(track.getTrackInfo().get(0).getTrackContent());
						int sum= this.routeManager.getnumru(routeRu);
						if(sum<1){
							this.routeManager.saverouteru(routeRu);
						}
					}
				}
			}
		}
	}
	
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	
	
	public IRouteManager getRouteManager() {
		return routeManager;
	}

	public void setRouteManager(IRouteManager routeManager) {
		this.routeManager = routeManager;
	}
}