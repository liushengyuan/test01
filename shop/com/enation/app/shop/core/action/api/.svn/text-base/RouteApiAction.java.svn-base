package com.enation.app.shop.core.action.api;
import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

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

import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.Route;
import com.enation.app.shop.core.model.RouteOrder;
import com.enation.app.shop.core.model.ordermanifest.OrderPaymentInformation;
import com.enation.app.shop.core.service.IFreightTypeManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IRouteManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.app.shop.core.utils.TransUtils;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

/**
 * 商品api
 * @author kingapex
 *2013-8-20下午8:17:14
 */


@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("route")
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class RouteApiAction extends WWAction {
	private IRouteManager routeManager;
	private IOrderManager  orderManager;
	private IFreightTypeManager freightTypeManager;
	public IFreightTypeManager getFreightTypeManager() {
		return freightTypeManager;
	}
	public void setFreightTypeManager(IFreightTypeManager freightTypeManager) {
		this.freightTypeManager = freightTypeManager;
	}
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("shunfeng");
	static String urlAPI = bundle.getString("url");
	static String checkWord2ru = bundle.getString("checkWord2ru");
	static String yuejiehao2ru = bundle.getString("yuejiehao2ru");
	
	static String checkWord2cn = bundle.getString("checkWord2cn");
	static String yuejiehao2cn = bundle.getString("yuejiehao2cn");
	
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
	//下订单  国内业务
	public  String fuwu() throws Exception {
//		String url = "https://bsp-oisp.test.sf-express.com/bsp-oisp/sfexpressService";//外网地址
		String url = urlAPI;//国内业务正式
//		String url = "http://218.17.248.244:11080/bsp-oisp/sfexpressService";//国内联调使用
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		OrderPaymentInformation orderPaymentInformation=new OrderPaymentInformation();
		String sn =request.getParameter("sn");
		String yewu =request.getParameter("yewu");
		String totalw =request.getParameter("totalw");
		String goodsname =request.getParameter("ename");
		String num =request.getParameter("num");
		String fuwu =request.getParameter("fuwu");
		/**新增寄件人信息以及业务类型jfw**/
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String region = request.getParameter("region");
		String area = request.getParameter("area");
		String j_name = request.getParameter("j_name");
		String j_mobile = request.getParameter("j_mobile");
		String express_type = request.getParameter("express_type");//顺丰国内业务1 标准快递  2 顺丰特惠 3 电商特惠 7电商速配   28 电商专配
		String j_address = province+city+region+area;//组装地址信息
		/**新增寄件人信息以及业务类型jfw**/
		String url2="http://bsp-oisp.test.sf-express.com:6080/bsp-oisp/ws/sfexpressService?wsdl";
		int port = 443;
		
		String checkword = checkWord2cn;//国内正式
//		String checkword = "CTZkCuwfjiVT7Bvu";//国内联调使用
		Order  order=  this.orderManager.get(sn);
		orderPaymentInformation.setOrder_id(order.getOrder_id());
		orderPaymentInformation.setLOGISTICSNAME("7");
		orderPaymentInformation.setLOGISTICSCODE("4413051030");
		orderPaymentInformation.setSHIPPER(j_name);
		orderPaymentInformation.setSHIPPERADDRESS(province+"-"+city+"-"+region+"-"+area);
		orderPaymentInformation.setSHIPPERTELEPHONE(j_mobile);
		orderPaymentInformation.setSHIPPERCOUNTRY("116");
		
		String  orderid="yizhifu-nan-l";
		String  mailno="234324234";
		String  bianma="szglkj";//联调
		String  bianma2="BSPdevelop";
//		String  bianma3="YZFKJ";//国内联调
		//String  chinayewu = "28";//1 标准快递  2 顺丰特惠 3 电商特惠 7电商速配   28 电商专配
		//月结号
		String   yuejiehao =yuejiehao2cn;//国内业务正式
		String xmls=null;
		if (fuwu.equals("2")   ) {//到俄国 出口
			xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+yuejiehao+"</Head><Body><Order orderid='"+sn+"' j_company='TradeEase' j_contact='caoning' j_tel='1244323433' j_mobile='23234234' j_shippercode='755VA' j_country='RU' j_province='guangdong' j_city='shen zhen' j_county='shen zhen' j_address='shen zhen cang ku' j_post_code='518000' d_deliverycode='MOW' d_country='RU' d_company='"+order.getShip_name()+"' d_contact='"+order.getShip_name()+"' d_tel='"+order.getShip_mobile()+"' d_mobile='"+order.getShip_mobile()+"' d_province='MOW' d_city='MOW' d_county='MOW' d_address='"+order.getShipping_area()+"-"+order.getShip_addr()+"' d_post_code='"+order.getShip_send_zip()+"' custid='"+yuejiehao+"' pay_method='1' express_type='"+yewu+"' parcel_quantity='1' cargo_total_weight='"+totalw+"' declared_value='1' declared_value_currency='USD' ><Cargo name='"+goodsname+"' count='"+num+"' unit='unit' weight='"+order.getWeight()+"' amount='"+order.getGoods_amount()+"' currency='USD' source_area='China' /></Order></Body></Request>";
		}else {//到中国 进口
				//d_post_code='"+order.getShip_send_zip()+"' 到方邮编，跨境必填（中国大陆，港澳台互寄除外）
			 xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+yuejiehao+"</Head><Body><Order orderid='"+sn+"' j_company='TradeEase' j_contact='"+j_name+"' j_tel='"+j_mobile+"' j_mobile='"+j_mobile+"'   j_address='"+j_address+"'   d_company='"+order.getShip_name()+"' d_contact='"+order.getShip_name()+"' d_tel='"+order.getShip_mobile()+"' d_mobile='"+order.getShip_mobile()+"'  d_address='"+order.getShipping_area()+"-"+order.getShip_addr()+"'  custid='"+yuejiehao+"' pay_method='1' express_type='"+express_type+"' parcel_quantity='1' cargo_total_weight='"+totalw+"' declared_value='1' declared_value_currency='USD' ><Cargo name='"+goodsname+"' count='"+num+"' unit='unit' weight='"+order.getWeight()+"' amount='"+order.getGoods_amount()+"' currency='USD' source_area='China' /></Order></Body></Request>";

		}
		Double price=0.0;
		 List<FreightStandard> freightStandard=freightTypeManager.getFreightStandardOfSF("3");
		     if(freightStandard.size()>0){
		    	 FreightStandard standard=freightStandard.get(0);
				  price =FreightUtls.getFreight(standard,Double.parseDouble(totalw));
		     }else{
		    	  price=0.0;
		   }
		//System.out.println(xmls);
	
		
		String verifyCode=TransUtils.md5EncryptAndBase64(xmls + checkword);
		//System.out.println(verifyCode);//测试看结果
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xmls));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
		
		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		 Map result=null;
		if (response.getStatusLine().getStatusCode() == 200){
		//	//System.out.println(EntityUtils.toString(response.getEntity()));//只能解析一次
		


			String xml8 = EntityUtils.toString(response.getEntity());

			SAXReader saxReader = new SAXReader();
			Document doc = null;
			ByteArrayInputStream is = new ByteArrayInputStream(xml8.getBytes("UTF-8"));
    doc = saxReader.read(is); 
    Element root = doc.getRootElement();
	  List<Element> childElements = root.elements();
	  for (Element child : childElements) {
	   //未知属性名情况下
	   List<Attribute> attributeList = child.attributes();
	   //System.out.println(child.getName() + ": " + child.getText());
	   for (Attribute attr : attributeList) {
	    //System.out.println(attr.getName() + ": " + attr.getValue());
	   }

	 
	   //未知子元素名情况下
	   List<Element> elementList = child.elements();
	   for (Element ele : elementList) {
		   List<Attribute> attributeList2 = ele.attributes();
		    //System.out.println(ele.getName() + ": " + ele.getText());
		    RouteOrder routeorder =new RouteOrder();
		    for (Attribute attr2 : attributeList2) {
		    //System.out.println(attr2.getName() + ": " + attr2.getValue());
		    if (attr2.getName().equals("orderid")) {
				//	Route route =new Route();
		    		routeorder.setOrderid(attr2.getValue());
				}
		    	if (attr2.getName().equals("mailno")) {
				//	Route route =new Route();
		    		routeorder.setMailno(attr2.getValue());
		    		orderPaymentInformation.setLOGISTICSNO(attr2.getValue());
				}
		    	if (attr2.getName().equals("origincode")) {
			//		Route route =new Route();
		    		routeorder.setOrigincode(attr2.getValue());
				}
		    	if (attr2.getName().equals("destcode")) {
			//		Route route =new Route();
		    		routeorder.setDestcode(attr2.getValue());
				}
		    	if (attr2.getName().equals("filter_result")) {
					//		Route route =new Route();
				    		routeorder.setFilter_result(attr2.getValue());
						}
		    	if (attr2.getName().equals("agent_mailno")) {
					//		Route route =new Route();
				    		routeorder.setAgent_mailno(attr2.getValue());
						}
		    }
		    this.routeManager.saverouteorder(routeorder);
		    this.routeManager.saveAddPaymentInformation(orderPaymentInformation);
		  
		   /* this.routeManager.updateOrderByRuston(routeorder.getMailno(), sn,totalw);*/
		    this.routeManager.updateOrderBySf(routeorder.getMailno(), sn, totalw, price);
		  
		    this.showExpressJson(routeorder.getMailno(), routeorder.getOrderid(),yewu);
			
		    List<Element> elementList2 = ele.elements();
		    for (Element ele2 : elementList2) {
		    	List<Attribute> attributeList3 = ele2.attributes();
			    //System.out.println(ele2.getName() + ": " + ele2.getText());
			  
			  
			    for (Attribute attr3 : attributeList3) {
			    	
			    //System.out.println(attr3.getName() + ": " + attr3.getValue());
			    }
			  
			    
		    }
	   }}
	 
		
		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
		return this.JSON_MESSAGE;
	}
	
	//下订单  出口到俄罗斯
	public  String fuwu2ru() throws Exception {
//		String url = "https://bsp-oisp.test.sf-express.com/bsp-oisp/sfexpressService";//外网地址
		String url = urlAPI;//出口到俄罗斯正式
//		String url = "http://218.17.248.244:11080/bsp-oisp/sfexpressService";//国内联调使用
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sn =request.getParameter("sn");
		String yewu =request.getParameter("yewu");
		String totalw =request.getParameter("totalw");
		String goodsname =request.getParameter("ename");
		String num =request.getParameter("num");
		String fuwu =request.getParameter("fuwu");
		/**新增寄件人信息以及业务类型jfw**/
		String province = request.getParameter("province");
		String city = request.getParameter("city");
		String region = request.getParameter("region");
		String area = request.getParameter("area");
		String j_name = request.getParameter("j_name");
		String j_mobile = request.getParameter("j_mobile");
		//String express_type = request.getParameter("express_type");//俄罗斯业务 ：9平邮 10挂号 顺丰国内业务1 标准快递  2 顺丰特惠 3 电商特惠 7电商速配   28 电商专配
		String j_address = province+city+region+area;//组装地址信息
		String express_type = "9";
		if(yewu.equals("P")){
			express_type ="9";
		}else {
			express_type ="10";
		}
		/**新增寄件人信息以及业务类型jfw**/
		String url2="http://bsp-oisp.test.sf-express.com:6080/bsp-oisp/ws/sfexpressService?wsdl";
		int port = 443;
		

		String checkword = checkWord2ru;//出口到俄罗斯正式
//		String checkword = "CTZkCuwfjiVT7Bvu";//国内联调使用
		Order  order=  this.orderManager.get(sn);
		String  orderid="yizhifu-nan-l";
		String  mailno="234324234";
		String  bianma="szglkj";//联调
		String  bianma2="BSPdevelop";
//		String  bianma3="YZFKJ";//国内联调
		//String  chinayewu = "28";//1 标准快递  2 顺丰特惠 3 电商特惠 7电商速配   28 电商专配
		//月结号
		String   yuejiehao =yuejiehao2ru;//出口到俄罗斯正式
		String xmls=null;
		if (fuwu.equals("2")   ) {//到俄国 出口
			xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+yuejiehao+"</Head><Body><Order orderid='"+sn+"' j_company='TradeEase' j_contact='"+j_name+"' j_tel='"+j_mobile+"' j_mobile='"+j_mobile+"' j_shippercode='755VA' j_country='CN' j_province='"+province+"' j_city='"+city+"' j_county='"+region+"' j_address='"+area+"' j_post_code='518000' d_deliverycode='MOW' d_country='RU' d_company='"+order.getShip_name()+"' d_contact='"+order.getShip_name()+"' d_tel='"+order.getShip_mobile()+"' d_mobile='"+order.getShip_mobile()+"' d_province='MOW' d_city='MOW' d_county='MOW' d_address='"+order.getShipping_area()+"-"+order.getShip_addr()+"' d_post_code='"+order.getShip_send_zip()+"' custid='"+yuejiehao+"' pay_method='1' express_type='"+express_type+"' parcel_quantity='1' cargo_total_weight='"+totalw+"' declared_value='1' declared_value_currency='USD' ><Cargo name='"+goodsname+"' count='"+num+"' unit='unit' weight='"+order.getWeight()+"' amount='"+order.getGoods_amount()+"' currency='USD' source_area='China' /></Order></Body></Request>";
		}else {//到中国 进口
				//d_post_code='"+order.getShip_send_zip()+"' 到方邮编，跨境必填（中国大陆，港澳台互寄除外）
			 //xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+bianma3+"</Head><Body><Order orderid='"+sn+"' j_company='TradeEase' j_contact='"+j_name+"' j_tel='"+j_mobile+"' j_mobile='"+j_mobile+"'   j_address='"+j_address+"'   d_company='"+order.getShip_name()+"' d_contact='"+order.getShip_name()+"' d_tel='"+order.getShip_mobile()+"' d_mobile='"+order.getShip_mobile()+"'  d_address='"+order.getShipping_area()+"-"+order.getShip_addr()+"'  custid='"+yuejiehao+"' pay_method='1' express_type='"+express_type+"' parcel_quantity='1' cargo_total_weight='"+totalw+"' declared_value='1' declared_value_currency='USD' ><Cargo name='"+goodsname+"' count='"+num+"' unit='unit' weight='"+order.getWeight()+"' amount='"+order.getGoods_amount()+"' currency='USD' source_area='China' /></Order></Body></Request>";

		}
		
		////System.out.println(xmls);
	
		
		String verifyCode=TransUtils.md5EncryptAndBase64(xmls + checkword);
		////System.out.println(verifyCode);//测试看结果
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xmls));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
		
		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);
		 Map result=null;
		if (response.getStatusLine().getStatusCode() == 200){
		//	//System.out.println(EntityUtils.toString(response.getEntity()));//只能解析一次
		


			String xml8 = EntityUtils.toString(response.getEntity());

			SAXReader saxReader = new SAXReader();
			Document doc = null;

    doc = saxReader.read(new ByteArrayInputStream(xml8.getBytes())); 
    Element root = doc.getRootElement();
	  List<Element> childElements = root.elements();
	  for (Element child : childElements) {
	   //未知属性名情况下
	   List<Attribute> attributeList = child.attributes();
	   //System.out.println(child.getName() + ": " + child.getText());
	   for (Attribute attr : attributeList) {
	    //System.out.println(attr.getName() + ": " + attr.getValue());
	   }

	 
	   //未知子元素名情况下
	   List<Element> elementList = child.elements();
	   for (Element ele : elementList) {
		   List<Attribute> attributeList2 = ele.attributes();
		    //System.out.println(ele.getName() + ": " + ele.getText());
		    RouteOrder routeorder =new RouteOrder();
		    for (Attribute attr2 : attributeList2) {
		    //System.out.println(attr2.getName() + ": " + attr2.getValue());
		 
		 
		    if (attr2.getName().equals("orderid")) {
				//	Route route =new Route();
		    		routeorder.setOrderid(attr2.getValue());
				}
		    	if (attr2.getName().equals("mailno")) {
				//	Route route =new Route();
		    		routeorder.setMailno(attr2.getValue());
				}
		    	if (attr2.getName().equals("origincode")) {
			//		Route route =new Route();
		    		routeorder.setOrigincode(attr2.getValue());
				}
		    	if (attr2.getName().equals("destcode")) {
			//		Route route =new Route();
		    		routeorder.setDestcode(attr2.getValue());
				}
		    	if (attr2.getName().equals("filter_result")) {
					//		Route route =new Route();
				    		routeorder.setFilter_result(attr2.getValue());
						}
		    	if (attr2.getName().equals("agent_mailno")) {
					//		Route route =new Route();
				    		routeorder.setAgent_mailno(attr2.getValue());
						}
		    }
		    this.routeManager.saverouteorder(routeorder);
		  
		    this.routeManager.updateOrderByRuston(routeorder.getMailno(), sn,totalw);
		  
		    this.showExpressJson(routeorder.getMailno(), routeorder.getOrderid(),yewu);
			
		    List<Element> elementList2 = ele.elements();
		    for (Element ele2 : elementList2) {
		    	List<Attribute> attributeList3 = ele2.attributes();
			    //System.out.println(ele2.getName() + ": " + ele2.getText());
			  
			  
			    for (Attribute attr3 : attributeList3) {
			    	
			    //System.out.println(attr3.getName() + ": " + attr3.getValue());
			    }
			  
			    
		    }
	   }}
	 
		
		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
		return this.JSON_MESSAGE;
	}
	
	private void showExpressJson(String referenceNumber, String trackingNumber,String yewu) {
		if (StringUtil.isEmpty(referenceNumber))
			this.json = "{\"result\":1}";
		else
			this.json = "{\"result\":1,\"mailno\":\""
					+ referenceNumber + "\",\"sn\":\""
					+ trackingNumber + "\",\"yewu\":\""
					+ yewu + "\"}";
	}

	//确认订单
	public  void fuwu3() throws Exception {
		//参数 begin
		//验证IP的URL
//		String url = "https://bsp-test.sf-express.com:9443/bsp-ois/sfexpressService";//外网地址
//		String url = "https://10.0.78.14/bsp-ois/sfexpressService";//顺丰内网地址
		//不验证IP的URL
		String url = "https://bsp-oisp.test.sf-express.com/bsp-oisp/sfexpressService";//外网地址
	//	String url = "https://10.0.78.9/bsp-oisp/sfexpressService";//顺丰内网地址
	//	int port = 9443;//不能用
		int port = 443;
		String  url2="http://bsp-oisp.test.sf-express.com:6080/bsp-oisp/ws/sfexpressService?wsdl";
		String checkword = "j8DzkIFgmlomPt0aLuwU";//"PAYHZH    Hi9go87nCbFEuRWC";
	//	String checkword2 = "Hi9go87nCbFEuRWC";//"PAYHZH    Hi9go87nCbFEuRWC";不能用
		String checkword2 =	"MoZYb2Lenno3N3TLfI[;";
		String  bianma="szglkj";
		String  bianma2="BSPdevelop";
		String xmlFile="D:\\user.xml";
		//参数 end
		String xmlFile2="D:\\ceshi.xml";
		String xmlFile3="D:\\cs.xml";
		String xml=TransUtils.loadFile(xmlFile2);
		//System.out.println(xml);
		//System.out.println("111");
		String xml4=xml;
		//System.out.println(xml4);
		//System.out.println("222");
		String  orderid="yizhifu-nan-2";
		String  mailno="234324234";
		String  xmls2="<Request service='RouteService' lang='zh-CN'><Head>"+bianma+"</Head><Body><RouteRequest tracking_type='1' method_type='1' tracking_number='444003077898'/> </Body> </Request>";
		String  xmls3="<Request service='OrderConfirmService' lang='zh-CN'><Head>"+bianma+"</Head><Body><OrderConfirm orderid='"+orderid+"' dealtype='1'/></Body></Request>";
		String xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+bianma+"</Head><Body><Order orderid='"+orderid+"' j_company='yizhifu' j_contact='nan' j_tel='1244323433' j_mobile='ewr234234' j_shippercode='755VA' j_country='SG' j_province='Singapore' j_city='Singapore' j_county='Singapore' j_address='7SixthLokYangRoad#11-11Singapore628105' j_post_code='628105' d_deliverycode='852' d_country='HK' d_company='Daniel' d_contact='DanielLi' d_tel='87654321' d_mobile='87654321' d_province='Hong Kong' d_city='Hong Kong' d_county='Hong Kong' d_address='27/Ftestaddress,Kowloon,HongKong' d_post_code='852852' custid='7551878519' pay_method='1' express_type='23' parcel_quantity='1' cargo_total_weight='2.18' declared_value='1' declared_value_currency='CNY' sendstarttime='2014-12-17 07:08:38' remark='beizhu'><Cargo name='nan-ceshi2' count='20' unit='pcs' weight='0.003' amount='0.05' currency='CNY' source_area='China' /></Order></Body></Request>";
		//System.out.println(xmls);
	/*	byte[] midbytes=xmls.getBytes("utf8");
		String xml2=new String(midbytes,"utf8");
		//System.out.println(xml2);*///不能用
		
		String verifyCode=TransUtils.md5EncryptAndBase64(xmls3 + checkword2);
		//System.out.println(verifyCode);//测试看结果
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xmls3));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
		
		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == 200){
			//System.out.println(EntityUtils.toString(response.getEntity()));//只能解析一次
		
		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
	}
	//查路由
	public  void fuwu2() throws Exception {
		//参数 begin
		//验证IP的URL
//		String url = "https://bsp-test.sf-express.com:9443/bsp-ois/sfexpressService";//外网地址
//		String url = "https://10.0.78.14/bsp-ois/sfexpressService";//顺丰内网地址
		//不验证IP的URL
		String url = "https://bsp-oisp.test.sf-express.com/bsp-oisp/sfexpressService";//外网地址
	//	String url = "https://10.0.78.9/bsp-oisp/sfexpressService";//顺丰内网地址
	//	int port = 9443;//不能用
		int port = 443;
		String  url2="http://bsp-oisp.test.sf-express.com:6080/bsp-oisp/ws/sfexpressService?wsdl";
		String checkword = "j8DzkIFgmlomPt0aLuwU";//"PAYHZH    Hi9go87nCbFEuRWC";
	//	String checkword2 = "Hi9go87nCbFEuRWC";//"PAYHZH    Hi9go87nCbFEuRWC";不能用
		String xmlFile="D:\\user.xml";
		String checkword2 =	"MoZYb2Lenno3N3TLfI[;";
		String  bianma="szglkj";
		String  bianma2="BSPdevelop";
		//参数 end
		
		String  orderid="yizhifu-nan-1";
		String  mailno="234324234";
		String  mailno3="444031881543";
		String  xmls2="<Request service='RouteService' lang='zh-CN'><Head>"+bianma2+"</Head><Body><RouteRequest tracking_type='1' method_type='1' tracking_number='"+mailno3+"'/> </Body> </Request>";
		String xmls="<?xml version='1.0' encoding='UTF-8'?><Request service='OrderService' lang='zh-CN'><Head>"+bianma+"</Head><Body><Order orderid='"+orderid+"' j_company='yizhifu' j_contact='nan' j_tel='1244323433' j_mobile='ewr234234' j_shippercode='755VA' j_country='SG' j_province='Singapore' j_city='Singapore' j_county='Singapore' j_address='7SixthLokYangRoad#11-11Singapore628105' j_post_code='628105' d_deliverycode='852' d_country='HK' d_company='Daniel' d_contact='DanielLi' d_tel='87654321' d_mobile='87654321' d_province='Hong Kong' d_city='Hong Kong' d_county='Hong Kong' d_address='27/Ftestaddress,Kowloon,HongKong' d_post_code='852852' custid='7551878519' pay_method='1' express_type='23' parcel_quantity='1' cargo_total_weight='2.18' declared_value='1' declared_value_currency='CNY' sendstarttime='2014-12-17 07:08:38' remark='beizhu'><Cargo name='nan-ceshi2' count='20' unit='pcs' weight='0.003' amount='0.05' currency='CNY' source_area='China' /></Order></Body></Request>";
		//System.out.println(xmls);
	/*	byte[] midbytes=xmls.getBytes("utf8");
		String xml2=new String(midbytes,"utf8");
		//System.out.println(xml2);*///不能用
		
		String verifyCode=TransUtils.md5EncryptAndBase64(xmls2 + checkword);
		//System.out.println(verifyCode);//测试看结果
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("xml", xmls2));
		nvps.add(new BasicNameValuePair("verifyCode", verifyCode));
		
		HttpClient httpclient=getHttpClient(port);
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		HttpResponse response = httpclient.execute(httpPost);

		if (response.getStatusLine().getStatusCode() == 200){
		//	//System.out.println(EntityUtils.toString(response.getEntity()));//只能解析一次
	
			


			String xml8 = EntityUtils.toString(response.getEntity());

			SAXReader saxReader = new SAXReader();
			Document doc = null;

    doc = saxReader.read(new ByteArrayInputStream(xml8.getBytes())); 
    Element root = doc.getRootElement();
	  List<Element> childElements = root.elements();
	  for (Element child : childElements) {
	   //未知属性名情况下
	   List<Attribute> attributeList = child.attributes();
	   //System.out.println(child.getName() + ": " + child.getText());
	   for (Attribute attr : attributeList) {
	    //System.out.println(attr.getName() + ": " + attr.getValue());
	   }

	 
	   //未知子元素名情况下
	   List<Element> elementList = child.elements();
	   for (Element ele : elementList) {
		   List<Attribute> attributeList2 = ele.attributes();
		    //System.out.println(ele.getName() + ": " + ele.getText());
		    for (Attribute attr2 : attributeList2) {
		    	if (attr2.getName().equals("mailno")) {
		    		String mailno2= attr2.getValue();
		    		//System.out.println(mailno2);
				}
		    	
		    //System.out.println(attr2.getName() + ": " + attr2.getValue());
		    }
		    List<Element> elementList2 = ele.elements();
		    for (Element ele2 : elementList2) {
		    	List<Attribute> attributeList3 = ele2.attributes();
			    //System.out.println(ele2.getName() + ": " + ele2.getText());
			    Route route =new Route();
			    route.setMailno(mailno);
			    for (Attribute attr3 : attributeList3) {
			    	if (attr3.getName().equals("remark")) {
					//	Route route =new Route();
						route.setRemark(attr3.getValue());
					}
			    	if (attr3.getName().equals("accept_time")) {
					//	Route route =new Route();
						route.setAccept_time(attr3.getValue());
					}
			    	if (attr3.getName().equals("accept_address")) {
				//		Route route =new Route();
						route.setAccept_address(attr3.getValue());
					}
			    	if (attr3.getName().equals("opcode")) {
				//		Route route =new Route();
						route.setOpcode(attr3.getValue());
					}
			    //System.out.println(attr3.getName() + ": " + attr3.getValue());
			    }
			  
			    this.routeManager.saveroute(route);
		    }
	   }}
		
		} else {
			EntityUtils.consume(response.getEntity());
			throw new RuntimeException("response status error: " + response.getStatusLine().getStatusCode());
		}
	}
	
	public IRouteManager getRouteManager() {
		return routeManager;
	}
	public void setRouteManager(IRouteManager routeManager) {
		this.routeManager = routeManager;
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	
	
	
	
}
