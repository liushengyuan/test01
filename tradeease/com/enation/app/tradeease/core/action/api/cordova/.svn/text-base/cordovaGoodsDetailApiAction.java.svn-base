package com.enation.app.tradeease.core.action.api.cordova;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.store.StoreSilde;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberCommentManager;
import com.enation.app.b2b2c.core.service.store.IStoreSildeManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.gallery.model.GoodsGallery;
import com.enation.app.shop.component.gallery.service.IGoodsGalleryManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.Specification;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IMemberCommentManager;
import com.enation.app.shop.core.service.IMemberOrderManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.tradeease.core.service.cordova.IFavoriteManagerApp;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.spring.SpringContextHolder;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * 卖家中心 产品列表
 * 
 * @author yanpeng 2015-6-23 下午6:54:50
 * 
 */

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("cordovaGoodsList")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class cordovaGoodsDetailApiAction extends WWAction {
	private IStoreCartManager storeCartManager;
	private ICartManager cartManager;
	private IGoodsManager goodsManager;
	private IGoodsGalleryManager goodsGalleryManager;
	private IProductManager productManager;
	private IOrderManager orderManager;
	private IMemberOrderManager memberOrderManager;
	private Integer catid;
	private IFavoriteManagerApp favoriteManagerApp;
	private IStoreGoodsManager storeGoodsManager;
	private IStoreSildeManager StoreSildeManager;
	private String store_id;
	private IMemberCommentManager MemberCommentManager;//获取评论详细
	private int goods_id;
	private int pageNo;
	private int pageSizes;
	private int grade;
	private IStoreMemberCommentManager StoreMemberCommentManager;
	HttpServletRequest request = ThreadContextHolder.getHttpRequest();
	String goods_ids = request.getParameter("goods_id");
	String productid = request.getParameter("productid");
	String num = request.getParameter("num");
	String status = request.getParameter("status");
	String keyword = request.getParameter("keyword");
	public String getGoodsGalleryImage(){
		List<GoodsGallery> galleryList = this.getGoodsGalleryManager().list(Integer.parseInt(goods_ids));
		showGridJson(galleryList);
		return this.JSON_MESSAGE;
		
	}
	
	
	
	/**
	 * 
	 * 获取商品基本信息
	 * @return
	 */
	public String getGoodsBaseInfor(){
		String sessionid = ThreadContextHolder.getHttpRequest().getSession()
				.getId();
		Integer goods_id = Integer.parseInt(goods_ids);
			Map goodsMap = getGoodsManager().get(goods_id);

	
			
			if (goodsMap.get("disabled").toString().equals("1")) {
				throw new UrlNotFoundException();
			}

			String intro = (String) goodsMap.get("intro");
			if (!StringUtil.isEmpty(intro)) {
				intro = UploadUtil.replacePath(intro);
				goodsMap.put("intro", intro);
			}
			intro = intro.replace("\"429\"", "100%");
			intro= intro.replace("<img", "<img width=100%");
		
			goodsMap.put("intro", intro);
		List<Map> goodsList = new ArrayList<Map>();
		goodsList.add(goodsMap);
		showGridJson(goodsList);
		return this.JSON_MESSAGE;
			
	}
	public String getStoreGood(){
		Member member = UserConext.getCurrentMember();
		int count = this.favoriteManagerApp.getCount(Integer.parseInt(goods_ids),member.getMember_id());
		showSuccessJson(count+"");
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 判断商品是否被收藏
	 * */
	public String getCountFavorite(){
		Member member = UserConext.getCurrentMember();
		int count = this.favoriteManagerApp.getCount(Integer.parseInt(goods_ids),member.getMember_id());
		showSuccessJson(count+"");
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 
	 * 获取商品规格list
	 * @return
	 */
	public String getGoodsSpecList(){
		
		try {
			List<Product> productList  = this.productManager.list(Integer.parseInt(goods_ids));
			List<Specification> specList = this.productManager.listSpecs(Integer.parseInt(goods_ids));
			Map map = new HashMap();
			map.put("productList", productList);
			map.put("specList", specList);
			this.json = JsonMessageUtil.getObjectJson(map);
		} catch (NumberFormatException e) {
			this.showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
		
	}
	
	/**
	 * 
	 * 添加货品至购物车
	 * @return
	 */
	public String addProductToCart(){
		String language = "zh";
		Product product = null;
		if(!productid.equals("")){
		product = this.productManager.get(Integer.parseInt(productid));
		}
		String sessionid = ThreadContextHolder.getHttpRequest().getSession()
				.getId();

		if (product != null) {
			try {
				if (product.getEnable_store() < Integer.parseInt(num)) {
					throw new RuntimeException("抱歉！您所选选择的货品库存不足。");
				}
				Cart cart = new Cart();
				cart.setGoods_id(product.getGoods_id());
				cart.setProduct_id(product.getProduct_id());
				cart.setSession_id(sessionid);
				cart.setNum(Integer.parseInt(num));
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

				this.getCartManager().add(cart);
				if (language.equals("zh")) {
					this.showSuccessJson("货品成功添加到购物车");
				} else if (language.equals("ru")) {
					this.showSuccessJson("Пункт успешно добавлен в корзину");
				}

			} catch (RuntimeException e) {
				this.logger.error("将货品添加至购物车出错", e);
				if (language.equals("zh")) {
					this.showErrorJson("将货品添加至购物车出错[" + e.getMessage() + "]");
				} else if (language.equals("ru")) {
					this.showErrorJson("Добавление элементов в корзину ошибке["
							+ e.getMessage() + "]");
				}
				return this.JSON_MESSAGE;
			}

		} else {
			if (language.equals("zh")) {
				this.showErrorJson("该货品不存在，请选择商品规格！");
			} else if (language.equals("ru")) {
				this.showErrorJson("Товар не существует, Выберите спецификации продукта！");
			}
			return this.JSON_MESSAGE;
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 
	 * 获取购物车商品列表 所有
	 * @return
	 */
	public String getCartGoodsList(){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		cartManager = SpringContextHolder.getBean("cartManager");
		String sessionid = request.getSession().getId();
		List goodsList = cartManager.listGoodsForApp( sessionid ); //商品列表
		showGridJson(goodsList);
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 获取购物车商品列表根据商店id（lsy）
	 * @return
	 */
	public String getCartGoodsList2(){
		List<Map> storeGoodsList= new ArrayList<Map>();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		storeGoodsList=storeCartManager.storeListGoods(sessionid);
		
		showGridJson(storeGoodsList);
		return this.JSON_MESSAGE;
		
		
	}
	/**
	 * 
	 * 获取订单list
	 * @param page pageSize status keyword
	 * @return
	 */
	public String getOrderList(){
		
		Page ordersPage = memberOrderManager.pageOrders(Integer.valueOf(page), pageSize);
		
		showGridJson(ordersPage);
		return this.JSON_MESSAGE;
		
	}
	
	/**
	 * 
	 * 获取订单明细list
	 * @param page pageSize status keyword
	 * @return
	 */
	public String getOrderItemList(){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		int order_id = SpringContextHolder.getBean("order_id");
		List<Map> itemList=orderManager.getItemsByOrderid(order_id);
		
		
		showGridJson(itemList);
		return this.JSON_MESSAGE;
		
	}
	//获得相似的商品
	public String getLikeGoods(){
		List<Goods> list=this.goodsManager.otherLookGoods(catid);
		if(list!=null){
			for(Goods a:list){
				if(a.getThumbnail()!=null){
					a.setThumbnail(UploadUtil.replacePath(a.getThumbnail()));
				}
				if(a.getBig()!=null){
					a.setBig(UploadUtil.replacePath(a.getBig()));
				}
				if(a.getSmall()!=null){
					a.setSmall(UploadUtil.replacePath(a.getSmall()));
				}
				if(a.getOriginal()!=null){
					a.setOriginal(UploadUtil.replacePath(a.getOriginal()));
				}
			}
		}
		this.showGridJson(list);
		return this.JSON_MESSAGE;
	}
	//获取店铺的商品列表
	public String getStoreGoods(){
		Map map = new HashMap();
		map.put("store_id", store_id);
		map.put("disable", "0");
		map.put("market_enable", "1");
		Page webPage = this.storeGoodsManager.storeGoodsList(page,pageSize,map);
		Object goods_list_obj = null;
		goods_list_obj = webPage.getResult();
		List<Map> goods_list = (List<Map>) goods_list_obj;
		
		for(Map a:goods_list){
			if(a.get("thumbnail")!=null){
				a.put("thumbnail",UploadUtil.replacePath(a.get("thumbnail").toString()));
			}
			if(a.get("big")!=null){
				a.put("big",UploadUtil.replacePath(a.get("big").toString()));
			}
		}
		showGridJson(goods_list);
		return this.JSON_MESSAGE;
	}
	/**
	 * 获得店铺的详细数据
	 * @param store_id 店铺id
	 * @return
	 */
	public String storeEdit(){
		try {
			Integer store=Integer.parseInt(store_id);
			List<StoreSilde> list3=StoreSildeManager.list(store);
			for(StoreSilde aStoreSlide:list3){
				aStoreSlide.setImg(UploadUtil.replacePath(aStoreSlide.getImg()));
			}
			this.showGridJson(list3);
		} catch (NumberFormatException e) {
			this.showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取评论（lsy）
	 * @return
	 */
	public String getCommment(){
		try {
			Page page=this.MemberCommentManager.getGoodsCommentsByGrade(goods_id, pageNo, pageSizes, 1,grade);
			this.showGridJson(page);
			int goodNum = this.MemberCommentManager.commentNum(goods_id, 3);
			int mediumNum = this.MemberCommentManager.commentNum(goods_id, 2);
			int poorNum = this.MemberCommentManager.commentNum(goods_id, 1);
			this.json = this.json.substring(0,this.json.length()-1)+",\"goodNum\":"+goodNum+",\"mediumNum\":"+mediumNum+",\"poorNum\":"+poorNum+"}";
		} catch (Exception e) {
			this.showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取商品总分数（lsy）
	 * @return
	 */
	public String getGoodsGrade(){
		//System.out.println(goods_id);
		try {
			double grade= this.StoreMemberCommentManager.getGoodsStore_desccredit(goods_id);
			this.showErrorJson(""+grade);
		} catch (Exception e) {
			this.showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		
		return this.JSON_MESSAGE;
	}
	public int getGoods_id() {
		return goods_id;
	}



	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}



	public IMemberCommentManager getMemberCommentManager() {
		return MemberCommentManager;
	}



	public void setMemberCommentManager(IMemberCommentManager memberCommentManager) {
		MemberCommentManager = memberCommentManager;
	}



	public IGoodsGalleryManager getGoodsGalleryManager() {
		return goodsGalleryManager;
	}
	public void setGoodsGalleryManager(IGoodsGalleryManager goodsGalleryManager) {
		this.goodsGalleryManager = goodsGalleryManager;
	}



	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}



	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}



	public IProductManager getProductManager() {
		return productManager;
	}



	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}



	public ICartManager getCartManager() {
		return cartManager;
	}



	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}



	public IOrderManager getOrderManager() {
		return orderManager;
	}



	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}



	public IMemberOrderManager getMemberOrderManager() {
		return memberOrderManager;
	}



	public void setMemberOrderManager(IMemberOrderManager memberOrderManager) {
		this.memberOrderManager = memberOrderManager;
	}



	public Integer getCatid() {
		return catid;
	}



	public void setCatid(Integer catid) {
		this.catid = catid;
	}



	public IFavoriteManagerApp getFavoriteManagerApp() {
		return favoriteManagerApp;
	}



	public void setFavoriteManagerApp(IFavoriteManagerApp favoriteManagerApp) {
		this.favoriteManagerApp = favoriteManagerApp;
	}



	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}



	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}



	public String getStore_id() {
		return store_id;
	}



	public void setStore_id(String store_id) {
		this.store_id = store_id;
	}



	public IStoreSildeManager getStoreSildeManager() {
		return StoreSildeManager;
	}



	public void setStoreSildeManager(IStoreSildeManager storeSildeManager) {
		StoreSildeManager = storeSildeManager;
	}



	public IStoreMemberCommentManager getStoreMemberCommentManager() {
		return StoreMemberCommentManager;
	}



	public void setStoreMemberCommentManager(
			IStoreMemberCommentManager storeMemberCommentManager) {
		StoreMemberCommentManager = storeMemberCommentManager;
	}



	public int getPageSizes() {
		return pageSizes;
	}



	public void setPageSizes(int pageSizes) {
		this.pageSizes = pageSizes;
	}



	public int getGrade() {
		return grade;
	}



	public void setGrade(int grade) {
		this.grade = grade;
	}



	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}



	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}
	
	
	
}

