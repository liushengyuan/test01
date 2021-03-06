package com.enation.app.tradeease.core.action.api.cordova;

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
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.model.Cart;
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
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.app.tradeease.core.service.cordova.IOrderManagerApp;
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
@Namespace("/api/cordova")
@Action("cordovaCart")
public class cordovaCartApiAction extends WWAction {
	private ICartManager cartManager;
	private IPromotionManager promotionManager;
	private IExchangeManager exchangeManager;
	private IOrderManagerApp orderManagerApp;
	private int goodsid;
	private int productid;
	private int num;// 要向购物车中活加的货品数量
	private IProductManager productManager;
	private IStoreMemberManager storeMemberManager;
	private ILogiManager logiManager;
	private IMemberManager memberManager;
	private IMemberLvManager memberLvManager;
	private Integer isSelect;
	private Integer goods_id;
	private Integer order_id;
	public IExchangeManager getExchangeManager() {
		return exchangeManager;
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
	private Integer enableNum = 0;//已选商品数量和购物车中该商品数量
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
		String language = "zh";
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Integer freight_id = 0;
		if("".equals(request.getParameter("freight_id"))||request.getParameter("freight_id")==null||request.getParameter("freight_id")=="null"){
		}else{
			 freight_id=Integer.parseInt(request.getParameter("freight_id"));
		}
		Double sendprice=0.0;
		if(freight_id==0){
			sendprice=0.0;
		}else{
			sendprice=Double.parseDouble(request.getParameter("sendprice"));
		}
		Integer store_id= Integer.parseInt(request.getParameter("store_id"));
		HttpSession session = ServletActionContext.getRequest().getSession();
		String sessionid = session.getId();
		int count = this.cartManager.countItemNum(sessionid);
		List<CartItem> cartItems = cartManager.listGoods(sessionid);
//		Locale locale = (Locale) session.getAttribute("locale");
//		String language = locale.getLanguage();
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
		List<StoreCartItem> listcart= cartManager.listGoods3(sessionid,this.goodsid);
		Integer tepmNum = 1;
		if(listcart.size()>0){
			for (StoreCartItem storecart : listcart) {
				if(productid!=storecart.getProduct_id()){
				}else{
					tepmNum = num+storecart.getNum();
				}
			}
		}else{
			tepmNum = num;
		}
		if (language=="zh") {
			curCurrency = "CNY";
			goodLogisDetail.setSendprice(sendprice*tepmNum);
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
		if (count == 0) {
			enableNum = num;
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
					if(freight_id==0){
						this.logiManager.addGoodLogisDetail(goodLogisDetail);
					}else{
						if(productid!=storecart.getProduct_id()){
							count1++;
						}else{
							enableNum = num+storecart.getNum();
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
			String noexit = "请选择规格";
		if (product != null) {
			try {
				if (product.getEnable_store() < enableNum) {
					throw new RuntimeException("库存不足");
				}
				if(min_number > num){
						throw new RuntimeException("最小购买量"+min_number);
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
				cart.setGoods_id(product.getGoods_id());
				cart.setProduct_id(product.getProduct_id());
				cart.setSession_id(sessionid);
				cart.setNum(num);
				cart.setIs_select(1);
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
				this.logger.error("添加失败", e);
					this.showErrorJson("[" + e.getMessage() + "]");
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
			cartManager.deleteForApp(request.getSession().getId(),
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
				json = JsonMessageUtil.getNumberJson("store", store);
			}else{
				this.json = JsonMessageUtil.getErrorJson("超出库存");
			}
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
	public String updateItemSelect(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		try {
			if(isSelect==0){
				this.cartManager.updateCartByDisselect(sessionid,goods_id);
			}else{
				this.cartManager.updateCartByIsselect(sessionid,goods_id);
			}
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.showErrorJson(e.getMessage().toString());
		}
		return this.JSON_MESSAGE;
	}
	public String sellectAll(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		try {
			if(isSelect==0){
				this.cartManager.deleteCartByIsselect(sessionid);
			}else{
				this.cartManager.selectCartByIsselect(sessionid);
			}
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.showErrorJson(e.getMessage().toString());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 更新子订单sn
	 * @param order_id 子订单id
	 * @return 新订单sn
	 */
	public String updateOrderSn(){
		try{
			String sn = this.orderManagerApp.updateOrderSn(order_id);
			this.showSuccessJson(sn);
		}catch(Exception e){
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

	public Integer getIsSelect() {
		return isSelect;
	}

	public void setIsSelect(Integer isSelect) {
		this.isSelect = isSelect;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public IOrderManagerApp getOrderManagerApp() {
		return orderManagerApp;
	}

	public void setOrderManagerApp(IOrderManagerApp orderManagerApp) {
		this.orderManagerApp = orderManagerApp;
	}

	public Integer getOrder_id() {
		return order_id;
	}

	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}
	
}
