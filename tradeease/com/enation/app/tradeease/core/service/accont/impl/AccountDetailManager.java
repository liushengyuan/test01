package com.enation.app.tradeease.core.service.accont.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.service.accont.IAccountDetailManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

@SuppressWarnings({ "rawtypes" })
public class AccountDetailManager extends BaseSupport implements IAccountDetailManager{

	@Override
	public Page list(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time) {

		String sql = "SELECT ad.* from es_account_detail ad where ad.member_id = ? ";
		
		if (start_time > 0 && end_time > 0) {
			sql += " and ad.create_time>=" + start_time + " and ad.create_time<"
					+ end_time;
		}
		sql += "  order by ad.create_time desc ";
		
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize,
				member_id);
		return page;
	}

	@Override
	public void add(AccountDetail accountDetail) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.insert("account_detail", accountDetail);
		int rec_id = this.baseDaoSupport.getLastId("accountDetail");
		accountDetail.setRec_id(rec_id);
	}

	@Override
	public Page store_account_list(Map map, int pageNo, int pageSize) {
		// TODO Auto-generated method stub
		StringBuffer sql=new StringBuffer("");
		Integer type=(Integer) map.get("type")==null?-2:(Integer) map.get("type");
		String store_name=map.get("name")==null?"":map.get("name").toString();
		if(type.equals(-2)){
			sql.append("select a.* from es_account_detail a   where  type!="+type);
		}
		else{
			sql.append("select a.* from es_account_detail a   where  type="+type);
		}
		if(!StringUtil.isEmpty(store_name)){
			sql.append( "  and a.store_name like '%" + store_name + "%'");
		}
		//sql.append("select a.* from es_account_detail a");
		return this.daoSupport.queryForPage(sql.toString(),pageNo ,pageSize);
	}	
	
}
