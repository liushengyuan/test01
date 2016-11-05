package com.enation.app.shop.core.action.api;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.dom4j.Element;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.BankName;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.JsonMessageUtil;

/**
 * 商品api
 * @author kingapex
 *2013-8-20下午8:17:14
 */


@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("goods")
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class GoodsApiAction extends WWAction {
	private IProductManager productManager;
	private IGoodsManager goodsManager;
	private int goodsid;
	
	private IDaoSupport daoSupport;
	
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public int getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(int goodsid) {
		this.goodsid = goodsid;
	}

	private Integer catid;
	private String keyword;
	private Integer brandid;
	private List<Goods> goodslist;
	private Map goodsMap;
	
	/**
	 * 搜索商品
	 * 输入参数：
	 * @param catid ：分类id,如果不输入，则搜索全部的分类下的商品
	 * @param brandid:品牌id，如果不佃入，是搜索全部的品牌下的商品
	 * @param keyword：搜索关键字，会搜索商品名称和商品编号
	 * @return 商品搜索结果 
	 * {@link Goods}
	 */
	public String search(){ 
		goodsMap=new HashMap();
		
		goodsMap.put("catid", catid);
		goodsMap.put("brandid", brandid);
		goodsMap.put("keyword", keyword);
		goodsMap.put("stype", 0);
		
		goodslist =goodsManager.searchGoods(goodsMap);
		this.json = JsonMessageUtil.getListJson(goodslist);
		return JSON_MESSAGE;
	}
	
	
	public String productList(){
		/**
		 *  {'product_id':${product.product_id},'goods_id':${product.goods_id},'sn':'${product.sn}','store':${product.store!0},
		'price':${product.price},
	   'specs':${product.specsvIdJson}
	  }
		 */
		try {
			List<Product> productList  = this.productManager.list(goodsid);
			StringBuffer str  = new StringBuffer();
			for (Product product : productList) {
				if(str.length()!=0){str.append(",");}
				str.append("{\"product_id\":"+product.getProduct_id()+",");
				str.append("\"goods_id\":"+product.getGoods_id()+",");
				str.append("\"sn\":\""+product.getSn()+"\",");
				str.append("\"store\":"+product.getStore()+",");
				str.append("\"enable_store\":"+product.getEnable_store()+",");
				str.append("\"price\":"+product.getPrice()+",");
				str.append("\"specs\":"+product.getSpecsvIdJson()+"}");
			 
			}
			this.json=  "{\"result\":1,\"data\":["+str+"]}";
			
			
		} catch (Exception e) {
			this.logger.error("获取产品列表出错", e);
			this.showErrorJson("获取产品列表出错");
		}
		
		
		
		return this.JSON_MESSAGE;
	}
	/**
	 * 搜素提示内容
	 * @return
	 */
	public String searcherContent(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String keyword = ThreadContextHolder.getHttpRequest().getParameter("keyword");
		
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String sql ="";
		if(language=="zh"){
			sql = "select  Group_concat(g.name  SEPARATOR ',')   from es_goods  g where   name like '%"+keyword+"%' ";
		}else{
			sql = "select  Group_concat(g.name_ru  SEPARATOR ',')   from es_goods  g where   name_ru like '%"+keyword+"%' ";
		}
		 
		////System.out.println(sql);
		//this.json=  "{\"result\":1,\"data\":["+this.daoSupport.queryForString(sql)+"]}";
		String content =this.daoSupport.queryForString(sql); 
		if(content!=null){
			this.json=  JsonMessageUtil.getSuccessJson(content);			
		}else {
			this.json=  JsonMessageUtil.getSuccessJson("");
		}
		//String aaaString = this.JSON_MESSAGE;
		//String data = this.daoSupport.queryForString(sql);
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 加载银行的名称
	 * @return
	 */
	public String loadBankTotalName(){
		String sql = "select * from es_bankname ";
		List bankNames = this.daoSupport.queryForList(sql);
		this.json = JsonMessageUtil.getListJson(bankNames);
		return this.JSON_MESSAGE;
	}
	/**
	 * 加载银行的所在省的信息
	 * @return
	 */
	public String loadBankProvince(){
		//String p1 = ThreadContextHolder.getHttpRequest().getParameter("p1");
		String sql = "select * from es_areacode where P1 = 0";
		List bankProvince = this.daoSupport.queryForList(sql);
		this.json = JsonMessageUtil.getListJson(bankProvince);
		return this.JSON_MESSAGE;
	}
	/**
	 * 加载银行的所在市的信息
	 * @return
	 */
	public String loadBankCity(){
		String p1 = ThreadContextHolder.getHttpRequest().getParameter("p1");
		int member = Integer.parseInt(p1);
		String sql = "select * from es_areacode where P1 = " + member;
		List bankCity = this.daoSupport.queryForList(sql);
		this.json = JsonMessageUtil.getListJson(bankCity);
		return this.JSON_MESSAGE;
	}
	/**
	 * 加载银行的支行信息
	 * @return
	 */
	public String loadBankName(){
		String totalname = ThreadContextHolder.getHttpRequest().getParameter("totalname");
		String areaid = ThreadContextHolder.getHttpRequest().getParameter("areaid");
		int bank = Integer.parseInt(totalname);
		int areacode_id = Integer.parseInt(areaid);
		String sql = "select * from es_bankcode where BANK ="+bank+" and AREACODE_ID ="+areacode_id;
		////System.out.println(sql);
		List bankName = this.daoSupport.queryForList(sql);
		this.json = JsonMessageUtil.getListJson(bankName);
		return this.JSON_MESSAGE;
	}
	/*
	 * 三级联动，回显数据，返回已经储存的银行信息
	 * @return
	 */
	public String loadBankMessage(){
		String member_id = ThreadContextHolder.getHttpRequest().getParameter("member_id");
		Integer id = Integer.parseInt(member_id);
		String sql = "select * from es_store where member_id = "+id;
		List bankMessage = this.daoSupport.queryForList(sql);
		this.json = JsonMessageUtil.getListJson(bankMessage);
		return this.JSON_MESSAGE;
	}

	
	
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public Integer getCatid() {
		return catid;
	}

	public void setCatid(Integer catid) {
		this.catid = catid;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getBrandid() {
		return brandid;
	}

	public void setBrandid(Integer brandid) {
		this.brandid = brandid;
	}

	public List<Goods> getGoodslist() {
		return goodslist;
	}

	public void setGoodslist(List<Goods> goodslist) {
		this.goodslist = goodslist;
	}

	public Map getGoodsMap() {
		return goodsMap;
	}

	public void setGoodsMap(Map goodsMap) {
		this.goodsMap = goodsMap;
	}

	public IProductManager getProductManager() {
		return productManager;
	}

	public void setProductManager(IProductManager productManager) {
		this.productManager = productManager;
	}
	
	

}
