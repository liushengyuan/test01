package com.enation.app.shop.core.service;

import java.util.List;



import com.enation.app.shop.core.model.BrandTag;
import com.enation.app.shop.core.model.BrandTagRel;
import com.enation.app.shop.core.model.Team;
import com.enation.app.shop.core.model.TeamProduct;
import com.enation.framework.database.Page;

/**
 * 推荐商品类管理
 * @author ljs
 * 2010-1-17下午01:03:41
 */
public interface ITeamManager {
	
	/**
	 * 检测推荐商品类名是否同名
	 * @param name 标签名
	 * @param teamid 要排除的推荐类id,编辑时判断重名所用
	 * @return 存在重名返回真，不存在返回假
	 */
	public boolean checkname(String name,Integer teamid);
	
	
	/**
	 * 检测某些标签是否已经关联商品
	 * @param tagids 标签id数组
	 * @return 有关联返回真，否则返回假
	 */
/*	public boolean checkJoinGoods(Integer[] tagids);
	*/
	/**
	 * 根据推荐类Id获取对象
	 * @param teamid 推荐类Id
	 * @return Tag
	 */
	public Team getById(Integer teamid);
	public BrandTag getByIdBrand(Integer teamid);
	/**
	 * 添加推荐类
	 * @param team 推荐类对象
	 */
	public void add(Team team);
	public void addBrand(BrandTag teamTag);
	/**
	 * 修改推荐类
	 * @param team 推荐类
	 */
	public void update(Team team);
	public void updatebrand(BrandTag team);
	/**
	 * 修改推荐商品
	 * @param teamProduct 推荐商品类
	 */
	public void update(TeamProduct product);
	public void updateBrand(BrandTagRel product);
	/**
	 * 删除标签
	 * @param teamids 推荐类Id数组,Integer[]
	 */
	public void delete(Integer[] teamids);
	public void deletebrand(Integer[] brand_id);
	/**
	 * @param id 推荐商品类Id
	 * @param goodsId  推荐的商品id
	 */
	public void delete(Integer id,Integer goodsId);
	public void deleteBrand(Integer id,Integer goodsId);
	/**
	 * 推荐类列表
	 * @param pageNo 分页
	 * @param pageSize 分页每页显示数量
	 * @return Page
	 */
	public Page list(int pageNo,int pageSize);
	public Page listBrand(int pageNo,int pageSize);
	/**
	 * 推荐类列表
	 * @return List<Tag>
	 */
	public List<Team> list(Integer market);
	
	
	/**
	 * 读取某个引用的推荐类id集合
	 * @param relid
	 * @return
	 */
	//public List<Integer> list(Integer productid); 
	
	
	
	
	/**
	 * 某个引用设置推荐类
	 * @param relid
	 * @param tagids
	 */
	public void saveRels(Integer productid,Integer[] teamids);
	
	/**
	 * 根据推荐类Id获取对象
	 * @param teamid 推荐类Id
	 * @return Tag
	 */
	public TeamProduct getByIdForeTeamProduct(Integer id);
	public BrandTagRel getByIdForeTeamBrand(Integer id);
	/**
	 * 推荐商品列表
	 * @return List<Tag>
	 */
	public List<TeamProduct> productList();
	/**
	 * 推荐商品列表for app
	 * 
	 */
	public List productListForApp();
	
}
