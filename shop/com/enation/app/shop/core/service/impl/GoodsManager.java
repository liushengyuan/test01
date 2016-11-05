package com.enation.app.shop.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.Depot;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.GoodsCatSpec;
import com.enation.app.shop.core.model.GoodsMove;
import com.enation.app.shop.core.model.GoodsStores;
import com.enation.app.shop.core.model.MetalKeywordsLogs;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.SpecValue;
import com.enation.app.shop.core.model.Specification;
import com.enation.app.shop.core.model.Tag;
import com.enation.app.shop.core.model.support.GoodsEditDTO;
import com.enation.app.shop.core.plugin.goods.GoodsDataFilterBundle;
import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
import com.enation.app.shop.core.service.IDepotMonitorManager;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IMemberPriceManager;
import com.enation.app.shop.core.service.IPackageProductManager;
import com.enation.app.shop.core.service.ITagManager;
import com.enation.app.shop.core.service.SnDuplicateException;
import com.enation.app.tradeease.core.model.sms.VistUserCount;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * Goods业务管理
 * 
 * @author kingapex 2010-1-13下午12:07:07
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GoodsManager extends BaseSupport implements IGoodsManager {
	private ITagManager tagManager;
	private GoodsPluginBundle goodsPluginBundle;
	private IPackageProductManager packageProductManager;
	private IGoodsCatManager goodsCatManager;
	private IMemberPriceManager memberPriceManager;
	private IMemberLvManager memberLvManager;
	private IDepotMonitorManager depotMonitorManager;
	private GoodsDataFilterBundle goodsDataFilterBundle;
	private IStoreGoodsManager storeGoodsManager;

	// private IDaoSupport<Goods> daoSupport;
	@Override
	public void addTranslation(Integer goodsId, String addTranslation,
			String china_keyword, String ru_keyword, String ru_content) {
		String sql = "update es_goods   set  goods_translation = 1 ,  "
				+ " name_ru ='" + addTranslation + "' ,meta_keywords = '"
				+ china_keyword + "' ,ru_keyword = '" + ru_keyword
				+ "' ,ru_content = '" + ru_content + "'  where goods_id ="
				+ goodsId;
		 ////System.out.println(sql+"*********************");
		this.baseDaoSupport.execute(sql);

	}
	
	

	/**
	 * 添加商品，同时激发各种事件
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(Goods goods) {
		try {
			StoreGoods goods2=(StoreGoods)goods;
			if(goods2.getWhprice()==null){
				goods2.setWhprice(0D);
			}
			if(goods2.getWhprice_ru()==null){
				goods2.setWhprice_ru(0D);
			}
			if(goods2.getWhprice_en()==null){
				goods2.setWhprice_en(0D);
			}
			if(goods2.getWholesale_volume()==null){
				goods2.setWholesale_volume(0);
			}
			Map goodsMap = po2Map(goods2);

			// 触发商品添加前事件
			goodsPluginBundle.onBeforeAdd(goodsMap);
			goodsMap.put("disabled", 0);
			goodsMap.put("create_time", DateUtil.getDateline());
			goodsMap.put("view_count", 0);
			goodsMap.put("buy_count", 0);
			goodsMap.put("last_modify", DateUtil.getDateline());

			HttpServletRequest httpRequest = ThreadContextHolder
					.getHttpRequest();
			Integer store = Integer
					.valueOf(httpRequest.getParameter("store") == null ? "0"
							: httpRequest.getParameter("store"));
			goodsMap.put("store", store);
			goodsMap.put("enable_store", store);
			/*
			 * if ("0".equals((String) goodsMap.get("have_spec"))) {
			 * //添加商品时，默认商品库存是0，但是这是在没有规格的情况下。 如有问题，请高手优先修改这里， whj-2015-05-21
			 * goodsMap.put("store", 0); }
			 */
			this.baseDaoSupport.insert("goods", goodsMap);
			Integer goods_id = this.baseDaoSupport.getLastId("goods");
			goods.setGoods_id(goods_id);
			goodsMap.put("goods_id", goods_id);
			goodsPluginBundle.onAfterAdd(goodsMap);
		} catch (RuntimeException e) {
			if (e instanceof SnDuplicateException) {
				throw e;
			}
			e.printStackTrace();
		}
	}

	/**
	 * 修改商品同时激发各种事件
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(Goods goods) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("开始保存商品数据...");
			}
			StoreGoods goods2=(StoreGoods)goods;
			if(goods2.getWhprice()==null){
				goods2.setWhprice(0D);
			}
			if(goods2.getWhprice_ru()==null){
				goods2.setWhprice_ru(0D);
			}
			if(goods2.getWhprice_en()==null){
				goods2.setWhprice_en(0D);
			}
			if(goods2.getWholesale_volume()==null){
				goods2.setWholesale_volume(0);
			}
			Map goodsMap = this.po2Map(goods2);
			this.goodsPluginBundle.onBeforeEdit(goodsMap);
			this.baseDaoSupport.update("goods", goodsMap,
					"goods_id=" + goods.getGoods_id());
			String sql = "select * from es_goods where goods_id=?";

			goodsMap = this.daoSupport.queryForMap(sql, goods.getGoods_id());

			this.goodsPluginBundle.onAfterEdit(goodsMap);
			if (logger.isDebugEnabled()) {
				logger.debug("保存商品数据完成.");
			}
		} catch (RuntimeException e) {
			if (e instanceof SnDuplicateException) {
				throw e;
			}
			e.printStackTrace();
		}
	}

	/**
	 * @Description: 商品上下架时修改商品数据 yanpeng
	 * @param
	 * @return void
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void myEditGood(Goods goods) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("开始保存商品数据...");
			}
			Map goodsMap = this.po2Map(goods);
			// goodsMap.put("have_spec", 0);
			// goodsMap.put("enable_store", 0);
			this.baseDaoSupport.update("goods", goodsMap,
					"goods_id=" + goods.getGoods_id());
			if (logger.isDebugEnabled()) {
				logger.debug("保存商品数据完成.");
			}
		} catch (RuntimeException e) {
			if (e instanceof SnDuplicateException) {
				throw e;
			}
			e.printStackTrace();
		}
	}

	/**
	 * 得到修改商品时的数据
	 * 
	 * @param goods_id
	 * @return
	 */

	public GoodsEditDTO getGoodsEditData(Integer goods_id) {
		GoodsEditDTO editDTO = new GoodsEditDTO();
		String sql = "select * from goods where goods_id=?";
		Map goods = this.baseDaoSupport.queryForMap(sql, goods_id);

		String intro = (String) goods.get("intro");
		if (intro != null) {
			intro = UploadUtil.replacePath(intro);
			goods.put("intro", intro);
		}

		Map<Integer, String> htmlMap = goodsPluginBundle
				.onFillEditInputData(goods);

		editDTO.setGoods(goods);
		editDTO.setHtmlMap(htmlMap);

		return editDTO;
	}
	public List<Goods> getBrand(Integer brand_id){
		String sql = "select g.* from es_goods g where g.brand_id=? and g.market_enable=1 and g.audit_status=1 and g.disabled=0 limit 0,5";
		List<Goods> goods = this.daoSupport.queryForList(sql,Goods.class,brand_id);

		return goods;
	}
	/**
	 * 读取一个商品的详细<br/>
	 * 处理由库中读取的默认图片和所有图片路径:<br>
	 * 如果是以本地文件形式存储，则将前缀替换为静态资源服务器地址。
	 */

	public Map get(Integer goods_id) {
		String sql = "select g.*,b.name as brand_name,c.name as cat_name from "
				+ this.getTableName("goods") + " g left join "
				+ this.getTableName("brand") + " b on g.brand_id=b.brand_id " 
				+ "left join es_goods_cat c on c.cat_id=g.cat_id";
		sql += "  where goods_id=?";

		Map goods = this.daoSupport.queryForMap(sql, goods_id);

		/**
		 * ====================== 对商品图片的处理 ======================
		 */

		String small = (String) goods.get("small");
		if (small != null) {
			small = UploadUtil.replacePath(small);
			goods.put("small", small);
		}
		String big = (String) goods.get("big");
		if (big != null) {
			big = UploadUtil.replacePath(big);
			goods.put("big", big);
		}

		return goods;
	}

	public void getNavdata(Map goods) {
		// lzf 2011-08-29 add,lzy modified 2011-10-04
		int catid = (Integer) goods.get("cat_id");
		List list = goodsCatManager.getNavpath(catid);
		goods.put("navdata", list);
		// lzf add end
	}

	private String getListSql(int disabled) {
		String selectSql = this.goodsPluginBundle.onGetSelector();
		String fromSql = this.goodsPluginBundle.onGetFrom();

		String sql = "select s.store_initiallist,g.sn,g.name,g.is_belong,g.store_name,g.price,g.is_discount,g.audit_status,g.audit_discribe,g.goods_id,g.market_enable,g.enable_store,g.create_time,g.goods_translation,g.last_modify,g.audit_discribe,b.name as brand_name ,t.name as type_name,c.name as cat_name, d.store_market "
				+ selectSql
				+ " from "
				+ this.getTableName("goods")
				+ " g left join "
				+ this.getTableName("goods_cat")
				+ " c on g.cat_id=c.cat_id left join "
				+ this.getTableName("brand")
				+ " b on g.brand_id = b.brand_id and b.disabled=0 left join "
				+ this.getTableName("goods_type")
				+ " t on g.type_id =t.type_id left join "
				+ this.getTableName("es_store")
				+ " d on g.store_id=d.store_id "
				+ fromSql
				+ " where g.goods_type = 'normal' and g.disabled=" + disabled;

		return sql;
	}

	/**
	 * 取得捆绑商品列表
	 * 
	 * @param disabled
	 * @return
	 */
	private String getBindListSql(int disabled) {
		String sql = "select g.*,b.name as brand_name ,t.name as type_name,c.name as cat_name from "
				+ this.getTableName("goods")
				+ " g left join "
				+ this.getTableName("goods_cat")
				+ " c on g.cat_id=c.cat_id left join "
				+ this.getTableName("brand")
				+ " b on g.brand_id = b.brand_id left join "
				+ this.getTableName("goods_type")
				+ " t on g.type_id =t.type_id"
				+ " where g.goods_type = 'bind' and g.disabled=" + disabled;
		return sql;
	}

	/**
	 * 后台搜索商品
	 * 
	 * @param params
	 *            通过map的方式传递搜索参数
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page searchBindGoods(String name, String sn, String order, int page,
			int pageSize) {

		String sql = getBindListSql(0);

		if (order == null) {
			order = "goods_id desc";
		}

		if (name != null && !name.equals("")) {
			sql += "  and g.name like '%" + name + "%'";
		}

		if (sn != null && !sn.equals("")) {
			sql += "   and g.sn = '" + sn + "'";
		}

		sql += " order by g." + order;
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);

		List<Map> list = (List<Map>) (webpage.getResult());

		for (Map map : list) {
			List productList = packageProductManager.list(Integer.valueOf(map
					.get("goods_id").toString()));
			productList = productList == null ? new ArrayList() : productList;
			map.put("productList", productList);
		}

		return webpage;
	}

	/**
	 * 读取商品回收站列表
	 * 
	 * @param name
	 * @param sn
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page pageTrash(String name, String sn, String order, int page,
			int pageSize) {

		String sql = getListSql(1);
		if (order == null) {
			order = "goods_id desc";
		}

		if (name != null && !name.equals("")) {
			sql += "  and g.name like '%" + name + "%'";
		}

		if (sn != null && !sn.equals("")) {
			sql += "   and g.sn = '" + sn + "'";
		}

		sql += " order by g." + order;

		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);

		return webpage;
	}

	/***
	 * 库存余量提醒分页列表
	 * 
	 * @param warnTotal
	 *            总报警数
	 * @param page
	 * @param pageSize
	 */
	public List<GoodsStores> storeWarnGoods(int warnTotal, int page,
			int pageSize) {
		// String sql =
		// " where g.market_enable = 1 and g.goods_type = 'normal' and g.disabled= 0 order by g.goods_id desc ";
		String select_sql = "select gc.name as gc_name,b.name as b_name,g.cat_id,g.goods_id,g.name,g.sn,g.price,g.last_modify,g.market_enable,s.sumstore ";
		String left_sql = " left join " + this.getTableName("goods")
				+ " g  on s.goodsid = g.goods_id  left join "
				+ this.getTableName("goods_cat")
				+ " gc on gc.cat_id = g.cat_id left join "
				+ this.getTableName("brand") + " b on b.brand_id = g.brand_id ";
		List<GoodsStores> list = new ArrayList<GoodsStores>();

		String sql_2 = select_sql
				+ " from  (select ss.* from (select goodsid,productid,sum(store) sumstore from "
				+ this.getTableName("product_store")
				+ "  group by goodsid,productid   ) ss "
				+ "  left join "
				+ this.getTableName("warn_num")
				+ " wn on wn.goods_id = ss.goodsid  where ss.sumstore <=  (case when (wn.warn_num is not null or wn.warn_num <> 0) then wn.warn_num else ?  end )  ) s  "
				+ left_sql;
		List<GoodsStores> list_2 = this.daoSupport.queryForList(sql_2,
				new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						GoodsStores gs = new GoodsStores();
						gs.setGoods_id(rs.getInt("goods_id"));
						gs.setName(rs.getString("name"));
						gs.setSn(rs.getString("sn"));
						gs.setRealstore(rs.getInt("sumstore"));
						gs.setPrice(rs.getDouble("price"));
						gs.setLast_modify(rs.getLong("last_modify"));
						gs.setBrandname(rs.getString("b_name"));
						gs.setCatname(rs.getString("gc_name"));
						gs.setMarket_enable(rs.getInt("market_enable"));
						gs.setCat_id(rs.getInt("cat_id"));
						return gs;
					}
				}, warnTotal);
		list.addAll(list_2);// 普通商品

		return list;
	}

	/**
	 * 批量将商品放入回收站
	 * 
	 * @param ids
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer[] ids) {
		if (ids == null)
			return;

		for (Integer id : ids) {
			this.tagManager.saveRels(id, null);
		}
		String id_str = StringUtil.arrayToString(ids, ",");
		String sql = "update  goods set disabled=1  where goods_id in ("
				+ id_str + ")";

		this.baseDaoSupport.execute(sql);
	}

	/**
	 * 还原
	 * 
	 * @param ids
	 */
	public void revert(Integer[] ids) {
		if (ids == null)
			return;
		String id_str = StringUtil.arrayToString(ids, ",");
		String sql = "update  goods set disabled=0  where goods_id in ("
				+ id_str + ")";
		this.baseDaoSupport.execute(sql);
	}

	/**
	 * 清除
	 * 
	 * @param ids
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void clean(Integer[] ids) {
		if (ids == null)
			return;
		for (Integer id : ids) {
			this.tagManager.saveRels(id, null);
		}
		this.goodsPluginBundle.onGoodsDelete(ids);
		String id_str = StringUtil.arrayToString(ids, ",");
		String sql = "delete  from goods  where goods_id in (" + id_str + ")";
		this.baseDaoSupport.execute(sql);
	}

	public List list(Integer[] ids) {
		if (ids == null || ids.length == 0)
			return new ArrayList();
		String idstr = StringUtil.arrayToString(ids, ",");
		String sql = "select * from goods where goods_id in(" + idstr + ")";
		return this.baseDaoSupport.queryForList(sql);
	}

	public GoodsPluginBundle getGoodsPluginBundle() {
		return goodsPluginBundle;
	}

	public void setGoodsPluginBundle(GoodsPluginBundle goodsPluginBundle) {
		this.goodsPluginBundle = goodsPluginBundle;
	}

	/**
	 * 将po对象中有属性和值转换成map
	 * 
	 * @param po
	 * @return
	 */
	protected Map po2Map(Object po) {
		Map poMap = new HashMap();
		Map map = new HashMap();
		try {
			map = BeanUtils.describe(po);
		} catch (Exception ex) {
		}
		Object[] keyArray = map.keySet().toArray();
		for (int i = 0; i < keyArray.length; i++) {
			String str = keyArray[i].toString();
			if (str != null && !str.equals("class")) {
				if (map.get(str) != null) {
					poMap.put(str, map.get(str));
				}
			}
		}
		return poMap;
	}

	public IPackageProductManager getPackageProductManager() {
		return packageProductManager;
	}

	public void setPackageProductManager(
			IPackageProductManager packageProductManager) {
		this.packageProductManager = packageProductManager;
	}

	public Goods getGoods(Integer goods_id) {
		Goods goods = (Goods) this.baseDaoSupport.queryForObject(
				"select * from es_goods where goods_id=?", Goods.class,
				goods_id);
		return goods;
	}
	
	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}

	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void batchEdit() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String[] ids = request.getParameterValues("goodsidArray");
		String[] names = request.getParameterValues("name");
		String[] prices = request.getParameterValues("price");
		String[] cats = request.getParameterValues("catidArray");
		String[] market_enable = request.getParameterValues("market_enables");
		String[] store = request.getParameterValues("store");
		String[] sord = request.getParameterValues("sord");

		String sql = "";

		for (int i = 0; i < ids.length; i++) {
			sql = "";
			if (names != null && names.length > 0) {
				if (!StringUtil.isEmpty(names[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " name='" + names[i] + "'";
				}
			}

			if (prices != null && prices.length > 0) {
				if (!StringUtil.isEmpty(prices[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " price=" + prices[i];
				}
			}
			if (cats != null && cats.length > 0) {
				if (!StringUtil.isEmpty(cats[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " cat_id=" + cats[i];
				}
			}
			if (store != null && store.length > 0) {
				if (!StringUtil.isEmpty(store[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " store=" + store[i];
				}
			}
			if (market_enable != null && market_enable.length > 0) {
				if (!StringUtil.isEmpty(market_enable[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " market_enable=" + market_enable[i];
				}
			}
			if (sord != null && sord.length > 0) {
				if (!StringUtil.isEmpty(sord[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " sord=" + sord[i];
				}
			}
			sql = "update  goods set " + sql + " where goods_id=?";
			this.baseDaoSupport.execute(sql, ids[i]);

		}
	}

	public Map census() {
		// 计算上架商品总数
		String sql = "select count(0) from goods ";
		int allcount = this.baseDaoSupport.queryForInt(sql);

		// 计算上架商品总数
		sql = "select count(0) from goods where market_enable=1 and  disabled = 0";
		int salecount = this.baseDaoSupport.queryForInt(sql);

		// 计算下架商品总数
		sql = "select count(0) from goods where market_enable=0 and  disabled = 0";
		int unsalecount = this.baseDaoSupport.queryForInt(sql);

		// 计算回收站总数
		sql = "select count(0) from goods where   disabled = 1";
		int disabledcount = this.baseDaoSupport.queryForInt(sql);

		// 读取商品评论数
		sql = "select count(0) from comments where   for_comment_id is null  and commenttype='goods' and object_type='discuss'";
		int discusscount = this.baseDaoSupport.queryForInt(sql);

		// 读取商品评论数
		sql = "select count(0) from comments where for_comment_id is null  and  commenttype='goods' and object_type='ask'";
		int askcount = this.baseDaoSupport.queryForInt(sql);

		Map<String, Integer> map = new HashMap<String, Integer>(2);
		map.put("salecount", salecount);
		map.put("unsalecount", unsalecount);
		map.put("disabledcount", disabledcount);
		map.put("allcount", allcount);
		map.put("discuss", discusscount);
		map.put("ask", askcount);
		return map;
	}

	/**
	 * 获取某个分类的推荐商品
	 */
	public List getRecommentList(int goods_id, int cat_id, int brand_id, int num) {
		// 原美睛网代码，去掉
		return null;
	}

	public List list() {
		String sql = "select * from goods where disabled = 0";
		return this.baseDaoSupport.queryForList(sql);
	}

	public IMemberPriceManager getMemberPriceManager() {
		return memberPriceManager;
	}

	public ITagManager getTagManager() {
		return tagManager;
	}

	public void setTagManager(ITagManager tagManager) {
		this.tagManager = tagManager;
	}

	public void setMemberPriceManager(IMemberPriceManager memberPriceManager) {
		this.memberPriceManager = memberPriceManager;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	@Override
	public void updateField(String filedname, Object value, Integer goodsid) {
		this.baseDaoSupport.execute("update goods set " + filedname
				+ "=? where goods_id=?", value, goodsid);
	}

	@Override
	public Goods getGoodBySn(String goodSn) {
		Goods goods = (Goods) this.baseDaoSupport.queryForObject(
				"select * from goods where sn=?", Goods.class, goodSn);
		return goods;
	}

	public IDepotMonitorManager getDepotMonitorManager() {
		return depotMonitorManager;
	}

	public void setDepotMonitorManager(IDepotMonitorManager depotMonitorManager) {
		this.depotMonitorManager = depotMonitorManager;
	}

	@Override
	public List listByCat(Integer catid) {
		String sql = getListSql(0);
		if (catid.intValue() != 0) {
			Cat cat = this.goodsCatManager.getById(catid);
			sql += " and  g.cat_id in(";
			sql += "select c.cat_id from " + this.getTableName("goods_cat")
					+ " c where c.cat_path like '" + cat.getCat_path()
					+ "%')  ";
		}
		return this.daoSupport.queryForList(sql);
	}

	@Override
	public List listByTag(Integer[] tagid) {
		String sql = getListSql(0);
		if (tagid != null && tagid.length > 0) {
			String tagidstr = StringUtil.arrayToString(tagid, ",");
			sql += " and g.goods_id in(select rel_id from "
					+ this.getTableName("tag_rel") + " where tag_id in("
					+ tagidstr + "))";
		}
		return this.daoSupport.queryForList(sql);
	}

	@Override
	public void incViewCount(Integer goods_id) {
		this.baseDaoSupport
				.execute(
						"update goods set view_count = view_count + 1 where goods_id = ?",
						goods_id);
	}

	public List listGoods(String catid, String tagid, String goodsnum) {
		int num = 10;
		if (!StringUtil.isEmpty(goodsnum)) {
			num = Integer.valueOf(goodsnum);
		}

		StringBuffer sql = new StringBuffer();
		sql.append("select g.* from "
				+ this.getTableName("tag_rel")
				+ " r LEFT JOIN "
				+ this.getTableName("goods")
				+ " g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");

		if (!StringUtil.isEmpty(catid)) {
			Cat cat = this.goodsCatManager.getById(Integer.valueOf(catid));
			if (cat != null) {
				String cat_path = cat.getCat_path();
				if (cat_path != null) {
					sql.append(" and  g.cat_id in(");
					sql.append("select c.cat_id from "
							+ this.getTableName("goods_cat") + " ");
					sql.append(" c where c.cat_path like '" + cat_path + "%')");
				}
			}
		}

		if (!StringUtil.isEmpty(tagid)) {
			sql.append(" AND r.tag_id=" + tagid + "");
		}

		sql.append(" order by r.ordernum desc");
		// //System.out.println(sql.toString());
		List list = this.daoSupport.queryForListPage(sql.toString(), 1, num);
		this.goodsDataFilterBundle.filterGoodsData(list);
		return list;
	}

	public GoodsDataFilterBundle getGoodsDataFilterBundle() {
		return goodsDataFilterBundle;
	}

	public void setGoodsDataFilterBundle(
			GoodsDataFilterBundle goodsDataFilterBundle) {
		this.goodsDataFilterBundle = goodsDataFilterBundle;
	}

	@Override
	public List goodsBuyer(int goods_id, int pageSize) {
		String sql = "select distinct m.* from es_order o left join es_member m "
				+ "on o.member_id=m.member_id where order_id in (select order_id from es_order_items "
				+ "where goods_id=?)";
		Page page = this.daoSupport.queryForPage(sql, 1, pageSize, goods_id);

		return (List) page.getResult();
	}

	@Override
	public Page searchGoods(Map goodsMap, int page, int pageSize, String other,
			String sort, String order) {
		String sql = creatTempSql(goodsMap, other);
		StringBuffer _sql = new StringBuffer(sql);
		this.goodsPluginBundle.onSearchFilter(_sql);
		_sql.append(" order by " + sort + " " + order);
		Page webpage = this.daoSupport.queryForPage(_sql.toString(), page,
				pageSize);
		return webpage;
	}

	@Override
	public List searchGoods(Map goodsMap) {
		String sql = creatTempSql(goodsMap, null);
		return this.daoSupport.queryForList(sql, Goods.class);
	}

	private String creatTempSql(Map goodsMap, String other) {

		other = other == null ? "" : other;
		String sql = getListSql(0);
		Integer brandid = (Integer) goodsMap.get("brandid");
		Integer catid = (Integer) goodsMap.get("catid");
		String name = (String) goodsMap.get("name");
		String sn = (String) goodsMap.get("sn");
		Integer[] tagid = (Integer[]) goodsMap.get("tagid");
		Integer stype = (Integer) goodsMap.get("stype");
		String keyword = (String) goodsMap.get("keyword");
		String order = (String) goodsMap.get("order");
		String storeName = (String) goodsMap.get("storeName");
		Integer auditStatus = (Integer) goodsMap.get("auditStatus");
		Integer searchTranslation = (Integer) goodsMap.get("searchTranslation");
		Integer market_enable=(Integer)goodsMap.get("market_enable");
		String store_market = (String) goodsMap.get("store_market");
		Integer is_belong=(Integer)goodsMap.get("storeStatus");
		Integer store_initiallist =(Integer) goodsMap.get("store_initiallist");
		String c="";
		if(goodsMap.get("c")==null){
			 c =(String)goodsMap.get("c");
		}else{
			c =goodsMap.get("c").toString();
		}
		if(store_market != null && store_market != ""){
			sql += " and d.store_market = '"+store_market+ "' ";
		}
		if (brandid != null && brandid != 0) {
			sql += " and g.brand_id = " + brandid + " ";
		}
		if (is_belong != null) {
			sql += " and g.is_belong = " + is_belong + " ";
		}
		if ("1".equals(other)) {
			// 商品属性为不支持打折的商品
			sql += " and g.no_discount=1";
		}
		if ("2".equals(other)) {
			// 特殊打折商品，即单独设置了会员价的商品
			sql += " and (select count(0) from "
					+ this.getTableName("goods_lv_price")
					+ " glp where glp.goodsid=g.goods_id) >0";
		}
		
		if(store_initiallist!=null) {
			if(store_initiallist!=0){
				sql+=" AND s.store_initiallist = '" + store_initiallist + "' ";
			}
			
		}
		if (stype != null && keyword != null) {
			if (stype == 0) {
				sql += " and ( g.name like '%" + keyword + "%'";
				sql += " or g.sn like '%" + keyword + "%'";
				if(keyword.contains("中国")){
					keyword="CHN";
				}else if(keyword.contains("俄罗斯")){
					keyword="RUS";
				}
				sql += " or d.store_market like '%" + keyword + "%')";
			}
		}
		if (stype != null && storeName != null) {
			if (stype == 1) {
				sql += " and s.store_name like '%" + storeName + "%'";
			}
		}

		if (stype != null && auditStatus != null) {
			if (stype ==1 ){
				if(auditStatus==0){
					sql += " and g.audit_status = " + auditStatus + " and g.market_enable!=-1";
				}else{
					if(auditStatus==1){
						sql += " and g.audit_status in (" + auditStatus+",5) ";//查询通过的时候检索下架且删除的商品信息
					}else if(auditStatus==2){
						sql += " and g.audit_status in (" + auditStatus+",4) ";//查询未通过的时候检索未通过且删除的商品信息
					}else{
						sql += " and g.audit_status = " + auditStatus;
					}
				}
			}
		}
		if (stype != null && storeName != null) {
			if (stype == 0) {
				sql += " and s.store_name like '%" + storeName + "%'";
			}
		}

		if (stype != null && auditStatus != null) {
			if (stype == 0) {
				if(auditStatus==0){
					sql += " and g.audit_status = " + auditStatus + " and g.market_enable!=-1";
				}else{
					if(auditStatus==1){
						sql += " and g.audit_status in (" + auditStatus+",5) ";//查询通过的时候检索下架且删除的商品信息
					}else if(auditStatus==2){
						sql += " and g.audit_status in (" + auditStatus+",4) ";//查询未通过的时候检索未通过且删除的商品信息
					}else{
						sql += " and g.audit_status = " + auditStatus;
					}
				}
			}
		}

		if (name != null && !name.equals("")) {
			name = name.trim();
			String[] keys = name.split("\\s");
			for (String key : keys) {
				sql += (" and g.name like '%");
				sql += (key);
				sql += ("%'");
			}
		}

		if (sn != null && !sn.equals("")) {
			sql += "   and g.sn like '%" + sn + "%'";
		}

		if (catid != null && catid != 0) {
			Cat cat = this.goodsCatManager.getById(catid);
			sql += " and  g.cat_id in(";
			sql += "select c.cat_id from " + this.getTableName("goods_cat")
					+ " c where c.cat_path like '" + cat.getCat_path()
					+ "%')  ";
		}

		if (tagid != null && tagid.length > 0) {
			String tagidstr = StringUtil.arrayToString(tagid, ",");
			sql += " and g.goods_id in(select rel_id from "
					+ this.getTableName("tag_rel") + " where tag_id in("
					+ tagidstr + "))";
		}
		
		// //System.out.println(sql);
		// 查找是否翻译过
		if (searchTranslation != null && !searchTranslation.equals("")) {
			sql += " and g.goods_translation = " + searchTranslation;
		}
		//是否上架
		if(market_enable!=null){
			sql += " and g.market_enable = " + market_enable;
		}
		////System.out.println(sql);
		if (!StringUtil.isEmpty(c)) {
			if(Integer.parseInt(c)==0){
				sql+=(" and g.goods_id in(select g.goods_id from es_goods g WHERE g.enable_store=(SELECT SUM(p.enable_store) from es_product p WHERE p.goods_id=g.goods_id))" );
		     }else if(Integer.parseInt(c)==1){
		    	 sql+=(" and g.goods_id in(select g.goods_id from es_goods g WHERE g.enable_store!=(SELECT SUM(p.enable_store) from es_product p WHERE p.goods_id=g.goods_id))" );
		     }else{
		     }
			}
		return sql;
	}

	@Override
	public void addTranslation(Integer goods_id, String goods_russion) {
		// TODO Auto-generated method stub
		// 此方法作废
	}

	@Override
	public void addTranslation(Goods goods) {
		// TODO Auto-generated method stub
		this.baseDaoSupport.update("es_goods", goods, " goods_id="+goods.getGoods_id());
	}
	/**
	 * 查询访问信息
	 */
	@Override
	public VistUserCount queryvist(String vistdate, String message) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT * from es_visituser where visit_time like "+ "'"+vistdate.substring(0, 10)+"%'");
		sql.append(" order by visit_id desc ");
		//List<VistUserCount>vislist=this.baseDaoSupport.queryForList(sql.toString());
		List<VistUserCount>vislist=this.baseDaoSupport.queryForList(sql.toString(), VistUserCount.class);
		//VistUserCount visitList = (VistUserCount) this.baseDaoSupport.queryForObject(sql.toString(), VistUserCount.class);
		if(vislist!=null && vislist.size()>0){
			VistUserCount vis  = vislist.get(0);
			return vis;
		}else{
			return null;
		}
		
	}
	/**
	 * 如果是一天的，就更新数量
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateVistCount(VistUserCount vistUserCount) {
		this.baseDaoSupport.update("es_visituser", vistUserCount,"visit_id="+vistUserCount.getVisit_id());	
	}

	/**
	 * 根据goods_translation和store_market查询goods_id
	 */
	public List<String> getGoodsId(Integer goods_translation,String store_market) {
		List <String> list = this.baseDaoSupport.queryForList(
				"SELECT goods_id FROM es_goods,es_store WHERE es_goods.store_id=es_store.store_id AND goods_translation='"+goods_translation+"' AND store_market='"+store_market+"'");
		return list;
	}

	/**
	 * 添加访问信息
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addVisitUser(VistUserCount vistUserCount) {
		this.baseDaoSupport.insert("es_visituser", vistUserCount);
		
	}
	/**
	 * 查询访问信息
	 */
	@Override
	public Page queryVistUser(int page, int pageSize, String visit_time) {
		StringBuffer sql = new StringBuffer();
		if(visit_time!=null){
			sql.append(" SELECT * from es_visituser where visit_time like "+ "'"+visit_time+"%'");
		}else{
			sql.append(" SELECT * from es_visituser");
		}
		sql.append(" order by visit_id desc ");
		
		Page visitpage = this.daoSupport.queryForPage(sql.toString(), page, pageSize);
		return visitpage;
	}

	public List<Cart> getCartById(Integer id,String sessionid,Integer s){
		String sql="select * from es_cart where goods_id=? and session_id=? and store_id=? and is_select=1 ";
		return this.baseDaoSupport.queryForList(sql, Cart.class, id,sessionid,s);
	}

	@Override
	public List<StoreGoods> exportGoods(Map goodsMap) {
		// TODO Auto-generated method stub
		String sql = creatTempSql(goodsMap, null);
		StringBuffer _sql = new StringBuffer(sql);
		//this.goodsPluginBundle.onSearchFilter(_sql);
		_sql.append(" order by " + "" + " " + "g.goods_id");
		////System.out.println(_sql.toString());
		return this.baseDaoSupport.queryForList(_sql.toString(), StoreGoods.class);
	}



	@Override
	public void deleteStore(Integer goods_id,Integer audit_status,Integer market_enable) {
		if(goods_id!=null){ 
			String sql="delete from es_product where goods_id=?";//直接删除es_product表中商品信息
			String sql1="delete from es_goods where goods_id=?"; //直接删除es_goods表中商品信息
			String sql2="update es_goods set audit_status=4,market_enable=4 where goods_id=?";//隐藏es_goods表中商品信息
			String sql3="update es_goods set audit_status=5,market_enable=5 where goods_id=?";//隐藏es_goods表中商品信息
			int num=this.baseDaoSupport.queryForList("select * from es_product where goods_id=?", goods_id).size();
			if(num!=0){
				//草稿直接删除
				if(audit_status==0 && market_enable==-1){
					this.baseDaoSupport.execute(sql, goods_id); //删除es_product表中商品信息
					this.baseDaoSupport.execute(sql1, goods_id);//删除es_goods表中商品信息
				}
				//未通过隐藏
				if(audit_status==2 && market_enable==-1){
					this.baseDaoSupport.execute(sql2, goods_id);//隐藏es_goods表中商品信息
				}
				//已下架
				if(audit_status==1 && market_enable==0){
					this.baseDaoSupport.execute(sql3, goods_id);//隐藏es_goods表中商品信息
				}
			}
			//this.baseDaoSupport.execute(sql1,id);
		}
	}



	@Override
	public int selectProduct(Integer id){
		String sql="select * from es_order_items where goods_id=?";
		int num=this.baseDaoSupport.queryForList(sql, id).size();
		return num;
	}
    public StoreGoods getStoreGoods(Integer id){
	    StoreGoods goods=(StoreGoods) this.baseDaoSupport.queryForObject("select * from es_goods where goods_id=?", StoreGoods.class, id);
	    return goods;
	    }
    
    /**
     * 存储抓取过来的数据
     * @param goodsMove
     */
    public void addGoodsMove(GoodsMove goodsMove){
    	this.baseDaoSupport.insert("es_goods_move", goodsMove);
    }



	@Override
	public Tag getTagByTagId(Integer id) {
		String sql="select * from es_tags where tag_id=?";
	   List<Tag> list=this.baseDaoSupport.queryForList(sql, Tag.class, id);
	   Tag tag=null;
	   if(list.size()>0){
		   tag=list.get(0);
	   }else{
		   tag=new Tag();
	   }
		return tag;
	}
	@Override
	public List<Cat> getCat_id(Integer is_belong) {
		String sql = "select DISTINCT g.cat_id as cat_id,c.cat_path from es_goods g left join es_goods_cat c on g.cat_id=c.cat_id and g.market_enable=1 and g.audit_status=1 where g.is_belong=?";
		return this.baseDaoSupport.queryForList(sql,Cat.class, is_belong);
	}
   
   
	//查看相似的商品
	@Override
	public List<Goods> otherLookGoods(Integer catid) {
		String sql="select * from es_goods where cat_id like '%"+catid+"%' AND  audit_status=1  AND market_enable=1 ORDER BY view_count DESC LIMIT 6";
		List<Goods> list=this.baseDaoSupport.queryForList(sql, Goods.class);
		return list;
	}

	/**
	 * 根据goodsId查询product
	 */
	@Override
	public List<Product> queryforproduct(Integer goodsId) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from es_product where goods_id=? ");
		List<Product> plist= this.baseDaoSupport.queryForList(sql.toString(), Product.class, goodsId);
		return plist;
	}

	/**
	 * 更新库存
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateProductStore(Integer store,Integer enablestore,Integer goodsId,Integer product_id) {
		this.daoSupport.execute("update es_product set store=?,enable_store=? where product_id=?", 1,1, product_id);
		List storelist = this.storeGoodsManager.getstoregoods(goodsId, product_id);
		if(storelist!=null && !storelist.isEmpty()){
			this.daoSupport.execute("update es_product_store set store=?,enable_store=? where goodsid=? and productid=? ", 1,1, goodsId,product_id);
		}else{
			this.daoSupport.execute("insert into es_product_store(goodsid,productid,depotid,store,enable_store)values(?,?,?,?,?)", goodsId, product_id, 1, 1,1);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updategoodsstore(Integer goodsid,int size) {
		this.daoSupport.execute("update es_goods set store=?,enable_store=? where goods_id=?", size,size, goodsid);
		
	}

	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}



	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}



	@Override
	public Integer getGoodsId(Map<String, Object> goodsMap) {
		this.baseDaoSupport.insert("goods", goodsMap);
		Integer goods_id = this.baseDaoSupport.getLastId("goods");
		return goods_id;
	}
	@Override
	public void addDepot(Integer goods_id) {
		List<Depot> depotList =this.baseDaoSupport.queryForList("select * from depot", Depot.class);
		for(Depot depot:depotList){
			this.baseDaoSupport.execute("insert into goods_depot(goodsid,depotid,iscmpl)values(?,?,?)", goods_id,depot.getId(),0);
		}
	}



	@Override
	public List<String> getCatNumber(Integer catid) {
		List<String> list=new ArrayList<String>();
		String sql="select spec_id from es_goods_type_spec where cat_id=?";
		List<GoodsCatSpec> catSpecList  =this.daoSupport.queryForList(sql, GoodsCatSpec.class, catid);
		if(catSpecList.size()>0){
			for (GoodsCatSpec goodsCatSpec : catSpecList) {
				String sql1="select spec_name from es_specification where spec_id=?";
				Specification specification=(Specification) this.baseDaoSupport.queryForObject(sql1, Specification.class, goodsCatSpec.getSpec_id());
				list.add(specification.getSpec_name());
			}
		}
		return list;
	}



	@Override
	public Integer getSpecId(String name) {
		String sql="select spec_id from es_specification where spec_name=?";
		Specification specification=(Specification) this.baseDaoSupport.queryForObject(sql, Specification.class,name);
		return specification.getSpec_id();
	}



	@Override
	public Integer getSpecValue(String name) {
		Integer spec_value_id=null;
		String sql="select * from es_spec_values where spec_id=?";
		Integer spec_id=this.getSpecId("颜色");
		List<SpecValue> list=this.baseDaoSupport.queryForList(sql, SpecValue.class, spec_id);
		if(list.size()>0){
			for (SpecValue specValue : list) {
				if(specValue.getSpec_value().equalsIgnoreCase(name.trim())){
					spec_value_id=specValue.getSpec_value_id();
				}
			}
		}
		return spec_value_id;
	}



	@Override
	public Integer getSpecValueSize(String name) {
		Integer spec_value_id=null;
		String sql="select * from es_spec_values where spec_id=?";
		Integer spec_id=this.getSpecId("男女鞋尺寸");
		List<SpecValue> list=this.baseDaoSupport.queryForList(sql, SpecValue.class, spec_id);
		if(list.size()>0){
			for (SpecValue specValue : list) {
				if(specValue.getSpec_value().equalsIgnoreCase(name.trim())){
					spec_value_id=specValue.getSpec_value_id();
				}
			}
		}
		return spec_value_id;
	}



	@Override
	public Integer getSpecValueSize1(String name) {
		Integer spec_value_id=null;
		String sql="select * from es_spec_values where spec_id=?";
		Integer spec_id=this.getSpecId("衣服尺寸");
		List<SpecValue> list=this.baseDaoSupport.queryForList(sql, SpecValue.class, spec_id);
		if(list.size()>0){
			for (SpecValue specValue : list) {
				if(specValue.getSpec_value().equalsIgnoreCase(name.trim())){
					spec_value_id=specValue.getSpec_value_id();
				}
			}
		}
		return spec_value_id;
	}



	@Override
	public SpecValue getSpecValuePictureUrl(String name) {
		String sql="select * from es_spec_values WHERE spec_value=?";
		SpecValue specValue=null;
		List<SpecValue> list=this.baseDaoSupport.queryForList(sql, SpecValue.class, name);
		if(list.size()>0){
			specValue=list.get(0);
		}else{
			specValue=new SpecValue();
		}
		return specValue;
	}



	@Override
	public void addMetalKeywords(MetalKeywordsLogs metalKeywordsLogs) {
		String sql="select * from es_metal_keywords_logs WHERE goods_id=?";
		String sql1="update es_metal_keywords_logs set admin=?,original_keywords_ru=?,update_keywords_ru=?,nowtime=? where goods_id=?";
		List<MetalKeywordsLogs> list=this.baseDaoSupport.queryForList(sql, MetalKeywordsLogs.class, metalKeywordsLogs.getGoods_id());
		if(list.size()==0){
			this.baseDaoSupport.insert("es_metal_keywords_logs", metalKeywordsLogs);
		}else{
			this.baseDaoSupport.execute(sql1,metalKeywordsLogs.getAdmin(),metalKeywordsLogs.getOriginal_keywords_ru(),metalKeywordsLogs.getUpdate_keywords_ru(),metalKeywordsLogs.getNowtime(),metalKeywordsLogs.getGoods_id());
		}
	}



	@Override
	public void editMetalKeywords(Integer goods_id, String metal_keywords) {
		String sql="select * from es_metal_keywords_logs WHERE goods_id=?";
		String sql1="update es_metal_keywords_logs set original_metal_keywords=? where goods_id=?";
		List<MetalKeywordsLogs> list=this.baseDaoSupport.queryForList(sql, MetalKeywordsLogs.class, goods_id);
		if(list.size()>0){
			this.baseDaoSupport.execute(sql1, metal_keywords,goods_id);
		}
	}



	@Override
	public MetalKeywordsLogs showMetalKeywords(Integer goods_id) {
		MetalKeywordsLogs metalKeywordsLogs=null;
		String sql="select * from es_metal_keywords_logs WHERE goods_id=?";
		List<MetalKeywordsLogs> list=this.baseDaoSupport.queryForList(sql, MetalKeywordsLogs.class, goods_id);
		if(list.size()>0){
			metalKeywordsLogs=list.get(0);
		}else{
			metalKeywordsLogs=new MetalKeywordsLogs();
		}
		return metalKeywordsLogs;
	}
}
