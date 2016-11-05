package com.enation.app.tradeease.core.service.message;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.model.message.MessageCenter;
import com.enation.app.tradeease.core.model.message.MessageFlag;
import com.enation.framework.database.Page;

/**
 * 买家中心站内信接口
 * 
 * @author yanpeng
 * 
 */
public interface IBuyerMessageManager {

	/**
	 * 
	 * @Description: 买家发送的消息
	 * @param @param memberid
	 * @param @param map
	 * @return List<Map>
	 */
	@SuppressWarnings("rawtypes")
	List<Map> buyerMessageList(int memberid, Map map);

	/**
	 * 
	 * @Description: 买家收到的消息
	 * @param @param memberid
	 * @param @param params
	 * @return List<Map>
	 */
	@SuppressWarnings("rawtypes")
	public List<Map> buyerAcceptMessageList(Integer memberid, Map map);

	/**
	 * 
	 * @Description: 商品咨询
	 * @param @param memberid
	 * @param @param params
	 * @return List<Map>
	 */
	public void addGoodsMessage(MessageCenter messageCenter,
			MessageFlag messageFlag);

	/**
	 * 
	 * @Description: 买家中心站内信 发送的消息（分页方法）
	 * @param @param memberid
	 * @param @param params
	 * @return List<Map>
	 */
	public Page list(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time);
	/**
	 * 
	 * @Description: 买家中心站内信 发送的消息（分页方法）
	 * @param @param memberid
	 * @param @param params
	 * @return List<Map>
	 */
	public Page sendList(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time);

	/**
	 * 
	 * @Description: 买家中心站内信 收到的消息（分页方法）
	 * @param @param memberid
	 * @param @param params
	 * @return List<Map>
	 */
	public Page acceptList(Integer member_id, int page, int pageSize,
			long start_time, long end_time, String message_state,
			String goods_id, String buyMembername);
}