package com.enation.app.tradeease.core.action.api.cordova;


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

import com.enation.app.base.core.model.AdColumn;
import com.enation.app.base.core.model.Adv;
import com.enation.app.base.core.service.IAdColumnManager;
import com.enation.app.base.core.service.IAdvManager;
import com.enation.app.shop.component.gallery.model.GoodsGallery;
import com.enation.app.shop.component.gallery.service.IGoodsGalleryManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.Team;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IGoodsSearchManager;
import com.enation.app.shop.core.service.IMemberCommentManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.service.ITeamManager;
import com.enation.app.shop.core.service.SearchEngineFactory;
import com.enation.app.shop.core.service.impl.CartManager;
import com.enation.app.shop.core.utils.SortContainer;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

/**
 * Cordova first page interface
 * 
 * @author SunXinqiang
 * 
 */

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("cordovaBestseller")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class cordovaBestsellerApiAction extends WWAction{
	private IGoodsManager goodsManager;
	private IAdColumnManager adColumnManager;
	private IAdvManager advManager;
	private ITeamManager teamManager;
	private int pid;
	public IMemberCommentManager getMemberCommentManager() {
		return MemberCommentManager;
	}
	public void setMemberCommentManager(IMemberCommentManager memberCommentManager) {
		MemberCommentManager = memberCommentManager;
	}
	private IGoodsGalleryManager goodsGalleryManager;
	private IProductManager ProductManager;
	private CartManager cartManager;
	private IMemberCommentManager MemberCommentManager;//获取评论详细
	private IGoodsSearchManager goodsSearchManager = SearchEngineFactory.getSearchEngine();
	HttpServletRequest request = ThreadContextHolder.getHttpRequest();
	private IBrandManager brandManager;
	String catid ="";
	String tagid = request.getParameter("tagid");
	String goodsnum =request.getParameter("goodsnum");
	String acid = request.getParameter("acid");
	String market = request.getParameter("market");
	String goods_ids = request.getParameter("goods_id");
	String pageSize = request.getParameter("pageSize");
	String productid = request.getParameter("productid");
	String num = request.getParameter("num");
	
	public String getBestSeller(){
		List goodsList  = goodsManager.listGoods(catid,tagid,goodsnum);
		List<Map> list = goodsList;
		
		for(Map a:list){
			if(a.get("thumbnail")!=null){
				a.put("thumbnail",UploadUtil.replacePath(a.get("thumbnail").toString()));
			}
			if(a.get("big")!=null){
				a.put("big",UploadUtil.replacePath(a.get("big").toString()));
			}
		}
		
		showGridJson(goodsList);
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 获取轮播商品
	 * @return
	 */
	public String getAdv(){
		//设置语言为汉语
		HttpSession session = ServletActionContext.getRequest().getSession();
		Locale locale = new Locale("zh","CN");
		session.setAttribute("locale", locale);
		
		AdColumn adDetails = getAdColumnManager().getADcolumnDetail(Long.valueOf(acid));
		List<Adv> advList = null;
		
		if( adDetails!=null){
			advList = getAdvManager().listAdv(Long.valueOf(acid));
		}
		advList = advList == null ? new ArrayList<Adv>():advList;
		showGridJson(advList);
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取新品上架list大图
	 * @return
	 */
	public String getTeamList(){
		List teamList=getTeamManager().list(Integer.valueOf(market));
		showGridJson(teamList);
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 获取新品上架list商品
	 * @return
	 */
	public String getTeamProductList(){
		List teamProductList=teamManager.productListForApp();
		showGridJson(teamProductList);
		return this.JSON_MESSAGE;
	}
	/**
	 * 新品上架带有好评率(lsy)
	 * @return
	 */
	public String getTeamProductListByComment(){
		List<Map> teamProductList=teamManager.productListForApp();
		for (Map tean : teamProductList) {
			 pid=Integer.parseInt(tean.get("product_id").toString());
			 int goodsnum=Integer.parseInt(this.getGoodsNum());
			 tean.put("good_grade",goodsnum);
		}
		showGridJson(teamProductList);
		return this.JSON_MESSAGE;
	}
	/**
	 * 获得好评率lsy
	 * @return
	 */
	public String getGoodsNum(){
		int goodNum = this.MemberCommentManager.commentNum(pid, 3);
		int mediumNum = this.MemberCommentManager.commentNum(pid, 2);
		int poorNum = this.MemberCommentManager.commentNum(pid, 1);
		int total = goodNum+mediumNum+poorNum;
		if(total==0){
			return "100";
		}
		String goodsnum=(goodNum/(goodNum+mediumNum+poorNum))*100+"";
		return goodsnum;
	}
	/**
	 * 根据product_id获取商品详细
	 * @return
	 */
	public String getGoodsDetailByProductId(){
		Product p = new Product();
				p = this.ProductManager.get(10071);
//		Product p = this.ProductManager.get(Integer.parseInt(product_id));
		int goods_id = p.getGoods_id();
		List<GoodsGallery> galleryList = this.getGoodsGalleryManager().list(goods_id);
		showGridJson(galleryList);
		return this.JSON_MESSAGE;
	}
	/**
	 * 
	 * 获取商品基本信息
	 * @return
	 */
	public String getGoodsBaseInfor(){
		Integer goods_id = Integer.parseInt(goods_ids);
			Map goodsMap = goodsManager.get(goods_id);

//			String intro = goodsMap.get("intro");
			
			if (goodsMap.get("disabled").toString().equals("1")) {
				throw new UrlNotFoundException();
			}

			String intro = (String) goodsMap.get("intro");
			if (!StringUtil.isEmpty(intro)) {
				intro = UploadUtil.replacePath(intro);
				goodsMap.put("intro", intro);
			}
		List<Map> goodsList = new ArrayList<Map>();
		goodsList.add(goodsMap);
		showGridJson(goodsList);
		return this.JSON_MESSAGE;
			
	}
	/**
	 * 
	 * 商品列表
	 * @return
	 */
	public String getGoodsListCat(){
		SortContainer.getSortList();
		HttpSession session = ServletActionContext.getRequest().getSession(); //创建
		Locale locale = new Locale("zh","CN");
		session.setAttribute("locale",locale);
		int page=Integer.parseInt(request.getParameter("page"));//使支持？号传递
		Page webPage  =  this.goodsSearchManager.search(page, Integer.parseInt(pageSize));
		//webPage.setCurrentPageNo(page);
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
			String goodsIdTemp = a.get("goods_id").toString();
			pid = Integer.parseInt(goodsIdTemp);
			String goodGrade = getGoodsNum();
			a.put("good_grade", goodGrade);
		}
		showGridJson(goods_list);
		return this.JSON_MESSAGE;
		
	}
	/**
	 * 
	 * 添加货品至购物车
	 * @return
	 */
	public String addProductToCart(){
		String language = "zh";
		Product product = this.ProductManager.get(Integer.parseInt(productid));
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

				this.cartManager.add(cart);
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
	 * 获取品牌列表
	 * @param tagid String
	 * @param goodsnum String
	 * @return
	 */
	public String getBrandList(){
		try{
			List<Map> list = this.brandManager.listGoods(tagid, goodsnum);
			for(Map a:list){
				if(a.get("rel_logo")!=null){
					a.put("rel_logo",UploadUtil.replacePath(a.get("rel_logo").toString()));
				}
			}
			this.json = JsonMessageUtil.getListJson(list);
		}catch(Exception e){
			this.showErrorJson(e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 获取最新上架商品整合上面两个接口
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public String getTeamProductListNeed(){
		try{
			List<Team> teamList = getTeamManager().list(Integer.valueOf(market));
			List<Map> teamProductList=teamManager.productListForApp();
			List<Map> listTeam = new ArrayList();
			for(Team t:teamList){
				Map tempMap = new HashMap();
				List tempList = new ArrayList();
				tempMap.put("Team", t);
				for(Map m:teamProductList){
					if(t!=null&&t.getTeam_id()==Integer.parseInt(m.get("team_id").toString())){
						pid=Integer.parseInt(m.get("product_id").toString());
						int goodsnum=Integer.parseInt(this.getGoodsNum());
						m.put("good_grade",goodsnum);
						tempList.add(m);
					}
				}
				tempMap.put("productList", tempList);
				listTeam.add(tempMap);
			}
			this.json = JsonMessageUtil.getListJson(listTeam);
		}catch(Exception e){
			this.showErrorJson(e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	public IAdColumnManager getAdColumnManager() {
		return adColumnManager;
	}
	public void setAdColumnManager(IAdColumnManager adColumnManager) {
		this.adColumnManager = adColumnManager;
	}
	public IAdvManager getAdvManager() {
		return advManager;
	}
	public void setAdvManager(IAdvManager advManager) {
		this.advManager = advManager;
	}
	public ITeamManager getTeamManager() {
		return teamManager;
	}
	public void setTeamManager(ITeamManager teamManager) {
		this.teamManager = teamManager;
	}
	public IGoodsGalleryManager getGoodsGalleryManager() {
		return goodsGalleryManager;
	}
	public void setGoodsGalleryManager(IGoodsGalleryManager goodsGalleryManager) {
		this.goodsGalleryManager = goodsGalleryManager;
	}
	public IProductManager getProductManager() {
		return ProductManager;
	}
	public void setProductManager(IProductManager productManager) {
		ProductManager = productManager;
	}
	public IGoodsSearchManager getGoodsSearchManager() {
		return goodsSearchManager;
	}
	public void setGoodsSearchManager(IGoodsSearchManager goodsSearchManager) {
		this.goodsSearchManager = goodsSearchManager;
	}
	public CartManager getCartManager() {
		return cartManager;
	}
	public void setCartManager(CartManager cartManager) {
		this.cartManager = cartManager;
	}
	public IBrandManager getBrandManager() {
		return brandManager;
	}
	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}
}
