package com.enation.app.shop.core.tag;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IAllActivityProductManager;
import com.enation.app.shop.core.service.impl.AllActivityManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ActivityKillSecTag extends BaseFreeMarkerTag {
	private IAllActivityProductManager allActivityProductManager;
	private AllActivityManager allActivityManager;
	public IAllActivityProductManager getAllActivityProductManager() {
		return allActivityProductManager;
	}
	public void setAllActivityProductManager(
			IAllActivityProductManager allActivityProductManager) {
		this.allActivityProductManager = allActivityProductManager;
	}
	public AllActivityManager getAllActivityManager() {
		return allActivityManager;
	}
	public void setAllActivityManager(AllActivityManager allActivityManager) {
		this.allActivityManager = allActivityManager;
	}
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String id=(String) params.get("id");
		java.util.Map<String, Object> data =new HashMap();
		try {
			List<Map> activityProductList=(List<Map>)this.allActivityProductManager.getAll(Integer.parseInt(id));
			for (Map m : activityProductList) {
	            double original_price = (Double)m.get("original_price");
	            double activity_price = (Double)m.get("activity_price");
	            String newOriginal_Price ="";
	            String newActivity_Price ="";
	            DecimalFormat format = new DecimalFormat("0.00");
	            newOriginal_Price = format.format(new BigDecimal(original_price));
	            newActivity_Price = format.format(new BigDecimal(activity_price));
	            m.put("original_price",newOriginal_Price);
	            m.put("activity_price",newActivity_Price);	            
	       }
			data.put("activityProductList", activityProductList);
		}catch(RuntimeException e){
			if(this.logger.isDebugEnabled()){
				this.logger.error(e.getStackTrace());
	       }
		}		
		return data;
	}
}
