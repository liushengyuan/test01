package com.enation.app.shop.core.tag.detail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.shop.core.model.CustomerFreight;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.service.IExchangeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.app.tradeease.core.util.UserComparator;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
   /**
    * 遍历商品所产生的运费规则
    * @author Administrator
    *
    */
@Component
@Scope("prototype")
public class GoodsLogisModelTag extends BaseFreeMarkerTag{
	private ILogiManager logiManager;
	private IExchangeManager exchangeManager;
	private IGoodsManager goodsManager;
	 
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

	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer goods_id=Integer.parseInt(params.get("goods_id").toString());
		StoreGoods storeGoods=this.goodsManager.getStoreGoods(goods_id);
		 Double weight=storeGoods.getWeight();
		Double depth=Double.parseDouble(storeGoods.getDepth());
        Double width=Double.parseDouble(storeGoods.getWidth());
        Double height=Double.parseDouble(storeGoods.getHeight());
      /*  //System.out.println(depth+"**"+width+"**"+height);*/
		Integer freight_id=Integer.parseInt(params.get("freightType").toString());
		Double type_weight=storeGoods.getWeight();
		//System.out.println(freight_id+"***"+type_weight+"**"+goods_id);
		if(freight_id==0){
			List<CustomerFreight> customerFreights=new ArrayList<CustomerFreight>();
			  CustomerFreight customerFreight=new CustomerFreight();
			  customerFreight.setFreightprice(storeGoods.getFreight());
			  customerFreight.setFreight_id(-1);
			  customerFreights.add(customerFreight);
			  return customerFreights;
		}else if(freight_id==2){//biaozhun
		   List<CustomerFreight> customerFreights=new ArrayList<CustomerFreight>();
		   List<LogisModel> logisModels=this.logiManager.findLogisModel(freight_id);
		   for (LogisModel logisModel : logisModels) {
				CustomerFreight customerFreight = new CustomerFreight();
				Integer freightid = logisModel.getFreight_id();
				FreightStandard freightStandard = this.logiManager.getFreightById(freightid);
				
				Double min_long=freightStandard.getMin_long();
				Double max_long=freightStandard.getMax_long();
				Double min_width=freightStandard.getMin_width();
				Double max_width=freightStandard.getMax_width();
				double min_height=freightStandard.getMin_high();
				double max_height=freightStandard.getMax_high();
				double min_weight=freightStandard.getMin_weight();
				double max_weight=freightStandard.getMax_weight();
				if(depth>=min_long && depth<max_long && width>=min_width && width<max_width && height>=min_height && height<max_height && weight>=min_weight && weight<max_weight){
					HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
					Locale locale = (Locale) session.getAttribute("locale");
					String language = locale.getLanguage();
					Double	freightprice=0.0;
					if(language=="zh"){
						freightprice=FreightUtls.getFreight(freightStandard, type_weight);
					}else if(language=="ru"){
						 Double price=exchangeManager.getExchange("RUB");
						 freightprice=(FreightUtls.getFreight(freightStandard, type_weight))*price;
					}
					customerFreight.setFreightprice(freightprice);
					customerFreight.setLogis_name(freightStandard.getLogis_name());
					customerFreight.setProductname(freightStandard.getProduct_name());
					customerFreight.setValidatedays(freightStandard.getValidatedays());
					customerFreight.setLogis_model_id(logisModel.getLogis_model_id());
					customerFreight.setFreight_id(freightid);
					customerFreights.add(customerFreight);
				}	
		   }
		 //创建比较器对象
		   UserComparator comp=new UserComparator();
		   //调用排序方法
		   Collections.sort(customerFreights,comp);
		   return customerFreights;
		}else if(freight_id==1){
			  List<CustomerFreight> customerFreights=new ArrayList<CustomerFreight>();
			  CustomerFreight customerFreight=new CustomerFreight();
			  customerFreight.setFreightprice(0.0);
			  customerFreight.setFreight_id(0);
			  customerFreights.add(customerFreight);
			  return customerFreights;
		}else{//shangjia zidingyi 
			List<CustomerFreight> customerFreights=new ArrayList<CustomerFreight>();
			   List<LogisModel> logisModels=this.logiManager.findLogisModel(freight_id);
			   for (LogisModel logisModel : logisModels) {
					CustomerFreight customerFreight = new CustomerFreight();
					Integer type_id=logisModel.getLogis_price_type();
					if(type_id==1){
						Double freight=0.0;
						Integer freightid = logisModel.getFreight_id();
						FreightStandard freightStandard = this.logiManager.getFreightById(freightid);
						customerFreight.setFreightprice(freight);
						customerFreight.setLogis_name(freightStandard.getLogis_name());
						customerFreight.setProductname(freightStandard.getProduct_name());
						customerFreight.setValidatedays(freightStandard.getValidatedays());
						customerFreight.setLogis_model_id(logisModel.getLogis_model_id());
						customerFreight.setFreight_id(freightid);
						customerFreights.add(customerFreight);
					}else{
						Integer freightid = logisModel.getFreight_id();
						FreightStandard freightStandard = this.logiManager.getFreightById(freightid);
						
						Double min_long=freightStandard.getMin_long();
						Double max_long=freightStandard.getMax_long();
						Double min_width=freightStandard.getMin_width();
						Double max_width=freightStandard.getMax_width();
						double min_height=freightStandard.getMin_high();
						double max_height=freightStandard.getMax_high();
						double min_weight=freightStandard.getMin_weight();
						double max_weight=freightStandard.getMax_weight();
						if(depth>=min_long && depth<max_long && width>=min_width && width<max_width && height>=min_height && height<max_height && weight>=min_weight && weight<max_weight){
							HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
							Locale locale = (Locale) session.getAttribute("locale");
							String language = locale.getLanguage();
							Double	freightprice=0.0;
							if(language=="zh"){
								freightprice=FreightUtls.getFreight(freightStandard, type_weight);
							}else if(language=="ru"){
								 Double price=exchangeManager.getExchange("RUB");
								 freightprice=(FreightUtls.getFreight(freightStandard, type_weight))*price;
							}
							customerFreight.setFreightprice(freightprice);
							customerFreight.setLogis_name(freightStandard.getLogis_name());
							customerFreight.setProductname(freightStandard.getProduct_name());
							customerFreight.setValidatedays(freightStandard.getValidatedays());
							customerFreight.setLogis_model_id(logisModel.getLogis_model_id());
							customerFreight.setFreight_id(freightid);
							customerFreights.add(customerFreight);
						}
						
					}
					
			   }
			 //创建比较器对象
			   UserComparator comp=new UserComparator();
			   //调用排序方法
			   Collections.sort(customerFreights,comp);
			 
			   return customerFreights;
		}
		
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
   
}
