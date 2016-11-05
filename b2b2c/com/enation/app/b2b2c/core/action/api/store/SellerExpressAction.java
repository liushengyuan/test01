package com.enation.app.b2b2c.core.action.api.store;


import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.SellerExpress;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.ISellerExpressManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.StringUtil;

/**
 * 卖家自定义配送商
 * @author WKZ
 * @date 2015-9-17 下午7:11:04
 */
@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("sellerExpress")
public class SellerExpressAction extends WWAction {
	
	@Autowired
	private ISellerExpressManager sellerExpressManager;
	private IStoreMemberManager storeMemberManager;
	private SellerExpress express;
	private Integer expressId;
	private String old_express_name;
	
	public String add() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		
		String expressNameInfo=this.getText("sellerExpress.expressNameInfo");
		String timeInfo=this.getText("sellerExpress.timeInfo");
		String successInfo=this.getText("sellerExpress.successInfo");
		String failureInfo=this.getText("sellerExpress.failureInfo");
		/*if(language == "ru") {
			expressNameInfo = "Наименование компанией доставки не может быть пустым";
			timeInfo = "Предварительное время  не может быть пустым";
			successInfo = "Добавить успешно";
			failureInfo = "Добавить неуспех";
		} else if(language == "zh") {
			expressNameInfo = "配送商名称不能为空！";
			timeInfo = "预计时间不能为空！";
			successInfo = "添加成功";
			failureInfo = "添加失败";
		}*/
		try {
			if(StringUtil.isEmpty(express.getExpress_name())) {
				throw new RuntimeException(expressNameInfo);
			}
			
			if(StringUtil.isEmpty(express.getExpress_deliver_time())) {
				throw new RuntimeException(timeInfo);
			}
			StoreMember storeMember = storeMemberManager.getStoreMember();
			int storeid = storeMember.getStore_id();
			Integer check = sellerExpressManager.check(express.getExpress_name(),storeid);
			if(check>0){
				this.showErrorJson("配送商名称重复");
				return this.JSON_MESSAGE;
			}
			express.setExpress_storeid(String.valueOf(storeid));
			sellerExpressManager.add(express);
			this.showSuccessJson(successInfo);
		} catch (Exception e) {
			if(e instanceof RuntimeException) {
				this.showErrorJson(e.getMessage());
			} else {
				this.showErrorJson(failureInfo);
			}
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	
	public String edit() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		
		String expressNameInfo=this.getText("sellerExpress.expressNameInfo");
		String timeInfo=this.getText("sellerExpress.timeInfo");
		String successInfo=this.getText("sellerExpress.changeSuccessInfo");
		String failureInfo=this.getText("sellerExpress.changefailureInfo");
		/*if(language == "ru") {
			expressNameInfo = "Наименование компанией доставки не может быть пустым";
			timeInfo = "Предварительное время  не может быть пустым";
			successInfo = "Исправить успешно";
			failureInfo = "Исправить неуспех";
		} else if(language == "zh") {
			expressNameInfo = "配送商名称不能为空！";
			timeInfo = "预计时间不能为空！";
			successInfo = "修改成功";
			failureInfo = "修改失败";
		}*/
		try {
			if(StringUtil.isEmpty(express.getExpress_name())) {
				throw new RuntimeException(expressNameInfo);
			}
			
			if(StringUtil.isEmpty(express.getExpress_deliver_time())) {
				throw new RuntimeException(timeInfo);
			}
			////System.out.println(old_express_name.equals(express.getExpress_name()));
			if(!old_express_name.equals(express.getExpress_name())){
				StoreMember storeMember = storeMemberManager.getStoreMember();
				int storeid = storeMember.getStore_id();
				Integer check = sellerExpressManager.check(express.getExpress_name(),storeid);
				if(check>0){
					this.showErrorJson("配送商名称重复");
					return this.JSON_MESSAGE;
				}				
			}
			sellerExpressManager.update(express);
			this.showSuccessJson(successInfo);
		} catch (Exception e) {
			if(e instanceof RuntimeException) {
				this.showErrorJson(e.getMessage());
			} else {
				this.showErrorJson(failureInfo);
			}
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	
	public String delete() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		
		String successInfo=this.getText("sellerExpress.deletesuccessInfo");
		String failureInfo=this.getText("sellerExpress.deletefailureInfo");
		/*if(language == "ru") {
			successInfo = "Исключить успешно";
			failureInfo = "Исключить неуспех";
		} else if(language == "zh") {
			successInfo = "删除成功";
			failureInfo = "删除失败";
		}*/
		try {
			sellerExpressManager.delete(expressId);
			this.showSuccessJson(successInfo);
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson(failureInfo);
		}
		return this.JSON_MESSAGE;
	}
	
	public SellerExpress getExpress() {
		return express;
	}

	public void setExpress(SellerExpress express) {
		this.express = express;
	}

	public Integer getExpressId() {
		return expressId;
	}

	public void setExpressId(Integer expressId) {
		this.expressId = expressId;
	}

	public ISellerExpressManager getSellerExpressManager() {
		return sellerExpressManager;
	}

	public void setSellerExpressManager(ISellerExpressManager sellerExpressManager) {
		this.sellerExpressManager = sellerExpressManager;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public String getOld_express_name() {
		return old_express_name;
	}

	public void setOld_express_name(String old_express_name) {
		this.old_express_name = old_express_name;
	}
	
}
