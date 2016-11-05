package com.enation.app.b2b2c.core.action.backend.express;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.SellerExpress;
import com.enation.app.b2b2c.core.service.ISellerExpressManager;
import com.enation.app.b2b2c.core.service.warehouse.IWarehouse;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.StringUtil;
import com.gd.express.exception.BaseException;
import com.gd.express.util.JDBCUtil;
import com.gd.express.util.PropertiesUtil;

import com.orderonline.api.fpx.CreateAndPreAlertOrderRequest;
import com.orderonline.api.fpx.CreateAndPreAlertOrderResponse;
import com.orderonline.api.fpx.DeclareInvoice;
import com.orderonline.api.fpx.OrderOnlineServiceExtImplService;
import com.orderonline.api.tool.CalculateFee;
import com.orderonline.api.tool.CargoTrackingResponse;
import com.orderonline.api.tool.ChargeCalculateRequest;
import com.orderonline.api.tool.ChargeCalculateResponse;
import com.orderonline.api.tool.IOrderOnlineToolService;
import com.orderonline.api.tool.OrderOnlineToolServiceImplService;
import com.orderonline.api.tool.Track;


/**
 * 物流处理
 * 俄通收API接口
 * @author zks
 * 
 */
//@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Results({
		@Result(name = "express", type = "freemarker", location = "/b2b2c/admin/order/express.html"),
		@Result(name = "next", type = "freemarker", location = "/b2b2c/admin/order/express.html"),
		@Result(name = "scusses", type = "freemarker", location = "/b2b2c/admin/order/express.html") })
@Action("express2")
public class ExpressAction2 extends WWAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String weight;
	private String length;
	private String size;
	private String hight;
	private String productCode;
	private String productCode2;
	private String member_id;
	private String order_sn; // 订单编号
	private String amount;
	private String tracksNo; // 物流跟踪
	private String warehouse_id;
	private Date ship_time;
	private String express_id;
	private String addr_id;
	
	@Autowired
	private ISellerExpressManager sellerExpressManager;
	
	@Autowired
	private IMemberAddressManager memberAddressManager;
	
	
	private Logger logger = Logger.getLogger(ExpressAction2.class);
	// 获取
	private final QName SERVICE_NAME_TOOL = new QName(
			"entity.orderonlinetool.api.fpx",
			"OrderOnlineToolServiceImplService");
	URL wsdlURL_tool = OrderOnlineToolServiceImplService.WSDL_LOCATION;
	OrderOnlineToolServiceImplService tool = new OrderOnlineToolServiceImplService(
			wsdlURL_tool, SERVICE_NAME_TOOL);
	IOrderOnlineToolService toolPort = tool.getOrderOnlineToolServiceImplPort();

	//
	private static final QName SERVICE_NAME = new QName(
			"entity.orderonline.api.fpx", "OrderOnlineServiceExtImplService");
	URL wsdlURL = OrderOnlineServiceExtImplService.WSDL_LOCATION;
	OrderOnlineServiceExtImplService ss = new OrderOnlineServiceExtImplService(
			wsdlURL, SERVICE_NAME);
	com.orderonline.api.fpx.IOrderOnlineService port = ss.getOrderOnlineServiceExtImplPort();
	
	
	
	/** 用户token **/
	static String token = PropertiesUtil.getToken();
	static String token2 = PropertiesUtil.getToken2();
	/** 货运方式 **/
	//static String productCode = PropertiesUtil.getProductCode();
