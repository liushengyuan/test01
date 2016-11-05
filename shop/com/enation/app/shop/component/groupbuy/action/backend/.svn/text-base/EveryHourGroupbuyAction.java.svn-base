package com.enation.app.shop.component.groupbuy.action.backend;

import java.io.File;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.shop.component.groupbuy.model.GroupBuy;
import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.app.shop.component.groupbuy.model.GroupBuyArea;
import com.enation.app.shop.component.groupbuy.model.GroupBuyCat;
import com.enation.app.shop.component.groupbuy.plugin.GroupbuyPluginBundle;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyAreaManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.DateUtil;

/**
 * 团购定时任务Action
 * @author kingapex
 */
@Component
@ParentPackage("eop_default")
@Namespace("/shop/admin")
@Action("groupBuy2")
public class EveryHourGroupbuyAction extends WWAction{
	
	 private IGroupBuyManager groupBuyManager;
	 private IDaoSupport daoSupport;
	 private GroupbuyPluginBundle groupbuyPluginBundle;
	 
	 public void edit(){
		 //System.out.println("团购定时任务开始执行了。");
		 this.groupBuyManager.updateNum();
	 }
	 
//	 public void end(){//北京时间11月6号早上5点触发
//		 //System.out.println("团购结束任务触发！时间："+DateUtil.getDateline());
//		//关闭团购
//		String sqlEnd = "SELECT * FROM es_groupbuy_active WHERE  act_status=1 ";
//		GroupBuyActive endGroupBuyActive = (GroupBuyActive) this.daoSupport.queryForObject(sqlEnd, GroupBuyActive.class);
//		Integer actId = 0;
//		if(endGroupBuyActive!=null){
//			actId = endGroupBuyActive.getAct_id();
//		}
//		String sql="UPDATE es_groupbuy_active SET act_status=2  WHERE act_status=1 ";
//		this.daoSupport.execute(sql); 
//		if(actId!=null&&!actId.equals(0)){
//			groupbuyPluginBundle.onGroupBuyEnd(actId);
//		}
//	 }
	 
	public IGroupBuyManager getGroupBuyManager() {
		return groupBuyManager;
	}

	public void setGroupBuyManager(IGroupBuyManager groupBuyManager) {
		this.groupBuyManager = groupBuyManager;
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public GroupbuyPluginBundle getGroupbuyPluginBundle() {
		return groupbuyPluginBundle;
	}

	public void setGroupbuyPluginBundle(GroupbuyPluginBundle groupbuyPluginBundle) {
		this.groupbuyPluginBundle = groupbuyPluginBundle;
	}
	
	
}
