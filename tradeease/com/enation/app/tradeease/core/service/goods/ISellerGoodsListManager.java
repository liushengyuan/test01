package com.enation.app.tradeease.core.service.goods;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

/**
 * 卖家中心 产品列表查询
 * 
 * @author yanpeng 2015-6-24 下午1:27:29
 * 
 */
public interface ISellerGoodsListManager {

	/**
	 * 卖家中心 产品列表查询
	 * 
	 * @Description: TODO
	 * @param
	 * @return Page
	 */
	Page queryList(int page, int pageSize, Map map);

	// 查询全部
	long queryListall(int store_id);

	// 查询已上架
	long queryListAllOne(int store_id);

	// 查询待审核
	long queryListAllTwo(int store_id);

	// 查询审核未通过
	long queryListUnapprove(int store_id);

	// 已下架
	long queryListOut(int store_id);
	//草稿
	long queryListDraft(int store_id);
}