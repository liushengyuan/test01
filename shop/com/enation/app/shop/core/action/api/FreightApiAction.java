package com.enation.app.shop.core.action.api;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.util.JSONUtils;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.BankName;
import com.enation.app.shop.core.model.FreightStandard;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.Product;
import com.enation.app.shop.core.service.IFreightTypeManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IProductManager;
import com.enation.app.shop.core.utils.FreightUtls;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.JsonMessageUtil;

/**
 * 运费计算api
 * @author jfw
 *2015-11-06下午8:17:14
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("freight")
@SuppressWarnings({ "rawtypes", "unchecked", "serial" })
public class FreightApiAction extends WWAction {
	
	private IFreightTypeManager freightTypeManager;
	private double weight;
	
	public String sfprice(){
		//查询顺丰运费标准
		List<FreightStandard> freightStandards = this.freightTypeManager.getFreightStandardOfSF("1");
		//根据标准计算运费
		FreightStandard freightStandard =null;
		if(freightStandards.size()>0){
			freightStandard= freightStandards.get(0);
		}else{
			freightStandard=new FreightStandard();
		}
		Double price = FreightUtls.getFreight(freightStandard, weight);
		//返回到页面上去
		this.json = JsonMessageUtil.getNumberJson("price", price);
		return this.JSON_MESSAGE;
	}

	public IFreightTypeManager getFreightTypeManager() {
		return freightTypeManager;
	}

	public void setFreightTypeManager(IFreightTypeManager freightTypeManager) {
		this.freightTypeManager = freightTypeManager;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
	

	
	
	
}
