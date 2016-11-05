package com.enation.app.shop.core.action.api;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.tool.LuceneService;
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("goodsLucene")
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class GoodsLuecneApiaction extends WWAction {
	private LuceneService luceneService;
	private IGoodsManager goodsManager;
	private IDaoSupport daoSupport;
	public void addLuceneForGoods(){
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT g.goods_id,g.`name`,g.name_en,g.name_ru,g.market_enable,g.meta_keywords,g.ru_keyword from es_goods g ORDER BY goods_id DESC ");
		List<StoreGoods> goodslist = null;
		goodslist = this.daoSupport.queryForList(sql.toString(),StoreGoods.class);
		if(goodslist!=null && goodslist.size()>0){
			for(StoreGoods g: goodslist){
				//String content = this.luecneContent(g);
				this.luceneService.addindex(g.getGoods_id().toString(), g.getClass().getName(), g, true);
			}
		}
		
	}
	
	public String luecneContent(StoreGoods g){
		StringBuffer content = new StringBuffer();
		//商品ID
		content.append(StringUtils.isNotEmpty(g.getGoods_id().toString()) ? g.getGoods_id() : "").append(" ");
		//商品中文名称
		content.append(StringUtils.isNotEmpty(g.getName()) ? g.getName() : "").append(" ");
		//商品英文名称
		content.append(StringUtils.isNotEmpty(g.getName_en()) ? g.getName_en() : "").append(" ");
		//商品俄文名称
		content.append(StringUtils.isNotEmpty(g.getName_ru()) ? g.getName_ru() : "").append(" ");
		return content.toString();
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
			e.printStackTrace();
		}
		String keyword = ThreadContextHolder.getHttpRequest().getParameter("keyword");
		
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String content =null;
		if(language=="zh"){
			content=this.luceneService.queryGoodsLucene(keyword, "zh");
		}else{
			content=this.luceneService.queryGoodsLucene(keyword, "ru");
		}
		if(content!=null){
			this.json=  JsonMessageUtil.getSuccessJson(content);			
		}else {
			this.json=  JsonMessageUtil.getSuccessJson("");
		}
		return this.JSON_MESSAGE;
	}
	
	public String searcherContentbyguan(){
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String keyword = ThreadContextHolder.getHttpRequest().getParameter("keyword");
		String is_belong = request.getParameter("is_belong");
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String content =null;
		if(language=="zh"){
			content=this.luceneService.queryGoodsLucene(keyword, "zh",is_belong);
		}else{
			content=this.luceneService.queryGoodsLucene(keyword, "ru",is_belong);
		}
		if(content!=null){
			this.json=  JsonMessageUtil.getSuccessJson(content);			
		}else {
			this.json=  JsonMessageUtil.getSuccessJson("");
		}
		return this.JSON_MESSAGE;
	}
	
	public String queryGoodsWithLucene(){
		this.luceneService.queryGoodsLucene("zmm", "zh");
		this.json=  JsonMessageUtil.getSuccessJson("abc");
		return this.JSON_MESSAGE;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public LuceneService getLuceneService() {
		return luceneService;
	}

	public void setLuceneService(LuceneService luceneService) {
		this.luceneService = luceneService;
	}
	
	
	
}
