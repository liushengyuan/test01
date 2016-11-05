package com.enation.app.b2b2c.core.action.api.goods;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.IStoreTemplateManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.b2b2c.core.test.GoodsGgalleryTest;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.impl.ProductStoreManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.tool.LuceneService;

/**
 * 店铺商品管理API
 * 
 * @author LiFenLong 2014-9-15
 */
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("goods")
public class StoreGoodsApiAction extends WWAction {
	private IGoodsManager goodsManager;
	private IOrderManager orderManager;
	private ILogiManager logiManager;
	private IStoreGoodsManager storeGoodsManager;
	private IStoreTemplateManager storeTemplateManager;
	private IStoreMemberManager storeMemberManager;
	private IStoreManager storeManager;
    private Integer goods_id1;
    private Integer audit_status1;//添加数据库audit_status字段
	private Integer market_enable1;//添加数据库market_enable字段
	private StoreGoods storeGoods;
	private Integer[] goods_id;
	private Integer productid;
	private Integer store;
	private Integer storeid;
	private Integer[] productIds;
	private Integer[] storeIds;
	private Integer[] storeNum;
	private GoodsGgalleryTest goodsGgalleryTest;
	private LuceneService luceneService;
	public GoodsGgalleryTest getGoodsGgalleryTest() {
		return goodsGgalleryTest;
	}

	public void setGoodsGgalleryTest(GoodsGgalleryTest goodsGgalleryTest) {
		this.goodsGgalleryTest = goodsGgalleryTest;
	}

	public String kai() {
		goodsGgalleryTest.test();
		return this.JSON_MESSAGE;
	}

