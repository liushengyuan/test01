package com.enation.app.base.core.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.ITeamManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 商品推荐类标签
 * @author ljs
 * 2013-12-20
 */
@Component
@Scope("prototype")
public class TeamListTag extends BaseFreeMarkerTag {
	private ITeamManager teamManager;
	/**
	 * teamList:推荐类列表 
	 * teamProductList:推荐商品列表
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		String market = (String) params.get("market");
		market = market == null ? "1" : market;
		java.util.Map<String, Object> data =new HashMap();
		try{
			List teamList=teamManager.list(Integer.valueOf(market));
			List teamProductList=teamManager.productList();
			data.put("teamProductList", teamProductList);//广告位详细信息
			data.put("teamList", teamList);//广告列表
		}catch(RuntimeException e){
			if(this.logger.isDebugEnabled()){
				this.logger.error(e.getStackTrace());
			}
		}
		return data;
	}
	public ITeamManager getTeamManager() {
		return teamManager;
	}
	public void setTeamManager(ITeamManager teamManager) {
		this.teamManager = teamManager;
	}
	
}
