package com.enation.app.shop.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.GoodsMove;
import com.enation.app.shop.core.model.GoodsStores;
import com.enation.app.shop.core.model.MetalKeywordsLogs;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.SpecValue;
import com.enation.app.shop.core.model.Tag;
import com.enation.app.shop.core.model.support.GoodsEditDTO;
import com.enation.app.tradeease.core.model.sms.VistUserCount;
import com.enation.framework.database.Page;

/**
 * 商品管理接口
 * 
 * @author kingapex
 * 
 */
public interface IGoodsManager {

	public static final String plugin_type_berforeadd = "goods_add_berforeadd";
	public static final String plugin_type_afteradd = "goods_add_afteradd";

	/**
	 * 给商品添加翻译20150616
	 * 
	 * @param goods_id
	 */
	public void addTranslation(Integer goods_id, String goods_russion);
	public void addTranslation(Goods goods);
	/**
	 * 读取一个商品的详细
	 * 
	 * @param Goods_id
	 * @return Map
	 */
	public Map get(Integer goods_id);
	public List<Goods> getBrand(Integer brand_id);

	/**
	 * 根据商品ID获取商品
	 * 
	 * @param goods_id
	 *            商品Id
	 * @return Goods
	 */
	public Goods getGoods(Integer goods_id);
	public void addMetalKeywords(MetalKeywordsLogs metalKeywordsLogs);
	public void editMetalKeywords(Integer goods_id,String metal_keywords);
	public MetalKeywordsLogs showMetalKeywords(Integer goods_id);

	/**
	 * 修改时获取数据
	 * 
	 * @param goods_id
	 * @return
	 */
	public GoodsEditDTO getGoodsEditData(Integer goods_id);

	/**
	 * 添加商品
	 * 
	 * @param goods
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(Goods goods);

	/**
	 * 修改商品
	 * 
	 * @param goods
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(Goods goods);

	/**
	 * 商品搜索
	 * 
	 * @param goodsMap
	 *            搜索参数
	 * @param page
	 *            分页
	 * @param pageSize
	 *            分页每页数量
	 * @param other
	 *            其他
	 * @return Page
	 */
	public Page searchGoods(Map goodsMap, int page, int pageSize, String other,
			String sort, String order);

	/**
	 * 获取商品列表
	 * 
	 * @param goodsMap
	 *            搜索参数
	 * @return List
	 */
	public List searchGoods(Map goodsMap);

	/**
	 * 后台搜索商品
	 * 
	 * @param name
	 *            商品名称
	 * @param sn
	 *            商品编号
	 * @param order
	 *            排序
	 * @param page
	 *            分页
	 * @param pageSize
	 *            每页数量
	 * @return Page
	 */
	public Page searchBindGoods(String name, String sn, String order, int page,
			int pageSize);

	/**
	 * 读取商品回收站列表
	 * 
	 * @param name
	 *            商品名称
	 * @param sn
	 *            商品编号
	 * @param order
	 *            排序
	 * @param page
	 *            分页
	 * @param pageSize
	 *            每页数量
	 * @return Page
	 */
	public Page pageTrash(String name, String sn, String order, int page,
			int pageSize);

	/**
	 * 库存余量提醒分页列表
	 * 
	 * @param warnTotal
	 * @param page
	 *            分页
	 * @param pageSize
	 *            每页数量
	 * @return List<GoodsStores>
	 */
	public List<GoodsStores> storeWarnGoods(int warnTotal, int page,
			int pageSize);// 库存余量提醒分页列表

	/**
	 * 将商品加入回收站
	 * 
	 * @param ids
	 *            商品Id数组
	 */
	public void delete(Integer[] ids);
	/**
	 * 删除商品
	 * 
	 * @param ids
	 *            商品Id数组
	 */
	public void deleteStore(Integer goods_id,Integer audit_status,Integer market_enable);//接口参数

	/**
	 * 商品还原
	 * 
	 * @param ids
	 *            商品Id数组
	 */
	public void revert(Integer[] ids);

	/**
	 * 清除商品
	 * 
	 * @param ids
	 *            商品Id数组
	 */
	public void clean(Integer[] ids);

	/**
	 * 根据商品id数组读取商品列表
	 * 
	 * @param ids
	 * @return
	 */
	public List list(Integer[] ids);

	/**
	 * 按分类id列表商品
	 * 
	 * @param catid
	 * @return
	 */
	public List listByCat(Integer catid);

