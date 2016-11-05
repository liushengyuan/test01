package com.enation.app.shop.core.action.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.CheckMemberLogin;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.GoodLogisDetail;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IExchangeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.app.shop.core.service.impl.AllActivityManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * 购物车api
 * 
 * @author kingapex 2013-7-19下午12:58:43
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("cart")
public class CartApiAction extends WWAction {
	private IMemberAddressManager memberAddressManager ;
	private ICartManager cartManager;
	private AllActivityManager allActivityManager;
	private IPromotionManager promotionManager;
	private IExchangeManager exchangeManager;
	private int goodsid;
	private int productid;
	private int num;// 要向购物车中活加的货品数量
	private IProductManager productManager;
	private IStoreMemberManager storeMemberManager;
	private ILogiManager logiManager;
	public IExchangeManager getExchangeManager() {
		return exchangeManager;
	}
	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}

	public void setMemberAddressManager(IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}
	public void setExchangeManager(IExchangeManager exchangeManager) {
		this.exchangeManager = exchangeManager;
	}
	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}
	private IGoodsManager goodsManager;
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	// 在向购物车添加货品时，是否在返回的json串中同时显示购物车数据。
	// 0为否,1为是
	private int showCartData;
	
	private Integer min_number;//最小发货量
	/**
	 * 将一个货品添加至购物车。 需要传递productid和num参数
	 * 
	 * @param productid
	 *            货品id，int型
	 * @num 数量，int 型
	 * 
	 * @return 返回json串 result 为1表示调用成功0表示失败 ，int型 message 为提示信息
	 */
	public String addProduct() {
		/*HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		Integer store_id= Integer.parseInt(request.getParameter("store_id"));
		HttpSession session = ServletActionContext.getRequest().getSession();
		String sessionid = session.getId();
		int count = this.cartManager.countItemNum(sessionid);
		List<CartItem> cartItems = cartManager.listGoods(sessionid);
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String curCurrency = "";
		String nocheck=this.getText("cart.NoChose");
		String bizhong=this.getText("cart.different");
		//获取当前登录的会员
		StoreMember member= storeMemberManager.getStoreMember();
		if(member!=null){
			if(member.getStore_id()!=null && member.getStore_id().equals(store_id)){
				this.showErrorJson(nocheck);
				return JSON_MESSAGE;
			}
		}
		if (language.equals("zh")) {
			curCurrency = "CNY";
		} else if (language.equals("ru")) {
			curCurrency = "RUB";
		}
		if (count == 0) {
			Product product = productManager.get(productid);
			this.addProductToCart(product, language);
		} else {
			String currency = cartItems.get(0).getCurrency();
			if (curCurrency.equals(currency)) {
				Product product = productManager.get(productid);
				this.addProductToCart(product, language);
			} else {
					this.showErrorJson(bizhong);
			}
		}
		return this.JSON_MESSAGE;*/
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Integer freight_id=Integer.parseInt(request.getParameter("freight_id"));
		//System.out.println(freight_id);
		Integer store_id= Integer.parseInt(request.getParameter("store_id"));
		HttpSession session = ServletActionContext.getRequest().getSession();
		String sessionid = session.getId();
		int count = this.cartManager.countItemNum(sessionid);
		List<CartItem> cartItems = cartManager.listGoods(sessionid);
		Double sendprice=0.0;
		if(freight_id!=-1){
			if(freight_id==0){
				sendprice=0.0;
			}else{
				sendprice=Double.parseDouble(request.getParameter("sendprice"));
			}
		}else{
			 int numCart=0;
			 if(cartItems.size()>0){
				 for (CartItem cartItem : cartItems) {
						if(cartItem.getGoods_id()==this.goodsid){
							numCart=cartItem.getNum()+this.num;
							Goods goods=this.goodsManager.getGoods(goodsid);
							sendprice=goods.getFreight()*numCart;
						}else{
							Goods goods=this.goodsManager.getGoods(goodsid);
							sendprice=goods.getFreight()*this.num;
						}
					}
			 }else{
				 Goods goods=this.goodsManager.getGoods(goodsid);
					sendprice=goods.getFreight()*this.num;
			 }
		}
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String noBuy=this.getText("cart.NoChose");
		String different=this.getText("cart.different");
		String onChoose=this.getText("cart.different");
		String curCurrency = "";
		//获取当前登录的会员
		StoreMember member= storeMemberManager.getStoreMember();
		if(member!=null){
			if(member.getStore_id()!=null && member.getStore_id().equals(store_id)){
				this.showErrorJson(noBuy);
				return JSON_MESSAGE;
			}
		}
		Double send=0.0;
		GoodLogisDetail goodLogisDetail=new GoodLogisDetail();
		if (language=="zh") {
			curCurrency = "CNY";
			goodLogisDetail.setSendprice(sendprice);
		} else if (language=="ru") {
			curCurrency = "RUB";
			Double price=exchangeManager.getExchange("RUB");
			send=sendprice/price;
			goodLogisDetail.setSendprice(send);
			
		}
	 if(freight_id!=-1){
		if(freight_id!=0 && sendprice!=0.0){
			Goods goods=this.goodsManager.getGoods(goodsid);
			Double weight=goods.getWeight();
			FreightStandard freightStandard=this.logiManager.getFreightById(freight_id);
		    Double	price11=0.0;
			if (language=="zh") {
				price11=FreightUtls.getFreight(freightStandard, weight*num);
				String price111=price11+"";
				if(price111.equals(sendprice+"") || price11==sendprice){
					
				}else{
					this.showErrorJson(onChoose);
					return this.JSON_MESSAGE;
				}
			}/*else if (language=="ru") {
				Double price22=(FreightUtls.getFreight(freightStandard, weight*num));
				String price222=price22+"";
				double dsend=Math.round(send);
				if(price222.equals(dsend+"") || price22==dsend){
					
				}else{
					this.showErrorJson(onChoose);
					return this.JSON_MESSAGE;
				}
			}*/
		}
	 }	
		
		goodLogisDetail.setCurrency(curCurrency.toUpperCase());
		goodLogisDetail.setFreight_id(freight_id);
		goodLogisDetail.setGoods_id(this.goodsid);
		
		goodLogisDetail.setSession_id(sessionid);
		goodLogisDetail.setStore_id(store_id);
		//System.out.println(productid);
		if (count == 0) {
			Product product = productManager.get(productid);
			this.addProductToCart(product, language);
			this.logiManager.addGoodLogisDetail(goodLogisDetail);
		} else {
			String currency = cartItems.get(0).getCurrency();
			if (curCurrency.equals(currency)) {
				List<StoreCartItem> list= cartManager.listGoods3(sessionid,this.goodsid);
				int count1=0;
				int count2=0;
				for (StoreCartItem storecart : list) {
					if(freight_id==0 || freight_id==-1){
						this.logiManager.addGoodLogisDetail(goodLogisDetail);
					}else{
						if(productid!=storecart.getProduct_id()){
							count1++;
						}else{
							count2++;
						}
					}
					
				}
				if(count1==list.size()){
					this.logiManager.addGoodLogisDetail(goodLogisDetail);
				}
				if(count2>=1){
					this.logiManager.addGoodLogisDetail(goodLogisDetail);
				}
				Product product = productManager.get(productid);
				this.addProductToCart(product, language);
				
			} else {
				this.showErrorJson(different);
			}
			
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 将一个商品添加到购物车 需要传递goodsid 和num参数
	 * 
	 * @param goodsid
	 *            商品id，int型
	 * @param num
	 *            数量，int型
	 * 
	 * @return 返回json串 result 为1表示调用成功0表示失败 message 为提示信息
	 */
	public String addGoods() {
		HttpSession session = ServletActionContext.getRequest().getSession();
		String sessionid = session.getId();
		int count = this.cartManager.countItemNum(sessionid);
		List<CartItem> cartItems = cartManager.listGoods(sessionid);
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String bizhong=this.getText("cart.different");
		String curCurrency = "";
		if (language.equals("zh")) {
			curCurrency = "CNY";
		} else if (language.equals("ru")) {
			curCurrency = "RUB";
		}
		
		if (count == 0) {
			Product product = productManager.getByGoodsId(goodsid);
			this.addProductToCart(product, language);
		} else {
			String currency = cartItems.get(0).getCurrency();
			if (curCurrency.equals(currency)) {
				Product product = productManager.getByGoodsId(goodsid);
				this.addProductToCart(product, language);
			} else {
					this.showErrorJson(bizhong);
			}
		}
		return this.JSON_MESSAGE;
	}
    public String getDynaticCartData(){
    	HttpServletRequest request=ThreadContextHolder.getHttpRequest();
    	HttpSession session =request.getSession();
		String sessionid  = session.getId();
    	String propertys = request.getParameter("propertys");
    	//System.out.println(propertys);
    	String[] ids={};
    	if(!StringUtil.isEmpty(propertys) && !propertys.trim().equalsIgnoreCase("")){
    		     ids = propertys.split(",");
    		     this.cartManager.deleteCartByIsselect(sessionid);  
    		for (int i = 0; i < ids.length; i++) {
    			this.cartManager.updateCartByIsselect(sessionid, Integer.valueOf(ids[i]));
    	   }
    	}else{
    		this.cartManager.deleteCartByIsselect(sessionid);
    	}
    	//*
		Map<String, Object> result = new HashMap<String, Object>();
		/*Integer addressid=(Integer.parseInt(request.getParameter("address_id")));
		Integer shipping_id=(Integer.parseInt(request.getParameter("shipping_id")));*/
		String regionid =null;
		Integer shipping_id=null;
		
		//如果传递了地址，已经选完配送方式了
		/*if(addressid!=null){
			MemberAddress address =memberAddressManager.getAddress(addressid);
			regionid= ""+address.getRegion_id();
		}*/
		
		OrderPrice orderprice  =this.cartManager.countPrice1(cartManager.listGoods1(sessionid), shipping_id,regionid);
		/*cartManager.listGoods(sessionid).get(0).getCurrency();*/
		result.put("orderprice", orderprice);
		Locale locale = (Locale)session.getAttribute("locale");
		String language=locale.getLanguage();
		Double sum=0.0;
		//int count = this.cartManager.countItemNum(sessionid);
		/*List<CartItem> cartItems=cartManager.listGoods(sessionid);*/
		List<StoreCartItem> cartItems=cartManager.listGoods1(sessionid);
		if(cartItems.size()>0){
			for (StoreCartItem storeCartItem : cartItems) {
				if(storeCartItem.getFreight_id()!=-1){
				  if(storeCartItem.getFreight_id()!=0 && storeCartItem.getSendprice()!=0.0){
				Integer goods_id=storeCartItem.getGoods_id();
				Goods goods=this.goodsManager.getGoods(goods_id);
				Integer num=storeCartItem.getNum();
				Double weight=goods.getWeight();
				if(storeCartItem.getFreight_id()!=0){
				FreightStandard freightStandard=this.logiManager.getFreightById(storeCartItem.getFreight_id());
				double sendprice=FreightUtls.getFreight(freightStandard, weight*num);
				   if(language=="zh"){
					   sum+=sendprice;
					   this.logiManager.updateLogisModel(sendprice,sessionid,storeCartItem.getGoods_id());
				   }else if(language=="ru"){
					   Double price1=this.exchangeManager.getExchange("RUB");
//						//System.out.println(price1);
					   sum+=sendprice*price1;
					   this.logiManager.updateLogisModel(sendprice*price1,sessionid,storeCartItem.getGoods_id());
				       }
				     }
				  }	else{
					  Double m=0.0;
					  sum+=m;
					  this.logiManager.updateLogisModel(m,sessionid,storeCartItem.getGoods_id());
				  }	
//				  this.logiManager.updateLogisModel(sum,sessionid,storeCartItem.getGoods_id());
				}else{
					Goods goods=this.goodsManager.getGoods(storeCartItem.getGoods_id());
				    Double freightprice=goods.getFreight()*storeCartItem.getNum();
					/*sum+=storeCartItem.getSendprice();*/
					sum+=freightprice;
					this.logiManager.updateLogisModel(freightprice,sessionid,storeCartItem.getGoods_id());
				}
		}
		}
		String currency=null;
		if(cartItems.size()>0){
			currency=cartItems.get(0).getCurrency();
		}else{
			currency="CNY";
		}
		if(currency!=null && currency.equals("CNY")){
			result.put("currency", "CNY");
		}else if(currency!=null && currency.equals("RUB")){
			result.put("currency", "RUB");
		}
		result.put("pricesum", sum);
		result.put("currencyZh", "商品总价");
		result.put("currencyRu", "итого");
		result.put("count", ids.length);
		this.json = JsonMessageUtil.getObjectJson(result);
    	return this.JSON_MESSAGE;
    }
	/**
	 * 获取购物车数据
	 * 
	 * @param 无
	 * @return 返回json串 result 为1表示调用成功0表示失败 data.count：购物车的商品总数,int 型
	 *         data.total:购物车总价，int型
	 * 
	 */
	public String getCartData() {
		String cart=this.getText("cart.carts");
		try {

			String sessionid = ThreadContextHolder.getHttpRequest()
					.getSession().getId();

			Double goodsTotal = cartManager.countGoodsTotal(sessionid);
			int count = this.cartManager.countItemNum(sessionid);

			java.util.Map<String, Object> data = new HashMap();
			data.put("count", count);// 购物车中的商品数量
			data.put("total", goodsTotal);// 总价

			this.json = JsonMessageUtil.getObjectJson(data);

		} catch (Throwable e) {
			HttpSession session = ThreadContextHolder.getHttpRequest()
					.getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();
			this.logger.error("获取购物车数据出错", e);
				this.showErrorJson(cart+"[" + e.getMessage() + "]");
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 添加货品的购物车
	 * 
	 * @param product
	 * @return
	 */
	private boolean addProductToCart(Product product, String language) {
		String sessionid = ThreadContextHolder.getHttpRequest().getSession()
				.getId();
			String sorry=this.getText("cart.sorry");
			String wuwan=this.getText("cart.wuwan");
			String huopin=this.getText("cart.chenggong");
			String chucuo=this.getText("cart.chucuo");
			String noexit=this.getText("cart.noexit");
		if (product != null) {
			try {
				if (product.getEnable_store() < num) {
					throw new RuntimeException("抱歉！您所选选择的货品库存不足。");
				}
				if(min_number > num){
						throw new RuntimeException("商品最小购买量不能低于"+min_number);
				}
				//获取当前页面产品的价格
				Double productPrice = 0.0;
				if (language.equals("zh")) {
					productPrice = product.getPrice();
				} else if (language.equals("ru")) {
					productPrice = product.getPrice_ru();
				}
				//商品最大允许购买的金额。50000美元。当前汇率*50000。以后实现实时汇率查询
				Double productMaxPrice = 0.0;
				if (language.equals("zh")) {
					productMaxPrice = 6.3417 * 50000;//20151013美元对人民币汇率6.3417
				} else if (language.equals("ru")) {
					productMaxPrice = 62.61 * 50000;//20151013美元对人民币汇率62.61
				}
				if(productPrice*num>productMaxPrice){
						throw new RuntimeException(wuwan);
				}
				Cart cart = new Cart();
				cart.setIs_select(0);
				cart.setGoods_id(product.getGoods_id());
				cart.setProduct_id(product.getProduct_id());
				cart.setSession_id(sessionid);
				cart.setNum(num);
				Member member = UserConext.getCurrentMember();
				// 非匿名添加购物车
				if (member != null) {
					cart.setMember_id(member.getMember_id());
				}
				cart.setItemtype(0); // 0为product和产品 ，当初是为了考虑有赠品什么的，可能有别的类型。
				cart.setWeight(product.getWeight());
				if (language.equals("zh")) {
					cart.setPrice(product.getPrice());
					cart.setCurrency("CNY");
				} else if (language.equals("ru")) {
					cart.setPrice(product.getPrice_ru());
					cart.setCurrency("RUB");
				}
				// cart.setPrice( product.getPrice() );
				cart.setName(product.getName());

				this.cartManager.add(cart);
					this.showSuccessJson(huopin);

				// 需要同时显示购物车信息
				if (showCartData == 1) {
					this.getCartData();
				}

				return true;
			} catch (RuntimeException e) {
				this.logger.error("将货品添加至购物车出错", e);
					this.showErrorJson(chucuo+"[" + e.getMessage() + "]");
				return false;
			}

		} else {
				this.showErrorJson(noexit);
			return false;
		}
	}

	/**
	 * 删除购物车一项
	 * 
	 * @param cartid
	 *            :要删除的购物车id,int型,即 CartItem.item_id
	 * 
	 * @return 返回json字串 result 为1表示调用成功0表示失败 message 为提示信息
	 * 
	 *         {@link CartItem }
	 */
	public String delete() {
		try {
			String delete=this.getText("cart.delete");
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String cartid = request.getParameter("cartid");
			cartManager.delete(request.getSession().getId(),
					Integer.valueOf(cartid));
				this.showSuccessJson(delete);

		} catch (RuntimeException e) {
			String gouwuxiang=this.getText("cart.gouwuxiang");
			this.logger.error("删除购物项失败", e);
				this.showErrorJson(gouwuxiang);

		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 更新购物车的数量
	 * 
	 * @param cartid
	 *            :要更新的购物车项id，int型，即 CartItem.item_id
	 * @param num
	 *            :要更新数量,int型
	 * @return 返回json字串 result： 为1表示调用成功0表示失败 int型 store: 此商品的库存 int型
	 */
	public String updateNum() {
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String cartid = request.getParameter("cartid");
			String num = request.getParameter("num");
			num = StringUtil.isEmpty(num) ? "1" : num;// lzf add 20110113
			String productid = request.getParameter("productid");
			Product product = productManager.get(Integer.valueOf(productid));
			Integer store = product.getEnable_store();
			if (store == null)
				store = 0;
			if (store >= Integer.valueOf(num)) {
				cartManager.updateNum(request.getSession().getId(),
						Integer.valueOf(cartid), Integer.valueOf(num));
			}
			json = JsonMessageUtil.getNumberJson("store", store);
		} catch (RuntimeException e) {
			HttpSession session = ThreadContextHolder.getHttpRequest()
					.getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();
			this.logger.error("更新购物车数量出现意外错误", e);
			String gengxin=this.getText("cart.gengxin");
				this.showErrorJson(gengxin + e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 检查商品活动购买限制一个
	 * @return
	 */
   public String checkSkillSecGoods(){
		   try {
			    HttpServletRequest request = ThreadContextHolder.getHttpRequest();
				String goodsid = request.getParameter("check_id");
				String adderess_id=request.getRemoteAddr();
				List<Integer> check=new ArrayList<Integer>();
				List<Integer> ischeck=new ArrayList<Integer>();
				List<Integer> isloginbuy=new ArrayList<Integer>();
				Integer data=0;
				if(!StringUtil.isEmpty(goodsid) && !goodsid.trim().equalsIgnoreCase("")){
					String[] str=goodsid.split(",");
					for (String goods_id : str) {
						List<Map> map=(List<Map>)allActivityManager.checkGoodsIsExists(Integer.parseInt(goods_id));
						 if(map.size()>0){
							 for (Map map2 : map) {
								 check.add((Integer)map2.get("goods_id"));
								 Integer active_id=(Integer)map2.get("allactivity_id");
								 List<CheckMemberLogin> list=this.allActivityManager.isCheck(active_id, adderess_id);
								 if(list.size()>0){
									 for (CheckMemberLogin checkMemberLogin : list) {
										 isloginbuy.add(checkMemberLogin.getGoods_id());
									}
								 }
							}
						 }else{
							 ischeck.add(Integer.parseInt(goods_id));
						 }
					}
					if(str.length==ischeck.size()){
						data=0;
					}else if(check.size()<=str.length && check.size()>1 && isloginbuy.size()==0){
						data=1;
					}else if(isloginbuy.size()>0){
						data=2;
					}
				}
		     this.json=JsonMessageUtil.getNumberJson("data",data);
			
		   } catch (RuntimeException e) {
			this.showErrorJson(e.getMessage());
		}
		   return this.JSON_MESSAGE;
   }
	/**
	 * 购物车的价格总计信息
	 * 
	 * @param 无
	 * @return 返回json字串 result： 为1表示调用成功0表示失败 int型 orderprice: 订单价格，OrderPrice型
	 *         {@link OrderPrice}
	 */
	public String getTotal() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		OrderPrice orderprice = this.cartManager.countPrice(
				cartManager.listGoods(sessionid), null, null);
		this.json = JsonMessageUtil.getObjectJson(orderprice);
		return this.JSON_MESSAGE;
	}

	/**
	 * 清空购物车
	 */

	public String clean() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		try {
			/*HttpSession session = ThreadContextHolder.getHttpRequest()
					.getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();*/
			String qingkong=this.getText("cart.qingkong");
			cartManager.clean(request.getSession().getId());
				this.showSuccessJson(qingkong);
		} catch (RuntimeException e) {
			this.logger.error("清空购物车", e);
			this.showErrorJson(e.getMessage());
		}
		return this.JSON_MESSAGE;
	}

	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	public int getProductid() {
		return productid;
	}

	public void setProductid(int productid) {
		this.productid = productid;
	}

	public IProductManager getProductManager() {
		return productManager;
	}

	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getShowCartData() {
		return showCartData;
	}

	public void setShowCartData(int showCartData) {
		this.showCartData = showCartData;
	}

	public IPromotionManager getPromotionManager() {
		return promotionManager;
	}

	public void setPromotionManager(IPromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public Integer getMin_number() {
		return min_number;
	}

	public void setMin_number(Integer min_number) {
		this.min_number = min_number;
	}
	public AllActivityManager getAllActivityManager() {
		return allActivityManager;
	}
	public void setAllActivityManager(AllActivityManager allActivityManager) {
		this.allActivityManager = allActivityManager;
	}
	
}
