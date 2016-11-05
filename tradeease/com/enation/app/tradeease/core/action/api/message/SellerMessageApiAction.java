package com.enation.app.tradeease.core.action.api.message;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.tradeease.core.model.message.MessageCenter;
import com.enation.app.tradeease.core.model.message.MessageFlag;
import com.enation.app.tradeease.core.service.message.ISellerMessageManager;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;
/**
 * 卖家中心站内信
 * 
 * @author yanpeng 2015-6-13 下午2:19:46
 * 
 */

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("sellerMessage")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class SellerMessageApiAction extends WWAction {

	private ISellerMessageManager sellerMessageManager;
	private IStoreMemberManager storeMemberManager;
	private IStoreManager storeManager;
	private IOrderManager orderManager;
	private IMemberManager memberManager;
	private IAdminUserManager adminUserManager;

	private String message_title;
	private String message_type;
	private String accept_member_id;
	private String message_text;
	private String message_state;
	private String start_message_time;
	private String end_message_time;

	/**
	 * 卖家中心 群发给买家 站内信
	 * 
	 * @param
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	public String sellerMassMessages() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String weidenglu=this.getText("sellerMessage.wedenglu");
		String meikaidian=this.getText("sellerMessage.meikaidian");
		String wudingdan=this.getText("sellerMessage.meidingdan");
		String maijia=this.getText("sellerMessage.qunfa");
		String apidiaoyong=this.getText("sellerMessage.apidiaoyong");
		try {
			long send_date = DateUtil.getDateline();
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
				this.showErrorJson(weidenglu);
				return this.JSON_MESSAGE;
			}
			// 判断用户是否已经拥有店铺
			if (storeMember.getIs_store() != 1) {
				this.showErrorJson(meikaidian);
				return this.JSON_MESSAGE;
			}
			Integer member_id = storeMember.getMember_id();
			// 根据会员ID获得店铺
			Store store = storeManager.getStoreByMember(member_id);
			// 根据店铺ID获得相应的订单
			List<Order> orderList = orderManager.listOrderByStoreId(store
					.getStore_id());
			if (orderList.size() == 0) {
				this.showErrorJson(wudingdan);
				return this.JSON_MESSAGE;
			}
			Map<Integer, Order> orderMap = new HashMap<Integer, Order>();
			if (orderList.size() > 0) {

				for (Order order : orderList) {
					orderMap.put(order.getMember_id(), order);
				}
			}
			if (orderMap.values() != null) {
				// 根据订单获取买家ID
				for (Order order : orderMap.values()) {
					MessageCenter messageCenter = new MessageCenter();
					MessageFlag messageFlag = new MessageFlag();

					messageCenter = this.createMessageCenter(member_id, order,
							send_date);
					messageFlag = this.createMessageFlag(store, order);
					this.sellerMessageManager.addSellerMessage(messageCenter,
							messageFlag);
						this.showSuccessJson(maijia);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson(apidiaoyong + e.getMessage());
			this.logger.error("卖家站内信群发出错", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 卖家中心 登录名发送给网站会员 站内信
	 * 
	 * @param
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	public String sellerLoginMessages() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String weidenglu=this.getText("sellerMessage.wedenglu");
		String meikaidian=this.getText("sellerMessage.meikaidian");
		String wudingdan=this.getText("sellerMessage.meidingdan");
		String zhanneixin=this.getText("sellerMessage.zhanneixin");
		String NOcunzai=this.getText("sellerMessage.yonghubucunzai");
		String gaiyonghu=this.getText("sellerMessage.gaiyonghu");
		String yidenglu=this.getText("sellerMessage.yidengluming");
		String apidiaoyong=this.getText("sellerMessage.apidiaoyong");
		try {
			// 统一获取发送时间
			long send_date = DateUtil.getDateline();
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
				this.showErrorJson(weidenglu);
				return this.JSON_MESSAGE;
			}
			// 判断用户是否已经拥有店铺
			if (storeMember.getIs_store() != 1) {
				this.showErrorJson(meikaidian);
				return this.JSON_MESSAGE;
			}
			// 发送的消息内容实例
			MessageCenter messageCenter = new MessageCenter();
			Integer member_id = storeMember.getMember_id();
			messageCenter.setFrom_member_id(member_id);
			String message_title = request.getParameter("message_title");
			messageCenter.setMessage_title(message_title);
			String message_text = request.getParameter("message_text");
			messageCenter.setMessage_text(message_text);
			messageCenter.setMessage_type("单发买家");
			messageCenter.setDescribe_text("");
			// long send_date = DateUtil.getDateline();
			messageCenter.setSend_date(send_date);
			// 得到以“,”分割的消息的接收者
			String acceptMembers = request.getParameter("acceptMembers");
			if (StringUtil.isEmpty(acceptMembers)) {
				this.showErrorJson(zhanneixin);
				return this.JSON_MESSAGE;
			}
			// acceptMembers = "baihuashu,nangq";
			List<String> memberList = StringUtil.stringToList(acceptMembers,
					",");
			if (memberList.size() == 0) {
				this.showErrorJson(NOcunzai);
				return this.JSON_MESSAGE;
			}
			for (String memberName : memberList) {
				// 判断是否是会员，如果是得到会员的ID
				Member member = memberManager.getMemberByUname(memberName);
				if (member == null) {
					this.showErrorJson(memberName + gaiyonghu);
					return this.JSON_MESSAGE;
				}
				// 消息接收者实例
				MessageFlag messageFlag = new MessageFlag();
				String message_state = "NO_CHECK";
				messageFlag.setMessage_state(message_state);
				// 收信人：订单的member_id
				messageFlag.setAccept_member_id(member.getMember_id());
				this.sellerMessageManager.addSellerMessage(messageCenter,
						messageFlag);
					this.showSuccessJson(yidenglu);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson(apidiaoyong + e.getMessage());
			this.logger.error("站内信以登录名发送出错", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 管理员群发消息给member表中的所有用户
	 * 
	 * @param
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	public String adminMassMessages() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String nohuiyuan=this.getText("sellerMessage.noguanli");
		String danqian=this.getText("sellerMessage.dangqian");
		String yonghuNO=this.getText("sellerMessage.yonghuNO");
		String guanliqunfa=this.getText("sellerMessage.guanliqunfa");
		String apidiaoyong=this.getText("sellerMessage.apidiaoyong");
		try {
			long send_date = DateUtil.getDateline();
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			AdminUser adminUser = UserConext.getCurrentAdminUser();
			if (adminUser == null) {
				this.showErrorJson(nohuiyuan);
				return this.JSON_MESSAGE;
			}
			List<Member> members = sellerMessageManager.searchMembers();
			if (members.size() == 0) {
				this.showErrorJson(danqian);
				return this.JSON_MESSAGE;
			}
			// 发送的消息内容实例
			MessageCenter messageCenter = new MessageCenter();
			Integer admin_id = adminUser.getUserid();
			// 消息发送者ID
			messageCenter.setFrom_member_id(admin_id);
			String message_title = request.getParameter("message_title");
			messageCenter.setMessage_title(message_title);
			String message_text = request.getParameter("message_text");
			messageCenter.setMessage_text(message_text);
			messageCenter.setMessage_type("管理员群发");
			messageCenter.setDescribe_text("");
			// long send_date = DateUtil.getDateline();
			messageCenter.setSend_date(send_date);

			for (Member member : members) {
				if (member == null) {
					this.showErrorJson(yonghuNO);
					return this.JSON_MESSAGE;
				}
				// 消息接收者实例
				MessageFlag messageFlag = new MessageFlag();
				String message_state = "NO_CHECK";
				messageFlag.setMessage_state(message_state);
				// 消息接收者ID
				messageFlag.setAccept_member_id(member.getMember_id());
				this.sellerMessageManager.addSellerMessage(messageCenter,
						messageFlag);
					this.showSuccessJson(guanliqunfa);
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson(apidiaoyong + e.getMessage());
			this.logger.error("c", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 买家投诉消息给系统管理员(投诉给按时间排序的管理员List中的第一个管理员)
	 * 
	 * @param
	 * @return 消息列表， List<Map> 型的json，Map中存的是messages
	 */
	@SuppressWarnings("unused")
	public String memberToAdmin() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String wuquan=this.getText("sellerMessage.wuquan");
		String wuguanliyuan=this.getText("sellerMessage.xitongNO");
		String maijia=this.getText("sellerMessage.maijia");
		String apidiaoyong=this.getText("sellerMessage.apidiaoyong");
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Member member = UserConext.getCurrentMember();
			Integer memberid = member.getMember_id();
			if (member == null) {
				this.showErrorJson(wuquan);
				return this.JSON_MESSAGE;
			}
			// 查询系统的所有管理员
			List<AdminUser> adminUserList = adminUserManager.list();
			if (adminUserList.size() == 0) {
				this.showErrorJson(wuguanliyuan);
				return this.JSON_MESSAGE;
			}
			// 投诉给list中的以一个管理员
			AdminUser toAdminUser = adminUserList.get(0);
			// 发送的消息内容实例
			MessageCenter messageCenter = new MessageCenter();
			// 消息发送者ID
			messageCenter.setFrom_member_id(memberid);
			String message_title = request.getParameter("message_title");
			messageCenter.setMessage_title(message_title);
			String message_text = request.getParameter("message_text");
			messageCenter.setMessage_text(message_text);
			messageCenter.setMessage_type("买家投诉");
			messageCenter.setDescribe_text("");
			long send_date = DateUtil.getDateline();
			messageCenter.setSend_date(send_date);

			// 消息接收者实例
			MessageFlag messageFlag = new MessageFlag();
			String message_state = "NO_CHECK";
			messageFlag.setMessage_state(message_state);
			// 消息接收者ID
			messageFlag.setAccept_member_id(toAdminUser.getUserid());
			this.sellerMessageManager.addSellerMessage(messageCenter,
					messageFlag);
				this.showSuccessJson(maijia);
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson(apidiaoyong + e.getMessage());
			this.logger.error("买家投诉失败", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 查看消息（修改消息的状态）
	 * 
	 * @param
	 * @return
	 */
	@SuppressWarnings("unused")
	public String openMessage() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String apidiaoyong=this.getText("sellerMessage.apidiaoyong");
		String success=this.getText("sellerMessage.xiugaiSuccess");
		Member member = new Member();
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String message_id = request.getParameter("message_id");
			
			//检测发送者的member_id
			member = this.sellerMessageManager.searchMemberByID(StringUtil.toInt(message_id));
			
			// 消息接收者实例
			MessageFlag messageFlag = this.sellerMessageManager.searchMessageFlagByID(StringUtil.toInt(message_id));
			messageFlag.setMessage_state("CHECKED");
			this.sellerMessageManager.editMessageFlag(messageFlag);
				this.showSuccessJson(success);
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson(apidiaoyong + e.getMessage());
			this.logger.error("修改信息状态失败", e);
		}
		
		//转换json并返回发送消息人的email
		this.json=JsonMessageUtil.getStringJson("email",member.getEmail());
		return this.JSON_MESSAGE;
	}

	/**
	 * 
	 * @Description: 消息内容实例
	 * @param @param member
	 * @return MessageCenter
	 */
	private MessageCenter createMessageCenter(int member_id, Order order,
			long send_date) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		MessageCenter messageCenter = new MessageCenter();
		String goods_id = request.getParameter("goods_id");
		if (goods_id != null) {
			messageCenter.setGoods_id(Integer.valueOf(goods_id));
		}
		messageCenter.setOrder_id(order.getOrder_id());
		messageCenter.setFrom_member_id(member_id);
		String message_text = request.getParameter("message_text");
		messageCenter.setMessage_text(message_text);
		String message_title = request.getParameter("message_title");
		messageCenter.setMessage_title(message_title);
		messageCenter.setMessage_type("群发买家");
		messageCenter.setDescribe_text("");
		// long send_date = DateUtil.getDateline();
		messageCenter.setSend_date(send_date);
		return messageCenter;
	}

	/**
	 * 
	 * @Description: 消息类型实例
	 * @param @param member
	 * @return MessageCenter
	 */
	@SuppressWarnings("unused")
	private MessageFlag createMessageFlag(Store store, Order order) {

		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		MessageFlag messageFlag = new MessageFlag();
		messageFlag.setStore_id(store.getStore_id());
		String message_state = "NO_CHECK";
		messageFlag.setMessage_state(message_state);
		// 收信人：订单的member_id
		messageFlag.setAccept_member_id(order.getMember_id());
		return messageFlag;
	}

	public ISellerMessageManager getSellerMessageManager() {
		return sellerMessageManager;
	}

	public void setSellerMessageManager(
			ISellerMessageManager sellerMessageManager) {
		this.sellerMessageManager = sellerMessageManager;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
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

	public String getAccept_member_id() {
		return accept_member_id;
	}

	public void setAccept_member_id(String accept_member_id) {
		this.accept_member_id = accept_member_id;
	}

	public String getMessage_text() {
		return message_text;
	}

	public void setMessage_text(String message_text) {
		this.message_text = message_text;
	}

	public String getMessage_state() {
		return message_state;
	}

	public void setMessage_state(String message_state) {
		this.message_state = message_state;
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

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public IAdminUserManager getAdminUserManager() {
		return adminUserManager;
	}

	public void setAdminUserManager(IAdminUserManager adminUserManager) {
		this.adminUserManager = adminUserManager;
	}
}