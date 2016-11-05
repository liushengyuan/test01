package com.enation.app.b2b2c.core.action.api.goods;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
import com.opensymphony.util.DataUtil;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;

import com.enation.app.b2b2c.core.model.Country;
import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.ICountryManager;
import com.enation.app.b2b2c.core.service.ILogiCountryManager;
import com.enation.app.b2b2c.core.service.IStoreTemplateManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.b2b2c.core.test.GoodsGgalleryTest;
import com.enation.app.shop.core.model.Logi;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.JsonMessageUtil;

/**
 * 
 * @author WKZ
 * @date 2015-9-19 上午9:53:16
 */
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("goodsLogi")
@Results({
	@Result(name="express_chose_content", type="freemarker", location="/themes/b2b2cv2/express_chose_content.html")
})
public class GoodsLogiApiAction extends WWAction{
	
	@Autowired
	private ILogiCountryManager logiCountryManager;
	
	@Autowired
	private ICountryManager countryManager;
	
	private List<Country> countryList;
	private List<Logi> expressList;
	private String code;
	
	public String getLogi() {
		if(StringUtil.isEmpty(code)) code = "CHN";
		countryList = countryManager.getList();
		expressList = logiCountryManager.getLogiByCountrycode(code);
		if(expressList == null) {
			expressList = new ArrayList<Logi>();
		} else {
			for(Logi i : expressList) {
				if("CHN".equals(code))  i.setCost("￥ 10");
				if("RUS".equals(code))  i.setCost("10 p.");
				i.setTime("3--5");
			}
		}
		return "express_chose_content";
	}
	

	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}

	public List<Logi> getExpressList() {
		return expressList;
	}

	public void setExpressList(List<Logi> expressList) {
		this.expressList = expressList;
	}


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}
	
	
}
