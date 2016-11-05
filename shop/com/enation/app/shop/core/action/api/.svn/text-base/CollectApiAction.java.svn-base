package com.enation.app.shop.core.action.api;

import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Favorite;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IFavoriteManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;

/**
 * 红酒星空详细页收藏
 * @author lina
 *2013-8-15下午2:44:05
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/shop")
@Action("collect")
public class CollectApiAction extends WWAction {

	private IFavoriteManager favoriteManager;
	private IStoreGoodsManager storeGoodsManager;
	private IStoreMemberManager storeMemberManager;
	private Integer goods_id;
	private Integer favorite_id;
	
	
	
	/**
	 * 收藏一个商品，必须登录才能调用此api
	 * @param goods_id ：要收藏的商品id,int型
	 * @return
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 */
	public String addCollect(){
		Member memberLogin = UserConext.getCurrentMember();
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String add=this.getText("collect.add");
		String noShouCang=this.getText("collect.noShouCang");
		String yiShouCang=this.getText("collect.yiShouCang");
		String denglu=this.getText("collect.denglu");
		if(memberLogin!=null){
			int count = favoriteManager.getCount(this.getGoods_id(),memberLogin.getMember_id());
			StoreGoods goods=storeGoodsManager.getGoods(this.getGoods_id());
			StoreMember storeMember=storeMemberManager.getMember(memberLogin.getMember_id());
			try {
				if (storeMember != null) {
					if(storeMember.getIs_store()==1 && storeMember.getStore_id()!=null){
						if (storeMember.getStore_id().equals(goods.getStore_id())) {
							count = -1;
						}
					}

				} 
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			if (count == 0){
				favoriteManager.add(this.getGoods_id());

				this.showSuccessJson(add);
			}else if(count==-1){
					this.showErrorJson(noShouCang);
			}else{
				this.showErrorJson(yiShouCang);
			}
		}else{
			this.showErrorJson(denglu);
		}
		return this.JSON_MESSAGE;
	}
	
	
	/**
	 * 取消一个商品的收藏，必须登录才能调用此api
	 * @param favorite_id ：收藏id，即Favorite.favorite_id
	 * @return
	 * @return 返回json串
	 * result  为1表示调用成功0表示失败 ，int型
	 * message 为提示信息
	 * 
	 * {@link Favorite}
	 */
	public String cancelCollect(){
		String quxiao=this.getText("collect.quxiao");
		favoriteManager.delete(this.getFavorite_id());
		this.showSuccessJson(quxiao);
		return this.JSON_MESSAGE;
	}
	
	
	
	//以下非api，不用关心

	public Integer getGoods_id() {
		return goods_id;
	}

	public IFavoriteManager getFavoriteManager() {
		return favoriteManager;
	}


	public void setFavoriteManager(IFavoriteManager favoriteManager) {
		this.favoriteManager = favoriteManager;
	}


	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getFavorite_id() {
		return favorite_id;
	}

	public void setFavorite_id(Integer favorite_id) {
		this.favorite_id = favorite_id;
	}


	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}


	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}


	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}


	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	

	
}
