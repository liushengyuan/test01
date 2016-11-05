package com.enation.app.b2b2c.core.tag.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IExchangeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.CurrencyUtil;

import freemarker.template.TemplateModelException;
/**
 * @author LiFenLong
 *
 */
@Component
public class StoreCartGoodsTag extends BaseFreeMarkerTag{
	private IStoreCartManager storeCartManager;
	private IStoreDlyTypeManager storeDlyTypeManager;
	private IStoreMemberAddressManager storeMemberAddressManager;
	private IStoreGoodsManager storeGoodsManager;
	private IDlyTypeManager dlyTypeManager;
	private IStoreTemplateManager storeTemplateManager;
	private ICartManager cartManager;
	private IGoodsManager goodsManager;
	private ILogiManager logiManager;
	private IExchangeManager exchangeManager;
	public IExchangeManager getExchangeManager() {
		return exchangeManager;
	}

	public void setExchangeManager(IExchangeManager exchangeManager) {
		this.exchangeManager = exchangeManager;
	}

	public ILogiManager getLogiManager() {
		return logiManager;
	}

	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	int flag=0;
	Double jfprice=null;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	/**
	 * 返回购物车中的购物列表
	 * @param 无 
	 * @return 购物列表 类型List<CartItem>
	 * {@link storeGoodsList}
	 */
	protected Object exec(Map params) throws TemplateModelException {
		List<Map> storeGoodsList= new ArrayList<Map>();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		storeGoodsList=storeCartManager.storeListGoods(sessionid);
		////System.out.println(storeGoodsList.size()+"购物车列表数量");
		Integer addrid = (Integer) params.get("addrid");
		Double integratedprice = (Double) params.get("integratedprice");
		Integer regionsid=0;
		//首次进入页面integratedprice和jfprice比较，若相等flag为1若不等flag为2,jfprice初始化为null。在页面中判断若flag大于1说明地址栏价格变动，跳转history.go(-1)
		if(integratedprice!=null){
			if(jfprice==null){
				jfprice=integratedprice;
			}
			if(jfprice!=null && integratedprice!=null && jfprice.toString().equalsIgnoreCase(integratedprice.toString())){
				flag=1;
			}
			if(jfprice!=null && integratedprice!=null && !jfprice.toString().equalsIgnoreCase(integratedprice.toString())){
				flag=2;
				jfprice=null;
			}
		}else{
			flag=0;
			jfprice=null;
		}
		
		if(addrid!=null && addrid!=0){
			regionsid =  this.storeMemberAddressManager.getRegionid(addrid);
		}
		
		for(Map map : storeGoodsList){
			Integer store_id=  (Integer) map.get("store_id");
			List<StoreCartItem> list = (List<StoreCartItem>) map.get("goodslist");
			HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();
			Integer num=0;
			for (StoreCartItem item :list) {
			   Double frieghtprice11=item.getSendprice();
			      if(item.getFreight_id()!=-1){
				   if(item.getFreight_id()!=0 && frieghtprice11!=0.0){
						 Integer goodsid= item.getGoods_id();
						   Goods goods=this.goodsManager.getGoods(goodsid);
						   num=item.getNum();
						   Double weight=goods.getWeight();
						   FreightStandard freightStandard=this.logiManager.getFreightById(item.getFreight_id());
						   if(language=="zh"){
							   Double ss=FreightUtls.getFreight(freightStandard, weight*num);
							   item.setSendprice(ss);
						   }else if(language=="ru"){
							   Double price1=this.exchangeManager.getExchange("RUB");
							   Double price22=(FreightUtls.getFreight(freightStandard, weight*num))*price1;
							   item.setSendprice(price22);
					    }
					}else{
						 item.setSendprice(0.0);
					}
			      }else if(item.getFreight_id()==-1){
					   if(language=="zh"){
						   item.setSendprice(item.getSendprice());
					   }
				   }
			}
			//Double allweight=CurrencyUtil.mul(Double.valueOf(item.getWeight()), Double.valueOf(item.getNum()));
			Integer tempid = this.storeTemplateManager.getDefTempid(store_id);
			Integer type_id =0;
			if(tempid!=null){
				List<Map> dlyList = this.storeDlyTypeManager.getDlyTypeList(tempid);
				Map dlymap = dlyList.get(0);
				type_id = (Integer) dlymap.get("type_id");
			}
			OrderPrice orderPrice =  this.storeCartManager.countPrice(list, regionsid+"", type_id, false ,null);
			//获取购物车列表数量
			Integer count = storeGoodsList.size();
			//平均分配优惠金额
			double everyDiscountPrice = CurrencyUtil.div(orderPrice.getDiscountPrice(), count);
			map.put("freight", orderPrice.getFreight());
			map.put("storeprice", orderPrice.getOrderPrice());
			map.put("originalPrice", orderPrice.getOriginalPrice());
			map.put("weight", orderPrice.getWeight());
			map.put("discountprice", everyDiscountPrice);
			map.put("count", count);
			map.put("flag", flag);
			if(integratedprice!=null){
				if(count>0){
					map.put("needPayMoney", orderPrice.getNeedPayMoney()-integratedprice/count);	
				}else{
					map.put("needPayMoney", orderPrice.getNeedPayMoney());
				}
			}else{
				map.put("needPayMoney", orderPrice.getNeedPayMoney());
			}
			
		}
		return storeGoodsList;
	}
	
	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}
	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}

	public IStoreDlyTypeManager getStoreDlyTypeManager() {
		return storeDlyTypeManager;
	}

	public void setStoreDlyTypeManager(IStoreDlyTypeManager storeDlyTypeManager) {
		this.storeDlyTypeManager = storeDlyTypeManager;
	}

	public IStoreMemberAddressManager getStoreMemberAddressManager() {
		return storeMemberAddressManager;
	}

	public void setStoreMemberAddressManager(
			IStoreMemberAddressManager storeMemberAddressManager) {
		this.storeMemberAddressManager = storeMemberAddressManager;
	}

	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}

	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}

	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IStoreTemplateManager getStoreTemplateManager() {
		return storeTemplateManager;
	}

	public void setStoreTemplateManager(IStoreTemplateManager storeTemplateManager) {
		this.storeTemplateManager = storeTemplateManager;
	}

	public ICartManager getCartManager() {
		return cartManager;
	}

	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Double getJfprice() {
		return jfprice;
	}

	public void setJfprice(Double jfprice) {
		this.jfprice = jfprice;
	}
	
}
