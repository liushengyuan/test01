package com.enation.app.shop.component.payment.plugin.yandexpay;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.springframework.stereotype.Component;

import com.capinfo.crypt.RSA_MD5;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.component.payment.plugin.payease.Md5;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.model.PayEnable;
import com.enation.app.shop.core.plugin.payment.AbstractPaymentPlugin;
import com.enation.app.shop.core.plugin.payment.IPaymentEvent;
import com.enation.app.shop.core.plugin.payment.PaySuccessProcessorFactory;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.eop.sdk.utils.DateUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.IPluginBundle;
import com.enation.framework.util.StringUtil;

/**
 * 易支付在线支付插件
 */
@Component
public class YandexPlugin extends AbstractPaymentPlugin implements
		IPaymentEvent {
	private IStoreOrderManager storeOrderManager;
	private IOrderManager orderManager;
	private IProductManager productManager;
	private IMemberManager memberManager;
	private String callbackUrl;
	private String payedOrderSn;
	private String signature;
	public static final String FILESEPARATOR = System.getProperty("file.separator");

	public YandexPlugin() {
		super();
	}

	public YandexPlugin(IPluginBundle pluginBundle) {
		pluginBundle.registerPlugin(this);
	}

	@Override
	public String getId() {
		return "yandexPlugin";
	}

	@Override
	public String getName() {
		return "首信易支付网上支付";
	}

	@Override
	public String onPay(PayCfg payCfg, PayEnable order) {
		Map<String, String> params = paymentManager.getConfigParams(this
				.getId());

		// 当前时间 yyyyMMdd
		String v_ymd = DateUtil.toString(new Date(), "yyyyMMdd");
		// 请求地址
		String v_action_url = params.get("v_action_url");
		// 商户号
		String v_mid = params.get("v_mid");
		// 订单号
		String strReq = order.getSn();
		// 支付订单号
		String v_oid = v_ymd + "-" + v_mid + "-" + strReq;
		// 收货人姓名
		String v_rcvname = params.get("v_mid");
		// 收货人地址
		String v_rcvaddr = params.get("v_mid");
		// 收货人电话
		String v_rcvtel = params.get("v_mid");
		// 收货人邮政编码
		String v_rcvpost = params.get("v_mid");
		// 订单总金额
		// String v_amount = "123.45";
		String v_amount = String.valueOf(order.getNeedPayMoney());
		// 支付返回商户页面调用地址
		String v_url = this.getCallBackUrl(payCfg, order);
		// 商户配货状态 0 未配齐 1 已配齐
		String v_orderstatus = params.get("v_orderstatus");
		// 订货人姓名
		String v_ordername = params.get("v_mid");
		// 支付币种 0为人民币
		String v_moneytype = params.get("v_moneytype");
		// 数字签名
		String signature = params.get("v_signature");
		this.signature = signature;

		// MD5 明文
		String md5Source = v_moneytype + v_ymd + v_amount + v_rcvname + v_oid
				+ v_mid + v_url;
		// String md5Source = v_moneytype + v_ymd + v_amount + v_rcvname + v_oid
		// + v_mid + v_url;
		// 数字指纹
		String v_md5info = getMd5Info(signature, md5Source);
		// 新增参数
		// -----------------------------
		// 设置支付参数
		// -----------------------------
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("v_mid", params.get("v_mid"));
		sParaTemp.put("v_oid", v_oid);
		sParaTemp.put("v_rcvname", v_rcvname);
		sParaTemp.put("v_rcvaddr", v_rcvaddr);
		sParaTemp.put("v_rcvtel", v_rcvtel);
		sParaTemp.put("v_rcvpost", v_rcvpost);
		sParaTemp.put("v_amount", v_amount);
		sParaTemp.put("v_ymd", v_ymd);
		sParaTemp.put("v_orderstatus", v_orderstatus);
		sParaTemp.put("v_ordername", v_ordername);
		sParaTemp.put("v_moneytype", v_moneytype);
		sParaTemp.put("v_url", v_url);
		sParaTemp.put("v_md5info", v_md5info);

		return buildRequest(sParaTemp, v_action_url);
	}

	@Override
	public String onCallBack(String ordertype) {
		Map<String, String> params = paymentManager.getConfigParams(this
				.getId());
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();

		// 获取返回参数数据
		String v_url = request.getParameter("v_url");
		String v_oid = request.getParameter("v_oid");
		String v_pmode = request.getParameter("v_pmode");
		// String v_card = request.getParameter("v_card");
		try {
			v_pmode = java.net.URLDecoder.decode(v_pmode, "UTF-8"); 
//			String v_pmode2 = java.net.URLDecoder.decode(v_pmode, "GB2312"); 
//			String v_pmode3 = URLEncoder.encode(v_pmode, "GB2312");
//			String v_pmode4 = URLEncoder.encode(v_pmode, "GBK");
//			String v_pmode5 = URLEncoder.encode(v_pmode, "UTF-8");
			//加入此行代码，乱码得到解决
//			v_idname = URLEncoder.encode(member.getName(), "GBK");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String v_pstatus = request.getParameter("v_pstatus");
		String v_pstring = request.getParameter("v_pstring");
		try {
			v_pstring = java.net.URLDecoder.decode(v_pstring, "GBK");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String v_md5info = request.getParameter("v_md5info");
		String v_amount = request.getParameter("v_amount");
		String v_moneytype = request.getParameter("v_moneytype");
		String v_md5money = request.getParameter("v_md5money");
		String v_sign = request.getParameter("v_sign");
		String md5Source = v_oid + v_pstatus + v_amount + v_moneytype;
		String md5Money = v_amount + v_moneytype;
		String md5Info = v_oid + v_pstatus + v_pstring + v_pmode;

		String order_id = null;
		String[] v_oidArr = v_oid.split("-");
		if (v_oidArr.length == 3) {
			order_id = v_oidArr[2];
		}
		// 验证商城签名
		boolean verified = verifyMd5Info(v_sign, md5Source);
		// 验证数字签名
//		boolean verifiedMd5money = verifyMd5Money(v_md5money, signature,
//				md5Money);
		// 验证数字签名
//		boolean verifiedMd5info = verifyMd5info(v_md5info, signature, md5Info);
		// 验证数字签名
		boolean verifiedMd5money = verifyMd5moneyMd5(v_md5money, signature,
				md5Money);
		// 验证数字签名
//		boolean verifiedMd5info2 = verifyMd5infoMd5(v_md5info, signature, md5Info);
		// boolean verified = true;
		if (verified && verifiedMd5money) {
			this.paySuccess(order_id, v_oid, ordertype);
			this.payedOrderSn = order_id;
			// return order_id;
			return "sent";
		} else {
			throw new RuntimeException("验证失败");
		}
	}

	@Override
	public String onBackgroundCallBack(String ordertype) {
		Map<String, String> params = paymentManager.getConfigParams(this
				.getId());
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();

		// 获取返回参数数据
		// String v_url = request.getParameter("v_url");
		String v_oid = request.getParameter("v_oid");
		String v_pmode = request.getParameter("v_pmode");
		// String v_card = request.getParameter("v_card");
		String v_count = request.getParameter("v_count");
		try {
			v_pmode = java.net.URLDecoder.decode(v_pmode, "UTF-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String v_pstatus = request.getParameter("v_pstatus");
		String v_pstring = request.getParameter("v_pstring");
		try {
			v_pstring = java.net.URLDecoder.decode(v_pstring, "GBK");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		// String v_md5info = request.getParameter("v_md5info");
		String v_amount = request.getParameter("v_amount");
		String v_moneytype = request.getParameter("v_moneytype");
		String v_mac = request.getParameter("v_mac");
		String v_md5money = request.getParameter("v_md5money");
		String v_sign = request.getParameter("v_sign");
		String md5Source = v_oid + v_pstatus + v_amount + v_moneytype + v_count;
		String md5Money = v_amount + v_moneytype;
		String mac = v_oid + v_pmode + v_pstatus + v_pstring + v_count;
		String order_id = null;
		String[] v_oidArr = v_oid.split("-");
		if (v_oidArr.length == 3) {
			order_id = v_oidArr[2];
		}

		boolean verified = verifyMd5Info(v_sign, md5Source);
		boolean verifiedMd5money = verifyMd5moneyMd5(v_md5money, signature,
				md5Money);
//		boolean verifiedMac = verifyMd5Mac(v_mac, signature, mac);
		// boolean verified = true;
		if (verified && verifiedMd5money) {
			this.paySuccess(order_id, v_oid, ordertype);
			this.payedOrderSn = order_id;
			return "sent";
		} else {
			throw new RuntimeException("验证失败");
		}
	}

	@Override
	public String onReturn(String ordertype) {
		return payedOrderSn;
	}

	/**
	 * 支付成功后调用此方法来改变订单的状态
	 * 
	 * @param ordersn
	 *            订单编号
	 * @param tradeno
	 *            第三方交易流水号
	 * @param ordertype
	 *            订单类型 已知的：standard 标准订单，credit:信用账户充值
	 */
	protected void paySuccess(String ordersn, String tradeno, String ordertype) {
		PaySuccessProcessorFactory.getProcessor(ordertype).paySuccess(ordersn,
				tradeno, ordertype);
	}

	public static String buildRequest(Map<String, String> sParaTemp,
			String v_action_url) {
		Map sPara = paraFilter(sParaTemp);
		ArrayList keys = new ArrayList(sPara.keySet());
		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append("<form id=\"yandexsubmit\" name=\"yandexsubmit\" action=\""
				+ v_action_url + "\" accept-charset=\"GBK\" method=\"post\">");

		for (int i = 0; i < keys.size(); ++i) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);
			sbHtml.append("<input type=\"hidden\" name=\"" + name
					+ "\" value=\"" + value + "\"/>");
		}

		sbHtml.append("<input type=\"submit\" value=\"确认支付\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms[\'yandexsubmit\'].submit();</script>");
		return sbHtml.toString();
	}

	public static Map<String, String> paraFilter(Map<String, String> sArray) {
		HashMap result = new HashMap();
		if (sArray != null && sArray.size() > 0) {
			Iterator keys = sArray.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = (String) sArray.get(key);
				if (!StringUtil.isEmpty(value)) {
					result.put(key, value);
				}
			}
			return result;
		} else {
			return result;
		}
	}

	/**
	 * 获取支付时的 数字指纹信息
	 * 
	 * @param signature
	 * @param md5Source
	 * @return
	 */
	public String getShaMd5Info(String signature, String md5Source) {
		return HmacSha2.hmacSha2(md5Source, signature);
	}

	/**
	 * 获取支付时的 数字指纹信息
	 * 
	 * @param signature
	 * @param md5Source
	 * @return
	 */
	public String getMd5Info(String signature, String md5Source) {
		Md5 md5 = new Md5("");
		try {
			md5.hmac_Md5(md5Source, signature);
			byte b[] = md5.getDigest();
			return md5.stringify(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 校验返回商城数据签名
	 * 
	 * @param v_sign
	 * @param md5Source
	 * @return
	 */
	@Test
	public boolean verifyMd5Info(String v_sign, String md5Source) {
		// File directory = new File("Public1024.key"); // 设定为当前文件夹
		// String fullFilePath = directory.getAbsolutePath(); // 获取文件完整路径
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String  RealPath = request.getSession().getServletContext().getRealPath("/");
		String fullFilePath = RealPath+"themes"+FILESEPARATOR+"Public1024.key";
		/*String fullFilePath = this.getClass().getClassLoader().getResource("")
				.getPath()
				+ "Public1024.key";*/
		RSA_MD5 rsaMD5 = new RSA_MD5();
		int verifyStatus = rsaMD5.PublicVerifyMD5(fullFilePath, v_sign,
				md5Source);
		if (verifyStatus == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验返回数字指纹
	 * 
	 * @param v_sign
	 * @param md5Source
	 * @return
	 */
	public boolean verifyMd5Money(String v_md5money, String signature,
			String md5Money) {
		if (signature == null) {
			signature = "test";
		}
		String sh2 = HmacSha2.hmacSha2(md5Money, signature);
		if (sh2.equals(v_md5money)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验返回数字指纹
	 * 
	 * @param v_sign
	 * @param md5Source
	 * @return
	 */
	public boolean verifyMd5Mac(String v_mac, String signature, String mac) {
		if (signature == null) {
			signature = "test";
		}
		String sh2 = HmacSha2.hmacSha2(mac, signature);
		if (sh2.equals(v_mac)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验返回数字指纹
	 * 
	 * @param v_sign
	 * @param md5Source
	 * @return
	 */
	public boolean verifyMd5info(String v_md5info, String signature,
			String md5Info) {
		if (signature == null) {
			signature = "test";
		}
		String sh2 = HmacSha2.hmacSha2(md5Info, signature);
		if (sh2.equals(v_md5info)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验返回数字指纹
	 * 
	 * @param v_sign
	 * @param md5Source
	 * @return
	 */
	public boolean verifyMd5infoMd5(String v_md5info, String signature,
			String md5Info) {
		if (signature == null) {
			signature = "test";
		}
		Md5 md5 = new Md5("");
		try {
			md5.hmac_Md5(md5Info, signature);
			byte b[] = md5.getDigest();
			String sh2 = md5.stringify(b);
			if (sh2.equals(v_md5info)) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 校验返回数字指纹
	 * 
	 * @param v_sign
	 * @param md5Source
	 * @return
	 */
	public boolean verifyMd5moneyMd5(String v_md5money, String signature,
			String md5Money) {
		if (signature == null) {
			signature = "test";
		}
		Md5 md5 = new Md5("");
		try {
			md5.hmac_Md5(md5Money, signature);
			byte b[] = md5.getDigest();
			String sh2 = md5.stringify(b);
			if (sh2.equals(v_md5money)) {
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		String v_pmode = null;
		try {
			v_pmode = java.net.URLDecoder.decode(
					"%D5%D0%C9%CC%D2%F8%D0%D0%D2%BB%CD%F8%CD%A8", "GBK");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(v_pmode);
	}

	protected String getCallBackUrl(PayCfg payCfg, PayEnable order) {
		if (callbackUrl != null)
			return callbackUrl;
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String serverName = request.getServerName();
		int port = request.getLocalPort();//获取实际应用端口号
		int port1 = request.getServerPort();//获取的是URL请求的端口
		String portstr = "";
		if (port1 != 80) {
			portstr = ":" + port1;
		}
		String contextPath = request.getContextPath();
		// return "http://" + serverName + portstr + contextPath + "/api/shop/"
		// + order.getOrderType() + "_" + payCfg.getType()
		// + "_payment-callback.do";
		return "http://" + serverName + portstr + contextPath + "/api/shop/"
				+ "b" + "_" + payCfg.getType() + "_payment-callback.do";
	}

	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}

	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public IProductManager getProductManager() {
		return productManager;
	}

	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public String getPayedOrderSn() {
		return payedOrderSn;
	}

	public void setPayedOrderSn(String payedOrderSn) {
		this.payedOrderSn = payedOrderSn;
	}

}
