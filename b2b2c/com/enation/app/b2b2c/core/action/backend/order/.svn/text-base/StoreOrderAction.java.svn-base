package com.enation.app.b2b2c.core.action.backend.order;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.component.count.action.ConutDiscountAction;
import com.enation.app.shop.core.model.DlyCenter;
import com.enation.app.shop.core.model.DlyType;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.service.IDlyCenterManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Component
/**
 * 店铺订单管理
 * @author LiFenLong
 *
 */
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
	 @Result(name="list",type="freemarker", location="/b2b2c/admin/order/store_order_list.html"),
	 @Result(name="not_pay",type="freemarker", location="/b2b2c/admin/order/not_pay.html"),
	 @Result(name="detail",type="freemarker", location="/b2b2c/admin/order/order_detail.html")
})
@Action("storeOrder")
public class StoreOrderAction extends WWAction{
	private Order ord;
	
	private Integer orderId;
	private Integer stype;
	private Integer status=null;
	private Integer paystatus=null;
	private Integer shipstatus=null;
	private Integer shipping_type=null;
	private Integer payment_id=null;
	
	private String uname;
	private String keyword;
	private String start_time;
	private String end_time;
	private String sn;
	private String logi_no;
	private String ship_name;
	private String status_Json;
	private String payStatus_Json;
	private String ship_Json;
	private String complete;
	private String store_name;
	private Integer store_id;
	
	private Map orderMap;
	private Map statusMap;
	private Map payStatusMap;
	private Map shipMap;
	//详细页面插件返回的数据 
	protected Map<Integer,String> pluginTabs;
	protected Map<Integer,String> pluginHtmls;
	
	private List provinceList;
	private List<DlyType> shipTypeList;
	private List<PayCfg> payTypeList;
	private List<Map> orderChildList;
	
	private IDlyTypeManager dlyTypeManager;
	private IPaymentManager paymentManager;
	private IRegionsManager regionsManager;
	private IOrderManager orderManager;
	private IStoreOrderManager storeOrderManager;
	private OrderPluginBundle orderPluginBundle;
	
	//选择发货地点
	private IDlyCenterManager dlyCenterManager;
	private List<DlyCenter> dlycenterlist;
	
