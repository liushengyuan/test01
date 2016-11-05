package com.enation.app.shop.core.service;

import java.util.List;

import com.enation.app.shop.core.model.Exchange;
import com.enation.framework.database.Page;
/**
 * 汇率管理类
 * @author jfw
 *
 */
public interface IExchangeManager {
	
	
	/**
	 * 读取所有汇率列表
	 * @return
	 */
	public List list();
	
	
	/**
	 * 分页读取汇率
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page pageExchange(String order ,Integer page,Integer pageSize);


	public void saveAdd(Exchange exchange);


	public Exchange getExchangeById(Integer eid);


	public void saveEdit(Exchange exchange);


	public void delete(Integer[] ids);
	/**
	 * 根据货币。获取设置的汇率
	 * @param currency
	 * @return
	 */
	public Double getExchange(String currency);
}
