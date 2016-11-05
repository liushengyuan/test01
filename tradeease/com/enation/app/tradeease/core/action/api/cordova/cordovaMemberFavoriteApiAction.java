package com.enation.app.tradeease.core.action.api.cordova;

import java.util.List;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IFavoriteManager;
import com.enation.app.tradeease.core.service.cordova.IFavoriteManagerApp;
import com.enation.app.tradeease.core.service.cordova.IStoreManagerApp;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/cordova")
@Action("cordovaMemberFavorite")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class cordovaMemberFavoriteApiAction extends WWAction {
	private IFavoriteManagerApp favoriteManagerApp;
	private Integer favorite_id;
	private Integer id;
	private Integer goods_id;
	private Integer storeCollectId;
	private Integer store_id;
	private String goods_ids;
	private String store_ids;
	private IStoreManagerApp StoreManagerApp;
	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}

	public Integer getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(Integer goods_id) {
		this.goods_id = goods_id;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFavorite_id() {
		return favorite_id;
	}

	public void setFavorite_id(Integer favorite_id) {
		this.favorite_id = favorite_id;
	}

	

	public Integer getStoreCollectId() {
		return storeCollectId;
	}

	public void setStoreCollectId(Integer storeCollectId) {
		this.storeCollectId = storeCollectId;
	}

	public String selectMyFavorite(){
	List<Goods> myfavorite =this.getFavoriteManagerApp().list1();
		
	for(Goods a:myfavorite){
		if(a.getSmall()!=null){
			a.setSmall(UploadUtil.replacePath(a.getSmall()));
		}
	}
	showGridJson(myfavorite);
	return this.JSON_MESSAGE;
		
	}
	//获取收藏店铺列表
	public String selectStoreCollect(){
		List<Map> storecollect=this.getFavoriteManagerApp().liststore();
		for(Map a:storecollect){
			if(a.get("store_logo")!=null){
				a.put("store_logo", UploadUtil.replacePath(a.get("store_logo").toString()));
			}
		}
		showGridJson(storecollect);
		return this.JSON_MESSAGE;
	}
	//删除店铺收藏
	public String deleteStore(){
		this.favoriteManagerApp.deleteByStoreId(storeCollectId);
		showSuccessJson("删除成功");
		return this.JSON_MESSAGE;
	}
	//删除多个店铺收藏
	public String deleteFavoriteStroes(){
		
		try {
			String[] storeIdArray = store_ids.split(",");
			for(String storeIdStr:storeIdArray){
				this.favoriteManagerApp.deleteByStoreId(Integer.parseInt(storeIdStr));
			}
			showSuccessJson("删除成功");
		} catch (NumberFormatException e) {
			showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 判断店铺是否被收藏
	 * */
	public String getCountStore(){
		int count = this.favoriteManagerApp.getStoreCount(store_id);
		if(count>=1){
			this.showSuccessJson("已收藏");
		}else{
			this.showErrorJson("未收藏");
		}
		return this.JSON_MESSAGE;
		
	}
	//添加店铺收藏
	public String addStore(){
		int count=this.favoriteManagerApp.getStoreCount(store_id);
		if(count>=1){
			showSuccessJson("该店铺已收藏");
		}else{
			getFavoriteManagerApp().addstore(store_id);
			showSuccessJson("收藏成功");
		}
		return this.JSON_MESSAGE;
	}
	//删除商品收藏
	public String deleteFavoriteByGoods(){
		Member member = UserConext.getCurrentMember();
		this.favoriteManagerApp.deleteByGoodsIdAndMember(goods_id, member.getMember_id());
		showSuccessJson("删除成功");
		return this.JSON_MESSAGE;
	}
	//删除多个商品收藏
	public String deleteFavorites(){
		
		try {
			Member member = UserConext.getCurrentMember();
			String[] goodsIdArray = goods_ids.split(",");
			for(String goodsIdStr:goodsIdArray){
				this.favoriteManagerApp.deleteByGoodsIdAndMember(Integer.parseInt(goodsIdStr), member.getMember_id());
			}
			showSuccessJson("删除成功");
		} catch (NumberFormatException e) {
			showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	//添加收藏
	public String attention(){
			Member member = UserConext.getCurrentMember();
			int count = this.favoriteManagerApp.getCount(goods_id,member.getMember_id());
			if(count>=1){
				showErrorJson("该商品已收藏");
			}else{
				getFavoriteManagerApp().add(goods_id);
				showSuccessJson("收藏成功");
			}
			return this.JSON_MESSAGE;
		}
	//添加历史纪录
	public String addhistory(){
		Member member = UserConext.getCurrentMember();
		int num = this.favoriteManagerApp.getNum(goods_id,member.getMember_id());
		if(num>=1){
			this.deleteHistory();
			getFavoriteManagerApp().addHistory(goods_id);
		}else{
			getFavoriteManagerApp().addHistory(goods_id);
			showSuccessJson("添加历史纪录成功");
		}
		return this.JSON_MESSAGE;
	}
	//删除历史纪录
	public String deleteHistory(){
		Member member = UserConext.getCurrentMember();
		this.favoriteManagerApp.deleteByHistory(goods_id, member.getMember_id());
		showSuccessJson("删除成功");
		return this.JSON_MESSAGE;
	}
	/**
	 * 删除历史记录多个
	 * @param goods_ids 多个goods_id逗号分隔
	 * @return
	 */
	public String deleteHistorys(){
		try {
			Member member = UserConext.getCurrentMember();
			String[] goodsIdArray = goods_ids.split(",");
			for(String goodsIdStr:goodsIdArray){
				this.favoriteManagerApp.deleteByHistory(Integer.parseInt(goodsIdStr), member.getMember_id());
			}
			this.showSuccessJson("删除成功");
		} catch (NumberFormatException e) {
			this.showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	//查看历史纪录
	public String selectHistory(){
		List<Goods> myhistory =this.getFavoriteManagerApp().list7();
			
		for(Goods a:myhistory){
			if(a.getSmall()!=null){
				a.setSmall(UploadUtil.replacePath(a.getSmall()));
			}
		}
		showGridJson(myhistory);
		return this.JSON_MESSAGE;
			
		}
	//获取商店详情
	public String getstore(){
		 
		 List<Store> store=this.StoreManagerApp.getStore(store_id);
		 this.showGridJson(store);
		 return WWAction.JSON_MESSAGE;
	}
	/**
	 * 删除店铺收藏
	 * @param store_id
	 * @return
	 */
	public String deleteFavoriteStore(){
		try {
			Member member = UserConext.getCurrentMember();
			if(member==null){
				this.showErrorJson("请先登录");
				return this.JSON_MESSAGE;
			}
			if(store_id==null){
				this.showErrorJson("店铺信息错误");
				return this.JSON_MESSAGE;
			}
			this.favoriteManagerApp.deleteFavoriteByStoreId(store_id, member.getMember_id());
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson(e.getMessage());
			e.printStackTrace();
		}
		return this.JSON_MESSAGE;
	}
	public IFavoriteManagerApp getFavoriteManagerApp() {
		return favoriteManagerApp;
	}

	public void setFavoriteManagerApp(IFavoriteManagerApp favoriteManagerApp) {
		this.favoriteManagerApp = favoriteManagerApp;
	}

	public String getGoods_ids() {
		return goods_ids;
	}

	public void setGoods_ids(String goods_ids) {
		this.goods_ids = goods_ids;
	}

	public String getStore_ids() {
		return store_ids;
	}

	public void setStore_ids(String store_ids) {
		this.store_ids = store_ids;
	}

	public IStoreManagerApp getStoreManagerApp() {
		return StoreManagerApp;
	}

	public void setStoreManagerApp(IStoreManagerApp storeManagerApp) {
		StoreManagerApp = storeManagerApp;
	}
	
}
