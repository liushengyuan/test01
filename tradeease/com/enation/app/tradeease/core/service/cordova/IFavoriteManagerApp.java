package com.enation.app.tradeease.core.service.cordova;

import java.util.List;
import java.util.Map;

import com.enation.app.shop.core.model.Favorite;
import com.enation.framework.database.Page;

/**
 * 商品收藏管理
 * 
 * @author lzf<br/>
 *         2010-3-24 下午02:39:25<br/>
 *         version 1.0<br/>
 */
public interface IFavoriteManagerApp {

	/**
	 * 列表我的收藏
	 * 
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page list(int pageNo, int pageSize);
	
	
	
	/**
	 * 读取某个会员的收藏 
	 * @param memberid
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page list(int memberid,int pageNo,int pageSize);
	
	public List list1();
	/**
	 * 获取收藏店铺列表
	 * @return
	 */
	public List<Map> liststore();
	public List listStore2();
	
	/**
	 * 读取会员的所有收藏商品
	 * @return
	 */
	public List list();

	/**
	 * 删除店铺收藏
	 * 
	 * @param storeCollectId
	 */
	public void deleteByStoreId(int storeCollectId);
	/**
	 * 删除商品收藏
	 * @return
	 */
	public void deleteByGoodsIdAndMember(int goods_id,int member_id);
	
	/**
	 * 添加店铺收藏
	 * 
	 */
	public void addstore(Integer store_id);
	/**
	 * 添加一个收藏
	 * @param goodsid
	 * @param memberid
	 */
	public void add(Integer goodsid);
	/**
	 * 根据商品ID和用户ID获取一个收藏
	 * @param goodsid 商品Id
	 * @param memeberid 会员Id
	 * @return 个数
	 */
	public int getCount(Integer goodsid, Integer memeberid);
	/**
	 * 获取会员收藏个数
	 * @param memberId 会员ID
	 * @return 收藏个数
	 */
	public int getCount(Integer memberId);
	
	public int getStoreCount(Integer store_id);
	//删除商品的历史纪录
	public void deleteByHistory(int goods_id,int member_id);
	//添加商品的历史纪录
	public void addHistory(Integer goodsid);
	//获取商品收藏
	public List list7();
	public int getNum(Integer goodsid, Integer memeberid);
	/**
	 * 根据店铺id和member_id删除店铺收藏
	 * @param store_id
	 * @param member_id
	 */
	public void deleteFavoriteByStoreId(int store_id,int member_id);
}
