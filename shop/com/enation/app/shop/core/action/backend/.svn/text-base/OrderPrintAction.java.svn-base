package com.enation.app.shop.core.action.backend;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.enation.framework.util.*;

import net.sf.json.JSONObject;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.service.IOrderPrintManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.framework.action.WWAction;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;
/**
 * 订单发货 Action
 * @author lina
 *
 */
@Component
@Scope("prototype")
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Action("orderPrint")
@Results({
})
public class OrderPrintAction extends WWAction {
	private IMemberManager memberManager ;
	private IOrderPrintManager orderPrintManager;
	private Integer[] order_id;
	private String[] expressno;//批量物流单号
	private String[] logi_id;	//物流公司Id
	private String logi_name;
	private EmailProducer mailMessageProducer;
	public EmailProducer getMailMessageProducer() {
		return mailMessageProducer;
	}

	public void setMailMessageProducer(EmailProducer mailMessageProducer) {
		this.mailMessageProducer = mailMessageProducer;
	}

	/**
	 * 订单发货Action
	 * @param order_id 订单号数组,Integer[]
	 * @return json
	 * result 1,操作成功.0,操作失败
	 */
	public String ship(){
		try{
			String is_ship= orderPrintManager.ship(order_id);
			if(is_ship.equals("true")){
				this.showSuccessJson("发货成功");
				this.sendEmailToAdmin(order_id);

			}else{
				this.showErrorJson(is_ship);
			}
		}catch(Exception e){
			e.printStackTrace();
			this.showErrorJson(e.getMessage());
			this.logger.error("发货出错", e);
		}
		return this.JSON_MESSAGE;
	}
	@SuppressWarnings("unchecked")
	public  void  sendEmailToAdmin(Integer[] order_id){
	
		List<Order> orderList =orderPrintManager.getOrder(order_id);
		for (Order order : orderList) {
			int memberid=order.getMember_id();	
			Member member	=this.memberManager.getmember(memberid);
			int  orderid=order.getOrder_id();
			List<OrderItem>  item =orderPrintManager.getListItem(orderid);
			String allGoodsName = "";
			for (OrderItem orderItem : item) {
				allGoodsName+= orderItem.getName()+"     ";
			}
			EopSite site = EopSite.getInstance();
			EmailModel emailModel = new EmailModel();
			String domain =RequestUtil.getDomain();
			emailModel.getData().put("logo", site.getLogofile());
			emailModel.getData().put("sitename", site.getSitename());
			emailModel.getData().put("username", member.getUname());
		
			emailModel.getData().put("send_time",
				DateUtil.toString(new Date(), "yyyy年MM月dd日  hh:mm:ss"));
			emailModel.getData().put("domain", domain);
			emailModel.getData().put("order", order.getSn());
			emailModel.getData().put("goods", allGoodsName);
			emailModel.setTitle(member.getUname()+"您好，"+site.getSitename()+"会员发货成功!");

			emailModel.setEmail(member.getEmail());
			//	emailModel.setEmail(this.smtpManager.get(2).getUsername());
			emailModel.setTemplate("fahuo_email_template.html");
			emailModel.setEmail_type("发货成功");
			mailMessageProducer.send(emailModel);
		}
	}

	
	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	/**
	 * 保存发货单号
	 * @param order_id 订单号数组,Integer[]
	 * @param expressno 物流单号数组,String[]
	 * @return json
	 * result 1,操作成功.0,操作失败
	 */
	public String saveShipNo(){
		try {
			for (int i = 0; i < expressno.length; i++) {
				if(expressno[i].equals("")){
					this.showErrorJson("物流运单号不能为空。");
					return this.JSON_MESSAGE;
				}
			}
			String[] logiName=null;
			if(!logi_name.toString().isEmpty()){
				if(logi_name.toString().equalsIgnoreCase("后台系统顺丰")){
					logi_id[0]="-2";
				}
				logiName=logi_name.toString().split(",");
			}else{
				logi_name="后台系统顺丰";
				logi_id[0]="-2";
				logiName=logi_name.toString().split(",");
			}
			this.orderPrintManager.saveShopNos(order_id, expressno,logi_id,logiName);
			this.showSuccessJson("保存发货单号成功");
			
		} catch (Exception e) {
			this.showErrorJson(e.getMessage());
			this.logger.error("保存发货单号出错", e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 打印快递单
	 * @author LiFenLong
	 * @param order_id 订单号数组,Integer[]
	 * @param script 打印的script,String
	 * @return json
	 * result 1,操作成功.0,操作失败
	 * script 打印的script
	 */
	public String expressScript(){
			String script= orderPrintManager.getExpressScript(order_id);
			if(script.equals("快递单选择配送方式不同")||script.equals("请添加配送方式")||script.equals("没有此快递单模板请添加")||script.equals("请选择默认发货点")){
				this.showErrorJson(script);
			}else{
				Map map = new HashMap();
				map.put("script", script);
				map.put("result", 1);
				this.json=JSONObject.fromObject(map).toString();
			}
		return this.JSON_MESSAGE;
	}
	/**
	 * 打印发货单
	 * @author LiFenLong
	 * @param order_id 订单号数组,Integer[]
	 * @param script 打印的script,String
	 * @return 发货单的script
	 */
	public String shipScript() {
			String script= orderPrintManager.getShipScript(order_id);
			this.json=script;
		return this.JSON_MESSAGE;
	}

	public IOrderPrintManager getOrderPrintManager() {
		return orderPrintManager;
	}

	public void setOrderPrintManager(IOrderPrintManager orderPrintManager) {
		this.orderPrintManager = orderPrintManager;
	}

	public Integer[] getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer[] order_id) {
		this.order_id = order_id;
	}

	public String[] getExpressno() {
		return expressno;
	}

	public void setExpressno(String[] expressno) {
		this.expressno = expressno;
	}

	public String[] getLogi_id() {
		return logi_id;
	}

	public void setLogi_id(String[] logi_id) {
		this.logi_id = logi_id;
	}

	public String getLogi_name() {
		return logi_name;
	}

	public void setLogi_name(String logi_name) {
		this.logi_name = logi_name;
	}

}
