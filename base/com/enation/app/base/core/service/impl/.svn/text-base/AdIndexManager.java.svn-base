package com.enation.app.base.core.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.AdIndex;
import com.enation.app.base.core.service.IAdIndexManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 首页大屏广告管理
 * @author WKZ
 * @date 2015-9-2 上午10:41:04
 */
public class AdIndexManager extends BaseSupport<AdIndex> implements
		IAdIndexManager {
	
	@Override
	public Page getAdIndexPage(Integer pageNo, Integer pageSize) {
		String sql = "SELECT * FROM ad_index ";
		Page page = this.baseDaoSupport.queryForPage(sql, pageNo, pageSize, AdIndex.class);
		return page;
	}
	
	@Override
	public AdIndex getSingleAdIndex(Integer id) {
		String sql = "SELECT * FROM ad_index WHERE adindex_id = ? ";
		AdIndex ad = this.baseDaoSupport.queryForObject(sql, AdIndex.class, id);
		return ad;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modifyAdIndex(AdIndex adIndex) {
		Map<String, Object> fields = new HashMap<String, Object>();
		if(!StringUtil.isEmpty(adIndex.getAdindex_url())) {
			fields.put("adindex_url", adIndex.getAdindex_url());
		}
		fields.put("adindex_disable", adIndex.getAdindex_disable());
		fields.put("outer_css", adIndex.getOuter_css());
		fields.put("close_css", adIndex.getClose_css());
		fields.put("jump_css", adIndex.getJump_css());
		this.baseDaoSupport.update("ad_index", fields, " adindex_id = " + adIndex.getAdindex_id());
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void modifyAdIndexStatus(Integer id, Integer status) {
		Map<String,Object> fields = new HashMap<String,Object>();
		fields.put("adindex_disable", status);
		this.baseDaoSupport.update("ad_index", fields, " adindex_id = "+id);
	}
	
}
