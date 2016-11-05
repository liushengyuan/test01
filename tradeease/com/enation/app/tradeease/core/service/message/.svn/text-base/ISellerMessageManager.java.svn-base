package com.enation.app.tradeease.core.service.message;
import java.util.List;
import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.message.MessageCenter;
import com.enation.app.tradeease.core.model.message.MessageFlag;
/**
 * 买家中心站内信接口
 * @author yanpeng 2015-6-13 下午3:06:38
 */
public interface ISellerMessageManager {
	
	/**
	 * @Description: 群发消息
	 * @param @param memberid
	 * @param @param params
	 * @return List<Map>
	 */
	public void addSellerMessage(MessageCenter messageCenter,
			MessageFlag messageFlag);
	
	/**
	 * 查询全部会员
	 * @Description: TODO
	 * @param
	 * @return List<Member>
	 */
	List<Member> searchMembers();
	
	/**
	 * 根据messageID查询message
	 * @Description: TODO
	 * @param
	 * @return MessageFlag
	 */
	public MessageFlag searchMessageFlagByID(Integer message_id);
	
	/**
	 * 修改messageFlag
	 * @Description: TODO
	 * @param
	 * @return
	 * @return
	 */
	void editMessageFlag(MessageFlag messageFlag);
	
	
	/**
	 * 根据message_id查询member
	 * @Description: TODO
	 * @param
	 * @return
	 * @return
	 */
	public Member searchMemberByID(Integer message_id);
}