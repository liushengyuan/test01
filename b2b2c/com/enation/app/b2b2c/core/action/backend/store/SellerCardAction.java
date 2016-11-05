package com.enation.app.b2b2c.core.action.backend.store;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreLevelManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.tradeease.core.model.account.AccountDetail;
import com.enation.app.tradeease.core.service.accont.IAccountDetailManager;
import com.enation.app.tradeease.core.service.accont.ISellerCardManager;
import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.database.Page;
import com.enation.framework.jms.EmailModel;
import com.enation.framework.jms.EmailProducer;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
		@Result(name = "cart_list", type = "freemarker", location = "/b2b2c/admin/store/card_list.html"),
		@Result(name = "pass", type = "freemarker", location = "/b2b2c/admin/store/pass.html"),
		})
@Action("sellercard")
/**
 * 管理员后台 卖家银行卡信息管理
 * @author 刘继松
 *
 */
public class SellerCardAction extends WWAction {
	private ISellerCardManager sellerCardManager;
	private Integer storeId;
	private String keyMainWord;
	private Integer isEnable;
	private Integer status;
	private Integer id;
	
	/**
	 * 卖家银行卡信息列表
	 * 
	 * @return
	 */
	public String cart_list() {
		return "cart_list";
	}
	/**
	 * 查询卖家银行卡信息
	 * 
	 * @return
	 */
	public String store_listJson() {
		try {
			this.showGridJson(sellerCardManager.list(storeId, keyMainWord,
					isEnable, this.getPage(), this.getPageSize()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	public String editCardStatus(){
		try {
			if(isEnable!=null && status!=null && id!=null){
				if (status == 1) {
					if (isEnable == 1) {
						this.sellerCardManager.updateStatus(0, status, id);
						this.showSuccessJson("银行卡禁用成功！");
					} else {
						this.sellerCardManager.updateStatus(1, status, id);
						this.showSuccessJson("银行卡启用成功");
					}
				} else {
					this.sellerCardManager.updateStatus(isEnable, status, id);
					this.showSuccessJson("审核成功");
				}
			}else{
				this.showErrorJson("传之错误，请重新操作！");
			}

		} catch (Exception e) {
			// TODO: handle exception
			this.showErrorJson("审核操作失败！");
			logger.error("审核操作失败", e);
		}
		return this.JSON_MESSAGE;
	}
	public ISellerCardManager getSellerCardManager() {
		return sellerCardManager;
	}
	public void setSellerCardManager(ISellerCardManager sellerCardManager) {
		this.sellerCardManager = sellerCardManager;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getIsEnable() {
		return isEnable;
	}
	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	public String getKeyMainWord() {
		return keyMainWord;
	}
	public void setKeyMainWord(String keyMainWord) {
		this.keyMainWord = keyMainWord;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
