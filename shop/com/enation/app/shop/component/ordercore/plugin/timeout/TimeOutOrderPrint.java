package com.enation.app.shop.component.ordercore.plugin.timeout;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryDayExecuteEvent;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.DateUtil;

/**
 * 不付款的订单72小时内就取消
 * @author LiFenLong
 *
 */
@Component
public class TimeOutOrderPrint extends AutoRegisterPlugin implements IEveryDayExecuteEvent{
	private IDaoSupport daoSupport;
	private IOrderFlowManager orderFlowManager;
	/**
	 * 每晚23:30分执行
	 * 订单10天之内没有进行付款的，系统自动取消，并恢复库存；其中864000表示10天的总秒数，1398873600表示2014-05-01 00:00:00
	 */
	@Override
	public void everyDay() {
		String sql="SELECT order_id from es_order  WHERE disabled=0 AND create_time+?<? AND (status=? or status=?) AND create_time>? AND store_id IS NOT NULL";
		List<Map> list= daoSupport.queryForList(sql,864000,DateUtil.getDateline(),OrderStatus.ORDER_NOT_PAY,OrderStatus.ORDER_NOT_CONFIRM,1398873600);
		for (Map map:list) {
			orderFlowManager.cancel(Integer.parseInt(map.get("order_id").toString()), "订单10天之内没有进行付款");
		}
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	public IOrderFlowManager getOrderFlowManager() {
		return orderFlowManager;
	}
	public void setOrderFlowManager(IOrderFlowManager orderFlowManager) {
		this.orderFlowManager = orderFlowManager;
	}
}
