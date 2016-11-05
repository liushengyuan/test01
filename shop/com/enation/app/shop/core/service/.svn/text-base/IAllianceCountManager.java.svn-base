package com.enation.app.shop.core.service;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

//import com.enation.app.shop.core.model.AllianceAddress;
//import com.enation.app.shop.core.model.AllianceCount;
import com.enation.app.shop.core.model.FlowCount;
import com.enation.app.shop.core.model.Order;
import com.enation.framework.database.Page;

public interface IAllianceCountManager {
//	public List allianceCount_list(Map map);
//	public List alliCollectCount_list(Map map);
//	public List<AllianceCount> searchAllianceCount(Map map);
//	public AllianceCount allianceCountCheck(Integer count_id);
//	public List alliOrderCount_list(Map map, String name);
//	public List<AllianceCount> searchAllianceCount2(Map map, String name);
//	public List<AllianceCount> searchAllianceCount3(Map map, Integer order_id);
//	public AllianceCount getAlliName(Integer count_id);
//	public Order getOrder_id(String sn);
//	public List alliOrderGoodsCount_list(Map map, Integer order_id);
	public void editOrderStatus(String session_id);
	public void editAllianceAction(String adress, String session_id);
	public List<String> isSesID(String session_id);
//	public Page alliCountCheck_list(Integer count_id, int page, int pageSize);
	public void addOrderCount(Integer member_id, String session_id, Integer order_id);
	public Integer getMember_id(Integer order_id);
//	public void editOrderStatusBackend(String session_id);
	public void saveAddAddress(String address, String action);
	public List haveAction(String address);
	public List<String> haveSession(String session_id);
	public void addSession_id(String session_id);
	public void editFlowCount(String user_ip, String ipAddress, long come_time, String session_id);
	public void editLeaveTime(Long leaveTime, String session_id);
	public void editMember_id(String username, String session_id);
	public Page flowCount_list(Map map, int page, int pageSize);
	public FlowCount flowCountCheck(Integer flow_id);
	public Page flowCountCheck_list(Integer flow_id, int page, int pageSize, String string, String string2);
	public Page flowCountDetail(Map map, String user_addr, int page, int pageSize, String sort, String order);
	public Integer flowIPCount();
	public List<String> userAddrCount();
	public List<String> userAddrNumCount();
	public List<String> dayUserCount() throws ParseException;
	public Integer todayIPCount();
	public List flowCountExport(Map map);
	public List flowCountListExport(Map map, String user_addr);
	public List haveTime(String session_id);
	public String findSessionID(Integer order_id);
}
