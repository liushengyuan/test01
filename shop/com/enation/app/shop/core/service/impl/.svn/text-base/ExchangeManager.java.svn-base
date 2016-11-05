package com.enation.app.shop.core.service.impl;

import java.util.List;

import com.enation.app.shop.core.model.Exchange;
import com.enation.app.shop.core.service.IExchangeManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;
 
/**
 * 汇率管理类
 * @author LiFenLong 2014-4-2;4.0改版修改delete方法参数为integer[]
 *
 */
public class ExchangeManager extends BaseSupport implements IExchangeManager{
	

	
	public List list() {
		String sql = "select e.id as id ,e.currency as currency ,e.exchange_price as exchange_price  from es_exchange e";
		return this.baseDaoSupport.queryForList(sql);
	}

	@Override
	public Page pageExchange(String order, Integer page, Integer pageSize) {
		order = order == null ? "e. id desc" : order;
		String sql = "select e.id as id ,e.currency as currency ,e.exchange_price as exchange_price  from es_exchange e";
		sql += " order by  " + order;
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}

	@Override
	public void saveAdd(Exchange exchange) {
		this.baseDaoSupport.insert("es_exchange", exchange);
	}

	@Override
	public Exchange getExchangeById(Integer eid) {
		String sql  = "select e.id as id ,e.currency as currency ,e.exchange_price as exchange_price  from es_exchange e where e.id=?";
		Exchange exchange =  (Exchange) this.baseDaoSupport.queryForObject(sql, Exchange.class, eid);
		return exchange;
	}

	@Override
	public void saveEdit(Exchange exchange) {
		this.baseDaoSupport.update("es_exchange", exchange, "id="+exchange.getId());
	}

	@Override
	public void delete(Integer[] ids) {
		String id =StringUtil.implode(",", ids);//将数组转化为字符串,数据之间用逗号隔开
		if(id==null || id.equals("")){return ;}
		String sql = "delete from es_exchange where id in (" + id + ")";
		this.baseDaoSupport.execute(sql);
	}
	@Override
	public Double getExchange(String currency) {
		String sql = "SELECT e.exchange_price  FROM es_exchange e WHERE currency = '"+currency+"'";
		String price = this.baseDaoSupport.queryForString(sql);
		return Double.parseDouble(price);
	}

}
