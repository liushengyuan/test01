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
@Action("mymp")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class MyMpApiAction extends WWAction {

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
	
	public void mp() {
	String  sql="select member_id,SUM(mp) as mp  from es_point_history   where endtime>0 and  type=1  and endtime>time and endtime<?  GROUP BY  member_id";
	String  sql2="select SUM(mp) as mp from es_point_history where   type=0  and  member_id=?  ";
    List<Map>   ph1=  daoSupport.queryForList(sql, DateUtil.getDateline());
   
    for (Map  pHistory   : ph1  ) {
    int  member_id=	 Integer.valueOf(pHistory.get("member_id").toString())  ;
    Member  member=memberManager.getmembert(member_id);
    Integer  mp =member.getMp();
     
    Integer  mp2;
    mp2 = daoSupport.queryForInt(sql2, member_id);
    int index=pHistory.get("mp").toString().indexOf(".");
 // //System.out.println(Integer.parseInt(pHistory.get("mp").toString().substring(0,index)) );
  String sql4="select SUM(mp) as mp from es_point_history  where  member_id=? and  type=1  ";
  Integer  mp4 = daoSupport.queryForInt(sql4, member_id);
if (Integer.parseInt(pHistory.get("mp").toString().substring(0,index) )>0 ) {
	   Integer    mp3=  Integer.valueOf(pHistory.get("mp").toString().substring(0,index))- mp2  ;
	   String sql3=" update es_member set mp=mp-?  where  member_id=?";
	   //获得的-（过期的-使用的）和现有的比较
	   //（过期的-使用的）是真正消耗的
	   if (mp>mp4-mp3) {
		   daoSupport.execute(sql3,mp3, member_id);
		//   //System.out.println("到期了");
	}
	//  daoSupport.execute(sql3,mp3, member_id);
	   
   	}
   
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
