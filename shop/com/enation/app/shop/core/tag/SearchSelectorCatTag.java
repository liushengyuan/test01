package com.enation.app.shop.core.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.Separator;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.StringUtil;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class SearchSelectorCatTag extends BaseFreeMarkerTag {
	private IGoodsCatManager goodsCatManager ;
	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}
	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		// TODO Auto-generated method stub
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String cat = request.getParameter("cat");
		Map map = new HashMap();
		if(cat==null){
			return map;
		}
		String[] catar = cat.split(Separator.separator_prop_vlaue);
		if(catar.length==1){
			String catid3 =  catar[catar.length-1];
			if(!StringUtil.isEmpty(catid3)){
				List<Goods> list=this.goodsCatManager.getGoodsByCatId(StringUtil.toInt(catid3,0));
				List<Goods> list1=this.goodsCatManager.getGoodsByCatIdByView(StringUtil.toInt(catid3,0));
				map.put("commond", list);
				map.put("view", list1);
			}
		}
		if(catar.length==2){
			//String catid3 =  catar[catar.length-1];
			String catid2 =  catar[catar.length-2];
			if(!StringUtil.isEmpty(catid2)){
				List<Goods> list=this.goodsCatManager.getGoodsByCatId(StringUtil.toInt(catid2,0));
				List<Goods> list1=this.goodsCatManager.getGoodsByCatIdByView(StringUtil.toInt(catid2,0));
				map.put("commond", list);
				map.put("view", list1);
			}
		}
		if(catar.length==3){
			//String catid3 =  catar[catar.length-1];
			//String catid2 =  catar[catar.length-2];
			String catid1 =  catar[catar.length-3];
			if(!StringUtil.isEmpty(catid1)){
				List<Goods> list=this.goodsCatManager.getGoodsByCatId(StringUtil.toInt(catid1,0));
				List<Goods> list1=this.goodsCatManager.getGoodsByCatIdByView(StringUtil.toInt(catid1,0));
				map.put("commond", list);
				map.put("view", list1);
			}
		}
		
		return map;
	}

}