	/**
	 * 按标签id列表商品 如果tagid为空则列表全部
	 * 
	 * @param tagid
	 * @return
	 */
	public List listByTag(Integer[] tagid);

	/**
	 * 不分页、不分类别读取所有有效商品，包含捆绑商品
	 * 
	 * @return
	 */
	public List<Map> list();

	/**
	 * 批量编辑商品
	 */
	public void batchEdit();

	/**
	 * 商品信息统计
	 * 
	 * @return
	 */
	public Map census();

	/**
	 * 获取某个商品的位置信息
	 */
	public void getNavdata(Map goods);

	/**
	 * 更新某个商品的字段值
	 * 
	 * @param filedname
	 *            字段名称
	 * @param value
	 *            字段值
	 * @param goodsid
	 *            商品id
	 */
	public void updateField(String filedname, Object value, Integer goodsid);

	/**
	 * 获取某个商品的推荐组合
	 * 
	 * @param goods_id
	 * @param cat_id
	 * @param brand_id
	 * @param num
	 * @return
	 */
	public List getRecommentList(int goods_id, int cat_id, int brand_id, int num);

	/**
	 * 根据货物编号得到某个商品
	 * 
	 * @param goodSn
	 *            货物编号
	 * @return
	 */
	public Goods getGoodBySn(String goodSn);

	/**
	 * 修改商品访问次数
	 * 
	 * @param goods_id
	 */
	public void incViewCount(Integer goods_id);

	/**
	 * 商品列表
	 * 
	 * @param catid
	 *            分类Id
	 * @param tagid
	 *            标签Id
	 * @param goodsnum
	 *            数量
	 * @return List
	 */
	public List listGoods(String catid, String tagid, String goodsnum);

	/**
	 * 购买过商品的会员
	 * 
	 * @param goods_id
	 *            商品Id
	 * @param pageSize
	 *            显示数量
	 * @return List
	 */
	public List goodsBuyer(int goods_id, int pageSize);

	/**
	 * 给商品添加翻译、关键字、俄文内容20150616
	 * 
	 * @param goods_id
	 */
	public void addTranslation(Integer goodsId, String addTranslation,
			String china_keyword, String ru_keyword, String ru_content);

	/**
	 * @Description: 商品上下架时的编辑商品方法
	 * @param
	 * @return void
	 */
	public void myEditGood(Goods goods);
	
	public List <String> getGoodsId(Integer goods_translation,String store_market);

	public void updateVistCount(VistUserCount vistUserCount);
	public void addVisitUser(VistUserCount vistUserCount);
	/*
	 * public Page searchWds(Map goodsMap, int page, int pageSize, String other,
	 * String sort, String order);
	 * 
	 * Page searchAllWds(Map goodsMap, int page, int pageSize, String other,
	 * String sort, String order);
	 */
	public Page queryVistUser(int page, int pageSize, String visit_time);
	public VistUserCount queryvist(String vistdate, String message);
	/**
	 * 导出商品，获取对应的list
	 * @param goodsMap
	 * @return
	 */
	public List<Cart> getCartById(Integer id,String sessionid,Integer s);
	public List<StoreGoods> exportGoods(Map goodsMap);
	public  int selectProduct(Integer id);
	public StoreGoods getStoreGoods(Integer id);

	/**
	 * 存储抓取过来的数据
	 * @param goodsMove
	 */
	public void addGoodsMove(GoodsMove goodsMove);
	public Tag getTagByTagId(Integer id);
	public List<Cat> getCat_id(Integer is_belong); 
	//查看相似的商品
	public List<Goods> otherLookGoods(Integer catid);
	/**
	 * 根据goodsID查询product信息
	 * @param goodsId
	 * @return
	 */
	public List<Product> queryforproduct(Integer goodsId);
	/**
	 * 更新product库存
	 * @param product_id
	 */
	public void updateProductStore(Integer store,Integer enablestore,Integer goodsId,Integer product_id);
	/**
	 * 更新goods库存
	 * @param size
	 */
	public void updategoodsstore(Integer goodsId,int size); 
	public Integer getGoodsId(Map<String, Object> goodsMap);
	public void addDepot(Integer goods_id);
	public List<String> getCatNumber(Integer catid);
	public Integer getSpecId(String name);
	public Integer getSpecValue(String name);
	public Integer getSpecValueSize(String name);
	public Integer getSpecValueSize1(String name);
	public SpecValue getSpecValuePictureUrl(String name);
}