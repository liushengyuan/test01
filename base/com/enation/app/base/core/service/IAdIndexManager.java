package com.enation.app.base.core.service;

import com.enation.app.base.core.model.AdIndex;
import com.enation.framework.database.Page;

/**
 * 首页大屏广告接口
 * @author WKZ
 * @date 2015-9-2 上午10:37:53
 */
public interface IAdIndexManager {

	public Page getAdIndexPage(Integer pageNo, Integer pageSize);
	
	public AdIndex getSingleAdIndex(Integer id);
	
	public void modifyAdIndex(AdIndex adIndex);
	
	public void modifyAdIndexStatus(Integer id,Integer status);
	
}
