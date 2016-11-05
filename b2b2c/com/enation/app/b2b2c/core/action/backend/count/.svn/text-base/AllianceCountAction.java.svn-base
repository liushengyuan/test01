package com.enation.app.b2b2c.core.action.backend.count;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.FlowCount;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IAllianceCountManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;

@SuppressWarnings("serial")
@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
		@Result(name = "userFlowCount", type = "freemarker", location = "/b2b2c/admin/count/flowCount.html"),
		@Result(name = "flowCountCheck_list", type = "freemarker", location = "/b2b2c/admin/count/flowCountListCheck.html"),
		@Result(name = "userFlowDetail", type = "freemarker", location = "/b2b2c/admin/count/flowCountList.html"),
		@Result(name = "flowCountExport", type = "freemarker", location = "/b2b2c/admin/count/flowCountExport.html"),
		@Result(name = "anglePie", location = "/count/anglepie.jsp"),
		@Result(name = "datatable", location = "/count/datatable.jsp"),
		@Result(name = "flowCountListExport", type = "freemarker", location = "/b2b2c/admin/count/flowCountListExport.html")})
@Action("allianceCount")
public class AllianceCountAction extends WWAction {
	@SuppressWarnings("rawtypes")
	private Map map;
	private IAllianceCountManager allianceCountManager;
	private FlowCount flowCount;
	private Order my_order;
	private List list;

	private String user_ip;// 用户IP
	private String name;// 联盟商名称
	private String ad_name;// 广告名称
	private String come_time;// 进入时间
	private String go_time;// 离开时间
	private String order_status;// 是否完成订单
	private String order_create_time;// 订单生成时间
	private Integer count_id;
	private String start_time;
	private String stop_time;
	private String address;
	private String action;
	private Integer order_id;// 订单编号
	private String sn;// 订单编号
	private Integer flow_id;
	private String user_addr;
	private Integer ip_count;
	private Integer todayIPCount;
	private String user_name;
	Long startLong;
	Long stopLong;
	
	// 用户流量统计(总计)
	public String userFlowCount() {
		ip_count = this.allianceCountManager.flowIPCount();
		todayIPCount = this.allianceCountManager.todayIPCount();
		return "userFlowCount";
	}
	
	// 用户流量统计(详细)
	public String userFlowDetail() {
		this.start_time = start_time;
		this.stop_time = stop_time;
		this.flowCount = this.allianceCountManager.flowCountCheck(flow_id);
		return "userFlowDetail";
	}

	// 用户流量统计查看详情
	public String flowCountCheck(){
		this.flowCount = this.allianceCountManager.flowCountCheck(flow_id);
		return "flowCountCheck_list";
	}
	
	/**
	 * 用户流量统计列表(总计)
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	public String flowCountList() {
		this.map = new HashMap();
		map.put("start_time", this.start_time);
		map.put("stop_time", this.stop_time);
		this.webpage = this.allianceCountManager.flowCount_list(map, this.getPage(), this.getPageSize());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 用户流量统计列表(详细)
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access", "unchecked" })
	public String flowCountDetail() {
		this.map = new HashMap();
		map.put("user_ip", this.user_ip);
		map.put("user_name", this.user_name);
		map.put("order_status", this.order_status);
		map.put("start_time", this.start_time);
		map.put("stop_time", this.stop_time);
		this.webpage = this.allianceCountManager.flowCountDetail(map, user_addr, this.getPage(), this.getPageSize(),this.getSort(),this.getOrder());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 用户流量统计访问页面列表
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "static-access" })
	public String flowCountCheckList(){
		this.map = new HashMap();
		this.webpage = this.allianceCountManager.flowCountCheck_list(flow_id, this.getPage(), this.getPageSize(), this.getSort(), this.getOrder());
		this.showGridJson(webpage);
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 访问地饼状图
	 */
	@SuppressWarnings({ "rawtypes" })
	public String pieChart(){
		List addrlist = this.allianceCountManager.userAddrCount();
		List addrCountlist = this.allianceCountManager.userAddrNumCount();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		request .setAttribute("addrlist",addrlist);
		request .setAttribute("addrCountlist",addrCountlist);
		return "anglePie";
	}
	
