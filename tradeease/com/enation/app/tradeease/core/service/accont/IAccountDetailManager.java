package com.enation.app.tradeease.core.service.accont;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.framework.database.Page;

/**
 * 交易明细查询接口
 * @author jfw
 *
 */
public interface IAccountDetailManager {
	/**
	 * 查询所有的交易记录
	 * @param member_id 商户编号
	 * 
	 */

	public Page list(Integer member_id, int pageNo, int pageSize,
			long start_time, long end_time);
	/**
	 * 添加
	 */
	public void add(AccountDetail accountDetail);
	/**
	 * 查询店铺账户日志表
	 * @param map
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page store_account_list(Map map,int pageNo,int pageSize);
	
}
