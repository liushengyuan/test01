package com.enation.app.shop.component.search.plugin;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.plugin.search.IGoodsSearchFilter;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.StringUtil;

/**
 * 市场投放国过滤器
 * 
 * @author WKZ
 * @date 2015-9-15 下午7:08:32
 */
@Component
public class ShopMarketSearchFilter extends AutoRegisterPlugin implements
		IGoodsSearchFilter {

	public void filter(StringBuffer sql, Cat cat) {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = request.getSession();
		
		Locale locale = (Locale) session.getAttribute("locale");
		String language = null;
		if (locale != null) {
			language = locale.getLanguage();
		}
		String tempSql = " AND (SELECT store_market FROM es_store WHERE store_id = g.store_id ) = ";
		if(!StringUtil.isEmpty(language)) {
			if("ru".equals(language)) {
				sql.append(tempSql + " 'RUS' ");
			} else if("zh".equals(language)) {
				sql.append(tempSql + " 'CHN' " );
			}
		}
	}

	@Override
	public void createSelectorList(Map map, Cat cat) {

	}

	public String getAuthor() {
		return "WKZ";
	}

	public String getId() {
		return "shopMarketSearchFilter";
	}

	public String getName() {
		return "市场投放国过虑器";
	}

	public String getType() {
		return "goodssearch";
	}

	public String getVersion() {
		return "1.0";
	}

	public void perform(Object... params) {
	}

	public void register() {
	}

}
