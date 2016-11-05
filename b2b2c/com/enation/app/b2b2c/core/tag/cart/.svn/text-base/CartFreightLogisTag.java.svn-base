package com.enation.app.b2b2c.core.tag.cart;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
  /**
   * 获取购物车运费的规格
   * @author Administrator
   *
   */
@Component
@Scope("prototype")
public class CartFreightLogisTag extends BaseFreeMarkerTag {
	private ILogiManager logiManager;
	 
	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

	@Override
	protected Object exec(Map params) throws TemplateModelException {
	         Integer freight_id=Integer.parseInt(params.get("freight_id").toString());
	         //System.out.println(freight_id);
	        FreightStandard freightStandard=this.logiManager.getFreightById(freight_id);
		return freightStandard;
	}
	
}
