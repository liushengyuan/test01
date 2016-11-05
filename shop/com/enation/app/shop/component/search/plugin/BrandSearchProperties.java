package com.enation.app.shop.component.search.plugin;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.plugin.search.IGoodsSearchFilter;
import com.enation.app.shop.core.utils.BrandUrlUtils;
import com.enation.app.shop.core.utils.BrandUtil;
import com.enation.app.shop.core.utils.PriceUrlUtils;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.StringUtil;
@Component
public class BrandSearchProperties extends AutoRegisterPlugin implements
IGoodsSearchFilter{
	private static Map<String,List<BrandList>> brandMap;
	@Override
	public void createSelectorList(Map map, Cat cat) {
		BrandUtil.createAndPut(map);
		
	}

	@Override
	public void filter(StringBuffer sql, Cat cat) {
        HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String urlFragment = request.getParameter("brandlist");
		if(!StringUtil.isEmpty( urlFragment ) ){
				sql.append(" and  g.is_belong="+urlFragment);				
		}
	}
}
