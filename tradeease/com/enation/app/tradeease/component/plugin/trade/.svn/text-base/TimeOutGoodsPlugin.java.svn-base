package com.enation.app.tradeease.component.plugin.trade;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.base.core.plugin.job.IEveryDayExecuteEvent;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.DateUtil;

/**
 * 不付款的订单72小时内就取消
 * @author LiFenLong
 *
 */
@Component
public class TimeOutGoodsPlugin extends AutoRegisterPlugin implements IEveryDayExecuteEvent{
	private IDaoSupport daoSupport;
	private IGoodsManager goodsManager;
	@Override
	public void everyDay() {
		String sql="SELECT * from es_goods  WHERE market_enable=1 and expire_time<?";
		List<Goods> list=daoSupport.queryForList(sql,Goods.class,DateUtil.getDateline());
		for(Goods goods:list){
			goods.setMarket_enable(0);//使到期商品下架
			this.goodsManager.myEditGood(goods);
		}
		//System.out.println("自动任务，到期商品自动下架！");
/*		String sql="SELECT order_id from es_order  WHERE disabled=0 AND create_time+?<? AND (status=? or status=?) AND create_time>?";
		//System.out.println(DateUtil.getDateline());
		List<Map> list= daoSupport.queryForList(sql,259200,DateUtil.getDateline(),OrderStatus.ORDER_NOT_PAY,OrderStatus.ORDER_NOT_CONFIRM,1398873600);
		for (Map map:list) {
			orderFlowManager.cancel(Integer.parseInt(map.get("order_id").toString()), "订单72小时没有进行库款");
		}*/
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	
}