	/**
	 * 发布商品
	 * 
	 * @param storeMember
	 *            店铺会员,StoreMember
	 * @param storeGoods
	 *            店铺商品,StoreGoods
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	@SuppressWarnings("static-access")
	public String add() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String dianpu=this.getText("storeG.dianpu");
		String shangpinsuccess=this.getText("storeG.shangpinsuccess");
		String shangpinfail=this.getText("storeG.shangpinfail");
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if(storeGoods.getPrice()==null || storeGoods.getPrice().equals("")){
				storeGoods.setPrice(0.0);
			}
			if (storeGoods.getPrice_ru()==null || storeGoods.getPrice_ru().equals("")) {
				storeGoods.setPrice_ru(0.0);
			}
			int storeid = storeMember.getStore_id();

			storeGoods.setStore_id(storeid);
            
			if (storeGoods.getGoods_transfee_charge() == 0) {
				Integer tempid = this.storeTemplateManager
						.getDefTempid(storeMember.getStore_id());
				if (tempid == null) {

						this.showErrorJson(dianpu);
					return JSON_MESSAGE;
				}
			}
             
			// 2015-05-20新增店铺名称字段-by kingaepx
			Store store = this.storeManager.getStore(storeid);
			storeGoods.setStore_name(store.getStore_name());
			storeGoods.setExpire_time(DateUtil.getDateline(DateUtil
					.getDateAfterForGoods(storeGoods.getVaild_day())));
			// 设置商品编号
			storeGoods.setSn(Long.toString(new Date().getTime())
					+ Integer.toString(new Random().nextInt(100)));
			HttpServletRequest request=ThreadContextHolder.getHttpRequest();
	           Integer freight=storeGoods.getFreightType();
	           Double weight=Double.parseDouble(request.getParameter("weight"));
	           Double depth=Double.parseDouble(storeGoods.getDepth());
	           Double width=Double.parseDouble(storeGoods.getWidth());
	           Double height=Double.parseDouble(storeGoods.getHeight());
	          if(freight!=1){
	        	  if(freight!=0){
	        	        int count=0;
	        	    	 List<LogisModel> logisModel=this.logiManager.findLogisModel(freight);
	        	    	 for (LogisModel logisModel2 : logisModel) {
							FreightStandard freightStandard=this.logiManager.getFreightById(logisModel2.getFreight_id());
							Double min_long=freightStandard.getMin_long();
							Double max_long=freightStandard.getMax_long();
							Double min_width=freightStandard.getMin_width();
							Double max_width=freightStandard.getMax_width();
							double min_height=freightStandard.getMin_high();
							double max_height=freightStandard.getMax_high();
							double min_weight=freightStandard.getMin_weight();
							double max_weight=freightStandard.getMax_weight();
							if(depth>=min_long && depth<max_long && width>=min_width && width<max_width && height>=min_height && height<max_height && weight>=min_weight && weight<max_weight){
								count++;
							}
						}
	        	    	 if(count==0){
	        	    		 this.showErrorJson("重量或者包裹尺寸不符合要求,请重新选择商品运费类型");
								return this.JSON_MESSAGE;
	        	    	 }
	        	  }
	           }
			//三级联动
	        /*  String province = request.getParameter("province");
	  		storeGoods.setProvince(province);

	  		String city = request.getParameter("city");
	  		storeGoods.setCity(city);

	  		String region = request.getParameter("region");
	  		storeGoods.setRegion(region);
	  		String province_id = request.getParameter("province_id");
			if (province_id == null || province_id.equals("")
					|| province_id.equals("0")) {
				throw new RuntimeException("请选择地区中的省！");
			}
			storeGoods.setProvince_id(Integer.valueOf(province_id));

			String city_id = request.getParameter("city_id");
			if (city_id == null || city_id.equals("") || province_id.equals("0")) {
				throw new RuntimeException("请选择地区中的市！");
			}
			storeGoods.setCity_id(Integer.valueOf(city_id));

			String region_id = request.getParameter("region_id");
			if (region_id == null || region_id.equals("")
					|| province_id.equals("0")) {
				throw new RuntimeException("请选择地区中的县！");
			}
			storeGoods.setRegion_id(Integer.valueOf(region_id));*/
			goodsManager.add(storeGoods);
//			this.updatedefaultstore(storeGoods.getGoods_id());//更新默认库存
			//this.luceneService.addindex(storeGoods.getGoods_id().toString(), storeGoods.getClass().getName(), storeGoods, true);
			this.showSuccessJson(shangpinsuccess);
		} catch (Exception e) {
				this.showErrorJson(shangpinfail + e.getMessage());
			this.logger.error(e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 添加/更新修改默认库存
	 */
	public void updatedefaultstore(Integer goodsId){
		StoreGoods goods = this.goodsManager.getStoreGoods(goodsId);
		List<Product> plist = this.goodsManager.queryforproduct(goodsId);
		if(plist!=null && plist.size()>0){
			for(Product p : plist){
				if(p.getStore()==0 && p.getEnable_store()==0){
					this.goodsManager.updateProductStore(goods.getStore(),goods.getEnable_store(),goodsId,p.getProduct_id());
				}
			}
			this.goodsManager.updategoodsstore(goodsId,plist.size());
		}
	}
	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

	/**
	 * 编辑商品信息
	 * 
	 * @param storeGoods
	 *            店铺商品,StoreGoods
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String edit() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
	
		String dianpu=this.getText("storeG.dianpu");
		String shangpin=this.getText("storeG.shangpin");
		String shangFail=this.getText("storeG.shangFail");
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if(storeGoods.getPrice()==null || storeGoods.getPrice().equals("")){
				storeGoods.setPrice(0.0);
			}
			if (storeGoods.getPrice_ru()==null || storeGoods.getPrice_ru().equals("")) {
				storeGoods.setPrice_ru(0.0);
			}
			if (storeGoods.getGoods_transfee_charge() == 0) {
				Integer tempid = this.storeTemplateManager
						.getDefTempid(storeMember.getStore_id());
				if (tempid == null) {

						this.showErrorJson(dianpu);
					return JSON_MESSAGE;
				}
			}
			storeGoods.setExpire_time(DateUtil.getDateline(DateUtil
					.getDateAfterForGoods(storeGoods.getVaild_day())));// 设置商品有效期\
			HttpServletRequest request=ThreadContextHolder.getHttpRequest();
			Integer freight=storeGoods.getFreightType();
	           Double weight=Double.parseDouble(request.getParameter("weight"));
	           Double depth=Double.parseDouble(storeGoods.getDepth());
	           Double width=Double.parseDouble(storeGoods.getWidth());
	           Double height=Double.parseDouble(storeGoods.getHeight());
	           if(freight!=1){
	        	   if(freight!=0){
	        	         int count=0;
	        	    	 List<LogisModel> logisModel=this.logiManager.findLogisModel(freight);
	        	    	 for (LogisModel logisModel2 : logisModel) {
							FreightStandard freightStandard=this.logiManager.getFreightById(logisModel2.getFreight_id());
							Double min_long=freightStandard.getMin_long();
							Double max_long=freightStandard.getMax_long();
							Double min_width=freightStandard.getMin_width();
							Double max_width=freightStandard.getMax_width();
							double min_height=freightStandard.getMin_high();
							double max_height=freightStandard.getMax_high();
							double min_weight=freightStandard.getMin_weight();
							double max_weight=freightStandard.getMax_weight();
							if(depth>=min_long && depth<max_long && width>=min_width && width<max_width && height>=min_height && height<max_height && weight>=min_weight && weight<max_weight){
								count++;
							}
						}
	        	    	 if(count==0){
	        	    		 this.showErrorJson("重量或者包裹尺寸不符合要求,请重新选择商品运费类型");
								return this.JSON_MESSAGE;
	        	    	 }
	        	   }
	           }
	          /* String province_id = request.getParameter("province_id");
	   		if (province_id == null || province_id.equals("")
	   				|| province_id.equals("0")) {
	   			throw new RuntimeException("请选择地区中的省！");
	   		}
	   		storeGoods.setProvince_id(Integer.valueOf(province_id));

	   		String city_id = request.getParameter("city_id");
	   		if (city_id == null || city_id.equals("") || province_id.equals("0")) {
	   			throw new RuntimeException("请选择地区中的市！");
	   		}
	   		storeGoods.setCity_id(Integer.valueOf(city_id));

	   		String region_id = request.getParameter("region_id");
	   		if (region_id == null || region_id.equals("")
	   				|| province_id.equals("0")) {
	   			throw new RuntimeException("请选择地区中的县！");
	   		}
	   		storeGoods.setRegion_id(Integer.valueOf(region_id));
	   		String province = request.getParameter("province");
	   		storeGoods.setProvince(province);

	   		String city = request.getParameter("city");
	   		storeGoods.setCity(city);

	   		String region = request.getParameter("region");
	   		storeGoods.setRegion(region);*/
			goodsManager.edit(storeGoods);
			this.goodsManager.editMetalKeywords(storeGoods.getGoods_id(), storeGoods.getMeta_keywords());
			this.luceneService.updateInformation(storeGoods.getGoods_id().toString(), storeGoods.getClass().getName(), storeGoods, true);
			this.showSuccessJson(shangpin);
		} catch (Exception e) {
				this.showErrorJson(shangFail + e.getMessage());
			this.logger.error(e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 删除商品(将商品添加至回收站)
	 * 
	 * @param goods_id
	 *            商品Id,Integer[]型
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String deleteGoods() {
	/*	HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		*/
		String addSuccess=this.getText("storeG.addSuccess");
		String chose=this.getText("storeG.chose");
		String addFail=this.getText("storeG.addFail");
		try {
			if (goods_id != null) {
				goodsManager.delete(goods_id);
					this.showSuccessJson(addSuccess);
			} else {
					this.showErrorJson(chose);
			}
		} catch (Exception e) {
				this.showErrorJson(addFail);
			this.logger.error(e);
		}
		return this.JSON_MESSAGE;
	}
    
	public String deleteGood() {
		String deleteSuccess=this.getText("storeG.deleteSuccess");
		String deleteFail=this.getText("storeG.deleteFail");
		try{
				int num=this.goodsManager.selectProduct(this.goods_id1);
				if(num==0){
					this.goodsManager.deleteStore(this.goods_id1,this.audit_status1,this.market_enable1);//执行语句传参
					this.showSuccessJson(deleteSuccess);
				}else{
					this.showErrorJson("此商品已被买家购买。不能删除");
					return this.JSON_MESSAGE;
				}
		} catch (Exception e) {
				this.showErrorJson(deleteFail);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 清除商品
	 * 
	 * @param goods_id
	 *            商品Id,Integer[]型
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String cleanGoods() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		
		String cleanSuccess=this.getText("storeG.cleanSuccess");
		String chose=this.getText("storeG.chose");
		String cleanFail=this.getText("storeG.cleanFail");
		try {
			if (goods_id != null) {
				goodsManager.clean(goods_id);
					this.showSuccessJson(cleanSuccess);
			} else {
					this.showErrorJson(chose);
			}
		} catch (Exception e) {
				this.showErrorJson(cleanFail);
			this.logger.error(e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 检验是否有订单购买过此商品
	 * 
	 * @param productid
	 *            货品Id,Integer型
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String checkProInOrder() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		
		String Buyed=this.getText("storeG.Buyed");
		String delete=this.getText("storeG.delete");
		
		boolean isinorder = this.orderManager.checkProInOrder(productid);
		if (isinorder) {
				this.showErrorJson(Buyed);
		} else {
				this.showSuccessJson(delete);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 还原商品
	 * 
	 * @param goods_id
	 *            商品Id,Integer[]型
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 * 
	 */
	public String revertGoods() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		
		String huanYuanSuccess=this.getText("storeG.huanYuanSuccess");
		String chose=this.getText("storeG.chose");
		String huanYuanFail=this.getText("storeG.huanYuanFail");
		
		if (language=="zh") {
			
			try {
				if (goods_id != null) {
					this.goodsManager.revert(goods_id);
					this.showSuccessJson(huanYuanSuccess);
				} else {
					this.showErrorJson(chose);
				}
			} catch (RuntimeException e) {
				this.showErrorJson(huanYuanFail);
				logger.error("商品还原失败", e);
			}
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 修改商品库存
	 * 
	 * @param goods_id
	 *            商品Id,Integer[]型
	 * @param store
	 *            商品库存,Integer型
	 * @param storeid
	 *            店铺Id,Integer型
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 * 
	 */
	public String saveGoodsStore() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		
		String SaveSuccess=this.getText("storeG.SaveSuccess");
		String SaveFail=this.getText("storeG.SaveFail");
		try {
			if (productIds != null && productIds.length > 0) {
				this.storeGoodsManager.saveGoodsSpecStore(storeIds,
						goods_id[0], storeNum, productIds);
			} else {
				this.storeGoodsManager.saveGoodsStore(storeid, goods_id[0],
						store);
			}
				this.showSuccessJson(SaveSuccess);
		} catch (Exception e) {
				this.showErrorJson(SaveFail);
				this.logger.error("保存库存失败:" + e);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 店铺内搜索商品（商家）
	 * 
	 * @param keyword
	 *            :搜索关键字,String，可为空
	 * @param store_catid
	 *            店铺分类id,int ，如果为0，则搜索全部
	 * @param is_groupbuy
	 *            是否已经为团购商品.
	 * @return 商品列表， List<Map> 型的json，Map中存的是goods
	 */
	public String search() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		
		String noDenglu=this.getText("storeG.noDenglu");
		String apiDiaoyong=this.getText("storeG.apiDiaoyong");
		
		try {
			HttpServletRequest request = this.getRequest();
			String keyword = request.getParameter("keyword");
			String store_catid = request.getParameter("store_catid");
			String is_groupbuy = request.getParameter("is_groupbuy");
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
					this.showErrorJson(noDenglu);
				return this.JSON_MESSAGE;
			}

			Map params = new HashMap();
			params.put("keyword", keyword);
			params.put("store_catid", store_catid);
			params.put("is_groupbuy", is_groupbuy);
			params.put("language", language);
			List<Map> goodsList = this.storeGoodsManager.storeGoodsList(
					storeMember.getStore_id(), params);
			this.json = JsonMessageUtil.getListJson(goodsList);

		} catch (Exception e) {
				this.showErrorJson(apiDiaoyong + e.getMessage());
				this.logger.error("商品搜索出错", e);
		}

		return this.JSON_MESSAGE;
	}

	public StoreGoods getStoreGoods() {
		return storeGoods;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public void setStoreGoods(StoreGoods storeGoods) {
		this.storeGoods = storeGoods;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public Integer[] getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer[] goods_id) {
		this.goods_id = goods_id;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public Integer getProductid() {
		return productid;
	}

	public void setProductid(Integer productid) {
		this.productid = productid;
	}

	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}

	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public Integer getStoreid() {
		return storeid;
	}

	public void setStoreid(Integer storeid) {
		this.storeid = storeid;
	}

	public IStoreTemplateManager getStoreTemplateManager() {
		return storeTemplateManager;
	}

	public void setStoreTemplateManager(
			IStoreTemplateManager storeTemplateManager) {
		this.storeTemplateManager = storeTemplateManager;
	}

	public Integer[] getProductIds() {
		return productIds;
	}

	public void setProductIds(Integer[] productIds) {
		this.productIds = productIds;
	}

	public Integer[] getStoreIds() {
		return storeIds;
	}

	public void setStoreIds(Integer[] storeIds) {
		this.storeIds = storeIds;
	}

	public Integer[] getStoreNum() {
		return storeNum;
	}

	public void setStoreNum(Integer[] storeNum) {
		this.storeNum = storeNum;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public Integer getGoods_id1() {
		return goods_id1;
	}

	public void setGoods_id1(Integer goods_id1) {
		this.goods_id1 = goods_id1;
	}

	public LuceneService getLuceneService() {
		return luceneService;
	}

	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}
	public Integer getAudit_status1() {//gate和set方法
		return audit_status1;
	}

	public void setAudit_status1(Integer audit_status1) {
		this.audit_status1 = audit_status1;
	}

	public Integer getMarket_enable1() {
		return market_enable1;
	}

	public void setMarket_enable1(Integer market_enable1) {
		this.market_enable1 = market_enable1;
	}

	
}
