package com.enation.app.tradeease.core.action.api.accont;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.map.HashedMap;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.tradeease.core.model.account.SellerCard;
import com.enation.app.tradeease.core.service.accont.ISellerCardManager;
import com.enation.eop.processor.core.UrlNotFoundException;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 卖家中心添加银行卡
 * 
 * @author yanpeng
 * 
 */

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("withdrawAccount")
@SuppressWarnings({ "rawtypes", "unchecked", "serial", "static-access" })
public class WithdrawAccountApiAction extends WWAction {
	private IStoreManager storeManager;
	private IStoreMemberManager storeMemberManager;
	private ISellerCardManager sellerCardManager;

	/**
	 * 新增银行卡账户
	 * 
	 * @return
	 */
	public String addAccount() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		/*HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String manName=this.getText("withdrawAccount.manName");
		String noempty=this.getText("withdrawAccount.noempty");
		String geshi=this.getText("withdrawAccount.geshi");
		String bankName=this.getText("withdrawAccount.bankName");
		String reset=this.getText("withdrawAccount.reset");
		String add=this.getText("withdrawAccount.add");
		String fail=this.getText("withdrawAccount.fail");
		String review=this.getText("withdrawAccount.review");
		String submit=this.getText("withdrawAccount.submit");
		String changesuccess=this.getText("withdrawAccount.changesuccess");
		String changefail=this.getText("withdrawAccount.changefail");
		/*if (language=="zh") {
			manName="持卡人姓名不能为空!";
			noempty="银行卡号不能为空!";
			geshi="银行卡号格式不正确!";
			bankName="银行名称不能为空!";
			reset="银行卡号已存在，请重新填写";
			add="新增银行卡成功";
			fail="新增银行卡失败";
			review="提交成功，审核中";
			submit="提交失败";
			changesuccess="修改银行卡成功";
			changefail="修改银行卡失败";
		}else {
			manName="Имя владельца карты не может быть пустым!";
			noempty="Номер банковской карты не может быть пустым!";
			geshi="Неверный формат номера карты банковской!";
			bankName="Название банка не может быть пустым!";
			reset="Номер банковской карты уже существует, пожалуйста, заполните еще раз";
			add="Добавлять новую банковскую карту успех";
			fail="Добавлять новую банковскую карту не удалось";
			review="Успешное представление, аудит";
			submit="Представление не удалось";
			changesuccess="Изменение банковской карты успех";
			changefail="Не удалось изменить банковской карты";
		}*/
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
				HttpServletResponse response = ThreadContextHolder
						.getHttpResponse();
				try {
					response.sendRedirect("login.html");
				} catch (IOException e) {
					throw new UrlNotFoundException();
				}
			}
			Store store = storeManager.getStoreByMember(storeMember
					.getMember_id());
			String accountName = request.getParameter("accountName");
			String accountNumber = request.getParameter("accountNumber");
			String bank_name = request.getParameter("bank_name");
			if (StringUtil.isEmpty(accountName)) {
				this.showErrorJson(manName);
				return this.JSON_MESSAGE;
			}
			if (StringUtil.isEmpty(accountNumber)) {
				this.showErrorJson(noempty);
				return this.JSON_MESSAGE;
			}
			if (!StringUtil.validBankCard(accountNumber)) {
				this.showErrorJson(geshi);
				return this.JSON_MESSAGE;
			}
			if (StringUtil.isEmpty(bank_name)) {
				this.showErrorJson(bankName);
				return this.JSON_MESSAGE;
			}
			List<SellerCard> cardList = sellerCardManager.queryList();
			for (SellerCard card : cardList) {
				if (accountNumber != null
						&& accountNumber.equals(card.getCard_num())) {
					this.showErrorJson(reset);
					return this.JSON_MESSAGE;
				}
			}
			SellerCard sellerCard = new SellerCard();
			sellerCard.setLogin_name(store.getMember_name());
			sellerCard.setStore_id(store.getStore_id());
			sellerCard.setStore_name(store.getStore_name());
			sellerCard.setCard_holder(accountName);
			sellerCard.setCard_num(accountNumber);
			sellerCard.setBank_name(bank_name);
			sellerCard.setIs_enable(0);
			sellerCard.setStatus(8);
			long check_time = DateUtil.getDateline();
			sellerCard.setCheck_time(check_time);
			this.sellerCardManager.addCard(sellerCard);
			this.showSuccessJson(add);
		} catch (Exception e) {
			this.logger.error("新增银行卡失败！", e);
			this.showErrorJson(fail);
		}
		return this.JSON_MESSAGE;
	}

	/**
	 * 银行卡账户的开启和禁用
	 * 
	 * @return
	 */
	public String editAccount() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		/*HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();	*/
		String review=this.getText("withdrawAccount.review");
		String submit=this.getText("withdrawAccount.submit");
		String changesuccess=this.getText("withdrawAccount.changesuccess");
		String changefail=this.getText("withdrawAccount.changefail");
		/*if (language=="zh") {
			review="提交成功，审核中";
			submit="提交失败";
			changesuccess="修改银行卡成功";
			changefail="修改银行卡失败";
		}else {
			review="Успешное представление, аудит";
			submit="Представление не удалось";
			changesuccess="Изменение банковской карты успех";
			changefail="Не удалось изменить банковской карты";
		}*/
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
				HttpServletResponse response = ThreadContextHolder
						.getHttpResponse();
				try {
					response.sendRedirect("login.html");
				} catch (IOException e) {
					throw new UrlNotFoundException();
				}
			}
			String id = request.getParameter("id");
			SellerCard card = this.sellerCardManager.getAccountById(Integer
					.valueOf(id));
			card.setStatus(0);
			long check_time = DateUtil.getDateline();
			card.setCheck_time(check_time);
			this.sellerCardManager.saveAccount(card);
			this.showSuccessJson(review);
		} catch (Exception e) {
			this.logger.error("提交失败！", e);
			this.showErrorJson(submit);
		}
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 修改银行账户信息
	 * @author WKZ
	 */
	public String editAccountInfo() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		HttpSession session = request.getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String manName="";
		String noempty="";
		String geshi="";
		String bankName="";
		String reset="";
		String add="";
		String fail="";
		String review="";
		String submit="";
		String changesuccess="";
		String changefail="";
		if (language=="zh") {
			manName="持卡人姓名不能为空!";
			noempty="银行卡号不能为空!";
			geshi="银行卡号格式不正确!";
			bankName="银行名称不能为空!";
			reset="银行卡号已存在，请重新填写";
			add="新增银行卡成功";
			fail="新增银行卡失败";
			review="提交成功，审核中";
			submit="提交失败";
			changesuccess="修改银行卡成功";
			changefail="修改银行卡失败,请重试!";
		}else {
			manName="Имя владельца карты не может быть пустым!";
			noempty="Номер банковской карты не может быть пустым!";
			geshi="Неверный формат номера карты банковской!";
			bankName="Название банка не может быть пустым!";
			reset="Номер банковской карты уже существует, пожалуйста, заполните еще раз";
			add="Добавлять новую банковскую карту успех";
			fail="Добавлять новую банковскую карту не удалось";
			review="Успешное представление, аудит";
			submit="Представление не удалось";
			changesuccess="Изменение банковской карты успех";
			changefail="Не удалось изменить банковской карты!";
		}
		try {
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
				HttpServletResponse response = ThreadContextHolder.getHttpResponse();
				try {
					response.sendRedirect("login.html");
				} catch (IOException e) {
					throw new UrlNotFoundException();
				}
			}
			Store store = storeManager.getStoreByMember(storeMember
					.getMember_id());
			String id = request.getParameter("id");
			String accountName = request.getParameter("accountName");
			String accountNumber = request.getParameter("accountNumber");
			String bank_name = request.getParameter("bank_name");
			if (StringUtil.isEmpty(accountName)) {
				this.showErrorJson(manName);
				return this.JSON_MESSAGE;
			}
			if (StringUtil.isEmpty(accountNumber)) {
				this.showErrorJson(noempty);
				return this.JSON_MESSAGE;
			}
			if (!StringUtil.validBankCard(accountNumber)) {
				this.showErrorJson(geshi);
				return this.JSON_MESSAGE;
			}
			if (StringUtil.isEmpty(bank_name)) {
				this.showErrorJson(bankName);
				return this.JSON_MESSAGE;
			}
			List<SellerCard> cardList = sellerCardManager.queryList();
			for (SellerCard card : cardList) {
				if (accountNumber != null
						&& accountNumber.equals(card.getCard_num())) {
					this.showErrorJson(reset);
					return this.JSON_MESSAGE;
				}
			}
			Map modify = new HashedMap();
			modify.put("card_holder", accountName);
			modify.put("card_num", accountNumber);
			modify.put("bank_name", bank_name);
			modify.put("is_enable", 0);
			modify.put("status", 8);
			long check_time = DateUtil.getDateline();
			modify.put("check_time", check_time);
			this.sellerCardManager.modifySingleBankCard(modify,id);
			this.showSuccessJson(changesuccess);
		} catch (Exception e) {
			this.logger.error("修改银行卡失败！", e);
			this.showErrorJson(changefail);
		}
		return this.JSON_MESSAGE;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public ISellerCardManager getSellerCardManager() {
		return sellerCardManager;
	}

	public void setSellerCardManager(ISellerCardManager sellerCardManager) {
		this.sellerCardManager = sellerCardManager;
	}
}
