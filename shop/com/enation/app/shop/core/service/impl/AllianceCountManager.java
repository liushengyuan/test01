package com.enation.app.shop.core.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;
import com.enation.app.shop.core.model.FlowCount;
import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@SuppressWarnings("rawtypes")
public class AllianceCountManager extends BaseSupport implements
		IAllianceCountManager {

	/**
	 * 订单完成支付时改变es_flow_count表的订单状态
	 */
	public void editOrderStatus(String session_id){
		String sql = "UPDATE es_flow_count SET order_status='是' WHERE session_id='"+session_id+"'";
		this.baseDaoSupport.execute(sql);
	}
	
	/**
	 * 根据order_id在es_count_order中查找session_id
	 */
	public String findSessionID(Integer order_id){
		String sql = "SELECT c.session_id FROM es_count_order c WHERE c.order_id='"+order_id+"'";
		String sessionID = this.baseDaoSupport.queryForString(sql);
		return sessionID;
	}
	
	/**
	 * 确定此session_id是否存在
	 */
	public List<String> isSesID(String session_id){
		String sessionSQL = "SELECT a.session_id FROM es_count_action a WHERE a.session_id='"+session_id+"'";
		List list = this.baseDaoSupport.queryForList(sessionSQL);
		return list;
	}
	
	/**
	 * 记录用户操作，包括访问的页面，进入时间，session_id
	 */
	public void editAllianceAction(String adress, String session_id){
		Long time = DateUtil.getDateline();
		String actionSQL = "INSERT INTO es_count_action (address, session_id, come_time) VALUES (\""+adress+"\", \""+session_id+"\",\""+time+"\")";
		this.baseDaoSupport.execute(actionSQL);
	}
	
//	/**
//	 * 流量统计页面查看详情操作显示列表
//	 */
//	public Page alliCountCheck_list(Integer count_id, int page, int pageSize){
//		String sql = "SELECT c.count_id, c.ad_link, a.come_time, a.address, (SELECT s.action FROM es_count_address s WHERE s.address=a.address) AS action FROM es_alliance_count c, es_count_action a WHERE a.session_id = c.session_id AND count_id = '"+count_id+"'";
//		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
//		return webpage;
//	}
	
	/**
	 * 根据order_id获取member_id
	 * @param order_id
	 * @return
	 */
	public Integer getMember_id(Integer order_id){
		String sql = "SELECT o.member_id FROM es_order o WHERE o.order_id='"+order_id+"'";
		Integer member_id = this.baseDaoSupport.queryForInt(sql);
		return member_id;
	}
	
	/**
	 * 向es_count_order插入相应数据
	 * @param member_id
	 * @param session_id
	 * @param order_id
	 */
	public void addOrderCount(Integer member_id, String session_id, Integer order_id){
		String masterOrderSN = "SELECT o.sn FROM es_order o WHERE o.order_id='"+order_id+"'";
		String string = this.baseDaoSupport.queryForString(masterOrderSN);
		String order_idSQL = "SELECT o.order_id FROM es_order o WHERE o.sn LIKE '"+string+"%'";
		List list = this.baseDaoSupport.queryForList(order_idSQL);
		for(int i=0; i<list.size(); i++){
			String s = list.get(i).toString();
			String orderID = s.substring(10, s.length()-1);
			String sql = "INSERT INTO es_count_order (member_id,session_id,order_id) VALUES (\""+member_id+"\",\""+session_id+"\",\""+orderID+"\")";
			this.baseDaoSupport.execute(sql);
		}
	}
	
	/**
	 * 保存用户行为
	 */
	public void saveAddAddress(String address, String action){
		String sql = "INSERT INTO es_count_address (address, action) VALUES (\""+address+"\",\""+action+"\")";
		this.baseDaoSupport.execute(sql);
	}
	
	/**
	 * 判断是否已经存在address
	 */
	public List haveAction(String address){
		String sql = "SELECT a.action FROM es_count_address a WHERE a.address='"+address+"'";
		List list = this.baseDaoSupport.queryForList(sql);
		return list;
	}
	
	/**
	 * es_flow_count是否已经存在session_id
	 * @param session_id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> haveSession(String session_id){
		String sql = "SELECT f.flow_id FROM es_flow_count f WHERE f.session_id='"+session_id+"'";
		List list = this.baseDaoSupport.queryForList(sql);
		return list;
	}
	
	/**
	 * es_flow_count插入session_id
	 * @param session_id
	 */
	public void addSession_id(String session_id){
		String sql = "INSERT INTO es_flow_count (session_id) VALUES (\""+session_id+"\")";
		this.baseDaoSupport.execute(sql);
	}
	
	/**
	 * 更新用户ip，进入时间
	 */
	public void editFlowCount(String user_ip, String ipAddress, long come_time, String session_id){
		String sql = "UPDATE es_flow_count SET user_ip='"+user_ip+"', user_addr='"+ipAddress+"', come_time='"+come_time+"' WHERE session_id='"+session_id+"'";
		this.baseDaoSupport.execute(sql);
	}
	
	/**
	 * es_flow_count中是否存在come_time
	 * @param session_id
	 * @return
	 */
	public List haveTime(String session_id){
		String time = "select f.come_time from es_flow_count f where f.session_id='"+session_id+"'";
		List list = this.baseDaoSupport.queryForList(time);
		return list;
	}
	
	/**
	 * es_flow_count更新用户离开时间
	 * @param session_id
	 */
	public void editLeaveTime(Long leaveTime, String session_id){
		String sql = "UPDATE es_flow_count SET go_time='"+leaveTime+"' WHERE session_id='"+session_id+"'";
		this.baseDaoSupport.execute(sql);
	}
	
	/**
	 * es_flow_count更新username
	 * @param username
	 * @param session_id
	 */
	public void editMember_id(String username, String session_id){
		String sql = "UPDATE es_flow_count SET user_name='"+username+"' WHERE session_id='"+session_id+"'";
		this.baseDaoSupport.execute(sql);
	}
	
	/**
	 * 用户流量统计页面(总计)
	 */
	public Page flowCount_list(Map map, int page, int pageSize){
		String start_time = (String) map.get("start_time");
		String stop_time = (String) map.get("stop_time");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT f.flow_id, f.user_addr, COUNT(user_ip) AS ip_count, (SELECT COUNT(order_status) FROM es_flow_count c WHERE c.order_status='是' AND c.user_addr=f.user_addr ");
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql.append(" and c.come_time >" + stime);
		}
		if (stop_time != null && !StringUtil.isEmpty(stop_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(stop_time + " 23:59:59");
			sql.append(" and c.go_time <" + etime);
		}
		sql.append(" ) AS order_count FROM es_flow_count f where 1=1 ");
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql.append(" and f.come_time >" + stime);
		}
		if (stop_time != null && !StringUtil.isEmpty(stop_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(stop_time + " 23:59:59");
			sql.append(" and f.go_time <" + etime);
		}
		sql.append(" AND user_addr is not null GROUP BY f.user_addr");
		List list = this.baseDaoSupport.queryForList(sql.toString());
		Page webpage = new Page();
		webpage.setParam(0, list.size(), pageSize, list);
		return webpage;
	}
	
	/**
	 * 用户流量统计页面(总计)导出
	 */
	public List flowCountExport(Map map){
		String start_time = (String) map.get("start_time");
		String stop_time = (String) map.get("stop_time");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT f.flow_id, f.user_addr, COUNT(user_ip) AS ip_count, (SELECT COUNT(order_status) FROM es_flow_count c WHERE c.order_status='是' ");
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql.append(" and c.come_time >" + stime);
		}
		if (stop_time != null && !StringUtil.isEmpty(stop_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(stop_time + " 23:59:59");
			sql.append(" and c.go_time <" + etime);
		}
		sql.append(" ) AS order_count FROM es_flow_count f where 1=1 ");
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql.append(" and f.come_time >" + stime);
		}
		if (stop_time != null && !StringUtil.isEmpty(stop_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(stop_time + " 23:59:59");
			sql.append(" and f.go_time <" + etime);
		}
		sql.append(" AND user_addr is not null GROUP BY f.user_addr");
		List list = this.baseDaoSupport.queryForList(sql.toString());
		return list;
	}
	
	/**
	 * 用户流量统计页面(详细)导出
	 */
	public List flowCountListExport(Map map, String user_addr){
		String user_ip = (String) map.get("user_ip");
		String user_name = (String) map.get("user_name");
		String order_status = (String) map.get("order_status");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT f.flow_id, f.user_addr, f.user_ip, f.come_time, f.go_time, f.user_name, f.order_status FROM es_flow_count f WHERE f.user_addr='"+user_addr+"' ");
		if (user_ip != null && !user_ip.isEmpty()) {
			sql.append(" and f.user_ip LIKE '%" + user_ip + "%'");
		}
		if (user_name != null && !user_name.isEmpty()) {
			sql.append(" and f.user_name LIKE '%" + user_name + "%'");
		}
		if (order_status != null && !order_status.isEmpty()) {
			sql.append(" and f.order_status LIKE '%" + order_status + "%'");
		}
		List list = this.baseDaoSupport.queryForList(sql.toString());
		return list;
	}
	
	/**
	 * 用户流量统计页面(详细)
	 */
	public Page flowCountDetail(Map map, String user_addr, int page, int pageSize, String sort, String order){
		String user_ip = (String) map.get("user_ip");
		String user_name = (String) map.get("user_name");
		String order_status = (String) map.get("order_status");
		String start_time = (String) map.get("start_time");
		String stop_time = (String) map.get("stop_time");
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT f.flow_id, f.user_ip, f.come_time, f.go_time, f.user_name, f.order_status FROM es_flow_count f WHERE f.user_addr='"+user_addr+"' ");
		if (user_ip != null && !user_ip.isEmpty()) {
			sql.append(" and f.user_ip LIKE '%" + user_ip + "%'");
		}
		if (user_name != null && !user_name.isEmpty()) {
			sql.append(" and f.user_name LIKE '%" + user_name + "%'");
		}
		if (order_status != null && !order_status.isEmpty()) {
			sql.append(" and f.order_status LIKE '%" + order_status + "%'");
		}
		if (start_time != null && !StringUtil.isEmpty(start_time)) {
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time + " 00:00:00");
			sql.append(" and f.come_time >" + stime);
		}
		if (stop_time != null && !StringUtil.isEmpty(stop_time)) {
			long etime = com.enation.framework.util.DateUtil.getDateline(stop_time + " 23:59:59");
			sql.append(" and f.go_time <" + etime);
		}
		if(sort != null && !StringUtil.isEmpty(sort) && order != null && !StringUtil.isEmpty(order)){
			sql.append(" ORDER BY f."+sort+" "+order+"");
		}
		Page webpage = this.baseDaoSupport.queryForPage(sql.toString(), page, pageSize);
		return webpage;
	}
	
	/**
	 * 用户流量统计flow_id
	 * @param flow_id
	 * @return
	 */
	public FlowCount flowCountCheck(Integer flow_id) {
		String sql = "SELECT m.flow_id, m.user_addr, COUNT(c.user_ip) AS ip_count FROM es_flow_count c, (SELECT f.flow_id, f.user_addr FROM es_flow_count f WHERE f.flow_id = '"+flow_id+"') AS m";
		FlowCount flowCount = (FlowCount) this.baseDaoSupport.queryForObject(sql, FlowCount.class);
		return flowCount;
	}
	
	/**
	 * 访问总数
	 * @return
	 */
	public Integer flowIPCount() {
		String sql = "SELECT COUNT(f.user_ip) FROM es_flow_count f";
		Integer ip_count = this.baseDaoSupport.queryForInt(sql);
		return ip_count;
	}
	
	/**
	 * 当天访问量
	 * @return
	 */
	public Integer todayIPCount(){
		long nowTime = com.enation.framework.util.DateUtil.getDateline();
		String today = com.enation.framework.util.DateUtil.toString(nowTime, "yyyy-MM-dd");
		long todayTime = com.enation.framework.util.DateUtil.getDateline(today);
//		long tomorrowTime = todayTime + 86400l;
		String sql = "SELECT COUNT(f.user_ip) FROM es_flow_count f WHERE f.come_time>"+todayTime;
		Integer todayIPCount = this.baseDaoSupport.queryForInt(sql);
		return todayIPCount;
	}
	
	/**
	 * 用户流量统计查看访问页面
	 * @param flow_id
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Page flowCountCheck_list(Integer flow_id, int page, int pageSize, String sort, String order){
		String sql = "SELECT f.user_ip, a.come_time, a.address, (SELECT s.action FROM es_count_address s WHERE s.address = a.address) AS action FROM es_flow_count f, es_count_action a WHERE a.session_id = f.session_id AND f.flow_id = '"+flow_id+"'";
		if(sort != null && !StringUtil.isEmpty(sort) && order != null && !StringUtil.isEmpty(order)){
			sql += " ORDER BY a."+sort+" "+order+"";
		}
		String sql1 = "SELECT count(1) FROM es_flow_count f, es_count_action a WHERE a.session_id = f.session_id AND f.flow_id = '"+flow_id+"'";
		int count= this.daoSupport.queryForInt(sql1.toString());
		List<Object>list2 = this.baseDaoSupport.queryForListPage(sql, page, pageSize); 
		List list1 = new ArrayList();
		for(Object o : list2){
			JSONObject jsonObject=JSONObject.fromObject(o);
			String address = jsonObject.get("address").toString();
			if(address.length()>=7 &&  address.substring(0, 7).equalsIgnoreCase("/goods-")){
				jsonObject.put("action", "商品详情页");
			}
			list1.add(jsonObject);
		}
		Page webPage = new Page(0,count, pageSize, list1);
		return webPage;
	} 
	protected int getPage() {
		int page =1;
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		page  = StringUtil.toInt(request.getParameter("page"),1);
		page = page < 1 ? 1 : page;
		return page;
	}
	/**
	 * 访问地统计
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> userAddrCount(){
		String sql = "SELECT f.user_addr FROM es_flow_count f WHERE f.user_addr is not null GROUP BY f.user_addr";
		List list = this.baseDaoSupport.queryForList(sql);
		return list;
	} 
	
	/**
	 * 访问地数量统计
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> userAddrNumCount(){
		String sql = "SELECT COUNT(f.user_addr) AS addrCount FROM es_flow_count f WHERE f.user_addr is not null GROUP BY f.user_addr";
		List list = this.baseDaoSupport.queryForList(sql);
		return list;
	} 
	
	/**
	 * 过去半个月的日访问数
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public List<String> dayUserCount() throws ParseException{
		List dayUserList = new ArrayList();
		Long todayTime = DateUtil.getDateline();
		DateUtil.toString(todayTime, "yyyy-MM-dd");
		long today = DateUtil.getDateline(DateUtil.toString(todayTime, "yyyy-MM-dd"));
		for(long l = today-86400l*15; l < today; l += 86400l){
			long lo = l + 86400l;
			String sql = "SELECT COUNT(f.user_ip) FROM es_flow_count f WHERE f.come_time>"+l+" AND f.go_time<"+lo+"";
			dayUserList.add(this.baseDaoSupport.queryForInt(sql));
		}
		return dayUserList;
	}
}