	/**
	  * 跳转至订单详细页面
	  * @param uname 会员名称,String
	  * @param ship_name 收货人姓名,String
	  * @param orderId 订单号,Integer
	  * @param ord 订单,Order
	  * @param provinceList 省列表
	  * @param pluginTabs 订单详细页的选项卡
	  * @param pluginHtmls 订单详细页的内容
	  * @param dlycenterlist 发货信息列表
	  * @return 订单详细页面
	  */
	public String orderDetail(){
		if(ship_name!=null ) ship_name = StringUtil.toUTF8(ship_name);
		if(uname!=null ) uname = StringUtil.toUTF8(uname);
		this.ord = this.orderManager.get(orderId);
		provinceList = this.regionsManager.listProvince();
		
		pluginTabs= this.orderPluginBundle.getTabList(ord);
		pluginHtmls = this.orderPluginBundle.getDetailHtml(ord);
		dlycenterlist= dlyCenterManager.list();
		return "detail";
	}
	/**
	 * 分页读取订单列表
	 * 根据订单状态 state 检索，如果未提供状态参数，则检索所有
	 * @param statusMap 订单状态集合,Map
	 * @param payStatusMap 订单付款状态集合,Map
	 * @param shipMap,订单配送人状态集合,Map
	 * @param shipTypeList 配送方式集合,List
	 * @param payTypeList 付款方式集合,List
	 * @return 订单列表
	 */
	public String list(){
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
		shipTypeList = dlyTypeManager.list();
		payTypeList = paymentManager.list();
		return "list";
	}
	/**
	 * @author LiFenLong
	 * @param stype 搜索状态, Integer
	 * @param keyword 搜索关键字,String
	 * @param start_time 开始时间,String
	 * @param end_time 结束时间,String
	 * @param sn 订单编号,String
	 * @param ship_name 订单收货人姓名,String
	 * @param status 订单状态,Integer
	 * @param paystatus 订单付款状态,Integer
	 * @param shipstatus 订单配送状态,Integer
	 * @param shipping_type 配送方式,Integer
	 * @param payment_id 付款方式,Integer
	 * @param order_state 订单状态_从哪个页面进来搜索的(未付款、代发货、等),String
	 * @param complete 是否订单为已完成,String 
	 * @return 订单列表 json
	 */
	public String listJson(){
		HttpServletRequest requst = ThreadContextHolder.getHttpRequest();
		orderMap = new HashMap();
		orderMap.put("stype", stype);
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("status", status);
		orderMap.put("sn", sn);
		orderMap.put("ship_name", ship_name);
		orderMap.put("paystatus", paystatus);
		orderMap.put("shipstatus", shipstatus);
		orderMap.put("shipping_type", shipping_type);
		orderMap.put("payment_id", payment_id);
		orderMap.put("order_state", requst.getParameter("order_state"));
		orderMap.put("complete", complete);
		orderMap.put("store_name", store_name);
		orderMap.put("store_id", store_id);
		
		this.webpage = this.storeOrderManager.listOrder(orderMap, this.getPage(),this.getPageSize(), this.getSort(),this.getOrder());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	/**
	 * 导出订单
	 * @return
	 */
	public String exportOrder(){
		orderMap = new HashMap();
		orderMap.put("keyword", keyword);
		orderMap.put("start_time", start_time);
		orderMap.put("end_time", end_time);
		orderMap.put("status", status);
		orderMap.put("store_name", store_name);
		List<StoreOrder> orderList = this.storeOrderManager.listOrder(orderMap);
		
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		// 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook(); 
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        HSSFSheet sheet = wb.createSheet("订单明细表"); 
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("订单ID");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("订单编号");  
        cell.setCellStyle(style); 
       
        cell = row.createCell((short) 2);  
        cell.setCellValue("订单状态");  
        cell.setCellStyle(style);
       
        cell = row.createCell((short) 3);  
        cell.setCellValue("付款状态");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("发货状态");  
        cell.setCellStyle(style);
        
    /*    cell = row.createCell((short) 4);  
        cell.setCellValue("下单日期");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("发货日期");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("订单总额");  
        cell.setCellStyle(style); 
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("收货人");  
        cell.setCellStyle(style); */
        
        
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("发货店铺ID");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("店铺名称");  
        cell.setCellStyle(style);  
        
        /*cell = row.createCell((short) 7);  
        cell.setCellValue("发货人会员ID");  
        cell.setCellStyle(style);  */
    
        cell = row.createCell((short) 7);  
        cell.setCellValue("发货人姓名");  
        cell.setCellStyle(style); 
        
       /* cell = row.createCell((short) 9);  
        cell.setCellValue("收货人会员ID");  
        cell.setCellStyle(style);  
        */
        cell = row.createCell((short) 8);  
        cell.setCellValue("收货人姓名");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 9);  
        cell.setCellValue("收货人地址");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 10);  
        cell.setCellValue("收货人电话");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 11);  
        cell.setCellValue("收货人手机");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 12);  
        cell.setCellValue("发货时间");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 13);  
        cell.setCellValue("收货人邮编");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 14);  
        cell.setCellValue("支付方式ID");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 15);  
        cell.setCellValue("支付名称");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 16);  
        cell.setCellValue("订单金额");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 17);  
        cell.setCellValue("完成时间");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 18);  
        cell.setCellValue("运费");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 19);  
        cell.setCellValue("商品总金额");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 20);  
        cell.setCellValue("订单创建时间");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 21);  
        cell.setCellValue("支付金额");  
        cell.setCellStyle(style);
        
   /*     cell = row.createCell((short) 11);  
        cell.setCellValue("配送方式");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 13);  
        cell.setCellValue("币种");  
        cell.setCellStyle(style);*/
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到。
        int count = 0;
        for(StoreOrder order : orderList){

        	row = sheet.createRow(count + 1); 
        	try {
				
				row.createCell((short) 0).setCellValue(order.getOrder_id());  
		        row.createCell((short) 1).setCellValue(order.getSn()); 
		        row.createCell((short) 2).setCellValue(this.getOrderStatusText(order.getStatus())); 		        
		        row.createCell((short) 3).setCellValue(this.getPayStatusText(order.getPay_status())); 
		        row.createCell((short) 4).setCellValue(this.getShipStatusText(order.getShip_status())); 
		        row.createCell((short) 5).setCellValue(order.getStore_id()); 
		        row.createCell((short) 6).setCellValue(order.getStore_name()); 
		        row.createCell((short) 7).setCellValue(order.getShip_send_name()); 
		        row.createCell((short) 8).setCellValue(order.getShip_name()); 
		        row.createCell((short) 9).setCellValue(order.getShip_addr()); 
		        row.createCell((short) 10).setCellValue(order.getShip_tel()); 
		        row.createCell((short) 11).setCellValue(order.getShip_mobile()); 
		        row.createCell((short) 12).setCellValue(order.getShip_time()); 
		        row.createCell((short) 13).setCellValue(order.getShip_zip()); 
		        row.createCell((short) 14).setCellValue(order.getPayment_id()); 
		        row.createCell((short) 15).setCellValue(order.getPayment_name()); 
		        row.createCell((short) 16).setCellValue(order.getOrder_amount()); 
		        row.createCell((short) 17).setCellValue(this.getTime(order.getComplete_time())); 
		        row.createCell((short) 18).setCellValue(order.getShipping_amount()); 
		        row.createCell((short) 19).setCellValue(order.getGoods_amount()); 
		        row.createCell((short) 20).setCellValue(this.getTime(order.getCreate_time())); 
		        row.createCell((short) 21).setCellValue(order.getPaymoney()); 
		        
		      /*  row.createCell((short) 6).setCellValue(OrderStatus.getOrderStatusText(order.getStatus())); 
		        row.createCell((short) 7).setCellValue(OrderStatus.getPayStatusText(order.getPay_status())); 
		        row.createCell((short) 8).setCellValue(OrderStatus.getShipStatusText(order.getShip_status()));		        
		        row.createCell((short) 9).setCellValue(order.getShipping_type()); 
		        row.createCell((short) 10).setCellValue(order.getPayment_name()); 
		        if(order.getCurrency().equals("CNY")){
		        	row.createCell((short) 11).setCellValue("人民币"); 
		        }else {
		        	row.createCell((short) 11).setCellValue("卢布"); 
				}*/
		        
		        
			} catch (Exception e) {
				// TODO: handle exception
				 e.printStackTrace();
				 this.showErrorJson("导出数据失败" + e.getMessage());
				 this.logger.error("导出数据失败", e);
			} 
        	
        	
        	count ++;
        	
        }
        
        // 第六步，将文件存到指定位置  
        try{
            String filename = "dingdan.xls";//设置下载时客户端Excel的名称    
            // 请见：http://zmx.iteye.com/blog/622529 
            //http://lancijk.iteye.com/blog/1390341   
            //filename = Util.encodeFilename(filename, request);    
            response.setContentType("application/vnd.ms-excel");    
            response.setHeader("Content-disposition", "attachment;filename=" + filename);    
            OutputStream ouputStream = response.getOutputStream();    
            wb.write(ouputStream);    
            ouputStream.flush();    
            ouputStream.close(); 
        }catch (Exception e){  
        	this.showErrorJson("导出失败" + e.getMessage());
			this.logger.error("导出失败", e);
			e.printStackTrace();
          
        } 
        
        this.showSuccessJson("成功导出！");
		return null;
	}
	/*
	 * 日期转换
	 */
	public String getTime (Long time){
		String text = "";
		if(time!=null&&time>0){
			text = DateUtil.toString(time,"yyyy-MM-dd");
		}		
		return text;
	}
	/**
	 * 获取订单状态
	 * 
	 * @param status
	 * @return
	 */
	public String getOrderStatusText(int status) {
		String text = "";

		switch (status) {
		case -7:
			text = "已换货";
			break;
		case -6:
			text = "换货被拒绝";
			break;

		case -5:
			text = "退货被拒绝";
			break;

		case -4:
			text = "申请换货";
			break;

		case -3:
			text = "申请退货";
			break;
		case -2:
			text = "退货";
			break;
		case -1:
			text = "退款";
			break;

		case 0:
			text = "待付款";
			// text = "已确认";
			break;

		case 9:
			text = "订单已生效";
			break;

		case 1:
			text = "已付款待确认";
			break;

		case 2:
			text = "已付款";
			break;

		case 3:
			text = "配货中";
			break;

		case 4:
			text = "未发货";
			break;

		case  5:
			text = "已发货";
			break;

		case 7:
			text = "已完成";
			break;

		case 6:
			text = "已收货";
			break;

		case 8:
			text = "已取消";
			break;
			
		case 11:
			text = "已放款";
			break;
			
		case 10:
			text = "纠纷订单";
			break;

		default:
			text = "错误状态";
			break;
		}
		return text;

	}
	/**
	 * 获取付款状态
	 * 
	 * @param status
	 * @return
	 */
	public static String getPayStatusText(int pay_status) {
		String text = "";

		switch (pay_status) {
		case 0:
			text = "未付款";
			break;
		case 1:
			text = "已付款待确认";
			break;
		case 2:
			text = "已确认付款";
			break;

		case 3:
			text = "已经退款";
			break;
		case 4:
			text = "部分退款";
			break;
		case 5:
			text = "部分付款";
			break;
		default:
			text = "错误状态";
			break;
		}
		return text;
	}
	
	/**
	 * 获取货运状态
	 * 
	 * @param status
	 * @return
	 */
	public static String getShipStatusText(int ship_status) {
		String text = "";

		switch (ship_status) {
		case 0:
			text = "未配货";
			break;
		case 1:
			text = "配货中 ";
			break;

		case 2:
			text = "未发货";
			break;

		case 3:
			text = "已发货";
			break;

		case 4:
			text = "已退货";
			break;
		case 5:
			text = "部分发货";
			break;
		case 6:
			text = "部分退货";
			break;

		case 7:
			text = "部分换货";
			break;

		case 8:
			text = " 已换货";
			break;

		case 9:
			text = " 已收货";
			break;
		default:
			text = "错误状态";
			break;
		}

		return text;

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
	/**
	 * 未付款订单
	 * @author LiFenLong
	 * @param statusMap 订单状态集合,Map
	 * @param payStatusMap 订单付款状态集合,Map
	 * @param shipMap,订单配送人状态集合,Map
	 * @param shipTypeList 配送方式集合,List
	 * @param payTypeList 付款方式集合,List
	 * @return 未付款订单列表
	 */
	public String notPayOrder(){
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
		shipTypeList = dlyTypeManager.list();
		payTypeList = paymentManager.list();
		return "not_pay";
	}
	public Order getOrd() {
		return ord;
	}
	public void setOrd(Order ord) {
		this.ord = ord;
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}
	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public List<Map> getOrderChildList() {
		return orderChildList;
	}
	public void setOrderChildList(List<Map> orderChildList) {
		this.orderChildList = orderChildList;
	}
	public Map getOrderMap() {
		return orderMap;
	}
	public void setOrderMap(Map orderMap) {
		this.orderMap = orderMap;
	}
	public Integer getStype() {
		return stype;
	}
	public void setStype(Integer stype) {
		this.stype = stype;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getShip_name() {
		return ship_name;
	}
	public void setShip_name(String ship_name) {
		this.ship_name = ship_name;
	}
	public Integer getPaystatus() {
		return paystatus;
	}
	public void setPaystatus(Integer paystatus) {
		this.paystatus = paystatus;
	}
	public Integer getShipstatus() {
		return shipstatus;
	}
	public void setShipstatus(Integer shipstatus) {
		this.shipstatus = shipstatus;
	}
	public Integer getShipping_type() {
		return shipping_type;
	}
	public void setShipping_type(Integer shipping_type) {
		this.shipping_type = shipping_type;
	}
	public Integer getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}
	public String getComplete() {
		return complete;
	}
	public void setComplete(String complete) {
		this.complete = complete;
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
	public List<DlyType> getShipTypeList() {
		return shipTypeList;
	}
	public void setShipTypeList(List<DlyType> shipTypeList) {
		this.shipTypeList = shipTypeList;
	}
	public List<PayCfg> getPayTypeList() {
		return payTypeList;
	}
	public void setPayTypeList(List<PayCfg> payTypeList) {
		this.payTypeList = payTypeList;
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
	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}
	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}
	public IPaymentManager getPaymentManager() {
		return paymentManager;
	}
	public void setPaymentManager(IPaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public Map<Integer, String> getPluginTabs() {
		return pluginTabs;
	}
	public void setPluginTabs(Map<Integer, String> pluginTabs) {
		this.pluginTabs = pluginTabs;
	}
	public Map<Integer, String> getPluginHtmls() {
		return pluginHtmls;
	}
	public void setPluginHtmls(Map<Integer, String> pluginHtmls) {
		this.pluginHtmls = pluginHtmls;
	}
	public List getProvinceList() {
		return provinceList;
	}
	public void setProvinceList(List provinceList) {
		this.provinceList = provinceList;
	}
	public IRegionsManager getRegionsManager() {
		return regionsManager;
	}
	public void setRegionsManager(IRegionsManager regionsManager) {
		this.regionsManager = regionsManager;
	}
	public OrderPluginBundle getOrderPluginBundle() {
		return orderPluginBundle;
	}
	public void setOrderPluginBundle(OrderPluginBundle orderPluginBundle) {
		this.orderPluginBundle = orderPluginBundle;
	}
	public IDlyCenterManager getDlyCenterManager() {
		return dlyCenterManager;
	}
	public void setDlyCenterManager(IDlyCenterManager dlyCenterManager) {
		this.dlyCenterManager = dlyCenterManager;
	}
	public List<DlyCenter> getDlycenterlist() {
		return dlycenterlist;
	}
	public void setDlycenterlist(List<DlyCenter> dlycenterlist) {
		this.dlycenterlist = dlycenterlist;
	}
	public String getLogi_no() {
		return logi_no;
	}
	public void setLogi_no(String logi_no) {
		this.logi_no = logi_no;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
}
