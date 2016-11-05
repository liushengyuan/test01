package com.enation.app.b2b2c.core.action.api.member;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.MemberCollect;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.IStoreCollectManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * 收藏店铺	Action
 * @author xulipeng
 *
 */
@Component
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("storeCollect")
public class StoreCollectApiAction extends WWAction {
	private IStoreCollectManager storeCollectManager;
	private IStoreMemberManager storeMemberManager;
	private IStoreManager storeManager;
	private Integer store_id;
	private Integer celloct_id;
	/**
	 * 添加收藏店铺
	 * @param member 店铺会员,StoreMember
	 * @param store_id 店铺Id,Integer
	 * @param collect	收藏店铺,MemberCollect
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String addCollect(){
		//弹出框，国际化
		/*HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		*/
		String CannotCollect = this.getText("storeCollect.CannotCollect");
		String CollectionSuccess = this.getText("storeCollect.CollectionSuccess");
		String PleaseLogin = this.getText("storeCollect.PleaseLogin");
/*//		if(language=="zh"){
//			CannotCollect = "不能收藏自己的店铺!";
//			CollectionSuccess = "收藏成功!";
//			PleaseLogin = "请登录!收藏失败!";
//		}else{
//			CannotCollect = "не  собирать собственные магазины!";
//			CollectionSuccess = " коллекции  успех!";
//			PleaseLogin = " Пожалуйста, войдите! Коллекция  не! ";
//		}
*/		
		StoreMember member=storeMemberManager.getStoreMember();
		if(member!=null){
			if(member.getStore_id()!=null && member.getStore_id().equals(store_id)){
				this.showErrorJson(CannotCollect);
				return JSON_MESSAGE;
			}
			MemberCollect collect = new MemberCollect();
			collect.setMember_id(member.getMember_id());
			collect.setStore_id(store_id);
			try {
				this.storeCollectManager.addCollect(collect);
				this.storeManager.addcollectNum(store_id);
				this.showSuccessJson(CollectionSuccess);
				
			} catch (RuntimeException e) {
				this.showErrorJson(e.getMessage());
			}
		}else{
			this.showErrorJson(PleaseLogin);
		}
		return JSON_MESSAGE;
	}
	/**
	 * 删除收藏店铺
	 * @param celloct_id 收藏店铺Id,Integer
	 * @param store_id 店铺Id,Integer
	 * @return 返回json串
	 * result 	为1表示调用成功0表示失败
	 */
	public String del(){
		String success=this.getText("storeCollect.DeleteSuccess");
		String fail=this.getText("storeCollect.DeleteFail");
		try {
			this.storeCollectManager.delCollect(celloct_id);
			this.storeManager.reduceCollectNum(store_id);
			this.showSuccessJson(success);
		} catch (Exception e) {
			this.showErrorJson(fail);
		}
		return JSON_MESSAGE;
	}

	//set get
	public IStoreCollectManager getStoreCollectManager() {
		return storeCollectManager;
	}

	public void setStoreCollectManager(IStoreCollectManager storeCollectManager) {
		this.storeCollectManager = storeCollectManager;
	}


	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public Integer getCelloct_id() {
		return celloct_id;
	}

	public void setCelloct_id(Integer celloct_id) {
		this.celloct_id = celloct_id;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
	
}
