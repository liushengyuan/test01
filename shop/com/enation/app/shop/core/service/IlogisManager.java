package com.enation.app.shop.core.service;

import java.util.List;

import com.enation.app.shop.core.model.Logi;

/**
 * 配送中心
 * 
 * @author lxy<br/>
 *         2015-11-12上午
 *         version 1.0
 */
public interface IlogisManager {
	/**
	 * 获取配送商列表
	 * @return 配送商列表
	 */
	public List<Logi> find();
}
