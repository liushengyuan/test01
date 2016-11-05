package com.enation.app.tradeease.core.service.cordova.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.Delivery;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.service.IMemberOrderManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.app.tradeease.core.service.cordova.IMemberOrderManagerApp;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;


public class MemberOrderManagerApp extends BaseSupport implements
		IMemberOrderManagerApp {

	private IMemberManager memberManager;
	public Page pageOrders(int pageNo, int pageSize) {
		Member member = UserConext.getCurrentMember();
		
		String sql = "select * from order where member_id = ? and disabled=0 order by create_time desc";
		Page rpage = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize, member.getMember_id());
		List<Map> list = (List<Map>) (rpage.getResult());
		return rpage;
	}
	
	public Page pageOrders(int pageNo, int pageSize,String status, String keyword,Integer member_id){
		
		String sql = "select * from " + this.getTableName("order") + " where member_id = '" + member_id + "' and disabled=0";
		if(!StringUtil.isEmpty(status)){
			int statusNumber = -999;
			statusNumber = StringUtil.toInt(status);
			//等待付款的订单 按付款状态查询
			if(statusNumber==0){
				sql+=" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_NO;
			}else{
				sql += " AND status='" + statusNumber + "'";
			}
		}
		if(!StringUtil.isEmpty(keyword)){
			sql += " AND order_id in (SELECT i.order_id FROM " + this.getTableName("order_items") + " i LEFT JOIN "+this.getTableName("order")+" o ON i.order_id=o.order_id WHERE o.member_id='"+member_id+"' AND i.name like '%" + keyword + "%')";
		}
		sql += " order by create_time desc";
	 
		Page rpage = this.daoSupport.queryForPage(sql,pageNo, pageSize, Order.class);
//		List<Order> orderList= (List<Order>) rpage.getResult(); 
//		for(Order o:orderList){
//		    String itemJson = o.getItems_json();
//		    if(itemJson!=null){
//		        //System.out.println(itemJson+"就这个。");
//		    }
//		}
		return rpage;
	}
	
	public List<Map> listOrder(String status, String keyword,Integer member_id,Integer page,Integer pageSize){
		String sql = "select * from " + this.getTableName("order") +" o,"+this.getTableName("store") +" s" + " where o.member_id = '" + member_id + "' and o.disabled=0 and o.parent_id is not null and o.store_id=s.store_id";
		if(!StringUtil.isEmpty(status)){
			int statusNumber = -999;
			statusNumber = StringUtil.toInt(status);
			//等待付款的订单 按付款状态查询
			if(statusNumber==0){
				sql+=" AND o.status!="+OrderStatus.ORDER_CANCELLATION+" AND o.pay_status="+OrderStatus.PAY_NO;
			}else{
				sql += " AND o.status='" + statusNumber + "'";
			}
		}
		if(!StringUtil.isEmpty(keyword)){
			sql += " AND o.order_id in (SELECT i.order_id FROM " + this.getTableName("order_items") + " i LEFT JOIN "+this.getTableName("order")+" o ON i.order_id=o.order_id WHERE o.member_id='"+member_id+"' AND i.name like '%" + keyword + "%')";
		}
		sql += " order by o.create_time desc";
		List<Map> list = this.daoSupport.queryForListPage(sql, page, pageSize);
		return list;
	}
	
	public Page pageGoods(int pageNo, int pageSize,String keyword){
		Member member = UserConext.getCurrentMember();
		
		String sql = "select * from goods where goods_id in (SELECT i.goods_id FROM es_order_items i LEFT JOIN order o ON i.order_id=o.order_id WHERE o.member_id='"+member.getMember_id()
				+"' AND o.status in (" + OrderStatus.ORDER_COMPLETE +","+OrderStatus.ORDER_ROG+ " )) ";
		if(!StringUtil.isEmpty(keyword)){
			sql += " AND (sn='" + keyword + "' OR name like '%" + keyword + "%')";
		}
		sql += " order by goods_id desc";
		Page rpage = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize);
		List<Map> list = (List<Map>) (rpage.getResult());
		return rpage;
	}
	
	
	public Delivery getOrderDelivery(int order_id) {
		Delivery delivery = (Delivery)this.baseDaoSupport.queryForObject("select * from delivery where order_id = ?", Delivery.class, order_id);
		return delivery;
	}
	
	
	public List listOrderLog(int order_id) {
		String sql = "select * from order_log where order_id = ?";
		List list = this.baseDaoSupport.queryForList(sql, order_id);
		list = list == null ? new ArrayList() : list;
		return list;
	}

	
	public List listGoodsItems(int order_id) {
		String sql = "select * from order_items where order_id = ?";
		List list = this.baseDaoSupport.queryForList(sql, order_id);
		list = list == null ? new ArrayList() : list;
		return list;
	}

	
	public List listGiftItems(int orderid) {
		String sql  ="select * from order_gift where order_id=?";
		return this.baseDaoSupport.queryForList(sql, orderid);
	}


	public boolean isBuy(int goodsid) {
		Member member = UserConext.getCurrentMember();
		if(member==null) return false;
		String sql  ="select count(0) from " + this.getTableName("order_items") +
					 " where  order_id in(select order_id from "+this.getTableName("order")+" where member_id=?) and goods_id =? ";
		int count  = this.daoSupport.queryForInt(sql, member.getMember_id(),goodsid);
		return count>0;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}
	/**
	 * 获取各种订单状态的数量
	 */
	@Override
	public Map orderCount() {
		try {
			Member member = UserConext.getCurrentMember();
			String sql="select count(order_id) from "+this.getTableName("order")+" where member_id="+member.getMember_id()+" and status!="+OrderStatus.ORDER_CANCELLATION+" and disabled=0 and parent_id is not null and pay_status="+OrderStatus.PAY_NO;
			String sql2="select count(order_id) from "+this.getTableName("order")+" where member_id="+member.getMember_id()+" and status=2 and disabled=0 and parent_id is not null";
			String sql5="select count(order_id) from "+this.getTableName("order")+" where member_id="+member.getMember_id()+" and status=5 and disabled=0 and parent_id is not null";
			String sql7="select count(order_id) from "+this.getTableName("order")+" where member_id="+member.getMember_id()+" and status=7 and disabled=0 and parent_id is not null";
			String sql8="select count(order_id) from "+this.getTableName("order")+" where member_id="+member.getMember_id()+" and status=8 and disabled=0 and parent_id is not null";
			int status =this.daoSupport.queryForInt(sql);
			int status2 =this.daoSupport.queryForInt(sql2);
			int status5 =this.daoSupport.queryForInt(sql5);
			int status7 =this.daoSupport.queryForInt(sql7);
			int status8 =this.daoSupport.queryForInt(sql8);
			
			Map map = new HashMap();
			map.put("status", status);
			map.put("status2", status2);
			map.put("status5", status5);
			map.put("status7", status7);
			map.put("status8", status8);
			return  map;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
		//return null;
		
		
	}

}
