package com.enation.app.tradeease.core.action.api.message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.message.MessageCenter;
import com.enation.app.tradeease.core.model.message.MessageFlag;
import com.enation.app.tradeease.core.service.message.IBuyerMessageManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * 买家中心站内信
 * 
 * @author yanpeng 2015-6-12 下午6:11:49
 * 
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("buyerMessage")
@SuppressWarnings({ "rawtypes", "unchecked", "serial", "static-access" })
public class BuyerMessageApiAction extends WWAction {

	private IBuyerMessageManager buyerMessageManager;

	private String message_title;
	private String message_type;
	private String accept_member_id;
	private String message_text;
	private String message_state;
	private String start_message_time;
	private String end_message_time;

	/**
	 * 买家发送的消息
	 * 
	 * @param start_message_time
	 *            :起始时间
	 * @param end_message_time
	 *            ：终止时间
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	@SuppressWarnings("unused")
	public String searchSendMessages() {
		try {
			Member member = UserConext.getCurrentMember();
			Integer memberid = member.getMember_id();
			if (member == null) {
				this.showErrorJson("尚未登录，无权使用此api");
				return this.JSON_MESSAGE;
			}
			HttpServletRequest request = this.getRequest();
			String start_message_time = request
					.getParameter("start_message_time");
			String end_message_time = request.getParameter("end_message_time");
			//System.out.println(start_message_time);
			Map params = new HashMap();
			if (!StringUtil.isEmpty(start_message_time)) {
				long start_time = DateUtil.getDateline(start_message_time);
				params.put("start_time", start_time);
			} else {
				params.put("start_time", 0l);
			}
			if (!StringUtil.isEmpty(end_message_time)) {
				long end_time = DateUtil.getDateline(end_message_time);
				params.put("end_time", end_time);
			} else {
				params.put("end_time", 0l);
			}
			List<Map> sendMessagesList = this.buyerMessageManager
					.buyerMessageList(memberid, params);
			this.json = JsonMessageUtil.getListJson(sendMessagesList);
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("api调用失败" + e.getMessage());
			this.logger.error("查询买家发送的消息出错", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 买家接收的消息
	 * 
	 * @param start_message_time
	 *            :起始时间
	 * @param end_message_time
	 *            ：终止时间
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	@SuppressWarnings("unused")
	public String searchAcceptMessages() {
		try {
			Member member = UserConext.getCurrentMember();
			Integer memberid = member.getMember_id();
			if (member == null) {
				this.showErrorJson("尚未登录，无权使用此api");
				return this.JSON_MESSAGE;
			}
			HttpServletRequest request = this.getRequest();
			String start_message_time = request
					.getParameter("start_message_time");
			String end_message_time = request.getParameter("end_message_time");
			String message_state = request.getParameter("message_state");
			Map params = new HashMap();
			params.put("message_state", message_state);
			if (!StringUtil.isEmpty(start_message_time)) {
				long start_time = DateUtil.getDateline(start_message_time);
				params.put("start_time", start_time);
			} else {
				params.put("start_time", 0l);
			}
			if (!StringUtil.isEmpty(end_message_time)) {
				long end_time = DateUtil.getDateline(end_message_time);
				params.put("end_time", end_time);
			} else {
				params.put("end_time", 0l);
			}
			List<Map> acceptMessagesList = this.buyerMessageManager
					.buyerAcceptMessageList(memberid, params);
			this.json = JsonMessageUtil.getListJson(acceptMessagesList);
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("api调用失败" + e.getMessage());
			this.logger.error("查询买家接收的消息出错", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 买家产品咨询
	 * 
	 * @param start_message_time
	 *            :起始时间
	 * @param end_message_time
	 *            ：终止时间
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	public String askGoodsMessages() {
		try {
			Member member = UserConext.getCurrentMember();
			if (member == null) {
				this.showErrorJson("尚未登录，无权使用此api");
				return this.JSON_MESSAGE;
			}
			MessageCenter messageCenter = new MessageCenter();
			MessageFlag messageFlag = new MessageFlag();
			messageCenter = this.createMessageCenter(member);
			messageFlag = this.createMessageFlag();
			this.buyerMessageManager
					.addGoodsMessage(messageCenter, messageFlag);
			this.showSuccessJson("买家产品咨询成功");
		} catch (Exception e) {
			this.logger.error("买家产品咨询失败", e);
			this.showErrorJson("买家产品咨询失败" + e.getMessage());
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 
	 * @Description: 消息内容实例
	 * @param @param member
	 * @return MessageCenter
	 */
	private MessageCenter createMessageCenter(Member member) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		Integer memberid = member.getMember_id();
		MessageCenter messageCenter = new MessageCenter();
		String goods_id = request.getParameter("goods_id");
		if (goods_id != null) {
			messageCenter.setGoods_id(Integer.valueOf(goods_id));
		}
		String order_id = request.getParameter("order_id");
		if (order_id != null) {
			messageCenter.setOrder_id(Integer.valueOf(order_id));
		}
		messageCenter.setFrom_member_id(memberid);
		String message_text = request.getParameter("message_text");
		messageCenter.setMessage_text(message_text);
		String goods_sn = request.getParameter("goods_sn");
		String message_title = "卖家" + member.getUname() + "咨询商品标识为" + goods_sn;
		messageCenter.setMessage_title(message_title);
		messageCenter.setMessage_type("咨询卖家");
		messageCenter.setDescribe_text("");
		long send_date = DateUtil.getDateline();
		messageCenter.setSend_date(send_date);
		return messageCenter;
	}

	/**
	 * 
	 * @Description: 消息类型实例
	 * @param @param member
	 * @return MessageCenter
	 */
	private MessageFlag createMessageFlag() {

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		MessageFlag messageFlag = new MessageFlag();
		String store_id = request.getParameter("store_id");
		if (store_id != null) {
			messageFlag.setStore_id(Integer.valueOf(store_id));
		}
		String message_state = "NO_CHECK";
		messageFlag.setMessage_state(message_state);

		String store_member_id = request.getParameter("store_member_id");
		if (store_member_id != null) {
			messageFlag.setAccept_member_id(Integer.valueOf(store_member_id));
		}
		return messageFlag;
	}

	public String getStart_message_time() {
		return start_message_time;
	}

	public void setStart_message_time(String start_message_time) {
		this.start_message_time = start_message_time;
	}

	public String getEnd_message_time() {
		return end_message_time;
	}

	public void setEnd_message_time(String end_message_time) {
		this.end_message_time = end_message_time;
	}

	public IBuyerMessageManager getBuyerMessageManager() {
		return buyerMessageManager;
	}

	public void setBuyerMessageManager(IBuyerMessageManager buyerMessageManager) {
		this.buyerMessageManager = buyerMessageManager;
	}

	public String getMessage_title() {
		return message_title;
	}

	public void setMessage_title(String message_title) {
		this.message_title = message_title;
	}

	public String getMessage_type() {
		return message_type;
	}

	public void setMessage_type(String message_type) {
		this.message_type = message_type;
	}

	public String getMessage_text() {
		return message_text;
	}

	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}

	public String getAccept_member_id() {
		return accept_member_id;
	}

	public void setAccept_member_id(String accept_member_id) {
		this.accept_member_id = accept_member_id;
	}

	public String getMessage_state() {
		return message_state;
	}

	public void setMessage_state(String message_state) {
		this.message_state = message_state;
	}
}
