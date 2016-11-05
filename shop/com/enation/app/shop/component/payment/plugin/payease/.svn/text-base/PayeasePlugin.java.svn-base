package com.enation.app.shop.component.payment.plugin.payease;

import com.capinfo.crypt.RSA_MD5;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.model.PayEnable;
import com.enation.app.shop.core.plugin.payment.AbstractPaymentPlugin;
import com.enation.app.shop.core.plugin.payment.IPaymentEvent;
import com.enation.app.shop.core.plugin.payment.PaySuccessProcessorFactory;
import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.eop.sdk.utils.DateUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 易支付在线支付插件
 */
@Component
public class PayeasePlugin extends AbstractPaymentPlugin implements
		IPaymentEvent {
	private IAllianceCountManager allianceCountManager;
	private String callbackUrl;
	private String payedOrderSn;
	@Override
	public String getId() {
		return "payeasePlugin";
	}

	@Override
	public String getName() {
		return "Yandex(首信易支付网上支付)";
	}

	@Override
	public String onPay(PayCfg payCfg, PayEnable order) {
		Map<String, String> params = paymentManager.getConfigParams(this
				.getId());

		// 当前时间 yyyyMMdd
		String v_ymd = DateUtil.toString(new Date(), "yyyyMMdd");

		// //////////////////////////////////请求参数//////////////////////////////////////
		// 请求地址
		String v_action_url = params.get("v_action_url");
		// 商户号
		String v_mid = params.get("v_mid");
		// 支付币种
		String v_moneytype = params.get("v_moneytype");
		// 支付卡类型
		String v_card = params.get("v_card");
		// 支付方式
		String v_pmode = params.get("v_pmode");
		String v_currency = order.getCurrency();
		// if (!StringUtil.isEmpty(v_currency)) {
		// if (v_currency.equals("RUR")) {
		// v_mid = params.get("v_mid_ru");
		// v_pmode = params.get("v_pmode_ru");
		// v_moneytype = params.get("v_moneytype_ru");
		// } else if (v_currency.equals("USD")) {
		// v_mid = params.get("v_mid_en");
		// v_pmode = params.get("v_pmode_en");
		// v_moneytype = params.get("v_moneytype_en");
		// }
		// }
		// 订单号
		String strReq = order.getSn();
		// 支付订单号
		String v_oid = v_ymd + "-" + v_mid + "-" + strReq;
		// 订单总金额
		String v_amount = String.valueOf(order.getNeedPayMoney());
		// 支付返回调用地址
		String v_url = this.getCallBackUrl(payCfg, order);

		// 数字签名
		String signature = params.get("v_signature");
		//
		String v_rcvname = params.get("v_mid");
		// MD5 明文
		String md5Source = v_moneytype + v_ymd + v_amount + v_rcvname + v_oid
				+ v_mid + v_url;
		// 数字指纹
		String v_md5info = getMd5Info(signature, md5Source);

		//System.out.println("自己的v_md5info:" + v_md5info);
		this.logger.info("自己的v_md5info:" + v_md5info);
		// -----------------------------
		// 设置支付参数
		// -----------------------------
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("v_mid", params.get("v_mid"));
		sParaTemp.put("v_oid", v_oid);
		sParaTemp.put("v_rcvname", params.get("v_mid"));
		sParaTemp.put("v_rcvaddr", params.get("v_mid"));
		sParaTemp.put("v_rcvtel", params.get("v_mid"));
		sParaTemp.put("v_rcvpost", params.get("v_mid"));
		sParaTemp.put("v_amount", v_amount);
		sParaTemp.put("v_ymd", v_ymd);
		sParaTemp.put("v_orderstatus", params.get("v_orderstatus"));
		sParaTemp.put("v_ordername", params.get("v_mid"));
		sParaTemp.put("v_moneytype", v_moneytype);
		sParaTemp.put("v_url", v_url);
		sParaTemp.put("v_md5info", v_md5info);
		sParaTemp.put("v_pmode", v_pmode);
		sParaTemp.put("v_card", v_card);
		//System.out.println(sParaTemp.toString());
		this.logger.info("sParaTemp:" + sParaTemp.toString());
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
		//System.out.println("v_url:" + v_url);
		this.logger.info("v_url:" + v_url);
		String v_oid = request.getParameter("v_oid");
		//System.out.println("v_oid:" + v_oid);
		this.logger.info("v_oid:" + v_oid);
		String v_pmode = request.getParameter("v_pmode");
		//System.out.println("v_pmode:" + v_pmode);
		this.logger.info("v_pmode:" + v_pmode);
		String v_card = request.getParameter("v_card");
		//System.out.println("v_card:" + v_card);
		this.logger.info("v_card:" + v_card);
		String v_count = request.getParameter("v_count");
		//System.out.println("v_count:" + v_count);
		this.logger.info("v_count:" + v_count);
		try {
			v_pmode = java.net.URLDecoder.decode(v_pmode, "GB2312");
			//System.out.println("v_pmode:" + v_pmode);
			this.logger.info("v_pmode:" + v_pmode);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String v_pstatus = request.getParameter("v_pstatus");
		String v_pstring = request.getParameter("v_pstring");
		try {
			v_pstring = java.net.URLDecoder.decode(v_pstring, "GB2312");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String v_md5info = request.getParameter("v_md5info");
		String v_amount = request.getParameter("v_amount");
		//System.out.println("v_amount:" + v_amount);
		this.logger.info("v_amount:" + v_amount);
		String v_moneytype = request.getParameter("v_moneytype");
		//System.out.println("v_moneytype:" + v_moneytype);
		this.logger.info("v_moneytype:" + v_moneytype);
		String v_md5money = request.getParameter("v_md5money");
		String v_sign = request.getParameter("v_sign");
		//System.out.println("v_pstatus:" + v_pstatus);
		this.logger.info("v_pstatus:" + v_pstatus);
		//System.out.println("v_sign:" + v_sign);
		this.logger.info("v_sign:" + v_sign);
		//System.out.println("v_md5info:" + v_md5info);
		this.logger.info("v_md5info:" + v_md5info);
		//System.out.println("v_pstring:" + v_pstring);
		this.logger.info("v_pstring:" + v_pstring);
		//System.out.println("v_md5money:" + v_md5money);
		this.logger.info("v_md5money:" + v_md5money);
		String md5Source = v_oid + v_pstatus + v_amount + v_moneytype + v_count;
		//System.out.println("md5Source:" + md5Source);
		this.logger.info("md5Source:" + md5Source);
		String order_id = null;
		String[] v_oidArr = v_oid.split("-");
		if (v_oidArr.length == 3) {
			order_id = v_oidArr[2];
			this.payedOrderSn = order_id;
		}

		// boolean verified = verifyMd5Info("20150804-8445-1438656364292013.457"
		// , "20150804-8445-1438656364292013.457");
		// v_sign="9a8f05e66c08484757217daf825c5f507f269b1f3018a012635101e201e61e0362cd59f673bb9be461d8a581ec1f68da7715589f22bfc9cfc0f59803046670a703dedfda6d015786cb8fa4393de4ba780ebfd97fb302278eb9776d48d36e0e3498c2a1f8aa7e6adffa83ff216e06bf61b8d912de9719e7485b23049fe6ce680c";
		// md5Source="20150806-8445-1438838845622010.007";
		// String
		// v_sign2="088b6707b568dfd5008df44075c48b5c5abe1c592f2d471f81c442f4caa230a8bb5161e0fa1606cb67fd8a10c8a9be6b2089cf12936e48b2888ef645b4206b3070c88000f68110b1eb1673eb0d1c5302121f1f4b2d822e68cc8ae17e92ce561fa8e939e9515e6da964526384fbd62373bb4bb812251025acf44b7a3a027a0d8b";
		// String md5Source2 = "20150807-8445-14389195013511071";
		boolean verified = verifyMd5Info(v_sign, md5Source);
		//System.out.println("verified:" + verified);
		this.logger.info("verified:" + verified);
		// boolean verified = true;
		if (verified) {
			this.paySuccess(order_id, v_oid, ordertype);
			// return order_id;
			
			/***************** 改变es_flow_count（流量统计表）的支付状态 ***********/
			HttpSession session = request.getSession();
            String session_id = session.getId();
            this.allianceCountManager.editOrderStatus(session_id);
			return "sent";
		} else {
			//System.out.println("验证失败");
			return "error";
			// throw new RuntimeException("验证失败");
		}
	}

	@Override
	public String onReturn(String ordertype) {
		return this.payedOrderSn;
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
		sbHtml.append("<form id=\"payeasesubmit\" name=\"payeasesubmit\" action=\""
				+ v_action_url + "\" method=\"post\">");

		for (int i = 0; i < keys.size(); ++i) {
			String name = (String) keys.get(i);
			String value = (String) sPara.get(name);
			sbHtml.append("<input type=\"hidden\" name=\"" + name
					+ "\" value=\"" + value + "\"/>");
		}

		sbHtml.append("<input type=\"submit\" value=\"确认支付\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms[\'payeasesubmit\'].submit();</script>");
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
	 * 校验返回的指纹信息
	 * 
	 * @param v_sign
	 * @param md5Source
	 * @return
	 */
	public boolean verifyMd5Info(String v_sign, String md5Source) {
		// File directory = new File("Public1024.key"); //设定为当前文件夹
		// String fullFilePath = directory.getAbsolutePath(); // 获取文件完整路径
		String fullFilePath = this.getClass().getClassLoader().getResource("")
				.getPath()
				+ "Public1024.key";
		//System.out.println("fullFilePath=" + fullFilePath);
		this.logger.info("fullFilePath=" + fullFilePath);
		RSA_MD5 rsaMD5 = new RSA_MD5();
		int verifyStatus = rsaMD5.PublicVerifyMD5(fullFilePath, v_sign,
				md5Source);
		//System.out.println("verifyStatus=" + verifyStatus);
		this.logger.info("verifyStatus=" + verifyStatus);
		if (verifyStatus == 0) {
			return true;
		} else {
			return false;
		}
	}

	protected String getCallBackUrl(PayCfg payCfg, PayEnable order) {
		if (callbackUrl != null)
			return callbackUrl;
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String serverName = request.getServerName();
		int port = request.getLocalPort();
		String portstr = "";
		if (port != 80) {
			portstr = ":" + port;
		}
		String contextPath = request.getContextPath();
		return "http://" + serverName + portstr + contextPath + "/api/shop/"
				+ order.getOrderType() + "_" + payCfg.getType()
				+ "_payment-callback.do";
	}

	public static void main(String[] args) {

	}

	@Override
	public String onBackgroundCallBack(String ordertype) {
		Map<String, String> params = paymentManager.getConfigParams(this
				.getId());
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		// 获取返回参数数据
		String v_url = request.getParameter("v_url");
		String v_oid = request.getParameter("v_oid");
		String v_pmode = request.getParameter("v_pmode");
		String v_card = request.getParameter("v_card");
		String v_count = request.getParameter("v_count");
		try {
			v_pmode = java.net.URLDecoder.decode(v_pmode, "GB2312");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String v_pstatus = request.getParameter("v_pstatus");
		String v_pstring = request.getParameter("v_pstring");
		try {
			v_pstring = java.net.URLDecoder.decode(v_pstring, "GB2312");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		String v_md5info = request.getParameter("v_md5info");
		String v_amount = request.getParameter("v_amount");
		String v_moneytype = request.getParameter("v_moneytype");
		String v_md5money = request.getParameter("v_md5money");
		String v_sign = request.getParameter("v_sign");
		String md5Source = v_oid + v_pstatus + v_amount + v_moneytype + v_count;
		String order_id = null;
		String[] v_oidArr = v_oid.split("-");
		if (v_oidArr.length == 3) {
			order_id = v_oidArr[2];
		}
		boolean verified = verifyMd5Info(v_sign, md5Source);
		//System.out.println("verified:" + verified);
		this.logger.info("verified:" + verified);
		// boolean verified = true;
		if (verified) {
			this.paySuccess(order_id, v_oid, ordertype);
			return "sent";
		} else {
			throw new RuntimeException("验证失败");
		}
	}

	public String getPayedOrderSn() {
		return payedOrderSn;
	}

	public void setPayedOrderSn(String payedOrderSn) {
		this.payedOrderSn = payedOrderSn;
	}

	public IAllianceCountManager getAllianceCountManager() {
		return allianceCountManager;
	}

	public void setAllianceCountManager(IAllianceCountManager allianceCountManager) {
		this.allianceCountManager = allianceCountManager;
	}
	
	
}
