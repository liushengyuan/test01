package com.enation.app.b2b2c.core.action.api.store;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.store.IStoreSildeManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * 店铺幻灯片API
 * 
 * @author LiFenLong
 *
 */
@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("storeSilde")
public class StoreSildeApiAction extends WWAction {
	private IStoreSildeManager storeSildeManager;
	private String[] silde_url;
	private String[] store_fs;
	private Integer[] silde_id;

	/**
	 * 修改店铺幻灯片设置
	 * 
	 * @param silde_id
	 *            幻灯片Id,Integer[]
	 * @param store_fs
	 *            图片地址,String[]
	 * @param silde_url
	 *            映射地址,String[]
	 * @return 返回json串 result 为1表示调用成功0表示失败
	 */
	public String editStoreSilde() {
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String success=this.getText("storeSlide.changeSuccess");
		String fail=this.getText("storeSlide.changeFail");
		try {
			this.storeSildeManager.edit(silde_id, store_fs, silde_url);
				this.showSuccessJson(success);
		} catch (Exception e) {
				this.showErrorJson(fail);
		}
		return this.JSON_MESSAGE;
	}

	public String initSilde() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String success=this.getText("storeSlide.chushiSuccess");
		String fail=this.getText("storeSlide.chushiFail");
		try {
			this.storeSildeManager.initializeStoreSilde();
				this.showSuccessJson(success);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
				this.showErrorJson(fail);
		}
		return this.JSON_MESSAGE;
	}

	public IStoreSildeManager getStoreSildeManager() {
		return storeSildeManager;
	}

	public void setStoreSildeManager(IStoreSildeManager storeSildeManager) {
		this.storeSildeManager = storeSildeManager;
	}

	public String[] getSilde_url() {
		return silde_url;
	}

	public void setSilde_url(String[] silde_url) {
		this.silde_url = silde_url;
	}

	public String[] getStore_fs() {
		return store_fs;
	}

	public void setStore_fs(String[] store_fs) {
		this.store_fs = store_fs;
	}

	public Integer[] getSilde_id() {
		return silde_id;
	}

	public void setSilde_id(Integer[] silde_id) {
		this.silde_id = silde_id;
	}
}
