package com.enation.app.tradeease.core.service.accont.impl;

import com.enation.app.tradeease.core.service.accont.IMyAccountManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;


@SuppressWarnings({ "rawtypes" })
public class MyAccountManager extends BaseSupport implements IMyAccountManager {

	@Override
	public Page list(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time) {
		
		String sql = "SELECT st.* from es_store st where st.member_id = ?  ";
		
		if (start_time > 0 && end_time > 0) {
			sql += " and st.create_time>=" + start_time + " and st.create_time<"
					+ end_time;
		}
		
		sql += "  order by st.create_time desc ";
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize,
				member_id);
		
		return page;
	}

}
