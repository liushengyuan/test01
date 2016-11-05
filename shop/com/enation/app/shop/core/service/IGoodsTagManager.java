package com.enation.app.shop.core.service;

import java.util.List;

import com.enation.framework.database.Page;

public interface IGoodsTagManager {

	/**
	 * 根据tagID取出商品列表
	 * @param tagid
	 * @return
	 */
	public Page getGoodsList(int tagid, int page, int pageSize);
	/**
	 * 根据标签Id获取标签商品列表
	 * @param tagid
	 * @return List
	 */
	public List getGoodsList(int tagid);
	
	/**
	 * 根据tagid和分类ID取出商品列表
	 * @param tagid
	 * @param catid
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page getGoodsList(int tagid,int catid, int page, int pageSize);
	/**
	 * 根据teamid取出商品列表
	 * @param teamid
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page getGoodsListForTeam(int teamid, int page, int pageSize);
	public Page getGoodsListForBrand(int teamid, int page, int pageSize);
	public Page getGoodsListBrand(int page, int pageSize);
	
	/**
	 * 添加商品的tag
	 * @param tagId
	 * @param goodsId
	 */
	public void addTag(int tagId, int goodsId);
	/**
	 * 添加商品的team
	 * @param teamId
	 * @param goodsId
	 */
	public void addTeam(int teamId,int googsId);
	public void addTeamBrand(int teamId,int googsId);
	
	/**
	 * 移除商品的tag
	 * @param tagId
	 * @param goodsId
	 */
	public void removeTag(int tagId, int goodsId);
	
	/**
	 * 批量更新商品显示排序
	 * @param goodsId
	 * @param tagids
	 * @param ordernum
	 */
	public void updateOrderNum(Integer[] goodsId,Integer[] tagids, Integer[] ordernum);
	public void deleteOrdernum(Integer[] goods_id, Integer[] tagids,
			Integer[] ordernum);
}
