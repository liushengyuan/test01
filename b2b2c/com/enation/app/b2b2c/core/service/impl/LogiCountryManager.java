package com.enation.app.b2b2c.core.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.ILogiCountryManager;
import com.enation.app.shop.core.model.Logi;
import com.enation.eop.sdk.database.BaseSupport;

/**
 * 物流国家关联管理
 * @author WKZ
 * @date 2015-9-19 上午9:09:51
 */
@Component
public class LogiCountryManager extends BaseSupport<Logi> implements ILogiCountryManager {

	@Override
	public List<Logi> getLogiByCountrycode(String country_code) {
		String sql = "SELECT * FROM logi_company WHERE id IN " +
					 "( SELECT logi_id FROM es_country c,es_logi_country l " +
					 "WHERE c.country_code = ? AND l.country_id = c.country_id ) ";
		List<Logi> list = this.baseDaoSupport.queryForList(sql, Logi.class, country_code);
		return list;
	}


}