	/**
	 * 访问数折线图
	 * @throws ParseException 
	 */
	@SuppressWarnings({ "rawtypes" })
	public String dataTable() throws ParseException{
		List dayUserList = null;
		dayUserList = this.allianceCountManager.dayUserCount();
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		request .setAttribute("dayUserList",dayUserList);
		return "datatable";
	}
	
	/**
	 * 用户流量统计导出
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String flowCountExport() {
		map = new HashMap();
		map.put("start_time", this.start_time);
		map.put("stop_time", this.stop_time);
		list = this.allianceCountManager.flowCountExport(map);
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		request.setAttribute("export", true);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=export_" + System.currentTimeMillis() + ".xls");
		return "flowCountExport";
	}
	
	/**
	 * 用户流量详细导出
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String flowCountListExport() {
		map = new HashMap();
		map.put("user_ip", this.user_ip);
		map.put("user_name", this.user_name);
		map.put("order_status", this.order_status);
		list = this.allianceCountManager.flowCountListExport(map, user_addr);
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpServletResponse response = ThreadContextHolder.getHttpResponse();
		request.setAttribute("export", true);
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-disposition", "attachment;filename=export_" + System.currentTimeMillis() + ".xls");
		return "flowCountListExport";
	}
	
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public String getOrder_create_time() {
		return order_create_time;
	}

	public void setOrder_create_time(String order_create_time) {
		this.order_create_time = order_create_time;
	}

	public Integer getCount_id() {
		return count_id;
	}

	public void setCount_id(Integer count_id) {
		this.count_id = count_id;
	}

	@SuppressWarnings("rawtypes")
	public Map getMap() {
		return map;
	}

	@SuppressWarnings("rawtypes")
	public void setMap(Map map) {
		this.map = map;
	}

	public IAllianceCountManager getAllianceCountManager() {
		return allianceCountManager;
	}

	public void setAllianceCountManager(
			IAllianceCountManager allianceCountManager) {
		this.allianceCountManager = allianceCountManager;
	}

	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAd_name() {
		return ad_name;
	}

	public void setAd_name(String ad_name) {
		this.ad_name = ad_name;
	}

	public String getCome_time() {
		return come_time;
	}

	public void setCome_time(String come_time) {
		this.come_time = come_time;
	}

	public String getGo_time() {
		return go_time;
	}

	public void setGo_time(String go_time) {
		this.go_time = go_time;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

//	public AllianceCount getAllianceCount() {
//		return allianceCount;
//	}
//
//	public void setAllianceCount(AllianceCount allianceCount) {
//		this.allianceCount = allianceCount;
//	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getStop_time() {
		return stop_time;
	}

	public void setStop_time(String stop_time) {
		this.stop_time = stop_time;
	}

	public Order getMy_order() {
		return my_order;
	}

	public void setMy_order(Order my_order) {
		this.my_order = my_order;
	}
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

//	public List<AllianceAddress> getAllianceAddress() {
//		return allianceAddress;
//	}
//
//	public void setAllianceAddress(List<AllianceAddress> allianceAddress) {
//		this.allianceAddress = allianceAddress;
//	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}
	
	public FlowCount getFlowCount() {
		return flowCount;
	}

	public void setFlowCount(FlowCount flowCount) {
		this.flowCount = flowCount;
	}

	public Integer getFlow_id() {
		return flow_id;
	}

	public void setFlow_id(Integer flow_id) {
		this.flow_id = flow_id;
	}
	
	public String getUser_addr() {
		return user_addr;
	}

	public void setUser_addr(String user_addr) {
		this.user_addr = user_addr;
	}
	
	public Integer getIp_count() {
		return ip_count;
	}

	public void setIp_count(Integer ip_count) {
		this.ip_count = ip_count;
	}

	public Integer getTodayIPCount() {
		return todayIPCount;
	}

	public void setTodayIPCount(Integer todayIPCount) {
		this.todayIPCount = todayIPCount;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

}
