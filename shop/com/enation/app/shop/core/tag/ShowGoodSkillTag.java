package com.enation.app.shop.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.AllActivity;
import com.enation.app.shop.core.service.IAllActivityProductManager;
import com.enation.app.shop.core.service.impl.AllActivityManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.DateUtil;

import freemarker.template.TemplateModelException;

@Component
public class ShowGoodSkillTag extends BaseFreeMarkerTag{
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
		 long time=DateUtil.getDateline();
		String goods_id=params.get("goods_id").toString();
		 java.util.Map<String, Object> data =new HashMap();
		  List<Map>	activity=(List<Map>)allActivityManager.getGoodsById(Integer.parseInt(goods_id));
		  for (Map map : activity) {
			map.put("nowtime", time);
		  }
		  data.put("activity", activity);
		  return activity;
	}

}
