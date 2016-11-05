package com.enation.app.shop.component.groupbuy.plugin.act;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryDayExecuteEvent;
import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.app.shop.component.groupbuy.plugin.GroupbuyPluginBundle;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.DateUtil;
/**
 * 团购活动插件
 * @author fenlongli
 *
 */
@Component
public class GroupBuyActPlugin extends AutoRegisterPlugin implements IEveryDayExecuteEvent{
	private IDaoSupport daoSupport;
	private IGroupBuyManager groupBuyManager;
	private IGroupBuyActiveManager groupBuyActiveManager;
	private GroupbuyPluginBundle groupbuyPluginBundle;
	/**
	 * 当团购达到结束时间就关闭团购
	 * 当团购达到开始时间就开启团购
	 */
	@Override
	public void everyDay() {//每晚23:30执行
		////System.out.println("1、团购everyday定时任务开始执行了，开始时间："+DateUtil.getDateline());
		long time = DateUtil.getDateline()+3600;//往后推一个小时。如果time大于开始时间，则开启此团购
		String sqlStart = "SELECT * FROM es_groupbuy_active WHERE  act_status=0 AND start_time<?";
		Integer actId = 0;
		GroupBuyActive startGroupBuyActive = (GroupBuyActive) this.daoSupport.queryForObject(sqlStart, GroupBuyActive.class, time);
		if(startGroupBuyActive!=null){
			actId = startGroupBuyActive.getAct_id();
		}
		//开启团购
		String sql="UPDATE es_groupbuy_active SET act_status=1  WHERE act_status=0 AND start_time<?";
		this.daoSupport.execute(sql, time);//DateUtil.getDateline()
		//actId=this.daoSupport.getLastId("es_groupbuy_active");
		if(actId!=null&&!actId.equals(0)){
			groupbuyPluginBundle.onGroupBuyStart(actId);
		}
		
		//关闭团购
		String sqlEnd = "SELECT * FROM es_groupbuy_active WHERE  act_status=1 AND end_time<?";
		GroupBuyActive endGroupBuyActive = (GroupBuyActive) this.daoSupport.queryForObject(sqlEnd, GroupBuyActive.class, time);
		if(endGroupBuyActive!=null){
			actId = endGroupBuyActive.getAct_id();
		}
		sql="UPDATE es_groupbuy_active SET act_status=2  WHERE act_status=1 AND end_time<?";
		this.daoSupport.execute(sql, time);
		//actId=this.daoSupport.getLastId("es_groupbuy_active");
		////System.out.println("2、关闭团购。最后更新的主键id为："+actId);
		if(actId!=null&&!actId.equals(0)){
			groupbuyPluginBundle.onGroupBuyEnd(actId);
		}
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	public IGroupBuyManager getGroupBuyManager() {
		return groupBuyManager;
	}
	public void setGroupBuyManager(IGroupBuyManager groupBuyManager) {
		this.groupBuyManager = groupBuyManager;
	}
	public IGroupBuyActiveManager getGroupBuyActiveManager() {
		return groupBuyActiveManager;
	}
	public void setGroupBuyActiveManager(
			IGroupBuyActiveManager groupBuyActiveManager) {
		this.groupBuyActiveManager = groupBuyActiveManager;
	}
	public GroupbuyPluginBundle getGroupbuyPluginBundle() {
		return groupbuyPluginBundle;
	}
	public void setGroupbuyPluginBundle(GroupbuyPluginBundle groupbuyPluginBundle) {
		this.groupbuyPluginBundle = groupbuyPluginBundle;
	}
	
}
