package com.enation.app.shop.core.service;

import java.util.List;

import com.enation.app.shop.core.model.GoodsCatSpec;
import com.enation.framework.database.Page;
/**
 * 商品分类和商品规格管理类
 * @author liujisong
 *
 */
public interface IGoodsCatSpecManager {
	/**
	 * 添加商品分类和商品规格的关联
	 * @param name
	 */
	public void saveAdd(GoodsCatSpec catSpec);
	/**
	 * 根据关联id删除关联记录
	 * @param id
	 */
	public void delete(Integer id);
	/**
	 * 根据分类id和规格id查出
	 * @param cat_id
	 * @param spec_id
	 */
	public void delete(Integer cat_id,Integer spec_id);
	/**
	 * 根据分类id，查找这个分类关联的规格
	 * @param cat_id
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page findByCatId(Integer cat_id,int page,int pageSize);
	/**
	 * 根据分类id，查找这个分类关联的规格形成一个List
	 * @param cat_id
	 * @return
	 */
	public List<GoodsCatSpec> findSpecList(Integer cat_id);
}
