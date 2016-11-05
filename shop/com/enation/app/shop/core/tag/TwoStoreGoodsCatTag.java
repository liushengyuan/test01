package com.enation.app.shop.core.tag;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
@Component
@Scope("prototype")
public class TwoStoreGoodsCatTag extends BaseFreeMarkerTag {
	private IGoodsManager goodsManager;
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		HttpServletResponse response=ThreadContextHolder.getHttpResponse();
		if(request.getParameter("is_belong")==null){
			try {
				response.sendRedirect("index.html");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
		}
		Integer is_belong = (Integer.parseInt(params.get("is_belong").toString()));
		List<Cat> list = this.goodsManager.getCat_id(is_belong);
		Set<Long> catOneId = new HashSet<Long>();
		Set<Long> catTwoId = new HashSet<Long>();
		Set<Long> catThreeId = new HashSet<Long>();
		for (Cat cat : list) {
			String cat_path=cat.getCat_path();
			if(cat_path!=null){
				String[] s=cat_path.split("\\|");
				for (int i = 0; i < s.length; i++) {
					if(s.length==4){
						catOneId.add(Long.valueOf(s[1]));
						catTwoId.add(Long.valueOf(s[2]));
						catThreeId.add(Long.valueOf(s[3]));
					}
					if(s.length==3){
						catOneId.add(Long.valueOf(s[1]));
						catTwoId.add(Long.valueOf(s[2]));
					}
					if(s.length==2){
						catOneId.add(Long.valueOf(s[1]));
					}
				}
			}
			
		}
		Map map = new HashMap();
		map.put("one", catOneId);
		map.put("two", catTwoId);
		map.put("three", catThreeId);
		return map;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

}
