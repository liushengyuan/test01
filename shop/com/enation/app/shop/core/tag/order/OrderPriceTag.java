package com.enation.app.shop.core.tag.order;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IExchangeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.IMemberAddressManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 订单价格tag
 * @author kingapex
 *2013-7-26下午1:27:28
 */
@Component
@Scope("prototype")
public class OrderPriceTag extends BaseFreeMarkerTag {
	private ICartManager cartManager;
	private IGoodsManager goodsManager;
	private ILogiManager logiManager;
	private IExchangeManager exchangeManager;
	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}

	public IExchangeManager getExchangeManager() {
		return exchangeManager;
	}

	public void setExchangeManager(IExchangeManager exchangeManager) {
		this.exchangeManager = exchangeManager;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	private IMemberAddressManager memberAddressManager ;
	
	/**
	 * 订单价格标签
	 * @param address_id:收货地址id，int型
	 * @param shipping_id:配送方式id，int型
	 * @return 订单价格,OrderPrice型
	 * {@link OrderPrice}
	 */
	@Override
	public Object exec(Map args) throws TemplateModelException {
		HttpServletRequest request =ThreadContextHolder.getHttpRequest();
		HttpSession session =request.getSession();
		String sessionid  = session.getId();
		this.cartManager.deleteCartByIsselect(sessionid);
		Map<String, Object> result = new HashMap<String, Object>();
		Integer addressid =(Integer)args.get("address_id");
		Integer shipping_id =(Integer)args.get("shipping_id"); 
		String regionid =null;
		
		//如果传递了地址，已经选完配送方式了
		if(addressid!=null){
			MemberAddress address =memberAddressManager.getAddress(addressid);
			regionid= ""+address.getRegion_id();
		}
		
		OrderPrice orderprice  =this.cartManager.countPrice1(cartManager.listGoods1(sessionid), shipping_id,regionid);
		/*cartManager.listGoods(sessionid).get(0).getCurrency();*/
		result.put("orderprice", orderprice);
		Locale locale = (Locale)session.getAttribute("locale");
		String language=locale.getLanguage();
		Double sum=0.0;
		//int count = this.cartManager.countItemNum(sessionid);
		/*List<CartItem> cartItems=cartManager.listGoods(sessionid);*/
		List<StoreCartItem> cartItems=cartManager.listGoods1(sessionid);
		if(cartItems.size()>0){
			for (StoreCartItem storeCartItem : cartItems) {
				if(storeCartItem.getFreight_id()!=-1){
				  if(storeCartItem.getFreight_id()!=0 && storeCartItem.getSendprice()!=0.0){
				Integer goods_id=storeCartItem.getGoods_id();
				Goods goods=this.goodsManager.getGoods(goods_id);
				Integer num=storeCartItem.getNum();
				Double weight=goods.getWeight();
				if(storeCartItem.getFreight_id()!=0){
				FreightStandard freightStandard=this.logiManager.getFreightById(storeCartItem.getFreight_id());
				double sendprice=FreightUtls.getFreight(freightStandard, weight*num);
				   if(language=="zh"){
					   sum+=sendprice;
					   this.logiManager.updateLogisModel(sendprice,sessionid,storeCartItem.getGoods_id());
				   }else if(language=="ru"){
					   Double price1=this.exchangeManager.getExchange("RUB");
//						//System.out.println(price1);
					   sum+=sendprice*price1;
					   this.logiManager.updateLogisModel(sendprice*price1,sessionid,storeCartItem.getGoods_id());
				       }
				     }
				  }	else{
					  Double m=0.0;
					  sum+=m;
					  this.logiManager.updateLogisModel(m,sessionid,storeCartItem.getGoods_id());
				  }	
//				  this.logiManager.updateLogisModel(sum,sessionid,storeCartItem.getGoods_id());
				}else{
					sum+=storeCartItem.getSendprice();
					this.logiManager.updateLogisModel(storeCartItem.getSendprice(),sessionid,storeCartItem.getGoods_id());
				}
		  }
		}
		String currency=null;
		if(cartItems.size()>0){
			currency=cartItems.get(0).getCurrency();
		}else{
			currency="CNY";
		}
	
		if(currency!=null && currency.equals("CNY")){
			result.put("currency", "CNY");
		}else if(currency!=null && currency.equals("RUB")){
			result.put("currency", "RUB");
		}
		result.put("pricesum", sum);
		result.put("currencyZh", "商品总价");
		result.put("currencyRu", "итого");
		return result;
	}

	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}

	public IMemberAddressManager getMemberAddressManager() {
		return memberAddressManager;
	}

	public void setMemberAddressManager(IMemberAddressManager memberAddressManager) {
		this.memberAddressManager = memberAddressManager;
	}

}
