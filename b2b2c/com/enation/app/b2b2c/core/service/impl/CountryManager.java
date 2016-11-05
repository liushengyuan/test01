package com.enation.app.b2b2c.core.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.Country;
import com.enation.app.b2b2c.core.service.ICountryManager;
import com.enation.eop.sdk.database.BaseSupport;

/**
 * 国家管理Manger
 * @author WKZ
 * @date 2015-9-19 上午9:59:27
 */
@Component
public class CountryManager extends BaseSupport<Country> implements ICountryManager {

	@Override
	public List<Country> getList() {
		String sql = "SELECT * FROM country ";
		List<Country> list = this.baseDaoSupport.queryForList(sql, Country.class);
		return list;
	}
	

}
