package com.enation.app.shop.component.largeorder.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.largeorder.model.BigOrder;
import com.enation.app.shop.core.model.LargeOrder;
import com.enation.framework.database.Page;

/**
 * 大宗订单交易
 * @author jfw
 *2015-10-14上午09:14:39
 */
public interface ILargeOrderManager {
	
	/**
	 * 增加大宗交易意向
	 */
	public void add(LargeOrder largeOrder);
	/**
	 * 查询大宗交易意向
	 */
	public Page listMind(Map mindMap, int page, int pageSize, String sort,
			String order);
	/**
	 * 获取一单大宗交易意向
	 */
	public LargeOrder getLargeOrder(Integer mind_id);
	/**
	 * 修改一单大宗交易意向
	 */
	public void update(LargeOrder largeOrder);
	/**
	 * 查询大宗交易订单
	 */
	public Page listOrder(Map orderMap, int page, int pageSize, String sort,
			String order);
	/**
	 * 增加大宗交易订单
	 */
	public void addOrder(BigOrder bigOrder);
	/**
	 * 得到当前的大宗交易订单
	 * @param order_id
	 * @return
	 */
	public BigOrder getBigOrder(Integer order_id);
	/**
	 * 修改当前的大宗交易订单
	 * @param order_id
	 * @return
	 */
	public void updateBigOrder(BigOrder bigOrder);
	
}
