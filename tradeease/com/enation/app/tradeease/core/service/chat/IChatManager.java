package com.enation.app.tradeease.core.service.chat;

import java.util.List;

import com.enation.app.tradeease.core.model.chat.Chat;
/**
 * 聊天chat表接口
 * lmn
 */
public interface IChatManager {
	/**查询给定发件人和收件人ID的聊天记录
	 * @param sender_id
	 * @param sendee_id
	 * @return
	 */
	public List<Chat> findBySender(Integer sender_id,Integer sendee_id);
	
	/**发送消息 插入数据库
	 * @param chat
	 */
	public void addMessage(Chat chat);
	public Integer findByMember(Integer sender,Integer sendee);
	public void updateState(Integer sender, Integer sendee);
	public void updateStateToNotRead(Integer sender, Integer sendee);
	public List<Chat> selectFromLanguage(Integer sender);
	public List<Chat> findByAll(Integer sender_id,Integer sendee_id);
	public List<Chat> topHTMLNewMsg(Integer member_id);
	public List<Chat> getGoodsId(Integer sender,Integer sendee);
}
