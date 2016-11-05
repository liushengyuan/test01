package com.enation.app.shop.core.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.IAllActivityProductManager;
import com.enation.app.shop.core.service.impl.AllActivityManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class ShowGoodTag extends BaseFreeMarkerTag {
	private IAllActivityProductManager allActivityProductManager;
	private AllActivityManager allActivityManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String id=params.get("id").toString();
		ArrayList<Integer> data=new ArrayList<Integer>();
		List<Map> list=(List<Map>)allActivityProductManager.showGoodsId(Integer.parseInt(id));
		   if(list.size()==2){
			   for (Map map2 : list) {
				Integer goods_id=(Integer) map2.get("goods_id");
				  data.add(goods_id);
			   }
		   }
		return data;
	}
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
}
