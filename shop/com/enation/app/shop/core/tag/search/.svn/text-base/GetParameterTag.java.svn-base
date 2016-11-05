package com.enation.app.shop.core.tag.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.component.search.plugin.BrandList;
import com.enation.app.shop.core.model.Brand;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.RequestUtil;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class GetParameterTag extends BaseFreeMarkerTag {
	private IBrandManager brandManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map m=new HashMap();
		Map map=RequestUtil.paramToMap(ThreadContextHolder.getHttpRequest());
	    String brandlist=(String) map.get("brandlist");
	    if(!StringUtil.isEmpty(brandlist) && !brandlist.equals("")){
	    	if(brandlist.equals("1")){
	    		brandlist="龙江物产";
	    	}else if(brandlist.equals("0")){
	    		brandlist="俄罗斯馆";
	    	}else if(brandlist.equals("2")){
	    		brandlist="澳大利亚馆";
	    	}else if(brandlist.equals("3")){
	    		brandlist="新西兰馆";
	    	}else if(brandlist.equals("4")){
	    		brandlist="韩国馆";
	    	}else if(brandlist.equals("5")){
	    		brandlist="德国馆";
	    	}
	    	m.put("brandlist", brandlist);
	    }
	    String price=(String) map.get("price");
	    if(!StringUtil.isEmpty(price) && !price.equals("")){
	    	if(price.equals("_5")){
	    		price="5元以下";
	    	}else if(price.equals("5_10")){
	    		price="5-10元";
	    	}else if(price.equals("10_20")){
	    		price="10-20元";
	    	}else if(price.equals("20_50")){
	    		price="20-50元";
	    	}else if(price.equals("50_100")){
	    		price="50-100元";
	    	}else if(price.equals("100_200")){
	    		price="100-200元";
	    	}else if(price.equals("200_500")){
	    		price="200-500元";
	    	}else if(price.equals("500_1000")){
	    		price="500-1000元";
	    	}else if(price.equals("1000_")){
	    		price="1000元以上";
	    	}
	    	m.put("price", price);
	    }
	    String brand=(String) map.get("brand");
	    if(!StringUtil.isEmpty(brand) && !brand.equals("")){
	    	 List<Brand> list=this.brandManager.list();
	    	 for (Brand brand2 : list) {
				if(brand.equals(brand2.getBrand_id().toString())){
					brand=brand2.getName();
				}
			}
	    	 m.put("brand", brand);
	    }
	   
		return m;
	}
	public IBrandManager getBrandManager() {
		return brandManager;
	}
	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}
}
