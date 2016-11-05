package com.enation.app.tradeease.core.service.message.impl;
import java.util.List;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.enation.app.base.core.model.Member;
import com.enation.app.tradeease.core.model.message.MessageCenter;
import com.enation.app.tradeease.core.model.message.MessageFlag;
import com.enation.app.tradeease.core.service.message.ISellerMessageManager;
import com.enation.eop.sdk.database.BaseSupport;
/**
 * 卖家中心站内信群发
 * 
 * @author yanpeng 2015-6-13 上午11:22:37
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class SellerMessageManager extends BaseSupport implements
		ISellerMessageManager{

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addSellerMessage(MessageCenter messageCenter,
			MessageFlag messageFlag) {
		int message_id;
		this.baseDaoSupport.insert("message_center", messageCenter);
		message_id = this.baseDaoSupport.getLastId("message_center");
		messageFlag.setMessage_id(message_id);
		this.baseDaoSupport.insert("message_flag", messageFlag);
	}

	@Override
	public List<Member> searchMembers() {
		String sql = " select m.* from es_member m where 1=1 ";
		return this.baseDaoSupport.queryForList(sql, Member.class);
	}

	@Override
	public MessageFlag searchMessageFlagByID(Integer message_id){
		String sql = " select m.* from es_message_flag m where m.message_id = ? ";
		MessageFlag messageFlag = (MessageFlag) this.baseDaoSupport
				.queryForObject(sql, MessageFlag.class, message_id);
		return messageFlag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void editMessageFlag(MessageFlag messageFlag){
		this.baseDaoSupport.update("es_message_flag", messageFlag,
				"message_flag_id=" + messageFlag.getMessage_flag_id());
	}

	//获取发件人的member
	@Override
	public Member searchMemberByID(Integer message_id) {
		String sql = "select * from es_member where member_id = ( select from_member_id from es_message_center where message_id = ? )";
		Member member = (Member) this.baseDaoSupport.queryForObject(sql, Member.class, message_id);
		return member;
	}
}