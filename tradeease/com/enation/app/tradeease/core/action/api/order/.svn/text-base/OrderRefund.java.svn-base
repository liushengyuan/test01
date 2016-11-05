package com.enation.app.tradeease.core.action.api.order;
import java.io.IOException;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import com.enation.app.tradeease.core.util.SAXParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * 提交退款申请到支付平台的退款接口
 */

public class OrderRefund {
	//申请退款url
	private static final String REFUND_APPLY_URL = "https://api.yizhifubj.com/merchant/refund/ref_ack_submit.jsp";
	
	public String refundApply(Map map) throws IOException {
		//1.退款申请
		String R_XML =	refund(map) ;
		//System.out.println(R_XML);
		String ret = SAXParser.SAXParseNodeValue(R_XML,"statusdesc");
		//单笔退款申请，参照文档
		StringBuffer sb = new StringBuffer("");
		if (ret!=null && "Success".equals(ret)) {
			return ret;
		}else {
			//操作失败,获取操作失败的原因
			String statusdescall = SAXParser.SAXParseNodeValue(R_XML,"statusdesc");
			String statusdesc = statusdescall.substring(statusdescall.lastIndexOf(")")+1);
			if ("Invalid order number".equals(statusdesc)) {
				sb.append("没有符合退款条件的订单记录；</br>");
			}else if ("Invalid refund money".equals(statusdesc)) {
				sb.append("申请退款金额不合法；</br>");
			}else if ("Refund money exceed order limit".equals(statusdesc)) {
				sb.append("退款金额超过允许范围；</br>");
			}else if ("Fingerprint invalid".equals(statusdesc)) {
				sb.append("MD5验证数字指纹错误；</br>");
			}else if ("Invalid operator".equals(statusdesc)) {
				sb.append("退款申请操作员不存在；</br>");
			}else if ("Invalid operation permit".equals(statusdesc)) {
				sb.append("操作员没有退款处理操作权限；</br>");
			}else if ("Invalid merchant".equals(statusdesc)) {
				sb.append("商户编号非法；</br>");
			}else if ("The service for this merchant is unavailable".equals(statusdesc)) {
				sb.append("商户未开通退款服务；</br>");
			}else if ("Invalid request".equals(statusdesc)) {
				sb.append("退款申请请求IP地址不合法；</br>");
			}else if ("System error".equals(statusdesc)) {
				sb.append("系统错误；</br>");
			}else {
				sb.append("其他异常错误；</br>");
			}
		}
		//System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
	 * 退款申请
	 * @return
	 * @throws IOException
	 */
	public String refund(Map map) throws IOException {
		Client cc = Client.create();
		WebResource rr = cc.resource(REFUND_APPLY_URL);
		MultivaluedMap queryParams = new MultivaluedMapImpl();
		queryParams.add("v_mid", map.get("v_mid"));  
		queryParams.add("v_oid",map.get("v_oid"));
		queryParams.add("v_refamount",map.get("v_refamount"));
		queryParams.add("v_refreason",map.get("v_refreason"));
		queryParams.add("v_refoprt", map.get("v_refoprt"));
		queryParams.add("v_mac",  map.get("v_mac"));
		String ret = rr.queryParams(queryParams).post(String.class);
		return ret;
	}

}
