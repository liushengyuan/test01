package com.enation.app.b2b2c.core.service.cart.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.shop.core.model.support.DiscountPrice;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.CurrencyUtil;
@Component
public class StoreCartManager extends BaseSupport implements IStoreCartManager {
	private CartPluginBundle cartPluginBundle;
	private IDlyTypeManager dlyTypeManager;
	private IPromotionManager promotionManager;
	private IStoreGoodsManager storeGoodsManager;
	private IStoreMemberManager storeMemberManager;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.cart.IStoreCartManager#listGoods(java.lang.String)
	 */
	/*@Override
	public List<StoreCartItem> listGoods(String sessionid) {
		StringBuffer sql = new StringBuffer();
		//int wholesalenumber = this.wholesaleNumber(sessionid);
		//int goodsnum = this.goodsNum(sessionid);
		//if(goodsnum<wholesalenumber){
			sql.append("select s.store_id as store_id,p.wholesale_volume,g.freight,g.freightru ,p.whprice,p.whprice_ru, s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*c.price) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		}else{
			sql.append("select s.store_id as store_id,p.wholesale_volume,p.whprice,s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*p.whprice) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		}
		
		
		////System.out.println(sql);
		List list  =this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, sessionid);
		
		cartPluginBundle.filterList(list, sessionid);
		
		return list;
	}*/
	public List<StoreCartItem> listGoods(String sessionid) {
		StringBuffer sql = new StringBuffer();
		//int wholesalenumber = this.wholesaleNumber(sessionid);
		//int goodsnum = this.goodsNum(sessionid);
		//if(goodsnum<wholesalenumber){
			sql.append("select l.sendprice,l.freight_id,s.store_id as store_id,p.wholesale_volume ,p.whprice,p.whprice_ru, s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon,c.is_select as is_select, (c.num*c.price) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ,"+this.getTableName("good_logis_detail")+" l ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id and l.session_id=? and l.goods_id=c.goods_id");
		/*}else{
			sql.append("select s.store_id as store_id,p.wholesale_volume,p.whprice,s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*p.whprice) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		}*/
		
		
		////System.out.println(sql);
		List list  =this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, sessionid,sessionid);
		//System.out.println(sql.toString());
		cartPluginBundle.filterList(list, sessionid);
		
