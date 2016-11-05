package com.enation.app.tradeease.core.service.accont.impl;

import java.util.List;
import java.util.Map;

import com.enation.app.tradeease.core.model.account.SellerCard;
import com.enation.app.tradeease.core.service.accont.ISellerCardManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

@SuppressWarnings({ "rawtypes" })
public class SellerCardManager extends BaseSupport implements
		ISellerCardManager {

	@Override
	public void updateStatus(Integer isEnable, Integer status, Integer id) {
		// TODO Auto-generated method stub
		if (isEnable != null && status != null && id != null) {
			String sql = "update  seller_card set is_enable=" + isEnable
					+ ",status=" + status + "  where id=" + id;
			this.baseDaoSupport.execute(sql);
		}
	}

	@Override
	public Page list(Integer storeId, String searchKeyword, Integer isEnable,
			int pageNo, int pageSize) {

		String sql = "SELECT sc.* from es_seller_card sc where sc.card_num !='0000000000000000' ";
		if (storeId != null) {
			sql = sql + " and sc.store_id=" + storeId;
		}
		if (searchKeyword != null && searchKeyword.length() > 0) {
			sql = sql + " and ( sc.store_name like '%" + searchKeyword
					+ "%' or sc.card_num like '%" + searchKeyword + "%')";
		}
		if (isEnable != null && isEnable != -2) {
			sql = sql + " and sc.status=" + isEnable;
		}
		Page page = this.daoSupport.queryForPage(sql, pageNo, pageSize);

		return page;
	}

	@Override
	public void addCard(SellerCard sellerCard) {
		this.baseDaoSupport.insert("es_seller_card", sellerCard);
	}

	@Override
	public SellerCard getAccountById(Integer id) {
		String sql = "select * from es_seller_card where id=? ";
		SellerCard card = (SellerCard) this.baseDaoSupport.queryForObject(sql,
				SellerCard.class, id);
		return card;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void saveAccount(SellerCard card) {
		this.baseDaoSupport
				.update("es_seller_card", card, "id=" + card.getId());
	}

	@Override
	public List<SellerCard> queryList() {
		String sql = " select * from es_seller_card ";
		List<SellerCard> cardList = this.baseDaoSupport.queryForList(sql, SellerCard.class);
		return cardList;
	}

	@Override
	public SellerCard getSingleBankCard(String memberName,String card_num) {
		String sql = " select * from seller_card where login_name = ? AND card_num = ? ";
		return (SellerCard) this.baseDaoSupport.queryForObject(sql, SellerCard.class, memberName,card_num);
	}

	@Override
	public void modifySingleBankCard(Map map,String id) {
		this.baseDaoSupport.update("seller_card", map, " id = " + id);
	}
	
	

}
