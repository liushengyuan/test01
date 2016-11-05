package com.enation.app.shop.core.service.impl;

import java.util.List;

import com.enation.app.shop.core.model.Logi;
import com.enation.app.shop.core.service.IlogisManager;
import com.enation.eop.sdk.database.BaseSupport;

public class LogisManager extends BaseSupport<Logi> implements IlogisManager{

	@Override
	public List<Logi> find() {
		String sql="select * from es_logi_company";
		List<Logi> findAllLogi=this.baseDaoSupport.queryForList(sql, Logi.class);
		return findAllLogi;
	}

}