		return list;
	}
	public List<StoreCartItem> listGoodsCheckOut(String sessionid) {
		StringBuffer sql = new StringBuffer();
		//int wholesalenumber = this.wholesaleNumber(sessionid);
		//int goodsnum = this.goodsNum(sessionid);
		//if(goodsnum<wholesalenumber){
			sql.append("select l.sendprice,l.freight_id,s.store_id as store_id,p.wholesale_volume ,p.whprice,p.whprice_ru, s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*c.price) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ,"+this.getTableName("good_logis_detail")+" l ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id and l.session_id=? and c.is_select=1 and l.goods_id=c.goods_id");
		/*}else{
			sql.append("select s.store_id as store_id,p.wholesale_volume,p.whprice,s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*p.whprice) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		}*/
		
		
		////System.out.println(sql);
		List list  =this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, sessionid,sessionid);
		//System.out.println(sql.toString());
		cartPluginBundle.filterList(list, sessionid);
		
		return list;
	}
	/**
	 * 获取用户子在购物车中某一产品的某一规格下的最低批发数
	 * @param sessionid
	 * @return
	 */
	public int wholesaleNumber(String sessionid){
		StringBuffer sql = new StringBuffer();

		sql.append("select p.wholesale_volume from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		
		int wholesaleNumber = this.daoSupport.queryForInt(sql.toString(), sessionid);
		
		return wholesaleNumber;
	}
	/**
	 * 获取用户在购物车中购买某一产品的某一规格下的产品数
	 * @param sessionid
	 * @return
	 */
	public int goodsNum(String sessionid){
		StringBuffer sql = new StringBuffer();
		sql.append("select c.num from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		int goodsnum = this.daoSupport.queryForInt(sql.toString(), sessionid);
		
		return goodsnum;
	}
	
	@Override
	public OrderPrice countPrice(List<StoreCartItem> storeCart,String regionid, Integer shippingid, Boolean isProtected,Map map) {
		OrderPrice orderPrice = new OrderPrice();
		Integer store_id=null;
		//计算商品重量
		Double weight=0.0;
		//计算商品原始价格
		Double originalPrice=0.0;
		//订单总价格
		Double  orderTotal = 0d;
		//配送费用
		Double dlyPrice = 0d; //如果没有计算配送信息，为0
		Double dlyPrice2 = 0d;
		//获取会员
		StoreMember member  = storeMemberManager.getStoreMember();
		
		for (StoreCartItem storeCartItem : storeCart) {
			StoreGoods goods = this.storeGoodsManager.getGoods(storeCartItem.getGoods_id());
			if(this.getLanguage().equalsIgnoreCase("zh")){
			dlyPrice2=CurrencyUtil.add(dlyPrice2, storeCartItem.getFreight());
		}else{
			dlyPrice2=CurrencyUtil.add(dlyPrice2, storeCartItem.getFreightru());
		}
			//重量
			if(null!=goods && goods.getWeight() != null) {
				weight = CurrencyUtil.add(weight, CurrencyUtil.mul(goods.getWeight(), storeCartItem.getNum()));
			}else{
				weight = 0d;
			}
			Integer wholesalenumber = 0;
			if(storeCartItem.getWholesale_volume()!=null){
				wholesalenumber = storeCartItem.getWholesale_volume();
			}
			int goodsnum = storeCartItem.getNum();
			/*if(wholesalenumber==0){
				originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
			}*/
			if(goodsnum>=wholesalenumber && wholesalenumber>0){
				//批发价格
				if(this.getLanguage().equalsIgnoreCase("zh")){
					originalPrice+=CurrencyUtil.mul(storeCartItem.getWhprice(), storeCartItem.getNum());
				}else{
					originalPrice+=CurrencyUtil.mul(storeCartItem.getWhprice_ru(), storeCartItem.getNum());
				}
			}else if(goodsnum<wholesalenumber && goodsnum>0){
				originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
			}else if(wholesalenumber==0 && goodsnum>0){
				originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
			}
			//如果运费存在，则把运费添加到对应 店铺里面去
			/*if(storeCartItem.getSendprice()>0){
				originalPrice=CurrencyUtil.add(originalPrice,storeCartItem.getSendprice());
			}*/
		}
		
		
		
		/**
		 * -------------------------------
		 * 如果传递了配送信息，计算配送费用
		 * -------------------------------
		 * 
		 */
		if(regionid!=null &&shippingid!=null &&isProtected!=null  ){
			if(shippingid!=0 && weight!=0d ){
				//计算原始配置送费用
				Double[] priceArray = this.dlyTypeManager.countPrice(shippingid, weight, originalPrice, regionid);
				dlyPrice = priceArray[0];//费送费用
			}
		}
		/**
		 * ---------------------------------
		 * 设置订单的各种费用项
		 * ---------------------------------
		 */
		
		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
		orderTotal = CurrencyUtil.add(originalPrice, dlyPrice); 
		
		orderPrice.setGoodsPrice(originalPrice); //商品金额，优惠后的
		orderPrice.setShippingPrice(dlyPrice2);
		//积分设置为0
		orderPrice.setPoint(0); 
		orderPrice.setFreight(dlyPrice2);
		orderPrice.setOriginalPrice(originalPrice);
		orderPrice.setOrderPrice(CurrencyUtil.add(dlyPrice2,orderTotal));
		orderPrice.setWeight(weight);
		orderPrice.setNeedPayMoney(CurrencyUtil.add(dlyPrice2,orderTotal));// 需支付的金额默认为订单总金额
		orderPrice  = this.cartPluginBundle.coutPrice(store_id,orderPrice,map);
		return orderPrice;
	}
	
	@Override
	public OrderPrice countPriceByBonus(Integer store_id,List<StoreCartItem> storeCart,String regionid, Integer shippingid, Boolean isProtected,Map map) {
		OrderPrice orderPrice = new OrderPrice();
		//计算商品重量
		Double weight=0.0;
		//计算商品原始价格
		Double originalPrice=0.0;
		//订单总价格
		Double  orderTotal = 0d;
		//配送费用
		Double dlyPrice = 0d; //如果没有计算配送信息，为0
		Double dlyPrice2 = 0d;
		//获取会员
		StoreMember member  = storeMemberManager.getStoreMember();
		
		for (StoreCartItem storeCartItem : storeCart) {
			StoreGoods goods = this.storeGoodsManager.getGoods(storeCartItem.getGoods_id());
			if(this.getLanguage().equalsIgnoreCase("zh")){
			dlyPrice2=CurrencyUtil.add(dlyPrice2, storeCartItem.getFreight());
		}else{
			dlyPrice2=CurrencyUtil.add(dlyPrice2, storeCartItem.getFreightru());
		}
			//重量
			if(null!=goods && goods.getWeight() != null) {
				weight = CurrencyUtil.add(weight, CurrencyUtil.mul(goods.getWeight(), storeCartItem.getNum()));
			}else{
				weight = 0d;
			}
			Integer wholesalenumber = 0;
			if(storeCartItem.getWholesale_volume()!=null){
				wholesalenumber = storeCartItem.getWholesale_volume();
			}
			int goodsnum = storeCartItem.getNum();
			/*if(wholesalenumber==0){
				originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
			}*/
			if(goodsnum>=wholesalenumber && wholesalenumber>0){
				//批发价格
				if(this.getLanguage().equalsIgnoreCase("zh")){
					originalPrice+=CurrencyUtil.mul(storeCartItem.getWhprice(), storeCartItem.getNum());
				}else{
					originalPrice+=CurrencyUtil.mul(storeCartItem.getWhprice_ru(), storeCartItem.getNum());
				}
			}else if(goodsnum<wholesalenumber && goodsnum>0){
				originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
			}else if(wholesalenumber==0 && goodsnum>0){
				originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
			}
			//如果运费存在，则把运费添加到对应 店铺里面去
			/*if(storeCartItem.getSendprice()>0){
				originalPrice=CurrencyUtil.add(originalPrice,storeCartItem.getSendprice());
			}*/
		}
		
		
		
		/**
		 * -------------------------------
		 * 如果传递了配送信息，计算配送费用
		 * -------------------------------
		 * 
		 */
		if(regionid!=null &&shippingid!=null &&isProtected!=null  ){
			if(shippingid!=0 && weight!=0d ){
				//计算原始配置送费用
				Double[] priceArray = this.dlyTypeManager.countPrice(shippingid, weight, originalPrice, regionid);
				dlyPrice = priceArray[0];//费送费用
			}
		}
		/**
		 * ---------------------------------
		 * 设置订单的各种费用项
		 * ---------------------------------
		 */
		
		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
		orderTotal = CurrencyUtil.add(originalPrice, dlyPrice); 
		
		orderPrice.setGoodsPrice(originalPrice); //商品金额，优惠后的
		orderPrice.setShippingPrice(dlyPrice2);
		//积分设置为0
		orderPrice.setPoint(0); 
		orderPrice.setFreight(dlyPrice2);
		orderPrice.setOriginalPrice(originalPrice);
		orderPrice.setOrderPrice(CurrencyUtil.add(dlyPrice2,orderTotal));
		orderPrice.setWeight(weight);
		orderPrice.setNeedPayMoney(CurrencyUtil.add(dlyPrice2,orderTotal));// 需支付的金额默认为订单总金额
		orderPrice  = this.cartPluginBundle.coutPrice(store_id,orderPrice,map);
		return orderPrice;
	}
//	@Override
//	public OrderPrice countPrice(List<StoreCartItem> storeCart,String regionid, String[] shippingId, Boolean isProtected,Map map) {
//		OrderPrice orderPrice = new OrderPrice();
//		//计算商品重量
//		Double weight=0.0;
//		//计算商品原始价格
//		Double originalPrice=0.0;
//		//订单总价格
//		Double  orderTotal = 0d;
//		//配送费用
//		Double dlyPrice = 0d; //如果没有计算配送信息，为0
//		//优惠后的订单价格,默认为商品原始价格
//		Double coupPrice =0.0; 
//		//获取会员
//		StoreMember member  = storeMemberManager.getStoreMember();
//		
//		if(member==null){coupPrice=originalPrice;}
//		for (StoreCartItem storeCartItem : storeCart) {
//			
//			StoreGoods goods = this.storeGoodsManager.getGoods(storeCartItem.getGoods_id());
//			
//			weight=CurrencyUtil.add(weight, CurrencyUtil.mul(goods.getWeight(), storeCartItem.getNum()));
//			originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
//			if(member!=null){
//				coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
//			}
//		}
//		
//		/**
//		 * -------------------------------
//		 * 如果传递了配送信息，计算配送费用
//		 * -------------------------------
//		 * 
//		 */
//		if(regionid!=null &&isProtected!=null  ){
//			if(shippingId.length>0){
//				Double dlyPriceTotal = 0.0d;
//				for(int i=0;i<shippingId.length;i++){
//					//计算原始配置送费用
//					Double[] priceArray = this.dlyTypeManager.countPrice(Integer.valueOf(shippingId[i]), weight, originalPrice, regionid);
//					dlyPrice = priceArray[0];//配送费用
//					
//					if(member!=null){ //计算会员优惠
//						//对订单价格和积分执行优惠 			积分设置为0
//						DiscountPrice discountPrice  = this.promotionManager.applyOrderPmt(coupPrice, dlyPrice,0, member.getLv_id()); 
//						coupPrice=discountPrice.getOrderPrice() ; //优惠会后订单金额
//						dlyPrice = discountPrice.getShipFee(); //优惠后的配送费用
//					}
//					dlyPriceTotal = CurrencyUtil.add(dlyPriceTotal, dlyPrice);
//				}
//				dlyPrice = dlyPriceTotal;
//			}
//			//去除计算保价费用
//		}
//		
//		/**
//		 * ---------------------------------
//		 * 设置订单的各种费用项
//		 * ---------------------------------
//		 */
//		//打折金额：原始的商品价格-优惠后的商品金额
//		Double reducePrice = CurrencyUtil.sub(originalPrice , coupPrice);
//		
//		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
//		orderTotal = CurrencyUtil.add(coupPrice, 21.00); 
//		
//		orderPrice.setDiscountPrice(reducePrice); //优惠的金额
//		orderPrice.setGoodsPrice(coupPrice); //商品金额，优惠后的
//		orderPrice.setShippingPrice(21.00);
//		//积分设置为0
//		orderPrice.setPoint(0); 
//		orderPrice.setOriginalPrice(originalPrice);
//		orderPrice.setOrderPrice(orderTotal);
//		orderPrice.setWeight(weight);
//		orderPrice.setNeedPayMoney(orderTotal);// 需支付的金额默认为订单总金额
//		orderPrice  = this.cartPluginBundle.coutPrice(orderPrice,null);
//		return null;
//	}
	
	@Override
	public List<Map> storeListGoods(String sessionid) {
		List<Map> storeGoodsList= new ArrayList<Map>();
		List<StoreCartItem> goodsList =new ArrayList();
		
		goodsList= this.listGoods(sessionid); //商品列表
		for (StoreCartItem item : goodsList) {
			
			Integer wholesalenumber = 0;
			if(item.getWholesale_volume()!=null){
				wholesalenumber = item.getWholesale_volume();
			}
			int goodsnum = item.getNum();
			if(wholesalenumber==0){
				item.setCoupPrice(item.getPrice());
			}
			if(goodsnum>=wholesalenumber && wholesalenumber>0){
				if(this.getLanguage().equalsIgnoreCase("zh")){
					item.setCoupPrice(item.getWhprice());
				}else{
					item.setCoupPrice(item.getWhprice_ru());
				}
			}else{
				item.setCoupPrice(item.getPrice());
			}
			findStoreMap(storeGoodsList, item);
		}
		return storeGoodsList;
	}
	@Override
	public List<Map> storeListGoodsCheckOut(String sessionid) {
		List<Map> storeGoodsList= new ArrayList<Map>();
		List<StoreCartItem> goodsList =new ArrayList();
		
		goodsList= this.listGoodsCheckOut(sessionid); //商品列表
		for (StoreCartItem item : goodsList) {
			
			Integer wholesalenumber = 0;
			if(item.getWholesale_volume()!=null){
				wholesalenumber = item.getWholesale_volume();
			}
			int goodsnum = item.getNum();
			if(wholesalenumber==0){
				item.setCoupPrice(item.getPrice());
			}
			if(goodsnum>=wholesalenumber && wholesalenumber>0){
				if(this.getLanguage().equalsIgnoreCase("zh")){
					item.setCoupPrice(item.getWhprice());
				}else{
					item.setCoupPrice(item.getWhprice_ru());
				}
			}else{
				item.setCoupPrice(item.getPrice());
			}
			findStoreMap(storeGoodsList, item);
		}
		return storeGoodsList;
	}
	/**
	 * 获取店铺商品列表
	 * @param storeGoodsList
	 * @param map
	 * @param StoreCartItem
	 * @return list<Map>
	 */
	private void findStoreMap(List<Map> storeGoodsList,StoreCartItem item){
		int is_store=0;
		if (storeGoodsList.isEmpty()){
			addGoodsList(item, storeGoodsList);
		}else{
			for (Map map: storeGoodsList) {
				if(map.containsValue(item.getStore_id())){
					List list=(List) map.get("goodslist");
					list.add(item);
					is_store=1;
				}
			}
			if(is_store==0){
				addGoodsList(item, storeGoodsList);
			}
		}
	}
	/**
	 * 添加至店铺列表
	 * @param item
	 * @param storeGoodsList
	 */
	private void addGoodsList(StoreCartItem item,List<Map> storeGoodsList){
		Map map=new HashMap();
		List list=new ArrayList();
		list.add(item);
		map.put("store_id", item.getStore_id());
		map.put("store_name", item.getStore_name());
		map.put("goodslist", list);
		storeGoodsList.add(map);
	}
	/**
	 * 获取当前情况下所用的语言
	 * @return
	 */
	public String getLanguage(){
		try {
			HttpSession session = ThreadContextHolder.getHttpRequest()
					.getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();
			return language;
		} catch (Exception e) {
			return "zh";
		}
	}
	@Override
	public void  clean(String sessionid){
		String sql ="delete from cart where session_id=?";
		
		this.baseDaoSupport.execute(sql, sessionid);
	}
	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}
	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}
	public IPromotionManager getPromotionManager() {
		return promotionManager;
	}
	public void setPromotionManager(IPromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}
	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}
	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}
	public CartPluginBundle getCartPluginBundle() {
		return cartPluginBundle;
	}
	public void setCartPluginBundle(CartPluginBundle cartPluginBundle) {
		this.cartPluginBundle = cartPluginBundle;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
}