//	static String productCode2 = PropertiesUtil.getProductCode2();
	private IWarehouse warehouse;

	/**
	 * 查询买家地址信息
	 * 
	 * @param orderId
	 * @return List
	 * @throws BaseException
	 */
	public Map<String, Object> query(String orderId) throws BaseException {
		String sql = "SELECT * FROM order_header WHERE ORDER_ID = " + orderId
				+ " ";
		Map<String, Object> map = JDBCUtil.queryForMap(sql);
		return map;
	}

	/**
	 * 创建物流信息
	 * 
	 * @param order_id
	 * @param warehouse_id
	 * @param orderId
	 * @param logisticsKey
	 * @return
	 * @throws SQLException
	 */
	private void createLogistics(Integer order_id, String trackingNumber,
			String referenceNumber, String createTime, String warehouse_id) {
		warehouse.createLogistics(order_id, trackingNumber, referenceNumber,
				createTime, warehouse_id);
	}

	/**
	 * 创建报关单信息
	 * 
	 * @param orderMap
	 * @param orderId
	 * @param con
	 * @return
	 * @throws BaseException
	 */
	private void createDeclareInvoice(Map<String, String[]> parameterMap,
			CreateAndPreAlertOrderRequest e) throws BaseException {
		if (parameterMap.get("ename") != null) {
			for (int i = 0; i < parameterMap.get("ename").length; i++) {
				DeclareInvoice invoice = new DeclareInvoice();
				String ename = parameterMap.get("ename")[i];
				String name = parameterMap.get("name")[i];
				String unitPrice = parameterMap.get("unitPrice")[i];
				String count = parameterMap.get("count")[i];
				invoice.setEName(ename);
				invoice.setName(name);
				invoice.setUnitPrice(unitPrice);
				invoice.setDeclarePieces(count);
				e.getDeclareInvoice().add(invoice);
			}
		} else {
			throw new BaseException("数据填写不完整");
		}

	}

	/**
	 * 订单状态验证
	 * 
	 * @param orderId
	 * @throws BaseException
	 */
	private Order validateOrder(String orderId) throws BaseException {
		Order order = warehouse.queryOrder(order_sn);// 查询订单信息
		if (null == order) {
			throw new BaseException("订单信息有误");
		}
		if (!order.getShip_status().equals(OrderStatus.SHIP_NO)) {
			throw new BaseException("订单状态有误 不可发货");
		}
		return order;
	}

	/**
	 * 
	 * 订单状态修改
	 * 
	 * @param orderId
	 * @return
	 * @throws Exception
	 */
	public void updateOrder(String logistics_id, String orderId, String amount)
			throws Exception {

		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			String sql = "update order_header set STATUS_ID='ORDER_SENT' where ORDER_ID = ? ";
			condition.put("1", orderId);
			JDBCUtil.update(sql, condition);

			insertOrderStatus(orderId);

			updateLogistics(logistics_id, amount);
		} catch (BaseException ex) {
			ex.printStackTrace();
			throw ex;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("系统错误");
		}
	}

	/**
	 * 物流信息修改
	 * 
	 * @param orderId
	 * @param amount
	 * @throws Exception
	 */
	private void updateLogistics(String logistics_id, String amount)
			throws Exception {
		try {
			String sql = "update logisticsInformation set cofirmtime= ?,amount = ? where logistics_id = ?";
			Map<String, Object> condition = new HashMap<String, Object>();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = sdf.format(date);
			condition.put("1", time);
			condition.put("2", amount);
			condition.put("3", logistics_id);
			int i = JDBCUtil.update(sql, condition);
			if (i != 1) {
				throw new BaseException("数据更新错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("系统错误");
		}

	}

	/**
	 * 插入订单状态信息
	 * 
	 * @param orderId
	 * @throws Exception
	 */
	private void insertOrderStatus(String orderId) throws Exception {
		try {
			String sql = "insert into order_status (ORDER_STATUS_ID, STATUS_ID, ORDER_ID,STATUS_DATETIME,STATUS_USER_LOGIN) "
					+ "values (?,?,?,?,?)";

			Date date = new Date();
			long key = date.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String abcValue = sdf.format(date);

			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("1", "express" + key);
			condition.put("2", "ORDER_SENT");
			condition.put("3", orderId);
			condition.put("4", abcValue);
			condition.put("5", "admin");
			JDBCUtil.insert(sql, condition);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("系统错误");
		}
	}

	/**
	 * 运费试算
	 * 
	 * @param weight
	 * @param length
	 * @param size
	 * @param hight
	 * @param count
	 * @return
	 * @throws Exception
	 */
	public String chargeCalculate() throws Exception {
		BigDecimal amount = new BigDecimal("0");
		Map<String, String> result = new HashMap<String, String>();
		try {

			Map<String, Object> conditon = new HashMap<String, Object>();
			conditon.put("weight", weight);
			conditon.put("length", length);
			conditon.put("size", size);
			conditon.put("hight", hight);
		//	conditon.put("productCode", productCode);

		//	validate(conditon);

			ChargeCalculateRequest request = new ChargeCalculateRequest();
			request.setWeight(weight);
			request.setLength(length);
			//request.setWidth(size);
			request.setWidth(size);
			request.setHeight(hight);
		//	request.set(productCode);
			request.setCountryCode("RU");
		/*	if ( Double.parseDouble(weight)<=2) {
				if ( Double.parseDouble(length)+Double.parseDouble(size)+Double.parseDouble(hight)<=90) {
					if ( Double.parseDouble(length)<=60&& Double.parseDouble(length)>=14) {
						if ( Double.parseDouble(size)<=60&& Double.parseDouble(size)>=9) {
							if ( Double.parseDouble(hight)<=60) {
							request.getProductCode().add(productCode2);
							}
						}
					}
				}
			}else {*/
				request.getProductCode().add(productCode);
			//System.out.println(productCode+"------------");
			
			ChargeCalculateResponse response;

			response = toolPort.chargeCalculateService(token2, request);
			List<CalculateFee> fee = response.getCalculateFee();
			amount = new BigDecimal(fee.get(0).getTotalRmbAmount());
			result.put("amount", amount.toString());
			result.put("limit", fee.get(0).getDeliveryperiod());
			showJson(amount.toString(), fee.get(0).getDeliveryperiod());
//		} catch (BaseException ex) {
//			ex.printStackTrace();
//			this.showErrorJson(ex.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("系统错误");
		}

		return this.JSON_MESSAGE;
	}

	protected void showJson(String fee, String date) {
		if (StringUtil.isEmpty(fee))
			this.json = "{\"result\":1}";
		else
			this.json = "{\"result\":1,\"amount\":\"" + fee + "\",\"limit\":\""
					+ date + "\"}";
	}

	/**
	 * 数据校验
	 * 
	 * @param condtion
	 * @throws BaseException
	 */
	private void validate(Map<String, Object> condition) throws BaseException {
		String regex = "^(([1-9]((\\.[0-9]{1,3})?))|0\\.[0-9]{0,2}[1-9]|(0\\.(([0-9][1-9])|([1-9][0-9]))[0-9]{0,1}))$";
		Pattern p = Pattern.compile(regex);
	/*	Matcher m = p.matcher((String) condition.get("weight"));
		if (!m.matches()) {
			throw new BaseException("重量填写有误");
		}*/
		condition.remove("weight");
		Set<String> set = condition.keySet();
		BigDecimal totalSize = new BigDecimal("0");
		int i = 0;
		int l = 0;
		for (String key : set) {
			String value = (String) condition.get(key);
			if (!isNumber(value) || Double.parseDouble(value) > 180) {
				throw new BaseException("信息填写有误 请输入小于60cm的数字 保留一位小数");
			}
			BigDecimal num = new BigDecimal(value);
			totalSize = totalSize.add(num);
		/*	if (num.compareTo(new BigDecimal(9)) > 0)
				i++;
			if (num.compareTo(new BigDecimal(14)) > 0)
				l++;*/
		}
		if (totalSize.compareTo(new BigDecimal(180)) > 0) {
			throw new BaseException("长、宽、高之和必须小于180cm");
		}
		/*if (!(i >= 2 && l >= 1)) {
			throw new BaseException("至少有一面的长度大于14cm，宽度大于9cm");
		}*/
	}

	/**
	 * 数字校验
	 * 
	 * @param params
	 * @return
	 */
	private boolean isNumber(String params) {
		String matchRegex = "^(\\d+)((\\.[0-9])?)$";
		Pattern p = Pattern.compile(matchRegex);
		Matcher m = p.matcher(params);
		return m.matches();
	}

	/**
	 * 查询订单信息 返回订单中产品的种类及每一类产品的数量及图片链接
	 * 
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> findOrderIterm(String orderNo)
			throws Exception {
		String sql = "SELECT r.product_id , r.quantity,p.small_image_url FROM order_item r left join product p "
				+ "on r.product_id = p.product_id WHERE r.order_id = ?";
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("1", orderNo);
		List<Map<String, Object>> list = JDBCUtil.queryForList(sql, condition);
		if (list == null || list.isEmpty()) {
			throw new Exception("订单信息不存在");
		}
		return list;
	}

	public void queryTracking() {
		List<CargoTrackingResponse> responseList = new ArrayList<CargoTrackingResponse>();
		List<String> list = new ArrayList<String>();
		list.add("1002520150422151603");
		responseList = toolPort.cargoTrackingService(token2, list);
		for (CargoTrackingResponse response : responseList) {
			List<Track> trackList = response.getTracks();
			for (Track track : trackList) {
				track.getTrackInfo();
			}
		}
	}

	/**
	 * 查询仓库信息
	 * 
	 * @return
	 * @throws BaseException
	 */
	public List<Map<String, Object>> queryWarehouse() throws BaseException {
		String sql = "select warehouse_id, warehouse_name, ShipperStateOrProvince, ShipperCity, ShipperAddress, ShipperTelephone, ShipperPostCode from warehouse";
		List<Map<String, Object>> list = JDBCUtil.queryForList(sql);
		return list;
	}

	/**
	 * 发货创建订单
	 * 
	 * @return
	 * @throws Exception
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public String generalLogistics() throws Exception {

		List<CreateAndPreAlertOrderResponse> response = null;

		try {

			Order order = validateOrder(order_sn);// 查询并验证订单信息
			Integer order_id = order.getOrder_id();
			Integer address_id = order.getAddress_id();
			// 查询订单地址
			MemberAddress address = warehouse.queryMemberAddress(address_id);
			String addr = address.getAddr();
			String province = address.getProvince();
			String city = address.getCity();
			String signeeName = address.getName();
			String consigneePostCode = address.getZip();
			String tel = address.getMobile();
			if (tel == null || StringUtil.isEmpty(tel)) {
				tel = address.getTel();
			}

			HttpServletRequest request = this.getRequest();
			Map<String, String[]> parameterMap = request.getParameterMap();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			String createTime = sdf.format(date);

			List<CreateAndPreAlertOrderRequest> requestList = new ArrayList<CreateAndPreAlertOrderRequest>();
			CreateAndPreAlertOrderRequest e = new CreateAndPreAlertOrderRequest();
			e.setOrderNo(order_sn); // 客户订单号码，由 客户自己定义
			/*if ( Double.parseDouble(weight)<=2) {
				if ( Double.parseDouble(length)+Double.parseDouble(size)+Double.parseDouble(hight)<=90) {
					if ( Double.parseDouble(length)<=60&& Double.parseDouble(length)>=14) {
						if ( Double.parseDouble(size)<=60&& Double.parseDouble(size)>=9) {
							if ( Double.parseDouble(hight)<=60) {
								e.setProductCode(productCode2);
							}
						}
					}
				}
			}else {*/
				e.setProductCode(productCode2);
			
			

			e.setDestinationCountryCode("RU"); // 目的国家二字代码
			e.setCustomerWeight(weight); // 客户自己称的重量 (单位：KG)
			e.setShipperName("suiyitong"); // 发件人姓名
			e.setShipperStateOrProvince("heilongjiang");// 发件人省
			e.setShipperCity("haerbin"); // 发件人城市
			e.setShipperAddress("xiangganglu"); // 发件人地址
			e.setConsigneeName(signeeName); // 收件人姓名
			e.setStreet(addr); // 收件人街道
			e.setCity(city); // 收件人城市
			e.setConsigneeTelephone(tel); // 收件人电话
			e.setStateOrProvince(province); // 收件人州 省
			e.setConsigneePostCode(consigneePostCode); // 收件人邮编
			e.setMctCode("1");

			createDeclareInvoice(parameterMap, e);// 创建报关单信息

			requestList.add(e);

			response = port.createAndPreAlertOrderService(token2, requestList);
			CreateAndPreAlertOrderResponse orderResponse = response.get(0);
			if (orderResponse.getAck().equals("Failure")) {
				String message = orderResponse.getErrors().get(0)
						.getCnMessage();
				showErrorJson(message);
				//System.out.println(message);
				// throw new Exception(message);
				return this.JSON_MESSAGE;

			}
			String trackingNumber = orderResponse.getTrackingNumber();
			String referenceNumber = orderResponse.getReferenceNumber();

			createLogistics(order_id, trackingNumber, referenceNumber,
					createTime, warehouse_id);// 创建物流信息
			// warehouse.updateOrder(trackingNumber, order_sn, amount,
			// createTime);
			String logi_name = PropertiesUtil.getRuston();
			warehouse.updateOrderByRuston(trackingNumber, order_sn, amount,
					createTime, logi_name,weight);// 修改订单信息

			showExpressJson(referenceNumber, trackingNumber);
		} catch (BaseException e) {
			e.printStackTrace();
			showErrorJson(e.getMessage());
			return this.JSON_MESSAGE;
		} catch (Exception e) {
			e.printStackTrace();
			showErrorJson("系统错误");
			return this.JSON_MESSAGE;
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 返回发货成功后的信息
	 * 
	 * @param referenceNumber
	 * @param trackingNumber
	 */
	private void showExpressJson(String referenceNumber, String trackingNumber) {
		if (StringUtil.isEmpty(referenceNumber))
			this.json = "{\"result\":1}";
		else
			this.json = "{\"result\":1,\"referenceNumber\":\""
					+ referenceNumber + "\",\"trackingNumber\":\""
					+ trackingNumber + "\"}";
	}

	/**
	 * 用户手动入手运单号
	 * @return
	 */
	public String inputTracksNo() {
		try {
			if(StringUtil.isEmpty(express_id) || !NumberUtils.isNumber(express_id)) {
				throw new RuntimeException("请选择配送商");
			}
			if(StringUtil.isEmpty(addr_id) || !NumberUtils.isNumber(addr_id)) {
				throw new RuntimeException("请选择发货地址");
			}
			if(StringUtil.isEmpty(tracksNo)) {
				throw new RuntimeException("请输入运单号");
			}
			if(StringUtil.isEmpty(ship_time.toString())) {
				throw new RuntimeException("请选择发货日期");
			}
			Map<String,Object> fields = new HashMap<String, Object>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String time = sdf.format(ship_time);
			
			//获取卖家配送信息
			SellerExpress express = sellerExpressManager.getSingle(Integer.parseInt(express_id));
			if(express == null) throw new RuntimeException("未查到卖家配送信息");
			
			//获取卖家发货信息
			MemberAddress address = memberAddressManager.getAddress(Integer.parseInt(addr_id));
			if(address == null) throw new RuntimeException("未查到卖家发货信息");
			
			String addressStr = address.getProvince() + "-" + address.getCity() + "-" + address.getRegion();
				
			fields.put("ship_no", tracksNo);  //运单号
			fields.put("sn", order_sn);      //订单号
			fields.put("ship_time", time);  //发货时间
			fields.put("shipping_id", express_id);  //配送ID
			// TODO  配送方式
			fields.put("shipping_type", "卖家自定义配送商发货");
			fields.put("ship_send_area", addressStr);  //发货地区
			fields.put("ship_send_address", address.getAddr());  //发货地址
			fields.put("ship_send_zip", address.getZip());  //发货邮编
			fields.put("ship_send_name", address.getName());  //发货人
			
			if(!StringUtil.isEmpty(address.getTel())) {
				fields.put("ship_send_tel", address.getTel());  //发货人联系电话
			} else if(!StringUtil.isEmpty(address.getMobile())) {
				fields.put("ship_send_tel", address.getMobile());  //发货人联系电话
			}
			fields.put("logi_id", express.getExpress_id());  //配送商id
			fields.put("logi_name", express.getExpress_name());  //配送商名称
			//更改订单状态为已发货
			fields.put("status", OrderStatus.ORDER_SHIP);   
			fields.put("ship_status", OrderStatus.SHIP_YES);
			
			warehouse.updateOrderBySend(fields);
//			warehouse.updateOrder(tracksNo, order_sn, amount, time);// 修改订单信息
			this.showSuccessJson("添加运单号成功");
		} catch (Exception e) {
			if(e instanceof RuntimeException) {
				this.showErrorJson(e.getMessage());
			} else {
				this.showErrorJson("添加失败");
			}
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}

	/****************************************************************************/

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getHight() {
		return hight;
	}

	public void setHight(String hight) {
		this.hight = hight;
	}

	public String getMember_id() {
		return member_id;
	}

	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}

	public IWarehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(IWarehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTracksNo() {
		return tracksNo;
	}

	public void setTracksNo(String tracksNo) {
		this.tracksNo = tracksNo;
	}

	public String getWarehouse_id() {
		return warehouse_id;
	}

	public void setWarehouse_id(String warehouse_id) {
		this.warehouse_id = warehouse_id;
	}

	public Date getShip_time() {
		return ship_time;
	}

	public void setShip_time(Date ship_time) {
		this.ship_time = ship_time;
	}

	public String getExpress_id() {
		return express_id;
	}

	public void setExpress_id(String express_id) {
		this.express_id = express_id;
	}

	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	public ISellerExpressManager getSellerExpressManager() {
		return sellerExpressManager;
	}

	public void setSellerExpressManager(ISellerExpressManager sellerExpressManager) {
		this.sellerExpressManager = sellerExpressManager;
	}

	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}

	public void setMemberAddressManager(IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getProductCode2() {
		return productCode2;
	}

	public void setProductCode2(String productCode2) {
		this.productCode2 = productCode2;
	}
	
	/****************************************************************************/

}
