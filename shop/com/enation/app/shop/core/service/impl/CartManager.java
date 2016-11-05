package com.enation.app.shop.core.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.service.cart.impl.StoreCartManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.GoodsLvPrice;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.mapper.CartItemMapper;
import com.enation.app.shop.core.model.mapper.CartItemMapperForApp;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.DiscountPrice;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.DoubleMapper;
import com.enation.framework.util.CurrencyUtil;

/**
 * 购物车业务实现
 * 
 * @author kingapex 2010-3-23下午03:30:50
 * edited by lzf 2011-10-08
 */

public class CartManager extends BaseSupport implements ICartManager {
	private IDlyTypeManager dlyTypeManager;
	private AllActivityManager allActivityManager;
	private CartPluginBundle cartPluginBundle;
	private IMemberLvManager memberLvManager;
	private IPromotionManager promotionManager;
	private StoreCartManager storeCartManager;
	private IProductManager productManager;
	public IProductManager getProductManager() {
		return productManager;
	}

	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int add(Cart cart) {
		//HttpCacheManager.sessionChange();
		
		
		/*
		 * 触发购物车添加事件
		 */
		this.cartPluginBundle.onAdd(cart);
		
		
		
		String sql ="select count(0) from cart where  product_id=? and session_id=? and itemtype=? ";	
		int count = this.baseDaoSupport.queryForInt(sql, cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
		if(count>0){
			Cart cart2=(Cart) this.baseDaoSupport.queryForObject("select * from cart where  product_id=? and session_id=? and itemtype=? ", Cart.class, cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
			List<Map> list=(List<Map>)allActivityManager.checkGoodsIsExists(cart.getGoods_id());
			if(list.size()>0){
				for (Map map : list) {
					 Integer limitbuy=(Integer)map.get("limitbuy");
					 if(limitbuy==1){
						 Integer limitnumber=(Integer)map.get("limitnumber");
						 Integer coun=cart2.getNum()+cart.getNum();
						 if(coun>=limitnumber){
							 this.baseDaoSupport.execute("update cart set num=? where  product_id=? and session_id=? and itemtype=? ",limitnumber,cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
								return 0;
						 }else{
							 Product product = productManager.get(cart.getProduct_id());
							 Integer newcount=cart.getNum()+cart2.getNum();	
							 if(newcount>product.getEnable_store()){
									this.baseDaoSupport.execute("update cart set num=? where  product_id=? and session_id=? and itemtype=? ", product.getEnable_store(),cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
									return 1;
								}else{
									this.baseDaoSupport.execute("update cart set num=num+? where  product_id=? and session_id=? and itemtype=? ", cart.getNum(),cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
									return 0;
								}
						 }
					 }
				}
			}else{
				Product product = productManager.get(cart.getProduct_id());
				Integer newcount=cart.getNum()+cart2.getNum();
				if(newcount>product.getEnable_store()){
					this.baseDaoSupport.execute("update cart set num=? where  product_id=? and session_id=? and itemtype=? ", product.getEnable_store(),cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
					return 1;
				}else{
					this.baseDaoSupport.execute("update cart set num=num+? where  product_id=? and session_id=? and itemtype=? ", cart.getNum(),cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
					return 0;
				}
			}
			
		}else{
			
			
			this.baseDaoSupport.insert("cart", cart);


			Integer cartid  = this.baseDaoSupport.getLastId("cart");
			cart.setCart_id(cartid);
			
			this.cartPluginBundle.onAfterAdd(cart);
			return cartid;
		}
       return 0;
	}
	
	/**
	 * 
	 */
	public Cart get(int cart_id){
		return (Cart)this.baseDaoSupport.queryForObject("SELECT * FROM cart WHERE cart_id=?", Cart.class, cart_id);
	}
	
	public Cart getCartByProductId(int productId, String sessionid){
		return (Cart)this.baseDaoSupport.queryForObject("SELECT * FROM cart WHERE product_id=? AND session_id=?", Cart.class, productId,sessionid);
	}
	
	public Cart getCartByProductId(int productId, String sessionid, String addon){
		return (Cart)this.baseDaoSupport.queryForObject("SELECT * FROM cart WHERE product_id=? AND session_id=? AND addon=?", Cart.class, productId, sessionid, addon);
	}

	public Integer countItemNum(String sessionid) {
		String sql = "select count(0) from cart where session_id =?";
		return this.baseDaoSupport.queryForInt(sql, sessionid);
	}
	
	
	public List<CartItem> listGoods(String sessionid) {
	
		List<CartItem>  list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select g.cat_id as catid,g.goods_id,g.freight,g.freightru,g.thumbnail,g.name_ru as name_ru,c.name ,p.wholesale_volume,p.whprice,p.whprice_ru,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.currency,c.addon,c.weight  from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id  and  c.session_id=?  order by c.cart_id desc ");
		 list =this.daoSupport.queryForList(sql.toString(), new CartItemMapper(), sessionid);
		 if(list.size()>0){
			 cartPluginBundle.filterList(list, sessionid);
		 }
		return list;
	}
	public List<CartItem> listGoodsForApp(String sessionid) {
		
		List<CartItem>  list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select g.cat_id as catid,g.goods_id,g.freight,g.freightru,g.thumbnail,g.name_ru as name_ru,c.name ,p.wholesale_volume,p.whprice,p.whprice_ru,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.currency,c.addon,c.weight,c.is_select  from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id  and  c.session_id=?  order by c.cart_id desc ");
		list =this.daoSupport.queryForList(sql.toString(), new CartItemMapperForApp(), sessionid);
		if(list.size()>0){
			cartPluginBundle.filterList(list, sessionid);
		}
		return list;
	}
	public List<CartItem> listGoodsOrder(String sessionid) {
		
		List<CartItem>  list = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select g.cat_id as catid,g.goods_id,g.freight,g.freightru,g.thumbnail,g.name_ru as name_ru,c.name ,p.wholesale_volume,p.whprice,p.whprice_ru,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.currency,c.addon,c.weight  from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id  and  c.session_id=? and c.is_select=1  order by c.cart_id desc ");
		list =this.daoSupport.queryForList(sql.toString(), new CartItemMapper(), sessionid);
		if(list.size()>0){
			cartPluginBundle.filterList(list, sessionid);
		}
		return list;
	}

 
	/**
	 * 应用会员价
	 * 
	 * @param itemList
	 * @param memPriceList
	 * @param discount
	 */
	private void applyMemPrice(List<CartItem> itemList,
			List<GoodsLvPrice> memPriceList, double discount) {
		for (CartItem item : itemList) {
			double price = item.getCoupPrice()* discount;
			for (GoodsLvPrice lvPrice : memPriceList) {
				if (item.getProduct_id().intValue() == lvPrice.getProductid()) {
					price = lvPrice.getPrice();
				}
			}

		 
			item.setCoupPrice(price);
		}
 
 
	}


 
 
	
	
	
	public void  clean(String sessionid){
		String sql ="delete from cart where session_id=?";
 
		this.baseDaoSupport.execute(sql, sessionid);
//		HttpCacheManager.sessionChange();
	}

	public void clean(String sessionid, Integer userid, Integer siteid) {

			String sql = "delete from cart where session_id=?";
			this.baseDaoSupport.execute(sql, sessionid);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("clean cart sessionid[" + sessionid + "]");
		}
//		HttpCacheManager.sessionChange();
	}

	public void delete(String sessionid, Integer cartid) {
		String sql = "delete from cart where session_id=? and cart_id=?";
		this.baseDaoSupport.execute(sql, sessionid, cartid);
		this.cartPluginBundle.onDelete(sessionid, cartid);
		this.cartPluginBundle.onDelete(sessionid, cartid);
//		HttpCacheManager.sessionChange();
	}

	public void updateNum(String sessionid, Integer cartid, Integer num) {
		String sql = "update cart set num=? where session_id =? and cart_id=?";
		this.baseDaoSupport.execute(sql, num, sessionid, cartid);
	}

	public Double countGoodsTotal(String sessionid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum( c.price * c.num ) as num from cart c ");
		sql.append("where  c.session_id=? and c.itemtype=0 and c.is_select=1 ");
		Double price = (Double) this.baseDaoSupport.queryForObject(sql
				.toString(), new DoubleMapper(), sessionid);
		return price;
	}
	public List<StoreCartItem> listGoods3(String sessionid,Integer id) {
		StringBuffer sql = new StringBuffer();
		//int wholesalenumber = this.wholesaleNumber(sessionid);
		//int goodsnum = this.goodsNum(sessionid);
		//if(goodsnum<wholesalenumber){
			sql.append("select l.sendprice,l.freight_id,s.store_id as store_id,p.wholesale_volume ,p.whprice,p.whprice_ru, s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*c.price) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ,"+this.getTableName("good_logis_detail")+" l ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id and l.session_id=? and l.goods_id=c.goods_id and g.goods_id=?");
		/*}else{
			sql.append("select s.store_id as store_id,p.wholesale_volume,p.whprice,s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*p.whprice) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		}*/
		
		
		////System.out.println(sql);
		List list  =this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, sessionid,sessionid,id);
		//System.out.println(sql.toString());
		cartPluginBundle.filterList(list, sessionid);
		
		return list;
	}

	public List<StoreCartItem> listGoods1(String sessionid) {
		StringBuffer sql = new StringBuffer();
		//int wholesalenumber = this.wholesaleNumber(sessionid);
		//int goodsnum = this.goodsNum(sessionid);
		//if(goodsnum<wholesalenumber){
			sql.append("select l.sendprice,l.freight_id,s.store_id as store_id,p.wholesale_volume ,p.whprice,p.whprice_ru, s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*c.price) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ,"+this.getTableName("good_logis_detail")+" l ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=? and c.is_select=1  AND c.store_id=s.store_id and l.session_id=? and l.goods_id=c.goods_id");
		/*}else{
			sql.append("select s.store_id as store_id,p.wholesale_volume,p.whprice,s.store_name as store_name,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs ,g.mktprice,g.unit,g.point,p.product_id,c.price, c.currency as currency, c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*p.whprice) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		}*/
		
		
		////System.out.println(sql);
		List list  =this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, sessionid,sessionid);
		//System.out.println(sql.toString());
		if(list.size()>0){
			cartPluginBundle.filterList(list, sessionid);
		}
		return list;
	}
	
	public Double  countGoodsDiscountTotal(String sessionid){
		

		List<CartItem> itemList = this.listGoodsOrder(sessionid);

		double price = 0; // 计算商品促销规则优惠后的总价
		for (CartItem item : itemList) {
			if(item.getWholesale_volume()!=null){
				if(item.getNum()>=item.getWholesale_volume() && item.getWholesale_volume()>0){
					if(this.storeCartManager.getLanguage().equalsIgnoreCase("zh")){
						item.setCoupPrice(item.getWhprice());
					}else{
						item.setCoupPrice(item.getWhprice_ru());
					}
					
				}
			}
			// price+=item.getSubtotal();
			price = CurrencyUtil.add(price, item.getSubtotal());
		}

		return price;
	}

	
	public Integer countPoint(String sessionid) {

//		Member member = UserServiceFactory.getUserService().getCurrentMember();
//		if (member != null) {
//			Integer memberLvId = member.getLv_id();
//			StringBuffer sql = new StringBuffer();
//			sql
//					.append("select c.*, g.goods_id, g.point from "
//							+ this.getTableName("cart")
//							+ " c,"
//							+ this.getTableName("goods")
//							+ " g, "
//							+ this.getTableName("product")
//							+ " p where p.product_id = c.product_id and g.goods_id = p.goods_id and c.session_id = ?");
//			List<Map> list = this.daoSupport.queryForList(sql.toString(),
//					sessionid);
//			Integer result = 0;
//			for (Map map : list) {
//				Integer goodsid = StringUtil.toInt(map.get("goods_id")
//						.toString());
//				List<Promotion> pmtList = new ArrayList();
//				
//				if(memberLvId!=null){
//					pmtList = promotionManager.list(goodsid, memberLvId);
//				}
//				
//				for (Promotion pmt : pmtList) {
//
//					// 查找相应插件
//					String pluginBeanId = pmt.getPmts_id();
//					IPromotionPlugin plugin = promotionManager
//							.getPlugin(pluginBeanId);
//
//					if (plugin == null) {
//						logger.error("plugin[" + pluginBeanId + "] not found ");
//						throw new ObjectNotFoundException("plugin["
//								+ pluginBeanId + "] not found ");
//					}
//
//					// 查找相应优惠方式
//					String methodBeanName = plugin.getMethods();
//					if (this.logger.isDebugEnabled()) {
//						this.logger.debug("find promotion method["
//								+ methodBeanName + "]");
//					}
//					IPromotionMethod promotionMethod = SpringContextHolder
//							.getBean(methodBeanName);
//					if (promotionMethod == null) {
//						logger.error("plugin[" + methodBeanName
//								+ "] not found ");
//						throw new ObjectNotFoundException("promotion method["
//								+ methodBeanName + "] not found ");
//					}
//
//					// 翻倍积分方式
//					if (promotionMethod instanceof ITimesPointBehavior) {
//						Integer point = StringUtil.toInt(map.get("point")
//								.toString());
//						ITimesPointBehavior timesPointBehavior = (ITimesPointBehavior) promotionMethod;
//						point = timesPointBehavior.countPoint(pmt, point);
//						result += point;
//					}
//
//				}
//			}
//			return result;
//		} else {
			StringBuffer sql = new StringBuffer();
			sql.append("select  sum(g.point * c.num) from "
					+ this.getTableName("cart") + " c,"
					+ this.getTableName("product") + " p,"
					+ this.getTableName("goods") + " g ");
			sql
					.append("where (c.itemtype=0  or c.itemtype=1)  and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?");

			return this.daoSupport.queryForInt(sql.toString(), sessionid);
//		}
	}

	public Double countGoodsWeight(String sessionid) {
		StringBuffer sql = new StringBuffer(
				"select sum( c.weight * c.num )  from cart c where c.session_id=?");
		Double weight = (Double) this.baseDaoSupport.queryForObject(sql
				.toString(), new DoubleMapper(), sessionid);
		return weight;
	}
	

 
	
	@Override
	public OrderPrice countPrice1(List<StoreCartItem> cartItemList, Integer shippingid,String regionid) {
		 
		
		OrderPrice orderPrice = new OrderPrice();
		//计算商品重量
		Double weight=0.0;
		//计算商品原始价格
		Double originalPrice=0.0;
		//订单总价格
		Double  orderTotal = 0d;
		//配送费用
		Double dlyPrice = 0d; //如果没有计算配送信息，为0
		//优惠后的订单价格,默认为商品原始价格
		Double coupPrice =0.0; 
		//获取会员
		Member member = UserConext.getCurrentMember();
        if(cartItemList.size()>0){
        	for (StoreCartItem cartItem : cartItemList) {
    			//System.out.println(cartItem.getSendprice());
    			weight=CurrencyUtil.add(weight, CurrencyUtil.mul(cartItem.getWeight(), cartItem.getNum()));
    			originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
    			if(member!=null){
    				if(cartItem.getWholesale_volume()!=null){
    					if(cartItem.getNum()>=cartItem.getWholesale_volume() && cartItem.getWholesale_volume()>0){
    						if(this.storeCartManager.getLanguage().equalsIgnoreCase("zh")){
    							coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getWhprice(), cartItem.getNum()));
    						}else{
    							coupPrice =  CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getWhprice_ru(), cartItem.getNum()));
    						}		
    					}else{
    						coupPrice =CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
    					}
    				}else{
    					coupPrice =CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()) );
    				}	
    			}
    		}
        }
		//计算会员优惠
		if(member!=null){
			originalPrice =countGoodsDiscountTotal(ThreadContextHolder.getHttpRequest().getSession().getId()); //应用了商品优惠规则后的商品价格
		}
		int point =0;
		
		/**
		 * -------------------------------
		 * 如果传递了配送信息，计算配送费用
		 * -------------------------------
		 * 
		 */
		if(regionid!=null &&shippingid!=null ){
			if(shippingid!=0){
				//计算原始配置送费用
				Double[] priceArray = this.dlyTypeManager.countPrice(shippingid, weight, originalPrice, regionid);
				dlyPrice = priceArray[0];//费送费用
			}
			
			if(member!=null){ //计算会员优惠
				//对订单价格和积分执行优惠
				DiscountPrice discountPrice  = this.promotionManager.applyOrderPmt(coupPrice, dlyPrice,point, member.getLv_id()); 
				coupPrice=discountPrice.getOrderPrice() ; //优惠会后订单金额
				dlyPrice = discountPrice.getShipFee(); //优惠后的配送费用
				point = discountPrice.getPoint(); //优惠后的积分
			}
			
			//去除保价费用
		}
		
		
		
		/**
		 * ---------------------------------
		 * 设置订单的各种费用项
		 * ---------------------------------
		 */
		if(member==null){coupPrice=originalPrice;}
		//打折金额：原始的商品价格-优惠后的商品金额
		Double reducePrice = CurrencyUtil.sub(originalPrice , coupPrice);
		
		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
		orderTotal = CurrencyUtil.add(coupPrice, dlyPrice); 
		
		orderPrice.setDiscountPrice(reducePrice); //优惠的金额
		
		
			orderPrice.setGoodsPrice(coupPrice); //商品金额，优惠后的          whj2015-05-15修改，如果不修改，导致商品金额是0，之前的源码“coupPrice-originalPrice”，优惠金额与原始价格相同，所以相减为0,不知道对不对，请小李休闲修改
	
		
		orderPrice.setShippingPrice(dlyPrice);
		orderPrice.setPoint(point); 
		orderPrice.setOriginalPrice(originalPrice);
		orderPrice.setOrderPrice(orderTotal);
		orderPrice.setWeight(weight);
		orderPrice.setNeedPayMoney(orderTotal);// 需支付的金额默认为订单总金额
		if(cartItemList.size()>0){
			Integer store_id=null;
			orderPrice  = this.cartPluginBundle.coutPrice(store_id,orderPrice,null);
		}
		return orderPrice;
		 
		
		/**
		 * --------------------------------------
		 * 废弃掉的代码在这里，有可能要改成插件的
		 * --------------------------------------
		 * 
		 * 		//计算捆绑商品的总价，并加入订单总价中
		Double pgkTotal = countPgkTotal(sessionid);
		//计算团购总价
		Double groupBuyTotal = countGroupBuyTotal(sessionid);
		
		originalPrice = CurrencyUtil.add(originalPrice,pgkTotal);
		originalPrice = CurrencyUtil.add(originalPrice,groupBuyTotal);
		 */
	}
	@Override
	public OrderPrice countPrice(List<CartItem> cartItemList, Integer shippingid,String regionid) {
		 
		
		OrderPrice orderPrice = new OrderPrice();
		//计算商品重量
		Double weight=0.0;
		//计算商品原始价格
		Double originalPrice=0.0;
		//订单总价格
		Double  orderTotal = 0d;
		//配送费用
		Double dlyPrice = 0d; //如果没有计算配送信息，为0
		//优惠后的订单价格,默认为商品原始价格
		Double coupPrice =0.0; 
		Double dlyPrice2 = 0d;
		//获取会员
		Member member = UserConext.getCurrentMember();

		for (CartItem cartItem : cartItemList) {
			weight=CurrencyUtil.add(weight, CurrencyUtil.mul(cartItem.getWeight(), cartItem.getNum()));
			originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
			if(this.storeCartManager.getLanguage().equalsIgnoreCase("zh")){
			dlyPrice2=CurrencyUtil.add(dlyPrice2, cartItem.getFreight());
			}else{
				dlyPrice2=CurrencyUtil.add(dlyPrice2, cartItem.getFreightru());
			}
			if(member!=null){
				if(cartItem.getWholesale_volume()!=null){
					if(cartItem.getNum()>=cartItem.getWholesale_volume() && cartItem.getWholesale_volume()>0){
						if(this.storeCartManager.getLanguage().equalsIgnoreCase("zh")){
							coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getWhprice(), cartItem.getNum()));
						}else{
							coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getWhprice_ru(), cartItem.getNum()));
						}		
					}else{
						coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
					}
				}else{
					coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
				}	
			}else{

				if(cartItem.getWholesale_volume()!=null){
					if(cartItem.getNum()>=cartItem.getWholesale_volume() && cartItem.getWholesale_volume()>0){
						if(this.storeCartManager.getLanguage().equalsIgnoreCase("zh")){
							coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getWhprice(), cartItem.getNum()));
						}else{
							coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getWhprice_ru(), cartItem.getNum()));
						}		
					}else{
						coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
					}
				}else{
					coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
				}	
			
			}
		}
		//计算会员优惠
		if(member!=null){
			originalPrice =countGoodsDiscountTotal(ThreadContextHolder.getHttpRequest().getSession().getId()); //应用了商品优惠规则后的商品价格
		}else{
			originalPrice =countGoodsDiscountTotal(ThreadContextHolder.getHttpRequest().getSession().getId()); //应用了商品优惠规则后的商品价格
		}
		int point =0;
		
		/**
		 * -------------------------------
		 * 如果传递了配送信息，计算配送费用
		 * -------------------------------
		 * 
		 */
		if(regionid!=null &&shippingid!=null ){
			if(shippingid!=0){
				//计算原始配置送费用
				Double[] priceArray = this.dlyTypeManager.countPrice(shippingid, weight, originalPrice, regionid);
				dlyPrice = priceArray[0];//费送费用
			}
			
			if(member!=null){ //计算会员优惠
				//对订单价格和积分执行优惠
				DiscountPrice discountPrice  = this.promotionManager.applyOrderPmt(coupPrice, dlyPrice,point, member.getLv_id()); 
				coupPrice=discountPrice.getOrderPrice() ; //优惠会后订单金额
				dlyPrice = discountPrice.getShipFee(); //优惠后的配送费用
				point = discountPrice.getPoint(); //优惠后的积分
			}
			
			//去除保价费用
		}
		
		
		
		/**
		 * ---------------------------------
		 * 设置订单的各种费用项
		 * ---------------------------------
		 */
		if(member==null){coupPrice=originalPrice;}
		//打折金额：原始的商品价格-优惠后的商品金额
		Double reducePrice = CurrencyUtil.sub(originalPrice , coupPrice);
		
		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
		orderTotal = CurrencyUtil.add(coupPrice, dlyPrice); 
		
		orderPrice.setDiscountPrice(reducePrice); //优惠的金额
		orderPrice.setGoodsPrice(coupPrice); //商品金额，优惠后的          whj2015-05-15修改，如果不修改，导致商品金额是0，之前的源码“coupPrice-originalPrice”，优惠金额与原始价格相同，所以相减为0,不知道对不对，请小李休闲修改
		orderPrice.setShippingPrice(dlyPrice2);
		orderPrice.setPoint(point); 
		orderPrice.setFreight(dlyPrice2);
