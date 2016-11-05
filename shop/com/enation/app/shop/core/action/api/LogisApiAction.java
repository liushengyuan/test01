package com.enation.app.shop.core.action.api;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Attration;
import com.enation.app.shop.core.model.AttrationPage;
import com.enation.app.shop.core.model.Brand;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.GoodLogisDetail;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.LogisModel;
import com.enation.app.shop.core.service.IBrandManager;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.ILogiManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.JsonMessageUtil;
import com.enation.framework.util.StringUtil;

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("logis")
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class LogisApiAction extends WWAction {
	private IStoreMemberManager storeMemberManager;
	private IGoodsManager goodsManager;
	private ICartManager cartManager;
	private IBrandManager brandManager;
	public ICartManager getCartManager() {
		return cartManager;
	}
	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}
	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}
	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}
	private ILogiManager logiManager;
	private String model_name;
	private String model_name2;
	private Integer is_true;
	public Integer getIs_true() {
		return is_true;
	}
	public void setIs_true(Integer is_true) {
		this.is_true = is_true;
	}
	private Integer store_id;
	private String modelname;
	private Integer brand_id;
	public String getModel_name() {
		return model_name;
	}
	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	/**
	 * 获取商户的模板
	 * @return 模板
	 */
	public String loadLogisModel(){
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Integer store_id=Integer.parseInt(request.getParameter("store_id"));
		List logismodel=this.logiManager.selectLogisModel(store_id);
		this.json = JsonMessageUtil.getListJson(logismodel);
		return this.JSON_MESSAGE;
	}
	public String myattion(){
		Brand brand=brandManager.get(brand_id);
		try {
			HttpServletRequest request=ThreadContextHolder.getHttpRequest();
			HttpServletResponse response=ThreadContextHolder.getHttpResponse();
			Member member = UserConext.getCurrentMember();
			Integer	member_id=member.getMember_id();
			//判断关注表里是否存在此品牌用户
			List<Attration> attlist= querForAtteation(member_id,brand_id);
			if(attlist!=null){
				if(is_true==1){
					brand.setAttention(brand.getAttention()+1);
				}else{
					brand.setAttention(brand.getAttention()-1);
				}
				Attration atteation = attlist.get(0);
				atteation.setEndtime(DateUtil.getDateline());
				atteation.setIs_true(is_true);
				this.brandManager.updateatt(atteation);
				this.brandManager.update(brand);
			}else{
				Attration attration=new Attration();
				attration.setBrandid(brand_id);
				attration.setMemberid(member_id);
				Integer attrationcount=brand.getAttention();
				Integer count=attrationcount+1;
				brand.setAttention(count);
				attration.setIs_true(is_true);
				attration.setStarttime(DateUtil.getDateline());
				attration.setEndtime(DateUtil.getDateline());
				this.brandManager.addAttion(attration);
				this.brandManager.update(brand);
			}
			AttrationPage page=this.brandManager.getPageView(brand.getBrand_id());
			this.json = JsonMessageUtil.getObjectJson(page);
		} catch (RuntimeException e) {
			this.showErrorJson("关注失败");
			logger.error("关注失败", e);
		}
		return this.JSON_MESSAGE;
	}
	public List<Attration> querForAtteation(Integer member_id,Integer brand_id){
		List<Attration> attlist = this.brandManager.querForAtteation(member_id,brand_id);
		if(attlist.size()>0){
			return attlist;
		}else{
			return null;
		}
	}
	/**
	 *  模版名称是否存在  
	 * @return 模板名称
	 */
	public String checkModelName(){ 
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		Integer store_id=Integer.parseInt(request.getParameter("store_id"));
	    String	modelname=request.getParameter("modelname");
		int result = this.logiManager.checkModelName(modelname,store_id);
		if(result==0){
			this.showSuccessJson("模版名称不存在，可以使用");
		}else{
			this.showErrorJson("该模版名称已经存在！");
		}
		return this.JSON_MESSAGE;
	}
		/**
		 * 编辑商户的模板
		 * @return 模板
		 */
	public String editAddLogisMode(){
		String success=this.getText("logis.addsuccess");
		String fail=this.getText("logis.addfail");
		String weidenglu=this.getText("logis.weidenglu");
		String weikaidian=this.getText("logis.noOpen");
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			Integer store_id=Integer.parseInt(request.getParameter("store_id"));
			 String	modelname=request.getParameter("modelname");
			 String modelname1=request.getParameter("modelname1");
			 if(modelname.equals(modelname1)){
				 
			 }else{
				 int result = this.logiManager.checkModelName(modelname,store_id);
					if(result!=0){
						this.showErrorJson("模板添加重复，请重新输入");
						return this.JSON_MESSAGE;
					}
			 }
			    
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
				this.showErrorJson(weidenglu);
				return this.JSON_MESSAGE;
			}
			// 判断用户是否已经拥有店铺
			if (storeMember.getIs_store() != 1) {
				this.showErrorJson("");
				return this.JSON_MESSAGE;
			}
			String check_id=request.getParameter("check_id");
			String[] check=check_id.split(",");
			if(check[0].endsWith("0")){
					
			}else{
				for (int i = 0; i < check.length; i++) {
					this.logiManager.deleteLogisModel(check[i]);
					//System.out.println(check[i]+";");
				  }
			}
			String select_goods_ids = request.getParameter("freight_id1");
			Map<String, String> idMap = new HashMap<String, String>();
			String[] ids = select_goods_ids.split(";");
			for(int i=0;i<ids.length;i++){
				idMap.put(ids[i].split(",")[0], ids[i].split(",")[1]);
			   }
				  Set<String> keySet = idMap.keySet();
				  //System.out.println("222");
				  Iterator<String> it = keySet.iterator();
				  //System.out.println("333");
				  while (it.hasNext()) {
					 String key = it.next();
					 String value = idMap.get(key);
					 LogisModel logisModel1=this.logiManager.getModelNameById(key);
					 LogisModel logisModel=new LogisModel();
					 logisModel.setFreight_id(logisModel1.getFreight_id());
					 logisModel.setLogis_price_type(Integer.parseInt(value));
					 logisModel.setModel_name(this.model_name);  
			         logisModel.setLogis_model_id(key);
					 this.logiManager.updateLogiModel(logisModel);
				}
			this.showSuccessJson("修改成功");
		} catch (RuntimeException e) {
			this.showErrorJson("修改失败");
			logger.error("修改失败", e);
		}
		return this.JSON_MESSAGE;
	}
		/**
		 * 添加商户的模板
		 * @return 模板
		 */
	public String addLogisMode(){
		String success=this.getText("logis.addsuccess");
		String fail=this.getText("logis.addfail");
		String weidenglu=this.getText("logis.weidenglu");
		String weikaidian=this.getText("logis.noOpen");
		
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			String	modelname=request.getParameter("modelname");
			Integer store_id=Integer.parseInt(request.getParameter("store_id"));
		    int result = this.logiManager.checkModelName(modelname,store_id);
			if(result!=0){
				this.showErrorJson("模板添加重复，请重新输入");
				return this.JSON_MESSAGE;
			}
			
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
				this.showErrorJson(weidenglu);
				return this.JSON_MESSAGE;
			}
			// 判断用户是否已经拥有店铺
			if (storeMember.getIs_store() != 1) {
				this.showErrorJson(weikaidian);
				return this.JSON_MESSAGE;
			}
			int max=this.logiManager.getMaxLogisModelNAME()+1;
			String select_goods_ids = request.getParameter("freight_id1");
			Map<String, String> idMap = new HashMap<String, String>();
			String[] ids = select_goods_ids.split(";");
			     for(int i=0;i<ids.length;i++){
				   idMap.put(ids[i].split(",")[0], ids[i].split(",")[1]);
			    }
                  Set<String> keySet = idMap.keySet();
				  Iterator<String> it = keySet.iterator();
				  while (it.hasNext()) {
					 String key = it.next();
					 FreightStandard freightStandard=this.logiManager.getFreightById(Integer.parseInt(key));
					 String validatedays=freightStandard.getValidatedays();
					 String product_name=freightStandard.getProduct_name();
					 String value = idMap.get(key);
					 String date=Long.toString(new Date().getTime())+ Integer.toString(new Random().nextInt(100));
					 LogisModel logisModel=new LogisModel();
					 logisModel.setFreight_id(Integer.parseInt(key));
					 logisModel.setLogis_price_type(Integer.parseInt(value));
					 logisModel.setStore_id(store_id);
					 logisModel.setLogis_model_id(date);
					 logisModel.setModel_name(this.model_name);
					 logisModel.setIs_name(max);
					 //System.out.println(key + "***"+value+"**"+validatedays+"**"+product_name+"**"+date+"***"+this.model_name+"**"+max);
					  this.logiManager.addLogiModel(logisModel);
				}
			this.showSuccessJson("添加成功");
		} catch (RuntimeException e) {
			this.showErrorJson("添加失败");
			logger.error("添加失败", e);
		}
		return this.JSON_MESSAGE;
	}
	public String addGoodsLogisDetails(){
		String success=this.getText("logis.addsuccess");
		String fail=this.getText("logis.addfail");
		try {
			HttpServletRequest request=ThreadContextHolder.getHttpRequest();
			String sessionid = ThreadContextHolder.getHttpRequest()
					.getSession().getId();
			int goods_id=Integer.parseInt(request.getParameter("goods_id"));
			StoreGoods goods=this.goodsManager.getStoreGoods(goods_id);
			Double weight=goods.getWeight();
			//System.out.println(goods.getWeight());
			int store_id=Integer.parseInt(request.getParameter("store_id"));
			int num=Integer.parseInt(request.getParameter("num"));
			String logis_model_id=request.getParameter("check_id");
		    LogisModel logisModel=this.logiManager.getModelNameById(logis_model_id);
		    FreightStandard freightStandard=this.logiManager.getFreightById(logisModel.getFreight_id());
			GoodLogisDetail goodLogisDetail=new GoodLogisDetail();
			HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();
			String currency="";
			Double sendprice=0.0;
			if(language=="zh"){
				  currency="CNY";
				String q=request.getParameter("price");
				if(q.equals("0")){
					goodLogisDetail.setSendprice(0.0);
				}else{
					int x= q.indexOf("¥");
				     String m=q.substring(x+1);
				        //System.out.println(m);
				 sendprice=Double.parseDouble(m);
				 goodLogisDetail.setSendprice(sendprice);
				}
				     
			}else if(language=="ru"){
				currency="RUB";
				String q=request.getParameter("price");
				if(q.equals("0")){
					goodLogisDetail.setSendprice(0.0);
				}else{
					 int x= q.indexOf("¥");
					 //System.out.println(x);
					 int y=q.indexOf("p.");
					 //System.out.println(y);
					 String m=q.substring(x+1,y);
				        //System.out.println(m);
				    sendprice=Double.parseDouble(m);
				 goodLogisDetail.setSendprice(sendprice);
				}
				
			}
		    /*Double price1=0.0;
		    if(language=="zh"){
		    	 currency="CNY";
		    	price1=FreightUtls.getFreight(freightStandard, weight*num);
		    	goodLogisDetail.setSendprice(price1);
		    }else if(language=="ru"){
		    	currency="RUB";
		    	//Double price=exchangeManager.getExchange("RUB");
				//price1=(FreightUtls.getFreight(freightStandard, weight*num))*price;
				goodLogisDetail.setSendprice(price1);
		    }*/
			goodLogisDetail.setCurrency(currency);
			goodLogisDetail.setGoods_id(goods_id);
			goodLogisDetail.setStore_id(store_id);
		
			goodLogisDetail.setSession_id(sessionid);
			goodLogisDetail.setFreight_id(freightStandard.getFreight_id());
			this.logiManager.addGoodLogisDetail(goodLogisDetail);
			
			this.showSuccessJson1(freightStandard.getFreight_id()+"",CurrencyUtil.round(sendprice, 2));
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("添加失败");
		}
		return this.JSON_MESSAGE;
	  }
	public void showSuccessJson1(String message,Double price){
		//System.out.println(message+"**"+price);
		if(StringUtil.isEmpty(message))
			this.json="{\"result\":1}";
		else
			this.json="{\"price\":\""+price+"\",\"message\":\""+message+"\"}";
	}
	public String caculateLogisPrice(){
		HttpServletRequest request=ThreadContextHolder.getHttpRequest();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		int goods_id=Integer.parseInt(request.getParameter("goods_id"));
		int num=Integer.parseInt(request.getParameter("num"));
		int freight_id=Integer.parseInt(request.getParameter("freight_id"));
		if(freight_id==-1){
			if(num<=1){
				num=1;
			}
			Goods goods=this.goodsManager.getGoods(goods_id);
			Double freightprice=0.0;
			if(language=="zh"){
				freightprice=goods.getFreight()*num;
				this.showSuccessJson("¥"+CurrencyUtil.round(freightprice, 2));
			}
		}else{
			Goods goods=this.goodsManager.getGoods(goods_id);
			Double type_weight=goods.getWeight();
			FreightStandard freightStandard = this.logiManager.getFreightById(freight_id);
			if(num<=1){
				num=1;
			}
			Double freightprice=0.0;
			if(language=="zh"){
				freightprice=FreightUtls.getFreight(freightStandard, type_weight*num);
				//System.out.println(freightprice);
				this.showSuccessJson("¥"+CurrencyUtil.round(freightprice, 2));
				
			}else if(language=="ru"){
				//Double price=exchangeManager.getExchange("RUB");
				//freightprice=(FreightUtls.getFreight(freightStandard, type_weight*num))*price;
				//System.out.println(freightprice);
				this.showSuccessJson(CurrencyUtil.round(freightprice, 2)+"p.");
			}
		}
		return this.JSON_MESSAGE;
	}
	public String caculateLogisPrice1(){
		try {
			HttpServletRequest request=ThreadContextHolder.getHttpRequest();
			String sessionid = ThreadContextHolder.getHttpRequest()
					.getSession().getId();
			HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();
			String currency="";
			if(language=="zh"){
				  currency="CNY";
			}else if(language=="ru"){
				currency="RUB";
			}
			Double sendprice=Double.parseDouble(request.getParameter("sendprice"));
			int freight_id=Integer.parseInt(request.getParameter("freight_id"));
			int goods_id=Integer.parseInt(request.getParameter("goods_id"));
			int num=Integer.parseInt(request.getParameter("num"));
			
			int store_id=Integer.parseInt(request.getParameter("store_id")); 
			GoodLogisDetail goodLogisDetail=new GoodLogisDetail();
			if(freight_id==-1){
				Goods goods=this.goodsManager.getGoods(goods_id);
			    Double	freightprice=goods.getFreight()*num;
			    goodLogisDetail.setSendprice(freightprice);
				goodLogisDetail.setCurrency(currency);
				goodLogisDetail.setGoods_id(goods_id);
				goodLogisDetail.setStore_id(store_id);
				goodLogisDetail.setSession_id(sessionid);
				goodLogisDetail.setFreight_id(freight_id);
				this.logiManager.addGoodLogisDetail(goodLogisDetail);
			}else{
				if(sendprice==0.0 || freight_id==0 ){
					goodLogisDetail.setSendprice(0.0);
					goodLogisDetail.setCurrency(currency);
					goodLogisDetail.setGoods_id(goods_id);
					goodLogisDetail.setStore_id(store_id);
					goodLogisDetail.setSession_id(sessionid);
					goodLogisDetail.setFreight_id(freight_id);
					this.logiManager.addGoodLogisDetail(goodLogisDetail);
				}else{
					goodLogisDetail.setCurrency(currency);
					goodLogisDetail.setGoods_id(goods_id);
					goodLogisDetail.setStore_id(store_id);
					Goods goods=this.goodsManager.getGoods(goods_id);
					Double weight=goods.getWeight();
					FreightStandard freightStandard=this.logiManager.getFreightById(freight_id);
				    sendprice=FreightUtls.getFreight(freightStandard, weight*num);
					goodLogisDetail.setSendprice(sendprice);

					goodLogisDetail.setSession_id(sessionid);
					goodLogisDetail.setFreight_id(freight_id);
					this.logiManager.addGoodLogisDetail(goodLogisDetail);
				}	
			}
			this.showSuccessJson("添加成功");
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("添加失败");
		}
		
		return this.JSON_MESSAGE;
	}
	public String caculateLogisPrice2(){
		String success=this.getText("logis.addsuccess");
		String fail=this.getText("logis.addfail");
		try {
			HttpServletRequest request=ThreadContextHolder.getHttpRequest();
			String sessionid = ThreadContextHolder.getHttpRequest()
					.getSession().getId();
			HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
			Locale locale = (Locale) session.getAttribute("locale");
			String language = locale.getLanguage();
			String currency="";
			if(language=="zh"){
				  currency="CNY";
			}else if(language=="ru"){
				currency="RUB";
			}
			int goods_id=Integer.parseInt(request.getParameter("goods_id"));
			int num=Integer.parseInt(request.getParameter("num"));
			String check_id=request.getParameter("check_id");
			//System.out.println(check_id);
			LogisModel logisModel=this.logiManager.getModelNameById(check_id);
			Integer logis_price_type=logisModel.getLogis_price_type();
			//System.out.println(logis_price_type);
			int store_id=Integer.parseInt(request.getParameter("store_id")); 
			GoodLogisDetail goodLogisDetail=new GoodLogisDetail();
			goodLogisDetail.setFreight_id(logisModel.getFreight_id());
			goodLogisDetail.setCurrency(currency);
			goodLogisDetail.setGoods_id(goods_id);
			goodLogisDetail.setStore_id(store_id);
			Goods goods=this.goodsManager.getGoods(goods_id);
			Double weight=goods.getWeight();
			FreightStandard freightStandard=this.logiManager.getFreightById(logisModel.getFreight_id());
			if(logis_price_type==1){
				goodLogisDetail.setSendprice(0.0);
				
			}else{
				double sendprice=FreightUtls.getFreight(freightStandard, weight*num);
				goodLogisDetail.setSendprice(sendprice);
			}	
			goodLogisDetail.setSession_id(sessionid);
	        
			/*List<StoreCartItem> list= cartManager.listGoods1(sessionid);
			if(list.size()==1){
				
			}else{
				for (StoreCartItem storecart : list) {
					String freitid=storecart.getSendprice()+"**"+storecart.getFreight_id();
					//System.out.println(freitid);
					if(logisModel.getFreight_id()==0){
						
					}else{
						if(logisModel.getFreight_id()!=storecart.getFreight_id()){
							this.showErrorJson("该货品与购物车中的其他商品选择物流不一致，请重新选择");
							return this.JSON_MESSAGE;
						}
					}
					
				}
			}*/
			
			this.logiManager.addGoodLogisDetail(goodLogisDetail);
			
			this.showSuccessJson(success);
		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson(fail);
		}
		
		return this.JSON_MESSAGE;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public ILogiManager getLogiManager() {
		return logiManager;
	}
	public void setLogiManager(ILogiManager logiManager) {
		this.logiManager = logiManager;
	}
	public String getModel_name2() {
		return model_name2;
	}
	public void setModel_name2(String model_name2) {
		this.model_name2 = model_name2;
	}
	public String getModelname() {
		return modelname;
	}
	public void setModelname(String modelname) {
		this.modelname = modelname;
	}
	public Integer getBrand_id() {
		return brand_id;
	}
	public void setBrand_id(Integer brand_id) {
		this.brand_id = brand_id;
	}
	public IBrandManager getBrandManager() {
		return brandManager;
	}
	public void setBrandManager(IBrandManager brandManager) {
		this.brandManager = brandManager;
	}

}
