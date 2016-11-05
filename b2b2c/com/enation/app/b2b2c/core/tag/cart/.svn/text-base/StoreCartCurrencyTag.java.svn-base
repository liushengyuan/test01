package com.enation.app.b2b2c.core.tag.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.service.IStoreDlyTypeManager;
import com.enation.app.b2b2c.core.service.IStoreMemberAddressManager;
import com.enation.app.b2b2c.core.service.IStoreTemplateManager;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.CurrencyUtil;

import freemarker.template.TemplateModelException;
/**
 * @author LiFenLong
 *
 */
@Component
public class StoreCartCurrencyTag extends BaseFreeMarkerTag{
	private ICartManager cartManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	/**
	 * 返回购物车中的商品的币种
	 * @param 无 
	 * @return Map 包含商品的币种
	 * 
	 */
	protected Object exec(Map params) throws TemplateModelException {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		List<CartItem> cartItemList = cartManager.listGoodsOrder(session.getId());
		CartItem cartItem=cartItemList.get(0);//获取购物车中的第一件商品的信息
		String goodsCurrency = cartItem.getCurrency();
		Map currency = new HashMap();
		currency.put("goodsCurrency", goodsCurrency);
		return currency;
	}

	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}
	
	
	
	
}