//		orderPrice.getFreightru(point);
		orderPrice.setOriginalPrice(originalPrice);
		orderPrice.setOrderPrice(CurrencyUtil.add(dlyPrice2,orderTotal));
		orderPrice.setWeight(weight);
		orderPrice.setNeedPayMoney(CurrencyUtil.add(dlyPrice2,orderTotal));// 需支付的金额默认为订单总金额
		Integer store_id=null;
		orderPrice  = this.cartPluginBundle.coutPrice(store_id,orderPrice,null);
		return orderPrice;
		 
		
		/**
		 * --------------------------------------
		 * 废弃掉的代码在这里，有可能要改成插件的
		 * --------------------------------------
		 * 
		 * 		//计算捆绑商品的总价，并加入订单总价中
		Double pgkTotal = countPgkTotal(sessionid);
		//计算团购总价
		Double groupBuyTotal = countGroupBuyTotal(sessionid);
		
		originalPrice = CurrencyUtil.add(originalPrice,pgkTotal);
		originalPrice = CurrencyUtil.add(originalPrice,groupBuyTotal);
		 */
	}
	

	public IPromotionManager getPromotionManager() {
		return promotionManager;
	}
 

	public CartPluginBundle getCartPluginBundle() {
		return cartPluginBundle;
	}

	public void setCartPluginBundle(CartPluginBundle cartPluginBundle) {
		this.cartPluginBundle = cartPluginBundle;
	}

 
	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setPromotionManager(IPromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}

	@Override
	public boolean checkGoodsInCart(Integer goodsid) {
		String sql ="select count(0) from cart where goods_id=?";
		return this.baseDaoSupport.queryForInt(sql, goodsid)>0;
	}

	public StoreCartManager getStoreCartManager() {
		return storeCartManager;
	}

	public void setStoreCartManager(StoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}

	/**
	 * 查询没有登录用户添加到的购物车商品，此方法用到登录用户，可查看历史购物车
	 * @param sessionid
	 * @return
	 */
	@Override
	public List<Cart> queryCartlist(String sessionid) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select c.* from es_cart c where c.session_id =? and c.member_id=0 ");
		List<Cart> cartlist = this.baseDaoSupport.queryForList(sql.toString(), Cart.class, sessionid);
		if(cartlist!=null && cartlist.size()>0){
			return cartlist;
		}else{
			return null;
		}
	}
	@Override
	public List<CartItem> listGoodsHistory(String sessionid) {

		
		List<CartItem>  list = null;
		StringBuffer sql = new StringBuffer();

		sql.append("select g.cat_id as catid,g.goods_id,g.freight,g.freightru,g.thumbnail,g.name_ru as name_ru,c.name ,p.wholesale_volume,p.whprice,p.whprice_ru,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.currency,c.addon,c.weight  from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ");
		Member member = UserConext.getCurrentMember();
		if(member!=null){
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and ( c.session_id=? ");
			sql.append(" or c.member_id=? ) and c.is_select=0 order by c.cart_id desc ");
			 list =this.daoSupport.queryForList(sql.toString(), new CartItemMapper(), sessionid,member.getMember_id());
		}else{
			sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and  c.session_id=? c.is_select=0 order by c.cart_id desc ");
			 list =this.daoSupport.queryForList(sql.toString(), new CartItemMapper(), sessionid);
		}
		cartPluginBundle.filterList(list, sessionid);
	

		return list;
	
	}
	@Override
	public void deleteCartByIsselect(String sessionid) {
		 String sql="update es_cart set is_select=? where session_id=? ";
		  this.baseDaoSupport.execute(sql,0,sessionid);
	}
	@Override
	public void selectCartByIsselect(String sessionid) {
		 String sql="update es_cart set is_select=? where session_id=? ";
		  this.baseDaoSupport.execute(sql,1,sessionid);
	}

	@Override
	public void updateCartByIsselect(String sessionid, Integer goods_id) {
		String sql="update es_cart set is_select=? where session_id=? and goods_id=?";
		this.baseDaoSupport.execute(sql,1,sessionid,goods_id);
	}
	@Override
	public void updateCartByDisselect(String sessionid, Integer goods_id) {
		String sql="update es_cart set is_select=? where session_id=? and goods_id=?";
		this.baseDaoSupport.execute(sql,0,sessionid,goods_id);
	}
	public void deleteForApp(String sessionid, Integer cartid) {
		StringBuffer sql1 = new StringBuffer();
		String sql="";
		sql1.append(" select goods_id from es_cart where session_id=? and cart_id=? ");
		Integer goods_id = this.baseDaoSupport.queryForInt(sql1.toString(),sessionid, cartid);
		if(goods_id!=null){
			 sql= "delete from es_good_logis_detail where session_id=? and goods_id=?";
			 this.baseDaoSupport.execute(sql, sessionid, goods_id);
		}
		String sql2 = "delete from cart where session_id=? and cart_id=?";
		this.baseDaoSupport.execute(sql2, sessionid, cartid);
		this.cartPluginBundle.onDelete(sessionid, cartid);
		this.cartPluginBundle.onDelete(sessionid, cartid);
//		HttpCacheManager.sessionChange();
	}
//**

	@Override
	public void cleanIsCheck(String sessionid, Integer goods_id) {
		String sql ="delete from cart where session_id=? and goods_id=? and is_select=1";
		this.baseDaoSupport.execute(sql, sessionid,goods_id);
		
	}

	public AllActivityManager getAllActivityManager() {
		return allActivityManager;
	}

	public void setAllActivityManager(AllActivityManager allActivityManager) {
		this.allActivityManager = allActivityManager;
	}

	@Override
	public Double countGoodsTotalForRussion(String sessionid,Integer is_belong) {
		StringBuffer sql = new StringBuffer();
		Double price=0.0;
		if(is_belong==-1){
			sql.append("SELECT sum( c.price * c.num ) as num from es_cart AS c LEFT JOIN es_goods AS g ON g.goods_id=c.goods_id WHERE c.session_id=? and c.itemtype=0 and c.is_select=1 ");
			 price = (Double) this.baseDaoSupport.queryForObject(sql
						.toString(), new DoubleMapper(), sessionid);
		}else{
			sql.append("SELECT sum( c.price * c.num ) as num from es_cart AS c LEFT JOIN es_goods AS g ON g.goods_id=c.goods_id WHERE c.session_id=? and c.itemtype=0 and c.is_select=1 AND is_belong=?");
			 price = (Double) this.baseDaoSupport.queryForObject(sql
					.toString(), new DoubleMapper(), sessionid,is_belong);
		}
		return price;
	}

	@Override
	public Integer getCartCountForGoods(String sessionid, Integer store_id,Integer is_belong) {
		List list=null;
		if(is_belong==-1){
			String sql="select * from es_cart AS c LEFT JOIN es_goods AS g ON c.goods_id=g.goods_id   WHERE c.session_id=? and c.itemtype=0 and c.is_select=1  AND c.store_id=?";
			list=this.baseDaoSupport.queryForList(sql,sessionid,store_id);
		}else{
			String sql="select * from es_cart AS c LEFT JOIN es_goods AS g ON c.goods_id=g.goods_id   WHERE c.session_id=? and c.itemtype=0 and c.is_select=1 AND is_belong=? AND c.store_id=?";
			list=this.baseDaoSupport.queryForList(sql,sessionid,is_belong,store_id);
		}
		return list.size();
	}
}
