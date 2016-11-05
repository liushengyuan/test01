package com.enation.app.shop.component.payment.plugin.alipay.direct;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.alipay.config.AlipayConfig;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.enation.app.shop.core.model.PayCfg;
import com.enation.app.shop.core.model.PayEnable;
import com.enation.app.shop.core.plugin.payment.AbstractPaymentPlugin;
import com.enation.app.shop.core.plugin.payment.IPaymentEvent;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

/**
 * 支付宝即时到账插件 
 * @author kingapex
 *
 */
@Component
public class AlipayDirectPlugin extends AbstractPaymentPlugin  implements IPaymentEvent {


	@Override
	public String onCallBack(String ordertype) {
		Map<String,String> cfgparams = paymentManager.getConfigParams(this.getId());
		
		String key =cfgparams.get("key");
		String partner =cfgparams.get("partner");
		String encoding= cfgparams.get("callback_encoding");
		AlipayConfig.key=key;
		AlipayConfig.partner=partner;
		AlipayConfig.input_charset=encoding;
		
		HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
		 
		//获取支付宝POST过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			//valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
			//valueStr = StringUtil.toUTF8(valueStr);
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
		//商户订单号
		 String out_trade_no = StringUtil.toUTF8(request.getParameter("out_trade_no") );
		//支付宝交易号

		String trade_no =StringUtil.toUTF8(request.getParameter("trade_no") );// new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = StringUtil.toUTF8(request.getParameter("trade_status") );//new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");

		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

		if(AlipayNotify.verify(params)){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码
			this.paySuccess(out_trade_no,trade_no, ordertype);
			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			
			if(trade_status.equals("TRADE_FINISHED")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
			} else if (trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
					
				//注意：
				//付款完成后，支付宝系统发送该交易状态通知
			}

			//——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
				
			return ("success");	//请不要修改或删除

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{//验证失败
			return ("fail");
		}
		
	} 

	
 
	@Override
	public String onPay(PayCfg payCfg, PayEnable order) {
		
			Map<String,String> params = paymentManager.getConfigParams(this.getId());
			String seller_email =params.get("seller_email");
			String partner =params.get("partner");
			String key =  params.get("key");
			String encoding= params.get("callback_encoding");
			AlipayConfig.key=key;
			AlipayConfig.partner=partner;
			AlipayConfig.input_charset=encoding;
			String show_url = this.getShowUrl(order);
			String notify_url = this.getCallBackUrl(payCfg,order);
			String return_url =this.getReturnUrl(payCfg,order);
		    
			this.logger.info("notify_url is ["+notify_url+"]");
			
			////////////////////////////////////请求参数//////////////////////////////////////
			
			//必填参数//

			//请与贵网站订单系统中的唯一订单号匹配
			String out_trade_no = order.getSn();
			//订单名称，显示在支付宝收银台里的“商品名称”里，显示在支付宝的交易管理的“商品名称”的列表里。
			String subject = "订单:" + order.getSn(); 
			//订单描述、订单详细、订单备注，显示在支付宝收银台里的“商品描述”里
			String body = "网店订单"; 
			//订单总金额，显示在支付宝收银台里的“应付总额”里
			String total_fee =String.valueOf( order.getNeedPayMoney());
			
			
			//扩展功能参数——默认支付方式//
			
			//默认支付方式，取值见“即时到帐接口”技术文档中的请求参数列表
			String paymethod = "";
			
			HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
			//默认网银代号，代号列表见“即时到帐接口”技术文档“附录”→“银行列表”
			//String defaultbank = "";
			String defaultbank = request.getParameter("bank");			
			defaultbank=defaultbank==null?"":defaultbank;
			
			//如果是数字则说明选择的是支付宝，快钱
			Pattern pattern = Pattern.compile("[0-9]*");
			if(pattern.matcher(defaultbank).matches()){
				defaultbank="";
			}
			
			//扩展功能参数——防钓鱼//

			//防钓鱼时间戳
			String anti_phishing_key  = "";
			//获取客户端的IP地址，建议：编写获取客户端IP地址的程序
			String exter_invoke_ip= "";
			//注意：
			//1.请慎重选择是否开启防钓鱼功能
			//2.exter_invoke_ip、anti_phishing_key一旦被设置过，那么它们就会成为必填参数
			//3.开启防钓鱼功能后，服务器、本机电脑必须支持远程XML解析，请配置好该环境。
			//4.建议使用POST方式请求数据
			//示例：
			//anti_phishing_key = AlipayService.query_timestamp();	//获取防钓鱼时间戳函数
			//exter_invoke_ip = "202.1.1.1";
			
			//扩展功能参数——其他///
			
			//自定义参数，可存放任何内容（除=、&等特殊字符外），不会显示在页面上
			String extra_common_param = "";
			//默认买家支付宝账号
			String buyer_email = "";
			//商品展示地址，要用http:// 格式的完整路径，不允许加?id=123这类自定义参数
			
			//扩展功能参数——分润(若要使用，请按照注释要求的格式赋值)//
			
			//提成类型，该值为固定值：10，不需要修改
			String royalty_type = "";
			//提成信息集
			String royalty_parameters ="";
			//注意：
			//与需要结合商户网站自身情况动态获取每笔交易的各分润收款账号、各分润金额、各分润说明。最多只能设置10条
			//各分润金额的总和须小于等于total_fee
			//提成信息集格式为：收款方Email_1^金额1^备注1|收款方Email_2^金额2^备注2
			//示例：
			//royalty_type = "10"
			//royalty_parameters	= "111@126.com^0.01^分润备注一|222@126.com^0.01^分润备注二"
			
			//////////////////////////////////////////////////////////////////////////////////
			
			//把请求参数打包成数组
			Map<String, String> sParaTemp = new HashMap<String, String>();
	        sParaTemp.put("payment_type", "1");
	        sParaTemp.put("out_trade_no", out_trade_no);
	        sParaTemp.put("subject", subject);
	        sParaTemp.put("body", body);
	        sParaTemp.put("total_fee", total_fee);
	        sParaTemp.put("show_url", show_url);
	        sParaTemp.put("paymethod", paymethod);
	        sParaTemp.put("defaultbank", defaultbank);
	        sParaTemp.put("anti_phishing_key", anti_phishing_key);
	        sParaTemp.put("exter_invoke_ip", exter_invoke_ip);
	        sParaTemp.put("extra_common_param", extra_common_param);
	        sParaTemp.put("buyer_email", buyer_email);
	        sParaTemp.put("royalty_type", royalty_type);
	        sParaTemp.put("royalty_parameters", royalty_parameters);
	        

	    	//增加基本配置
	        sParaTemp.put("service", "create_direct_pay_by_user");
	        sParaTemp.put("partner",  partner);
	        sParaTemp.put("return_url",  return_url);
	        sParaTemp.put("notify_url",  notify_url);
	        sParaTemp.put("seller_email", seller_email);
	        sParaTemp.put("_input_charset", AlipayConfig.input_charset);

	  

	        return  AlipaySubmit.buildRequest(sParaTemp,"get","确认");
	        
	       
	}

	 
	
	
	@Override
	public String  onReturn(String ordertype) {
		Map<String,String> cfgparams = paymentManager.getConfigParams(this.getId());
		HttpServletRequest request  =  ThreadContextHolder.getHttpRequest();
		String key =cfgparams.get("key");
		String partner =cfgparams.get("partner");
		String encoding= cfgparams.get("return_encoding");
		AlipayConfig.key=key;
		AlipayConfig.partner=partner;
		AlipayConfig.input_charset=encoding;
		//获取支付宝GET过来反馈信息
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			//乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//			valueStr =  new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
		//	valueStr = StringUtil.toUTF8(valueStr);
			params.put(name, valueStr);
		}
		
		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//

		//商户订单号
		 String out_trade_no = StringUtil.toUTF8(request.getParameter("out_trade_no") );
		//支付宝交易号

		String trade_no =StringUtil.toUTF8(request.getParameter("trade_no") );// new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

		//交易状态
		String trade_status = StringUtil.toUTF8(request.getParameter("trade_status") );//new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");


		//获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
		
		//计算得出通知验证结果
		boolean verify_result = AlipayNotify.verify(params);
		
		if(verify_result){//验证成功
			//////////////////////////////////////////////////////////////////////////////////////////
			//请在这里加上商户的业务逻辑程序代码

			//——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
			if(trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")){
				//判断该笔订单是否在商户网站中已经做过处理
					//如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
					//如果有做过处理，不执行商户的业务程序
			}
			
			//该页面可做页面美工编辑
			this.paySuccess(out_trade_no,trade_no,ordertype);
			return out_trade_no;

			//////////////////////////////////////////////////////////////////////////////////////////
		}else{
			//该页面可做页面美工编辑
			throw new RuntimeException("验证失败");  
		}
		
	 
		 
	}

	


	@Override
	public String getId() {
		
		return "alipayDirectPlugin";
	}

	@Override
	public String getName() {
		
		return "支付宝即时到帐接口";
	}



	@Override
	public String onBackgroundCallBack(String ordertype) {
		// TODO Auto-generated method stub
		return null;
	}



}
