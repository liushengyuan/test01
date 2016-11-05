package com.enation.app.b2b2c.core.tag.goods;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.component.spec.service.ISpecManager;
import com.enation.app.shop.core.model.GoodsCatSpec;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.model.Specification;
import com.enation.app.shop.core.service.IGoodsCatSpecManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 查找分类关联的商品规格Tag
 * @author LiuJiSong
 *
 */
@Component
public class StoreGoodsSpecForCatTag extends BaseFreeMarkerTag{
	private ISpecManager specManager;
	private IGoodsCatSpecManager goodsCatSpecManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer catid =Integer.parseInt(params.get("catid").toString());
		List<GoodsCatSpec> catSpecList=goodsCatSpecManager.findSpecList(catid);
		List<Specification> specList = this.specManager.listSpecAndValue();
		List<Specification> specList2=new ArrayList<Specification>();
		if(catSpecList.size()==0){
			specList=new ArrayList<Specification>();
		}else{
			for(int i=0;i<specList.size();i++){
				for(int j=0;j<catSpecList.size();j++){
					if(specList.get(i).getSpec_id().equals(catSpecList.get(j).getSpec_id())){
						//specList.remove(i);
						specList2.add(specList.get(i));
					}
				}
			}
		}
		if(specList2.size()!=0){
			specList=specList2;
		}
		return specList;
	}
	public ISpecManager getSpecManager() {
		return specManager;
	}
	public void setSpecManager(ISpecManager specManager) {
		this.specManager = specManager;
	}
	public IGoodsCatSpecManager getGoodsCatSpecManager() {
		return goodsCatSpecManager;
	}
	public void setGoodsCatSpecManager(IGoodsCatSpecManager goodsCatSpecManager) {
		this.goodsCatSpecManager = goodsCatSpecManager;
	}
	
}
