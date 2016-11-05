package com.enation.app.tradeease.core.action.api.cordova;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.shop.core.model.CustomerFreight;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.service.IExchangeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.app.tradeease.core.util.UserComparator;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.JsonMessageUtil;

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/cordova")
@Action("cordovaFreight")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class cordovaFreightApiAction extends WWAction {
	private ILogiManager logiManager;
	private IExchangeManager exchangeManager;
	private IGoodsManager goodsManager;
	// for getFreight
	private Integer goods_id;
	private Integer freightType;
	private Double goods_weight;

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getFreightType() {
		return freightType;
	}

	public void setFreightType(Integer freightType) {
		this.freightType = freightType;
	}

	public Double getGoods_weight() {
		return goods_weight;
	}

	public void setGoods_weight(Double goods_weight) {
		this.goods_weight = goods_weight;
	}
	public String getFreight(){
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
//		String language = locale.getLanguage();
		String language = "zh";
		StoreGoods storeGoods=this.goodsManager.getStoreGoods(goods_id);
		 Double weight=storeGoods.getWeight();
		Double depth=Double.parseDouble(storeGoods.getDepth());
        Double width=Double.parseDouble(storeGoods.getWidth());
        Double height=Double.parseDouble(storeGoods.getHeight());
		Integer freight_id=freightType;
		Double type_weight=goods_weight;
		if(freight_id==0){
			List<CustomerFreight> customerFreights=new ArrayList<CustomerFreight>();
			  CustomerFreight customerFreight=new CustomerFreight();
			  customerFreight.setFreightprice(storeGoods.getFreight());
			  customerFreight.setFreight_id(-1);
			  customerFreights.add(customerFreight);
			  this.json = JsonMessageUtil.getListJson(customerFreights);
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
		   this.json = JsonMessageUtil.getListJson(customerFreights);
		}else if(freight_id==1){
			  List<CustomerFreight> customerFreights=new ArrayList<CustomerFreight>();
			  CustomerFreight customerFreight=new CustomerFreight();
			  customerFreight.setFreightprice(0.0);
			  customerFreight.setFreight_id(0);
			  customerFreights.add(customerFreight);
			  this.json = JsonMessageUtil.getListJson(customerFreights);
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
			 
			   this.json = JsonMessageUtil.getListJson(customerFreights);
		}
		
	
		return this.JSON_MESSAGE;
		
	}
	//获取购物车运费总价
	public String getFreightTotalPrice(){
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		try{
			int totalPrice = this.logiManager.getFreightTotalPrice(session.getId());
			showSuccessJson(totalPrice+"");
		}catch(Exception e){
			showErrorJson(e.getMessage());
		}
		return this.JSON_MESSAGE;
	}
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
}
