package com.enation.app.shop.core.action.backend;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.ordermanifest.Goodsinformation;
import com.enation.app.shop.core.model.ordermanifest.OrderPaymentInformation;
import com.enation.app.shop.core.model.ordermanifest.Savexml;
import com.enation.app.shop.core.service.IOrderInformationManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.eop.SystemSetting;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;
import com.opensymphony.util.Data;

@Component
@ParentPackage("eop_default")
@Namespace("/shop/admin")
@Action("orderinformation")
@Results({
	 @Result(name="showInformation",type="freemarker", location="/shop/admin/orderinformation/showInformation.html"),
	 @Result(name="showOffineInformation",type="freemarker", location="/shop/admin/orderinformation/showOffineInformation.html"),
	 @Result(name="lookShowXmlInformation",type="freemarker", location="/shop/admin/orderinformation/lookShowXmlInformation.html")
})
public class OrderInformationAction extends WWAction {
	private IOrderInformationManager orderInformationManager;
	private IOrderManager orderManager;
	private IMemberManager memberManager;
	private Integer order_id;
	private Integer id;
    public String showInformation(){
    	return "showInformation";
    }
    public String showOffineInformation(){
    	return "showOffineInformation";
    }
    public String listShowInformation(){
    	 HashMap<String, String> map = new HashMap<String,String>();
    	this.webpage = this.orderInformationManager.getInformation(map,this.getPage(), this.getPageSize());
		this.showGridJson(this.webpage);
		return this.JSON_MESSAGE;
    }
    public String listShowOffineInformation(){
    	 HashMap<String, String> map = new HashMap<String,String>();
    	 map.put("order_id", this.order_id+"");
     	this.webpage = this.orderInformationManager.getOffineInformation(map,this.getPage(), this.getPageSize());
 		this.showGridJson(this.webpage);
 		return this.JSON_MESSAGE;
    }
    public String listlookShowXmlInformation(){
    	HashMap<String, String> map = new HashMap<String,String>();
   	    map.put("order_id", this.order_id+"");
    	this.webpage = this.orderInformationManager.getXmlInformation(map,this.getPage(), this.getPageSize());
		this.showGridJson(this.webpage);
		return this.JSON_MESSAGE;
    }
    public String lookShowXmlInformation(){
    	return "lookShowXmlInformation";
    }
    public String createXmlDocument(){
    	try {
    		Integer count=this.orderInformationManager.getXmlCount();
			List<Map> list=(List<Map>)this.orderInformationManager.getInformtionByXml(id);
			String xml="";
			for (Map map : list) {
				String CUSTOMSCODE=(String) map.get("CUSTOMSCODE");
				String  BIZTYPE=(String) map.get("BIZTYPE");
				Long BIZTIME=Long.valueOf(map.get("BIZTIME").toString());
				String ECPCODE=(String) map.get("ECPCODE");
				String ECPNAME=(String) map.get("ECPNAME");
				String CBECODE=(String) map.get("CBECODE");
				String CBENAME=(String) map.get("CBENAME");
				String ORDERNO=(String) map.get("ORDERNO");
				String shipname=(String) map.get("ship_name");
				Integer order_id=Integer.parseInt(map.get("ziorder_id").toString());
				List<Goodsinformation> goodsinformations=this.orderInformationManager.getGoodsInformation(order_id);
			   xml+="<?xml version=\"1.0\" encoding=\"utf-8\"?><CBECMESSAGE><MESSAGEHEAD>";
			   xml+="<MESSAGEID>"+creatUUID()+"</MESSAGEID ><MESSAGETYPE>EPT301</MESSAGETYPE>";
			   xml+="<SENDERID>DP06374253</SENDERID><RECEIVERID>0100</RECEIVERID><SENDTIME>"+DateUtil.toString(new Date().getTime(),"yyyy-MM-dd")+"T"+DateUtil.toString(new Date().getTime(),"HH:mm:ss")+"</SENDTIME></MESSAGEHEAD><MESSAGEBODY>";
			   xml+="<BODYMASTER><CUSTOMSCODE>"+CUSTOMSCODE+"</CUSTOMSCODE><BIZTYPE>"+BIZTYPE+"</BIZTYPE><BIZTIME>"+DateUtil.toString(BIZTIME,"yyyy-MM-dd")+"T"+DateUtil.toString(BIZTIME,"HH:mm:ss")+"</BIZTIME><IEFLAG>I</IEFLAG>";
			   xml+="<ECPCODE>"+ECPCODE+"</ECPCODE><ECPNAME>"+ECPNAME+"</ECPNAME><CBECODE>"+CBECODE+"</CBECODE><CBENAME>"+CBENAME+"</CBENAME>";
			   xml+="<ORDERNO>"+ORDERNO+"</ORDERNO><CUSTOMER>"+shipname+"</CUSTOMER><CUSTOMERID></CUSTOMERID><NOTE></NOTE></BODYMASTER>";
			   for (Goodsinformation goodsinformation : goodsinformations) {
				   String GOODSNO=goodsinformation.getGOODSNO();
				   String goodsname=goodsinformation.getGOODSNAME();
				   String GOODSMODEL=goodsinformation.getGOODSMODEL();
				   String CODETS=goodsinformation.getCODETS();
				   String COUNTRY=goodsinformation.getCOUNTRY();
				   String CURRENCY=goodsinformation.getCURRENCY();
				   String UNIT=goodsinformation.getUNIT();
				   Integer QUANTITY=goodsinformation.getQUANTITY();
				   Double PRICE=goodsinformation.getPRICE();
				   xml+="<GOODSLIST><GOODSNO>"+GOODSNO+"</GOODSNO><GOODSNAME>"+goodsname+"</GOODSNAME><GOODSMODEL>"+GOODSMODEL+"</GOODSMODEL><BARCODE></BARCODE><CODETS>"+CODETS+"</CODETS><COUNTRY>"+COUNTRY+"</COUNTRY><CURRENCY>"+CURRENCY+"</CURRENCY><UNIT>"+UNIT+"</UNIT><QUANTITY>"+QUANTITY+"</QUANTITY><PRICE>"+PRICE+"</PRICE><DISCOUNT></DISCOUNT><FLAG>N</FLAG><TAXRATE></TAXRATE></GOODSLIST>";
			    }
			   Double CHARGE=Double.valueOf(map.get("CHARGE").toString());
			   Double GOODSVALUE=Double.valueOf(map.get("GOODSVALUE").toString());
			   Double OTHERVALUE=Double.valueOf(map.get("OTHERVALUE").toString());
			   Double TAX=Double.valueOf(map.get("TAX").toString());
			   String PAYMENTCODE=(String)map.get("PAYMENTCODE");
			   String PAYMENTNAME=(String)map.get("PAYMENTNAME");
			   String PAYMENTNO=(String)map.get("PAYMENTNO");
			   xml+="<CHARGEDATA><CHARGE>"+CHARGE+"</CHARGE><GOODSVALUE>"+GOODSVALUE+"</GOODSVALUE><OTHERVALUE>"+OTHERVALUE+"</OTHERVALUE><TAX>"+TAX+"</TAX></CHARGEDATA><PAYMENTDATA><PAYMENTCODE>"+PAYMENTCODE+"</PAYMENTCODE><PAYMENTNAME>"+PAYMENTNAME+"</PAYMENTNAME><PAYMENTNO>"+PAYMENTNO+"</PAYMENTNO></PAYMENTDATA>";
			   OrderPaymentInformation paymentInformation=this.orderInformationManager.getPaymentInformation(order_id);
			   if(paymentInformation!=null){
				   String LOGISTICSCODE=paymentInformation.getLOGISTICSCODE();
				   String LOGISTICSNAME=paymentInformation.getLOGISTICSNAME();
				   String LOGISTICSNO=paymentInformation.getLOGISTICSNO();
				   String SHIPPER=paymentInformation.getSHIPPER();
				   String SHIPPERADDRESS=paymentInformation.getSHIPPERADDRESS();
				   String SHIPPERTELEPHONE=paymentInformation.getSHIPPERTELEPHONE();
				   String SHIPPERCOUNTRY=paymentInformation.getSHIPPERCOUNTRY();
				    xml+="<LOGISTICSDATA><LOGISTICSCODE>"+LOGISTICSCODE+"</LOGISTICSCODE><LOGISTICSNAME>"+LOGISTICSNAME+"</LOGISTICSNAME><LOGISTICSNO>"+LOGISTICSNO+"</LOGISTICSNO></LOGISTICSDATA>";
				    xml+="<SHIPPERDATA><SHIPPER>"+SHIPPER+"</SHIPPER><SHIPPERADDRESS>"+SHIPPERADDRESS+"</SHIPPERADDRESS><SHIPPERTELEPHONE>"+SHIPPERTELEPHONE+"</SHIPPERTELEPHONE><SHIPPERCOUNTRY>"+SHIPPERCOUNTRY+"</SHIPPERCOUNTRY></SHIPPERDATA>"; 
			   }
			    String CONSIGNEE=(String)map.get("CONSIGNEE");
			    String CONSIGNEEADDRESS=(String)map.get("CONSIGNEEADDRESS");
			    String CONSIGNEETELEPHONE=(String)map.get("CONSIGNEETELEPHONE");
			    String CONSIGNEECOUNTRY=(String)map.get("CONSIGNEECOUNTRY");
			    xml+="<CONSIGNEEDATA><CONSIGNEE>"+CONSIGNEE+"</CONSIGNEE><CONSIGNEEADDRESS>"+CONSIGNEEADDRESS+"</CONSIGNEEADDRESS><CONSIGNEETELEPHONE>"+CONSIGNEETELEPHONE+"</CONSIGNEETELEPHONE><CONSIGNEECOUNTRY>"+CONSIGNEECOUNTRY+"</CONSIGNEECOUNTRY></CONSIGNEEDATA><TOTALDATA><TOTALAMOUNT>0</TOTALAMOUNT><TOTALQUANTITY>0</TOTALQUANTITY><NOTE></NOTE></TOTALDATA></MESSAGEBODY></CBECMESSAGE>";
			    String filename="";
				HttpServletRequest request = ServletActionContext.getRequest();
				String savePath = request.getRealPath("/")+"test/xml/";
				File test = new File(savePath);
				if(!test.exists()){
					test.mkdirs();
				}
				String ymd=DateUtil.toString(new Date().getTime(),"yyyyMMddHHmmss");
				filename+=savePath+ymd+"EPT301.xml";
				Savexml savexml=new Savexml();
				savexml.setCreate_time(DateUtil.getDateline());
				savexml.setFilename(ymd+"EPT301.xml");
				savexml.setOrder_id(id);
				savexml.setSn(this.orderManager.get(id).getSn());
				savexml.setIs_true(0);
				savexml.setIs_count(count+1);
				this.orderInformationManager.saveXmlInformation(savexml);
				System.out.println(xml.toString());
				saveFile(filename, xml.toString());
			}
			String xml1="";
			for (Map map : list) {
				String CUSTOMSCODE=(String) map.get("CUSTOMSCODE");
				String  BIZTYPE=(String) map.get("BIZTYPE");
				Long BIZTIME=Long.valueOf(map.get("BIZTIME").toString());
				String ORDERNO=(String) map.get("ORDERNO");
				 xml1+="<?xml version=\"1.0\" encoding=\"utf-8\"?><CBECMESSAGE><MESSAGEHEAD>";
				 xml1+="<MESSAGEID>"+creatUUID()+"</MESSAGEID ><MESSAGETYPE>EPT401</MESSAGETYPE>";
				 xml1+="<SENDERID>DP06374253</SENDERID><RECEIVERID>0100</RECEIVERID><SENDTIME>"+DateUtil.toString(new Date().getTime(),"yyyy-MM-dd")+"T"+DateUtil.toString(new Date().getTime(),"HH:mm:ss")+"</SENDTIME></MESSAGEHEAD><MESSAGEBODY>";
				 xml1+="<BODYMASTER><CUSTOMSCODE>"+CUSTOMSCODE+"</CUSTOMSCODE><BIZTYPE>"+BIZTYPE+"</BIZTYPE><BIZTIME>"+DateUtil.toString(BIZTIME,"yyyy-MM-dd")+"T"+DateUtil.toString(BIZTIME,"HH:mm:ss")+"</BIZTIME><ORDERNO>"+ORDERNO+"</ORDERNO>";
				 String PAYMENTCODE=(String)map.get("PAYMENTCODE");
				 String PAYMENTNAME=(String)map.get("PAYMENTNAME");
				 String PAYMENTNO=(String)map.get("PAYMENTNO");
				 Double CHARGE=Double.valueOf(map.get("CHARGE").toString());
				 Double GOODSVALUE=Double.valueOf(map.get("GOODSVALUE").toString());
				 Double OTHERVALUE=Double.valueOf(map.get("OTHERVALUE").toString());
				 Double TAX=Double.valueOf(map.get("TAX").toString());
				 String currency="142";
				 xml1+="<PAYMENTCODE>"+PAYMENTCODE+"</PAYMENTCODE><PAYMENTNAME>"+PAYMENTNAME+"</PAYMENTNAME><PAYMENTNO>"+PAYMENTNO+"</PAYMENTNO><CURRENCY>"+currency+"</CURRENCY>";
				 xml1+="<CHARGE>"+CHARGE+"</CHARGE><GOODSVALUE>"+GOODSVALUE+"</GOODSVALUE><OTHERVALUE>"+OTHERVALUE+"</OTHERVALUE><TAX>"+TAX+"</TAX>";
			     xml1+="<NOTE></NOTE></BODYMASTER></MESSAGEBODY></CBECMESSAGE>";
			        String filename="";
					HttpServletRequest request = ServletActionContext.getRequest();
					String savePath = request.getRealPath("/")+"test/xml/";
					File test = new File(savePath);
					if(!test.exists()){
						test.mkdirs();
					}
					String ymd=DateUtil.toString(new Date().getTime(),"yyyyMMddHHmmss");
					filename+=savePath+ymd+"EPT401.xml";
					Savexml savexml=new Savexml();
					savexml.setCreate_time(DateUtil.getDateline());
					savexml.setFilename(ymd+"EPT401.xml");
					savexml.setOrder_id(id);
					savexml.setSn(this.orderManager.get(id).getSn());
					savexml.setIs_true(1);
					savexml.setIs_count(count+1);
					this.orderInformationManager.saveXmlInformation(savexml);
					System.out.println(xml1.toString());
					saveFile(filename, xml1.toString());
			}
			String xml2="";
			for (Map map : list) {
				String ORDERNOnew=(String) map.get("ORDERNO");//额外修改的订单号
				String CUSTOMSCODE=(String) map.get("CUSTOMSCODE");
				String  BIZTYPE=(String) map.get("BIZTYPE");
				Long BIZTIME=Long.valueOf(map.get("BIZTIME").toString());
				xml2+="<?xml version=\"1.0\" encoding=\"utf-8\"?><CBECMESSAGE><MESSAGEHEAD>";
				xml2+="<MESSAGEID>"+creatUUID()+"</MESSAGEID ><MESSAGETYPE>EPT501</MESSAGETYPE>";
				xml2+="<SENDERID>DP06374253</SENDERID><RECEIVERID>0100</RECEIVERID><SENDTIME>"+DateUtil.toString(new Date().getTime(),"yyyy-MM-dd")+"T"+DateUtil.toString(new Date().getTime(),"HH:mm:ss")+"</SENDTIME></MESSAGEHEAD><MESSAGEBODY>";
				xml2+="<BODYMASTER><CUSTOMSCODE>"+CUSTOMSCODE+"</CUSTOMSCODE><BIZTYPE>"+BIZTYPE+"</BIZTYPE><BIZTIME>"+DateUtil.toString(BIZTIME,"yyyy-MM-dd")+"T"+DateUtil.toString(BIZTIME,"HH:mm:ss")+"</BIZTIME>";
				Integer order_id=Integer.parseInt(map.get("ziorder_id").toString());
				OrderPaymentInformation paymentInformation=this.orderInformationManager.getPaymentInformation(order_id);
				   if(paymentInformation!=null){
					   Order order=this.orderManager.get(paymentInformation.getOrder_id());
					   /*String ORDERNO=order.getSn();*/
					   String LOGISTICSCODE=paymentInformation.getLOGISTICSCODE();
					   String LOGISTICSNAME=paymentInformation.getLOGISTICSNAME();
					   String LOGISTICSNO=paymentInformation.getLOGISTICSNO();
					   String SHIPPER=paymentInformation.getSHIPPER();
					   String SHIPPERADDRESS=paymentInformation.getSHIPPERADDRESS();
					   String SHIPPERTELEPHONE=paymentInformation.getSHIPPERTELEPHONE();
					   String SHIPPERCOUNTRY=paymentInformation.getSHIPPERCOUNTRY();
					   xml2+="<ORDERNO>"+ORDERNOnew+"</ORDERNO><LOGISTICSCODE>"+LOGISTICSCODE+"</LOGISTICSCODE><LOGISTICSNAME>"+LOGISTICSNAME+"</LOGISTICSNAME><LOGISTICSNO>"+LOGISTICSNO+"</LOGISTICSNO><NOTE></NOTE></BODYMASTER>";
				       xml2+="<DELIVERYDATA><FREIGHT></FREIGHT><INSUREDFEES>0</INSUREDFEES>";
				       List<OrderItem> items=this.orderManager.listGoodsItems(paymentInformation.getOrder_id());
				       OrderItem orderItem=items.get(0);
				       xml2+="<WEIGHT>"+order.getWeight()+"</WEIGHT><QUANTITY>"+orderItem.getNum()+"</QUANTITY><TRAFNAME></TRAFNAME>";
				       xml2+="<DESTPORTCODE>0129</DESTPORTCODE><PORTCODE>1480</PORTCODE><GOODSINFO>无</GOODSINFO></DELIVERYDATA>";
				       xml2+="<SHIPPERDATA><SHIPPER>"+SHIPPER+"</SHIPPER><SHIPPERADDRESS>"+SHIPPERADDRESS+"</SHIPPERADDRESS><SHIPPERTELEPHONE>"+SHIPPERTELEPHONE+"</SHIPPERTELEPHONE><SHIPPERCOUNTRY>"+SHIPPERCOUNTRY+"</SHIPPERCOUNTRY></SHIPPERDATA>";
				   }
				   String CONSIGNEE=(String)map.get("CONSIGNEE");
				   String CONSIGNEEADDRESS=(String)map.get("CONSIGNEEADDRESS");
				   String CONSIGNEETELEPHONE=(String)map.get("CONSIGNEETELEPHONE");
				   String CONSIGNEECOUNTRY=(String)map.get("CONSIGNEECOUNTRY");
				   xml2+="<CONSIGNEEDATA><CONSIGNEE>"+CONSIGNEE+"</CONSIGNEE><CONSIGNEEADDRESS>"+CONSIGNEEADDRESS+"</CONSIGNEEADDRESS><CONSIGNEETELEPHONE>"+CONSIGNEETELEPHONE+"</CONSIGNEETELEPHONE><CONSIGNEECOUNTRY>"+CONSIGNEECOUNTRY+"</CONSIGNEECOUNTRY></CONSIGNEEDATA></MESSAGEBODY></CBECMESSAGE>";
				   String filename="";
					HttpServletRequest request = ServletActionContext.getRequest();
					String savePath = request.getRealPath("/")+"test/xml/";
					File test = new File(savePath);
					if(!test.exists()){
						test.mkdirs();
					}
					String ymd=DateUtil.toString(new Date().getTime(),"yyyyMMddHHmmss");
					filename+=savePath+ymd+"EPT501.xml";
					Savexml savexml=new Savexml();
					savexml.setCreate_time(DateUtil.getDateline());
					savexml.setFilename(ymd+"EPT501.xml");
					savexml.setOrder_id(id);
					savexml.setSn(this.orderManager.get(id).getSn());
					savexml.setIs_true(2);
					savexml.setIs_count(count+1);
					this.orderInformationManager.saveXmlInformation(savexml);
					System.out.println(xml2.toString());
					saveFile(filename, xml2.toString());
			}
			String xml3="";
			for (Map map : list) {
				String ORDERNOnew=(String) map.get("ORDERNO");//额外修改的订单号
				String CUSTOMSCODE=(String) map.get("CUSTOMSCODE");
				String  BIZTYPE=(String) map.get("BIZTYPE");
				 Long BIZTIME=Long.valueOf(map.get("BIZTIME").toString());
				 xml3+="<?xml version=\"1.0\" encoding=\"utf-8\"?><CBECMESSAGE><MESSAGEHEAD>";
				 xml3+="<MESSAGEID>"+creatUUID()+"</MESSAGEID><MESSAGETYPE>EPT602</MESSAGETYPE>";
				 xml3+="<SENDERID>DP06374253</SENDERID><RECEIVERID>0100</RECEIVERID><SENDTIME>"+DateUtil.toString(new Date().getTime(),"yyyy-MM-dd")+"T"+DateUtil.toString(new Date().getTime(),"HH:mm:ss")+"</SENDTIME></MESSAGEHEAD><MESSAGEBODY>";
				 xml3+="<BODYMASTER><CUSTOMSCODE>"+CUSTOMSCODE+"</CUSTOMSCODE><BIZTYPE>"+BIZTYPE+"</BIZTYPE><BIZTIME>"+DateUtil.toString(BIZTIME,"yyyy-MM-dd")+"T"+DateUtil.toString(BIZTIME,"HH:mm:ss")+"</BIZTIME>";
			     Integer order_id=Integer.parseInt(map.get("ziorder_id").toString());
				 OrderPaymentInformation paymentInformation=this.orderInformationManager.getPaymentInformation(order_id);
				 if(paymentInformation!=null){	
					 Order order=this.orderManager.get(paymentInformation.getOrder_id());
					 Long create_time=order.getComplete_time();
					/* String ORDERNO=order.getSn();*/
					 String LOGISTICSNO=paymentInformation.getLOGISTICSNO();
					 String LOGISTICSNAME=paymentInformation.getLOGISTICSNAME();
				   xml3+="<HEAD><CBECBILLNO></CBECBILLNO><ORDERNO>"+ORDERNOnew+"</ORDERNO><PARENTBILLNO>"+LOGISTICSNO+"</PARENTBILLNO><BILLNO>"+LOGISTICSNO+"</BILLNO>";
				   xml3+="<IEDATE>"+DateUtil.toString(new Date().getTime(),"yyyy-MM-dd")+"</IEDATE><DECLAREDATE>"+DateUtil.toString(new Date().getTime(),"yyyy-MM-dd")+"</DECLAREDATE>";
				   xml3+="<DECLARECODE>DP06374253</DECLARECODE><DECLARECOP>北京易智付电子商务有限公司</DECLARECOP><IEPORT>0127</IEPORT><DECLAREPORT>0127</DECLAREPORT>";
				   xml3+="<TRAFWAY>"+LOGISTICSNAME+"</TRAFWAY><SHIPNAME>"+LOGISTICSNAME+"</SHIPNAME><WRAPTYPECODE>6</WRAPTYPECODE>";
				   String SHIPPER=paymentInformation.getSHIPPER();
				   String SHIPPERADDRESS=paymentInformation.getSHIPPERADDRESS();
				   String SHIPPERCOUNTRY=paymentInformation.getSHIPPERCOUNTRY();
				   xml3+="<SHIPPER>"+SHIPPER+"</SHIPPER><SHIPPERCITY>"+SHIPPERADDRESS+"</SHIPPERCITY><SHIPPERCOUNTRY>"+SHIPPERCOUNTRY+"</SHIPPERCOUNTRY>";
				   String CONSIGNEE=(String)map.get("CONSIGNEE");
				   String CONSIGNEEADDRESS=(String)map.get("CONSIGNEEADDRESS");
				   String CONSIGNEETELEPHONE=(String)map.get("CONSIGNEETELEPHONE");
				   String CONSIGNEECOUNTRY=(String)map.get("CONSIGNEECOUNTRY");
				   Member member=this.memberManager.get(order.getMember_id());
				   xml3+="<CONSIGNEE>"+CONSIGNEE+"</CONSIGNEE><CONSIGNEEID>"+member.getId_number()+"</CONSIGNEEID>";
				   xml3+="<CONSIGNEETEL>"+CONSIGNEETELEPHONE+"</CONSIGNEETEL><CONSIGNEECITY>"+CONSIGNEEADDRESS+"</CONSIGNEECITY><CONSIGNEEADDRESS>"+CONSIGNEEADDRESS+"</CONSIGNEEADDRESS><CONSIGNEECOUNTRY>"+CONSIGNEECOUNTRY+"</CONSIGNEECOUNTRY><COUNTRYCODE>133</COUNTRYCODE>"; 
				   List<OrderItem> items=this.orderManager.listGoodsItems(paymentInformation.getOrder_id());
			       OrderItem orderItem=items.get(0);
				   xml3+="<PACKNO>"+orderItem.getNum()+"</PACKNO><WEIGHT>"+order.getWeight()+"</WEIGHT></HEAD></BODYMASTER>";
				 }
				 Integer order_id1=Integer.parseInt(map.get("ziorder_id").toString());
				 List<Goodsinformation> goodsinformations=this.orderInformationManager.getGoodsInformation(order_id1);
				 Integer countNo=1;
				 for (Goodsinformation goodsinformation : goodsinformations) {
					 String GOODSNO=goodsinformation.getGOODSNO();
					   String goodsname=goodsinformation.getGOODSNAME();
					   String GOODSMODEL=goodsinformation.getGOODSMODEL();
					   String CODETS=goodsinformation.getCODETS();
					   String COUNTRY=goodsinformation.getCOUNTRY();
					   String CURRENCY=goodsinformation.getCURRENCY();
					   String UNIT=goodsinformation.getUNIT();
					   Integer QUANTITY=goodsinformation.getQUANTITY();
					   Double PRICE=goodsinformation.getPRICE();
				   xml3+="<GOODSLIST><NO>"+countNo+"</NO><GOODSNO>"+GOODSNO+"</GOODSNO><CODETS>3304990010</CODETS><GOODSNAME>"+goodsname+"</GOODSNAME><GOODSMODEL>"+GOODSMODEL+"</GOODSMODEL>";
				   xml3+="<QUANTITY>"+QUANTITY+"</QUANTITY><UNIT>"+UNIT+"</UNIT><TRADEQUANTITY>"+QUANTITY+"</TRADEQUANTITY><TRADEUNIT>"+UNIT+"</TRADEUNIT>";
				   xml3+="<COUNTRY>"+COUNTRY+"</COUNTRY><PRICE>"+PRICE+"</PRICE><TOTAL>"+QUANTITY*PRICE+"</TOTAL><CURRENCY>"+CURRENCY+"</CURRENCY></GOODSLIST></MESSAGEBODY></CBECMESSAGE>";
				   countNo=countNo+1;
				 }
				 String filename="";
					HttpServletRequest request = ServletActionContext.getRequest();
					String savePath = request.getRealPath("/")+"test/xml/";
					File test = new File(savePath);
					if(!test.exists()){
						test.mkdirs();
					}
					String ymd=DateUtil.toString(new Date().getTime(),"yyyyMMddHHmmss");
					filename+=savePath+ymd+"EPT602.xml";
					Savexml savexml=new Savexml();
					savexml.setCreate_time(DateUtil.getDateline());
					savexml.setFilename(ymd+"EPT602.xml");
					savexml.setOrder_id(id);
					savexml.setSn(this.orderManager.get(id).getSn());
					savexml.setIs_true(3);
					savexml.setIs_count(count+1);
					this.orderInformationManager.saveXmlInformation(savexml);
					System.out.println(xml3.toString());
					saveFile(filename, xml3.toString());
			}
			this.showSuccessJson("上传成功");
		} catch (NumberFormatException e) {
			this.showErrorJson("上传失败");
			e.printStackTrace();
		}
        return this.JSON_MESSAGE;
    }
    public String deletexmlInformation(){
    	HttpServletRequest request = this.getRequest();
		Savexml savexml = this.orderInformationManager.getXmlInformation(id);
		try {
			String file = savexml.getFilename();
			this.orderInformationManager.deleteXmlInformation(id);
			String savePath = request.getRealPath("/") + "test/xml/";
			if(file!=null && !StringUtils.isEmpty(file)){
				File delfile = new File(savePath + file);
				if(delfile.exists()){
					FileUtil.delete(savePath + file);
				}
			}
			this.showSuccessJson("删除文件成功！");
		} catch (Exception e) {
			this.showErrorJson("删除失败！");
		}
		return this.JSON_MESSAGE;
    }
    public String updownxml(){
    		HttpServletRequest request = this.getRequest();
    		HttpServletResponse response =ThreadContextHolder.getHttpResponse();
    		String static_server_path = SystemSetting.getStatic_server_path();
    		Savexml savexml = this.orderInformationManager.getXmlInformation(id);
    		String str=request.getRealPath("/") + "test/xml/"+savexml.getFilename();
    		try {
				downloadFile(request, response, str,savexml.getFilename());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	return "";
    }
public static void downloadFile(HttpServletRequest request,
		   HttpServletResponse response, String uri,String filename) throws IOException {
		  // 获取服务其上的文件名称
		  File file = new File(uri);
		  String name =filename;
		  StringBuffer sb = new StringBuffer();
		  sb.append("attachment;  filename=").append(name);
		  response.setHeader("Expires", "0");
		  response.setHeader("Cache-Control",
		    "must-revalidate, post-check=0, pre-check=0");
		  response.setHeader("Pragma", "public");
		  response.setContentType("application/x-msdownload;charset=UTF-8");
		  response.setHeader("Content-Disposition", new String(sb.toString()
		    .getBytes(),"UTF-8"));
		  // 将此文件流写入到response输出流中
		  FileInputStream inputStream = new FileInputStream(file);
		  OutputStream outputStream = response.getOutputStream();
		  byte[] buffer = new byte[1024];
		  int i = -1;
		  while ((i = inputStream.read(buffer)) != -1) {
		   outputStream.write(buffer, 0, i);
		  }
		  outputStream.flush();
		  outputStream.close();
		  inputStream.close();
		 }
    public  String creatUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
    public static void saveFile(String fileName, String content) {
        try {      
            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(fileName, true),"UTF-8");
            osw.write(content);
            osw.close();
        } catch (IOException e) {      
            e.printStackTrace();      
        }      
   }
	public IOrderInformationManager getOrderInformationManager() {
		return orderInformationManager;
	}

	public void setOrderInformationManager(
			IOrderInformationManager orderInformationManager) {
		this.orderInformationManager = orderInformationManager;
	}
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	public IMemberManager getMemberManager() {
		return memberManager;
	}
	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
}
