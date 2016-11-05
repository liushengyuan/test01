package com.enation.app.shop.component.search.plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.plugin.search.IGoodsSearchFilter;
import com.enation.app.shop.core.plugin.search.IPutWidgetParamsEvent;
import com.enation.app.shop.core.plugin.search.SearchSelector;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.StringUtil;

/**
 * 关键字搜索过滤器
 * @author kingapex
 *
 */
@Component
public class KeywordSearchFilter extends AutoRegisterPlugin implements
		IGoodsSearchFilter {
	

	@Override
	public void createSelectorList(Map mainmap ,Cat cat) {
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String keyword  = request.getParameter("keyword");
		if(!StringUtil.isEmpty(keyword)){
			mainmap.put("selected_keyword", keyword);
		}else{
			mainmap.put("selected_keyword", "");
		}
	}
	
 
	public void filter(StringBuffer sql, Cat cat) {
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String keyword =  request.getParameter("keyword");
		if (!StringUtil.isEmpty(keyword)) {
			keyword = keyword.replaceAll("-xie-", "/").trim();
			keyword = keyword.replaceAll("%", "").trim();
			keyword = keyword.replaceAll("\'", "").trim();
			
			String[] keys = StringUtils.split(keyword,"\\s");
			
			if(keyword.equals("tradediscount")){
				//只查询打折的商品
				sql.append(" AND g.is_discount = 1 ");
			}else {
				for(String key:keys){ 
					sql.append(" and (g.name like '%");
					sql.append(key);
					sql.append("%'");
					sql.append(" or g.meta_keywords like '%");//添加关键字的搜索
					sql.append(key);
					sql.append("%') and (g.market_enable=1");
					sql.append(" or g.name_ru like '%");//添加关键字的搜索
					sql.append(key);
					sql.append("%') and (g.market_enable=1");
					if(language=="zh"){
					  	sql.append(" or g.cat_id in (SELECT gc.cat_id from es_goods_cat gc where gc.name like '%");
						sql.append(key);
						sql.append("%'))");
					}else{
					 	sql.append(" or g.cat_id in (SELECT gc.cat_id from es_goods_cat gc where gc.name_ru like '%");
						sql.append(key);
						sql.append("%'))");
					}
				}
			}
			
		 
		}
	}
	
	public static void main(String [] args){
		String keyword = "测试的  123   0000   11   22";
		String[] keys = StringUtils.split(keyword," ");
		//String[] keys  = keyword.split("\\s");
		for(String key:keys){
			//System.out.println(key);
		}
	}
	
	
	public String getFilterId() {
		
		return "keyword";
	}

	
	public String getAuthor() {
		
		return "kingapex";
	}

	
	public String getId() {
		
		return "keywordSearchFilter";
	}

	
	public String getName() {
		
		return "关键字搜索过滤器";
	}

	
	public String getType() {
		
		return "searchFilter";
	}

	
	public String getVersion() {
		
		return "1.0";
	}

	
	public void perform(Object... params) {
		

	}
	public void register() {
		

	}



}
