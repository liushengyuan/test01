package com.enation.app.shop.component.count.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.bonus.service.IBonusTypeManager;
import com.enation.app.shop.component.count.service.IConutDiscountManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;


/**
 * 促销活动统计管理
 * @author jfw
 *2015-10-9下午14:43:39
 */
@Component
public class ConutDiscountManager extends BaseSupport  implements IConutDiscountManager  {

	@Override
	public List getStoreList() {
		String sql = "SELECT DISTINCT(g.store_id), g.store_name FROM es_goods g where g.is_discount=1 ";
		return this.baseDaoSupport.queryForList(sql);
	}

	@Override
	public Page searchGoods(int page, int pageSize) {
		String sql = "SELECT g.store_id,g.store_name,g.`name`,g.goods_id  FROM es_goods g where g.is_discount=1 ORDER BY g.store_id DESC";
		Page webpage = this.daoSupport.queryForPage(sql, page,
				pageSize);
		return webpage;
	}

	@Override
	public List getBonusList() {
		String sql = "SELECT DISTINCT(b.store_id) FROM es_bonus_type b WHERE b.store_id IS NOT NULL";
		return this.baseDaoSupport.queryForList(sql);
	}

	

}
