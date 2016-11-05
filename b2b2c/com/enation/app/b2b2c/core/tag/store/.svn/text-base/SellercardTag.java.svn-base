package com.enation.app.b2b2c.core.tag.store;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.tradeease.core.model.account.SellerCard;

import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.CurrencyUtil;

import freemarker.template.TemplateModelException;
/**
 * 订单支付详细标签
 * @author LiFenLong
 *
 */
@Component
public class SellercardTag extends BaseFreeMarkerTag{
	private IStoreManager storeManager;
	public IStoreManager getStoreManager() {
		return storeManager;
	}
	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
	
	@Override
	protected Object exec(Map params) throws TemplateModelException {
	SellerCard sellerCard = this.storeManager.defaultCard();
		if(sellerCard==null ){
			return "";
		}else {
			return sellerCard;
		}
		//return sellerCard;
		
		
	}
	
}
