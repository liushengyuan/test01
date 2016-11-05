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
import java.util.ResourceBundle;
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


import entity.orderonline.api.tool.CargoTrackingResponse;
import entity.orderonline.api.tool.CargoTrackingServiceResponse;
import entity.orderonline.api.tool.ChargeCalculateResponse;
import entity.orderonline.api.tool.IOrderOnlineToolService;
import entity.orderonline.api.tool.OrderOnlineToolServiceImplService;
import entity.orderonline.api.tool.TrackInfo;

import entity.orderonline.api.tool.Track;



@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("myroute")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class MyRouteApiAction extends WWAction {
	private final QName SERVICE_NAME_TOOL = new QName(
			"entity.orderonlinetool.api.fpx",
			"OrderOnlineToolServiceImplService");
	URL wsdlURL_tool = OrderOnlineToolServiceImplService.WSDL_LOCATION;
	OrderOnlineToolServiceImplService tool = new OrderOnlineToolServiceImplService(
			wsdlURL_tool, SERVICE_NAME_TOOL);
	IOrderOnlineToolService toolPort = tool.getOrderOnlineToolServiceImplPort();
	private IRouteManager routeManager;
	private IDaoSupport daoSupport;
	static String token = PropertiesUtil.getToken();
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("shunfeng");
	static String urlAPI = bundle.getString("url");
	static String checkWord2ru = bundle.getString("checkWord2ru");
	static String yuejiehao2ru = bundle.getString("yuejiehao2ru");
	
	static String checkWord2cn = bundle.getString("checkWord2cn");
	static String yuejiehao2cn = bundle.getString("yuejiehao2cn");
	
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
	//顺丰 国内业务查询轨迹定时任务
	public  void route() throws Exception {
		////System.out.println("顺丰定时任务");

//		String url = "http://218.17.248.244:11080/bsp-oisp/sfexpressService";//国内联调使用
		String url = urlAPI;//国内正式环境
		int port = 443;
		String  url2="http://bsp-oisp.test.sf-express.com:6080/bsp-oisp/ws/sfexpressService?wsdl";

//		String checkword = "CTZkCuwfjiVT7Bvu";//国内联调使用
		String checkword = checkWord2cn;//国内正式环境
		String xmlFile="D:\\user.xml";
		String checkword2 =	"MoZYb2Lenno3N3TLfI[;";
		String  bianma="szglkj";
		String  bianma2="BSPdevelop";
		String  bianma3="YZFKJ";//国内联调
		//月结号
		String yuejiehao =yuejiehao2cn;//国内业务正式
		//参数 end
		List<RouteOrder> routeOrderlist=  this.routeManager.getRoute();
		for (RouteOrder  routes:      routeOrderlist  ) {
				
			
			String  orderid=routes.getOrderid();
			String  mailno=routes.getMailno();
			String  mailno3="444501061775";
			String  xmls2="<Request service='RouteService' lang='zh-CN'><Head>"+yuejiehao+"</Head><Body><RouteRequest tracking_type='1' method_type='1' tracking_number='"+mailno+"'/> </Body> </Request>";
			String xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+bianma+"</Head><Body><Order orderid='"+orderid+"' j_company='yizhifu' j_contact='nan' j_tel='1244323433' j_mobile='ewr234234' j_shippercode='755VA' j_country='SG' j_province='Singapore' j_city='Singapore' j_county='Singapore' j_address='7SixthLokYangRoad#11-11Singapore628105' j_post_code='628105' d_deliverycode='852' d_country='HK' d_company='Daniel' d_contact='DanielLi' d_tel='87654321' d_mobile='87654321' d_province='Hong Kong' d_city='Hong Kong' d_county='Hong Kong' d_address='27/Ftestaddress,Kowloon,HongKong' d_post_code='852852' custid='7551878519' pay_method='1' express_type='23' parcel_quantity='1' cargo_total_weight='2.18' declared_value='1' declared_value_currency='CNY' sendstarttime='2014-12-17 07:08:38' remark='beizhu'><Cargo name='nan-ceshi2' count='20' unit='pcs' weight='0.003' amount='0.05' currency='CNY' source_area='China' /></Order></Body></Request>";
			////System.out.println(xmls);
			/*	byte[] midbytes=xmls.getBytes("utf8");
			String xml2=new String(midbytes,"utf8");
			//System.out.println(xml2);*///不能用
			////System.out.println(xmls2);
			String verifyCode=TransUtils.md5EncryptAndBase64(xmls2 + checkword);
			//	//System.out.println(verifyCode);//测试看结果
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("xml", xmls2));
			nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
			
			HttpClient httpclient=getHttpClient(port);
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
			HttpResponse response = httpclient.execute(httpPost);

			if (response.getStatusLine().getStatusCode() == 200){
				////System.out.println(EntityUtils.toString(response.getEntity()));//只能解析一次
				String xml8 = EntityUtils.toString(response.getEntity());
	
				SAXReader saxReader = new SAXReader();
				Document doc = null;
				
				ByteArrayInputStream is = new ByteArrayInputStream(xml8.getBytes("UTF-8"));
				doc = saxReader.read(is); 
				////System.out.println("doc2成功");	
				Element root = doc.getRootElement();
				List<Element> childElements = root.elements();
				for (Element child : childElements) {
					//未知属性名情况下
					List<Attribute> attributeList = child.attributes();
					////System.out.println(child.getName() + ": " + child.getText());
					for (Attribute attr : attributeList) {
					////System.out.println(attr.getName() + ": " + attr.getValue());
					}
	
		 
					//未知子元素名情况下
					List<Element> elementList = child.elements();
					for (Element ele : elementList) {
						List<Attribute> attributeList2 = ele.attributes();
						////System.out.println(ele.getName() + ": " + ele.getText());
						for (Attribute attr2 : attributeList2) {
							if (attr2.getName().equals("mailno")) {
								String mailno2= attr2.getValue();
								////System.out.println(mailno2);
							}
							// //System.out.println(attr2.getName() + ": " + attr2.getValue());
						}
						List<Element> elementList2 = ele.elements();
						for (Element ele2 : elementList2) {
							List<Attribute> attributeList3 = ele2.attributes();
							////System.out.println(ele2.getName() + ": " + ele2.getText());
							Route route =new Route();
							route.setMailno(mailno);
							for (Attribute attr3 : attributeList3) {
								if (attr3.getName().equals("remark")) {
									//Route route =new Route();
									route.setRemark(attr3.getValue());
								}
								if (attr3.getName().equals("accept_time")) {
									//Route route =new Route();
									route.setAccept_time(attr3.getValue());
								}
								if (attr3.getName().equals("accept_address")) {
									//Route route =new Route();
									route.setAccept_address(attr3.getValue());
								}
								if (attr3.getName().equals("opcode")) {
									//Route route =new Route();
									route.setOpcode(attr3.getValue());
								}
								////System.out.println(attr3.getName() + ": " + attr3.getValue());
							}
							int sum= this.routeManager.getnum(route);
							if (sum<1) {
								this.routeManager.saveroute(route);
							}
						}
					}
				}
			} else {
				EntityUtils.consume(response.getEntity());
				throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
			}
		}
	}
		//顺丰 出口到俄罗斯业务查询轨迹定时任务
		public  void routefsru() throws Exception {
			////System.out.println("顺丰定时任务");

//			String url = "http://218.17.248.244:11080/bsp-oisp/sfexpressService";//国内联调使用
			String url = urlAPI;//出口正式环境
			int port = 443;
			String  url2="http://bsp-oisp.test.sf-express.com:6080/bsp-oisp/ws/sfexpressService?wsdl";

//			String checkword = "CTZkCuwfjiVT7Bvu";//国内联调使用
			String checkword = checkWord2ru;//出口正式环境
			String xmlFile="D:\\user.xml";
			String checkword2 =	"MoZYb2Lenno3N3TLfI[;";
			String  bianma="szglkj";
			String  bianma2="BSPdevelop";
			String  bianma3="YZFKJ";//国内联调
			//月结号
			String yuejiehao =yuejiehao2ru;//出口业务正式
			//参数 end
			List<RouteOrder> routeOrderlist=  this.routeManager.getRoute();
			for (RouteOrder  routes:      routeOrderlist  ) {
					
				
				String  orderid=routes.getOrderid();
				String  mailno=routes.getMailno();
				String  mailno3="444501061775";
				String  xmls2="<Request service='RouteService' lang='zh-CN'><Head>"+yuejiehao+"</Head><Body><RouteRequest tracking_type='1' method_type='1' tracking_number='"+mailno+"'/> </Body> </Request>";
				String xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+bianma+"</Head><Body><Order orderid='"+orderid+"' j_company='yizhifu' j_contact='nan' j_tel='1244323433' j_mobile='ewr234234' j_shippercode='755VA' j_country='SG' j_province='Singapore' j_city='Singapore' j_county='Singapore' j_address='7SixthLokYangRoad#11-11Singapore628105' j_post_code='628105' d_deliverycode='852' d_country='HK' d_company='Daniel' d_contact='DanielLi' d_tel='87654321' d_mobile='87654321' d_province='Hong Kong' d_city='Hong Kong' d_county='Hong Kong' d_address='27/Ftestaddress,Kowloon,HongKong' d_post_code='852852' custid='7551878519' pay_method='1' express_type='23' parcel_quantity='1' cargo_total_weight='2.18' declared_value='1' declared_value_currency='CNY' sendstarttime='2014-12-17 07:08:38' remark='beizhu'><Cargo name='nan-ceshi2' count='20' unit='pcs' weight='0.003' amount='0.05' currency='CNY' source_area='China' /></Order></Body></Request>";
				////System.out.println(xmls);
				/*	byte[] midbytes=xmls.getBytes("utf8");
				String xml2=new String(midbytes,"utf8");
				//System.out.println(xml2);*///不能用
				////System.out.println(xmls2);
				String verifyCode=TransUtils.md5EncryptAndBase64(xmls2 + checkword);
				//	//System.out.println(verifyCode);//测试看结果
				List<NameValuePair> nvps = new ArrayList<NameValuePair>();
				nvps.add(new BasicNameValuePair("xml", xmls2));
				nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
				
				HttpClient httpclient=getHttpClient(port);
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
				HttpResponse response = httpclient.execute(httpPost);

				if (response.getStatusLine().getStatusCode() == 200){
					////System.out.println(EntityUtils.toString(response.getEntity()));//只能解析一次
					String xml8 = EntityUtils.toString(response.getEntity());
		
					SAXReader saxReader = new SAXReader();
					Document doc = null;
					
					ByteArrayInputStream is = new ByteArrayInputStream(xml8.getBytes("UTF-8"));
					doc = saxReader.read(is); 
					////System.out.println("doc2成功");	
					Element root = doc.getRootElement();
					List<Element> childElements = root.elements();
					for (Element child : childElements) {
						//未知属性名情况下
						List<Attribute> attributeList = child.attributes();
						////System.out.println(child.getName() + ": " + child.getText());
						for (Attribute attr : attributeList) {
						////System.out.println(attr.getName() + ": " + attr.getValue());
						}
		
			 
						//未知子元素名情况下
						List<Element> elementList = child.elements();
						for (Element ele : elementList) {
							List<Attribute> attributeList2 = ele.attributes();
							////System.out.println(ele.getName() + ": " + ele.getText());
							for (Attribute attr2 : attributeList2) {
								if (attr2.getName().equals("mailno")) {
									String mailno2= attr2.getValue();
									////System.out.println(mailno2);
								}
								// //System.out.println(attr2.getName() + ": " + attr2.getValue());
							}
							List<Element> elementList2 = ele.elements();
							for (Element ele2 : elementList2) {
								List<Attribute> attributeList3 = ele2.attributes();
								////System.out.println(ele2.getName() + ": " + ele2.getText());
								Route route =new Route();
								route.setMailno(mailno);
								for (Attribute attr3 : attributeList3) {
									if (attr3.getName().equals("remark")) {
										//Route route =new Route();
										route.setRemark(attr3.getValue());
									}
									if (attr3.getName().equals("accept_time")) {
										//Route route =new Route();
										route.setAccept_time(attr3.getValue());
									}
									if (attr3.getName().equals("accept_address")) {
										//Route route =new Route();
										route.setAccept_address(attr3.getValue());
									}
									if (attr3.getName().equals("opcode")) {
										//Route route =new Route();
										route.setOpcode(attr3.getValue());
									}
									////System.out.println(attr3.getName() + ": " + attr3.getValue());
								}
								int sum= this.routeManager.getnum(route);
								if (sum<1) {
									this.routeManager.saveroute(route);
								}
							}
						}
					}
				} else {
					EntityUtils.consume(response.getEntity());
					throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
				}
			}
		}
	private static HttpClient getHttpClient(int port){
		PoolingClientConnectionManager pcm = new PoolingClientConnectionManager();
		SSLContext ctx=null;
		try{
			ctx = SSLContext.getInstance("TLS");
			X509TrustManager x509=new X509TrustManager(){
				public void checkClientTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
				}
				public void checkServerTrusted(X509Certificate[] xcs, String string)
					throws CertificateException {
				}
				public X509Certificate[] getAcceptedIssuers(){
					return null;
				}
			};
			ctx.init(null, new TrustManager[]{x509}, null);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme sch = new Scheme("https", port, ssf);
		pcm.getSchemeRegistry().register(sch);
		return new DefaultHttpClient(pcm);
	}
	
	//俄速通轨迹
	public  void eroute(){
		////System.out.println("俄速通轨迹定时任务2");
		List<LogisticsInformationVo> list1=this.routeManager.getlistlogi();
		for (LogisticsInformationVo logi : list1) {
			List<CargoTrackingResponse> responseList = new ArrayList<CargoTrackingResponse>();
			List<String> list = new ArrayList<String>();
			list.add(logi.getReferenceNumber());
			//发送请求。获取响应的结果。
			responseList = toolPort.cargoTrackingService(token, list);
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
	public  void eroute2(){
		
		//	List<LogisticsInformationVo> list1=this.routeManager.getlistlogi();
		//	for (LogisticsInformationVo logi : list1) {
				List<CargoTrackingResponse> responseList = new ArrayList<CargoTrackingResponse>();
				List<String> list = new ArrayList<String>();
				list.add("1002520150422151603");
				responseList = toolPort.cargoTrackingService(token, list);
				RouteRu  routeRu=new RouteRu();
				for (CargoTrackingResponse response : responseList) {
					List<Track> trackList = response.getTracks();
					for (Track track : trackList) {
						track.getTrackInfo();
					
						routeRu.setOccur_address(track.getTrackInfo().get(0).getOccurAddress());
						//System.out.println(track.getTrackInfo().get(0).getOccurAddress());
						routeRu.setOccur_date(track.getTrackInfo().get(0).getOccurDate());
						//System.out.println(track.getTrackInfo().get(0).getOccurDate());
						routeRu.setTrack_code(track.getTrackInfo().get(0).getTrackCode());
						//System.out.println(track.getTrackInfo().get(0).getTrackCode());
					//	routeRu.setTracking_number(logi.getReferenceNumber());
						routeRu.setTrack_content(track.getTrackInfo().get(0).getTrackContent());
						//System.out.println(track.getTrackInfo().get(0).getTrackContent());
//						  int  sum= this.routeManager.getnumru(routeRu);
//						  if (sum<1) {
//					//	this.routeManager.saverouteru(routeRu);
//						  }
					}
				}
			}
}