package com.enation.app.tradeease.core.service.message.impl;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.tradeease.core.model.message.MessageCenter;
import com.enation.app.tradeease.core.model.message.MessageFlag;
import com.enation.app.tradeease.core.service.message.IBuyerMessageManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 买家中心站内信实现
 * 
 * @author yanpeng 2015-6-9 下午1:36:45
 * 
 */
@SuppressWarnings({ "rawtypes" })
public class BuyerMessageManager extends BaseSupport implements
		IBuyerMessageManager {

	@Override
	public List<Map> buyerMessageList(int memberid, Map map) {

		StringBuffer sql = new StringBuffer(
				" SELECT mc.*,mf.* from es_message_flag mf left join es_message_center mc on mf.message_id = mc.message_id where mc.from_member_id= "
						+ memberid);
		long start_time = (Long) map.get("start_time");
		long end_time = (Long) map.get("end_time");
		if (start_time > 0 && end_time > 0) {
			sql.append(" and mc.send_date>=" + start_time
					+ " and mc.send_date<" + end_time);
		}
		return this.daoSupport.queryForList(sql.toString());
	}

	@Override
	public List<Map> buyerAcceptMessageList(Integer memberid, Map map) {
		StringBuffer sql = new StringBuffer(
				" SELECT mc.*,mf.* from es_message_flag mf left join es_message_center mc on mf.message_id = mc.message_id where mf.accept_member_id= "
						+ memberid);
		String message_state = String.valueOf(map.get("message_state"));
		if (!StringUtil.isEmpty(message_state)) {
			sql.append(" and message_state = '" + message_state + "'");
		}
		long start_time = (Long) map.get("start_time");
		long end_time = (Long) map.get("end_time");
		if (start_time > 0 && end_time > 0) {
			sql.append(" and mc.send_date >= " + start_time
					+ " and mc.send_date < " + end_time);
		}
		return this.daoSupport.queryForList(sql.toString());
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addGoodsMessage(MessageCenter messageCenter,
			MessageFlag messageFlag) {

		int message_id;
		this.baseDaoSupport.insert("message_center", messageCenter);
		message_id = this.baseDaoSupport.getLastId("message_center");
		messageFlag.setMessage_id(message_id);
		this.baseDaoSupport.insert("message_flag", messageFlag);
	}

	@Override
	public Page list(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time) {
		String sql = " SELECT mc.*,mf.* from es_message_flag mf left join es_message_center mc on mf.message_id = mc.message_id ";
		sql += " where mc.from_member_id= ? ";
		if (start_time > 0 && end_time > 0) {
			sql += " and mc.send_date>=" + start_time + " and mc.send_date<"
					+ end_time;
		}
		sql += " and mc.message_id in (select mc1.message_id from es_message_center mc1 where mc1.from_member_id= ? ";
		sql += " and mc1.message_id in (select mc2.message_id from es_message_center mc2 group by mc2.message_text ) group by mc1.send_date ) ";
		sql += " order by mc.send_date desc ";
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize,
				member_id,member_id);
		return page;
	}
	@Override
	public Page sendList(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time) {
		String sql = " SELECT mc.*,mf.* from es_message_flag mf left join es_message_center mc on mf.message_id = mc.message_id ";
		sql += " where mc.from_member_id= ? ";
		sql += " and mc.message_id in (select min(message_id) from es_message_center where send_date=mc.send_date)";
		if (start_time > 0 && end_time > 0) {
			sql += " and mc.send_date>=" + start_time + " and mc.send_date<"
					+ end_time;
		}
		sql +=" order by mc.send_date desc ";
		
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize,
				member_id);
		return page;
	}

	@Override
	public Page acceptList(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time, String message_state,
			String goods_id, String buyMembername) { 
		String sql = " SELECT mc.*,mf.*,m.* from es_message_flag mf left join es_message_center mc on mf.message_id = mc.message_id left join es_member m on m.member_id=mf.accept_member_id ";
		sql += " where mf.accept_member_id= ? ";
		if (!StringUtil.isEmpty(message_state)) {
			sql += "and mf.message_state = '" + message_state + "'";
		}
		if (!StringUtil.isEmpty(goods_id)) {
			sql += " and mc.goods_id = " + StringUtil.toInt(goods_id);
		}
		if (!StringUtil.isEmpty(buyMembername)) {
			sql += " and m.email like '%"+buyMembername+"%'";
		}
		if (start_time > 0 && end_time > 0) {
			sql += " and mc.send_date>=" + start_time + " and mc.send_date<="
					+ end_time;
		}
		sql += "  order by mc.send_date desc ";
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize,
				member_id);
		return page;
	}
}
