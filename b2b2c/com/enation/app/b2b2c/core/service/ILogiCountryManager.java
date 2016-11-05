package com.enation.app.b2b2c.core.service;

import java.util.List;

import com.enation.app.shop.core.model.Logi;

/**
 * 国家物流关联类
 * @author WKZ
 * @date 2015-9-19 上午9:07:57
 */
public interface ILogiCountryManager {
	
	/**
	 * 根据国家简写    获取物流公司
	 * @author WKZ
	 */
	public List<Logi> getLogiByCountrycode(String country_code);
	
	
}
