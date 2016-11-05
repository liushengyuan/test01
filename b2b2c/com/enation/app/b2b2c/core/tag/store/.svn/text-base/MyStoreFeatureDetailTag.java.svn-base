package com.enation.app.b2b2c.core.tag.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.enation.app.b2b2c.core.model.store.StoreFeature;
import com.enation.app.b2b2c.core.service.store.IStoreFeatureManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import freemarker.template.TemplateModelException;

/**
 * 店铺关键字和主营类别信息Tag
 * @author LiFenLong
 *
 */
@Component
public class MyStoreFeatureDetailTag extends BaseFreeMarkerTag{
	private IStoreFeatureManager storeFeatureManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		List<StoreFeature> storeFeatures=new ArrayList<StoreFeature>();
		Map<String,String> map=new HashMap<String,String>();
		try {
			storeFeatures=storeFeatureManager.getAll(Integer.parseInt(params.get("store_id").toString()));
			if(storeFeatures.size()>0){
				for(StoreFeature feature:storeFeatures){
					if(feature.getFeature_type()==1){
						map.put("featureName_"+feature.getFeature_index().toString(), feature.getName());
					}else{
						map.put("categoreName_"+feature.getFeature_index().toString(), feature.getName());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new UrlNotFoundException();
		}
		return map;
	}
	public IStoreFeatureManager getStoreFeatureManager() {
		return storeFeatureManager;
	}
	public void setStoreFeatureManager(IStoreFeatureManager storeFeatureManager) {
		this.storeFeatureManager = storeFeatureManager;
	}

	
}
