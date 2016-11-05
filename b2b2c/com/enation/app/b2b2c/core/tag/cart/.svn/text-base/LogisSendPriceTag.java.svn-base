package com.enation.app.b2b2c.core.tag.cart;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.GoodLogisDetail;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IExchangeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.service.IlogisManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

@Component
@Scope("prototype")
public class LogisSendPriceTag extends BaseFreeMarkerTag{
	private ILogiManager logiManager;
	private IExchangeManager exchangeManager;
	private IGoodsManager goodsManager;
	public IExchangeManager getExchangeManager() {
		return exchangeManager;
	}

	public void setExchangeManager(IExchangeManager exchangeManager) {
		this.exchangeManager = exchangeManager;
	}

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		   Integer store_id=Integer.parseInt(params.get("store_id").toString());
		   //System.out.println(store_id);
		   HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		   String sessionid = request.getSession().getId();
		   List<GoodLogisDetail> goodLogisDetails=this.logiManager.findLogisDetail(store_id, sessionid);
		   HttpSession session =request.getSession();
		   Locale locale = (Locale)session.getAttribute("locale");
		   String language=locale.getLanguage();
		   Double sum=0.0;
		   for (GoodLogisDetail goodLogisDetail : goodLogisDetails) {
			   List<Cart> list=this.goodsManager.getCartById(goodLogisDetail.getGoods_id(), goodLogisDetail.getSession_id(), goodLogisDetail.getStore_id());
			    Double sendprice=0.0;
			    Integer goods_id=goodLogisDetail.getGoods_id();
				Goods goods=this.goodsManager.getGoods(goods_id);
				Double weight=goods.getWeight();
				if(list.size()>0){
				if(goodLogisDetail.getFreight_id()!=-1){
					//加上list不等于null，是为了防止同一sessiongId用户添加到购物车不进行支付，而保留支付记录。页面没有关闭又进行购买其他产品，
					//这时通购物车产品进行比较，如果是购物车的产品则进行运费计算，如果不是购物车的产品则进行过滤。
					if(goodLogisDetail.getFreight_id()!=0 && goodLogisDetail.getSendprice()!=0.0 && list!=null){
						FreightStandard freightStandard=this.logiManager.getFreightById(goodLogisDetail.getFreight_id());
						for (Cart cart : list) {
							int num=cart.getNum();
							sendprice+=FreightUtls.getFreight(freightStandard, weight*num);
						}
						if(language=="zh"){
							sum+=sendprice;
							this.logiManager.updateLogisModel(sendprice, goodLogisDetail.getSession_id(), goodLogisDetail.getGoods_id());
						}else if(language=="ru"){
							Double price1=this.exchangeManager.getExchange("RUB");
//							//System.out.println(price1);
							sum+=sendprice*price1;
							this.logiManager.updateLogisModel(sendprice*price1, goodLogisDetail.getSession_id(), goodLogisDetail.getGoods_id());
						}
					}else if(list!=null){
						Double m=0.0;
						sum+=m;
						 this.logiManager.updateLogisModel(m, goodLogisDetail.getSession_id(), goodLogisDetail.getGoods_id());
					}
				  }else if( list!=null && list.size()>0 && goodLogisDetail.getFreight_id()==-1  ){
					  sum+=goodLogisDetail.getSendprice();
					  this.logiManager.updateLogisModel(goodLogisDetail.getSendprice(), goodLogisDetail.getSession_id(), goodLogisDetail.getGoods_id());
				  }else{
					  sum=0.0;
					  this.logiManager.updateLogisModel(sum, goodLogisDetail.getSession_id(), goodLogisDetail.getGoods_id());
				  }
				}
//				 this.logiManager.updateLogisModel(sum, goodLogisDetail.getSession_id(), goodLogisDetail.getGoods_id());
			}
		   
		   return sum;
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
	
}
