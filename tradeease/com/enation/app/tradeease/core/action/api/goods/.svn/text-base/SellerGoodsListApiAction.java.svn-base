package com.enation.app.tradeease.core.action.api.goods;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
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
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 卖家中心 产品列表
 * 
 * @author yanpeng 2015-6-23 下午6:54:50
 * 
 */

@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("sellerGoodsList")
@SuppressWarnings({ "unchecked", "serial", "static-access" })
public class SellerGoodsListApiAction extends WWAction {

	private IStoreMemberManager storeMemberManager;
	private IGoodsManager goodsManager;
	private IStoreGoodsManager storeGoodsManager;

	/**
	 * 卖家中心 商品列表 批量上下架
	 * 
	 * @param
	 * @return
	 */
	public String updateMarketEnable() {
	/*	HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();*/
		String weidenglu=this.getText("sellerGoodsList.wedenglu");
		String meikaidian=this.getText("sellerGoodsList.meikaidian");
		String meixuanze=this.getText("sellerGoodsList.meixuanze");
		String xiajiaSuccess=this.getText("sellerGoodsList.xiajiaSuccess");
		String shangjiaSuccess=this.getText("sellerGoodsList.shangjiaSuccess");
		String qingxuanzeshangjia=this.getText("sellerGoodsList.qingxuanzeshangjia");
		String qingxuanzexiajia=this.getText("sellerGoodsList.qingxuanzexiajia");
		String apidiaoyong=this.getText("sellerGoodsList.apidiaoyong");
		try {
			int result = 0;
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
					this.showErrorJson(weidenglu);
				return this.JSON_MESSAGE;
			}
			// 判断用户是否已经拥有店铺
			if (storeMember.getIs_store() != 1) {
					this.showErrorJson(meikaidian);
				return this.JSON_MESSAGE;
			}
			String select_goods_ids = request.getParameter("goods_ids");
			String market_enable = request.getParameter("market_enable");
			List<String> goods_ids = StringUtil.stringToList(select_goods_ids,
					",");
			if (goods_ids.size() == 0) {
					this.showErrorJson(meixuanze);
				return this.JSON_MESSAGE;
			}
			for (String goods_id : goods_ids) {
				Goods goods = goodsManager.getGoods(Integer.valueOf(goods_id));
				if (Integer.valueOf(market_enable) == 0
						&& goods.getMarket_enable() == 1) {
					goods.setMarket_enable(Integer.valueOf(market_enable));
					goodsManager.myEditGood(goods);
					result = 1;
				}
				if (Integer.valueOf(market_enable) == 1
						&& goods.getMarket_enable() == 0) {
					goods.setMarket_enable(Integer.valueOf(market_enable));
					goodsManager.myEditGood(goods);
					result = 1;
				}
				this.storeGoodsManager.setGoodsNum(goods.getGoods_id());
			}

			/*
			 * String my_message = "上架"; if (Integer.valueOf(market_enable) ==
			 * 0) { my_message = "下架"; }
			 */
			if (result == 1) {
					if (Integer.valueOf(market_enable) == 0) {
						this.showSuccessJson(xiajiaSuccess);
					} else {
						this.showSuccessJson(shangjiaSuccess);
					}
			} else {
				if (Integer.valueOf(market_enable) == 0) {
						this.showErrorJson(qingxuanzeshangjia);
				} else {
						this.showErrorJson(qingxuanzexiajia);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
				this.showErrorJson(apidiaoyong + e.getMessage());
			this.logger.error("产品列表上下架出错", e);
		}

		return this.JSON_MESSAGE;
	}

	/**
	 * 卖家中心 商品列表 更新有效期
	 * 
	 * @param
	 * @return
	 */
	public String updatePeriod() {
		HttpSession session = ThreadContextHolder.getHttpRequest().getSession();
		Locale locale = (Locale) session.getAttribute("locale");
		String language = locale.getLanguage();
		String weidenglu=this.getText("sellerGoodsList.wedenglu");
		String meikaidian=this.getText("sellerGoodsList.meikaidian");
		String meixuanze=this.getText("sellerGoodsList.meixuanze");
		String xiajiaSuccess=this.getText("sellerGoodsList.xiajiaSuccess");
		String shangjiaSuccess=this.getText("sellerGoodsList.shangjiaSuccess");
		String qingxuanzeshangjia=this.getText("sellerGoodsList.qingxuanzeshangjia");
		String qingxuanzexiajia=this.getText("sellerGoodsList.qingxuanzexiajia");
		String apidiaoyong=this.getText("sellerGoodsList.apidiaoyong");
		String gengxin=this.getText("sellerGoodsList.gengxin");
		try {
			HttpServletRequest request = ThreadContextHolder.getHttpRequest();
			StoreMember storeMember = storeMemberManager.getStoreMember();
			if (storeMember == null) {
					this.showErrorJson(weidenglu);
				return this.JSON_MESSAGE;
			}
			// 判断用户是否已经拥有店铺
			if (storeMember.getIs_store() != 1) {
					this.showErrorJson(meikaidian);
				return this.JSON_MESSAGE;
			}
			String select_goods_ids = request.getParameter("goods_ids");
			List<String> goods_ids = StringUtil.stringToList(select_goods_ids,
					",");
			if (goods_ids.size() == 0) {
					this.showErrorJson(meixuanze);
				return this.JSON_MESSAGE;
			}
			for (String goods_id : goods_ids) {

				StoreGoods goods = storeGoodsManager.getGoods(Integer
						.valueOf(goods_id));
				long last_modify = DateUtil.getDateline();
				goods.setLast_modify(last_modify);
				goods.setExpire_time(DateUtil.getDateline(this
						.getDateAfterForGoods(goods.getExpire_time())));
				goodsManager.myEditGood(goods);
			}
				this.showSuccessJson(gengxin);
		} catch (Exception e) {
			e.printStackTrace();
				this.showErrorJson(apidiaoyong + e.getMessage());
			this.logger.error("更新有效期出错", e);
		}

		return this.JSON_MESSAGE;
	}

	// 获取当前时间30天后的时间
	public static String getDateAfterForGoods(Long time) {
		Long time1=DateUtil.getDateline();
		Calendar now = Calendar.getInstance();
		if(time1<=time){
			if(time.toString().length()==10){
				time = time*1000;
				Date date = new Date(time);
				now.setTime(date);
			}
		}else{
			now.setTime(new Date());
		}
		now.add(Calendar.DATE,30);
		// 获取当前时间
		//now.set(Calendar.DATE, now.get(Calendar.DATE) + days);
		String dateString = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateString = sdf.format(now.getTime());
		} catch (Exception ex) {
				ex.printStackTrace();
		}
		//return toString(now.getTime() , "yyyy-MM-dd");
		return dateString;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public IGoodsManager getGoodsManager() {
		return goodsManager;
	}

	public void setGoodsManager(IGoodsManager goodsManager) {
		this.goodsManager = goodsManager;
	}

	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}

	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}

}
