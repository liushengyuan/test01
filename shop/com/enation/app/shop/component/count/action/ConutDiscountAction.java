package com.enation.app.shop.component.count.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.context.annotation.Scope;

import com.enation.app.shop.component.bonus.model.BonusType;
import com.enation.app.shop.component.bonus.service.IBonusTypeManager;
import com.enation.app.shop.component.count.service.IConutDiscountManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 促销活动统计
 * @author jfw
 *2015-10-9下午14:43:39
 */
@ParentPackage("shop_default")
@Namespace("/shop/admin")
@Scope("prototype")
@Action("count")
@Results({
	 @Result(name="list", type="freemarker", location="/com/enation/app/shop/component/count/action/html/count_discount_list.html") ,
	 @Result(name="listGoods", type="freemarker", location="/com/enation/app/shop/component/count/action/html/count_goods_list.html"),
	 @Result(name="listBouns", type="freemarker", location="/com/enation/app/shop/component/count/action/html/count_bouns_list.html"),
	 @Result(name="edit", type="freemarker", location="/com/enation/app/shop/component/bonus/action/html/bonus_type_edit.html") 
})
public class ConutDiscountAction extends WWAction {
	
	private IConutDiscountManager conutDiscountManager;
	private IBonusTypeManager bonusTypeManager;

	
	public String list(){
		return "list";
	}
	
	public String listJson(){
		List<HashMap<String, String>> data = new ArrayList<HashMap<String,String>>();
		//1、参加折扣活动的店铺数量
		HashMap<String, String> discountMap = new HashMap<String, String>();
		List discountStore = this.conutDiscountManager.getStoreList();
		Integer discountNumber = 0;
		if(discountStore != null && discountStore.size() > 0){
			discountNumber = discountStore.size();
		}
		discountMap.put("id", "1");
		discountMap.put("name", "商品打折促销活动");
		discountMap.put("type", "折扣");
		discountMap.put("number", discountNumber.toString());
		data.add(discountMap);
		//2、参加优惠券活动的店铺数量
		HashMap<String, String> bonusMap = new HashMap<String, String>();
		List bonusStore = this.conutDiscountManager.getBonusList();
		Integer bonusNumber = 0;
		if(bonusStore != null && bonusStore.size() > 0){
			bonusNumber = bonusStore.size();
		}
		bonusMap.put("id", "2");
		bonusMap.put("name", "优惠券促销活动");
		bonusMap.put("type", "优惠券");
		bonusMap.put("number", bonusNumber.toString());
		data.add(bonusMap);
		Page webPage = new Page(0,10, 10, data);
		this.webpage = webPage;
		
		this.showGridJson(this.webpage);
		return JSON_MESSAGE;
	}

	public String listGoods(){
		return "listGoods";
	}
	
	public String listGoodsJson(){
		
		this.webpage =this.conutDiscountManager.searchGoods(this.getPage(), this.getPageSize());
		this.showGridJson(this.webpage);
		return JSON_MESSAGE;
	}
	
	public String listBouns(){
		return "listBouns";
	}
	
	public String listBounsJson(){
		
		this.webpage = this.bonusTypeManager.listStoreBouns(this.getPage(),this.getPageSize());
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	
	public IConutDiscountManager getConutDiscountManager() {
		return conutDiscountManager;
	}

	public void setConutDiscountManager(IConutDiscountManager conutDiscountManager) {
		this.conutDiscountManager = conutDiscountManager;
	}

	public IBonusTypeManager getBonusTypeManager() {
		return bonusTypeManager;
	}

	public void setBonusTypeManager(IBonusTypeManager bonusTypeManager) {
		this.bonusTypeManager = bonusTypeManager;
	}

	
	
	


}
