package com.enation.app.shop.core.tag;

import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.CustomerLogi;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class LogisEditTag extends BaseFreeMarkerTag {
	private ILogiManager logiManager;
	 

	public ILogiManager getLogiManager() {
	return logiManager;
}


public void setLogiManager(ILogiManager logiManager) {
	this.logiManager = logiManager;
}
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		// TODO Auto-generated method stub
		String model_name=(String) params.get("model_name");
		String store_id=(String) params.get("store_id");
		 Integer storeid=Integer.parseInt(store_id);
		//System.out.println(store_id);
		List<CustomerLogi> customerLogis=this.logiManager.getLogiModel(model_name,storeid);
		return customerLogis;
	}

}
