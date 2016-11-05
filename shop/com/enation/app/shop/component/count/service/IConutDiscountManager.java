package com.enation.app.shop.component.count.service;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.framework.database.Page;

/**
 * 促销活动统计管理
 * @author jfw
 *2015-10-9下午14:43:39
 */
public interface IConutDiscountManager {
	
	/**
	 * 查询参加打折促销活动的商户列表
	 */
	public List getStoreList();
	/**
	 * 查询打折的商品以及店铺
	 */
	public Page searchGoods(int page, int pageSize);
	/**
	 * 查询参加优惠券活动的商户列表
	 */
	public List getBonusList();
}
