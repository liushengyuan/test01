package com.enation.app.tradeease.core.service.accont;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.model.account.SellerCard;
import com.enation.framework.database.Page;

public interface ISellerCardManager {
	/**
	 * 更新卖家银行卡的状态信息
	 * 
	 * @param isEnable
	 * @param status
	 */
	public void updateStatus(Integer isEnable, Integer status, Integer id);

	/**
	 * 查询所有的店铺的银行卡信息
	 * 
	 * @param member_id
	 *            商户编号
	 * 
	 */
	public Page list(Integer storeId, String searchKeyword, Integer isEnable,
			int pageNo, int pageSize);

	/**
	 * 新增银行卡账户
	 * 
	 * @param sellerCard
	 */
	public void addCard(SellerCard sellerCard);

	/**
	 * 根据账户ID查询账户
	 * 
	 * @param id
	 * @return
	 */
	public SellerCard getAccountById(Integer id);

	/**
	 * 修改银行卡账户
	 * 
	 * @param card
	 */
	public void saveAccount(SellerCard card);

	/**
	 * 查询所有的银行卡列表
	 * 
	 * @return
	 */
	public List<SellerCard> queryList();
	
	/**
	 * 根据Id获取单个银行卡信息
	 * @author WKZ
	 */
	public SellerCard getSingleBankCard(String memberName,String card_num);
	
	/**
	 * 修改单个银行卡信息
	 * @author WKZ
	 */
	public void modifySingleBankCard(Map map,String id);
}
