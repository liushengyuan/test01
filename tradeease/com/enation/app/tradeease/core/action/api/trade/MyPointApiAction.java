package com.enation.app.tradeease.core.action.api.trade;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.FreezePoint;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.PointHistory;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.model.trade.Trade;
import com.enation.app.tradeease.core.service.trade.ITradeManager;
import com.enation.app.tradeease.core.util.LoanFtpUtils;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;


@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("mypoint")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class MyPointApiAction extends WWAction {

	private IStoreManager storeManager;
	private ITradeManager tradeManager;
	private IOrderManager orderManager;
	private IStoreOrderManager storeOrderManager;
	private IDaoSupport daoSupport;
	private  IMemberManager memberManager ;
	private  IMemberLvManager memberLvManager ;
	
	
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	
	public void point() {
		String sql="SELECT order_id from es_order_log  WHERE message='确认付款' and  ?-5  <= op_time and op_time <? order by order_id desc " ;
	//	//System.out.println(DateUtil.getDateline());
		List<Map> list= daoSupport.queryForList(sql,DateUtil.getDateline(),DateUtil.getDateline());
		for (Map map:list) {
			
			Order  order= 	orderManager.get(Integer.valueOf(map.get("order_id").toString()));
			////System.out.println(Integer.valueOf(map.get("order_id").toString())+"d订单id");
			String  sql2="select * from  es_freeze_point  where  orderid=?";
			FreezePoint  fPoint  =(FreezePoint) daoSupport.queryForObject(sql2, FreezePoint.class, Integer.valueOf(map.get("order_id").toString()));
			
			String sql3="update es_member set point=?+point ,mp=?+mp  where member_id=?";
			
			daoSupport.execute(sql3, fPoint.getPoint(),fPoint.getMp(),fPoint.getMemberid());
			Member 	member = this.memberManager.getmembert(fPoint.getMemberid());
			List< MemberLv > list3=this.memberManager.memberlvlist();
			for (MemberLv  lv   :  list3 ) {
				if (lv.getPoint()<=member.getPoint()) {
					//member.setLv_id(lv.getLv_id());
					String  sql4="update es_member set lv_id=?  where  member_id=?";
					//System.out.println(lv.getLv_id());
					daoSupport.execute(sql4,lv.getLv_id(), member.getMember_id());
				}
			}
			
			
			PointHistory  pHistory=new  PointHistory();
			pHistory.setMember_id(fPoint.getMemberid());
			pHistory.setPoint(fPoint.getPoint());
			pHistory.setMp(fPoint.getMp());
			pHistory.setOrder_id(Integer.valueOf(map.get("order_id").toString()));
			pHistory.setReason("订单付款成功");
			pHistory.setTime(DateUtil.getDateline());
			pHistory.setPoint_type(0);
			pHistory.setType(1);
			//System.out.println("point增加");
		 
			MemberLv memberLv = memberLvManager.get(member.getLv_id());
			
			pHistory.setEndtime(DateUtil.getDateline()+memberLv.getValidity()*30*24*60*60);
			daoSupport.insert("es_point_history", pHistory);
			//System.out.println("很好");
		}
	}
	
	/**
	 * 
	 * 
	 */
	
	

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public ITradeManager getTradeManager() {
		return tradeManager;
	}

	public void setTradeManager(ITradeManager tradeManager) {
		this.tradeManager = tradeManager;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}

	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}
	
}
